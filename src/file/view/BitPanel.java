package file.view;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;
import file.vo.Disk;
import file.vo.SuperBlock;
import file.server.FilesysServer;

public class BitPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	FilesysServer fss;
	FilesysPanel fp;
	public BitPanel(FilesysPanel fp){
		this.fss=fp.fss;
		this.fp=fp;
	}
	public Color createRandomColor() {
		return new Color((new Double(Math.random()*128)).intValue()+128,
				(new Double(Math.random()*128)).intValue()+128,
				(new Double(Math.random()*128)).intValue()+128);
	}
	public void paint(Graphics g){
		super.paint(g);
		SuperBlock sb=fss.getSuperblock();
		g.setFont(new Font("����",Font.BOLD,15));
		g.drawString("-------���̿����------", 30, 20);
		g.drawString("------�����ڵ����-----", 270, 20);
		int tot1=100,tot2=80;
		//�����̿�������,�Ȼ������ٻ���
		for(int i=0;i<Disk.BNUM;i++){
			int xx=i%10,yy=i/10;
			if(sb.bitblock[i]==1){
				tot1--;
				if(i<=11)
					g.setColor(Color.DARK_GRAY);
				else
					g.setColor(createRandomColor());
				g.fillRect(30+xx*20,40+yy*20,20,20);
			}
		}
		g.setColor(Color.BLACK);
		for(int i=0;i<=10;i++)
			g.drawLine(30,40+20*i,230,40+20*i);
		for(int i=0;i<=10;i++)
			g.drawLine(30+20*i, 40, 30+20*i, 240);
		//�������ڵ�������,�Ȼ������ٻ���
		for(int i=0;i<Disk.INUM;i++){
			int xx=i%10,yy=i/10;
			if(sb.bitindex[i]==1){
				tot2--;
				g.setColor(createRandomColor());
				g.fillRect(270+xx*20,40+yy*20,20,20);
			}
		}
		g.setColor(Color.BLACK);
		for(int i=0;i<=8;i++)
			g.drawLine(270,40+20*i,470,40+20*i);
		for(int i=0;i<=10;i++)
			g.drawLine(270+20*i,40,270+20*i,200);
		//���ô�����Ϣ��ʾ
		fp.ft.jt[7].setText(512*tot1+"B");
		fp.ft.jt[9].setText(tot1+"");
		fp.ft.jt[10].setText(tot2+"");
	}
}