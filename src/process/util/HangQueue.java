package process.util;
import process.vo.Queue;

public class HangQueue extends Queue{
	private static Queue queue;
	public HangQueue(){
		queue = new Queue();
	}
	public static Queue getQueue() {
		return queue;
	}
	public static void setQueue(Queue queue) {
		HangQueue.queue = queue;
	}
}
