package dima.config.dao;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import dima.config.common.ConfigContext;
import dima.config.common.ConfigUtils;
import dima.config.common.models.NodeDevice;
import dima.config.common.models.SwitchDevice;
import dima.config.common.models.SwitchMonitor;
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
					String swName=ConfigUtils.getSwitchName(fileName);
					
					SwitchDevice swDevice = null;
					try{
						swDevice=BinFileHandler.readSwitch(swName);
					}catch(Exception ex){
						ex.printStackTrace();
					}
					if(swDevice!=null){
						allSwitches.put(swName, swDevice);
						
						if(swDevice.getNmu()!=null){
							allNodes.put(swName, swDevice.getNmu());
						}
					}
				}
				/*else if(fileName.startsWith("sw_") && fileName.endsWith("_nmu.bin")){
					String swName=ConfigUtils.getSwitchName(fileName);
					NodeDevice nmu = null;
					try{
						nmu=BinFileHandler.readNMUOrNode(swName, ConfigUtils.TYPE_NMU);
					}catch(Exception ex){
						ex.printStackTrace();
					}
					if(nmu!=null){
						allNodes.put(swName, nmu);
					}
				}*/
				else if(fileName.startsWith("fic_") && fileName.endsWith("_config.bin")){
					String portNo=ConfigUtils.getPortNo(fileName);
					NodeDevice curNode = null;
					try{
						curNode=BinFileHandler.readNMUOrNode(portNo, ConfigUtils.TYPE_NODE);
					}catch(Exception ex){
						ex.printStackTrace();
					}
					if(curNode!=null){
						allNodes.put(curNode.getNodeName(), curNode);
					}
				}else if(fileName.startsWith("sw_") && fileName.endsWith("_mon.bin")){
					String swName=ConfigUtils.getSwitchName(fileName);
					
					SwitchMonitor mon = null;
					try{
						mon=BinFileHandler.readMonitor(swName);
					}catch(Exception ex){
						ex.printStackTrace();
					}
					if(mon!=null){
						swMonitors.put(swName, mon);
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
	public void clearAll(){
		if(ConfigContext.topoView!=null){
			ConfigContext.topoView.clearAll();
		}
		
		allSwitches.clear();
		allNodes.clear();
		swMonitors.clear();
		
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
					/*
					String oldNmuBinFile = ConfigUtils.getNMUConfigFileName(oldSwitch.getSwitchName());
					String newNmuBinFile = ConfigUtils.getNMUConfigFileName(switchDevice.getSwitchName());
					try{
						File oldFile=new File(oldNmuBinFile);
						File newFile=new File(newNmuBinFile);
						oldFile.renameTo(newFile);
					}catch(Exception ex){
						ex.printStackTrace();
					}*/
					
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
		String binFile = ConfigUtils.getSwitchConfigFileName(switchName);
		try{
			File file=new File(binFile);
			file.delete();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		allSwitches.remove(switchName);
		
		// remove this switch's NMU file
		/*
		String nmuBinFile = ConfigUtils.getNMUConfigFileName(switchName);
		try{
			File file=new File(nmuBinFile);
			file.delete();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		*/
		allNodes.remove(switchName);
		
		// remove this switch's monitor file
		if(deleteMonitorFile){
			deleteSwitchMonitor(switchName);
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
		
		String fileName=null;
		if(nodeDevice.getType()==ConfigUtils.TYPE_NMU){
			fileName=ConfigUtils.getNMUConfigFileName(nodeDevice.getNodeName());
		}else{
			fileName=ConfigUtils.getNodeConfigFileName(nodeDevice.getPortNo()+"");
		}
		try{
			File file=new File(fileName);
			file.delete();
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
			BinFileHandler.writeMonitor(monitor);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		swMonitors.put(monitor.getSwitchName(), monitor);
	}

	@Override
	public void deleteSwitchMonitor(String switchName) {
		String fileName=ConfigUtils.getMonitorConfigFileName(switchName);
		try{
			File file=new File(fileName);
			file.delete();
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
