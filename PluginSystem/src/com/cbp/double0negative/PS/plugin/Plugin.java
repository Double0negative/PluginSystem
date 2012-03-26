package com.cbp.double0negative.PS.plugin;

import javax.swing.JPanel;

import com.cbp.double0negative.PS.TabHandler;
import com.cbp.double0negative.PS.Task.Task;

public class Plugin extends Task{

	JPanel p;
	
	public Plugin(){
		super(null,"Plugin");
	}
	
	public void init(String name, JPanel p){
		//super.setName(name);
		this.p = p;
		TabHandler.getInstance().setTitle(this,name);
	}
	
	public void setPluginTitle(String str){
		TabHandler.getInstance().setTitle(this,str);

	}
	public void setPluginName(String name){
		super.setTaskName(name);
	}

	public void execute(){
		onEnable(p);
	}

	public void onEnable(JPanel p){
		
		
	}
	
	public void shutDown(){
		onDisable();
	}

	public void onDisable(){
		System.out.println("Plugin: "+ getPluginName() + "... No Shutdown Method!");
	}
	
	public void onResize(int w, int h){	}
	
	public void onBlur(){	}
	
	public void onFocus(){}
	public JPanel getPane(){
		return p;
	}
	
	public String getPluginName()	{
		return getTaskName();
	}
	public Plugin getPlugin(){
		return this;
	}
	
}
