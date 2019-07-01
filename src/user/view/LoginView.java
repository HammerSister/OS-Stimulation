package user.view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import file.server.FilesysServer;
import user.Service.Service;
import user.init.InitThread;
import user.thread.UserThread;
import user.vo.User;

/*
 * 常用的布局管理器
 * 1.容器组件具有普通组件的一切的特性
 * 2.容器还具有承装其他组件的功能
 * 3,在BorderLayout的布局管理器中，默认的添加到Center
 */
public class LoginView extends JFrame{
	private static final long serialVersionUID = 1L;
	JFrame jframe = new  JFrame("欢迎登录XXX虚拟操作系统");
	private FilesysServer fss1= null;
	int sw=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    int sh=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	public LoginView(FilesysServer fss){
		this.fss1 = fss;
		//初始化面板
	    JPanel loginPanel = new  JPanel();
	    JLabel titleJLable = new JLabel();
	    titleJLable.setText("欢迎登录XXX虚拟操作系统");
	    titleJLable.setSize(500,200);
	    loginPanel.setBackground(Color.lightGray);
	    titleJLable.setBackground(Color.blue);
	    loginPanel.add(titleJLable);
	    //用户面板
	    JPanel userPanel = new JPanel();
	    userPanel.setLayout(null);
	    JButton adminButton = new JButton();
	    adminButton.setText("管理员登录");
	    JButton userButton = new JButton();
	    userButton.setText("普通用户登录");
	    adminButton.setBounds(sw/2-150,520,140,40);
	    userButton.setBounds(sw/2+20,520,140,40);
	    //设置背景图片
	    Icon icon=new ImageIcon("pics/windows10.jpg");
	    JLabel bg=new JLabel(icon);
	    bg.setBounds(0,0,sw,sh);
	    userPanel.add(adminButton);
	    userPanel.add(userButton);
	    userPanel.add(bg);
	    jframe.setLayout(new BorderLayout());
	    jframe.setSize(sw, sh);
	    jframe.setLocation(0, 0);
	    jframe.add(loginPanel,BorderLayout.NORTH);
	    jframe.add(userPanel,BorderLayout.CENTER);
	    //事件监听
	    adminButton.addActionListener(new ActionListener() {
	    	JTextField adminName = null;
	    	JPasswordField password = null;
	    	JFrame adminFrame = null;
	    	User user = null; 
	    	/*
	    	 * 管理员登陆模块
	    	 */
			public void actionPerformed(ActionEvent e) {
				//管理员容器设置
				adminFrame = new JFrame("管理员登录");
				JPanel adminInfoPanel = new JPanel();
				adminInfoPanel.setLayout(null);
				adminFrame.setResizable(false);
				adminFrame.setLocation(sw/2-200,sh/2-195);
				adminFrame.setSize(400,390);
				adminFrame.add(adminInfoPanel);
				adminInfoPanel.setSize(400,390);
				//管理员组件设置
				Icon adminIcon=new ImageIcon("pics/login.jpg");
				JLabel lbIcon1=new JLabel(adminIcon);
				JLabel adminLable = new JLabel("用户名：");
		        adminName = new JTextField();
			    JLabel pwLable = new JLabel("密码：");
			    password = new JPasswordField();
			    JButton submitButton = new JButton("登录");
			    JButton resetButton  = new JButton("重置");
			    lbIcon1.setBounds(95,10,210,210);
			    adminLable.setBounds(70,240,60,25);
			    pwLable.setBounds(70,275,60,25);
			    adminName.setBounds(140,240,200,25);
			    password.setBounds(140,275,200,25);
			    submitButton.setBounds(110,315,80,25);
			    resetButton.setBounds(210,315,80,25);
				//管理员组件添加
			    adminInfoPanel.add(lbIcon1);
				adminInfoPanel.add(adminLable);
				adminInfoPanel.add(adminName);
			    adminInfoPanel.add(pwLable);
			    adminInfoPanel.add(password);
			    adminInfoPanel.add(submitButton);
			    adminInfoPanel.add(resetButton);
			    //管理员重置按钮监听
			    resetButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e){
						adminName.setText("");
						password.setText("");
					}
				});
			    //管理员登陆按钮监听
			    submitButton.addActionListener(new ActionListener(){
					@SuppressWarnings("deprecation")
					public void actionPerformed(ActionEvent e){
						Service service = new Service();
						//判断登陆是否成功
						try {
							user  = service.validate(adminName.getText(), password.getText());
						} catch (IOException e1){
							e1.printStackTrace();
						}
						//系统管理员不可以有多个
						if(user != null){
							//系统管理员向系统进行注册
							//如果当前用户登陆成功,就为当前的用户设定一个线程,注册用户
							UserThread userThread = new UserThread(user.getUserId(),user.getUserName());
							//向系统通知当前已经在线
							InitThread.threadList.add(userThread);
							adminFrame.dispose();	
							//用户管理模块入口,完成后加入到系统文件入口
							new UsermenuView(fss1, user);
						}else{
							JOptionPane.showMessageDialog(null,"用户名或密码错误");
							adminFrame.dispose();
						}
					}
				});
	            adminFrame.setAlwaysOnTop(true);
				adminFrame.setVisible(true);
			}
		});
	    /*
	     * 用户登陆模块
	     */
	    userButton.addActionListener(new ActionListener() {
	    	 //用户名
	    	 JTextField userName = null;
	    	 //密码
	    	 JPasswordField password = null;
	    	 JFrame userFrame = null;
			public void actionPerformed(ActionEvent e){
				//用户容器设置
				userFrame = new JFrame("普通用户登录");
				JPanel userInfoPanel = new JPanel();
				userInfoPanel.setLayout(null);
				userFrame.setResizable(false);
				userFrame.setLocation(sw/2-200,sh/2-195);
				userFrame.setSize(400,390);
				userFrame.add(userInfoPanel);
				userInfoPanel.setSize(400,390);
				//用户组件设置
				Icon userIcon=new ImageIcon("pics/login.jpg");
				JLabel lbIcon2=new JLabel(userIcon);
				JLabel userLable = new JLabel("用户名：");
		        userName = new JTextField();
			    JLabel pwLable = new JLabel("密码：");
			    password = new JPasswordField();
			    JButton submitButton = new JButton("登录");
			    JButton resetButton  = new JButton("重置");
			    //定位			    
			    lbIcon2.setBounds(95,10,210,210);
			    userLable.setBounds(70,240,60,25);
			    pwLable.setBounds(70,275,60,25);
			    userName.setBounds(140,240,200,25);
			    password.setBounds(140,275,200,25);
			    submitButton.setBounds(110,315,80,25);
			    resetButton.setBounds(210,315,80,25);
				//用户组件添加
			    userInfoPanel.add(lbIcon2);
			    userInfoPanel.add(userLable);
			    userInfoPanel.add(userName);
			    userInfoPanel.add(pwLable);
			    userInfoPanel.add(password);
			    userInfoPanel.add(submitButton);
			    userInfoPanel.add(resetButton);
			    userFrame.setAlwaysOnTop(true);
			    userFrame.setVisible(true);
			    /*
			     * 事件监听
			     */
			    //用户重置按钮监听
			    resetButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						userName.setText("");
						password.setText("");
					}
				});
			    //用户登录按钮监听
			    submitButton.addActionListener(new ActionListener(){
					@SuppressWarnings("deprecation")
					public void actionPerformed(ActionEvent e){
						Service service = new Service();
						//判断登陆是否成功
						User user = null; 
						try {
							user  = service.validate(userName.getText(), password.getText());
						} catch (IOException e1){
							e1.printStackTrace();
						}
						if(user != null){
							//如果当前用户登陆成功,就为当前的用户设定一个线程,注册用户
							UserThread userThread = new UserThread(user.getUserId(),user.getUserName());
							//向系统通知当前已经在线
							InitThread.threadList.add(userThread);
							//显示当前在线的用户的接口，完成放入普通用户文件接口
							userFrame.dispose();
							//用户管理模块入口,完成后加入到系统文件入口
							new UsermenuView(fss1, user);
						}else{
							JOptionPane.showMessageDialog(null,"用户名或密码错误");
							userFrame.dispose();
						}
					}
				});
			 }
		});
	    jframe.addWindowListener(new WindowOpe());
	    jframe.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	    jframe.setVisible(true);
	 }
	class WindowOpe extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			int result=JOptionPane.showConfirmDialog(null,"确认退出系统？","确认",JOptionPane.YES_NO_OPTION);
			if(result==JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}
	}
}