package dima.config.view.netconfig.topo;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dima.config.common.ConfigContext;
import dima.config.common.ConfigUtils;
import dima.config.common.models.NodeDevice;
import dima.config.common.models.SwitchDevice;
import dima.config.common.services.ConfigDAO;
import dima.config.common.services.ServiceFactory;
import dima.config.view.netconfig.UpdateCallback;
import twaver.Element;
import twaver.Link;
import twaver.Node;
import twaver.Port;
import twaver.TDataBox;
import twaver.TWaverConst;
import twaver.network.TNetwork;

public class TopoServiceImpl implements UpdateCallback{

	private volatile String iconScale="-m.png";
	private volatile int labelFontSize=12;
	
	private TDataBox box;
	private TNetwork network;
	private ConfigDAO dao;
	
	public TopoServiceImpl(TDataBox box, TNetwork network){
		this.box=box;
		this.network=network;
		this.dao=ServiceFactory.getService(ConfigDAO.class);
		
		System.out.println(this.network.getClass().getName());
	}

	@SuppressWarnings({ "rawtypes"})
	@Override
	public NodeDevice addNode(NodeDevice nodeDev, String oldId) {
		Node node2=null;
		if(oldId==null){
			node2=new Node(ConfigUtils.makeNodeTwaverID(nodeDev.getNodeName(), nodeDev.getType()));
			box.addElement(node2);
		}else{
			node2=(Node) box.getElementByID(ConfigUtils.makeNodeTwaverID(oldId, nodeDev.getType()));
		}
		
		Font labelFont = new Font(null, Font.PLAIN, labelFontSize);
		
		node2.setName("节点"+nodeDev.getNodeName());
		node2.putLabelPosition(1);
		node2.putLabelColor(Color.BLUE);
		node2.putLabelFont(labelFont);
		
		node2.setImage(ConfigUtils.getImageURLString(getIconName("ipm"))); 
		
		boolean leftOfSwitch=nodeInLeft(nodeDev.getPortNo());

		List existedNodes=getNodesInSameSide(leftOfSwitch, ConfigUtils.TYPE_STR_NODE);
		int ipmCount=existedNodes.size();
		
		int posX= (leftOfSwitch? 120: 780);
		int posY=60+ipmCount*(node2.getHeight()+20);
		node2.setLocation(posX, posY);
		
		String portIconURL = ConfigUtils.getImageURLString(getIconName("port"));
		int normalPortNum=ConfigContext.MAX_NUM_PORTS_NODE;
		Port[] ipmPorts=new Port[normalPortNum];
		for(int i=0; i<normalPortNum; i++){
			int portNo=i+1;
			String portCnName="FE"+portNo;
			
			Port port1 = new Port(ConfigUtils.buildPortId(nodeDev.getID(), portNo));
			ipmPorts[i]=port1;
			
			port1.setToolTipText(portCnName);
			port1.setImage(portIconURL);
			port1.setName(""+portNo);
			port1.putLabelPosition(1);
			port1.putLabelFont(labelFont);
			
			int hgap=(node2.getHeight()-normalPortNum*port1.getHeight())/(normalPortNum+1);
			if(leftOfSwitch){
				port1.setLocation(node2.getLocation().x+node2.getWidth(), node2.getLocation().y+hgap+i*(port1.getHeight()+hgap));
			}else{
				port1.setLocation(node2.getLocation().x-port1.getWidth(), node2.getLocation().y+hgap+i*(port1.getHeight()+hgap));
			}
			
			port1.setHost(node2);
			port1.setParent(node2);
			
			box.addElement(port1);
		}
		
		Map<String, String> data2=new HashMap<>();	
		if(nodeDev.getType()==ConfigUtils.TYPE_NMU){
			data2.put("type", ConfigUtils.TYPE_STR_NMU);
		}else{
			data2.put("type", ConfigUtils.TYPE_STR_NODE);
			data2.put("left", leftOfSwitch ? "true" : "false");
		}
		node2.setUserObject(data2);
		
		// add links
		List switchesInSameSide=getNodesInSameSide(leftOfSwitch, ConfigUtils.TYPE_STR_SW);
		Element switchA=null;
		if(switchesInSameSide.size()>0){
			switchA=(Element)switchesInSameSide.get(0);
		}
		Element switchB=null;
		if(switchesInSameSide.size()>1){
			switchB=(Element)switchesInSameSide.get(1);
		}
		
		Element tmp=null;
		if(switchB!=null && switchA.getLocation().x > switchB.getLocation().x){
			tmp=switchB;
			switchB=switchA;
			switchA=tmp;
		}
		
		if(nodeDev.getPortNo()>0){
			String[] swPort = ConfigUtils.getSwitchNamePortId(nodeDev.getPortNo());
			if(swPort[0]!=null){
				String portId=ConfigUtils.buildPortId(swPort[0], swPort[1]);
				Element dstSwPort = box.getElementByID(portId);
				
				Link link=new MidaPolyLink(ipmPorts[0], (Port)dstSwPort);
				link.putLinkWidth(1);
				link.putLinkColor(Color.BLACK); 
				link.putLinkOutlineWidth(0);
				link.setLinkType(TWaverConst.LINK_TYPE_XSPLIT);
				
				float offsetx=0.45f;
				offsetx+= 0.03f*ipmCount;
				if(offsetx>=1.0f){
					offsetx=0.8f;
				}
				link.putClientProperty(ConfigUtils.PROP_LINK_ADJUST_RATIO, offsetx);
				
				try{
					box.addElement(link);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
		
		return nodeDev;
	}
	
	@SuppressWarnings("rawtypes")
	private boolean nodeInLeft(int anetPort){
		int onePort=0;
		if(anetPort>0){ //a>0, b>0 | b=0
			onePort=anetPort;
		}
		if(onePort==0){ //don't connect any switches, so put in left
			return true;
		}
		
		int switchId=(onePort>>16) & 0xffff;
		List<SwitchDevice> swes = dao.readAllSwitchDevices(true);
		String switchName=null;
		for(SwitchDevice sw : swes){
			if(switchId==sw.getLocalDomainID()){
				switchName=sw.getSwitchName();
				break;
			}
		}
		if(switchName==null){
			return true;
		}
		Element switchEle = box.getElementByID(switchName);
		Object userObject = switchEle.getUserObject();
		Map data1 = (Map)userObject;
		Object left1=data1.get("left");
		return "true".equals(left1);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List getNodesInSameSide(boolean leftOfSwitch, String type){
		List eles = box.getElementsByType(Node.class);
		List nodesInSameSide=new ArrayList();
		for(Object obj : eles){
			Element ele=(Element)obj;
			Object data = ele.getUserObject();
			if(data instanceof Map){
				Map map=(Map)data;
				Object t=map.get("type");
				if(type.equals(t)){
					Object l = map.get("left");
					boolean isLeft=true;
					if(!"true".equals(l)){
						isLeft=false;
					}
					if(isLeft==leftOfSwitch){
						nodesInSameSide.add(ele);
					}
				}
			}
		}
		return nodesInSameSide;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public SwitchDevice addSwitch(SwitchDevice switchDev, String oldId) {
		Node switch1=null;
		int numOfSw=0;
		
		if(oldId==null){
			// check whether this ID has existed
			List eles = box.getElementsByType(Node.class);
			for(Object obj : eles){
				Element ele=(Element)obj;
				Object propVal = ConfigUtils.getUserObjectProp(ele, "type");
				if(ConfigUtils.TYPE_STR_SW.equals(propVal)){
					numOfSw++;
				}
			}
			
			switch1=new Node(switchDev.getSwitchName());
			box.addElement(switch1);
		}else{ // never run to this branch
			switch1=(Node) box.getElementByID(oldId);
		}

		Font labelFont = new Font(null, Font.PLAIN, labelFontSize);
		
		switch1.setName("交换机"+switchDev.getSwitchName());
		switch1.putLabelPosition(1);
		switch1.putLabelColor(Color.BLUE);
		switch1.putLabelFont(labelFont);
		switch1.setToolTipText("<html><p>"+switch1.getName()+"<p>本地域ID:"+switchDev.getLocalDomainID()+"</html>");
		if(switchDev.getPortNumber()<=16){
			switch1.setImage(ConfigUtils.getImageURLString(getIconName("switch16")));
		}else if(switchDev.getPortNumber()<=24){
			switch1.setImage(ConfigUtils.getImageURLString(getIconName("switch24")));
		}else if(switchDev.getPortNumber()<=32){
			switch1.setImage(ConfigUtils.getImageURLString(getIconName("switch32")));
		}else{
			switch1.setImage(ConfigUtils.getImageURLString(getIconName("switch48")));
		}
		
		List<Integer> eportFullNos = switchDev.getEportNos();
		List<Integer> eportShortNos=new ArrayList<>();
		
		// prepare
		Map<Integer, SwitchDevice> id2Switches=new HashMap<>();
		List<SwitchDevice> swDevs = dao.readAllSwitchDevices(true);
		for(SwitchDevice swDev : swDevs){
			id2Switches.put(swDev.getLocalDomainID(), swDev);
		}
		
		Element anotherSwitchElement=null;
		for(Integer eportId : eportFullNos){
			int swId=(eportId>>16) & 0xffff;
			int portShortNo=eportId & 0xffff; // low 2 bytes
			eportShortNos.add(portShortNo);
			
			SwitchDevice peerSwitch = id2Switches.get(swId);
			if(peerSwitch==null){
				continue;
			}
			if(anotherSwitchElement==null){
				anotherSwitchElement=box.getElementByID(peerSwitch.getSwitchName());
			}
		}
		
		boolean left=(numOfSw%2==0);
		if(anotherSwitchElement!=null){
			Object anotherSwLeft=ConfigUtils.getUserObjectProp(anotherSwitchElement, "left");
			if("true".equals(anotherSwLeft)){
				left=false;
			}else if("false".equals(anotherSwLeft)){
				left=true;
			}
		}
		
		int posX= (left ? 300 : 500);
		int posY=60+ (numOfSw/2) * (switch1.getHeight()+60);
		switch1.setLocation(posX , posY);
		
		Map<String, String> data1 = new HashMap<>();
		data1.put("type", ConfigUtils.TYPE_STR_SW);
		data1.put("left", left ? "true" : "false");
		switch1.setUserObject(data1);
		
		if(ConfigContext.REDUNDANCY==2){
			int scale=getShadowAttachmentScaleId();
			String shadowId=ConfigUtils.buildShadowId(ConfigContext.REDUNDANCY+"", switchDev.getPortNumber(), scale, !left);
			try{
				switch1.addAttachment(shadowId);
			}catch(Exception ex){
				System.out.println("add attachment failed for "+shadowId+", error message:"+ex.getMessage());
			}
			data1.put("shadowId", shadowId);
		}
		
		String portIconURL = ConfigUtils.getImageURLString(getIconName("port"));

		int normalPortNum=switchDev.getPortNumber()-switchDev.getEportNumber();
		int normalPortIndex=0;
		int eportIndex=0;
		boolean isEPort=false;

		for(int i=0; i<switchDev.getPortNumber(); i++){
			int portNo=i+1;
			
			if(eportShortNos.contains(portNo)){
				eportIndex++;
				isEPort=true;
			}else{
				normalPortIndex++;
				isEPort=false;
			}
			String portCnName="FE"+portNo;
			
			Port port1 = new Port(ConfigUtils.buildPortId(switch1.getID(), portNo));
			port1.setToolTipText(portCnName);
			port1.setImage(portIconURL);
			port1.setName(""+portNo);
			port1.putLabelPosition(1);
			port1.putLabelFont(labelFont);
			
			if(isEPort){
				double hgap=(switch1.getHeight()-switchDev.getEportNumber()*port1.getHeight())/(switchDev.getEportNumber()+1);
				if(left){
					port1.setLocation(switch1.getLocation().x+switch1.getWidth(), switch1.getLocation().y+hgap+(eportIndex-1)*(port1.getHeight()+hgap));
				}else{
					port1.setLocation(switch1.getLocation().x-port1.getWidth(), switch1.getLocation().y+hgap+(eportIndex-1)*(port1.getHeight()+hgap));
				}
			}else{
				double hgap=(switch1.getHeight()-normalPortNum*port1.getHeight())/(normalPortNum+1);
				if(left){
					port1.setLocation(switch1.getLocation().x-port1.getWidth(), switch1.getLocation().y+hgap+(normalPortIndex-1)*(port1.getHeight()+hgap));
				}else{
					port1.setLocation(switch1.getLocation().x+switch1.getWidth(), switch1.getLocation().y+hgap+(normalPortIndex-1)*(port1.getHeight()+hgap));
				}
			}
			
			port1.setHost(switch1);
			port1.setParent(switch1);
			
			box.addElement(port1);
		}

		for(Integer eportId : eportFullNos){
			int swId=(eportId>>16) & 0xffff;
			SwitchDevice peerSwitch = id2Switches.get(swId);
			if(peerSwitch==null || peerSwitch==switchDev){
				continue;
			}
			
			int portNo=(eportId & 0xffff);
			String srcPortId=ConfigUtils.buildPortId(switchDev.getSwitchName(), portNo);
			String dstPortId=ConfigUtils.buildPortId(peerSwitch.getSwitchName(), portNo);
			
			Element srcSwitchPort = box.getElementByID(srcPortId);
			Element dstSwitchPort = box.getElementByID(dstPortId);
			
			Link link=new MidaPolyLink();
			link.setFrom((Port)srcSwitchPort);
			link.setTo((Port)dstSwitchPort);
			link.putLinkWidth(1);
			link.putLinkColor(Color.BLACK); 
			link.putLinkOutlineWidth(0);
			box.addElement(link);
		}

		return switchDev;
	}

	@Override
	public void deleteDevice(String twaverId, int type) {
		box.removeElementByID(twaverId);
		
		if(ConfigUtils.TYPE_SW==type){
			dao.deleteSwitchDevice(twaverId, true);
		}else{
			box.removeElementByID(twaverId);
			
			String nodeName = ConfigUtils.getNodeNameFromTwaverID(twaverId);
			NodeDevice nodeDevice = dao.readNodeDeviceFromCache(nodeName);
			dao.deleteNodeDevice(nodeDevice);
		}
	}
	
	
	public String getIconName(String iconName){
		return iconName+iconScale;
	}
	
	public int getShadowAttachmentScaleId(){
		if(iconScale.equals("-m.png")){
			return 1;
		}else if(iconScale.equals("-s.png")){
			return 0;
		}
		
		return 2;
	}
	
	@SuppressWarnings("unchecked")
	public void updateAttachment(int newRedundancy){
		List<?> eles = box.getElementsByType(Node.class);
		for(Object obj : eles){
			Element ele=(Element)obj;
			Object userObj = ele.getUserObject();
			if(userObj==null){
				continue;
			}
			Map<String, String> map=(Map<String, String>)userObj;
			Object type = map.get("type");
			if(ConfigUtils.TYPE_STR_SW.equals(type)){
				String shadowId = map.get("shadowId");
				if(newRedundancy==1){
					if(shadowId!=null){
						map.remove("shadowId");
						ele.removeAttachment(shadowId);
					}
				}else if(newRedundancy==2){
					if(shadowId==null){
						String leftStr = map.get("left");
						boolean switchOnLeft=false;
						if("true".equals(leftStr)){
							switchOnLeft=true;
						}
						int scale=getShadowAttachmentScaleId();
						SwitchDevice swDev = dao.readSwitchDevice(ele.getID().toString(), true);
						shadowId=ConfigUtils.buildShadowId(newRedundancy+"", swDev.getPortNumber(), scale, !switchOnLeft);
						
						map.put("shadowId", shadowId);
						try{
							ele.addAttachment(shadowId);
						}catch(Exception ex){
							System.out.println("add attachment failed for "+shadowId+", error message:"+ex.getMessage());
						}
					}
				}
			}
		}
	}
	
}
