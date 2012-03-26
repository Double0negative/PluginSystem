package com.cbp.double0negative.PS;

public class PluginData {
	
	private int id;
	private String name = "";
	private String loc = "";
	private boolean enabled ;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getLoc(){
		return loc;
	}
	public PluginData(int id, String name,String loc, boolean enabled){
		this.id = id;
		this.name = name;
		this.enabled = enabled;
		this.loc = loc;
	}
	
	
}
