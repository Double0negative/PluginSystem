package com.cbp.double0negative.PS;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cbp.double0negative.PS.Task.Executer;
import com.cbp.double0negative.PS.Task.FutureTaskExecuter;
import com.cbp.double0negative.PS.Task.Task;
import com.cbp.double0negative.PS.plugin.Plugin;

public class TaskManager extends Thread {

	private static TaskManager _instance = new TaskManager();

	private int tickRate = 50;
	private int tick = 0;
	private int index = 0;

	private boolean systemLoaded = false;

	//private HashMap<Integer, Executer> taskLit = new HashMap<Integer, Executer>();
	private ConcurrentHashMap<Integer,Executer> taskList = new ConcurrentHashMap<Integer,Executer>();
	private TaskManager() {
		this.start();
		
	}

	public synchronized static TaskManager getInstance() {
		return _instance;
	}

	public synchronized void addRaw(Task t, int delay, int repeat) {
		add(new FutureTaskExecuter(t,delay,repeat));
	}

	public synchronized void setSystemLoaded(){
		if(!systemLoaded){
			index = 100;
			systemLoaded = true;
		}
	}

	public synchronized void add(Executer exec) {
		if(exec.getTask().getParent() == null && !(exec.getTask() instanceof Plugin)){
			System.err.println("ONLY PLUGIN CAN SET NULL PARENT");
		}
		taskList.put(index, exec);
		exec.getTask().setID(index);
		index++;
	}

	public int getTick() {
		return tick;
	}

	public void run() {
		while (true) {

			for (Executer t : taskList.values() ) {
				if (t instanceof FutureTaskExecuter) {
					((FutureTaskExecuter) t).checkAndRun(tick);
				}
			}
			try {
				sleep(tickRate);
			} catch (Exception e) {
			}
			tick++;
		}


	}

	public boolean forceStop(int i) {
		FutureTaskExecuter f = (FutureTaskExecuter) taskList.get(i);
		//f.forceStop();
		if (f.getTask().getMode() == Task.STATUS_STOPPED) {
			return true;
		}
		return false;
	}

	protected HashMap<Integer, Task> getTaskList(){
		HashMap<Integer, Task> list = new HashMap<Integer, Task>();

		Map<Integer, Task> map = new HashMap<Integer, Task>();
		for (Map.Entry<Integer, Executer> entry : taskList.entrySet()) {
			list.put( entry.getKey(), entry.getValue().getTask());
		}

		return list;
	}

	public void shutdown(){
		for (Executer t : taskList.values() ) {
			if (t instanceof FutureTaskExecuter) {
				((FutureTaskExecuter) t).shutDown();
			}


		}
	}
	public int getRunningTask(){
		int i = 0;
		for(Executer t : taskList.values()){
			if(t.getTask().getMode() == Task.STATUS_RUNNING)
				i++;
		}
		return i;
	}
	
	public int getSleepingTask(){
		int i = 0;
		for(Executer t : taskList.values()){
			if(t.getTask().getMode() == Task.STATUS_SLEEPING)
				i++;
		}
		return i;
	}
	public int getTotalTask(){
		return taskList.size();
	}

	protected void shutdownTask(){
		
		 taskList.clear();
	}
	protected void removeTask(int i){
		taskList.remove(i);
	}
	
	
	
}


