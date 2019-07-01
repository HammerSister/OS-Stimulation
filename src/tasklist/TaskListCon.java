package tasklist;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class TaskListCon {
	public DefaultTableModel tasklist() {
		String[][] result = new String[60][10];
		String[] title = null;
		Process pro = null;
		try {
			pro = Runtime.getRuntime().exec("cmd /c tasklist /FO csv");
			InputStreamReader ipsr = new InputStreamReader(pro.getInputStream());
			BufferedReader br = new BufferedReader(ipsr);
			String res = null;
			int x = 0;
			while ((res = br.readLine()) != null) {
				String[] value = res.replace("\",\"", ";").replace("\"", "")
						.split(";");
				if (x == 0) {
					title = new String[value.length + 1];
				}
				for (int i = 0; i < value.length + 1; i++) {

					if (x == 0) {
						if (i == 0) {
							title[i] = "±àºÅ";
						} else {
							title[i] = value[i - 1];
						}
					} else if (i == 0) {
						result[x - 1][i] = x + "";
					} else {
						result[x - 1][i] = value[i - 1];
					}
				}

				if (value.length < 10 && value.length > 1) {
					x++;
				}
				if (x == 60) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		DefaultTableModel modle = new DefaultTableModel(result, title) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		return modle;
	}
	@SuppressWarnings("deprecation")
	public void javaDos(String cmd, JTable table) {
		try {
			Runtime.getRuntime().exec("cmd /c " + cmd);
			Thread thread = new Thread();
			thread.destroy();
			showList(table);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public JTable showList(JTable table) {
		DefaultTableModel modle = tasklist();
		table.setModel(modle);
		TableColumn Column1 = table.getColumnModel().getColumn(0);
		Column1.setPreferredWidth(30);
		TableColumn Column2 = table.getColumnModel().getColumn(1);
		Column2.setPreferredWidth(150);
		TableColumn Column6 = table.getColumnModel().getColumn(5);
		Column6.setPreferredWidth(80);
		return table;
	}
}