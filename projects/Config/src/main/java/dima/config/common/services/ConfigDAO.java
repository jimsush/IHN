/**
 * 
 */
package dima.config.common.services;

import java.util.List;

import dima.config.common.models.NodeDevice;
import dima.config.common.models.SwitchDevice;
import dima.config.common.models.SwitchMonitor;

public interface ConfigDAO {
	
	public void clearAll();
	
	public List<SwitchDevice> readAllSwitchDevices(boolean fromCache);
	
	public SwitchDevice readSwitchDevice(String switchName, boolean fromCache);
	
	public void updateNMUCache(NodeDevice nmu);
	
	/**
	 * 
	 * @param switchDevice
	 * @param oldSwitch this parameter decides whether need to update device ID and name, [null] does not need to update name and id
	 */
	public void saveSwitchDevice(SwitchDevice switchDevice, SwitchDevice oldSwitch);
	
	public void deleteSwitchDevice(String switchName, boolean deleteMonitorFile);
	
	public List<NodeDevice> readAllNodeDevices(boolean fromCache);
	
	public NodeDevice readNodeDeviceFromCache(String nodeName);
	
	/***
	 * 
	 * @param nodeDevice
	 * @param oldNode this parameter decides whether need to update device ID and name, [null] does not need to update name and id
	 */
	public void saveNodeDevice(NodeDevice nodeDevice, NodeDevice oldNode);
	
	public void deleteNodeDevice(NodeDevice node);
	
	public SwitchMonitor readSwitchMonitor(String switchName);
	
	public void saveSwitchMonitor(SwitchMonitor monitor);
	
	public void deleteSwitchMonitor(String switchName);
	
}
