package process.util;
import process.vo.Queue;

public class ReadyQueue {
	private static Queue queue;
	public ReadyQueue(){
		queue = new Queue();
	}
	public static Queue getQueue() {
		return queue;
	}
	public static void setQueue(Queue queue) {
		ReadyQueue.queue = queue;
	}
}
