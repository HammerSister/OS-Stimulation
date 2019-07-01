package file.server;
import user.vo.User;
import file.vo.Dir;
import file.vo.Inode;
import file.vo.MyFile;
import file.vo.SuperBlock;
import file.view.NodeData;

//���û�����֮��Ľӿ�
public class FilesysServer {
	String filename;
	private boolean firstrun = false;
	DiskServer ks;
	FileServer fs;
	DirServer ds;
	public FilesysServer(String filename) {
		this.filename = filename;
		ks = new DiskServer(filename);
		fs = new FileServer(ks);
		ds = new DirServer(ks);
		if (firstrun) {
			//��һ�����У���ʼ������
			ks.init();
			User user=new User();
			user.setUserId(1);
			mkdir("#", "system",user);
			mkdir("#", "users",user);
			mkdir("#", "files",user);
			mkdir("#", "user",user);
			mkdir("#", "intel",user);
			mkdir("#", "root",user);
			mkdir("#", "temp",user);
			mkdir("#/users", "zsj",user);
			mkdir("#/ueser", "xyy",user);
			mkdir("#/users", "lct",user);
		} else {
			//�ǵ�һ������,��ȡ��������
			ks.read();
		}
	}
	//��װ�����ļ���Ŀ¼����,��Ϊ�û������֮��Ľӿ�
	public int mkdir(String path, String name,User user) {
		String s[] = path.split("/");
		int x[] = toindir(s);
		return ds.mkdir(x[x[0] - 1], name, s[s.length - 1],user);
	}
	public void rmdir(String path) {
		String s[] = path.split("/");
		int x[] = toindir(s);
		ds.rmdir(x[x[0] - 2], x[x[0] - 1], s[s.length - 1]);
	}
	public int mkfile(String path, String name, String cont,User user) {
		String s[] = path.split("/");
		int x[] = toindir(s);
		return fs.mkfile(x[x[0] - 1], name, cont, s[s.length - 1],user);
	}
	public void rmfile(String path) {
		String s[] = path.split("/");
		int x[] = toindir(s);
		fs.rmfile(x[x[0] - 2], s[s.length - 1]);
	}
	public MyFile readfile(String path) {
		String s[] = path.split("/");
		int x[] = toindir(s);
		return fs.readFile(x[x[0] - 2], s[s.length - 1]);
	}
	public MyFile readdir(String path) {
		String s[] = path.split("/");
		int x[] = toindir(s);
		return ds.readDir(x[x[0] - 2], s[s.length - 1]);
	}
	public int writefile(String path, String name, String cont) {
		String s[] = path.split("/");
		int x[] = toindir(s);
		return fs.updateFile(x[x[0]-2],x[x[0]-1],name, cont);
	}
	//���������ڵ�Ϳ���Ϣ
	public SuperBlock getSuperblock(){
		return ks.loadsb();
	}
	//��ýڵ�����ж���
	public NodeData[] getchild(int inode){
		Inode in=ks.readinode(inode);
		NodeData[] nd=new NodeData[in.fsize/36];
		for(int i=0;i<nd.length;i++){
			Dir dir=ks.readdir(in, i);
			Inode iin=ks.readinode(dir.findex);
			//1Ŀ¼2�ļ� , ��Ŀ¼inode,��Ŀ¼��
			nd[i]=new NodeData(iin.fmode/1000,dir.findex,dir.fname);
		}
		return nd;
	}
	//�Դ�����ַ������н���
	//����Ŀ¼�������� x[x[0]-1]�ǵ�ǰ�ڵ�,x[x[0]-2]�Ǹ��ڵ�
	public int[] toindir(String s[]) {
		int x[] = new int[100];
		x[1] = 0;
		x[0] = 2;
		//·��֮����/����,��Ŀ¼��#�ű�ʾ
		for (int i = 1; i < s.length; i++) {
			if (s[i].length() == 0)
				continue;
			int now = x[0];
			Inode in = ks.readinode(x[now - 1]);
			boolean find = false;
			for (int j = 0; j < in.fsize / 36; j++) {
				Dir dir = ks.readdir(in, j);
				if (dir.fname.equals(s[i])) {
					find = true;
					x[now] = dir.findex;
					x[0]++;
				}
			}
			//�ڸ�·����û���ҵ���Ӧ��Ŀ¼,x[0]���Ϊ-1
			if (find == false) {
				x[0] = -1;
				return x;
			}
		}
		return x;
	}
}