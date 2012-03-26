package com.cbp.double0negative.PS;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.cbp.double0negative.PS.plugin.Plugin;

public class TabHandler  {

	private static TabHandler _instance = new TabHandler();
	private static JTabbedPane tp = new JTabbedPane();
	private static ArrayList<Plugin> plugins = new ArrayList<Plugin>();
	private TabChange a = new TabChange();
	
	private Plugin lastActive;
	private boolean first = true;
	private TabHandler(){

	}

	public void setup(){
		if(first)
			tp.addChangeListener(a);
		first= false;
	}
	public static TabHandler getInstance(){
		return _instance;
	}

	public void add(Plugin p){
		plugins.add(p);
		tp.add(p.getPane());
	}

	public void remove(Plugin p){
		plugins.add(p);
		tp.remove(p.getPane());
	}

	public JTabbedPane getTabbedPane(){
		return tp;
	}

	public void setTitle(Plugin p, String str){
		int index = 0;
		boolean found = false;
		for(Plugin d : plugins){
			if(d.equals(p)){
				found = true;
				break;
			}
			else{
				index++;
			}

		}
		if(found)
			tp.setTitleAt(index, str);
	}

	public void fireResize(int w, int h){
		for(Plugin d : plugins){
			d.onResize(w, h);
		}
	}
	
	public ArrayList<Plugin> getPluginList(){
		return plugins;
	}
	
	public void resetTabs(){
		plugins.clear();
		tp.removeAll();
		//_instance = new TabHandler();
		
	}
	
	protected Plugin getActivePlugin(){
		try{
		return plugins.get(tp.getSelectedIndex());
		}
		catch(Exception e){return null;}
	}
	
	protected void fireTabChange(){
		try{
		if(lastActive!=null)
		lastActive.onBlur();
		lastActive = plugins.get(tp.getSelectedIndex());
		lastActive.onFocus();
		}catch(Exception e){}
	}

	static class TabChange implements ChangeListener{

		public void stateChanged(ChangeEvent arg0) {
			ToolbarHandler.getInstance().update();
			TabHandler.getInstance().fireTabChange();
		}
		
	}

}
