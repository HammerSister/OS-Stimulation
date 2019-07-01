package file.util;
import java.text.SimpleDateFormat;
import java.util.Date;

//Ê±¼ä¸¨ÖúÀà
public class TimeHelper {
	public String gettime(){
		return new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
	}
}