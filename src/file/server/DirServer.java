package file.server;
import javax.swing.JOptionPane;
import user.vo.User;
import file.util.TimeHelper;
import file.vo.Dir;
import file.vo.Disk;
import file.vo.Inode;
import file.vo.MyFile;

//关于目录的操作,规定每个目录的子目录只占一个盘块
public class DirServer {
	public int uid = 111, gid = 111;
	private DiskServer ds;
	public DirServer(DiskServer ds) {
		this.ds = ds;
	}
	//创建目录
	public int mkdir(int ind, String dirname, String curname,User user) {
		Inode in = ds.readinode(ind);
		if (Disk.BSIZE - in.fsize < 36) {
			JOptionPane.showMessageDialog(null, "当前目录下子目录已经达到上限！");
			return 0;
		} else if (hassamedir(dirname, in)) {
			JOptionPane.showMessageDialog(null, "该目录已经存在！");
			return 0;
		} else {
			//为该目录分配索引
			int iid = ds.allocinode();
			if (iid == -1) {
				JOptionPane.showMessageDialog(null, "索引不足！");
				return 0;
			}
			//为该目录分配盘块
			int bid = ds.allocblock();
			if (bid == -1) {
				ds.freeinode(iid);
				JOptionPane.showMessageDialog(null, "空间不足！");
				return 0;
			}
			//新建目录
			Dir dir = new Dir();
			dir.fname = dirname;
			dir.findex = iid;
			dir.pname = curname;
			dir.pindex = ind;
			ds.writedir(in, dir, in.fsize / Disk.DSIZE);
			//当前目录节点信息修改
			in.fsize += Disk.DSIZE;
			in.ftime = new TimeHelper().gettime();
			ds.writeinode(in, ind);
			//新建目录节点信息修改
			Inode inode = new Inode();
			inode.fsize = 0;
			inode.fblock = 1;
			inode.faddr[0] = bid;
			inode.faddr[1] = 0;
			inode.faddr[2] = 0;
			inode.faddr[3] = 0;
			inode.fadd1 = 0;
			inode.fownid = user.getUserId();
			inode.fgrpid = gid;
			inode.fmode = 1777;
			inode.ftime = new TimeHelper().gettime();
			ds.writeinode(inode, iid);
			return 1;
		}
	}
	//删除目录,非空时才能删除,传入父目录和当前目录的inodeid
	public void rmdir(int pind, int ind, String dirname) {
		Inode in = ds.readinode(ind);
		if (in.fsize != 0) {
			JOptionPane.showMessageDialog(null, "当前目录不为空，不能删除！");
		} else {
			//删除该目录占用的资源
			ds.freeblock(in.faddr[0]);
			ds.freeinode(ind);
			//删除该目录在父目录中的条目
			Inode pin = ds.readinode(pind);
			boolean find = false;
			for (int i = 0; i < pin.fsize / 36; i++) {
				Dir dir = ds.readdir(pin, i);
				if (dir.fname.equals(dirname)) {
					find = true;
					//已经找到,将后面的内容前移
				} else if (find) {
					ds.writedir(pin, ds.readdir(pin, i), i - 1);
				}
			}
			ds.cleardir(pin, pin.fsize / 36 - 1);
			pin.fsize -= Disk.DSIZE;
			pin.ftime = new TimeHelper().gettime();
			ds.writeinode(pin, pind);
		}
	}
	//返回目录信息
	public MyFile readDir(int ind, String dirname) {
		MyFile mf = new MyFile();
		mf.fname = dirname;
		Inode in = ds.readinode(ind);
		for (int i = 0; i < in.fsize / Disk.DSIZE; i++) {
			Dir dir = ds.readdir(in, i);
			//找到对应的目文件选项
			if (dir.fname.equals(dirname)) {
				Inode iin = ds.readinode(dir.findex);
				mf.fsize = iin.fsize;
				mf.ftime = iin.ftime;
				mf.fmode = iin.fmode;
				mf.fownid = iin.fownid;
				mf.fgrpid = iin.fgrpid;
				mf.fcont = "";
			}
		}
		return mf;
	}
	//判断是否有同名目录
	boolean hassamedir(String dirname, Inode in) {
		for (int i = 0; i < in.fsize / 36; i++) {
			Dir dir = ds.readdir(in, i);
			if (dir.fname.equals(dirname))
				return true;
		}
		return false;
	}
}