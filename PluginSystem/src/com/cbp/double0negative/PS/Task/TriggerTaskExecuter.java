package com.cbp.double0negative.PS.Task;

public class TriggerTaskExecuter{

	Task t;
	FutureTaskExecuter ft;
	
	public TriggerTaskExecuter(Task t){
		this.t = t;
		
		ft = new FutureTaskExecuter( t, Executer.WAIT_INF, 0);
		
		
	}
	
	public void trigger(){
		ft.forceRun();
	}
	
	
	
	
}
