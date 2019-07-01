package file.server;
import user.vo.User;
import file.vo.Dir;
import file.vo.Inode;
import file.vo.MyFile;
import file.vo.SuperBlock;
import file.view.NodeData;

//与用户操作之间的接口
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
			//第一次运行，初始化磁盘
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
			//非第一次运行,读取磁盘内容
			ks.read();
		}
	}
	//封装各种文件与目录操作,作为用户与磁盘之间的接口
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
	//载入索引节点和块信息
	public SuperBlock getSuperblock(){
		return ks.loadsb();
	}
	//获得节点的所有儿子
	public NodeData[] getchild(int inode){
		Inode in=ks.readinode(inode);
		NodeData[] nd=new NodeData[in.fsize/36];
		for(int i=0;i<nd.length;i++){
			Dir dir=ks.readdir(in, i);
			Inode iin=ks.readinode(dir.findex);
			//1目录2文件 , 该目录inode,该目录名
			nd[i]=new NodeData(iin.fmode/1000,dir.findex,dir.fname);
		}
		return nd;
	}
	//对传入的字符串进行解析
	//返回目录的索引序 x[x[0]-1]是当前节点,x[x[0]-2]是父节点
	public int[] toindir(String s[]) {
		int x[] = new int[100];
		x[1] = 0;
		x[0] = 2;
		//路径之间用/隔开,根目录用#号表示
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
			//在该路径下没有找到对应的目录,x[0]标记为-1
			if (find == false) {
				x[0] = -1;
				return x;
			}
		}
		return x;
	}
}