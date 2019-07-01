package process.view;
import javax.swing.JFrame;

public class About extends JFrame{
	private static final long serialVersionUID = 1L;
	public About(MainFrame frame){
		this.setBounds(frame.getX()+frame.getWidth()/2-150, frame.getY()+frame.getHeight()/2-100, 300, 200);
		this.setVisible(true);
	}
}
