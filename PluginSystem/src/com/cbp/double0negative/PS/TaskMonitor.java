package com.cbp.double0negative.PS;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.cbp.double0negative.PS.Task.FutureTaskExecuter;
import com.cbp.double0negative.PS.Task.Task;
import com.cbp.double0negative.PS.plugin.Plugin;

public class TaskMonitor extends Plugin {

	JTextArea ta = new JTextArea();
	FutureTaskExecuter updater = new FutureTaskExecuter(new updater(this, "Updater") , 0, 20);
	JSlider spin = new JSlider();
	JLabel text = new JLabel("Refresh Timer:");
	public void onEnable(JPanel p){
		super.setPluginTitle("Task Monitor");
		super.setPluginName("Task Monitor");
		p.setLayout(null);
		ta.setBounds(0,0,p.getWidth(),p.getHeight());
		p.add(ta);
		
		TaskManager.getInstance().add(updater);
		ToolbarHandler.getInstance().add(this, text);
		ToolbarHandler.getInstance().add(this, spin);

		
		
		spin.addChangeListener(new SetTime());
		
	}
	
	class SetTime implements ChangeListener{

		@Override
		public void stateChanged(ChangeEvent arg0) {
			updater.setRepeatTimer(spin.getValue());
			
			
		}
		
	}
	
	
	class updater extends Task{
		
		public updater(Task as,String name) {
			super(as,name);
		}

		public void execute(){
			ta.setText("");
			
			ta.append("Name \t\t Status \t\t Runtime(ms) \n-------------------------------------------------------------------------------------------------------------------------------\n\n");
			for(Task t: TaskManager.getInstance().getTaskList().values()){
				if(t.getParent()== null){
					ta.append(t.getTaskName()+": "+t.getStatus()+" \t\t"+Task.modeToString(t.getMode())+" \t\t"+t.getTotalRunTime()+'\n');

					boolean children  = getChildrenAndPrint(t,1);
					//if(children)
					ta.append("\n");

					
				}
			}
			//ta.append(TaskManager.getInstance().getTick()+"");

			
		}
		
		
	}
	
	public boolean getChildrenAndPrint(Task t, int level){
		boolean children = false;
		for(Task t1: t.getChildren()){
			children = true;
			for(int a = 0; a<level;a++)
				ta.append("   ");
			ta.append(t1.getTaskName()+": "+t1.getStatus()+((t1.getTaskName().length() >10)? "\t":"\t\t")+Task.modeToString(t1.getMode())+" \t\t"+t1.getTotalRunTime()+"\n");
			getChildrenAndPrint(t1,level+1);
			
		}
		return children;

	}
	
	public void onResize(int w, int h){
		ta.setSize(w, h);
	}
	
}
