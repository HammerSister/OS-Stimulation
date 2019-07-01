package chat;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

//接受客户端连接请求
public class Server extends JFrame implements Runnable, ActionListener {
	private static final long serialVersionUID = 1L;
	JFrame frame=this;
	private Socket s=null;  //客户端连接
	private ServerSocket ss=null;  //服务器端接受连接
	public static String userLists="";
    public static String selectedItem;
	public static ArrayList<ChatThread> clients=new ArrayList<ChatThread>();  //保存客户端线程的集合
	private JPanel plUser=new JPanel(new BorderLayout());	
	private JLabel lbUser=new JLabel("在线用户：");
	private JComboBox<String> cbUser=new JComboBox<String>();
	private JTextArea taMsg=new JTextArea();
	private JScrollPane spMsg=new JScrollPane(taMsg);
	private JPanel plManage=new JPanel(new BorderLayout());
	private JTextField tfMsg=new JTextField();
	private JButton btSend=new JButton("发送");
	private JButton btExit=new JButton("退出聊天室");
	private Font font1=new Font("华文楷体",Font.BOLD,12);
	private Font font2=new Font("黑体",Font.PLAIN,14);
	private Font font3=new Font("宋体",Font.PLAIN,12);
	public Server() throws Exception {
		this.setTitle("当前在线：admin");
		this.addWindowListener(new WindowOpe());
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		taMsg.setLineWrap(true);
		taMsg.setEditable(false);
		taMsg.setFont(font2);
		lbUser.setToolTipText("选中以强制下线或发送消息");
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
		//增加监听
		btSend.addActionListener(this);
		btExit.addActionListener(this);
		tfMsg.addActionListener(this);
		this.setSize(300,350);
		this.setResizable(false);
		this.setVisible(true);
	    ss=new ServerSocket(9999);  //服务器开辟端口，接受连接
	    new Thread(this).start();  //接受客户端连接的循环开始运行
	}
	public void run(){
		try{
			while(true){
				s=ss.accept();
				ChatThread ct=new ChatThread(s);
				clients.add(ct);
				ct.start();  //线程开始运行
			}
		}catch(Exception e){
			JOptionPane.showMessageDialog(this, "网络连接异常");
		}
	}
	public class ChatThread extends Thread {
		//为某个Socket负责接收信息
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
					String str=br.readLine();  //读取该Socket传来的信息
					if(str.startsWith("nickname#")) {
						//如果信息是客户端名称
						String[] strs=str.split("#");
						userLists=userLists+strs[1]+"#";
						taMsg.append("通知："+strs[1]+"上线了\n");  //在服务器上显示客户端上线
						String[] strs5=userLists.split("#");
						for(int i=0;i<clients.size();i++) {
							//给除了该客户端以外的其他客户端发送上线通知
							if(!strs5[i].equals(strs[1])) {
								ChatThread ct=(ChatThread)clients.get(i);
								ct.ps.println("*管理员消息："+strs[1]+"上线了");
							}
						}
						//更新服务器在线客户端列表
						cbUser.removeAllItems();
						cbUser.addItem("ALL");
						String[] strs4=userLists.split("#");
						for(int i=0;i<strs4.length;i++){
							cbUser.addItem(strs4[i]);
						}
						cbUser.setSelectedItem(selectedItem);
						//更新客户端好友列表
						sendMessage("nickname#"+"ALL#"+userLists);
					}
					else if(str.startsWith("exit#")) {
						//某个客户端的退出信号
						String[] strs=str.split("#");
						sendMessage("*管理员消息："+strs[1]+"下线了");
						taMsg.append("通知："+strs[1]+"下线了\n");  //在服务器上显示该客户端下线
						String[] strs2=userLists.split("#");
						//更新userLists、clients集合以及cbUser的内容
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
								ct.ps.println(strs1[1]+"私聊你："+strs1[3]);
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
			//处理退出服务器
			try{ 
				int result=JOptionPane.showConfirmDialog(this,"确认退出聊天室？","确认",JOptionPane.YES_NO_OPTION);      			     
				if(result==JOptionPane.YES_OPTION) {
					frame.dispose();
				}
			}catch(Exception ex) {}
		}
		else {
			//处理服务器发送消息
			selectedItem=(String)cbUser.getSelectedItem();
			try{
				String str=tfMsg.getText();
				tfMsg.setText("");
				if(selectedItem.equals("ALL")) {
					for(int i=0;i<clients.size();i++){
						ChatThread ct=(ChatThread)clients.get(i);  //从集合中取出客户端
						ct.ps.println("*管理员群发消息："+str);  //给所有客户端发信息
					}
					taMsg.append("群发消息："+str+"\n");
				}
				else {
					String[] strs3=userLists.split("#");
					for(int i=0;i<strs3.length;i++) {
						if(strs3[i].equals(selectedItem)) {
							ChatThread ct=(ChatThread)clients.get(i);
							ct.ps.println("*管理员私发消息："+str);  //给选中的客户端发信息
							taMsg.append("私发给"+selectedItem+"的消息："+str+"\n");
						}
					}
				}
			} catch(Exception ex) {
				JOptionPane.showMessageDialog(this, "消息发送异常");
			}
		}
	}
	public void sendMessage(String msg) {
		for(int i=0;i<clients.size();i++){
			ChatThread ct=(ChatThread)clients.get(i);  //从集合中取出客户端
			ct.ps.println(msg);  //给客户端发信息
		}
	}
	class WindowOpe extends WindowAdapter {
		public void windowClosing(WindowEvent e){
			int result=JOptionPane.showConfirmDialog(null,"确认退出聊天室？","确认",JOptionPane.YES_NO_OPTION);      			     
			if(result==JOptionPane.YES_OPTION)	{
				frame.dispose();
			}
		}
	}
}