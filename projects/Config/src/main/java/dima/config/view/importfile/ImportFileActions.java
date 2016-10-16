package dima.config.view.importfile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import dima.config.common.ConfigContext;
import dima.config.common.ConfigUtils;
import dima.config.common.models.NodeDevice;
import dima.config.common.models.NodeMessage;
import dima.config.common.models.SwitchDevice;
import dima.config.common.models.vo.ADCItem;
import dima.config.common.models.vo.ConfigMessageItem;
import dima.config.common.models.vo.ReceiveInfo;
import dima.config.common.models.vo.SendInfo;
import dima.config.common.models.vo.TopoDeviceVo;
import dima.config.common.models.vo.TopoLinkVo;
import dima.config.common.services.ConfigDAO;
import dima.config.common.services.ServiceFactory;
import dima.config.dao.XMLHandler;

public class ImportFileActions {

	public static void openImportADCAction(){
		String[] fileNames=openXMLFile(true);
        if(fileNames==null || fileNames.length==0){
        	return;
        }
        
        List<ADCItem> adcs=new ArrayList<>();
        XMLHandler handler=new XMLHandler();
        try{
        	for(String fileName : fileNames){
        		List<ADCItem> icds = handler.readAdc(fileName);
        		if(icds!=null){
        			adcs.addAll(icds);
        		}
        	}
        }catch(Exception ex){
        	JOptionPane.showMessageDialog(ConfigContext.mainFrame, ex.getMessage());
        	return;
        }

        // create switch first if no switches are created before
        final ConfigDAO dao=ServiceFactory.getService(ConfigDAO.class);
        List<SwitchDevice> switches = dao.readAllSwitchDevices(true);
        if(switches==null || switches.size()==0){
	        if(adcs.size()<=ConfigUtils.MAX_NUM_OF_PORTS){
	        	SwitchDevice sw1=new SwitchDevice("net1_sw1"); 
	        	sw1.setLocalDomainID(1);
	        	sw1.setPortNumber(ConfigUtils.MAX_NUM_OF_PORTS);
	        	sw1.setEportNumber(0);
	        	
	        	dao.saveSwitchDevice(sw1, null);
	    		if(ConfigContext.topoView!=null){
	    			ConfigContext.topoView.addSwitch(sw1, null);
	    		}
	        }else if(adcs.size()<=(2*ConfigUtils.MAX_NUM_OF_PORTS)){
	        	SwitchDevice sw1=new SwitchDevice("net1_sw1"); 
	        	sw1.setLocalDomainID(1);
	        	sw1.setPortNumber(ConfigUtils.MAX_NUM_OF_PORTS);
	        	List<Integer> eports1=new ArrayList<>();
	        	eports1.add(0x00020001);
	        	eports1.add(0x00020002);
	        	sw1.setEportNos(eports1);
	        	sw1.setEportNumber(eports1.size());
	        	
	        	dao.saveSwitchDevice(sw1, null);
	    		if(ConfigContext.topoView!=null){
	    			ConfigContext.topoView.addSwitch(sw1, null);
	    		}
	    		
	    		// switch2
	    		SwitchDevice sw2=new SwitchDevice("net1_sw2"); 
	        	sw2.setLocalDomainID(2);
	        	sw2.setPortNumber(ConfigUtils.MAX_NUM_OF_PORTS);
	        	List<Integer> eports2=new ArrayList<>();
	        	eports2.add(0x00010001);
	        	eports2.add(0x00010002);
	        	sw2.setEportNos(eports2);
	        	sw2.setEportNumber(eports2.size());
	        	
	        	dao.saveSwitchDevice(sw2, null);
	    		if(ConfigContext.topoView!=null){
	    			ConfigContext.topoView.addSwitch(sw2, null);
	    		}
	        }else{
	        	JOptionPane.showMessageDialog(ConfigContext.mainFrame, "不允许导入的节点数超过"+(ConfigUtils.MAX_NUM_OF_PORTS*2)+"个，目前是"+adcs.size()+"个！");
	        	return;
	        }
        }

        StringBuilder sb=new StringBuilder();
        for(ADCItem adc : adcs){
        	List<NodeMessage> newTxMessages=new ArrayList<>();
        	List<SendInfo> sendTable = adc.getSendTable();
    		for(SendInfo sendInfo : sendTable){
    			NodeMessage msg=sendInfo2NodeMessage(adc.getAppName(), sendInfo);
    			msg.setVl(msg.getMessageID());
    			newTxMessages.add(msg);
    		}
    		
    		List<NodeMessage> newRxMessages=new ArrayList<>();
    		List<ReceiveInfo> receiveTable = adc.getReceiveTable();
    		for(ReceiveInfo receiveInfo : receiveTable){
    			NodeMessage msg=receiveInfo2NodeMessage(adc.getAppName(), receiveInfo);
    			msg.setVl(msg.getMessageID());
    			newRxMessages.add(msg);
    		}
    		
    		String errorMsg=addNodeMessages(dao, adc.getAppName(), newRxMessages, newTxMessages, true);
    		if(errorMsg!=null){
    			sb.append(" ").append(errorMsg);
    		}
        };
        
        if(sb.length()>0){
        	JOptionPane.showMessageDialog(ConfigContext.mainFrame, "无可用的交换机端口与节点机相连接，忽略节点机"+sb.toString());
        }
	}

	public static void openImportConfigCtrlAction(){
		String[] fileNames=openXMLFile(false);
        if(fileNames==null || fileNames.length==0){
        	return;
        }
        
        List<ConfigMessageItem> configs =null;
        XMLHandler handler=new XMLHandler();
        try{
        	configs = handler.readConfigCtrl(fileNames[0]);
        }catch(Exception ex){
        	JOptionPane.showMessageDialog(ConfigContext.mainFrame, ex.getMessage());
        	return;
        }
        
        addConfigMessages(configs);
	}
	
	@SuppressWarnings("unchecked")
	public static void openImportTopoCtrlAction(){
		String[] fileNames=openXMLFile(false);
        if(fileNames==null || fileNames.length==0){
        	return;
        }
        
        Object[] res=null;
        XMLHandler handler=new XMLHandler();
        try{
        	res=handler.readTopoCtrl(fileNames[0]);
        }catch(Exception ex){
        	JOptionPane.showMessageDialog(ConfigContext.mainFrame, ex.getMessage());
        	return;
        }
        
        List<TopoDeviceVo> devices=(List<TopoDeviceVo>)res[0];
        List<TopoLinkVo> links=(List<TopoLinkVo>)res[1];
        
        List<TopoDeviceVo> switches=new ArrayList<>();
        List<TopoDeviceVo> nodes=new ArrayList<>();
        Map<String, Integer> name2DomainId=new HashMap<>();
        
        StringBuilder info=new StringBuilder();
        
        for(TopoDeviceVo dev : devices){
        	if(TopoDeviceVo.T_SW.equals(dev.getType())){
        		switches.add(dev);
        	}else{
        		nodes.add(dev);
        	}
        	name2DomainId.put(dev.getName(), dev.getDomainId());
        }
        Map<String, Integer> switchNames=new HashMap<>();
        if(switches.size()>ConfigContext.MAX_NUM_SWITCH){
        	int i=0;
        	Iterator<TopoDeviceVo> it = switches.iterator();
        	for(;it.hasNext();){
        		TopoDeviceVo sw1 = it.next();
        		if(i>=ConfigContext.MAX_NUM_SWITCH){
        			String warnInfo="目前只允许"+ConfigContext.MAX_NUM_SWITCH+"交换机,忽略"+sw1.getName();
        			
        			if(info.length()>0){
        				info.append("\r\n");
        			}
        			info.append(warnInfo);
        			
        			System.out.println(warnInfo);
        			
        			it.remove();
        		}else{
        			switchNames.put(sw1.getName(), sw1.getDomainId());
        			i++;
        		}
        	}
        }
        
        Map<String, String> cascadeLinks=new HashMap<>();
        Map<String, String> commonLinks=new HashMap<>();
        for(TopoLinkVo link : links){
        	String aportFullName=link.getRefReceiver();
        	int pos=aportFullName.indexOf("@port[name='");
        	String aport=aportFullName.substring(pos+12, aportFullName.length()-2);
        	if(aport.indexOf("'")>=0){
        		aport=aport.substring(0, aport.length()-1);
        	}
        	
        	String zportFullName=link.getRefSender();
        	int pos2=zportFullName.indexOf("@port[name='");
        	String zport=zportFullName.substring(pos2+12, zportFullName.length()-2);
        	if(zport.indexOf("'")>=0){
        		zport=aport.substring(0, zport.length()-1);
        	}
        	
        	if("CascadeLink".equals(link.getType())){
	        	cascadeLinks.put(aport, zport);
	        	cascadeLinks.put(zport, aport);
        	}else if("CommonLink".equals(link.getType())){
        		commonLinks.put(aport, zport);
        	}
        }
        
        Map<String, Integer> portName2No=new HashMap<>();
        ConfigDAO dao=ServiceFactory.getService(ConfigDAO.class);
        for(TopoDeviceVo sw : switches){
        	SwitchDevice swD = dao.readSwitchDevice(sw.getName(), true);
        	if(swD==null){
        		swD=new SwitchDevice(sw.getName()); 
        		swD.setLocalDomainID(sw.getDomainId());
        		
        		List<Object[]> ports = sw.getPorts();
        		swD.setPortNumber(ports.size());
        		
        		List<Integer> eportList =new ArrayList<>();
        		for(Object[] p : ports){
        			String portName=(String)p[1];
        			portName2No.put(portName, (Integer)p[0]);
        			
        			String zportName = cascadeLinks.get(portName);
        			if(zportName!=null){
        				String zDevName=getDevNameFromPortName(zportName);
            			int zDevId=name2DomainId.get(zDevName);
            			int portNo=ConfigUtils.getPortFullNo(zDevId, (int)p[0]);
            			eportList.add(portNo);
        			}
        		}
        		
        		swD.setEportNumber(eportList.size());
        		swD.setEportNos(eportList);
        		
        		dao.saveSwitchDevice(swD, null);
        		
        		if(ConfigContext.topoView!=null){
        			ConfigContext.topoView.addSwitch(swD, null);
        		}
        	}else{
        		// TODO switch is existed, ignore
        	}
        }
        
        for(Map.Entry<String, String> entry : commonLinks.entrySet()){
        	String aportFullName=entry.getKey();
        	String zportFullName=entry.getValue();
        	String aDevName=getDevNameFromPortName(aportFullName);
        	String zDevName=getDevNameFromPortName(zportFullName);
        	if(switchNames.containsKey(aDevName)){
        		//z is node
        		NodeDevice node=new NodeDevice(zDevName, ConfigUtils.TYPE_NODE);
        		
        		//aportFullName=net1_sw1_P1_0
        		int swPortNum=portName2No.get(aportFullName);
        		int swDomainId=switchNames.get(aDevName);
        		int portNo=swPortNum+(swDomainId<<16);
        		node.setPortNo(portNo);
            	if(ConfigContext.topoView!=null){
            		ConfigContext.topoView.addNode(node, null);
            	}
            	
            	dao.saveNodeDevice(node, null);
        	}else if(switchNames.containsKey(zDevName)){
        		// a is node
        		NodeDevice node=new NodeDevice(aDevName, ConfigUtils.TYPE_NODE);
        		//zportFullName=net1_sw1_P1_0
        		int swPortNum=portName2No.get(zportFullName);
        		int swDomainId=switchNames.get(zDevName);
        		int portNo=swPortNum+(swDomainId<<16);
        		node.setPortNo(portNo);
            	if(ConfigContext.topoView!=null){
            		ConfigContext.topoView.addNode(node, null);
            	}
            	
            	dao.saveNodeDevice(node, null);
        	}
        }
        
        if(info.length()>0){
        	JOptionPane.showMessageDialog(ConfigContext.mainFrame, info.toString());
        }
	}
	
	private static String[] openXMLFile(boolean multiSelection){
		JFileChooser jfc=new JFileChooser();  
		jfc.setCurrentDirectory(new File("."));
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);  
        FileFilter filter = new FileNameExtensionFilter("XML文件", new String[]{"xml"});
        jfc.setFileFilter(filter);
        jfc.setMultiSelectionEnabled(multiSelection);
        jfc.showDialog(new JLabel(), "选择"); 
        File[] files=null;
        if(multiSelection){
        	files=jfc.getSelectedFiles();  
        }else{
        	// single selection
        	File file=jfc.getSelectedFile();
        	if(file!=null){
        		files=new File[]{ file };
        	}
        }
        if(files==null || files.length==0){
        	return null;
        }
        String[] fileNames=new String[files.length];
        for (int i=0; i<files.length; i++) {
			fileNames[i]=files[i].getAbsolutePath();
		}
        return fileNames;
	}
	
	private static NodeMessage sendInfo2NodeMessage(String deviceName, SendInfo sendInfo){
		NodeMessage msg=new NodeMessage(deviceName, sendInfo.getMessageID());
		msg.setMaxOfLen(sendInfo.getMaxMessageLength());
		return msg;
	}
	
	private static NodeMessage receiveInfo2NodeMessage(String deviceName, ReceiveInfo receiveInfo){
		NodeMessage msg=new NodeMessage(deviceName, receiveInfo.getMessageID());
		msg.setMaxOfLen(receiveInfo.getMaxMessageLength());
		//msg.setType(receiveInfo.getRealMessageType());
		return msg;
	}
	
	@SuppressWarnings("unchecked")
	private static void addConfigMessages(List<ConfigMessageItem> configs){
		Map<String, List<ConfigMessageItem>[]> deviceMsgs=new HashMap<>();
		
		for(ConfigMessageItem config : configs){
			List<String> senderDevs = config.getRefSenderDevices();
			for(String sdev : senderDevs){
				List<ConfigMessageItem>[] msgs = deviceMsgs.get(sdev);
				if(msgs==null){
					msgs=new List[]{ new ArrayList<>(), new ArrayList<>() };
					deviceMsgs.put(sdev, msgs);
				}
				msgs[0].add(config);
			}
			
			List<String> receiverDevs = config.getRefReceiverDevices();
			for(String rdev : receiverDevs){
				List<ConfigMessageItem>[] msgs = deviceMsgs.get(rdev);
				if(msgs==null){
					msgs=new List[]{ new ArrayList<>(), new ArrayList<>() };
					deviceMsgs.put(rdev, msgs);
				}
				msgs[1].add(config);
			}
		}
		
		ConfigDAO dao=ServiceFactory.getService(ConfigDAO.class);
		for(Map.Entry<String, List<ConfigMessageItem>[]> entry : deviceMsgs.entrySet()){
			String devName=entry.getKey();
			List<ConfigMessageItem>[] msgs = entry.getValue();
			List<ConfigMessageItem> txMsgs=msgs[0];
			List<ConfigMessageItem> rxMsgs=msgs[1];
			
			List<NodeMessage> newRxMessages=new ArrayList<>();
			if(rxMsgs!=null){
				for(ConfigMessageItem rxMsg : rxMsgs){
					NodeMessage msg=configMessage2NodeMessage(devName, rxMsg);
					msg.setVl(msg.getMessageID());
					newRxMessages.add(msg);
				}
			}
			
			List<NodeMessage> newTxMessages=new ArrayList<>();
			if(txMsgs!=null){
				for(ConfigMessageItem txMsg : txMsgs){
					NodeMessage msg=configMessage2NodeMessage(devName, txMsg);
					msg.setVl(msg.getMessageID());
					newTxMessages.add(msg);
				}
			}
			
			addNodeMessages(dao, devName, newRxMessages, newTxMessages, false);
		}
	}
	
	private static NodeMessage configMessage2NodeMessage(String devName, ConfigMessageItem item){
		NodeMessage msg=new NodeMessage(devName, item.getMsgId());
		msg.setMessageName(item.getMessageName());
		msg.setMaxOfLen(item.getMaxLength());
		msg.setUseOfMessage((short)item.getMsgUse());
		msg.setType((short)item.getTypeID());
		//period="1000000000ns"
		return msg;
	}
	
	private static String addNodeMessages(ConfigDAO dao, String deviceName, List<NodeMessage> newRxMessages, List<NodeMessage> newTxMessages, boolean toCreateNode){
		NodeDevice node = dao.readNodeDeviceFromCache(deviceName);
		if(node==null && !toCreateNode){
			System.out.println("node "+deviceName+" not found, so ignore the messages");
			return null;
		}
		
		boolean flag=true;
    	if(node==null){
    		// the node is not existed, so create it
    		node=new NodeDevice(deviceName, ConfigUtils.TYPE_NODE);
    		
    		int portNo=ConfigUtils.getNextUnusedSwitchPort();
    		if(portNo<=0){
    			flag=false;
    			return deviceName;
    		}else{
        		node.setPortNo(portNo);
        		
        		if(ConfigContext.topoView!=null){
        			ConfigContext.topoView.addNode(node, null);
        		}
    		}
    	}
    	if(!flag){
    		return null;
    	}
    	
		// tx, send
		List<NodeMessage> txMessages = node.getTxMsgs();
		Set<Integer> txMsgIds=new HashSet<>();
		for(NodeMessage txMsg : txMessages){
			txMsgIds.add(txMsg.getMessageID());
		}
		//txMessages.clear();
		if(newTxMessages!=null){
			for(NodeMessage newMsg : newTxMessages){
				if(!txMsgIds.contains(newMsg.getMessageID())){
					txMessages.add(newMsg);
				}
			}
		}
		
		// rx, receive
		List<NodeMessage> rxMessages = node.getRxMsgs();
		Set<Integer> rxMsgIds=new HashSet<>();
		for(NodeMessage rxMsg : rxMessages){
			rxMsgIds.add(rxMsg.getMessageID());
		}
		//rxMessages.clear();
		if(newRxMessages!=null){
			for(NodeMessage newMsg : newRxMessages){
				if(!rxMsgIds.contains(newMsg.getMessageID())){
					rxMessages.add(newMsg);
				}
			}
		}
    	
    	dao.saveNodeDevice(node, null);
    	return null;
	}
	
	private static String getDevNameFromPortName(String portName){
		int pos=portName.indexOf("_P");
		String devName=portName.substring(0, pos); //net1_sw1_P1_0, 
		return devName;
	}
	
}
