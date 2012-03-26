package com.cbp.double0negative.PS.Task;

import java.util.ArrayList;
import java.util.Date;

import com.cbp.double0negative.PS.TabHandler;

public class Task {

	String name;
	String s = "";
	
	public static final int STATUS_NONE = 0;
	public static final int STATUS_SLEEPING = 1;
	public static final int STATUS_RUNNING = 2;
	public static final int STATUS_STOPPED = 3;
	
	
	
	private int status = 0;
	private int taskid;
	
	private long iterTime = 0;
	private long timmerStart = 0;
	private long totalTime = 0;
	private boolean timmerRunning = false;
	
	private ArrayList<Task>childList = new ArrayList<Task>();
	private Task parent = null;
	
	
	
	public Task(Task parent, String name){
		this.parent = parent;
		if(parent!=null)
			parent.addChildTask(this);
		this.name = name;
	}
	
	public void setTaskName(String name){
		this.name = name;
	}
	public String getTaskName(){
		return name;
	}
	long time = -1;

	public void startup(){
			time = new Date().getTime();
			status = STATUS_RUNNING;
			execute();
			long time2 = new Date().getTime() - time;
			iterTime = time2;
			totalTime = totalTime + time2;
			status = STATUS_SLEEPING;


	}
	
	public long getTotalRunTime(){
		if(status == STATUS_RUNNING){
			return  (totalTime) + (new Date().getTime() - time);
		}
		else if(time == -1)
			return 0;
		else
			return totalTime;
	}
	public long getLastRunTime(){
		return iterTime;
	}
	
	
	
	protected void execute(){
		
		
	}
	
	public void setID(int i){
		taskid = i;
	}
	public int getID(){
	return taskid;
}
	
	public void setStatus(String s){
		this.s = s;
	}
	
	public String getStatus(){
		return s;
	}
	
	public void shutdown(){
		System.out.println("Task: "+ getTaskName() + "... No Shutdown Method!");

		
	}
	
	public int getMode(){
		return status;
	}
	
	
	public void addChildTask(Task t){
		//t.setParent(parent);
		childList.add(t);
	}
	
	//public Task newChildTask(Task tk ,String name){
	//	Task t  = new Task(name);
	//	addChildTask(tk, t);
	//	return t;
	//}
	
	public void removeChildTask(Task t){
		childList.remove(t);
	}
	
	public ArrayList<Task> getChildren(){
		return childList;
	}
	
	public void setParent(Task t){
		parent = t;
	}
	
	
	public Task getParent(){
		return  parent;
	}
	
	public void fireInterrupted(){
		status = STATUS_STOPPED;
	}
	public static String modeToString(int i){
		String st;
		switch(i){
		case STATUS_NONE:
			st=  "Unknown";
			break;
		case STATUS_RUNNING:
			st =  "Running";
			break;
		case STATUS_SLEEPING:
			st =  "Sleeping";
			break;
		case STATUS_STOPPED:
			st = "Stopped";
			break;
		default:
			st = "Unknown";
			break;
		}
		return st;
			
		
		}

	}

