package process.scheduling;
import java.util.ArrayList;
import process.view.MainFrame;
import process.view.RunningPanel;
import process.util.BlockQueue;
import process.util.HangQueue;
import process.util.PartitionTable;
import process.util.ReadyQueue;
import process.util.ReserveQueue;
import process.util.ResumeQueue;
import process.vo.PCB;
import process.vo.Queue;

public class Scheduling{
	@SuppressWarnings("rawtypes")
	private static ArrayList PIDS = new ArrayList();
	private PartitionTable partitionTable;
	private boolean canRun;
	
	public Scheduling(){
		this.partitionTable = new PartitionTable();
		canRun = true;
	}
	//ģ��cpu����
	@SuppressWarnings("static-access")
	public boolean executeProcess(){
		Queue queue = ReadyQueue.getQueue();
		if(queue.isEmpty()){	//��������Ϊ��
			this.schedulingProcess();	//���Ƚ���
			RunningPanel.getTips_PID().setText("");
			RunningPanel.getTips_priority().setText("");
			RunningPanel.getTips_timeNeed().setText("");
			RunningPanel.getTips_memoryNeed().setText("");
			RunningPanel.getBar().setValue(0);
			MainFrame.appendSystemLog("����Ϊ��...");
			try{
				new Thread().sleep(2000);	//��ͣ���� ģ��cpuִ��ʱ��
			}catch(Exception ex){ex.printStackTrace();}
			return false;
		}else{
			queue.sortQueue1();	//�Ծ������а����ȼ�����
			PCB pcb = queue.getFirst();
			pcb.setState(PCB.state_running);
			int percent = pcb.getPecent();
			if(percent==100){
				percent = 0;
			}
			MainFrame.appendSystemLog("���н���"+pcb.getPID()+"...");
			RunningPanel.getTips_PID().setText(String.valueOf(pcb.getPID()));
			RunningPanel.getTips_priority().setText(String.valueOf(pcb.getPriority()));
			RunningPanel.getTips_timeNeed().setText(String.valueOf(pcb.getTimeNeed()));
			RunningPanel.getTips_memoryNeed().setText(String.valueOf(pcb.getMemoryNeed()));
			while(canRun){
				percent++;
				RunningPanel.getBar().setValue(percent);
				try{
					new Thread().sleep(20);
				}catch(Exception ex){ex.printStackTrace();}
				pcb.setPecent(percent);
				if(percent==100){
					pcb.setTimeNeed(pcb.getTimeNeed()-1);
					pcb.setPriority(pcb.getPriority()-1);
					break;
				}
			}			
			if(pcb.getTimeNeed()<=0){		//����ִ�����
				pcb.setState(PCB.state_dead);
				this.PIDS.remove(String.valueOf(pcb.getPID()));	//����pid
				MainFrame.appendSystemLog("��������"+pcb.getPID()+"...");
				this.resumeProcess(pcb);	//ɾ������
				this.schedulingProcess();	//���Ƚ���
			}else{
				pcb.setState(PCB.state_ready);
			}			
			queue.sortQueue1();	//�Ծ������а����ȼ�����
			return true;
		}
		
	}
	/*���Ƚ��̽���ReadyQueue*/
	public void schedulingProcess(){
		if(!ResumeQueue.getQueue().isEmpty()){	//��Ҷ��в�Ϊ��
			//�ӽ�Ҷ��е��Ƚ���
			this.schedulingFromResume();
			RunningPanel.getMemoryPanel().repaint();	//�ػ��ڴ����ͼ
			MainFrame.getQp_resume().refreshTable(ResumeQueue.getQueue());//ˢ�½�Ҷ���
			
		}else if(!ReserveQueue.getQueue().isEmpty()){//�󱸶��в�Ϊ�� 
			//�Ӻ󱸶��е��Ƚ���
			this.schedulingFromReserve();
			RunningPanel.getMemoryPanel().repaint();	//�ػ��ڴ����ͼ
			MainFrame.getQp_reserve().refreshTable(ReserveQueue.getQueue());//ˢ�º󱸶���
		}
	}
	/*�ӽ�Ҷ��е��Ƚ��̽����������*/
	public boolean schedulingFromResume(){
		Queue queue = ResumeQueue.getQueue();
		queue.sortQueue1();		//�Խ�Ҷ��а����ȼ�����
		PCB temp = queue.getHead();
		PCB temp2 = temp.getNext();
		boolean bool = false;
		while(temp!=null){
			boolean bl = this.partitionTable.assignMemory(temp);
			if(bl){
				//�ڴ����ɹ� ��temp2���뵽��������
				temp.setNext(temp2.getNext());	//��temp2�ӽ�Ҷ���ɾ��
				queue.setSize(queue.getSize()-1);//�����г��ȼ�һ
				ReadyQueue.getQueue().insert(temp2);		//���뵽��������
				temp2.setState(PCB.state_ready);
				MainFrame.appendSystemLog("�ӽ�Ҷ��е��Ƚ���"+temp2.getPID()+"...");
				bool = true;
				break;
			}else{
				//�ڴ����ʧ��  ����һ��pcb���з���
				temp = temp.getNext();
				temp2 = temp.getNext();
			}
		}
		return bool;
	}
	/*�Ӻ󱸶��е��Ƚ��̽����������*/
	public boolean schedulingFromReserve(){
		Queue queue = ReserveQueue.getQueue();
		queue.sortQueue1();		//�Խ�Ҷ��а����ȼ�����
		PCB temp = queue.getHead();
		PCB temp2 = temp.getNext();
		boolean bool = false;
		while(temp2!=null){
			boolean bl = this.partitionTable.assignMemory(temp2);
			if(bl){
				//�ڴ����ɹ� ��temp2���뵽��������
				temp.setNext(temp2.getNext());	//��temp2�Ӻ󱸶���ɾ��
				queue.setSize(queue.getSize()-1);//�����г��ȼ�һ
				ReadyQueue.getQueue().insert(temp2);		//���뵽��������
				temp2.setState(PCB.state_ready);
				MainFrame.appendSystemLog("�Ӻ󱸶��е��Ƚ���"+temp2.getPID()+"...");
				bool = true;
				break;
			}else{
				//�ڴ����ʧ��  ����һ��pcb���з���
				temp = temp.getNext();
				temp2 = temp.getNext();
			}
		}
		return bool;
	}
	/*����һ������*/
	public void resumeProcess(PCB pcb){
		ReadyQueue.getQueue().delete(pcb);			//�Ӿ�������ɾ��
		this.partitionTable.resumeTableItem(pcb);	//���ڴ���ɾ��
		RunningPanel.getMemoryPanel().repaint();	//�ػ��ڴ����ͼ
	}
	//��ӽ���
	public void addProcess(int pid,int timeNeed,int priority,int memoryNeed){
		PCB pcb = new PCB(pid,timeNeed,priority,PCB.state_reserve,memoryNeed,-1,null);
		if(ReserveQueue.getQueue().isEmpty()){//�󱸶���Ϊ��
			boolean bool = partitionTable.assignMemory(pcb);	//�����ڴ�
			if(bool){
				//�ڴ����ɹ� ��pcb���뵽��������
				pcb.setState(PCB.state_ready);
				ReadyQueue.getQueue().insert(pcb);
			}else{
				//�����ڴ�ʧ�� ��pcb���뵽�󱸶���
				pcb.setState(PCB.state_reserve);
				ReserveQueue.getQueue().insert(pcb);
			}
		}else{//�󱸶��в�Ϊ��  ���뵽�󱸶���
			pcb.setState(PCB.state_reserve);
			ReserveQueue.getQueue().insert(pcb);
		}
	}
	/*��������*/
	public boolean blockProcess(){
		Queue ready = ReadyQueue.getQueue();
		Queue block = BlockQueue.getQueue();
		PCB pcb = ready.getFirst();
		//��������Ϊ��
		if(pcb==null){
			javax.swing.JOptionPane.showMessageDialog(null, "����Ϊ�գ�");
			return false;
		}
		ReadyQueue.getQueue().delete(pcb);	//�Ӿ�������ɾ��
		block.insert(pcb);			//���뵽���������
		MainFrame.getQp_ready().refreshTable(ready);
		MainFrame.getQp_block().refreshTable(block);
		return true;
	}
	
	/*�������*/
	public void jiezuProgress() {
		Queue ready = ReadyQueue.getQueue();
		Queue block = BlockQueue.getQueue();
		PCB pcb = block.getFirst();
		block.delete(pcb);	//����������ɾ��
		pcb.setState(PCB.state_ready);
		ready.insert(pcb);	//���뵽��������
		MainFrame.getQp_ready().refreshTable(ready);
		MainFrame.getQp_block().refreshTable(block);
		MainFrame.appendSystemLog("����"+pcb.getPID()+"�������...");	
	}


	/*�������*/
	public boolean hangProcess(){
		Queue ready = ReadyQueue.getQueue();
		Queue hang = HangQueue.getQueue();
		PCB pcb = ready.getFirst();
		//��������Ϊ��
		if(pcb==null){
			javax.swing.JOptionPane.showMessageDialog(null, "����Ϊ�գ�");
			return false;
		}
		this.resumeProcess(pcb);	//�Ӿ���ɾ������ ���ͷ��ڴ�
		pcb.setState(PCB.state_hang);
		hang.insert(pcb);			//���뵽���������
		this.schedulingProcess();	//���Ƚ���
		MainFrame.getQp_ready().refreshTable(ready);
		MainFrame.getQp_hang().refreshTable(hang);
		MainFrame.appendSystemLog("�������"+pcb.getPID()+"...");
		return true;
	}
	/*��ҽ���*/
	public void resumeProgress() {
		Queue resume = ResumeQueue.getQueue();
		Queue hang = HangQueue.getQueue();
		PCB pcb = hang.getFirst();
		hang.delete(pcb);	//�ӹ������ɾ��
		pcb.setState(PCB.state_resume);
		resume.insert(pcb);	//���뵽��Ҷ���
		MainFrame.getQp_resume().refreshTable(resume);
		MainFrame.getQp_hang().refreshTable(hang);	
		MainFrame.appendSystemLog("��ҽ���"+pcb.getPID()+"...");
		
	}
	@SuppressWarnings("rawtypes")
	public static ArrayList getPIDS() {
		return PIDS;
	}
	@SuppressWarnings("rawtypes")
	public static void setPIDS(ArrayList pIDS) {
		PIDS = pIDS;
	}
	public PartitionTable getPartitionTable() {
		return partitionTable;
	}
	public void setPartitionTable(PartitionTable partitionTable) {
		this.partitionTable = partitionTable;
	}
	public boolean isCanRun() {
		return canRun;
	}
	public void setCanRun(boolean canRun) {
		this.canRun = canRun;
	}
}