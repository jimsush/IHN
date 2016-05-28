package com.sample.topo;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import twaver.Card;
import twaver.Link;
import twaver.Node;
import twaver.Port;
import twaver.TDataBox;
import twaver.TWaverConst;
import twaver.TWaverUtil;
import twaver.network.TNetwork;

import com.sample.topo.common.TopoConst;
import com.sample.topo.common.TopoUtils;

public class SampleFrame extends JFrame{
	
	private static final long serialVersionUID = 8890596822812284449L;
	private TDataBox box = null;
	private TNetwork network = null;
	private JPanel networkPanel = new JPanel(new BorderLayout());
	
	public SampleFrame() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().add(networkPanel,BorderLayout.CENTER);
		
		TopoContext.init(this);
		box=TopoContext.getBox();
		network=TopoContext.getNetwork();
		
		networkPanel.add(network, BorderLayout.CENTER);
		
		doSample();
	}
	
	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		frame.setSize(1000,680);
		frame.setTitle("TWaver Tutorial");
		TWaverUtil.centerWindow(frame);
		frame.setVisible(true);
	}
	
	private void doSample() { 
		Node ipm1=createIPM("ipm", 6 ,3, true);
		Port port1=(Port)(box.getElementByID(ipm1.getID().toString()+"_port2"));
		
		Node switch1=createSwitch("switch", 24);
		Port port2=(Port)(box.getElementByID(switch1.getID()+"_port15"));
		
		Link link = new Link(port1, port2);
		link.setLinkType(TWaverConst.LINK_TYPE_BOTTOM);
		link.putClientProperty("type", TopoConst.PRE_NAME_LINK);
		box.addElement(link);
	}
	
	private Node createIPM(String id, int numOfPartition, int numOfPort, boolean locked){
		Node ipm1 = new Node(id);
		ipm1.setImage(TopoUtils.getImageURL("ipm.png"));
		ipm1.setLocation(50,50);
		ipm1.putClientProperty("type", TopoConst.PRE_NAME_NE);
		ipm1.putClientProperty("subtype", TopoConst.PRE_NAME_IPM);
		ipm1.setToolTipText("IPM");
		box.addElement(ipm1);
		
		Card partition1 =null;
		int numOfRows=numOfPartition/2;
		
		for(int row=0; row<numOfRows; row++){
			for(int col=0; col<2; col++){
				partition1 = new Card(id+"_partition"+(row*2)+col); 
				partition1.setName("P"+(row*2+col));
				partition1.setToolTipText("分区"+(row*2+col));
				partition1.putClientProperty("type", TopoConst.PRE_NAME_PARTITION);
				partition1.putLabelPosition(1);
				
				partition1.setImage(TopoUtils.getImageURL("partition.png"));
				int hgap=(ipm1.getHeight()-partition1.getHeight()*numOfRows)/(numOfRows+1);
				int wgap=(ipm1.getWidth()-partition1.getWidth()*2)/3;
				partition1.setLocation(ipm1.getLocation().x+(col+1)*wgap+col*partition1.getWidth(),
						               ipm1.getLocation().y+ hgap*(row+1) + row*partition1.getHeight());
				partition1.setHost(ipm1);
				partition1.setParent(ipm1);
				box.addElement(partition1);
			}	
		}
		
		Port task1 = new Port(partition1.getID().toString()+"_task1");
		task1.setName("T1");
		task1.setToolTipText("任务1");
		task1.putLabelPosition(1);
		task1.putClientProperty("type", TopoConst.PRE_NAME_TASK);
		task1.setImage(TopoUtils.getImageURL("task.png"));
		task1.setLocation(partition1.getLocation().x+5,partition1.getLocation().y+5);
		task1.setHost(partition1);
		task1.setParent(partition1);
		box.addElement(task1);
		
		Port port1 =null;
		int NUM_P2=numOfPort;
		for(int i=0;i<NUM_P2; i++){
			port1 = new Port(id+"_port"+i);
			port1.setImage(TopoUtils.getImageURL("fe-d.png"));
			port1.putClientProperty("type", TopoConst.PRE_NAME_PORT);
			int wgap=(ipm1.getWidth()-NUM_P2*port1.getWidth())/(NUM_P2+1);
			
			port1.setLocation(ipm1.getLocation().x+wgap+i*(port1.getWidth()+wgap), ipm1.getLocation().y+ipm1.getHeight());
			port1.setHost(ipm1);
			port1.setParent(ipm1);
			box.addElement(port1);
		}
		return ipm1; 
	}
	
	private Node createSwitch(String id, int portNum){
		Node switch1 = new Node(id);
		switch1.setToolTipText("交换机");
		switch1.setImage(TopoUtils.getImageURL("switch.png"));
		switch1.setLocation(500,260);
		switch1.putClientProperty("type", TopoConst.PRE_NAME_NE);
		switch1.putClientProperty("subtype", TopoConst.PRE_NAME_SWITCH);
		
		box.addElement(switch1);
		
		Port port2=null;
		int NUM_P=portNum/2;
		for(int i=0;i<NUM_P;i++){
			int portNo=i+1;
			port2 = new Port(id+"_port"+portNo);
			port2.setImage(TopoUtils.getImageURL("fe-u.png"));
			port2.putClientProperty("type", TopoConst.PRE_NAME_PORT);
			int wgap=(switch1.getWidth()-NUM_P*port2.getWidth())/(NUM_P+1);
			port2.setLocation(switch1.getLocation().x + (i+1)*wgap+i*port2.getWidth(), switch1.getLocation().y-port2.getHeight());
			port2.setHost(switch1);
			port2.setParent(switch1);
			port2.setToolTipText("Port"+portNo);
			box.addElement(port2);
		}
		for(int i=0;i<NUM_P;i++){
			int portNo=i+NUM_P+1;
			port2 = new Port(id+"_port"+portNo);
			port2.setImage(TopoUtils.getImageURL("fe-d.png"));
			port2.putClientProperty("type", TopoConst.PRE_NAME_PORT);
			int wgap=(switch1.getWidth()-NUM_P*port2.getWidth())/(NUM_P+1);
			port2.setLocation(switch1.getLocation().x + (i+1)*wgap+i*port2.getWidth(), switch1.getLocation().y+switch1.getHeight());
			port2.setHost(switch1);
			port2.setParent(switch1);
			port2.setToolTipText("Port"+portNo);
			box.addElement(port2);
		}
		return switch1;
	}

}
