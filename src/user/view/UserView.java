package user.view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import chat.LoginFrame;
import chat.Server;
import user.init.InitThread;
import user.vo.User;

//��ͨ�û����Կ�����ǰ��½�������û�
public class UserView extends JFrame {
	private static final long serialVersionUID = 1L;
	//��ǰ��½�û���Ϣ
	private User userGloble = null;
	//����list���浱ǰ�����ߵ��������û�,������AWT����list
    @SuppressWarnings("rawtypes")
	private JList onLineList = null;
    private JFrame jFrame = null;
    private JPanel jPanel = null;
    private JLabel jLabel = null;
    private JButton jButton1 = null;
    private JButton jButton2 = null;
    //��ʼ��
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	public UserView(User user){
		this.userGloble = user;
		onLineList = new JList();
		jFrame = new JFrame("�û�������--�û� "+user.getUserName()+" ʹ����...");
		jPanel = new JPanel();
		jLabel = new JLabel("��ǰ�ѵ�¼�������û���");
		jButton1 = new JButton("�����û�");
		jButton2 = new JButton("����������");
		jButton1.setEnabled(false);
		jFrame.setBounds(0,0,400,400);
		jPanel.setBounds(0,0,400,400);
		java.util.List<String> resultList = new ArrayList<String>();
		onLineList.setSize(400,300);
		for(int i=0;i<InitThread.threadList.size();i++){
			if(user.getUserId() == InitThread.threadList.get(i).getThreadId()){
				continue;
			}
			System.out.println(InitThread.threadList.get(i));
			resultList.add(InitThread.threadList.get(i).getThreadName());
		}
		//�����˵�ǰ�û��ĸ��û���д�뵽list��
		if(resultList.size() == 0){
			String info = "��ǰ�������û���½ϵͳ";
			String info1[] = new String[1];
			info1[0]=info;
			onLineList.setListData(info1);
		}else{
			onLineList.setListData(resultList.toArray());
		}
		onLineList.setFixedCellHeight(30);
		onLineList.setFixedCellWidth(390);
		onLineList.setSelectionForeground(Color.PINK);
		onLineList.setVisible(true);
		jButton1.setBounds(90,320,100,30);
		jButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new UsermanagerView(userGloble);
			}
		});
		jButton2.setBounds(210,320,100,30);
		jButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(user.getIsAdmin())
					try {
						new Server();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				else
					new LoginFrame();
			}
		});
		if(user.getIsAdmin())
			jButton1.setEnabled(true);
		onLineList.setBackground(Color.PINK);
	    jPanel.add(jLabel,new BorderLayout().NORTH);
	    jPanel.add(onLineList);
	    jFrame.add(jButton1);
	    jFrame.add(jButton2);
	    jFrame.add(jLabel,new BorderLayout().NORTH);
	    jFrame.add(jPanel);
		jFrame.setResizable(true);
		jFrame.setVisible(true);
	}
}