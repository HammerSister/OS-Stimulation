package user.init;
import java.util.ArrayList;
import java.util.List;
import user.thread.UserThread;

//设置全局的线程存储类
public class InitThread {
	public static List<UserThread> threadList = new ArrayList<UserThread>();
	@SuppressWarnings("unused")
	private InitThread initThread = new InitThread();
	private InitThread(){
	}
}
