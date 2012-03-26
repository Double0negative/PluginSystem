package com.cbp.double0negative.PS;

import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.cbp.double0negative.PS.plugin.Plugin;

public class ToolbarHandler {


	private static ToolbarHandler _instance = new ToolbarHandler();
	private static JPanel pane;
	private static HashMap<Plugin, ArrayList<Component>> comp = new HashMap<Plugin, ArrayList<Component>>();
	private Plugin lastPL;

	private ToolbarHandler(){

	}

	public static ToolbarHandler getInstance(){
		return _instance;
	}

	public void setPanel(JPanel p){
		pane = p;
		pane.setOpaque(false);
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		pane.setLayout(fl);
	}
	public void add(Plugin p, Component c){
		ArrayList <Component> cp = comp.get(p);
		if(cp==null){
			cp = new ArrayList<Component>();
		}
		cp.add(c);
		comp.put(p, cp );
	}

	protected void update(){
		try{
			Plugin pl = TabHandler.getInstance().getActivePlugin();
			System.out.println(pl.getPluginName());
			try{
				if(lastPL!=null){
					for(Component c: comp.get(lastPL)){
						pane.remove(c);
					}
				}
			}catch(Exception e){}
			lastPL = pl;

			try{
				for(Component c: comp.get(pl)){
					pane.add(c);
				}
			}
			catch(Exception e){}
			pane.repaint();
		}catch(Exception e){}
	}
}
