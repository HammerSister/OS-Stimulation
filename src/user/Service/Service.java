package user.Service;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.swing.JOptionPane;

import user.init.InitSingon;
import user.init.InitThread;
import user.thread.UserThread;
import user.util.FileUtils;
import user.vo.User;

/*
 * ��װ�û���CRUD
 */
//����Ҫ����һ����ʼ����init
public class Service {
	List<User> userList = null;
	//1,�û�ע��
	//�����������ǰ����Id
	public Boolean register(User user){
		//Ϊע���user�ú͵�id��Ȩ�޸�ֵ
		List<User> userList = InitSingon.userList;
		if(userList != null){
			Integer id = userList.get(userList.size()-1).getUserId()+1;
			user.setUserId(id);
			user.setIsAdmin(false);
		}
	     //���ļ�д��ͬʱд���ڴ�init
		BufferedWriter bw = null;
			try {
				InitSingon.userList.add(user);
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("./user/user.txt"),true)));
				bw.write("isAdmin:"+user.getIsAdmin()+":"+"userId:"+user.getUserId()+":"+"userName:"+user.getUserName()+":"+"userPassword:"+user.getUserPassword()+"\n");
				return true;
			} catch (IOException e){
				e.printStackTrace();
			}finally{
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	       return false;
	}
	//2,ϵͳ��½ʱ��֤
	public User validate(String userName,String password) throws IOException{
		//�ж���ͨ���û��Ƿ�Ϸ�
		List<User> userList = InitSingon.userList;
		for(User user:userList){
			if(user.getUserName().equals(userName)&&user.getUserPassword().equals(password)){
				//if(InitThread.threadList.isEmpty())
				//	return user;
				//else {
					UserThread loginThread=new UserThread(user.getUserId(),user.getUserName());
					if(InitThread.threadList.contains(loginThread)) {
						JOptionPane.showMessageDialog(null, "�û��ѵ�¼��");
					}
					else
						return user;
				//}
			}
		}
		return null;
	}
	//�޸��û���Ϣ
	/*public Boolean updateUser(User user){
	   for(int i=0;i<InitSingon.userList.size();i++){
		   if(InitSingon.userList.get(i).getUserId()==user.getUserId()){
			   //��������
			   InitSingon.userList.set(i, user);
			   //д���ļ�
			   new FileUtils().writeFile(InitSingon.userList);
			   return true;
		   }
	   }
	   return false;
	}*/
	//ɾ���û���Ϣ
	public Boolean deleteUser(User user){
		for(int i=0;i<InitSingon.userList.size();i++){
			if(InitSingon.userList.get(i).getUserId().equals(user.getUserId())){
				InitSingon.userList.remove(i);
				//�û�����
			    //InitThread.threadList.remove(i);
				for(User user1:InitSingon.userList){
					System.out.println("ʣ���û�"+user1);
				}
				new FileUtils().writeFile(InitSingon.userList);
				return true;
			}
		}
		return false;	
	}
	//�����û�����ѯ�û�
    public  User findUserByName(String userName){
    	for(User user:InitSingon.userList){
    		if(user.getUserName().equals(userName)){
    			return user;
    		}
    	}
    	return null;
    }
}