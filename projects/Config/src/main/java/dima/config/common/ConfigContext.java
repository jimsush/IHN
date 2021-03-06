package dima.config.common;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dima.config.view.netconfig.TopoView;

public class ConfigContext {
	
	public static JFrame mainFrame;
	public static JPanel rightPanel;
	public static TopoView topoView;
	
	public static int REDUNDANCY=1;
	
	/** max of total number of switches, 2 */
	public static int MAX_NUM_SWITCH=2;
	
	public static int MAX_NUM_PORTS_NODE=1;
	
	/** max of configuration table, 4*/
	public static int MAX_NUM_CFGTABLE=4;
	
	public static String version="";
	public static String date="";
	public static short fileNo=0;

}
