package user.thread;

/*
 * �û��߳���
 */
public class UserThread implements Runnable{
	//�̵߳�id��name���û����̵߳�id��name���Ӧ
	private Integer threadId;
	private String threadName;
    public UserThread(Integer threadId,String threadName){
    	this.threadId = threadId;
    	this.threadName = threadName;
    }
	//�̵߳Ķ���
	public void run() {
	}
	public Integer getThreadId() {
		return threadId;
	}
	public void setThreadId(Integer threadId) {
		this.threadId = threadId;
	}
	public String getThreadName() {
		return threadName;
	}
	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}
	public String toString() {
		return "UserThread [threadId=" + threadId + ", threadName=" + threadName + "]";
	}
}
