package process.util;
import process.vo.Queue;

public class ReserveQueue {
	private static Queue queue;
	public ReserveQueue(){
		queue = new Queue();
	}
	public static Queue getQueue() {
		return queue;
	}
	public static void setQueue(Queue queue) {
		ReserveQueue.queue = queue;
	}
}
