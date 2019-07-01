package process.vo;

public class Queue {
	private PCB head;
	private int size;
	//���е������ֵ����㷨
	private int type;
	public void setType(int t){
		this.type=t;
	}
	public Queue() {
		head = new PCB();
		size = 0;
		type=0;
	}
	public void getMethod( ){

	}
	public boolean insert(PCB pcb) {// ����pcb������
		PCB temp = this.head;
		while (temp.getNext() != null) {
			temp = temp.getNext();
		}
		temp.setNext(pcb);
		pcb.setNext(null);
		size++;
		return true;
	}
	public boolean delete(PCB pcb) { // ɾ��������ָ����pcb
		PCB temp = this.head;
		boolean bool = false;
		while (temp.getNext() != null) {
			if (temp.getNext().getPID() == pcb.getPID()) {
				temp.setNext(temp.getNext().getNext()); // ɾ��pcb
				size--;
				bool = true; // ɾ���ɹ�
				break;
			}
			temp = temp.getNext();
		}
		return bool;
	}
	public boolean modify(PCB pcb) { // �޸Ķ����к�pcb��PID��ͬ��PCB
		PCB temp = this.head;
		boolean bool = false;
		while (temp.getNext() != null) {
			if (temp.getNext().getPID() == pcb.getPID()) {
				temp.getNext().setTimeNeed(pcb.getTimeNeed());
				temp.getNext().setPriority(pcb.getPriority());
				temp.getNext().setState(pcb.getState());
				temp.getNext().setMemoryNeed(pcb.getMemoryNeed());
				temp.getNext().setStartLocation(pcb.getStartLocation());
				bool = true; // �޸ĳɹ�
				break;
			}
			temp = temp.getNext();
		}
		return bool;
	}
	public PCB getFirst() { // �õ����еĵ�һ��PCB
		return this.head.getNext();
	}
	public PCB sortQueue1(){//���ȼ�����
		if(type==0){
			 sortQueue2();
		}else if(type==1){
			sortQueue3();
		}else 
			if(type==2){
			PCB pcb1,pcb2,temp;
			PCB tail = null;
			if(head.getNext() == null){	//����Ϊ��
				return head;
			}else if(head.getNext()!=null && head.getNext().getNext()==null){//����ֻ��һ������
				return head;
			}else{
				temp = head;
				pcb1 = head.getNext();
				pcb2 = pcb1.getNext();
				while(pcb2!=tail){
					while(pcb2!=tail){
						if(pcb1.getPriority()<pcb2.getPriority()){	//�Ե�����λ��
							temp.setNext(pcb2);
							pcb1.setNext(pcb2.getNext());
							pcb2.setNext(pcb1);
							pcb1 = temp.getNext();
							pcb2 = pcb1.getNext();
						}
						temp = temp.getNext();
						pcb1 = pcb1.getNext();
						pcb2 = pcb1.getNext();
					}
					tail = pcb1;
					temp = head;
					pcb1 = head.getNext();
					pcb2 = pcb1.getNext();
				}
			}
		}
		return head;
	}
	@SuppressWarnings("unused")
	public PCB sortQueue2(){//�Ƚ��ȷ���
		PCB pcb1,pcb2,temp;
		return head;
	}
	public PCB sortQueue3(){//����ҵ����
		PCB pcb1,pcb2,temp;
		PCB tail = null;
		if(head.getNext() == null){	//����Ϊ��
			return head;
		}else if(head.getNext()!=null && head.getNext().getNext()==null){//����ֻ��һ������
			return head;
		}else{
			temp = head;
			pcb1 = head.getNext();
			pcb2 = pcb1.getNext();
			while(pcb2!=tail){
				while(pcb2!=tail){
					if(pcb1.getTimeNeed()>pcb2.getTimeNeed()){	//�Ե�����λ��
						temp.setNext(pcb2);
						pcb1.setNext(pcb2.getNext());
						pcb2.setNext(pcb1);
						pcb1 = temp.getNext();
						pcb2 = pcb1.getNext();
					}
					temp = temp.getNext();
					pcb1 = pcb1.getNext();
					pcb2 = pcb1.getNext();
				}
				tail = pcb1;
				temp = head;
				pcb1 = head.getNext();
				pcb2 = pcb1.getNext();
			}
		}
		return head;
	}
	public boolean isEmpty(){
		return head.getNext()==null;
	}
	public PCB getHead() {
		return head;
	}
	public void setHead(PCB head) {
		this.head = head;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
}