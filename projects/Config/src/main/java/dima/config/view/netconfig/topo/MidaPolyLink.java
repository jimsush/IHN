package dima.config.view.netconfig.topo;

import twaver.Link;
import twaver.Node;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class MidaPolyLink extends Link{

	private static final long serialVersionUID = 7019742136981521866L;

	public MidaPolyLink(){
		super();
	}
	
	public MidaPolyLink(Node from, Node to){

		super(from, to);
		//super(id);
		this.setFrom(from);
		this.setTo(to);
		this.init();
	}
	
	@Override
	public String getUIClassID(){
		return MidaPolyLinkUI.class.getName();
	};

	/////////////////////////////////
	MidaPolyLink link;
	int flag = 0;

	int flowingGapUp = -1;
	int flowingGapDn = -1;

	int flowingOffset = 4;
	Color flowingColor = Color.BLACK;

	Color linkColor = new Color(128,128,128);

	public int getFlowingGapUp() {
		return flowingGapUp;
	}

	public void setFlowingGapUp(int flowingGapUp) {
		this.flowingGapUp = flowingGapUp;
		this.updateUI();
	}

	public int getFlowingGapDn() {
		return flowingGapDn;
	}

	public void setFlowingGapDn(int flowingGapDn) {
		this.flowingGapDn = flowingGapDn;
		this.updateUI();
	}

	public int getFlowingOffset() {
		return flowingOffset;
	}

	public void setFlowingOffset(int flowingOffset) {
		this.flowingOffset = flowingOffset;
		this.updateUI();
	}

	public Color getFlowingColor() {
		return flowingColor;
	}

	public void setFlowingColor(Color flowingColor) {
		this.flowingColor = flowingColor;
		this.updateUI();
	}

//		public CustomFlowingLink(Node node1, Node node2) {
//			super(node1, node2);
//			this.init();
//		}

	private void init(){
		link = this;
		this.putLinkFlowing(true);
		this.putLinkWidth(3);
		this.putLinkFlowingWidth(0);
		this.putLinkOutlineWidth(0);
		this.putLinkColor(linkColor);
		this.putLinkFlowingColor(new Color(128,128,128));
		this.putLinkAntialias(true);
		//dynamic();

	}

	public void dynamic(){
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){
			public void run() {
				flag = (int)(Math.random()*4);
				//flag = (flag + 1)%4;
				switch(flag){
					case 0:
						link.setFlowingGapUp(10);
						break;
					case 1:
						link.setFlowingGapUp(20);
						break;
					case 2:
						link.setFlowingGapUp(40);
						break;
					case 3:
						link.setFlowingGapUp(60);
						break;
					default:
						link.setFlowingGapUp(20);
						break;
				}
			}
		},0,500);
	}

}
