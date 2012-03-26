package com.cbp.double0negative.PS;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.awt.peer.RobotPeer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.cbp.double0negative.PS.Task.FutureTaskExecuter;
import com.cbp.double0negative.PS.Task.Task;
import com.cbp.double0negative.PS.library.Busy;
import com.cbp.double0negative.PS.plugin.Plugin;

public class PluginSystem extends Task{
	
	public PluginSystem(){
		super(null, "System");
	}

	public static final String appDataLoc = System.getenv("APPDATA");
	public static final String appDir = appDataLoc + "\\pluginsloader";
	public static final String dataDir = appDir + "\\Data";
	private static TaskManager tm = TaskManager.getInstance();

	private static Task SystemTask;
	
	public static void main(String args[]) {
		
		SystemTask = new PluginSystem();
		tm.addRaw(SystemTask, 0 ,-1);


	}

	//PluginManager pm = new PluginManager();

	JFrame f = new JFrame("Plugins");
	JPanel p = new JPanel();
	TabHandler th = TabHandler.getInstance();
	JTabbedPane tp =  th.getTabbedPane();
	JPanel statusPane = new JPanel();
	JPanel statusMsgArea = new JPanel();
	JPanel statusTaskArea = new JPanel();
	JPanel statusRunningTaskArea = new JPanel();
	JPanel statusTime = new JPanel();
	JLabel statusMsgAreaT = new JLabel();
	JLabel statusRunningTaskT = new JLabel();
	JLabel statusTaskT = new JLabel();
	JLabel statusTimeT = new JLabel();
	JToolBar tool = new JToolBar();
	JButton resetB = new JButton("<html>Reset");
	JSeparator resetButtonSeparator = new JSeparator(SwingConstants.VERTICAL);
	JPanel toolbarPane = new JPanel();
	
	JMenuBar menu = new JMenuBar();
	
	
	public void execute() {
		
		
		
		
		
		
		
		tm.addRaw(new LayoutManager(this, "Layout Manager"), 20 ,10);
		tm.addRaw(new StatusUpdater(this, "Status Updater"), 20 ,1);

		tm.addRaw(new Task(this, "GUI Setup"){ 
			public void execute(){ 
			f.setSize(1024,700);
			f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			f.addWindowListener(new close());
			f.setJMenuBar(menu);
			menu.add(new JMenu("File"));
			p.setLayout(null);
			statusPane.setLayout(null);
			
			Border loweredbevel = BorderFactory.createLoweredBevelBorder();
			statusMsgArea.setBorder(loweredbevel);
			statusTaskArea.setBorder(loweredbevel);
			statusRunningTaskArea.setBorder(loweredbevel);
			statusTime.setBorder(loweredbevel);
			
			resetB.addActionListener(new Restart());
			
			tool.setLayout(null);
			tool.setFloatable(false);
			
			f.add(p);
			p.add(tp);
			p.add(statusPane);
			p.add(tool);
			tool.add(resetB);
			tool.add(resetButtonSeparator);
			tool.add(toolbarPane);
			statusPane.add(statusMsgArea);
			statusMsgArea.add(statusMsgAreaT,FlowLayout.LEFT);
			statusPane.add(statusTaskArea);
			statusTaskArea.add(statusTaskT,FlowLayout.LEFT);
			statusPane.add(statusRunningTaskArea);
			statusRunningTaskArea.add(statusRunningTaskT,FlowLayout.LEFT);
			statusPane.add(statusTime);
			statusTime.add(statusTimeT,FlowLayout.LEFT);


			f.setVisible(true);
			resetB.setText("<html><img src=\"http://www.freeiconsweb.com/Icons-show/cc_mono_icon_set_png/playback_reload.png\" height=\"20\" width=\"20\"");
		}}, 0, -1);
		
		File dataD = new File(dataDir);
		dataD.mkdir();
		
		
		
		
		

		ArrayList<Plugin> pluginsT = new ArrayList<Plugin>();
		pluginsT.add(new TaskMonitor());

		for(Plugin pl: loadPlugins()){
			pluginsT.add(pl);
		}

		for(Plugin pl: pluginsT){
			if(pl!=null){
				JPanel panel = new JPanel();
				panel.setBounds(0,0,p.getWidth(),p.getHeight());
				//System.out.println(pl.getPluginName());
				pl.init("Loading", panel);
				tm.addRaw(pl, 5, -1);
				th.add(pl);
			}
		}

		
		
		
		
		

		TabHandler.getInstance().setup();
		ToolbarHandler.getInstance().setPanel(toolbarPane);
		tm.addRaw(new Task(this, "#24ErTy"){ 
			public void execute(){ 
					ToolbarHandler.getInstance().update();
			}}, 20,-1);

	}

	
	public void shutdown(){
		
		for(Component c: f.getComponents()){
			c = null;
		}
		
		//f.setVisible(false);
		//f.dispose();
		
	}
	
	private Plugin[] loadPlugins() {

		String[] fileList = genFileList();
		//String[] nameList = genNameList(fileList);
		
		Plugin[] plugins = new Plugin[fileList.length];
		int index = 0;
		for (String s : fileList) {
			try{
				ClassLoader loader = URLClassLoader.newInstance(
					    new URL[] { new File(appDir+"\\"+ s).toURI().toURL() },
					    getClass().getClassLoader()
					);
					Class<?> clazz = Class.forName("com.cbp.plugin.Main", false, loader);
					Class<? extends Plugin> runClass = clazz.asSubclass(Plugin.class);
					// Avoid Class.newInstance, for it is evil.
					Constructor<? extends Plugin> ctor = runClass.getConstructor();
					Plugin p = ctor.newInstance();
					plugins[index] = p;
					index++;
			}
			
			
			catch(Exception e){}
			
		}
		return plugins;

	}

	public String[] genFileList() {
		
		File dir = new File(appDir);
		//System.out.println(dir);
		dir.mkdir();
	
		String[] list = dir.list();
		
		
		//System.out.println(appDir+"\\"+list[0]);
		return list;

	}
	public String[] genNameList(String[] list){
		
		String[] name = new String[list.length];
		int a = 0;
		for(String l :list){
			name[a] = l.replaceAll(".jar", "");
		}
		return name;
	}

	public String[] readDataFile() {
		ArrayList<String> a = new ArrayList<String>();

		try {
			File dataF = new File(appDir + "\\data");
			dataF.createNewFile();
			Scanner fi = new Scanner(dataF);

			while (fi.hasNext()) {
				a.add(fi.nextLine());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (String[]) a.toArray();
	}
	
	class close implements WindowListener{

		@Override
		public void windowActivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			TaskManager.getInstance().shutdown();
			fadeWindow();
			System.exit(0);
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public void fadeWindow(){

	  /*  try {
	        Robot robot = new Robot();
	        Rectangle captureSize = new Rectangle(f.getX(), f.getY(), f.getWidth(), f.getHeight());
	        BufferedImage img1 = robot.createScreenCapture(captureSize);
	        
	        f.setVisible(false);
        	Thread.sleep(50);

	        BufferedImage img2 = robot.createScreenCapture(captureSize);
	        f.setVisible(true);
	        
	        JFrame f2 = new JFrame();
	        JPanel p2 = new JPanel();
	        f2.setBounds(captureSize);

	        f2.add(p2);
	        f2.setUndecorated(true);
	        p2.setOpaque(false);
	        f2.setVisible(true);
	        Graphics2D g2 = (Graphics2D) p2.getGraphics();
	        
	        

        	 Create an ARGB BufferedImage 
        	int w = img1.getWidth(null);
        	int h = img1.getHeight(null);
        	BufferedImage bi = new
        	    BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        	BufferedImage lo92  = new
            	    BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        	Graphics2D g3 = (Graphics2D) lo92.getGraphics();
        	Graphics g = bi.getGraphics();
        	f.setVisible(false);
	        for(float a = 1 ; a>0; a -=.05){
	        	g.drawImage(img1, 0, 0, null);

	        	float[] scales = { 1f, 1f, 1f, a };
	        	float[] offsets = new float[4];
	        	RescaleOp rop = new RescaleOp(scales, offsets, null);

	        	 Draw the image, applying the filter 
	        	g3.drawImage(img2, 0, 0, null);
	        	g3.drawImage(bi, rop, 0, 0);
	        	g2.drawImage(lo92, 0 ,0 , null);
	        	
	        	Thread.sleep(1);
	        	
	        }
	        f2.setVisible(false);
	        System.exit(0);
	        
	    }
	    catch(Exception e) {
	    	e.printStackTrace();
	    }
*/
        for(float a = 1 ; a>0; a -=.01){

		com.sun.awt.AWTUtilities.setWindowOpacity(f,a );
    	try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        }
		
		
		
		
	}
	
	class LayoutManager extends Task{

		public LayoutManager(Task parent, String name) {
			super(parent, name);
		}
		
		int w,h;
		public  void execute(){
			if(w!=f.getWidth() || h!=f.getHeight()){
			
			p.setBounds(0,0,f.getWidth(),f.getHeight()-30);
			tp.setBounds(0, 32, p.getWidth(), p.getHeight()-92);
			
			statusPane.setBounds(0,p.getHeight()-60,p.getWidth(),30);
			statusMsgArea.setBounds(0, 0, statusPane.getWidth()-450, 30);
			statusTaskArea.setBounds(statusPane.getWidth()-300, 0, 150, 30);
			statusRunningTaskArea.setBounds(statusPane.getWidth()-450, 0, 150, 30);
			statusTime.setBounds(statusPane.getWidth()-150, 0, 150, 30);
			tool.setBounds(0, 0, p.getWidth(), 32);
			resetButtonSeparator.setBounds(107, 3,3,26);
			resetB.setBounds(5, 3, 100, 26);
			toolbarPane.setBounds(110, -2, tool.getWidth()-110, 32);
			
			
			
			TabHandler.getInstance().fireResize(tp.getWidth()-10, tp.getHeight()-10);
			}
			
			w = f.getWidth();
			h = f.getHeight();
			
		}
	}
	
	class StatusUpdater extends Task{

		public StatusUpdater(Task parent, String name) {
			super(parent, name);
			// TODO Auto-generated constructor stub
		}
		
		public void execute(){
			Calendar c = Calendar.getInstance();
			statusTimeT.setText(+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.YEAR)+"  "+c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+((c.get(Calendar.SECOND)<10)?"0":"")+c.get(Calendar.SECOND));
			statusRunningTaskT.setText("Running Task: "+TaskManager.getInstance().getRunningTask());
			statusTaskT.setText("Total Task: "+TaskManager.getInstance().getTotalTask());

		}
	}
	
	
	class Restart implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			restart();
		}
		
		
	}
	private void restart(){
		ArrayList<Plugin> list = TabHandler.getInstance().getPluginList();
		
		for(Plugin pl2: list){
			pl2.shutDown();
			TaskManager.getInstance().removeTask(pl2.getID());
			ArrayList<Task> taskls = pl2.getChildren();
			for(Task t2: taskls){
				t2.shutdown();
				TaskManager.getInstance().removeTask(t2.getID());

			}
		}
		
		SystemTask.shutdown();
		f.setVisible(false);
		f.dispose();
		for(Task y:SystemTask.getChildren()){
			y.shutdown();
			TaskManager.getInstance().removeTask(y.getID());

		}
		
		TabHandler.getInstance().resetTabs();
		TaskManager.getInstance().removeTask(SystemTask.getID());

		SystemTask = new PluginSystem();
		tm.addRaw(SystemTask, 0 ,-1);

		
	}

}
