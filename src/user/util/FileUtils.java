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
 * ����ļ���Ҫ����user�ļ��������û���Ϣ�Ĳ���
 */
public class FileUtils {
	public FileUtils() {

	}
	/*
	 * ����path�����һ��Ŀ¼
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
	 * ����·��Ϊpath���ļ�
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
	 * ��ȡ��path·��������ļ��е����ݷ�װ�ɶ��󲢼���list
	 */
	public List<User> readFile(String path) throws IOException {
		BufferedReader br = null;
		List<User> userList = new ArrayList<User>();
		List<String> userString = new ArrayList<String>();
		try {
			br = new BufferedReader(new FileReader(new File(path)));
			String result = null;
			// Ϊ�˷�ֹ���һ�е�"\n"���µ������±�Խ��
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
	 * ����Ӧ��list�Ķ��������д���ļ���
	 */
	public Boolean writeFile(List<User> userList) {
		// ���ļ����
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
			System.out.println("���ݳɹ����û�" + user);
		}
		// ���ļ�������д��
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
// �����try{}catch{}�ŵ�for������ֻ��д��ȥһ������Ϊһ��ֻ��һ�β���
/*
 * ����������Ϊregister��һ�ζ�InitSingon�����userListѭ�������� Service service = new Service();
 * for(User user:InitSingon.userList){ System.out.println(user);
 * service.register(user); }
 */