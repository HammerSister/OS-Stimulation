package user.view;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import user.vo.User;
import user.init.InitThread;
import user.thread.UserThread;
import file.server.FilesysServer;
import file.view.FilesysPanel;
import process.view.MainFrame;
import tasklist.TaskList;

public class UsermenuView extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	JFrame frame=this;
	JButton jb[];
	FilesysServer fss;
	User user;
	int sw=(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	int sh=(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	public UsermenuView(FilesysServer fss,User user){
		this.fss=fss;
		this.user=user;
		this.setLayout(null);
		JLabel jl=new JLabel("欢迎进入XXX虚拟操作系统,  "+user.getUserName()+"  !");
		jl.setBounds(30,20,450,40);
		jl.setFont(new Font("微软雅黑",Font.BOLD,25));
		this.add(jl);
		Icon userIcon=new ImageIcon("pics/user.jpg");
		JLabel lbIcon1=new JLabel(userIcon);
		Icon fileIcon=new ImageIcon("pics/file.jpg");
		JLabel lbIcon2=new JLabel(fileIcon);
		Icon processIcon=new ImageIcon("pics/process.jpg");
		JLabel lbIcon3=new JLabel(processIcon);
		Icon systemIcon=new ImageIcon("pics/system.jpg");
		JLabel lbIcon4=new JLabel(systemIcon);
		jb=new JButton[4];
		jb[0]=new JButton("用户管理");
		jb[1]=new JButton("文件管理");
		jb[2]=new JButton("进程管理");
		jb[3]=new JButton("系统管理");
		lbIcon1.setBounds(160, 100, 40, 40);
		lbIcon2.setBounds(160, 160, 40, 40);
		lbIcon3.setBounds(160, 220, 40, 40);
		lbIcon4.setBounds(160, 280, 40, 40);
		this.add(lbIcon1);
		this.add(lbIcon2);
		this.add(lbIcon3);
		this.add(lbIcon4);
		for(int i=0;i<4;i++){
			jb[i].addActionListener(this);
			jb[i].setBounds(225, 100+i*60, 130, 40);
			this.add(jb[i]);
		}
		this.addWindowListener(new WindowOpe());
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setSize(500,400);
		this.setLocation(sw/2-250, sh/2-200);
		this.setVisible(true);
		this.setTitle("XXX虚拟操作系统");
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("用户管理")) {
			new UserView(user);
		    for(UserThread userT:InitThread.threadList){
				System.out.println(userT);
			}
		}else if(e.getActionCommand().equals("文件管理")){
			new FilesysPanel(fss,user);
		}else if(e.getActionCommand().equals("进程管理")){
			new MainFrame();
		}else if(e.getActionCommand().equals("系统管理")){
			new TaskList();
		}
	}
	class WindowOpe extends WindowAdapter {
		@SuppressWarnings("unlikely-arg-type")
		public void windowClosing(WindowEvent e) {
			int result=JOptionPane.showConfirmDialog(null,"确认退出登录？","确认",JOptionPane.YES_NO_OPTION);
			if(result==JOptionPane.YES_OPTION) {
				InitThread.threadList.remove(user.getUserId());
				frame.dispose();
			}
		}
	}
}