package process.util;
import process.vo.PCB;
import process.vo.TableItem;

public class PartitionTable {
	private static TableItem head ;
	public final static int memory_maxSize = 2000;	//�����ڴ�����Ϊ2000kb;
	public final static int memory_minSize = 20;		//������С�ڴ�����Ϊ20kb ��������Ƭ;
	//�ڴ���䷽��
	private int type;
	public void setType(int type) {
		this.type = type;
	}
	public PartitionTable(){
		//��ʼ��������һ���ձ�Ŀ ��СΪ�����ڴ��С
		head = new TableItem();
		TableItem tableItem = new TableItem(PartitionTable.memory_maxSize,0,TableItem.tableItem_state_notYet,null);
		head.setNext(tableItem);
		type=0;
	}
	//��ָ��pcb�����ڴ�
	public boolean assignMemory(PCB pcb){
		boolean bool = false;
		//�״���Ӧ�㷨
		if(type==0){
			TableItem temp = head.getNext();
			int memoryNeed = pcb.getMemoryNeed();
			while(temp!=null){
				if(temp.getSize()>=memoryNeed){
					//�״���Ӧ   ��pcb�����ڴ�
					if(temp.getSize()-memoryNeed<PartitionTable.memory_minSize){ //��δ�ַ�����Ŀʣ������С����С���� ��ȫ�������pcb
						pcb.setStartLocation(temp.getStartLocation());
						pcb.setMemoryNeed(temp.getSize());
						pcb.setState(PCB.state_ready);
						this.removeTableItem(temp);	//�Ƴ���Ŀ
					}else{
						pcb.setStartLocation(temp.getStartLocation());
						pcb.setState(PCB.state_ready);
						temp.setStartLocation(temp.getStartLocation()+memoryNeed);	//�޸ı�Ŀ��ʼλ��
						temp.setSize(temp.getSize()-memoryNeed);	//�޸ı�Ŀ��С
					}
					bool = true;
					break;
				}
				temp = temp.getNext();
			}
		//�����Ӧ�㷨
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
				if(best.getSize()-memoryNeed<PartitionTable.memory_minSize){ //��δ�ַ�����Ŀʣ������С����С���� ��ȫ�������pcb
					pcb.setStartLocation(best.getStartLocation());
					pcb.setMemoryNeed(best.getSize());
					pcb.setState(PCB.state_ready);
					this.removeTableItem(best);	//�Ƴ���Ŀ
				}else{
					pcb.setStartLocation(best.getStartLocation());
					pcb.setState(PCB.state_ready);
					best.setStartLocation(best.getStartLocation()+memoryNeed);	//�޸ı�Ŀ��ʼλ��
					best.setSize(best.getSize()-memoryNeed);	//�޸ı�Ŀ��С
				}
			}
		}
		return bool;
	}
	/*��δ�ַ�������ɾ��һ��ָ����Ŀ*/
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
	/*��δ�ַ������в���һ��ָ����Ŀ*/
	public boolean insertTableItem(TableItem item){
		boolean bool = true;
		TableItem temp = head;
		while(temp.getNext()!=null){
			temp = temp.getNext();
		}
		temp.setNext(item);
		return bool;
	}
	/*�ͷ�һ��pcbʱ ��δ�ַ���������޸� �ϲ�*/
	public void resumeTableItem(PCB pcb){
		TableItem temp = new TableItem(pcb.getMemoryNeed(),pcb.getStartLocation(),TableItem.tableItem_state_notYet,null);
		this.insertTableItem(temp);	//����
		this.sort();				//����
		this.adjust();				//�����ϲ�
	}
	/*��δ�ַ������ڵı���ڴ���ʼλ������*/
	public TableItem sort(){
		if(head.getNext().getNext()==null){		//ֻ��һ����Ŀ  ����Ҫ����
			return head;
		}else{
			TableItem temp,temp1,temp2;
			temp = head;
			temp1 = head.getNext();
			temp2 = temp1.getNext();
			TableItem tail = null;
			while(temp2!=tail){
				while(temp2!=tail){
					if(temp1.getStartLocation()>temp2.getStartLocation()){//����λ��
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
	/*��δ�ַ�������е����ϲ�*/
	public TableItem adjust(){
		TableItem temp1 = head.getNext();
		TableItem temp2 = temp1.getNext();
		while(temp2 != null){
			if(temp1.getStartLocation()+temp1.getSize() == temp2.getStartLocation()){ //temp1 ��temp2 ��������
				temp1.setSize(temp1.getSize() + temp2.getSize()); //�ϲ�temp1 temp2
				temp1.setNext(temp2.getNext());	// ɾ��temp2
				temp2 = temp1.getNext();
			}else{
				temp1 = temp1.getNext();
				temp2 = temp1.getNext();
			}
		}
		return head;
	}
	/*��ӡδ�ַ����� �������ã�*/
	/*public void printfTableItem(){
		TableItem temp = head.getNext();
		System.out.println("δ�ַ�����");
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