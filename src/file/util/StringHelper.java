package file.util;

//�����ļ��ַ���������
public class StringHelper {
	StringBuffer bf;
	public void clear() {
		bf=new StringBuffer();
	}
	//������ݲ��ÿո�ȫ�ַ���
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
	//�ÿո�ȫ����
	public void fill(int tsize){
		while(bf.length()<tsize)bf.append(" ");
	}
	public String get(){
		return bf.toString();
	}
}