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
	//模拟cpu运行
	@SuppressWarnings("static-access")
	public boolean executeProcess(){
		Queue queue = ReadyQueue.getQueue();
		if(queue.isEmpty()){	//就绪队列为空
			this.schedulingProcess();	//调度进程
			RunningPanel.getTips_PID().setText("");
			RunningPanel.getTips_priority().setText("");
			RunningPanel.getTips_timeNeed().setText("");
			RunningPanel.getTips_memoryNeed().setText("");
			RunningPanel.getBar().setValue(0);
			MainFrame.appendSystemLog("进程为空...");
			try{
				new Thread().sleep(2000);	//暂停两秒 模拟cpu执行时钟
			}catch(Exception ex){ex.printStackTrace();}
			return false;
		}else{
			queue.sortQueue1();	//对就绪队列按优先级排序
			PCB pcb = queue.getFirst();
			pcb.setState(PCB.state_running);
			int percent = pcb.getPecent();
			if(percent==100){
				percent = 0;
			}
			MainFrame.appendSystemLog("运行进程"+pcb.getPID()+"...");
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
			if(pcb.getTimeNeed()<=0){		//进程执行完毕
				pcb.setState(PCB.state_dead);
				this.PIDS.remove(String.valueOf(pcb.getPID()));	//回收pid
				MainFrame.appendSystemLog("结束进程"+pcb.getPID()+"...");
				this.resumeProcess(pcb);	//删除进程
				this.schedulingProcess();	//调度进程
			}else{
				pcb.setState(PCB.state_ready);
			}			
			queue.sortQueue1();	//对就绪队列按优先级排序
			return true;
		}
		
	}
	/*调度进程进入ReadyQueue*/
	public void schedulingProcess(){
		if(!ResumeQueue.getQueue().isEmpty()){	//解挂队列不为空
			//从解挂队列调度进程
			this.schedulingFromResume();
			RunningPanel.getMemoryPanel().repaint();	//重画内存分配图
			MainFrame.getQp_resume().refreshTable(ResumeQueue.getQueue());//刷新解挂队列
			
		}else if(!ReserveQueue.getQueue().isEmpty()){//后备队列不为空 
			//从后备队列调度进程
			this.schedulingFromReserve();
			RunningPanel.getMemoryPanel().repaint();	//重画内存分配图
			MainFrame.getQp_reserve().refreshTable(ReserveQueue.getQueue());//刷新后备队列
		}
	}
	/*从解挂队列调度进程进入就绪队列*/
	public boolean schedulingFromResume(){
		Queue queue = ResumeQueue.getQueue();
		queue.sortQueue1();		//对解挂队列按优先级排序
		PCB temp = queue.getHead();
		PCB temp2 = temp.getNext();
		boolean bool = false;
		while(temp!=null){
			boolean bl = this.partitionTable.assignMemory(temp);
			if(bl){
				//内存分配成功 将temp2插入到就绪队列
				temp.setNext(temp2.getNext());	//将temp2从解挂队列删除
				queue.setSize(queue.getSize()-1);//将队列长度减一
				ReadyQueue.getQueue().insert(temp2);		//插入到就绪队列
				temp2.setState(PCB.state_ready);
				MainFrame.appendSystemLog("从解挂队列调度进程"+temp2.getPID()+"...");
				bool = true;
				break;
			}else{
				//内存分配失败  对下一个pcb进行分配
				temp = temp.getNext();
				temp2 = temp.getNext();
			}
		}
		return bool;
	}
	/*从后备队列调度进程进入就绪队列*/
	public boolean schedulingFromReserve(){
		Queue queue = ReserveQueue.getQueue();
		queue.sortQueue1();		//对解挂队列按优先级排序
		PCB temp = queue.getHead();
		PCB temp2 = temp.getNext();
		boolean bool = false;
		while(temp2!=null){
			boolean bl = this.partitionTable.assignMemory(temp2);
			if(bl){
				//内存分配成功 将temp2插入到就绪队列
				temp.setNext(temp2.getNext());	//将temp2从后备队列删除
				queue.setSize(queue.getSize()-1);//将队列长度减一
				ReadyQueue.getQueue().insert(temp2);		//插入到就绪队列
				temp2.setState(PCB.state_ready);
				MainFrame.appendSystemLog("从后备队列调度进程"+temp2.getPID()+"...");
				bool = true;
				break;
			}else{
				//内存分配失败  对下一个pcb进行分配
				temp = temp.getNext();
				temp2 = temp.getNext();
			}
		}
		return bool;
	}
	/*结束一条进程*/
	public void resumeProcess(PCB pcb){
		ReadyQueue.getQueue().delete(pcb);			//从就绪队列删除
		this.partitionTable.resumeTableItem(pcb);	//在内存中删除
		RunningPanel.getMemoryPanel().repaint();	//重画内存分配图
	}
	//添加进程
	public void addProcess(int pid,int timeNeed,int priority,int memoryNeed){
		PCB pcb = new PCB(pid,timeNeed,priority,PCB.state_reserve,memoryNeed,-1,null);
		if(ReserveQueue.getQueue().isEmpty()){//后备队列为空
			boolean bool = partitionTable.assignMemory(pcb);	//分配内存
			if(bool){
				//内存分配成功 将pcb插入到就绪队列
				pcb.setState(PCB.state_ready);
				ReadyQueue.getQueue().insert(pcb);
			}else{
				//分配内存失败 将pcb插入到后备队列
				pcb.setState(PCB.state_reserve);
				ReserveQueue.getQueue().insert(pcb);
			}
		}else{//后备队列不为空  插入到后备队列
			pcb.setState(PCB.state_reserve);
			ReserveQueue.getQueue().insert(pcb);
		}
	}
	/*阻塞进程*/
	public boolean blockProcess(){
		Queue ready = ReadyQueue.getQueue();
		Queue block = BlockQueue.getQueue();
		PCB pcb = ready.getFirst();
		//就绪队列为空
		if(pcb==null){
			javax.swing.JOptionPane.showMessageDialog(null, "进程为空！");
			return false;
		}
		ReadyQueue.getQueue().delete(pcb);	//从就绪队列删除
		block.insert(pcb);			//插入到挂起队列中
		MainFrame.getQp_ready().refreshTable(ready);
		MainFrame.getQp_block().refreshTable(block);
		return true;
	}
	
	/*解除阻塞*/
	public void jiezuProgress() {
		Queue ready = ReadyQueue.getQueue();
		Queue block = BlockQueue.getQueue();
		PCB pcb = block.getFirst();
		block.delete(pcb);	//从阻塞队列删除
		pcb.setState(PCB.state_ready);
		ready.insert(pcb);	//插入到就绪队列
		MainFrame.getQp_ready().refreshTable(ready);
		MainFrame.getQp_block().refreshTable(block);
		MainFrame.appendSystemLog("进程"+pcb.getPID()+"解除阻塞...");	
	}


	/*挂起进程*/
	public boolean hangProcess(){
		Queue ready = ReadyQueue.getQueue();
		Queue hang = HangQueue.getQueue();
		PCB pcb = ready.getFirst();
		//就绪队列为空
		if(pcb==null){
			javax.swing.JOptionPane.showMessageDialog(null, "进程为空！");
			return false;
		}
		this.resumeProcess(pcb);	//从就绪删除进程 并释放内存
		pcb.setState(PCB.state_hang);
		hang.insert(pcb);			//插入到挂起队列中
		this.schedulingProcess();	//调度进程
		MainFrame.getQp_ready().refreshTable(ready);
		MainFrame.getQp_hang().refreshTable(hang);
		MainFrame.appendSystemLog("挂起进程"+pcb.getPID()+"...");
		return true;
	}
	/*解挂进程*/
	public void resumeProgress() {
		Queue resume = ResumeQueue.getQueue();
		Queue hang = HangQueue.getQueue();
		PCB pcb = hang.getFirst();
		hang.delete(pcb);	//从挂起队列删除
		pcb.setState(PCB.state_resume);
		resume.insert(pcb);	//插入到解挂队列
		MainFrame.getQp_resume().refreshTable(resume);
		MainFrame.getQp_hang().refreshTable(hang);	
		MainFrame.appendSystemLog("解挂进程"+pcb.getPID()+"...");
		
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