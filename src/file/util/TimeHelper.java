package file.util;
import java.text.SimpleDateFormat;
import java.util.Date;

//ʱ�丨����
public class TimeHelper {
	public String gettime(){
		return new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
	}
}