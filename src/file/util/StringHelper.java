package file.util;

//磁盘文件字符串辅助类
public class StringHelper {
	StringBuffer bf;
	public void clear() {
		bf=new StringBuffer();
	}
	//填充内容并用空格补全字符串
	public void app(int []x,int size) {
		size=bf.length()+size;
		for(int i=0;i<x.length;i++)bf.append(x[i]);
		while(bf.length()<size)bf.append(" ");
	}
	public void app(int x,int size){
		size=bf.length()+size;
		bf.append(x);
		while(bf.length()<size)bf.append(" ");
	}
	public void app(String x,int size){
		size=bf.length()+size;
		bf.append(x);
		while(bf.length()<size)bf.append(" ");		
	}
	//用空格补全长度
	public void fill(int tsize){
		while(bf.length()<tsize)bf.append(" ");
	}
	public String get(){
		return bf.toString();
	}
}