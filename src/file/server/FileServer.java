package file.server;
import java.util.Scanner;
import javax.swing.JOptionPane;
import file.vo.Disk;
import user.vo.User;
import file.vo.Dir;
import file.vo.Inode;
import file.vo.MyFile;
import file.util.TimeHelper;

public class FileServer {
	public int uid=111,gid=111;
	DiskServer ds;
	public FileServer(DiskServer ds){
		this.ds=ds;
	}
	//创建文件
	public int mkfile(int ind,String filename,String content,String curname,User user){
		Inode in=ds.readinode(ind);
		if(Disk.BSIZE-in.fsize<36){
			JOptionPane.showMessageDialog(null, "当前目录下子文件已经达到上限！");
			return 0;
		}else if(hassamefile(filename,in)){
			JOptionPane.showMessageDialog(null, "该文件已经存在！");
			return 0;
		}else{
			//为该文件申请索引节点
			int iid=ds.allocinode();
			if(iid==-1){
				JOptionPane.showMessageDialog(null, "索引节点不足！");
				return 0;
			}
			//为该节点申请块,注意分要不要使用一级间址
			int len=content.length();
			int bnum=(len-1)/Disk.BSIZE+1;
			int bid[]=new int[180];
			int now,bbid=0;
			boolean suc=true;
			for(now=0;now<bnum&&now<4&&suc;now++){
				bid[now]=ds.allocblock();
				if(bid[now]==-1)suc=false;
			}
			//大于四块时，使用一级间址
			if(suc&&bnum>4){
				bbid=ds.allocblock();
				if(bbid==-1){
					ds.freeblock(bbid);
					suc=false;
				}else{
					for(;now<bnum&&suc;now++){
						bid[now]=ds.allocblock();
						if(bid[now]==-1)suc=false;
					}
				}
			}
			//分配空间不足,释放已分配空间
			if(!suc){
				for(int i=0;i<now;i++){
					ds.freeblock(bid[i]);
				}
				JOptionPane.showMessageDialog(null, "空间不足！");
				return 0;
			}
			//更改当前目录信息
			Dir dir=new Dir();
			dir.fname=filename;
			dir.findex=iid;
			dir.pname=curname;
			dir.pindex=ind;
			ds.writedir(in, dir, in.fsize/Disk.DSIZE);
			in.fsize+=Disk.DSIZE;
			in.ftime=new TimeHelper().gettime();
			ds.writeinode(in, ind);
			//分配空间成功,创建inode信息
			Inode inode=new Inode();
			inode.fsize=len;
			inode.fblock=bnum;
			inode.faddr[0]=bid[0];
			inode.faddr[1]=bid[1];
			inode.faddr[2]=bid[2];
			inode.faddr[3]=bid[3];
			inode.fadd1=bbid;
			inode.fownid=user.getUserId();
			inode.fgrpid=gid;
			inode.fmode=2777;
			inode.ftime=new TimeHelper().gettime();
			ds.writeinode(inode, iid);;
			//将数据写入对应的块中
			for(int i=0;i<bnum;i++){
				int st=i*Disk.BSIZE;
				int en=i*Disk.BSIZE+Disk.BSIZE;
				if(en>len)en=len;
				ds.writeblock(bid[i], content.substring(st, en));
			}
			//将数据写入一级间址中
			if(bbid!=0){
				String s="";
				for(int i=4;i<bnum;i++){
					s+=bid[i]+" ";
				}
				ds.writeblock(bbid, s);
			}
			return 1;
		}
	}
	//删除文件,ind为父目录的索引
	public void rmfile(int ind,String filename){
		Inode in=ds.readinode(ind);
		boolean find=false;
		for(int i=0;i<in.fsize/36;i++){
			Dir dir=ds.readdir(in, i);
			//找到对应的目文件选项
			if(dir.fname.equals(filename)){
				find=true;
				Inode iin=ds.readinode(dir.findex);
				//删除直接索引块中的内容
				for(int j=0;j<4;j++){
					if(iin.faddr[j]!=0){
						ds.freeblock(iin.faddr[j]);
					}
				}
				//删除一级间块中的内容
				if(iin.fadd1!=0){
					String str=ds.readblock(iin.fadd1, Disk.BSIZE);
					@SuppressWarnings("resource")
					Scanner sc=new Scanner(str);
					while(sc.hasNextInt()){
						int x=sc.nextInt();
						ds.freeblock(x);
					}
					ds.freeblock(iin.fadd1);
				}
				//释放索引节点
				ds.freeinode(dir.findex);
			//已经删除,将后面的内容前移
			}else if(find){
				ds.writedir(in, ds.readdir(in, i),i-1);
			}
		}
		ds.cleardir(in, in.fsize/36-1);
		in.fsize-=Disk.DSIZE;
		in.ftime=new TimeHelper().gettime();
		ds.writeinode(in, ind);
	}
	//读取文件内容,传入父节点内容和文件信名
	public MyFile readFile(int ind,String filename){
		MyFile mf=new MyFile();
		mf.fname=filename;
		Inode in=ds.readinode(ind);
		for(int i=0;i<in.fsize/Disk.DSIZE;i++){
			Dir dir=ds.readdir(in, i);
			//找到对应的目文件选项
			if(dir.fname.equals(filename)){
				Inode iin=ds.readinode(dir.findex);
				mf.fsize=iin.fsize;
				mf.ftime=iin.ftime;
				mf.fmode=iin.fmode;
				mf.fownid=iin.fownid;
				mf.fgrpid=iin.fgrpid;
				mf.fcont="";
				//读取直接快中的内容
				int nowsize=mf.fsize;
				for(int j=0;j<4;j++){
					if(iin.faddr[j]!=0){
						mf.fcont+=ds.readblock(iin.faddr[j], nowsize>=Disk.BSIZE?Disk.BSIZE:nowsize);
						nowsize-=Disk.BSIZE;
					}
				}
				//读取一级间接块中的内容
				if(iin.fadd1!=0){
					String str=ds.readblock(iin.fadd1, Disk.BSIZE);
					@SuppressWarnings("resource")
					Scanner sc=new Scanner(str);
					while(sc.hasNextInt()){
						int x=sc.nextInt();
						mf.fcont+=ds.readblock(x, nowsize>=Disk.BSIZE?Disk.BSIZE:nowsize);
						nowsize-=Disk.BSIZE;
					}
				}
				
			}
		}
		return mf;
	}
	//修改文件内容
	public int updateFile(int pid,int id,String name,String cont){
		Inode in=ds.readinode(pid);
		//判断是否有重名
		for(int i=0;i<in.fsize/36;i++){
			Dir dir=ds.readdir(in,i);
			if(dir.findex!=id&&dir.fname.equals(name)){
				JOptionPane.showMessageDialog(null, "有重名文件！");
				return 0;
			}
		}
		for(int i=0;i<in.fsize/36;i++){
			Dir dir=ds.readdir(in, i);
			//找到对应的目文件选项
			if(dir.findex==id){
				//修改目录项
				dir.fname=name;
				ds.writedir(in, dir, i);
				Inode iin=ds.readinode(dir.findex);
				//删除现在占用的块
				for(int j=0;j<4;j++){
					if(iin.faddr[j]!=0){
						ds.freeblock(iin.faddr[j]);
					}
				}
				if(iin.fadd1!=0){
					String str=ds.readblock(iin.fadd1, Disk.BSIZE);
					@SuppressWarnings("resource")
					Scanner sc=new Scanner(str);
					while(sc.hasNextInt()){
						int x=sc.nextInt();
						ds.freeblock(x);
					}
					ds.freeblock(iin.fadd1);
				}
				//为该节点申请块
				int len=cont.length();
				int bnum=(len-1)/Disk.BSIZE+1;
				int bid[]=new int[180];
				int now,bbid=0;
				boolean suc=true;
				for(now=0;now<bnum&&now<4&&suc;now++){
					bid[now]=ds.allocblock();
					if(bid[now]==-1)suc=false;
				}
				if(suc&&bnum>4){
					bbid=ds.allocblock();
					if(bbid==-1){
						ds.freeblock(bbid);
						suc=false;
					}else{
						for(;now<bnum&&suc;now++){
							bid[now]=ds.allocblock();
							if(bid[now]==-1)suc=false;
						}
					}
				}
				if(!suc){
					for(int j=0;j<now;j++){
						ds.freeblock(bid[j]);
					}
					JOptionPane.showMessageDialog(null, "空间不足！");
					return 0;
				}
				//将内容写入块中
				Inode inode=ds.readinode(id);
				inode.fsize=len;
				inode.fblock=bnum;
				inode.faddr[0]=bid[0];
				inode.faddr[1]=bid[1];
				inode.faddr[2]=bid[2];
				inode.faddr[3]=bid[3];
				inode.fadd1=bbid;
				inode.ftime=new TimeHelper().gettime();
				ds.writeinode(inode, id);;
				//将数据写入对应的块中
				for(int j=0;j<bnum;j++){
					int st=j*Disk.BSIZE;
					int en=j*Disk.BSIZE+Disk.BSIZE;
					if(en>len)en=len;
					ds.writeblock(bid[j], cont.substring(st, en));
				}
				//将数据写入一级间址中
				if(bbid!=0){
					String s="";
					for(int j=4;j<bnum;j++){
						s+=bid[j]+" ";
					}
					ds.writeblock(bbid, s);
				}
				return 1;
			}
		}	
		return 0;
	}
	public boolean hassamefile(String filename,Inode in){
		for(int i=0;i<in.fsize/36;i++){
			Dir dir=ds.readdir(in, i);
			if(dir.fname.equals(filename))return true;
		}
		return false;
	}
}