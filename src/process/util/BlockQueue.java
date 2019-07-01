package process.util;
import process.vo.Queue;

public class BlockQueue {
	private static Queue queue;
	public BlockQueue(){
		queue = new Queue();
	}
	public static Queue getQueue() {
		return queue;
	}
	public static void setQueue(Queue queue) {
		BlockQueue.queue = queue;
	}
}
