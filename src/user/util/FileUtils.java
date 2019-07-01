package user.util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import user.init.InitSingon;
import user.vo.User;

/*
 * 这个文件主要用于user文件里里面用户信息的操作
 */
public class FileUtils {
	public FileUtils() {

	}
	/*
	 * 创建path下面的一个目录
	 */
	@SuppressWarnings("unused")
	public Boolean CreateDirectory(String path) {
		File file = new File(path);
		if (file != null) {
			file.mkdirs();
			return true;
		}
		return false;
	}
	/*
	 * 创建路径为path的文件
	 */
	@SuppressWarnings("unused")
	public Boolean CreateFile(String path) {
		File file = new File(path);
		if (file != null) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
	/*
	 * 读取在path路径下面的文件中的内容封装成对象并加入list
	 */
	public List<User> readFile(String path) throws IOException {
		BufferedReader br = null;
		List<User> userList = new ArrayList<User>();
		List<String> userString = new ArrayList<String>();
		try {
			br = new BufferedReader(new FileReader(new File(path)));
			String result = null;
			// 为了防止最后一行的"\n"导致的数组下标越界
			while ((result = br.readLine()) != null) {
				userString.add(result);
			}
			if (userString.size() != 0) {
				// System.out.println(userString.size());
				for (int i = 0; i < userString.size(); i++) {
					String[] info = userString.get(i).split(":");
					User user = new User();
					user.setIsAdmin(Boolean.parseBoolean(info[1]));
					user.setUserId(Integer.valueOf(info[3]));
					user.setUserName(info[5]);
					user.setUserPassword(info[7]);
					userList.add(user);
				}
				return userList;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/*
	 * 向相应的list的对象的内容写入文件中
	 */
	public Boolean writeFile(List<User> userList) {
		// 将文件清空
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File("./user/user.txt"));
			fw.write("");

		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				fw.close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		for (User user : InitSingon.userList) {
			System.out.println("传递成功的用户" + user);
		}
		// 向文件中重新写入
		for (User user : InitSingon.userList) {
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(new File(
								"./user/user.txt"), true)));
				bw.write("isAdmin:" + user.getIsAdmin() + ":" + "userId:"
						+ user.getUserId() + ":" + "userName:"
						+ user.getUserName() + ":" + "userPassword:"
						+ user.getUserPassword() + "\n");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
}
// int i = 0;
// String path = "./user/user.txt";
// 这里的try{}catch{}放到for的外面只能写进去一条，因为一共只用一次操作
/*
 * 有问题是因为register再一次对InitSingon里面的userList循环操作了 Service service = new Service();
 * for(User user:InitSingon.userList){ System.out.println(user);
 * service.register(user); }
 */