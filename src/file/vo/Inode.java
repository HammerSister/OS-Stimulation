package file.vo;

//使用索引分配(一级间接地址)
public class Inode {
	//索引节点信息	64B
	//TOTAL NOW		59B
	public int fsize;		//文件大小	6B
	public int fblock;		//文件块数	6B
	public int faddr[];	//直接块号	4*3B
	public int fadd1;		//一级块号	3B
	public int fownid;		//所有者ID	6B
	public int fgrpid;		//所在组ID	6B
	public int fmode;		//文件类型权限	6B
	public String ftime;	//最近修改时间	14B (20XXXXXXXXXX)
	public Inode(){
		faddr=new int[4];
	}
}