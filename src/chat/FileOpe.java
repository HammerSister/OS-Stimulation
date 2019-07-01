package chat;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Properties;
import javax.swing.JOptionPane;

public class FileOpe {
	private static String fileName="cus.inc";
	private static Properties pps;
	static{
		pps=new Properties();
		FileReader reader=null;
		try{
			reader=new FileReader(fileName);         
			pps.load(reader);
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "文件操作异常");
			System.exit(0);
		}finally {
			try{
				reader.close();
			}catch (Exception e) {}
		}
	}
	private static void listInfo() {
		PrintStream ps=null;
		try{
			ps=new PrintStream(fileName);
			pps.list(ps);
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "文件操作异常");
			System.exit(0);
		}finally {
			try{
				ps.close();
			}catch (Exception e) {}
		}
	}
	public static void getInfoByAccount(String account){
		String cusInfo=pps.getProperty(account);
		if(cusInfo!=null){
			String[] infos=cusInfo.split("#");
			Conf.account=account;
			Conf.password=infos[0];
			Conf.nickname=infos[1];
		}
	}
	public static void updateCustomer(String account,String password,String nickname){
		pps.setProperty(account, password+"#"+nickname);
		listInfo();
	}
}