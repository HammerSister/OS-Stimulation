package file.view;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;
import user.vo.User;
import file.vo.MyFile;
import file.server.FilesysServer;

public class FilePanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	DefaultMutableTreeNode nodes[];
	DefaultTreeModel model;
	JTree tree;
	JScrollPane jsp,jsp2;
	JLabel jl[];
	public JTextField jt[];
	JButton jb[];
	String nowpath="#";
	MyFile mf;
	FileeditDialog fd;
	FilesysServer fss;
	FilesysPanel parent;
	BitPanel bp;
	User user;
	public FilePanel(FilesysPanel fp){
		this.parent=fp;
		this.user=fp.user;
		fss=fp.fss;
		inittree();
		this.setLayout(null);
		jsp=new JScrollPane(tree);
		jsp.setBounds(0,30,200,510);
		this.add(jsp);
		final Font font=new Font("΢���ź�",Font.BOLD,14);
		jl=new JLabel[12];
		jt=new JTextField[12];
		//��ʾ�ļ���Ϣ
		jl[0]=new JLabel("--------�ļ�����-------");
		jl[1]=new JLabel("�ļ����ƣ�");
		jl[2]=new JLabel("�ļ����ͣ�");
		jl[3]=new JLabel("�ļ���С��");
		jl[4]=new JLabel("������ID��");
		jl[5]=new JLabel("�޸�ʱ�䣺");
		jl[0].setBounds(250,70,200,30);
		jl[0].setFont(new Font("����",Font.BOLD,15));
		this.add(jl[0]);
		for(int i=1;i<=5;i++){
			jl[i].setBounds(250,90+23*i,80,23);
			this.add(jl[i]);
		}
		for(int i=1;i<=5;i++){
			jt[i]=new JTextField();
			jt[i].setBounds(330, 90+23*i,120,23);
			jt[i].setEditable(false);
			this.add(jt[i]);
		}
		//��ʾ������Ϣ
		jl[11]=new JLabel("--------��������-------");
		jl[6]=new JLabel("ȫ���ռ䣺");
		jl[7]=new JLabel("ʣ��ռ䣺");
		jl[8]=new JLabel("�̿��С��");
		jl[9]=new JLabel("ʣ���̿飺");
		jl[10]=new JLabel("ʣ��������");
		jl[11].setBounds(490,70,200,30);
		jl[11].setFont(new Font("����",Font.BOLD,15));
		this.add(jl[11]);
		for(int i=6;i<=10;i++){
			jl[i].setBounds(490,90+23*(i-5),80,23);
			this.add(jl[i]);
		}
		for(int i=6;i<=10;i++){
			jt[i]=new JTextField();
			jt[i].setBounds(570,90+23*(i-5),120,23);
			jt[i].setEditable(false);
			this.add(jt[i]);
		}
		jt[6].setText("51200B");
		jt[8].setText("512B");
		//��ʾ·��
		jt[0]=new JTextField(nowpath);
		jt[0].setEditable(false);
		jt[0].setBounds(0,0,200,30);
		jt[0].setFont(font);
		this.add(jt[0]);
		//�ļ�����
		jb=new JButton[10];
		jb[0]=new JButton("�½��ļ�");
		jb[1]=new JButton("�½�Ŀ¼");
		jb[2]=new JButton("ɾ��Ŀ¼");
		jb[3]=new JButton("ɾ���ļ�");
		jb[4]=new JButton("��д�ļ�");
		for(int i=0;i<5;i++) {
			jb[i].setEnabled(false);
			jb[i].addActionListener(this);
			this.add(jb[i]);
		}
		jb[0].setBounds(200,2,95,25);
		jb[1].setBounds(300,2,95,25);
		jb[2].setBounds(400,2,95,25);
		jb[3].setBounds(520,2,95,25);
		jb[4].setBounds(620,2,95,25);
		//���λͼ���
		bp=new BitPanel(fp);
		bp.setBounds(220,240,510,300);
		this.add(bp);
	}
	//����������ص���������
	public boolean isokStr(String s){
		if(s.equals("")||s.length()>13)return false;
		for(int i=0;i<s.length();i++){
			char c=s.charAt(i);
			if(c>='a'&&c<='z'||c>='A'&&c<='Z'||c>='0'&&c<='9'||c=='.')continue;
			return false;
		}
		return true;
	}
	public String inputFileName(){
		String rs=JOptionPane.showInputDialog("�����ļ���(ֻ������ĸ�����������)");
		if(rs==null)
			return null;
		if(!isokStr(rs)){
			JOptionPane.showMessageDialog(this, "�ļ����Ƿ���");
			return null; 
		}
		return rs;
	}
	public String inputDirName(){
		String rs=JOptionPane.showInputDialog("����Ŀ¼��(ֻ������ĸ�����������)");
		if(rs==null)
			return null;
		if(!isokStr(rs)){
			JOptionPane.showMessageDialog(this, "Ŀ¼���Ƿ���");
			return null; 
		}
		return rs;
	}
	//���ļ������г�ʼ��
	String nowuserpath="",userpath="";
	public void inittree(){
		nodes=new DefaultMutableTreeNode[80];
		//������ȳ�ʼ����
		NodeData rt=new NodeData(1,0,"#");
		nodes[0]=new DefaultMutableTreeNode(rt);
		LinkedList<NodeData> q=new LinkedList<NodeData>();
		q.add(rt);
		while(q.size()>0){
			NodeData nd=q.getFirst();
			q.removeFirst();
			NodeData []nnd=fss.getchild(nd.inode);
			for(int i=0;i<nnd.length;i++){
				nodes[nnd[i].inode]=new DefaultMutableTreeNode(nnd[i]);
				nodes[nd.inode].add(nodes[nnd[i].inode]);
				if(nnd[i].type==1)
					q.add(nnd[i]);
			}
		}
		//�ø��ڵ��½�����������Ⱦ�������õ���ѡ��ģʽ
		tree=new JTree(nodes[0]);
		//tree.setEditable(true);
		tree.setRootVisible(false);
		tree.setCellRenderer(new MyRenderer());
		tree.setRowHeight(20);
		model=(DefaultTreeModel)tree.getModel();
		//�Զ�չ��
		for(int i=0;i <tree.getRowCount();i++)
			tree.expandRow(i);	
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		//����ѡ�иı�ʱ�ļ���
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				//������ʾ������
				if(e.getNewLeadSelectionPath()==null)
					return;
				String str=e.getNewLeadSelectionPath().toString();
				nowpath="";
				nowuserpath="";//ѡ���û�Ŀ¼
				userpath="#/users/"+user.getUserName();//��ǰ�û�Ŀ¼
				//���ȡ���ڶ���
				int dd=0;
				for(int i=0;i<str.length();i++){
					char s=str.charAt(i);
					if(s=='['||s==']'||s==' ')
						continue;
					if(s==','){
						dd++;
						if(dd==3)
							nowuserpath=nowpath;
						nowpath+='/';
					}
					else nowpath+=s;
				}
				if(dd<3)
					nowuserpath=nowpath;
				jt[0].setText(nowpath);
				//ȡ����ǰѡ���NodeData����
				DefaultMutableTreeNode dtn=(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				NodeData nd=(NodeData)dtn.getUserObject();
				//����ѡ�еĶ�����ʾ��Ӧ����Ϣ
				int type=nd.type;
				if(type==1){
					mf=fss.readdir(nowpath);
					jb[0].setEnabled(true);
					jb[1].setEnabled(true);
					jb[2].setEnabled(true);
					jb[3].setEnabled(false);
					jb[4].setEnabled(false);
				}else{
					mf=fss.readfile(nowpath);
					jb[0].setEnabled(false);
					jb[1].setEnabled(false);
					jb[2].setEnabled(false);
					jb[3].setEnabled(true);
					jb[4].setEnabled(true);
				}
				if(!nowuserpath.equals(userpath)&&!user.getIsAdmin()){
					for(int i=0;i<4;i++){
						jb[i].setEnabled(false);
					}
				}
				TreeNode tn=dtn.getParent();
				//�ǿ�Ŀ¼�Լ���Ŀ¼���ܱ�ɾ��
				if(dtn.getChildCount()!=0)
					jb[2].setEnabled(false);
				if(tn.toString().equals("#"))
					jb[2].setEnabled(false);
				jt[1].setText(mf.fname);
				jt[2].setText(type==1?"Ŀ¼":"�ļ�");
				jt[3].setText(mf.fsize+"B");
				jt[4].setText(new Integer(mf.fownid).toString());
				String s=mf.ftime,s2="";
				s2=s.substring(0,4)+"/"+s.substring(4,6)+"/"+s.substring(6,8)+" ";
				s2+=s.substring(8,10)+":"+s.substring(10);
				jt[5].setText(s2);
			}
		});
	}
	//��������
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("�½��ļ�")){
			//�½��ļ�
			String ss=inputFileName();
			if(ss==null)
				return;
			int ii=fss.mkfile(nowpath, ss, "",user);
			if(ii==0)
				return;
			DefaultMutableTreeNode dtn=(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			NodeData nd=(NodeData)dtn.getUserObject();
			NodeData tmp[]=fss.getchild(nd.inode);
			NodeData nnd=tmp[tmp.length-1];
			nodes[nnd.inode]=new DefaultMutableTreeNode(nnd);
			dtn.add(nodes[nnd.inode]);
			tree.expandPath(tree.getSelectionPath());
		}else if(e.getActionCommand().equals("�½�Ŀ¼")){
			//�½�Ŀ¼
			String ss=inputDirName();
			if(ss==null)
				return;
			int ii=fss.mkdir(nowpath, ss,user);
			if(ii==0)
				return;
			DefaultMutableTreeNode dtn=(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			NodeData nd=(NodeData)dtn.getUserObject();
			NodeData tmp[]=fss.getchild(nd.inode);
			NodeData nnd=tmp[tmp.length-1];
			nodes[nnd.inode]=new DefaultMutableTreeNode(nnd);
			dtn.add(nodes[nnd.inode]);
			tree.expandPath(tree.getSelectionPath());	
		}else if(e.getActionCommand().equals("ɾ��Ŀ¼")){
			//ɾ��Ŀ¼
			fss.rmdir(nowpath);
			DefaultMutableTreeNode dtn=(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			((DefaultTreeModel)tree.getModel()).removeNodeFromParent(dtn);
		}else if(e.getActionCommand().equals("ɾ���ļ�")){
			//ɾ���ļ�
			fss.rmfile(nowpath);
			jb[4].setEnabled(false);
			DefaultMutableTreeNode dtn=(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			model.removeNodeFromParent(dtn);
		}else if(e.getActionCommand().equals("��д�ļ�")){
			//��д�ļ�
			fd=new FileeditDialog(parent, mf.fname, mf.fcont,userpath.equals(nowuserpath)||user.getIsAdmin());
			if(fd.change==false)return;
			@SuppressWarnings("unused")
			int iid=fss.writefile(nowpath, fd.name, fd.cont);
			DefaultMutableTreeNode dtn=(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			NodeData nd=(NodeData)dtn.getUserObject();
			dtn.setUserObject(new NodeData(nd.type,nd.inode,fd.name));  
			tree.repaint();
		}
		tree.updateUI();
		bp.repaint();
	}
}
//������Ⱦ��,����ͼ��
class MyRenderer extends DefaultTreeCellRenderer{
	private static final long serialVersionUID = 1L;
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		//���ݽڵ�����ѡ��ͬ��ͼ��
		DefaultMutableTreeNode node=(DefaultMutableTreeNode)value;
		NodeData data=(NodeData)node.getUserObject();
		ImageIcon icon=null;
		if(data.type==1){
			icon=new ImageIcon("pics/dir.jpg");
		}else if(data.type==2){
			icon=new ImageIcon("pics/txt.jpg");
		}
		this.setFont(new Font("΢���ź�",Font.PLAIN,15));
		this.setIcon(icon);
		return this;
	}
}