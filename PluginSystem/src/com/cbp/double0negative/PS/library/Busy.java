package com.cbp.double0negative.PS.library;

import com.cbp.double0negative.PS.Task.Task;

public class Busy extends Task {

	public Busy(Task parent, String name) {
		super(parent, "BusyLoop");
	}
	
	
	
	
	
	public void execute(){
		while(true){
			try{Thread.sleep(20);}catch(Exception e){}
		}
	}

}
