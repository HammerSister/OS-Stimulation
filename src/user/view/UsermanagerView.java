package user.view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import user.Service.Service;
import user.init.InitSingon;
import user.vo.User;

public class UsermanagerView {
	//当前登陆用户信息
	private User userGloble;
	//利用list保存当前在在线的其它的用户,这里是AWT包的list
    @SuppressWarnings("rawtypes")
	private JList onLineList = null;
    private JFrame jFrame = null;
    private JPanel jPanel = null;
    private JLabel jLabel = null;
    //设定右键菜单
    @SuppressWarnings({ "rawtypes", "unused" })
	private JComboBox box = null;
    //定义右键菜单
    private JPopupMenu rightmenu = null;
    //初始化
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	public UsermanagerView(User user){
		this.userGloble = user;
		onLineList = new JList();
		jFrame = new JFrame("系统用户管理器");
		jPanel = new JPanel();
		jLabel = new JLabel();
		jFrame.setBounds(100,100,300,350);
		jPanel.setBounds(100,100,300,350);
		java.util.List<String> resultList = new ArrayList<String>();
		//初始化list中的数据
		for(int i=0;i<InitSingon.userList.size();i++){
			if(this.userGloble.getUserId().equals(InitSingon.userList.get(i).getUserId())){
				continue;
			}
		    resultList.add(InitSingon.userList.get(i).getUserName());
		}
		//初始化右键按钮数据
		Icon button1 = new ImageIcon("./src/images/menu_add.gif");
		Icon button2 = new ImageIcon("./src/images/menu_del.gif");
		//初始化按钮中的数据
		rightmenu = new JPopupMenu();
		JMenuItem jm1 = new JMenuItem("新建用户",button1);
		JMenuItem jm2 = new JMenuItem("删除用户",button2);
		//添加到右键菜单容器
		rightmenu.add(jm1);
		rightmenu.add(jm2);
		//将除了当前用户的该用户名写入到list中
		onLineList.setListData(resultList.toArray());
		onLineList.setFixedCellHeight(30);
		onLineList.setFixedCellWidth(290);
		onLineList.setSelectionForeground(Color.PINK);
		onLineList.setVisible(true);
		onLineList.setBackground(Color.PINK);
	    jLabel = new JLabel("当前系统中的用户如下：");
	    onLineList.setComponentPopupMenu(rightmenu);
	    //向面板中添加组件
		jPanel.add(onLineList);
		//主窗口的添加监听
		//设定用户添加监听begin
		jm1.addActionListener(new ActionListener() {
			JTextField newUserName = null;
	    	JPasswordField userPassword = null;
	    	JPasswordField apassword = null;
	    	JButton submit = new JButton("新建");
	    	JButton reset = new JButton("重置");
	    	JLabel userJLabel = null;
	    	JLabel passwordJLabel = null;
	    	JLabel aPasswordLable = null;
	    	JFrame newUserFrame = null;
	    	JPanel newUserPanel = null;
	    	User user = null;
			public void actionPerformed(ActionEvent e){
				newUserFrame = new JFrame("新建用户");
				newUserPanel = new JPanel();
				newUserPanel.setLayout(null);
				userJLabel = new JLabel("用户名：");
				userJLabel.setBounds(20,20,100,30);
			    newUserName = new JTextField();
				newUserName.setBounds(140,20,100,25);
				passwordJLabel = new JLabel("密码：");
				passwordJLabel.setBounds(20,65,100,30);
				userPassword = new JPasswordField();
				userPassword.setBounds(140,65,100,25);
				aPasswordLable = new JLabel("再次输入密码：");
				aPasswordLable.setBounds(20,110,100,30);
				apassword = new JPasswordField();
				apassword.setBounds(140,110,100,25);
				submit.setText("新建");
				submit.setBounds(65,155,60,25);
				reset.setText("重置");
				reset.setBounds(135,155,60,25);
				//面板添加
				newUserPanel.add(userJLabel);
				newUserPanel.add(newUserName);
				newUserPanel.add(passwordJLabel);
				newUserPanel.add(userPassword);
				newUserPanel.add(aPasswordLable);
				newUserPanel.add(apassword);
				newUserPanel.add(submit);
				newUserPanel.add(reset);
				//子窗体添加监听
				//注册监听
				submit.addActionListener(new ActionListener() {
					@SuppressWarnings("deprecation")
					public void actionPerformed(ActionEvent e) {
						user = new User();
						user.setUserName(newUserName.getText());
						if(!userPassword.getText().equals(apassword.getText())){
							//弹出提示框
							JOptionPane.showMessageDialog(newUserPanel,"两个密码不相同！");
							apassword.setText("");
						}else{
							user.setUserPassword(userPassword.getText());
							Service service = new Service();
							if(service.register(user)){
								JOptionPane.showMessageDialog(newUserPanel,"新建成功！");
								newUserFrame.dispose();
								jFrame.dispose();
								//刷新页面
								new UsermanagerView(userGloble); 
							}
						}
					}
				});
				//重置监听
				reset.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
				    	newUserName.setText("");
				    	userPassword.setText("");
				    	apassword.setText("");
					}
				});
				//子窗体添加
				newUserFrame.setBounds(150,150,260,230);
				newUserFrame.add(newUserPanel);
				newUserPanel.setVisible(true);
				newUserFrame.setVisible(true);
				newUserFrame.setResizable(false);
			}
		});
		//设定用户添加监听end
		//设定用户删除监听begin
		jm2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//拿到选中用户的用户名
				String delUserName  = (String)onLineList.getSelectedValue();
				//查到这个用户
				User user  = new Service().findUserByName(delUserName);
				if(new Service().deleteUser(user)){
					String delMessage = user.getUserName()+"删除成功";
					JOptionPane.showMessageDialog(jPanel,delMessage);
					//进行页面的刷新
					jFrame.dispose();
					//刷新页面
					new UsermanagerView(userGloble);
				}
			}
		});
		//主窗口添加
	    jFrame.add(jLabel,new BorderLayout().NORTH);
		jFrame.add(jPanel);
		jFrame.setResizable(false);
		jFrame.setVisible(true);
	}
}