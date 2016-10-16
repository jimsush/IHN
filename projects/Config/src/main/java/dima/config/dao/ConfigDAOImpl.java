package dima.config.dao;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import dima.config.common.ConfigContext;
import dima.config.common.ConfigUtils;
import dima.config.common.models.NodeBin;
import dima.config.common.models.NodeDevice;
import dima.config.common.models.SwitchBin;
import dima.config.common.models.SwitchDevice;
import dima.config.common.models.SwitchMonitor;
import dima.config.common.models.SwitchMonitorPort;
import dima.config.common.services.ConfigDAO;

public class ConfigDAOImpl implements ConfigDAO {

	private Map<String, SwitchDevice> allSwitches=new ConcurrentHashMap<>();
	private Map<String, NodeDevice> allNodes=new ConcurrentHashMap<>();
	private Map<String, SwitchMonitor> swMonitors=new ConcurrentHashMap<>();
	
	public void init(){
		File curDirectory=new File(".");
		File[] files = curDirectory.listFiles();
		for(File file : files){
			if(!file.isDirectory()){
				String fileName = file.getName();
				if(fileName.startsWith("sw_") && fileName.endsWith("_config.bin")){
					SwitchBin sb = null;
					try{
						sb=BinFileHandler.readSwitch(fileName);
					}catch(Exception ex){
						ex.printStackTrace();
					}
					if(sb!=null){
						sb.getSwCfgs().forEach(sw ->{
							allSwitches.put(sw.getSwitchName(), sw);
							
							if(sw.getNmu()!=null){
								allNodes.put(sw.getNmu().getNodeName(), sw.getNmu());
							}
						});
						
						BinFileHandler.updateSwitchFileMapping(fileName, sb);
					}
				}else if(fileName.startsWith("fic_") && fileName.endsWith("_config.bin")){
					NodeBin nb=null;
					try{
						nb=BinFileHandler.readNode(fileName);
					}catch(Exception ex){
						ex.printStackTrace();
					}
					if(nb!=null){
						nb.getNodeCfgs().forEach(node ->allNodes.put(node.getNodeName(), node));
						BinFileHandler.updateNodeFileMapping(fileName, nb);
					}
				}else if(fileName.startsWith("sw_") && fileName.endsWith("_mon.bin")){
					List<String> switchNames = ConfigUtils.parseSwitchMonitorNames(fileName);
					SwitchMonitor mon = null;
					try{
						mon=BinFileHandler.readMonitor(fileName, switchNames);
					}catch(Exception ex){
						ex.printStackTrace();
					}
					
					if(mon!=null){
						final SwitchMonitor mon2=mon;
						mon.getMonitorPorts().forEach(port -> swMonitors.put(port.getSwitchName(), mon2));
						BinFileHandler.updateMonitorFileMapping(fileName, mon);
					}
				}else if(fileName.equalsIgnoreCase(ConfigUtils.SYS_PROP_FILE)){
					Properties p = new Properties();
					FileInputStream in = null;
					try{
						in=new FileInputStream(ConfigUtils.SYS_PROP_FILE);
						p.load(in);  
					}catch(Exception ex){
						ex.printStackTrace();
					}finally{
						try{
							in.close();
						}catch(Exception ex2){
							ex2.printStackTrace();
						}
					}
					String redundancy=p.getProperty(ConfigUtils.PROP_KEY_REDUNDANCY);
					if(redundancy!=null){
						try{
							ConfigContext.REDUNDANCY=Integer.valueOf(redundancy);
						}catch(Exception ex){
							ex.printStackTrace();
							ConfigContext.REDUNDANCY=2;
						}
					}
					
					String version=p.getProperty(ConfigUtils.PROP_KEY_VERSION);
					if(version!=null){
						if(version.length()<=16){
							ConfigContext.version=version;
						}else{
							ConfigContext.version=version.substring(0,15);
						}
					}
					String date=p.getProperty(ConfigUtils.PROP_KEY_DATE);
					if(date!=null){
						if(date.length()<=16){
							ConfigContext.date=date;
						}else{
							ConfigContext.date=date.substring(0,15);
						}
					}
					String fileno=p.getProperty(ConfigUtils.PROP_KEY_FILENO);
					if(fileno!=null){
						try{
							ConfigContext.fileNo=Integer.valueOf(fileno).shortValue();
						}catch(Exception ex){
							System.out.println("parse file no failed, "+ex.getMessage());
						}
					}
				}
			}
		}
	}
	
	@Override
	public Map<String, String> file2Devices(int type){
		Map<String, String> map=new HashMap<>();
		switch(type){
		case 0:
			Map<String, Set<SwitchDevice>> switches = BinFileHandler.getFile2Switches();
			switches.forEach((file, devs)->{
				StringBuilder sb=new StringBuilder();
				devs.forEach(dev -> sb.append(dev.getSwitchName()).append(","));
				map.put(file, sb.substring(0, sb.length()-1));
			});
			break;
		case 1:
			Map<String, Set<NodeDevice>> nodes = BinFileHandler.getFile2Nodes();
			nodes.forEach((file, devs)->{
				StringBuilder sb=new StringBuilder();
				devs.forEach(dev -> sb.append(dev.getNodeName()).append(","));
				map.put(file, sb.substring(0, sb.length()-1));
			});
			break;
		case 2:
			Map<String, Set<SwitchMonitorPort>> monitors = BinFileHandler.getFile2Monitors();
			monitors.forEach((file, devs)->{
				StringBuilder sb=new StringBuilder();
				devs.forEach(dev -> sb.append(dev.getSwitchName()).append(","));
				map.put(file, sb.substring(0, sb.length()-1));
			});
			break;
		default:
			break;
		}
		return map;
	}

	@Override
	public void clearAll(){
		if(ConfigContext.topoView!=null){
			ConfigContext.topoView.clearAll();
		}
		
		allSwitches.clear();
		allNodes.clear();
		swMonitors.clear();
		
		BinFileHandler.clearAll();
		
		SimpleDateFormat sdt=new SimpleDateFormat("yyyymmddHHmmss");
		String now = sdt.format(new Date());
		String backupDirPath="./bak"+now;
		File backupDir=new File(backupDirPath);
		backupDir.mkdir();
		
		File curDirectory=new File(".");
		File[] files = curDirectory.listFiles();
		for(File file : files){
			if(!file.isDirectory()){
				String fileName = file.getName();
				if(isBinFile(fileName)){
					try{
						File backupFile=new File(backupDirPath+"/"+fileName);
						file.renameTo(backupFile);
						//file.delete();
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
			}
		}
	}
	
	private boolean isBinFile(String fileName){
		if(fileName.startsWith("sw_") && fileName.endsWith("_config.bin")){
			return true;
		}else if(fileName.startsWith("sw_") && fileName.endsWith("_nmu.bin")){
			return true;
		}else if(fileName.startsWith("fic_") && fileName.endsWith("_config.bin")){
			return true;
		}else if(fileName.startsWith("sw_") && fileName.endsWith("_mon.bin")){
			return true;
		}
		return false;
	}
	
	@Override
	public List<SwitchDevice> readAllSwitchDevices(boolean fromCache) {
		List<SwitchDevice> list=new ArrayList<>();
		list.addAll(allSwitches.values());
		Collections.sort(list, new Comparator<SwitchDevice>(){
			@Override
			public int compare(SwitchDevice s0, SwitchDevice s1) {
				if(s1==null)
					return 1;
				else if(s0==null)
					return -1;
				return s0.getLocalDomainID()-s1.getLocalDomainID();
			}
			
		});
		return list;
	}
	
	@Override
	public SwitchDevice readSwitchDevice(String switchName, boolean fromCache){
		return allSwitches.get(switchName);
	}

	@Override
	public void saveSwitchDevice(SwitchDevice switchDevice, SwitchDevice oldSwitch) {
		if(oldSwitch!=null){
			if(!switchDevice.getSwitchName().equals(oldSwitch.getSwitchName())){
				// update NMU first, otherwise the NMU will be deleted
				NodeDevice oldNmu = allNodes.get(oldSwitch.getSwitchName());
				if(oldNmu!=null){
					// update NMU cache
					oldNmu.setNodeName(switchDevice.getSwitchName());
					allNodes.put(switchDevice.getSwitchName(), oldNmu);
				}
				
				// the switch's name is changed
				deleteSwitchDevice(oldSwitch.getSwitchName(), false);
				
				// rename monitor file (don't delete)
				SwitchMonitor mon = swMonitors.get(oldSwitch.getSwitchName());
				if(mon!=null){
					String oldFileName=ConfigUtils.getMonitorConfigFileName(oldSwitch.getSwitchName());
					String newFileName=ConfigUtils.getMonitorConfigFileName(switchDevice.getSwitchName());
					try{
						File oldFile=new File(oldFileName);
						File newFile=new File(newFileName);
						oldFile.renameTo(newFile);
					}catch(Exception ex){
						ex.printStackTrace();
					}
				
					// update the cache
					swMonitors.remove(oldSwitch.getSwitchName());
					
					mon.setSwitchName(switchDevice.getSwitchName());
					swMonitors.put(switchDevice.getSwitchName(), mon);
				}
			}
		}

		try{
			BinFileHandler.writeSwitch(switchDevice);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		allSwitches.put(switchDevice.getSwitchName(), switchDevice);
	}

	@Override
	public void deleteSwitchDevice(String switchName, boolean deleteMonitorFile) {
		try{
			BinFileHandler.deleteSwitchFile(switchName);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		allSwitches.remove(switchName);
		// remove this switch's NMU
		allNodes.remove(switchName);
		
		// remove this switch's monitor file
		if(deleteMonitorFile){
			try{
				BinFileHandler.deleteSwitchMonitorFile(switchName);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}

	@Override
	public List<NodeDevice> readAllNodeDevices(boolean fromCache) {
		List<NodeDevice> list=new ArrayList<>();
		list.addAll(allNodes.values());
		Collections.sort(list, new Comparator<NodeDevice>(){
			@Override
			public int compare(NodeDevice s0, NodeDevice s1) {
				if(s1==null)
					return 1;
				else if(s0==null)
					return -1;
				return s0.getNodeName().compareTo(s1.getNodeName());
			}
			
		});
		return list;
	}

	@Override
	public void saveNodeDevice(NodeDevice newNodeDevice, NodeDevice oldNode) {
		if(oldNode!=null){
			if(newNodeDevice.getType()==ConfigUtils.TYPE_NMU){
				if(!newNodeDevice.getNodeName().equals(oldNode.getNodeName())){
					deleteNodeDevice(oldNode);
				}
			}else{
				if(!newNodeDevice.getNodeName().equals(oldNode.getNodeName())
						|| newNodeDevice.getPortNo()!=oldNode.getPortNo()){
					deleteNodeDevice(oldNode);
				}
			}
		}
		
		saveNode2File(newNodeDevice);
		allNodes.put(newNodeDevice.getNodeName(), newNodeDevice);
	}

	@Override
	public void deleteNodeDevice(NodeDevice nodeDevice) {
		if(nodeDevice==null)
			return;
		
		try{
			BinFileHandler.deleteNodeFile(nodeDevice.getNodeName());
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		allNodes.remove(nodeDevice.getNodeName());
	}

	@Override
	public SwitchMonitor readSwitchMonitor(String switchName){
		SwitchMonitor mon=swMonitors.get(switchName);
		return mon;
	}

	@Override
	public void saveSwitchMonitor(SwitchMonitor monitor) {
		try{
			List<SwitchMonitorPort> mps = monitor.getMonitorPorts();
			mps.forEach(BinFileHandler::writeMonitor);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		swMonitors.put(monitor.getSwitchName(), monitor);
	}

	@Override
	public void deleteSwitchMonitor(String switchName) {
		try{
			BinFileHandler.deleteSwitchMonitorFile(switchName);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		swMonitors.remove(switchName);
	}

	@Override
	public NodeDevice readNodeDeviceFromCache(String nodeName) {
		if(nodeName==null)
			return null;
		return allNodes.get(nodeName);
	}
	
	@Override
	public void updateNMUCache(NodeDevice nmu){
		if(nmu!=null){
			allNodes.put(nmu.getNodeName(), nmu);
		}
	}
	
	private void saveNode2File(NodeDevice nodeDevice){
		try{
			BinFileHandler.writeNMUOrNode(nodeDevice);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
