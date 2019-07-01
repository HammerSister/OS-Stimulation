package user.thread;

/*
 * 用户线程类
 */
public class UserThread implements Runnable{
	//线程的id和name与用户的线程的id和name相对应
	private Integer threadId;
	private String threadName;
    public UserThread(Integer threadId,String threadName){
    	this.threadId = threadId;
    	this.threadName = threadName;
    }
	//线程的动作
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
