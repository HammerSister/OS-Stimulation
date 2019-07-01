package chat;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Client extends JFrame implements ActionListener, Runnable {
	private static final long serialVersionUID = 1L;
	public static String selectedItem;
	public static Socket s=null;
	private PrintStream ps=null;
	private BufferedReader br=null;
	private JPanel plUser=new JPanel(new BorderLayout());
	private JLabel lbUser=new JLabel("���ߺ��ѣ�");
	private JComboBox<String> cbUser=new JComboBox<String>();
	private JTextArea taMsg=new JTextArea();
	private JScrollPane spMsg=new JScrollPane(taMsg);
	private JPanel plMsg=new JPanel(new BorderLayout());
	private JButton btOffline=new JButton("����");
	private JTextField tfMsg=new JTextField();
	private JButton btSend=new JButton("����");
	private Font font1=new Font("���Ŀ���",Font.BOLD,15);
	private Font font2=new Font("����",Font.PLAIN,16);
	private Font font3=new Font("����",Font.PLAIN,15);
    public Client() throws Exception {
		s=new Socket("127.0.0.1",9999);  //��������
		this.initFrame();
		this.setTitle("��ǰ���ߣ�"+Conf.nickname);
		ps=new PrintStream(s.getOutputStream());
		ps.println("nickname#"+Conf.nickname);  //�����ַ���������
		new Thread(this).start();
	}
    public void initFrame() {
    	taMsg.setLineWrap(true);
		taMsg.setEditable(false);
		taMsg.setBackground(Color.GRAY);
		taMsg.setForeground(Color.WHITE);
		taMsg.setFont(font2);
		lbUser.setToolTipText("ѡ���Է�����Ϣ");
		lbUser.setFont(font1);
		cbUser.addItem("ALL");
		cbUser.setFont(font1);
		btOffline.setFont(font3);
		tfMsg.setFont(font3);
		btSend.setFont(font3);
		plUser.add(lbUser,BorderLayout.WEST);
		plUser.add(cbUser,BorderLayout.CENTER);
		plMsg.add(btOffline,BorderLayout.WEST);
		plMsg.add(tfMsg,BorderLayout.CENTER);
		plMsg.add(btSend,BorderLayout.EAST);
		this.add(plUser,BorderLayout.NORTH);
		this.add(spMsg,BorderLayout.CENTER);
		this.add(plMsg,BorderLayout.SOUTH);
		//���Ӽ���
		btOffline.addActionListener(this);
		btSend.addActionListener(this);
		tfMsg.addActionListener(this);
		this.addWindowListener(new WindowOpe());
		this.setSize(450,500);
		this.setLocation(200,200);
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btOffline) {
			//����ͻ�������
		    try {
		    	int result=JOptionPane.showConfirmDialog(this,"���ߺ������������������\n��ȷ��������","ȷ��",JOptionPane.YES_NO_OPTION);
		    	if(result==JOptionPane.YES_OPTION) {
		    		ps=new PrintStream(s.getOutputStream());
		    		ps.println("exit#"+Conf.nickname);
		    		System.exit(0);
		    	}
		    } catch(Exception ex) {}
		}
		else {
			//����ͻ��˷�����Ϣ���س�/��ť��
			selectedItem=(String)cbUser.getSelectedItem();
			try {
				String str=tfMsg.getText();
				if(selectedItem.equals("ALL")){
					tfMsg.setText("");
					ps=new PrintStream(s.getOutputStream());
					ps.println(Conf.nickname+"��"+str);
				}
				else{
					tfMsg.setText("");
					ps=new PrintStream(s.getOutputStream());
					ps.println("private#"+Conf.nickname+"#"+selectedItem+"#"+str);
					taMsg.append("��˽��"+selectedItem+"��"+str+"��\n");
				}
			} catch(Exception ex){
				JOptionPane.showMessageDialog(this, "��Ϣ�����쳣");
			}
		}
	}
	public void run() {
		try {
			while(true) {
				br=new BufferedReader(new InputStreamReader(s.getInputStream()));
				ps=new PrintStream(s.getOutputStream());
				String mes=br.readLine();
				if(mes.startsWith("nickname#")) {
					String[] strs=mes.split("#");
					cbUser.removeAllItems();
					for(int i=1;i<strs.length;i++) {
						if(!strs[i].equals(Conf.nickname))
							cbUser.addItem(strs[i]);
					}
					cbUser.setSelectedItem(selectedItem);
				}
				else if(mes.startsWith("close#")) {
					String [] strss=mes.split("#");
					if(strss[1].equals(Conf.nickname)) {
						JOptionPane.showMessageDialog(this, "�Բ������ѱ�ǿ������");
						try{
							ps.println("exit#"+Conf.nickname);
						} catch(Exception e) {}
						System.exit(0);
					}
				}
				else {	
				    taMsg.append(mes+"\n");
				}
			}
		} catch(Exception e) {
			JOptionPane.showMessageDialog(this, "���������쳣");
			System.exit(-1);
		}
	}
	class WindowOpe extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			int result=JOptionPane.showConfirmDialog(null,"�˳���������������״̬\n��ȷ��Ҫ�˳���","ȷ��",JOptionPane.YES_NO_OPTION);
			if(result==JOptionPane.YES_OPTION) {
			    try{
			    	ps=new PrintStream(s.getOutputStream());
		        	ps.println("exit#"+Conf.nickname);
		        	System.exit(0);
			    } catch(Exception ex) {
			    	JOptionPane.showMessageDialog(null, "����");
			    }
			}
		}
	}
}