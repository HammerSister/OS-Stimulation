package process.view;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import process.vo.PCB;

public class NewPCBDialog extends JDialog implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JLabel tips_id;
	private JLabel tips_priority;
	private JLabel tips_timeNeed;
	private JLabel tips_memoryNeed;
	private JTextField text_id;
	private JTextField text_priority;
	private JTextField text_timeNeed;
	private JTextField text_memoryNeed;
	private JButton create;
	private MainFrame parentFrame;
	public NewPCBDialog(MainFrame frame){
		super(frame,"新建进程",true);
		this.parentFrame = frame;
		this.setLayout(null);
		this.setBounds(frame.getX()+frame.getWidth()/2-150, frame.getY()+frame.getHeight()/2-155, 300, 310);
		this.init();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	public void init(){
		tips_id = new JLabel("进程名：",JLabel.RIGHT);
		tips_id.setBounds(40, 50, 80, 20);
		this.add(tips_id);
		tips_priority = new JLabel("优先级：",JLabel.RIGHT);
		tips_priority.setBounds(40, 90, 80, 20);
		this.add(tips_priority);
		tips_timeNeed = new JLabel("所需时间：",JLabel.RIGHT);
		tips_timeNeed.setBounds(40, 130, 80, 20);
		this.add(tips_timeNeed);
		tips_memoryNeed = new JLabel("所需内存：",JLabel.RIGHT);
		tips_memoryNeed.setBounds(40, 170, 80, 20);
		this.add(tips_memoryNeed);
		text_id = new JTextField(20);
		text_id.setBounds(130, 50, 100, 20);
		this.add(text_id);
		text_priority = new JTextField(20);
		text_priority.setBounds(130, 90, 100, 20);
		this.add(text_priority);
		text_timeNeed = new JTextField(20);
		text_timeNeed.setBounds(130, 130, 100, 20);
		this.add(text_timeNeed);
		text_memoryNeed = new JTextField(20);
		text_memoryNeed.setBounds(130, 170, 100, 20);
		this.add(text_memoryNeed);
		create = new JButton("创建");
		create.setBounds(105, 220, 80, 25);
		this.add(create);
		create.addActionListener(this);
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == create){
			try{
				int id = Integer.parseInt(text_id.getText());
				int priority = Integer.parseInt(text_priority.getText());
				int timeNeed = Integer.parseInt(text_timeNeed.getText());
				int memoryNeed = Integer.parseInt(text_memoryNeed.getText());
				PCB pcb = this.parentFrame.getNewPCB();
				pcb.setPID(id);
				pcb.setPriority(priority);
				pcb.setTimeNeed(timeNeed);
				pcb.setMemoryNeed(memoryNeed);
				this.dispose();
			}catch(Exception ex){
				ex.printStackTrace();
				javax.swing.JOptionPane.showMessageDialog(this, "输入数据有误，请重新输入！");
			}
		}
	}
}