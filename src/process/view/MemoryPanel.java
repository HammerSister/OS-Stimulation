package process.view;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import process.util.BlockQueue;
import process.util.PartitionTable;
import process.util.ReadyQueue;
import process.vo.PCB;
import process.vo.Queue;

public class MemoryPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	public final static int width = 500;
	public final static int height = 50;
	public MemoryPanel(){
		this.setBounds(20, 170, MemoryPanel.width, MemoryPanel.height);
	}
	public void paint(Graphics g){
		g.clearRect(0, 0, MemoryPanel.width, MemoryPanel.height);
		g.setColor(Color.black);
		g.drawRect(0, 0, MemoryPanel.width-1, MemoryPanel.height-1);
		Queue ready = ReadyQueue.getQueue();
		Queue block = BlockQueue.getQueue();
		PCB temp = ready.getFirst();
		double maxSize = PartitionTable.memory_maxSize;
		while(temp!=null){
			if(temp.getColor()==null){
				temp.setColor(this.createRandomColor());
			}
			g.setColor(temp.getColor());
			double start = temp.getStartLocation();
			double need = temp.getMemoryNeed();
			int x = (int)(start/maxSize*MemoryPanel.width);
			int w = (int)((need/maxSize)*MemoryPanel.width);
			g.fillRect(x+1, 1, w, MemoryPanel.height-2);
			temp = temp.getNext();
		}
		temp = block.getFirst();
		while(temp!=null){
			if(temp.getColor()==null){
				temp.setColor(this.createRandomColor());
			}
			g.setColor(temp.getColor());
			g.fillRect((temp.getStartLocation()/PartitionTable.memory_maxSize)*MemoryPanel.width+1, 1, (temp.getMemoryNeed()/PartitionTable.memory_maxSize)*MemoryPanel.width, MemoryPanel.height-2);
			temp = temp.getNext();
		}
	}
	public Color createRandomColor(){
		return new Color((new Double(Math.random() * 128)).intValue() + 128,
				(new Double(Math.random() * 128)).intValue() + 128,
				(new Double(Math.random() * 128)).intValue() + 128);
	}
}