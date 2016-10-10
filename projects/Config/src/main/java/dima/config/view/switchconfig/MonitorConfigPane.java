package dima.config.view.switchconfig;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import dima.config.common.ConfigContext;
import dima.config.common.ConfigUtils;
import dima.config.common.controls.EnumTableCellRenderer;
import dima.config.common.controls.ListDataTableCellRenderer;
import dima.config.common.models.SwitchDevice;
import dima.config.common.models.SwitchMonitor;
import dima.config.common.models.SwitchMonitorCfg;
import dima.config.common.models.SwitchMonitorPort;
import dima.config.common.services.ConfigDAO;
import dima.config.common.services.ServiceFactory;
import dima.config.view.ConfigTableCallback;
import dima.config.view.CreateConfigTableDialog;
import twaver.Element;
import twaver.ElementAttribute;
import twaver.Node;
import twaver.TDataBox;
import twaver.table.TElementTable;
import twaver.table.TTableModel;
import twaver.tree.TTree;

public class MonitorConfigPane extends JPanel implements ConfigTableCallback{

	private static final long serialVersionUID = -2804441409728323222L;
	
	private ConfigDAO dao;
	
	private TDataBox tablebox;
	private TElementTable table;
	
	private JComboBox<String> switchBox;
	
	private TDataBox treebox;
	private TTree tree;
	private Node treeRoot;
	
	private String currentSwitchName;
	private String rootIconURL;
	private String cfgIconURL;
	
	private SwitchMonitorPort copiedMonitorPort;
	
	public MonitorConfigPane(){
		initService();
		initUI();
	}
	
	private void initService(){
		dao=ServiceFactory.getService(ConfigDAO.class);
		rootIconURL=ConfigUtils.getImageURLString("root.png");
		cfgIconURL=ConfigUtils.getImageURLString("config.png");
	}
	
	private void initUI(){
		this.setLayout(new BorderLayout());
		
		initTopPanel();
		initBottomPanel();
		
		if(switchBox.getSelectedItem()!=null){
			String switchName=switchBox.getSelectedItem().toString();
			this.currentSwitchName=switchName;
			/*
			fillTree(switchName);
			
			// select the first cfg table
			List<?> children = treeRoot.getChildren();
			if(children!=null && children.size()>0){
				Object child=children.get(0);
				Element cfgTableElement=(Element)child;
				
				cfgTableElement.setSelected(true);
				fillTable(switchName, cfgTableElement);
			}
			*/
			fillTable(switchName);
		}
	}
	
	private void initTopPanel(){
		JPanel switchContainer=new JPanel(new BorderLayout());
		
		JPanel switchPanel=new JPanel(new FlowLayout());
		switchPanel.setBorder(BorderFactory.createTitledBorder("显示范围"));
		
		switchContainer.add(switchPanel, BorderLayout.WEST);
		 
		JLabel label=new JLabel("选择交换机: ");
		switchPanel.add(label);
		
		switchBox=new JComboBox<>();
		List<SwitchDevice> swList = dao.readAllSwitchDevices(true);
		for(SwitchDevice sw : swList){
			switchBox.addItem(sw.getSwitchName());
		}
		switchPanel.add(switchBox);
		switchBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Object obj=switchBox.getSelectedItem();
				if(obj==null){
					return;
				}
				
				String switchName=obj.toString();
				currentSwitchName=switchName;
				
				//fillTree(switchName);
				
				//Element ele = treebox.getLastSelectedElement();
				//fillTable(switchName, ele);
				fillTable(switchName);
			}
		});
		
		JButton btnAddConfigTable=new JButton("创建配置表");
		//switchPanel.add(btnAddConfigTable);
		btnAddConfigTable.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(currentSwitchName==null){
					JOptionPane.showMessageDialog(ConfigContext.mainFrame, "请先选中交换机");
					return;
				}
				if(treeRoot!=null){
					List<?> children = treeRoot.getChildren();
					if(children!=null && children.size()>=ConfigContext.MAX_NUM_CFGTABLE){
						JOptionPane.showMessageDialog(ConfigContext.mainFrame, "目前已有4个配置表，不能继续创建配置表！");
						return;
					}
				}
				
				CreateConfigTableDialog dialog=new CreateConfigTableDialog(ConfigContext.mainFrame,
						"创建配置表", true, 0, MonitorConfigPane.this);
				dialog.setVisible(true);
			}
		});

		JButton btnDeleteConfigTable=new JButton("删除配置表");
		//switchPanel.add(btnDeleteConfigTable);
		btnDeleteConfigTable.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element ele = treebox.getLastSelectedElement();
				int cfgTableId=ConfigUtils.getCurrentCfgTableId(ele);
				if(cfgTableId<=0){
					return;
				}
				
				int confirm=JOptionPane.showConfirmDialog(ConfigContext.mainFrame, "确定要删除吗?", UIManager.getString("OptionPane.titleText"),JOptionPane.YES_NO_OPTION);
				if(confirm!=JOptionPane.OK_OPTION){
					return;
				}

				deleteConfigTable(cfgTableId);
				
				tablebox.clear();
			}
		});
		
		/*
		JButton btnUpdateConfigTable=new JButton("修改配置表");
		switchPanel.add(btnUpdateConfigTable);
		btnUpdateConfigTable.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element ele = treebox.getLastSelectedElement();
				int cfgTableId=getCurrentCfgTable(ele);
				if(cfgTableId<=0){
					return;
				}

				CreateMonCfgTableDialog dialog=new CreateMonCfgTableDialog(ConfigContext.mainFrame,
						"修改配置表", false, cfgTableId, 0, MonitorConfigPane.this);
				dialog.setVisible(true);
			}
		});
		*/
		
		JLabel label3=new JLabel("                        ");
		switchPanel.add(label3);
		
		this.add(switchContainer, BorderLayout.NORTH);
	}
	
	private void initBottomPanel(){
		/*
		// tree panel
		this.treebox=new TDataBox();
		this.tree=new TTree(this.treebox, new Comparator<Element>(){
			@Override
			public int compare(Element o1, Element o2) {
				if(o2==null)
					return 1;
				if(o1==null)
					return -1;
				return o1.getID().toString().compareTo(o2.getID().toString());
		}});
		this.tree.setRootVisible(false);
		this.tree.addTreeNodeClickedActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				ElementNode node=(ElementNode)obj;
				Element cfgTableElement=null;
				if(node!=null){
					cfgTableElement=node.getElement();
				}
				fillTable(currentSwitchName, cfgTableElement);
		}});
		
		JScrollPane treePane=new JScrollPane(this.tree);
		*/
		
		// right: button, table
		JPanel btnTbPanel=new JPanel(new BorderLayout());
		btnTbPanel.setBorder(BorderFactory.createTitledBorder("编辑监控设置"));
		
		JPanel buttonPanel=new JPanel(new FlowLayout());
		btnTbPanel.add(buttonPanel, BorderLayout.NORTH);
		
		JButton btnAdd=new JButton("增加记录");
		buttonPanel.add(btnAdd);
		btnAdd.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				openAddMonitor();
			}
		});
		
		JButton btnModify=new JButton("修改记录");
		buttonPanel.add(btnModify);
		btnModify.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element element = tablebox.getLastSelectedElement();
				if(element==null){
					JOptionPane.showMessageDialog(ConfigContext.mainFrame, "没有选中任何记录");
					return;
				}
				openUpdateMonitor((SwitchMonitorPort)element);
			}
		});
		
		JButton btnDelete=new JButton("删除记录");
		buttonPanel.add(btnDelete);
		btnDelete.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				deleteSelectedMonitorPort();
			}
		});
		
		JButton copyBtn=new JButton("复制记录");
		buttonPanel.add(copyBtn);
		copyBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element element = tablebox.getLastSelectedElement();
				if(element==null){
					JOptionPane.showMessageDialog(ConfigContext.mainFrame, "没有选中任何记录");
					return;
				}
				SwitchMonitorPort currentMon=(SwitchMonitorPort)element;
				copiedMonitorPort=cloneMonitorPort(currentMon, currentMon.getConfigTableId(), currentMon.getPortId());
			}
		});
		
		JButton pasteBtn=new JButton("粘贴记录");
		buttonPanel.add(pasteBtn);
		pasteBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(copiedMonitorPort==null){
					JOptionPane.showMessageDialog(ConfigContext.mainFrame, "请先复制一条记录");
					return;
				}
				
				//Element treeElement=treebox.getLastSelectedElement();
				//int cfgTableId=ConfigUtils.getCurrentCfgTableId(treeElement);
				//if(cfgTableId>0){
					int nextPortId=getNextMonitorPortId();
					SwitchMonitorPort newVL=cloneMonitorPort(copiedMonitorPort, 1, nextPortId);
					
					addOrUpdateSwitchMonitor(newVL, null);
				//}
			}
		});
		
		JButton btnDeleteAllMon=new JButton("删除所选交换机的所有监控配置");
		buttonPanel.add(btnDeleteAllMon);
		btnDeleteAllMon.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				deleteMonitorsInSwitch();
			}
		});
		
		JLabel label3=new JLabel("                        ");
		buttonPanel.add(label3);
		
		initTable();
		JScrollPane tablePanel=new JScrollPane(this.table);
		btnTbPanel.add(tablePanel, BorderLayout.CENTER);
		
		// main split panel
		//JSplitPane bottomPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePane, btnTbPanel);
		//bottomPane.setDividerLocation(140);
		
		//this.add(bottomPane, BorderLayout.CENTER);
		this.add(btnTbPanel, BorderLayout.CENTER);
	}
	
	private SwitchMonitorPort cloneMonitorPort(SwitchMonitorPort currentMon, int configTableId, int portId) {
		SwitchMonitorPort newMon=new SwitchMonitorPort(currentMon.getSwitchName(), configTableId, portId);
		newMon.setPortEnableMonitor(currentMon.getPortEnableMonitor());
		newMon.setPortMonitorMode(currentMon.getPortMonitorMode());
		newMon.setPortInputPortList(currentMon.getPortInputPortList());
		newMon.setPortOutputPortList(currentMon.getPortOutputPortList());
		newMon.setPortVLList(currentMon.getPortVLList());
		newMon.setLocationId(currentMon.getLocationId());
		return newMon;
	}
	
	private int getNextMonitorPortId() {
		List<?> elements = tablebox.getAllElements();
		if(elements==null || elements.size()==0){
			return 0;
		}
		int maxId=0;
		for(Object obj : elements){
			SwitchMonitorPort port=(SwitchMonitorPort)obj;
			if(port.getPortId()>=maxId){
				maxId=port.getPortId();
			}
		}
		return maxId+1;
	}

	/**
	 * @deprecated
	 * @param switchName
	 */
	private void fillTree(String switchName){
		if(true)
			return;
		
		/*
		treebox.clear();
		
		if(switchName==null){
			return;
		}
		
		treeRoot=new Node(switchName);
		treeRoot.setName(switchName);
		treeRoot.setIcon(this.rootIconURL);
		treebox.addElement(treeRoot);
		this.currentSwitchName=switchName;
		
		SwitchMonitor mon = dao.readSwitchMonitor(switchName);
		if(mon!=null){
			List<SwitchMonitorCfg> cfgs = mon.getMonitorCfgs();
			for(SwitchMonitorCfg cfg : cfgs){
				String cfgTwaverId = ConfigUtils.getCfgTableTwaverId(cfg.getCfgTableId());
				Node cfgNode=new Node(cfgTwaverId);
				cfgNode.setName("配置表"+cfg.getCfgTableId());
				cfgNode.setIcon(this.cfgIconURL);
				cfgNode.setParent(treeRoot);
				treebox.addElement(cfgNode);
			}
		}
		
		tree.expandAll();
		*/
	}
	
	private void fillTable(String switchName){//, Element cfgTableElement){
		tablebox.clear();
		
		//if(cfgTableElement==null || cfgTableElement.getParent()==null){
		//	return;
		//}
		
		//int cfgTableId=ConfigUtils.getCurrentCfgTableId(cfgTableElement);
		
		SwitchMonitor mon = dao.readSwitchMonitor(switchName);
		if(mon!=null){
			//List<SwitchMonitorCfg> cfgs = mon.getMonitorCfgs();
			//for(SwitchMonitorCfg cfg : cfgs){
			//	if(cfgTableId==cfg.getCfgTableId()){
			//		List<SwitchMonitorPort> ports = cfg.getMonitorPorts();
					List<SwitchMonitorPort> ports =mon.getMonitorPorts();
					for(SwitchMonitorPort port : ports){
						tablebox.addElement(port);
					}
			//		break;
			//	}
			//}
		}
	}
	
	private void deleteMonitorsInSwitch(){
		int confirm=JOptionPane.showConfirmDialog(ConfigContext.mainFrame, "确定要删除该交换机上的所有监控配置吗?", UIManager.getString("OptionPane.titleText"),JOptionPane.YES_NO_OPTION);
		if(confirm==JOptionPane.OK_OPTION){
			tablebox.clear();

			SwitchMonitor mon = dao.readSwitchMonitor(currentSwitchName);
			if(mon!=null){
				//List<SwitchMonitorCfg> cfgs = mon.getMonitorCfgs();
				//if(cfgs!=null){
				//	for(SwitchMonitorCfg cfg : cfgs){
				//		cfg.setMirrorPortNum(0);
				//		cfg.setMonitorPorts(new ArrayList<>());
				//	}
				List<SwitchMonitorPort> monitorPorts = mon.getMonitorPorts();
				monitorPorts.clear();
				
				dao.saveSwitchMonitor(mon);
				//}
			}
		}
	}
	
	private void deleteSelectedMonitorPort() {
		Element element = tablebox.getLastSelectedElement();
		if(element==null){
			JOptionPane.showMessageDialog(ConfigContext.mainFrame, "没有选中任何记录");
			return;
		}
		
		int confirm=JOptionPane.showConfirmDialog(ConfigContext.mainFrame, "确定要删除记录吗?", UIManager.getString("OptionPane.titleText"),JOptionPane.YES_NO_OPTION);
		if(confirm==JOptionPane.OK_OPTION){
			tablebox.removeElement(element);
			
			SwitchMonitorPort m=(SwitchMonitorPort)element;
			SwitchMonitor mon = dao.readSwitchMonitor(m.getSwitchName());
			/*if(mon!=null){
				List<SwitchMonitorCfg> cfgs = mon.getMonitorCfgs();
				if(cfgs!=null){
					for(SwitchMonitorCfg cfg : cfgs){
						if(cfg.getCfgTableId()==m.getConfigTableId()){
							List<SwitchMonitorPort> ports = cfg.getMonitorPorts();
							if(ports!=null){
								Iterator<SwitchMonitorPort> it = ports.iterator();
								for(; it.hasNext(); ){
									SwitchMonitorPort monPort = it.next();
									if(monPort.getPortId()==m.getPortId()){
										it.remove();
										break;
									}
								}
								cfg.setMirrorPortNum(ports.size());
							}
							break;
						}
					}
				}*/
				
				List<SwitchMonitorPort> ports = mon.getMonitorPorts();
				Iterator<SwitchMonitorPort> it = ports.iterator();
				for(; it.hasNext(); ){
					SwitchMonitorPort monPort = it.next();
					if(monPort.getPortId()==m.getPortId()){
						it.remove();
						break;
					}
				}
				
				dao.saveSwitchMonitor(mon);
		}
	}

	private void openUpdateMonitor(SwitchMonitorPort currentMonitor) {
		CreateMonitorDialog dialog=new CreateMonitorDialog(ConfigContext.mainFrame, "修改记录", currentMonitor.getSwitchName(), currentMonitor.getConfigTableId(), currentMonitor, this);
		dialog.setVisible(true);
	}

	private void openAddMonitor() {
		Object obj = switchBox.getSelectedItem();
		if(obj==null){
			return;
		}
		
		this.currentSwitchName=obj.toString();
		
		/*Element cfgTableElement = treebox.getLastSelectedElement();
		int curCfgTableId=ConfigUtils.getCurrentCfgTableId(cfgTableElement);
		if(curCfgTableId<=0){
			JOptionPane.showMessageDialog(ConfigContext.mainFrame, "请选择一个配置表");
			return;
		}*/

		CreateMonitorDialog dialog=new CreateMonitorDialog(ConfigContext.mainFrame, "添加记录", obj.toString(), 1, null, this);
		dialog.setVisible(true);
	}

	private void initTable(){
		tablebox=new TDataBox();
		table=tableInit(tablebox);
	}
	
	private TElementTable tableInit(TDataBox box){
		TElementTable table = new TElementTable(box);
		
		table.setEditable(false);
		table.setRowHeight(24);
		table.setAutoResizeMode(1);
		table.setTableHeaderPopupMenuFactory(null);
		table.setTableBodyPopupMenuFactory(null);
		
		table.setElementClass(SwitchMonitorPort.class);
		
		List<ElementAttribute> attributes = new ArrayList<ElementAttribute>();
		ElementAttribute attribute = new ElementAttribute();
		attribute.setName("switchName");
		attribute.setDisplayName("交换机名称");
		attributes.add(attribute);
		
		attribute = new ElementAttribute();
		attribute.setName("configTableId");
		attribute.setDisplayName("配置表");
		attributes.add(attribute);

		attribute = new ElementAttribute();
		attribute.setName("locationId");
		attribute.setDisplayName("交换机位置");
		attributes.add(attribute);
		
		attribute = new ElementAttribute();
		attribute.setName("portId");
		attribute.setDisplayName("监控口编号");
		attributes.add(attribute);
		
		attribute = new ElementAttribute();
		attribute.setName("portEnableMonitor");
		attribute.setDisplayName("捕获使能");
		attributes.add(attribute);
		attribute = new ElementAttribute();
		attribute.setName("portMonitorMode");
		attribute.setDisplayName("捕获模式");
		attributes.add(attribute);
		attribute = new ElementAttribute();
		attribute.setName("portInputPortList");
		attribute.setDisplayName("输入端口");
		attributes.add(attribute);
		attribute = new ElementAttribute();
		attribute.setName("portOutputPortList");
		attribute.setDisplayName("输出端口");
		attributes.add(attribute);
		attribute = new ElementAttribute();
		attribute.setName("portVLList");
		attribute.setDisplayName("VL");
		attributes.add(attribute);

		table.registerElementClassAttributes(SwitchMonitorPort.class, attributes);
		
		Map<Object, String> mappingPortMode=new HashMap<>();
		mappingPortMode.put(0, "端口捕获");
		mappingPortMode.put(1, "VL捕获");
		EnumTableCellRenderer rendererPortMode1=new EnumTableCellRenderer(mappingPortMode);
		table.getColumnByName("portMonitorMode").setCellRenderer(rendererPortMode1);
		
		Map<Object, String> mappingPortEnable=new HashMap<>();
		mappingPortEnable.put(0, "不使能");
		mappingPortEnable.put(1, "使能");
		EnumTableCellRenderer rendererPortEnable1=new EnumTableCellRenderer(mappingPortEnable);
		table.getColumnByName("portEnableMonitor").setCellRenderer(rendererPortEnable1);
		
		table.getColumnByName("portInputPortList").setCellRenderer(new ListDataTableCellRenderer());
		table.getColumnByName("portOutputPortList").setCellRenderer(new ListDataTableCellRenderer());
		table.getColumnByName("portVLList").setCellRenderer(new ListDataTableCellRenderer());
		
		table.getTableModel().sortColumn("portId", TTableModel.SORT_ASCENDING);
		
		table.addElementDoubleClickedActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				openUpdateMonitor((SwitchMonitorPort)e.getSource());
			}
		});
		
		return table;
	}
	
	public void addOrUpdateSwitchMonitor(SwitchMonitorPort newMonitorPort, SwitchMonitorPort oldMonitorPort){
		if(oldMonitorPort==null){
			tablebox.addElement(newMonitorPort);
			
			SwitchMonitor mon = dao.readSwitchMonitor(this.currentSwitchName);
			if(mon==null){
				mon=new SwitchMonitor(this.currentSwitchName);
			}
				/*
				List<SwitchMonitorCfg> cfgs = mon.getMonitorCfgs();
				for(SwitchMonitorCfg cfg : cfgs){
					if(cfg.getCfgTableId()==newMonitorPort.getConfigTableId()){
						cfg.addMonitorPort(newMonitorPort);
						
						List<SwitchMonitorPort> ports = cfg.getMonitorPorts();
						cfg.setMirrorPortNum(ports.size());
						
						Collections.sort(ports, new Comparator<SwitchMonitorPort>(){
							@Override
							public int compare(SwitchMonitorPort o1, SwitchMonitorPort o2) {
								if(o2==null){
									return 1;
								}
								if(o1==null){
									return -1;
								}
								return o1.getPortId()-o2.getPortId();
							}
						});
						
						dao.saveSwitchMonitor(mon);
						return;
					}
				}*/
				
				List<SwitchMonitorPort> monitorPorts = mon.getMonitorPorts();
				monitorPorts.add(newMonitorPort);
				Collections.sort(monitorPorts, new Comparator<SwitchMonitorPort>(){
					@Override
					public int compare(SwitchMonitorPort o1, SwitchMonitorPort o2) {
						if(o2==null){
							return 1;
						}
						if(o1==null){
							return -1;
						}
						return o1.getPortId()-o2.getPortId();
					}
				});
				
				dao.saveSwitchMonitor(mon);
		}else{ // for update
			tablebox.removeElementByID(oldMonitorPort.getSwitchName()+"-"+oldMonitorPort.getConfigTableId()
			                           +"-"+oldMonitorPort.getPortId());
			tablebox.addElement(newMonitorPort);
			
			SwitchMonitor mon = dao.readSwitchMonitor(currentSwitchName);
			if(mon!=null){
				//List<SwitchMonitorCfg> cfgs = mon.getMonitorCfgs();
				//for(SwitchMonitorCfg cfg : cfgs){
				//	if(cfg.getCfgTableId()==newMonitorPort.getConfigTableId()){
						List<SwitchMonitorPort> ports = mon.getMonitorPorts();
						for(SwitchMonitorPort port : ports){
							if(port.getPortId()==oldMonitorPort.getPortId()){
								port.setPortEnableMonitor(newMonitorPort.getPortEnableMonitor());
								port.setPortMonitorMode(newMonitorPort.getPortMonitorMode());
								port.setPortInputPortList(newMonitorPort.getPortInputPortList());
								port.setPortOutputPortList(newMonitorPort.getPortOutputPortList());
								port.setPortVLList(newMonitorPort.getPortVLList());
								port.setLocationId(newMonitorPort.getLocationId());
								break;
							}
						}
						dao.saveSwitchMonitor(mon);
						return;
					//}
				//}
			}
		}
	}
	
	/**
	 * @deprecated
	 */
	@Override
	public void addOrUpdateConfigTable(boolean isAdd, int cfgTableId){
		/*
		Node cfgNode=null;
		String twaverId = ConfigUtils.getCfgTableTwaverId(cfgTableId);
		if(isAdd){
			cfgNode=new Node(twaverId);
			if(treeRoot!=null){
				cfgNode.setParent(treeRoot);
			}
			cfgNode.setIcon(this.cfgIconURL);
		}else{
			cfgNode=(Node) treebox.getElementByID(twaverId);
		}
		
		cfgNode.setName("配置表"+cfgTableId);
		
		if(isAdd){
			treebox.addElement(cfgNode);
			tree.clearSelection();
			cfgNode.setSelected(true);
			tree.expandAll();
			
			tablebox.clear();
		}
		
		SwitchMonitor mon = dao.readSwitchMonitor(currentSwitchName);
		List<SwitchMonitorCfg> cfgs = null;
		if(mon!=null){
			cfgs=mon.getMonitorCfgs();
			if(isAdd){
				SwitchMonitorCfg cfg=new SwitchMonitorCfg();
				cfg.setCfgTableId(cfgTableId);
				cfgs.add(cfg);
				
				sortConfigTable(cfgs);
			}
		}else{
			cfgs=new ArrayList<SwitchMonitorCfg>();
			
			mon=new SwitchMonitor(currentSwitchName);
			mon.setMonitorCfgs(cfgs);
			SwitchMonitorCfg cfg=new SwitchMonitorCfg();
			cfg.setCfgTableId(cfgTableId);
			cfgs.add(cfg);
		}
		
		mon.setNumberOfConfigTable(cfgs.size());
		dao.saveSwitchMonitor(mon);
		*/
	}
	
	/**
	 * @deprecated
	 */
	@Override
	public void deleteConfigTable(int cfgTableId){
		/*
		treebox.removeElementByID(ConfigUtils.getCfgTableTwaverId(cfgTableId));
		
		SwitchMonitor mon = dao.readSwitchMonitor(currentSwitchName);
		if(mon!=null){
			List<SwitchMonitorCfg> cfgs = mon.getMonitorCfgs();
			Iterator<SwitchMonitorCfg> it = cfgs.iterator();
			for(; it.hasNext(); ){
				SwitchMonitorCfg cfg = it.next();
				if(cfgTableId==cfg.getCfgTableId()){
					it.remove();
					break;
				}
			}
			mon.setNumberOfConfigTable(cfgs.size());
			
			sortConfigTable(cfgs);
			dao.saveSwitchMonitor(mon);
		}
		*/
	}

	private void sortConfigTable(List<SwitchMonitorCfg> cfgs){
		Collections.sort(cfgs);
	}

}
