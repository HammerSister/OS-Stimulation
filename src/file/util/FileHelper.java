package file.util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

//文件辅助类
public class FileHelper {
	String filename;
	File file;
	RandomAccessFile raf;
	//初始化磁盘内容为空
	public FileHelper(String filename) {
		this.filename = filename;
	}
	//读取从指定位置开始指定长度的字符串
	public String read(int start, int size) {
		open();
		byte b[] = new byte[size];
		try {
			raf.seek(start);
			raf.read(b);
		} catch (IOException e) {
			System.out.println("Error：超出文件范围！");
		}
		close();
		return new String(b);
	}
	//从指定位置存取字符串
	public void save(int start, String str) {
		open();
		try {
			raf.seek(start);
			raf.writeBytes(str);
		} catch (IOException e) {
			System.out.println("Error：超出文件范围！");
		}
		close();
	}
	//打开文件操作
	public void open() {
		try {
			raf = new RandomAccessFile(filename, "rw");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	//关闭文件操作
	public void close() {
		try {
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}