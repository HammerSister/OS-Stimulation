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
	//��ǰ��½�û���Ϣ
	private User userGloble;
	//����list���浱ǰ�����ߵ��������û�,������AWT����list
    @SuppressWarnings("rawtypes")
	private JList onLineList = null;
    private JFrame jFrame = null;
    private JPanel jPanel = null;
    private JLabel jLabel = null;
    //�趨�Ҽ��˵�
    @SuppressWarnings({ "rawtypes", "unused" })
	private JComboBox box = null;
    //�����Ҽ��˵�
    private JPopupMenu rightmenu = null;
    //��ʼ��
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	public UsermanagerView(User user){
		this.userGloble = user;
		onLineList = new JList();
		jFrame = new JFrame("ϵͳ�û�������");
		jPanel = new JPanel();
		jLabel = new JLabel();
		jFrame.setBounds(100,100,300,350);
		jPanel.setBounds(100,100,300,350);
		java.util.List<String> resultList = new ArrayList<String>();
		//��ʼ��list�е�����
		for(int i=0;i<InitSingon.userList.size();i++){
			if(this.userGloble.getUserId().equals(InitSingon.userList.get(i).getUserId())){
				continue;
			}
		    resultList.add(InitSingon.userList.get(i).getUserName());
		}
		//��ʼ���Ҽ���ť����
		Icon button1 = new ImageIcon("./src/images/menu_add.gif");
		Icon button2 = new ImageIcon("./src/images/menu_del.gif");
		//��ʼ����ť�е�����
		rightmenu = new JPopupMenu();
		JMenuItem jm1 = new JMenuItem("�½��û�",button1);
		JMenuItem jm2 = new JMenuItem("ɾ���û�",button2);
		//��ӵ��Ҽ��˵�����
		rightmenu.add(jm1);
		rightmenu.add(jm2);
		//�����˵�ǰ�û��ĸ��û���д�뵽list��
		onLineList.setListData(resultList.toArray());
		onLineList.setFixedCellHeight(30);
		onLineList.setFixedCellWidth(290);
		onLineList.setSelectionForeground(Color.PINK);
		onLineList.setVisible(true);
		onLineList.setBackground(Color.PINK);
	    jLabel = new JLabel("��ǰϵͳ�е��û����£�");
	    onLineList.setComponentPopupMenu(rightmenu);
	    //�������������
		jPanel.add(onLineList);
		//�����ڵ���Ӽ���
		//�趨�û���Ӽ���begin
		jm1.addActionListener(new ActionListener() {
			JTextField newUserName = null;
	    	JPasswordField userPassword = null;
	    	JPasswordField apassword = null;
	    	JButton submit = new JButton("�½�");
	    	JButton reset = new JButton("����");
	    	JLabel userJLabel = null;
	    	JLabel passwordJLabel = null;
	    	JLabel aPasswordLable = null;
	    	JFrame newUserFrame = null;
	    	JPanel newUserPanel = null;
	    	User user = null;
			public void actionPerformed(ActionEvent e){
				newUserFrame = new JFrame("�½��û�");
				newUserPanel = new JPanel();
				newUserPanel.setLayout(null);
				userJLabel = new JLabel("�û�����");
				userJLabel.setBounds(20,20,100,30);
			    newUserName = new JTextField();
				newUserName.setBounds(140,20,100,25);
				passwordJLabel = new JLabel("���룺");
				passwordJLabel.setBounds(20,65,100,30);
				userPassword = new JPasswordField();
				userPassword.setBounds(140,65,100,25);
				aPasswordLable = new JLabel("�ٴ��������룺");
				aPasswordLable.setBounds(20,110,100,30);
				apassword = new JPasswordField();
				apassword.setBounds(140,110,100,25);
				submit.setText("�½�");
				submit.setBounds(65,155,60,25);
				reset.setText("����");
				reset.setBounds(135,155,60,25);
				//������
				newUserPanel.add(userJLabel);
				newUserPanel.add(newUserName);
				newUserPanel.add(passwordJLabel);
				newUserPanel.add(userPassword);
				newUserPanel.add(aPasswordLable);
				newUserPanel.add(apassword);
				newUserPanel.add(submit);
				newUserPanel.add(reset);
				//�Ӵ�����Ӽ���
				//ע�����
				submit.addActionListener(new ActionListener() {
					@SuppressWarnings("deprecation")
					public void actionPerformed(ActionEvent e) {
						user = new User();
						user.setUserName(newUserName.getText());
						if(!userPassword.getText().equals(apassword.getText())){
							//������ʾ��
							JOptionPane.showMessageDialog(newUserPanel,"�������벻��ͬ��");
							apassword.setText("");
						}else{
							user.setUserPassword(userPassword.getText());
							Service service = new Service();
							if(service.register(user)){
								JOptionPane.showMessageDialog(newUserPanel,"�½��ɹ���");
								newUserFrame.dispose();
								jFrame.dispose();
								//ˢ��ҳ��
								new UsermanagerView(userGloble); 
							}
						}
					}
				});
				//���ü���
				reset.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
				    	newUserName.setText("");
				    	userPassword.setText("");
				    	apassword.setText("");
					}
				});
				//�Ӵ������
				newUserFrame.setBounds(150,150,260,230);
				newUserFrame.add(newUserPanel);
				newUserPanel.setVisible(true);
				newUserFrame.setVisible(true);
				newUserFrame.setResizable(false);
			}
		});
		//�趨�û���Ӽ���end
		//�趨�û�ɾ������begin
		jm2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//�õ�ѡ���û����û���
				String delUserName  = (String)onLineList.getSelectedValue();
				//�鵽����û�
				User user  = new Service().findUserByName(delUserName);
				if(new Service().deleteUser(user)){
					String delMessage = user.getUserName()+"ɾ���ɹ�";
					JOptionPane.showMessageDialog(jPanel,delMessage);
					//����ҳ���ˢ��
					jFrame.dispose();
					//ˢ��ҳ��
					new UsermanagerView(userGloble);
				}
			}
		});
		//���������
	    jFrame.add(jLabel,new BorderLayout().NORTH);
		jFrame.add(jPanel);
		jFrame.setResizable(false);
		jFrame.setVisible(true);
	}
}