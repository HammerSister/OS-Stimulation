package tasklist;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.table.DefaultTableModel;

public class TaskList extends JFrame {
	private static final long serialVersionUID = 1L;
	static JTable table = null;
	JScrollPane p1;
	JComboBox<String> box;
	JPopupMenu rightmenu;
	private TaskListCon con = new TaskListCon();
	public TaskList() {
		super("任务管理器");
		this.addItem();
		this.pack();
		this.setVisible(true);
		this.setLocation(150, 100);
	}
	public static void main(String[] args) throws Exception {
		new TaskList();
	}
	public void addItem() {
		Container f = this.getContentPane();
		table = new JTable();
		table = con.showList(table);
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setSelectionBackground(Color.lightGray);
		table.setSelectionForeground(Color.WHITE);
		box = new JComboBox<String>();
		box.addItem("结束任务");
		box.addItem("刷新进程列表");
		rightmenu = new BasicComboPopup(box);
		rightmenu.setPopupSize(130, 45);
		box.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rightKey();
			}
		});
		table.setComponentPopupMenu(rightmenu);
		p1 = new JScrollPane(table);
		f.setLayout(new BorderLayout());
		f.add(p1, BorderLayout.CENTER);
	}
	public void rightKey() {
		int r = table.getSelectedRow();
		//1原来为3
		if (box.getSelectedIndex() == 1) {
			con.showList(table);
		} else if (r == -1) {
			JOptionPane.showMessageDialog(null, "对不起！你还没有选中一行！", "系统提示",JOptionPane.WARNING_MESSAGE);
		} else {
			if (box.getSelectedIndex() == 0) {
				
				//删除表格行
				DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
				tableModel.removeRow(r);// r是要删除的行序号
			}
		}
	}
}