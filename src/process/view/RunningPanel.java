package process.view;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import process.scheduling.Scheduling;

public class RunningPanel extends JPanel implements ActionListener,ChangeListener{
	private static final long serialVersionUID = 1L;
	public final static int width = 1000;
	public final static int height = 1000;
	@SuppressWarnings("unused")
	private Scheduling scheduling;
	private JLabel tips_systemTime;
	private JLabel tips_1;
	private JLabel tips_2;
	private static JProgressBar bar;
	private JLabel tips_3;
	private JLabel kedu;
	private Timer timer;
	private static JLabel PID;
	private static JLabel priority;
	private static JLabel timeNeed;
	private static JLabel memoryNeed;
	private static JLabel tips_PID;
	private static JLabel tips_priority;
	private static JLabel tips_timeNeed;
	private static JLabel tips_memoryNeed;
	private static MemoryPanel memoryPanel;
	private int hour;
	private int minute;
	private int second;
	private int count = 0;
	public RunningPanel(Scheduling scheduling){
		this.scheduling = scheduling;
		this.setBounds(0, 0,RunningPanel.width, RunningPanel.height);
		this.setLayout(null);
		this.setPanel();
		timer = new Timer(20,this);
		timer.start();
	}
	/*计算系统时间*/
	public String calculateSystemTime(){
		String[] zero = new String[]{"00","0",""};
		String time = zero[String.valueOf(hour).length()] + hour + ":" +zero[String.valueOf(minute).length()] + minute + ":" +zero[String.valueOf(second).length()] + second;
		return time;
	}
	/*修改系统时间*/
	public void modifySystemTime(){
		if(second<59){
			second++;
		}else{
			second = 0;
			if(minute<59){
				minute++;
			}else{
				minute = 0;
				if(hour<59){
					hour++;
				}else{
					hour = 0;
				}
			}
		}
	}
	/*设置面板上的一些控件*/
	public void setPanel(){
		tips_systemTime = new JLabel("当前系统时间：00:00:00");
		tips_systemTime.setBounds(20, 0, 300, 20);
		this.add(tips_systemTime);
		tips_1 = new JLabel("正在运行进程...");
		tips_1.setBounds(20, 20, 150, 30);
		this.add(tips_1);
		PID = new JLabel("*进程名：");
		PID.setBounds(100, 52, 100, 20);
		this.add(PID);
		tips_PID = new JLabel("");
		tips_PID.setBounds(205, 52, 100, 20);
		this.add(tips_PID);
		priority = new JLabel("*优先级：");
		priority.setBounds(100, 70, 100, 20);
		this.add(priority);
		tips_priority = new JLabel("");
		tips_priority.setBounds(205, 70, 100, 20);
		this.add(tips_priority);
		timeNeed = new JLabel("*所需时间：");
		timeNeed.setBounds(100, 90, 100, 20);
		this.add(timeNeed);
		tips_timeNeed = new JLabel("");
		tips_timeNeed.setBounds(205, 90, 100, 20);
		this.add(tips_timeNeed);
		memoryNeed = new JLabel("*所需内存：");
		memoryNeed.setBounds(100, 108, 100, 20);
		this.add(memoryNeed);
		tips_memoryNeed = new JLabel("");
		tips_memoryNeed.setBounds(205, 108, 100, 20);
		this.add(tips_memoryNeed);
		tips_2 = new JLabel("运行进度...");
		tips_2.setBounds(300, 52, 100, 30);
		this.add(tips_2);
		tips_3 = new JLabel("内存分配情况：");
		tips_3.setBounds(20, 140, 150, 30);
		this.add(tips_3);
		kedu = new JLabel("0                  500                 1000                 1500                 2000(kb)");
		kedu.setBounds(20, 225, 1000, 30);
		this.add(kedu);
		bar = new JProgressBar();
		bar.setMinimum(0);
		bar.setMaximum(100);
		bar.setOrientation(JProgressBar.HORIZONTAL);
		bar.setBorderPainted(true);
		bar.setBackground(Color.pink);
		bar.setStringPainted(true);
		bar.setPreferredSize(new Dimension(200,20));
		bar.setBounds(300, 80, 300, 30);
		bar.addChangeListener(this);
		this.add(bar);
		memoryPanel = new MemoryPanel();
		this.add(memoryPanel);
	}
	public void actionPerformed(ActionEvent e) {
		count++;
		if(count==50){
			this.modifySystemTime();
			this.tips_systemTime.setText("当前系统时间：" + this.calculateSystemTime());
			count = 0;
		}
	}
	public void stateChanged(ChangeEvent e) {
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	public int getSecond() {
		return second;
	}
	public void setSecond(int second) {
		this.second = second;
	}
	public static JProgressBar getBar() {
		return bar;
	}
	public static void setBar(JProgressBar bar) {
		RunningPanel.bar = bar;
	}
	public static JLabel getTips_PID() {
		return tips_PID;
	}
	public static void setTips_PID(JLabel tips_PID) {
		RunningPanel.tips_PID = tips_PID;
	}
	public static JLabel getTips_priority() {
		return tips_priority;
	}
	public static void setTips_priority(JLabel tips_priority) {
		RunningPanel.tips_priority = tips_priority;
	}
	public static JLabel getTips_timeNeed() {
		return tips_timeNeed;
	}
	public static void setTips_timeNeed(JLabel tips_timeNeed) {
		RunningPanel.tips_timeNeed = tips_timeNeed;
	}
	public static JLabel getTips_memoryNeed() {
		return tips_memoryNeed;
	}
	public static void setTips_memoryNeed(JLabel tips_memoryNeed) {
		RunningPanel.tips_memoryNeed = tips_memoryNeed;
	}
	public static MemoryPanel getMemoryPanel() {
		return memoryPanel;
	}
	public static void setMemoryPanel(MemoryPanel memoryPanel) {
		RunningPanel.memoryPanel = memoryPanel;
	}
}