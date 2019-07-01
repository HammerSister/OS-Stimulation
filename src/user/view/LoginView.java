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
 * ���õĲ��ֹ�����
 * 1.�������������ͨ�����һ�е�����
 * 2.���������г�װ��������Ĺ���
 * 3,��BorderLayout�Ĳ��ֹ������У�Ĭ�ϵ���ӵ�Center
 */
public class LoginView extends JFrame{
	private static final long serialVersionUID = 1L;
	JFrame jframe = new  JFrame("��ӭ��¼XXX�������ϵͳ");
	private FilesysServer fss1= null;
	int sw=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    int sh=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	public LoginView(FilesysServer fss){
		this.fss1 = fss;
		//��ʼ�����
	    JPanel loginPanel = new  JPanel();
	    JLabel titleJLable = new JLabel();
	    titleJLable.setText("��ӭ��¼XXX�������ϵͳ");
	    titleJLable.setSize(500,200);
	    loginPanel.setBackground(Color.lightGray);
	    titleJLable.setBackground(Color.blue);
	    loginPanel.add(titleJLable);
	    //�û����
	    JPanel userPanel = new JPanel();
	    userPanel.setLayout(null);
	    JButton adminButton = new JButton();
	    adminButton.setText("����Ա��¼");
	    JButton userButton = new JButton();
	    userButton.setText("��ͨ�û���¼");
	    adminButton.setBounds(sw/2-150,520,140,40);
	    userButton.setBounds(sw/2+20,520,140,40);
	    //���ñ���ͼƬ
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
	    //�¼�����
	    adminButton.addActionListener(new ActionListener() {
	    	JTextField adminName = null;
	    	JPasswordField password = null;
	    	JFrame adminFrame = null;
	    	User user = null; 
	    	/*
	    	 * ����Ա��½ģ��
	    	 */
			public void actionPerformed(ActionEvent e) {
				//����Ա��������
				adminFrame = new JFrame("����Ա��¼");
				JPanel adminInfoPanel = new JPanel();
				adminInfoPanel.setLayout(null);
				adminFrame.setResizable(false);
				adminFrame.setLocation(sw/2-200,sh/2-195);
				adminFrame.setSize(400,390);
				adminFrame.add(adminInfoPanel);
				adminInfoPanel.setSize(400,390);
				//����Ա�������
				Icon adminIcon=new ImageIcon("pics/login.jpg");
				JLabel lbIcon1=new JLabel(adminIcon);
				JLabel adminLable = new JLabel("�û�����");
		        adminName = new JTextField();
			    JLabel pwLable = new JLabel("���룺");
			    password = new JPasswordField();
			    JButton submitButton = new JButton("��¼");
			    JButton resetButton  = new JButton("����");
			    lbIcon1.setBounds(95,10,210,210);
			    adminLable.setBounds(70,240,60,25);
			    pwLable.setBounds(70,275,60,25);
			    adminName.setBounds(140,240,200,25);
			    password.setBounds(140,275,200,25);
			    submitButton.setBounds(110,315,80,25);
			    resetButton.setBounds(210,315,80,25);
				//����Ա������
			    adminInfoPanel.add(lbIcon1);
				adminInfoPanel.add(adminLable);
				adminInfoPanel.add(adminName);
			    adminInfoPanel.add(pwLable);
			    adminInfoPanel.add(password);
			    adminInfoPanel.add(submitButton);
			    adminInfoPanel.add(resetButton);
			    //����Ա���ð�ť����
			    resetButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e){
						adminName.setText("");
						password.setText("");
					}
				});
			    //����Ա��½��ť����
			    submitButton.addActionListener(new ActionListener(){
					@SuppressWarnings("deprecation")
					public void actionPerformed(ActionEvent e){
						Service service = new Service();
						//�жϵ�½�Ƿ�ɹ�
						try {
							user  = service.validate(adminName.getText(), password.getText());
						} catch (IOException e1){
							e1.printStackTrace();
						}
						//ϵͳ����Ա�������ж��
						if(user != null){
							//ϵͳ����Ա��ϵͳ����ע��
							//�����ǰ�û���½�ɹ�,��Ϊ��ǰ���û��趨һ���߳�,ע���û�
							UserThread userThread = new UserThread(user.getUserId(),user.getUserName());
							//��ϵͳ֪ͨ��ǰ�Ѿ�����
							InitThread.threadList.add(userThread);
							adminFrame.dispose();	
							//�û�����ģ�����,��ɺ���뵽ϵͳ�ļ����
							new UsermenuView(fss1, user);
						}else{
							JOptionPane.showMessageDialog(null,"�û������������");
							adminFrame.dispose();
						}
					}
				});
	            adminFrame.setAlwaysOnTop(true);
				adminFrame.setVisible(true);
			}
		});
	    /*
	     * �û���½ģ��
	     */
	    userButton.addActionListener(new ActionListener() {
	    	 //�û���
	    	 JTextField userName = null;
	    	 //����
	    	 JPasswordField password = null;
	    	 JFrame userFrame = null;
			public void actionPerformed(ActionEvent e){
				//�û���������
				userFrame = new JFrame("��ͨ�û���¼");
				JPanel userInfoPanel = new JPanel();
				userInfoPanel.setLayout(null);
				userFrame.setResizable(false);
				userFrame.setLocation(sw/2-200,sh/2-195);
				userFrame.setSize(400,390);
				userFrame.add(userInfoPanel);
				userInfoPanel.setSize(400,390);
				//�û��������
				Icon userIcon=new ImageIcon("pics/login.jpg");
				JLabel lbIcon2=new JLabel(userIcon);
				JLabel userLable = new JLabel("�û�����");
		        userName = new JTextField();
			    JLabel pwLable = new JLabel("���룺");
			    password = new JPasswordField();
			    JButton submitButton = new JButton("��¼");
			    JButton resetButton  = new JButton("����");
			    //��λ			    
			    lbIcon2.setBounds(95,10,210,210);
			    userLable.setBounds(70,240,60,25);
			    pwLable.setBounds(70,275,60,25);
			    userName.setBounds(140,240,200,25);
			    password.setBounds(140,275,200,25);
			    submitButton.setBounds(110,315,80,25);
			    resetButton.setBounds(210,315,80,25);
				//�û�������
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
			     * �¼�����
			     */
			    //�û����ð�ť����
			    resetButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						userName.setText("");
						password.setText("");
					}
				});
			    //�û���¼��ť����
			    submitButton.addActionListener(new ActionListener(){
					@SuppressWarnings("deprecation")
					public void actionPerformed(ActionEvent e){
						Service service = new Service();
						//�жϵ�½�Ƿ�ɹ�
						User user = null; 
						try {
							user  = service.validate(userName.getText(), password.getText());
						} catch (IOException e1){
							e1.printStackTrace();
						}
						if(user != null){
							//�����ǰ�û���½�ɹ�,��Ϊ��ǰ���û��趨һ���߳�,ע���û�
							UserThread userThread = new UserThread(user.getUserId(),user.getUserName());
							//��ϵͳ֪ͨ��ǰ�Ѿ�����
							InitThread.threadList.add(userThread);
							//��ʾ��ǰ���ߵ��û��Ľӿڣ���ɷ�����ͨ�û��ļ��ӿ�
							userFrame.dispose();
							//�û�����ģ�����,��ɺ���뵽ϵͳ�ļ����
							new UsermenuView(fss1, user);
						}else{
							JOptionPane.showMessageDialog(null,"�û������������");
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
			int result=JOptionPane.showConfirmDialog(null,"ȷ���˳�ϵͳ��","ȷ��",JOptionPane.YES_NO_OPTION);
			if(result==JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}
	}
}