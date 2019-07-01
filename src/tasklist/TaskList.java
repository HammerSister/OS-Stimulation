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
		super("���������");
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
		box.addItem("��������");
		box.addItem("ˢ�½����б�");
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
		//1ԭ��Ϊ3
		if (box.getSelectedIndex() == 1) {
			con.showList(table);
		} else if (r == -1) {
			JOptionPane.showMessageDialog(null, "�Բ����㻹û��ѡ��һ�У�", "ϵͳ��ʾ",JOptionPane.WARNING_MESSAGE);
		} else {
			if (box.getSelectedIndex() == 0) {
				
				//ɾ�������
				DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
				tableModel.removeRow(r);// r��Ҫɾ���������
			}
		}
	}
}