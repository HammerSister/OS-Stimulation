package file.view;
import java.awt.Toolkit;
import javax.swing.JFrame;
import user.vo.User;
import file.server.FilesysServer;

public class FilesysPanel extends JFrame{
	private static final long serialVersionUID = 1L;
	FilePanel ft;
	public FilesysServer fss;
	public User user;
	public FilesysPanel(FilesysServer fss,User user){
		this.fss=fss;
		this.user=user;
		this.setLayout(null);
		ft=new FilePanel(this);
		this.add(ft);
		ft.setBounds(0,0,730,540);
		int sw=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int sh=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.setSize(730,540);
		this.setLocation(sw/2-365, sh/2-270);
		this.setResizable(false);
		this.setTitle("文件管理器--用户 "+user.getUserName()+" 使用中...");
		this.setVisible(true);
	}
}