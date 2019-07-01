package file.util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

//�ļ�������
public class FileHelper {
	String filename;
	File file;
	RandomAccessFile raf;
	//��ʼ����������Ϊ��
	public FileHelper(String filename) {
		this.filename = filename;
	}
	//��ȡ��ָ��λ�ÿ�ʼָ�����ȵ��ַ���
	public String read(int start, int size) {
		open();
		byte b[] = new byte[size];
		try {
			raf.seek(start);
			raf.read(b);
		} catch (IOException e) {
			System.out.println("Error�������ļ���Χ��");
		}
		close();
		return new String(b);
	}
	//��ָ��λ�ô�ȡ�ַ���
	public void save(int start, String str) {
		open();
		try {
			raf.seek(start);
			raf.writeBytes(str);
		} catch (IOException e) {
			System.out.println("Error�������ļ���Χ��");
		}
		close();
	}
	//���ļ�����
	public void open() {
		try {
			raf = new RandomAccessFile(filename, "rw");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	//�ر��ļ�����
	public void close() {
		try {
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}