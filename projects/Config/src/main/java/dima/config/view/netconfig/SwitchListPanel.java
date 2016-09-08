package dima.config.view.netconfig;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import dima.config.common.ConfigContext;
import dima.config.common.ConfigUtils;
import dima.config.common.models.NodeDevice;
import dima.config.common.models.SwitchDevice;
import dima.config.common.services.ConfigDAO;
import dima.config.common.services.ServiceFactory;
import twaver.Element;
import twaver.ElementAttribute;
import twaver.TDataBox;
import twaver.table.TElementTable;
import twaver.table.TTableModel;

public class SwitchListPanel extends JPanel implements UpdateCallback{

	private static final long serialVersionUID = -3407645417475850254L;

	private TDataBox box;
	private TElementTable table;
	
	public SwitchListPanel(){
		initUI();
	}
	
	private void initUI(){
		setLayout(new BorderLayout());
		
		JPanel topPane=new JPanel(new FlowLayout());
		
		JButton modifyBtn=new JButton("修改记录");
		topPane.add(modifyBtn);
		modifyBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element element = box.getLastSelectedElement();
				if(element==null){
					JOptionPane.showMessageDialog(ConfigContext.mainFrame, "请选中一条记录");
					return;
				}
				
				openModifySwitchDialog((SwitchDevice)element);
			}
		});
		
		JButton deleteBtn=new JButton("删除记录");
		topPane.add(deleteBtn);
		deleteBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element element = box.getLastSelectedElement();
				if(element==null){
					JOptionPane.showMessageDialog(ConfigContext.mainFrame, "没有选中任何记录");
					return;
				}
				
				int confirm=JOptionPane.showConfirmDialog(ConfigContext.mainFrame, "确定要删除记录吗?");
				if(confirm==JOptionPane.OK_OPTION){
					deleteDevice(element.getID().toString(), ConfigUtils.TYPE_SW);
				}
			}
		});
		
		add(topPane, BorderLayout.NORTH);
		
		initTable();
		JScrollPane tablePanel=new JScrollPane(this.table);
		add(tablePanel, BorderLayout.CENTER);
	}
	
	private void initTable(){
		box=new TDataBox();
		this.table=tableInit(box);
		
		ConfigDAO dao = ServiceFactory.getService(ConfigDAO.class);
		List<SwitchDevice> switches = dao.readAllSwitchDevices(true);
		for(SwitchDevice sw : switches){
			box.addElement(sw);
		}
	}
	
	private TElementTable tableInit(TDataBox box){
		TElementTable table = new TElementTable(box);
		
		table.setEditable(false);
		table.setRowHeight(24);
		table.setAutoResizeMode(1);
		table.setTableHeaderPopupMenuFactory(null);
		table.setTableBodyPopupMenuFactory(null);
		
		table.setElementClass(SwitchDevice.class);
		
		List<ElementAttribute> attributes = new ArrayList<ElementAttribute>();
		ElementAttribute attribute = new ElementAttribute();
		attribute.setName("switchName");
		attribute.setDisplayName("交换机名称");
		attributes.add(attribute);
		attribute = new ElementAttribute();
		attribute.setName("localDomainID");
		attribute.setDisplayName("交换机ID");
		attributes.add(attribute);
		
		attribute = new ElementAttribute();
		attribute.setName("portNumber");
		attribute.setDisplayName("数据端口数目");
		attributes.add(attribute);
		
		attribute = new ElementAttribute();
		attribute.setName("eportFEPortNos");
		attribute.setDisplayName("级联端口列表");
		attributes.add(attribute);

		table.registerElementClassAttributes(SwitchDevice.class, attributes);
		table.getTableModel().sortColumn("switchName", TTableModel.SORT_ASCENDING);
		
		table.addElementDoubleClickedActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				openModifySwitchDialog((SwitchDevice)e.getSource());
			}
		});
		
		return table;
	}
	
	private void openModifySwitchDialog(SwitchDevice sw){
		NewSwitchDialog dialog=new NewSwitchDialog(ConfigContext.mainFrame,
				"修改交换机", sw, SwitchListPanel.this);
		dialog.setVisible(true);
	}

	@Override
	public NodeDevice addNode(NodeDevice nodeDev, String oldID) {
		return nodeDev;
	}

	@Override
	public SwitchDevice addSwitch(SwitchDevice newSwitchDev, String oldID) {
		if(oldID==null){
			box.addElement(newSwitchDev);
			return newSwitchDev;
		}else{
			SwitchDevice oldSwitchDevice = (SwitchDevice) box.getElementByID(oldID);
			SwitchDevice switchDevice=null;
			if(!newSwitchDev.getSwitchName().equals(oldID)){
				switchDevice=new SwitchDevice(newSwitchDev.getSwitchName());
				switchDevice.setVlCfgs(oldSwitchDevice.getVlCfgs());
				switchDevice.setVersion(oldSwitchDevice.getVersion());
				switchDevice.setDate(oldSwitchDevice.getDate());
				switchDevice.setFileNo(oldSwitchDevice.getFileNo());
			}else{
				switchDevice=oldSwitchDevice;
			}
			
			switchDevice.setPortNumber(newSwitchDev.getPortNumber());
			switchDevice.setEnableTimeSyncVL(newSwitchDev.isEnableTimeSyncVL());
			switchDevice.setEportFEPortNos(newSwitchDev.getEportFEPortNos());
			switchDevice.setEportNumber(newSwitchDev.getEportNumber());
			switchDevice.setLocalDomainID(newSwitchDev.getLocalDomainID());
			switchDevice.setOverallInterval(newSwitchDev.getOverallInterval());
			switchDevice.setTimeSyncRole(newSwitchDev.getTimeSyncRole());
			switchDevice.setTimeSyncVL(newSwitchDev.getTimeSyncVL());
			switchDevice.setClusterInterval(newSwitchDev.getClusterInterval());
			switchDevice.setLocationId(newSwitchDev.getLocationId());

			box.removeElementByID(oldID);
			box.addElement(switchDevice);
			return switchDevice;
		}
	}

	@Override
	public void deleteDevice(String twaverId, int type) {
		box.removeElementByID(twaverId);
		
		ConfigDAO dao = ServiceFactory.getService(ConfigDAO.class);
		dao.deleteSwitchDevice(twaverId, true);
	}
	
}
