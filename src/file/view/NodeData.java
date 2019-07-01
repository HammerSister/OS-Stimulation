package file.view;

public class NodeData {
	public int type;
	public String snode;
	public int inode;
	public NodeData(int type,int inode,String snode){
		this.type=type;
		this.snode=snode;
		this.inode=inode;
	}
	public String toString(){
		return snode;
	}
}