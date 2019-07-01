package file.server;
import java.util.Scanner;
import file.util.FileHelper;
import file.util.StringHelper;
import file.util.TimeHelper;
import file.vo.Dir;
import file.vo.Disk;
import file.vo.Inode;
import file.vo.SuperBlock;

public class DiskServer {
	String filename;
	FileHelper fh;
	StringHelper sh;
	public SuperBlock sb;
	public DiskServer(String filename) {
		this.filename = filename;
		//初始化辅助工具
		fh = new FileHelper(filename);
		sh = new StringHelper();
	}
	/* 初始化部分 */
	int rindex, rblock;
	Inode rnode;
	public void init() {
		//格式化磁盘后,读取超级块
		formatDisk();
		sb = readSuperblcok();
		//写入根节点
		rindex = allocinode();	// 0
		rblock = allocblock();	// 11
		rnode = new Inode();
		rnode.fsize = 0;
		rnode.fblock = 1;
		rnode.faddr[0] = rblock;
		rnode.faddr[1] = 0;
		rnode.faddr[2] = 0;
		rnode.faddr[3] = 0;
		rnode.fadd1 = 0;
		rnode.fownid = 1;
		rnode.fgrpid = 1;
		rnode.fmode = 1777;
		rnode.ftime = new TimeHelper().gettime();
		writeinode(rnode, rindex);
	}
	public void read() {
		//读取超级块
		sb = readSuperblcok();
		//读取根节点信息
		rindex = 0;		// 0
		rblock = 11;	// 11
		rnode = readinode(rindex);
	}
	/* 磁盘操作部分 */
	//格式化磁盘
	public void formatDisk() {
		sh.clear();
		sh.app(new int[Disk.INUM], 100);
		int[] x = new int[Disk.BNUM];
		for (int i = 0; i <= 10; i++)
			x[i] = 1;
		sh.app(x, 120);
		sh.app(Disk.INUM, 5);
		sh.app(Disk.BNUM - Disk.BASEB, 5);
		sh.fill(Disk.DISKSIZE);
		fh.save(0, sh.get());
	}
	/* 超级块操作部分 */
	//写入超级块
	public void writeSuperblock() {
		sh.clear();
		sh.app(sb.bitindex, 100);
		sh.app(sb.bitblock, 120);
		sh.app(sb.freindex, 5);
		sh.app(sb.freblock, 5);
		sh.fill(Disk.BSIZE);
		fh.save(0, sh.get());
	}
	//读取超级块
	public SuperBlock readSuperblcok() {
		SuperBlock ss = new SuperBlock();
		String str = fh.read(0, Disk.BSIZE);
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(str);
		String tmp = sc.next();
		ss.bitindex = new int[tmp.length()];
		for (int i = 0; i < ss.bitindex.length; i++)
			ss.bitindex[i] = tmp.charAt(i) - '0';
		tmp = sc.next();
		ss.bitblock = new int[tmp.length()];
		for (int i = 0; i < ss.bitblock.length; i++)
			ss.bitblock[i] = tmp.charAt(i) - '0';
		ss.freindex = sc.nextInt();
		ss.freblock = sc.nextInt();
		return ss;
	}
	/* 索引节点操作部分 */
	//申请inode索引节点
	public int allocinode() {
		if (sb.freindex == 0)
			return -1;
		for (int i = 0; i < Disk.INUM; i++) {
			if (sb.bitindex[i] == 0) {
				sb.bitindex[i] = 1;
				sb.freindex--;
				writeSuperblock();
				return i;
			}
		}
		return -1;
	}
	//释放inode索引节点
	public int freeinode(int ind) {
		if (sb.bitindex[ind] == 0)
			return -1;
		//修改超级块
		sb.bitindex[ind] = 0;
		sb.freindex++;
		writeSuperblock();
		//清除节点内容
		sh.clear();
		sh.fill(Disk.ISIZE);
		fh.save(Disk.BSIZE + ind * Disk.ISIZE, sh.get());
		return ind;
	}
	//读取inode索引节点
	public Inode readinode(int ind) {
		Inode in = new Inode();
		String str = fh.read(Disk.BSIZE + ind * Disk.ISIZE, Disk.ISIZE);
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(str);
		in.fsize = sc.nextInt();
		in.fblock = sc.nextInt();
		in.faddr[0] = sc.nextInt();
		in.faddr[1] = sc.nextInt();
		in.faddr[2] = sc.nextInt();
		in.faddr[3] = sc.nextInt();
		in.fadd1 = sc.nextInt();
		in.fownid = sc.nextInt();
		in.fgrpid = sc.nextInt();
		in.fmode = sc.nextInt();
		in.ftime = sc.next();
		return in;
	}
	//写入inode索引节点
	public void writeinode(Inode in, int ind) {
		sh.clear();
		sh.app(in.fsize, 6);
		sh.app(in.fblock, 6);
		sh.app(in.faddr[0], 3);
		sh.app(in.faddr[1], 3);
		sh.app(in.faddr[2], 3);
		sh.app(in.faddr[3], 3);
		sh.app(in.fadd1, 3);
		sh.app(in.fownid, 6);
		sh.app(in.fgrpid, 6);
		sh.app(in.fmode, 6);
		sh.app(in.ftime, 14);
		sh.fill(Disk.ISIZE);
		fh.save(Disk.BSIZE + ind * Disk.ISIZE, sh.get());
	}
	public SuperBlock loadsb(){
		return sb;
	}
	/* 块操作部分 */
	//申请盘块
	public int allocblock() {
		if (sb.freblock == 0)
			return -1;
		for (int i = 0; i < Disk.BNUM; i++) {
			if (sb.bitblock[i] == 0) {
				sb.bitblock[i] = 1;
				sb.freblock--;
				writeSuperblock();
				return i;
			}
		}
		return -1;
	}
	//释放盘块
	public int freeblock(int ind) {
		if (sb.bitblock[ind] == 0)
			return -1;
		//修改超级块
		sb.bitblock[ind] = 0;
		sb.freblock++;
		writeSuperblock();
		//清除块内容
		sh.clear();
		sh.fill(Disk.BSIZE);
		fh.save(Disk.BSIZE * ind, sh.get());
		return ind;
	}
	//对块写操作
	public void writeblock(int ind, String con) {
		sh.clear();
		sh.app(con, Disk.BSIZE);
		fh.save(Disk.BSIZE * ind, sh.get());
	}
	//对块读操作
	public String readblock(int ind, int size) {
		return fh.read(Disk.BSIZE * ind, size);
	}
	/* 目录操作部分 */
	//读目录
	public Dir readdir(Inode in, int ind) {
		String str = fh.read(in.faddr[0] * Disk.BSIZE + ind * Disk.DSIZE,Disk.DSIZE);
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(str);
		Dir dir = new Dir();
		dir.fname = sc.next();
		dir.findex = sc.nextInt();
		dir.pname = sc.next();
		dir.pindex = sc.nextInt();
		return dir;
	}
	//写目录
	public void writedir(Inode in, Dir dir, int ind) {
		sh.clear();
		sh.app(dir.fname, 15);
		sh.app(dir.findex, 3);
		sh.app(dir.pname, 15);
		sh.app(dir.pindex, 3);
		sh.fill(Disk.DSIZE);
		fh.save(in.faddr[0] * Disk.BSIZE + ind * Disk.DSIZE, sh.get());
	}
	//清空这个目录项,辅助删除目录
	public void cleardir(Inode in, int ind) {
		sh.clear();
		sh.fill(Disk.DSIZE);
		fh.save(in.faddr[0] * Disk.BSIZE + ind * Disk.DSIZE, sh.get());
	}
}