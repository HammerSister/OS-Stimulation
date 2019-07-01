package process.view;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import process.vo.PCB;
import process.vo.Queue;

public class QueuePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private String[] title;
	private Object[][] data;
	private DefaultTableModel dtm;
	private JTable table;
	private JScrollPane jsp;
	public QueuePanel(Queue queue){
		this.setLayout(new GridLayout(1,1));
		title = new String[]{"PID","优先级","所需时间","所需内存"};
		int size = queue.getSize();
		data = new Object[size][title.length];
		PCB temp = queue.getFirst();
		int index = 0;
		while(temp!=null){
			data[index][0] = temp.getPID();
			data[index][1] = temp.getPriority();
			data[index][2] = temp.getTimeNeed();
			data[index][3] = temp.getMemoryNeed();
			index++;
			temp = temp.getNext();
		}
		dtm = new DefaultTableModel(data,title);
		table = new JTable(dtm);
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();   
		r.setHorizontalAlignment(JLabel.CENTER);   
		table.setDefaultRenderer(Object.class, r);
		jsp = new JScrollPane(table);
		this.add(jsp);
	}
	/*刷新表格*/
	public void refreshTable(Queue queue){
		int size = queue.getSize();
		if(size == 0){
			table.setModel(new DefaultTableModel());
			return;
		}
		data = new Object[size][title.length];
		PCB temp = queue.getFirst();
		int index = 0;
		while(temp!=null){
			data[index][0] = temp.getPID();
			data[index][1] = temp.getPriority();
			data[index][2] = temp.getTimeNeed();
			data[index][3] = temp.getMemoryNeed();
			index++;
			temp = temp.getNext();
		}
		dtm = new DefaultTableModel(data,title);
		table.setModel(dtm);
	}
}