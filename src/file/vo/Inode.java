package file.vo;

//ʹ����������(һ����ӵ�ַ)
public class Inode {
	//�����ڵ���Ϣ	64B
	//TOTAL NOW		59B
	public int fsize;		//�ļ���С	6B
	public int fblock;		//�ļ�����	6B
	public int faddr[];	//ֱ�ӿ��	4*3B
	public int fadd1;		//һ�����	3B
	public int fownid;		//������ID	6B
	public int fgrpid;		//������ID	6B
	public int fmode;		//�ļ�����Ȩ��	6B
	public String ftime;	//����޸�ʱ��	14B (20XXXXXXXXXX)
	public Inode(){
		faddr=new int[4];
	}
}