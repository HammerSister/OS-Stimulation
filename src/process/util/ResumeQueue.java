package process.util;
import process.vo.Queue;

public class ResumeQueue {
	private static Queue queue;
	public ResumeQueue(){
		queue = new Queue();
	}
	public static Queue getQueue() {
		return queue;
	}
	public static void setQueue(Queue queue) {
		ResumeQueue.queue = queue;
	}
}
