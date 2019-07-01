package process.view;
import java.awt.Color;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.Timer;
import javax.swing.UIManager;
import process.scheduling.Scheduling;
import process.util.BlockQueue;
import process.util.HangQueue;
import process.util.ReadyQueue;
import process.util.ReserveQueue;
import process.util.ResumeQueue;
import process.vo.PCB;

public class MainFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private Scheduling scheduling;
	private ReadyQueue readyQueue;
	private ReserveQueue reserveQueue;
	private ResumeQueue resumeQueue;
	private HangQueue hangQueue;
	private BlockQueue blockQueue;
	private static QueuePanel qp_ready;
	private static QueuePanel qp_block;
	private static QueuePanel qp_reserve;
	private static QueuePanel qp_resume;
	private static QueuePanel qp_hang;
	private JLabel tool;
	private PCB newPCB = new PCB();
	private Timer timer;
	private JMenuItem item1;
	private static boolean canRun;
	private static boolean stop;
	private static RunningPanel runningPanel;
	private boolean refresh;
	private static TextArea systemLog = new TextArea();
	JLabel lblPro, lblSav;
	JComboBox<String> cbbPro, cbbSav;
	boolean firstRun;
	@SuppressWarnings("static-access")
	public MainFrame() {
		super("进程调度与内存分配...");
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		firstRun=true;
		int sw=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	    int sh=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.setBounds(sw/2-350,sh/2-370,700,710);
		this.init();
		this.setFrame();
		this.canRun = true;
		this.stop = false;
		this.refresh = false;
		timer = new Timer(50, this);
		this.appendSystemLog("系统启动...");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
	}
	/* 初始化函数 */
	public void init() {
		readyQueue = new ReadyQueue();
		reserveQueue = new ReserveQueue();
		resumeQueue = new ResumeQueue();
		hangQueue = new HangQueue();
		blockQueue = new BlockQueue();
		scheduling = new Scheduling();
	}
	public void initData() {
		scheduling.addProcess(1,5, 5, 500);
		scheduling.addProcess(2,2, 8, 500);
		scheduling.addProcess(3,3, 7, 400);
		scheduling.addProcess(4,7, 2, 200);
		scheduling.addProcess(5,3, 6, 300);
		scheduling.addProcess(6,3, 3, 300);
		scheduling.addProcess(7,3, 3, 100);
		scheduling.addProcess(8,5, 6, 700);
		scheduling.addProcess(9,3, 7, 100);
		// ReadyQueue.getQueue().sortQueue1();
	}
	/* 设置界面 添加所需控件 */
	public void setFrame() {
		this.setLayout(null);
		/* 设置菜单 */
		JMenu menu1 = new JMenu("菜单");
		item1 = new JMenuItem("退出");
		JMenu menu2 = new JMenu("帮助");
		JMenuItem item2 = new JMenuItem("关于");
		menu1.add(item1);
		menu2.add(item2);
		item1.addActionListener(this);
		JMenuBar bar = new JMenuBar();
		bar.add(menu1);
		bar.add(menu2);
		this.setJMenuBar(bar);
		/* 设置工具栏 */
		tool = new JLabel("工具栏 | 新建 | 开始 | 暂停 | 挂起 | 阻塞 | 解挂| 激活 ");
		tool.setBounds(355, 60, 350, 20);
		this.add(tool);
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(true);
		ToolBarAction tba_new = new ToolBarAction("NEW", new ImageIcon(
				MainFrame.class.getResource(("/images/new.png"))), this);
		ToolBarAction tba_begin = new ToolBarAction("BEGIN", new ImageIcon(
				MainFrame.class.getResource(("/images/begin.png"))), this);
		ToolBarAction tba_pause = new ToolBarAction("STOP", new ImageIcon(
				MainFrame.class.getResource(("/images/block.png"))), this);
		ToolBarAction tba_hang = new ToolBarAction("HANG", new ImageIcon(
				MainFrame.class.getResource(("/images/hang.jpg"))), this);
		ToolBarAction tba_block = new ToolBarAction("BLOCK", new ImageIcon(
				MainFrame.class.getResource(("/images/block1.png"))), this);
		ToolBarAction tba_resume = new ToolBarAction("RESUME", new ImageIcon(
				MainFrame.class.getResource(("/images/resume.png"))), this);
		ToolBarAction tba_jiezu = new ToolBarAction("JIEZU", new ImageIcon(
				MainFrame.class.getResource(("/images/jiezu.png"))), this);
		JButton jb;
		jb = toolBar.add(tba_new);
		jb.setActionCommand("NEW");
		jb.setToolTipText("新建");
		jb.setFocusPainted(false);
		toolBar.addSeparator();
		jb = toolBar.add(tba_begin);
		jb.setActionCommand("BEGIN");
		jb.setToolTipText("开始");
		jb.setFocusPainted(false);
		toolBar.addSeparator();
		jb = toolBar.add(tba_pause);
		jb.setActionCommand("PAUSE");
		jb.setToolTipText("暂停");
		jb.setFocusPainted(false);
		toolBar.addSeparator();
		jb = toolBar.add(tba_hang);
		jb.setActionCommand("HANG");
		jb.setToolTipText("挂起");
		jb.setFocusPainted(false);
		toolBar.addSeparator();
		jb = toolBar.add(tba_block);
		jb.setActionCommand("BLOCK");
		jb.setToolTipText("阻塞");
		jb.setFocusPainted(false);
		toolBar.addSeparator();
		jb = toolBar.add(tba_resume);
		jb.setActionCommand("RESUME");
		jb.setToolTipText("解挂");
		jb.setFocusPainted(false);
		toolBar.addSeparator();
		jb = toolBar.add(tba_jiezu);
		jb.setActionCommand("JIEZU");
		jb.setToolTipText("激活");
		jb.setFocusPainted(false);
		toolBar.addSeparator();
		// this.add(toolBar,BorderLayout.NORTH);
		/* 设置左边 tab 面板 */
		JTabbedPane queues = new JTabbedPane(JTabbedPane.EAST);
		qp_ready = new QueuePanel(ReadyQueue.getQueue());
		qp_block = new QueuePanel(BlockQueue.getQueue());
		qp_reserve = new QueuePanel(ReserveQueue.getQueue());
		qp_hang = new QueuePanel(HangQueue.getQueue());
		qp_resume = new QueuePanel(ResumeQueue.getQueue());
		queues.addTab("就绪", qp_ready);
		queues.addTab("阻塞", qp_block);
		queues.addTab("后备", qp_reserve);
		queues.addTab("挂起", qp_hang);
		queues.addTab("解挂", qp_resume);
		/* 系统日志 */
		systemLog.setEditable(false);
		systemLog.setBackground(Color.white);
		/* 整体显示布局 */
		runningPanel = new RunningPanel(this.scheduling);
		JSplitPane jspL = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true,
				queues, systemLog);
		jspL.setDividerLocation(250);
		JSplitPane jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true,
				jspL, runningPanel);
		jsp.setDividerLocation(250);
		toolBar.setBounds(400, 20, 290, 35);
		jsp.setBounds(0, 100, 695, 700);
		// 选择调度算法
		lblPro = new JLabel("选择进程调度算法：");
		cbbPro = new JComboBox<String>();
		cbbPro.addItem("先来先服务算法(FCFS)");
		cbbPro.addItem("短作业优先算法(SJF)");
		cbbPro.addItem("动态优先级算法(DPS)");
		lblPro.setBounds(20, 20, 150, 30);
		cbbPro.setBounds(150, 20, 180, 25);
		cbbPro.addActionListener(this);
		this.add(lblPro);
		this.add(cbbPro);
		lblSav = new JLabel("选择内存分配算法：");
		cbbSav = new JComboBox<String>();
		cbbSav.addItem("首次适应算法(FF)");
		cbbSav.addItem("最佳适应算法(BF)");
		lblSav.setBounds(20, 50, 150, 30);
		cbbSav.setBounds(150, 52, 180, 25);
		cbbSav.addActionListener(this);
		this.add(lblSav);
		this.add(cbbSav);
		this.add(toolBar);
		this.add(jsp);
	}
	class ToolBarAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		MainFrame frame;
		public ToolBarAction(String name, Icon icon, MainFrame frame) {
			super(name, icon);
			this.frame = frame;
		}
		@SuppressWarnings("static-access")
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("NEW")) {
				new NewPCBDialog(this.frame);
				if (newPCB.getMemoryNeed() != 0) {
					frame.scheduling.addProcess(newPCB.getPID(),newPCB.getTimeNeed(), newPCB
							.getPriority(), newPCB.getMemoryNeed());
					qp_ready.refreshTable(ReadyQueue.getQueue());
					qp_reserve.refreshTable(ReserveQueue.getQueue());
					frame.appendSystemLog("新增一个进程...");
					newPCB.setTimeNeed(0);
					newPCB.setPriority(0);
					newPCB.setMemoryNeed(0);
				}
			} else if (e.getActionCommand().equals("BEGIN")) { // 开始运行
				if(firstRun==true){
					firstRun=false;
					frame.initData();
					qp_ready.refreshTable(readyQueue.getQueue());
					runningPanel.getMemoryPanel().repaint();
					timer.start();
				}else if (frame.stop) {
					frame.appendSystemLog("开始运行...");
					frame.stop = false;
					frame.scheduling.setCanRun(true);
				}
			} else if (e.getActionCommand().equals("PAUSE")) { // 暂停运行
				if (!frame.stop) {
					frame.appendSystemLog("暂停运行...");
					frame.scheduling.setCanRun(false);
					frame.stop = true;
				}
			} else if (e.getActionCommand().equals("HANG")) {
				frame.scheduling.hangProcess();
			} else if (e.getActionCommand().equals("BLOCK")) {
				frame.scheduling.blockProcess();
			}else if (e.getActionCommand().equals("RESUME")) {
				frame.scheduling.resumeProgress();
			}else if (e.getActionCommand().equals("JIEZU")) {
				frame.scheduling.jiezuProgress();
			}
		}
	}
	public static void appendSystemLog(String log) {
		String str = "[" + runningPanel.calculateSystemTime() + "]:" + log;
		systemLog.append(str + "\n");
	}

	@SuppressWarnings("static-access")
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == timer && canRun && !stop) {
			canRun = false;
			new RunProcess().start();
		} else if (e.getSource() == item1) {
			System.exit(0);
		}else if(e.getSource()==cbbPro){
			int ind=cbbPro.getSelectedIndex();
			readyQueue.getQueue().setType(ind);
			reserveQueue.getQueue().setType(ind);
			resumeQueue.getQueue().setType(ind);
			hangQueue.getQueue().setType(ind);
			blockQueue.getQueue().setType(ind);
		}else if(e.getSource()==cbbSav){
			int ind=cbbSav.getSelectedIndex();
			scheduling.getPartitionTable().setType(ind);
		}
	}

	class RunProcess extends Thread {
		public void run() {
			if (refresh) {
				qp_ready.refreshTable(ReadyQueue.getQueue());
				qp_reserve.refreshTable(ReserveQueue.getQueue());
			}
			refresh = scheduling.executeProcess();
			canRun = true;
		}
	}

	public static QueuePanel getQp_ready() {
		return qp_ready;
	}

	public static void setQp_ready(QueuePanel qp_ready) {
		MainFrame.qp_ready = qp_ready;
	}

	public static QueuePanel getQp_block() {
		return qp_block;
	}

	public static void setQp_block(QueuePanel qp_block) {
		MainFrame.qp_block = qp_block;
	}

	public static QueuePanel getQp_reserve() {
		return qp_reserve;
	}

	public static void setQp_reserve(QueuePanel qp_reserve) {
		MainFrame.qp_reserve = qp_reserve;
	}

	public static QueuePanel getQp_resume() {
		return qp_resume;
	}

	public static void setQp_resume(QueuePanel qp_resume) {
		MainFrame.qp_resume = qp_resume;
	}

	public static QueuePanel getQp_hang() {
		return qp_hang;
	}

	public static void setQp_hang(QueuePanel qp_hang) {
		MainFrame.qp_hang = qp_hang;
	}

	public PCB getNewPCB() {
		return newPCB;
	}

	public void setNewPCB(PCB newPCB) {
		this.newPCB = newPCB;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public static boolean isCanRun() {
		return canRun;
	}

	public static void setCanRun(boolean canRun) {
		MainFrame.canRun = canRun;
	}

	public RunningPanel getRunningPanel() {
		return runningPanel;
	}

	@SuppressWarnings("static-access")
	public void setRunningPanel(RunningPanel runningPanel) {
		this.runningPanel = runningPanel;
	}

	public static void main(String[] args) throws Exception {
		new MainFrame();
	}

}