package process.util;
import process.vo.PCB;
import process.vo.TableItem;

public class PartitionTable {
	private static TableItem head ;
	public final static int memory_maxSize = 2000;	//定义内存容量为2000kb;
	public final static int memory_minSize = 20;		//定义最小内存容量为20kb 减少外碎片;
	//内存分配方法
	private int type;
	public void setType(int type) {
		this.type = type;
	}
	public PartitionTable(){
		//初始化建立第一条空表目 大小为整个内存大小
		head = new TableItem();
		TableItem tableItem = new TableItem(PartitionTable.memory_maxSize,0,TableItem.tableItem_state_notYet,null);
		head.setNext(tableItem);
		type=0;
	}
	//给指定pcb分配内存
	public boolean assignMemory(PCB pcb){
		boolean bool = false;
		//首次适应算法
		if(type==0){
			TableItem temp = head.getNext();
			int memoryNeed = pcb.getMemoryNeed();
			while(temp!=null){
				if(temp.getSize()>=memoryNeed){
					//首次适应   给pcb分配内存
					if(temp.getSize()-memoryNeed<PartitionTable.memory_minSize){ //此未分分区表目剩下容量小于最小容量 则全部分配给pcb
						pcb.setStartLocation(temp.getStartLocation());
						pcb.setMemoryNeed(temp.getSize());
						pcb.setState(PCB.state_ready);
						this.removeTableItem(temp);	//移除表目
					}else{
						pcb.setStartLocation(temp.getStartLocation());
						pcb.setState(PCB.state_ready);
						temp.setStartLocation(temp.getStartLocation()+memoryNeed);	//修改表目起始位置
						temp.setSize(temp.getSize()-memoryNeed);	//修改表目大小
					}
					bool = true;
					break;
				}
				temp = temp.getNext();
			}
		//最佳适应算法
		}else if(type==1){
			TableItem temp = head.getNext(),best=null;
			int memoryNeed = pcb.getMemoryNeed();
			while(temp!=null){
				if(temp.getSize()>=memoryNeed){
					if(best==null)best=temp;
					else{
						if(temp.getSize()<best.getSize()){
						
							best=temp;
						}
					}
					bool = true;
				}
				temp = temp.getNext();
			}
			if(bool){
				if(best.getSize()-memoryNeed<PartitionTable.memory_minSize){ //此未分分区表目剩下容量小于最小容量 则全部分配给pcb
					pcb.setStartLocation(best.getStartLocation());
					pcb.setMemoryNeed(best.getSize());
					pcb.setState(PCB.state_ready);
					this.removeTableItem(best);	//移除表目
				}else{
					pcb.setStartLocation(best.getStartLocation());
					pcb.setState(PCB.state_ready);
					best.setStartLocation(best.getStartLocation()+memoryNeed);	//修改表目起始位置
					best.setSize(best.getSize()-memoryNeed);	//修改表目大小
				}
			}
		}
		return bool;
	}
	/*在未分分区表中删除一条指定表目*/
	public boolean removeTableItem(TableItem item){
		TableItem temp = head;
		boolean bool = false;
		while(temp.getNext()!=null){
			if(temp.getNext()==item){
				temp.setNext(temp.getNext().getNext());
				bool = true;
				break;
			}
			temp = temp.getNext();
		}
		return bool;
	}
	/*在未分分区表中插入一条指定表目*/
	public boolean insertTableItem(TableItem item){
		boolean bool = true;
		TableItem temp = head;
		while(temp.getNext()!=null){
			temp = temp.getNext();
		}
		temp.setNext(item);
		return bool;
	}
	/*释放一条pcb时 对未分分区表进行修改 合并*/
	public void resumeTableItem(PCB pcb){
		TableItem temp = new TableItem(pcb.getMemoryNeed(),pcb.getStartLocation(),TableItem.tableItem_state_notYet,null);
		this.insertTableItem(temp);	//插入
		this.sort();				//排序
		this.adjust();				//调整合并
	}
	/*对未分分区表内的表项按内存起始位置排序*/
	public TableItem sort(){
		if(head.getNext().getNext()==null){		//只有一条表目  不需要排序
			return head;
		}else{
			TableItem temp,temp1,temp2;
			temp = head;
			temp1 = head.getNext();
			temp2 = temp1.getNext();
			TableItem tail = null;
			while(temp2!=tail){
				while(temp2!=tail){
					if(temp1.getStartLocation()>temp2.getStartLocation()){//交换位置
						temp.setNext(temp2);
						temp1.setNext(temp2.getNext());
						temp2.setNext(temp1);
						temp1 = temp.getNext();
						temp2 = temp1.getNext();
					}
					temp = temp.getNext();
					temp1 = temp1.getNext();
					temp2 = temp1.getNext();
				}
				tail = temp1;
				temp = head;
				temp1 = head.getNext();
				temp2 = temp1.getNext();
			}
		}
		return head;		
	}
	/*对未分分区表进行调整合并*/
	public TableItem adjust(){
		TableItem temp1 = head.getNext();
		TableItem temp2 = temp1.getNext();
		while(temp2 != null){
			if(temp1.getStartLocation()+temp1.getSize() == temp2.getStartLocation()){ //temp1 和temp2 是连续的
				temp1.setSize(temp1.getSize() + temp2.getSize()); //合并temp1 temp2
				temp1.setNext(temp2.getNext());	// 删除temp2
				temp2 = temp1.getNext();
			}else{
				temp1 = temp1.getNext();
				temp2 = temp1.getNext();
			}
		}
		return head;
	}
	/*打印未分分区表 （测试用）*/
	/*public void printfTableItem(){
		TableItem temp = head.getNext();
		System.out.println("未分分区表");
		System.out.println("size	startlocation		state");
		while(temp!=null){
			System.out.println(temp.getSize()+"		"+temp.getStartLocation()+"		"+temp.getState());
			temp = temp.getNext();
		}
	}*/
	public static TableItem getHead() {
		return head;
	}
	public static void setHead(TableItem head) {
		PartitionTable.head = head;
	}
}