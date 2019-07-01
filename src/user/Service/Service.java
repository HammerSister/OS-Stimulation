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
 * 封装用户的CRUD
 */
//这里要利用一个初始化的init
public class Service {
	List<User> userList = null;
	//1,用户注册
	//还必须读出当前最大的Id
	public Boolean register(User user){
		//为注册的user用和的id和权限赋值
		List<User> userList = InitSingon.userList;
		if(userList != null){
			Integer id = userList.get(userList.size()-1).getUserId()+1;
			user.setUserId(id);
			user.setIsAdmin(false);
		}
	     //向文件写入同时写入内存init
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
	//2,系统登陆时验证
	public User validate(String userName,String password) throws IOException{
		//判断普通的用户是否合法
		List<User> userList = InitSingon.userList;
		for(User user:userList){
			if(user.getUserName().equals(userName)&&user.getUserPassword().equals(password)){
				//if(InitThread.threadList.isEmpty())
				//	return user;
				//else {
					UserThread loginThread=new UserThread(user.getUserId(),user.getUserName());
					if(InitThread.threadList.contains(loginThread)) {
						JOptionPane.showMessageDialog(null, "用户已登录！");
					}
					else
						return user;
				//}
			}
		}
		return null;
	}
	//修改用户信息
	/*public Boolean updateUser(User user){
	   for(int i=0;i<InitSingon.userList.size();i++){
		   if(InitSingon.userList.get(i).getUserId()==user.getUserId()){
			   //更新数据
			   InitSingon.userList.set(i, user);
			   //写入文件
			   new FileUtils().writeFile(InitSingon.userList);
			   return true;
		   }
	   }
	   return false;
	}*/
	//删除用户信息
	public Boolean deleteUser(User user){
		for(int i=0;i<InitSingon.userList.size();i++){
			if(InitSingon.userList.get(i).getUserId().equals(user.getUserId())){
				InitSingon.userList.remove(i);
				//用户掉线
			    //InitThread.threadList.remove(i);
				for(User user1:InitSingon.userList){
					System.out.println("剩余用户"+user1);
				}
				new FileUtils().writeFile(InitSingon.userList);
				return true;
			}
		}
		return false;	
	}
	//根据用户名查询用户
    public  User findUserByName(String userName){
    	for(User user:InitSingon.userList){
    		if(user.getUserName().equals(userName)){
    			return user;
    		}
    	}
    	return null;
    }
}