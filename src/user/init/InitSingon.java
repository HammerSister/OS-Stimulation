package user.init;
import java.io.IOException;
import java.util.List;
import user.util.FileUtils;
import user.vo.User;

//user文件的内容缓存到userList里面
public class InitSingon {
	private static InitSingon init = new InitSingon();
	@SuppressWarnings("static-access")
	public static List<User> userList = InitSingon.init.getUserList();
	private InitSingon(){
		
	}
	public static InitSingon instance(){
		return  init;
	}
	//这个方法在这个系统中也有一个
	public static List<User> getUserList(){
		FileUtils fileUtils = new FileUtils();
		try {
		   userList = fileUtils.readFile("./user/user.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return userList;
	}
	public static void main(String[] args) {
		for(User user:InitSingon.userList){
			System.out.println(user);
		}
	}
}