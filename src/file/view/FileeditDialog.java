package file.view;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class FileeditDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	public String cont,name;
	boolean change=false;
	JTextArea jta;
	JTextField jtf;
	public FileeditDialog(JFrame parent,String name,String cont,boolean canw){
		super(parent,true);
		this.setLayout(null);
		jta=new JTextArea();
		jta.setLineWrap(true);
		JScrollPane jsp=new JScrollPane(jta);
		jta.setText(cont);
		jtf=new JTextField(name);
		JLabel jl=new JLabel("�ļ�����");
		JButton jb1=new JButton("ȷ�ϱ༭");
		JButton jb2=new JButton("�˳��༭");
		jb1.addActionListener(this);
		jb2.addActionListener(this);
		jl.setBounds(20,10,50,30);
		jtf.setBounds(80,10,380,30);
		jsp.setBounds(20,50,440,250);
		jb1.setBounds(110,322,100,25);
		jb2.setBounds(240,322,100,25);
		this.add(jtf);
		this.add(jsp);
		this.add(jl);
		this.add(jb1);
		this.add(jb2);
		jb1.setEnabled(canw);	//�����Ƿ��д
		this.setSize(500, 400);
		this.setTitle("�ļ���д��");
		int sw=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int sh=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.setLocation(sw/2-250, sh/2-200);
		this.setVisible(true);
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("ȷ�ϱ༭")){
			cont=jta.getText();
			name=jtf.getText();
			change=true;
		}else if(e.getActionCommand().equals("�˳��༭")){
			change=false;
		}
		this.setVisible(false);
	}
}