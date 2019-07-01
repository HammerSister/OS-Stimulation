package file.vo;

//磁盘配置类
public class Disk {
	public final static int DISKSIZE=100*512;	//磁盘大小		512B*100 = 51200B
	public final static int INUM=80;				//索引节点数量	80个
	public final static int BNUM=100;			//块数量		100个
	public final static int BSIZE=512;			//块大小		512B
	public final static int ISIZE=64;			//索引节点大小	64B*80 = 5120B
	public final static int DSIZE=36;			//dir大小		36B*14 ~= 512B
	public final static int BASEB=11;			//不能使用的块(系统占用块)
}