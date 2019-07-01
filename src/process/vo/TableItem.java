package process.vo;

public class TableItem {
	private int size;
	private int startLocation;
	private int state;
	private TableItem next;
	
	public final static int tableItem_state_notYet = 0;	//Œ¥∑÷≈‰
	
	public TableItem(){}
	
	public TableItem(int size, int startLocation, int state, TableItem next){
		this.size = size;
		this.startLocation = startLocation;
		this.state = state;
		this.next = next;
	}
	
	
	
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getStartLocation() {
		return startLocation;
	}
	public void setStartLocation(int startLocation) {
		this.startLocation = startLocation;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public TableItem getNext() {
		return next;
	}
	public void setNext(TableItem next) {
		this.next = next;
	}
	
	
	
}
