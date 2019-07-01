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
	//�����ļ�
	public int mkfile(int ind,String filename,String content,String curname,User user){
		Inode in=ds.readinode(ind);
		if(Disk.BSIZE-in.fsize<36){
			JOptionPane.showMessageDialog(null, "��ǰĿ¼�����ļ��Ѿ��ﵽ���ޣ�");
			return 0;
		}else if(hassamefile(filename,in)){
			JOptionPane.showMessageDialog(null, "���ļ��Ѿ����ڣ�");
			return 0;
		}else{
			//Ϊ���ļ����������ڵ�
			int iid=ds.allocinode();
			if(iid==-1){
				JOptionPane.showMessageDialog(null, "�����ڵ㲻�㣡");
				return 0;
			}
			//Ϊ�ýڵ������,ע���Ҫ��Ҫʹ��һ����ַ
			int len=content.length();
			int bnum=(len-1)/Disk.BSIZE+1;
			int bid[]=new int[180];
			int now,bbid=0;
			boolean suc=true;
			for(now=0;now<bnum&&now<4&&suc;now++){
				bid[now]=ds.allocblock();
				if(bid[now]==-1)suc=false;
			}
			//�����Ŀ�ʱ��ʹ��һ����ַ
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
			//����ռ䲻��,�ͷ��ѷ���ռ�
			if(!suc){
				for(int i=0;i<now;i++){
					ds.freeblock(bid[i]);
				}
				JOptionPane.showMessageDialog(null, "�ռ䲻�㣡");
				return 0;
			}
			//���ĵ�ǰĿ¼��Ϣ
			Dir dir=new Dir();
			dir.fname=filename;
			dir.findex=iid;
			dir.pname=curname;
			dir.pindex=ind;
			ds.writedir(in, dir, in.fsize/Disk.DSIZE);
			in.fsize+=Disk.DSIZE;
			in.ftime=new TimeHelper().gettime();
			ds.writeinode(in, ind);
			//����ռ�ɹ�,����inode��Ϣ
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
			//������д���Ӧ�Ŀ���
			for(int i=0;i<bnum;i++){
				int st=i*Disk.BSIZE;
				int en=i*Disk.BSIZE+Disk.BSIZE;
				if(en>len)en=len;
				ds.writeblock(bid[i], content.substring(st, en));
			}
			//������д��һ����ַ��
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
	//ɾ���ļ�,indΪ��Ŀ¼������
	public void rmfile(int ind,String filename){
		Inode in=ds.readinode(ind);
		boolean find=false;
		for(int i=0;i<in.fsize/36;i++){
			Dir dir=ds.readdir(in, i);
			//�ҵ���Ӧ��Ŀ�ļ�ѡ��
			if(dir.fname.equals(filename)){
				find=true;
				Inode iin=ds.readinode(dir.findex);
				//ɾ��ֱ���������е�����
				for(int j=0;j<4;j++){
					if(iin.faddr[j]!=0){
						ds.freeblock(iin.faddr[j]);
					}
				}
				//ɾ��һ������е�����
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
				//�ͷ������ڵ�
				ds.freeinode(dir.findex);
			//�Ѿ�ɾ��,�����������ǰ��
			}else if(find){
				ds.writedir(in, ds.readdir(in, i),i-1);
			}
		}
		ds.cleardir(in, in.fsize/36-1);
		in.fsize-=Disk.DSIZE;
		in.ftime=new TimeHelper().gettime();
		ds.writeinode(in, ind);
	}
	//��ȡ�ļ�����,���븸�ڵ����ݺ��ļ�����
	public MyFile readFile(int ind,String filename){
		MyFile mf=new MyFile();
		mf.fname=filename;
		Inode in=ds.readinode(ind);
		for(int i=0;i<in.fsize/Disk.DSIZE;i++){
			Dir dir=ds.readdir(in, i);
			//�ҵ���Ӧ��Ŀ�ļ�ѡ��
			if(dir.fname.equals(filename)){
				Inode iin=ds.readinode(dir.findex);
				mf.fsize=iin.fsize;
				mf.ftime=iin.ftime;
				mf.fmode=iin.fmode;
				mf.fownid=iin.fownid;
				mf.fgrpid=iin.fgrpid;
				mf.fcont="";
				//��ȡֱ�ӿ��е�����
				int nowsize=mf.fsize;
				for(int j=0;j<4;j++){
					if(iin.faddr[j]!=0){
						mf.fcont+=ds.readblock(iin.faddr[j], nowsize>=Disk.BSIZE?Disk.BSIZE:nowsize);
						nowsize-=Disk.BSIZE;
					}
				}
				//��ȡһ����ӿ��е�����
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
	//�޸��ļ�����
	public int updateFile(int pid,int id,String name,String cont){
		Inode in=ds.readinode(pid);
		//�ж��Ƿ�������
		for(int i=0;i<in.fsize/36;i++){
			Dir dir=ds.readdir(in,i);
			if(dir.findex!=id&&dir.fname.equals(name)){
				JOptionPane.showMessageDialog(null, "�������ļ���");
				return 0;
			}
		}
		for(int i=0;i<in.fsize/36;i++){
			Dir dir=ds.readdir(in, i);
			//�ҵ���Ӧ��Ŀ�ļ�ѡ��
			if(dir.findex==id){
				//�޸�Ŀ¼��
				dir.fname=name;
				ds.writedir(in, dir, i);
				Inode iin=ds.readinode(dir.findex);
				//ɾ������ռ�õĿ�
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
				//Ϊ�ýڵ������
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
					JOptionPane.showMessageDialog(null, "�ռ䲻�㣡");
					return 0;
				}
				//������д�����
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
				//������д���Ӧ�Ŀ���
				for(int j=0;j<bnum;j++){
					int st=j*Disk.BSIZE;
					int en=j*Disk.BSIZE+Disk.BSIZE;
					if(en>len)en=len;
					ds.writeblock(bid[j], cont.substring(st, en));
				}
				//������д��һ����ַ��
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