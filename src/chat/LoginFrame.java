package chat;
import java.awt.event.*;
import javax.swing.*;

//��ʾ��¼����
public class LoginFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	//������ؼ�
	JFrame frame=this;
	JPanel jpl=new JPanel();
	private Icon welcomeIcon=new ImageIcon("welcome.png");
	private JLabel lbWelcome=new JLabel(welcomeIcon);
	private JLabel lbAccount=new JLabel("���������˺�");
	private JTextField tfAccount=new JTextField(30);
	private JLabel lbPassword=new JLabel("������������");
	private JPasswordField pfPassword=new JPasswordField(30);
	private JButton btLogin=new JButton("��¼");
	private JButton btExit=new JButton("�˳�");
	public LoginFrame(){
		super("��¼");
		//jpl.setLayout(new FlowLayout());
		jpl.setBounds(0,0,480,340);
		jpl.setLayout(null);
		lbWelcome.setBounds(10,10,450,150);
		lbAccount.setBounds(100,190,120,25);
		tfAccount.setBounds(240,190,150,25);
		lbPassword.setBounds(100,230,120,25);
		pfPassword.setBounds(240,230,150,25);
		btLogin.setBounds(150,270,80,25);
		btExit.setBounds(250,270,80,25);
		jpl.add(lbWelcome);
		jpl.add(lbAccount);
		jpl.add(tfAccount);
		jpl.add(lbPassword);
		jpl.add(pfPassword);
		jpl.add(btLogin);
		jpl.add(btExit);
		this.add(jpl);
		this.setSize(480, 340);
		this.setLocation(200, 200);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowOpe());
		this.setResizable(false);
		this.setVisible(true);
		//���Ӽ���
		btLogin.addActionListener(this);
		btExit.addActionListener(this);
	}
	public void login() {
		try{
			String account=tfAccount.getText();
			String password=new String(pfPassword.getPassword());
			FileOpe.getInfoByAccount(account);
			if(Conf.account==null||Conf.password==null) {
				JOptionPane.showMessageDialog(this, "�˺����������Ϊ��");
				return;
			}else if(!Conf.account.equals(account)||!Conf.password.equals(password)) {
				JOptionPane.showMessageDialog(this, "�˺Ż��������");
				return;
			}
			JOptionPane.showMessageDialog(this, "��¼�ɹ�");
			new Client();
			this.dispose();
		}catch(Exception ex) {
			JOptionPane.showMessageDialog(this, "���������쳣");
			System.exit(-1);
		}
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btLogin){
			this.login();
		}else {
			frame.dispose();
		}
	}
	class WindowOpe extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			int result=JOptionPane.showConfirmDialog(null,"ȷ���˳������ң�","ȷ��",JOptionPane.YES_NO_OPTION);
			if(result==JOptionPane.YES_OPTION) {
				frame.dispose();
			}
		}
	}
}