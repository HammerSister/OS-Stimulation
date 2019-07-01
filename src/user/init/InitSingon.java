package user.init;
import java.io.IOException;
import java.util.List;
import user.util.FileUtils;
import user.vo.User;

//user�ļ������ݻ��浽userList����
public class InitSingon {
	private static InitSingon init = new InitSingon();
	@SuppressWarnings("static-access")
	public static List<User> userList = InitSingon.init.getUserList();
	private InitSingon(){
		
	}
	public static InitSingon instance(){
		return  init;
	}
	//������������ϵͳ��Ҳ��һ��
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