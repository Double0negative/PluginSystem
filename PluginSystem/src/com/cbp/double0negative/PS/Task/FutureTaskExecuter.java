package com.cbp.double0negative.PS.Task;

import com.cbp.double0negative.PS.TaskManager;


public class FutureTaskExecuter implements Executer{

	private Task t;
	private int delay;
	private int repeat;
	private boolean isRunning = false;
	private boolean runOnce, ran = false;
	
	public FutureTaskExecuter(Task t, int delay , int repeatDelay){
		
		this.t = t;
		
		this.delay = TaskManager.getInstance().getTick() + delay;
		this.repeat = repeatDelay;
		if(repeatDelay == -1){
			runOnce = true;
		}
		
	}
	
	public boolean isRunning(){
		return isRunning;
	}
	
	
	public boolean checkAndRun(int tick){
		if(tick  >= delay && (t.getMode() != Task.STATUS_RUNNING || t.getMode()!=Task.STATUS_STOPPED ) && !ran){
			if(repeat != -1)
				delay = tick+ repeat;
			new ThreadRunner().start();
			if(runOnce){
				ran = true;
			}
			//System.out.println(t.getTaskName());

			return true;
		}
		return false;
	}
	
	protected void forceRun(){
		new ThreadRunner().start();
	}
	
	protected void forceStop(){
		//t.interrupt();
	}

	
	public void shutDown(){
		t.shutdown();
	}
	
	public Task getTask() {
		return t;
	}
	
	class ThreadRunner extends Thread{
		public void run(){
			t.startup();
		}
		
	}
	
	public void trigger(){
		new ThreadRunner().start();

	}
	
	public void setRepeatTimer(int i){
		repeat = i;
	}
	
	
}
