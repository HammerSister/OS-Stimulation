package file.server;
import javax.swing.JOptionPane;
import user.vo.User;
import file.util.TimeHelper;
import file.vo.Dir;
import file.vo.Disk;
import file.vo.Inode;
import file.vo.MyFile;

//����Ŀ¼�Ĳ���,�涨ÿ��Ŀ¼����Ŀ¼ֻռһ���̿�
public class DirServer {
	public int uid = 111, gid = 111;
	private DiskServer ds;
	public DirServer(DiskServer ds) {
		this.ds = ds;
	}
	//����Ŀ¼
	public int mkdir(int ind, String dirname, String curname,User user) {
		Inode in = ds.readinode(ind);
		if (Disk.BSIZE - in.fsize < 36) {
			JOptionPane.showMessageDialog(null, "��ǰĿ¼����Ŀ¼�Ѿ��ﵽ���ޣ�");
			return 0;
		} else if (hassamedir(dirname, in)) {
			JOptionPane.showMessageDialog(null, "��Ŀ¼�Ѿ����ڣ�");
			return 0;
		} else {
			//Ϊ��Ŀ¼��������
			int iid = ds.allocinode();
			if (iid == -1) {
				JOptionPane.showMessageDialog(null, "�������㣡");
				return 0;
			}
			//Ϊ��Ŀ¼�����̿�
			int bid = ds.allocblock();
			if (bid == -1) {
				ds.freeinode(iid);
				JOptionPane.showMessageDialog(null, "�ռ䲻�㣡");
				return 0;
			}
			//�½�Ŀ¼
			Dir dir = new Dir();
			dir.fname = dirname;
			dir.findex = iid;
			dir.pname = curname;
			dir.pindex = ind;
			ds.writedir(in, dir, in.fsize / Disk.DSIZE);
			//��ǰĿ¼�ڵ���Ϣ�޸�
			in.fsize += Disk.DSIZE;
			in.ftime = new TimeHelper().gettime();
			ds.writeinode(in, ind);
			//�½�Ŀ¼�ڵ���Ϣ�޸�
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
	//ɾ��Ŀ¼,�ǿ�ʱ����ɾ��,���븸Ŀ¼�͵�ǰĿ¼��inodeid
	public void rmdir(int pind, int ind, String dirname) {
		Inode in = ds.readinode(ind);
		if (in.fsize != 0) {
			JOptionPane.showMessageDialog(null, "��ǰĿ¼��Ϊ�գ�����ɾ����");
		} else {
			//ɾ����Ŀ¼ռ�õ���Դ
			ds.freeblock(in.faddr[0]);
			ds.freeinode(ind);
			//ɾ����Ŀ¼�ڸ�Ŀ¼�е���Ŀ
			Inode pin = ds.readinode(pind);
			boolean find = false;
			for (int i = 0; i < pin.fsize / 36; i++) {
				Dir dir = ds.readdir(pin, i);
				if (dir.fname.equals(dirname)) {
					find = true;
					//�Ѿ��ҵ�,�����������ǰ��
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
	//����Ŀ¼��Ϣ
	public MyFile readDir(int ind, String dirname) {
		MyFile mf = new MyFile();
		mf.fname = dirname;
		Inode in = ds.readinode(ind);
		for (int i = 0; i < in.fsize / Disk.DSIZE; i++) {
			Dir dir = ds.readdir(in, i);
			//�ҵ���Ӧ��Ŀ�ļ�ѡ��
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
	//�ж��Ƿ���ͬ��Ŀ¼
	boolean hassamedir(String dirname, Inode in) {
		for (int i = 0; i < in.fsize / 36; i++) {
			Dir dir = ds.readdir(in, i);
			if (dir.fname.equals(dirname))
				return true;
		}
		return false;
	}
}