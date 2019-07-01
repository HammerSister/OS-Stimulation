package process.vo;
import java.awt.Color;

public class PCB {
	private int PID;
	private int timeNeed;
	private int priority;
	private int state;
	private int memoryNeed;
	private int startLocation;
	private PCB next;
	private Color color;
	private int pecent;
	
	public final static int state_running = 0;			//״̬��־λ 0��ʾ���� 1��ʾ���� 2��ʾ���� 3��ʾ�� 4��ʾ���
	public final static int state_ready = 1;			//����
	public final static int state_hang = 2;				//����
	public final static int state_reserve = 3;			//��
	public final static int state_resume = 3;			//���
	public final static int state_block = 4;			//����
	public final static int state_dead = 5;				//����
	
	public PCB(){}
	
	public PCB(PCB pcb){
		this.PID = pcb.PID;
		this.timeNeed = pcb.timeNeed;
		this.priority = pcb.priority;
		this.state = pcb.state;
		this.memoryNeed = pcb.memoryNeed;
		this.startLocation = pcb.startLocation;
		this.pecent = 0;
		this.next = null;
	}
	
	public PCB(int PID,int timeNeed,int priority,int state,int memoryNeed,int startLocation,PCB next){
		this.PID = PID;
		this.timeNeed = timeNeed;
		this.priority = priority;
		this.state = state;
		this.memoryNeed = memoryNeed;
		this.startLocation = startLocation;
		this.next = next;
	}
	
	
	
	
	public int getPID() {
		return PID;
	}
	public void setPID(int pID) {
		PID = pID;
	}
	public int getTimeNeed() {
		return timeNeed;
	}
	public void setTimeNeed(int timeNeed) {
		this.timeNeed = timeNeed;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getMemoryNeed() {
		return memoryNeed;
	}
	public void setMemoryNeed(int memoryNeed) {
		this.memoryNeed = memoryNeed;
	}
	public int getStartLocation() {
		return startLocation;
	}
	public void setStartLocation(int startLocation) {
		this.startLocation = startLocation;
	}
	public PCB getNext() {
		return next;
	}
	public void setNext(PCB next) {
		this.next = next;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getPecent() {
		return pecent;
	}

	public void setPecent(int pecent) {
		this.pecent = pecent;
	}
	
	
}
