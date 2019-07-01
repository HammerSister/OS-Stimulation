package main;
import javax.swing.UIManager;
import file.server.FilesysServer;
import user.view.LoginView;

//���������
public class Main {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		FilesysServer fss;
		fss=new FilesysServer("disk.txt");
		//��ʼ��
		new LoginView(fss);
	}
}