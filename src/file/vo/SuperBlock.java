package file.vo;

public class SuperBlock {
	public int bitindex[];		//空余块位示图		100B
	public int bitblock[];		//空余节点位示图	120B
	public int freindex;		//剩余的节点		5B
	public int freblock;		//剩余的块			5B
}
