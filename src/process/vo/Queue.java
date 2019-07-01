package process.vo;

public class Queue {
	private PCB head;
	private int size;
	//队列掉用哪种调度算法
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
	public boolean insert(PCB pcb) {// 插入pcb到队列
		PCB temp = this.head;
		while (temp.getNext() != null) {
			temp = temp.getNext();
		}
		temp.setNext(pcb);
		pcb.setNext(null);
		size++;
		return true;
	}
	public boolean delete(PCB pcb) { // 删除队列中指定的pcb
		PCB temp = this.head;
		boolean bool = false;
		while (temp.getNext() != null) {
			if (temp.getNext().getPID() == pcb.getPID()) {
				temp.setNext(temp.getNext().getNext()); // 删除pcb
				size--;
				bool = true; // 删除成功
				break;
			}
			temp = temp.getNext();
		}
		return bool;
	}
	public boolean modify(PCB pcb) { // 修改队列中和pcb的PID相同的PCB
		PCB temp = this.head;
		boolean bool = false;
		while (temp.getNext() != null) {
			if (temp.getNext().getPID() == pcb.getPID()) {
				temp.getNext().setTimeNeed(pcb.getTimeNeed());
				temp.getNext().setPriority(pcb.getPriority());
				temp.getNext().setState(pcb.getState());
				temp.getNext().setMemoryNeed(pcb.getMemoryNeed());
				temp.getNext().setStartLocation(pcb.getStartLocation());
				bool = true; // 修改成功
				break;
			}
			temp = temp.getNext();
		}
		return bool;
	}
	public PCB getFirst() { // 得到队列的第一个PCB
		return this.head.getNext();
	}
	public PCB sortQueue1(){//优先级队列
		if(type==0){
			 sortQueue2();
		}else if(type==1){
			sortQueue3();
		}else 
			if(type==2){
			PCB pcb1,pcb2,temp;
			PCB tail = null;
			if(head.getNext() == null){	//队列为空
				return head;
			}else if(head.getNext()!=null && head.getNext().getNext()==null){//队列只有一个进程
				return head;
			}else{
				temp = head;
				pcb1 = head.getNext();
				pcb2 = pcb1.getNext();
				while(pcb2!=tail){
					while(pcb2!=tail){
						if(pcb1.getPriority()<pcb2.getPriority()){	//对调两个位置
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
	public PCB sortQueue2(){//先进先服务
		PCB pcb1,pcb2,temp;
		return head;
	}
	public PCB sortQueue3(){//短作业优先
		PCB pcb1,pcb2,temp;
		PCB tail = null;
		if(head.getNext() == null){	//队列为空
			return head;
		}else if(head.getNext()!=null && head.getNext().getNext()==null){//队列只有一个进程
			return head;
		}else{
			temp = head;
			pcb1 = head.getNext();
			pcb2 = pcb1.getNext();
			while(pcb2!=tail){
				while(pcb2!=tail){
					if(pcb1.getTimeNeed()>pcb2.getTimeNeed()){	//对调两个位置
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