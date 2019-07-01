package chat;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

//���ܿͻ�����������
public class Server extends JFrame implements Runnable, ActionListener {
	private static final long serialVersionUID = 1L;
	JFrame frame=this;
	private Socket s=null;  //�ͻ�������
	private ServerSocket ss=null;  //�������˽�������
	public static String userLists="";
    public static String selectedItem;
	public static ArrayList<ChatThread> clients=new ArrayList<ChatThread>();  //����ͻ����̵߳ļ���
	private JPanel plUser=new JPanel(new BorderLayout());	
	private JLabel lbUser=new JLabel("�����û���");
	private JComboBox<String> cbUser=new JComboBox<String>();
	private JTextArea taMsg=new JTextArea();
	private JScrollPane spMsg=new JScrollPane(taMsg);
	private JPanel plManage=new JPanel(new BorderLayout());
	private JTextField tfMsg=new JTextField();
	private JButton btSend=new JButton("����");
	private JButton btExit=new JButton("�˳�������");
	private Font font1=new Font("���Ŀ���",Font.BOLD,12);
	private Font font2=new Font("����",Font.PLAIN,14);
	private Font font3=new Font("����",Font.PLAIN,12);
	public Server() throws Exception {
		this.setTitle("��ǰ���ߣ�admin");
		this.addWindowListener(new WindowOpe());
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		taMsg.setLineWrap(true);
		taMsg.setEditable(false);
		taMsg.setFont(font2);
		lbUser.setToolTipText("ѡ����ǿ�����߻�����Ϣ");
		lbUser.setFont(font1);
		cbUser.setFont(font1);
		plUser.add(lbUser,BorderLayout.WEST);
		plUser.add(cbUser,BorderLayout.CENTER);
		tfMsg.setFont(font3);
		btSend.setFont(font3);
		btExit.setFont(font3);
		plManage.add(tfMsg,BorderLayout.CENTER);
		plManage.add(btSend,BorderLayout.EAST);
		plManage.add(btExit,BorderLayout.SOUTH);
		this.add(plUser,BorderLayout.NORTH);
		this.add(spMsg,BorderLayout.CENTER);
		this.add(plManage,BorderLayout.SOUTH);
		//���Ӽ���
		btSend.addActionListener(this);
		btExit.addActionListener(this);
		tfMsg.addActionListener(this);
		this.setSize(300,350);
		this.setResizable(false);
		this.setVisible(true);
	    ss=new ServerSocket(9999);  //���������ٶ˿ڣ���������
	    new Thread(this).start();  //���ܿͻ������ӵ�ѭ����ʼ����
	}
	public void run(){
		try{
			while(true){
				s=ss.accept();
				ChatThread ct=new ChatThread(s);
				clients.add(ct);
				ct.start();  //�߳̿�ʼ����
			}
		}catch(Exception e){
			JOptionPane.showMessageDialog(this, "���������쳣");
		}
	}
	public class ChatThread extends Thread {
		//Ϊĳ��Socket���������Ϣ
		@SuppressWarnings("unused")
		private Socket s=null;
		private BufferedReader br=null;
		public PrintStream ps=null;
		public ChatThread(Socket s) throws Exception {
			this.s=s;
			br=new BufferedReader(new InputStreamReader(s.getInputStream()));
			ps=new PrintStream(s.getOutputStream());
		}
		public void run() {
			try {
				while(true) {
					String str=br.readLine();  //��ȡ��Socket��������Ϣ
					if(str.startsWith("nickname#")) {
						//�����Ϣ�ǿͻ�������
						String[] strs=str.split("#");
						userLists=userLists+strs[1]+"#";
						taMsg.append("֪ͨ��"+strs[1]+"������\n");  //�ڷ���������ʾ�ͻ�������
						String[] strs5=userLists.split("#");
						for(int i=0;i<clients.size();i++) {
							//�����˸ÿͻ�������������ͻ��˷�������֪ͨ
							if(!strs5[i].equals(strs[1])) {
								ChatThread ct=(ChatThread)clients.get(i);
								ct.ps.println("*����Ա��Ϣ��"+strs[1]+"������");
							}
						}
						//���·��������߿ͻ����б�
						cbUser.removeAllItems();
						cbUser.addItem("ALL");
						String[] strs4=userLists.split("#");
						for(int i=0;i<strs4.length;i++){
							cbUser.addItem(strs4[i]);
						}
						cbUser.setSelectedItem(selectedItem);
						//���¿ͻ��˺����б�
						sendMessage("nickname#"+"ALL#"+userLists);
					}
					else if(str.startsWith("exit#")) {
						//ĳ���ͻ��˵��˳��ź�
						String[] strs=str.split("#");
						sendMessage("*����Ա��Ϣ��"+strs[1]+"������");
						taMsg.append("֪ͨ��"+strs[1]+"������\n");  //�ڷ���������ʾ�ÿͻ�������
						String[] strs2=userLists.split("#");
						//����userLists��clients�����Լ�cbUser������
						userLists="";
						cbUser.removeAllItems();
						cbUser.addItem("ALL");
						for(int i=0;i<strs2.length;i++) {
							if(strs2[i].equals(strs[1])) {
								clients.remove(i);
							}
							else{
								cbUser.addItem(strs2[i]);
								userLists=userLists+strs2[i]+"#";
							}	
						}
						cbUser.setSelectedItem(selectedItem);
						sendMessage("nickname#"+"ALL#"+userLists);
					}
					else if(str.startsWith("private#")) {
						String[] strs1=str.split("#");
						String[] strs3=userLists.split("#");
						for(int i=0;i<strs3.length;i++) {
							if(strs3[i].equals(strs1[2])) {
								ChatThread ct=(ChatThread)clients.get(i);
								ct.ps.println(strs1[1]+"˽���㣺"+strs1[3]);
							}
						}
					}
					else {
						sendMessage(str);
					}
				}
			}catch(Exception e){}
		}
	}
	public void actionPerformed(ActionEvent e){
		selectedItem=(String)cbUser.getSelectedItem();    
		if(e.getSource()==btExit){
			//�����˳�������
			try{ 
				int result=JOptionPane.showConfirmDialog(this,"ȷ���˳������ң�","ȷ��",JOptionPane.YES_NO_OPTION);      			     
				if(result==JOptionPane.YES_OPTION) {
					frame.dispose();
				}
			}catch(Exception ex) {}
		}
		else {
			//���������������Ϣ
			selectedItem=(String)cbUser.getSelectedItem();
			try{
				String str=tfMsg.getText();
				tfMsg.setText("");
				if(selectedItem.equals("ALL")) {
					for(int i=0;i<clients.size();i++){
						ChatThread ct=(ChatThread)clients.get(i);  //�Ӽ�����ȡ���ͻ���
						ct.ps.println("*����ԱȺ����Ϣ��"+str);  //�����пͻ��˷���Ϣ
					}
					taMsg.append("Ⱥ����Ϣ��"+str+"\n");
				}
				else {
					String[] strs3=userLists.split("#");
					for(int i=0;i<strs3.length;i++) {
						if(strs3[i].equals(selectedItem)) {
							ChatThread ct=(ChatThread)clients.get(i);
							ct.ps.println("*����Ա˽����Ϣ��"+str);  //��ѡ�еĿͻ��˷���Ϣ
							taMsg.append("˽����"+selectedItem+"����Ϣ��"+str+"\n");
						}
					}
				}
			} catch(Exception ex) {
				JOptionPane.showMessageDialog(this, "��Ϣ�����쳣");
			}
		}
	}
	public void sendMessage(String msg) {
		for(int i=0;i<clients.size();i++){
			ChatThread ct=(ChatThread)clients.get(i);  //�Ӽ�����ȡ���ͻ���
			ct.ps.println(msg);  //���ͻ��˷���Ϣ
		}
	}
	class WindowOpe extends WindowAdapter {
		public void windowClosing(WindowEvent e){
			int result=JOptionPane.showConfirmDialog(null,"ȷ���˳������ң�","ȷ��",JOptionPane.YES_NO_OPTION);      			     
			if(result==JOptionPane.YES_OPTION)	{
				frame.dispose();
			}
		}
	}
}