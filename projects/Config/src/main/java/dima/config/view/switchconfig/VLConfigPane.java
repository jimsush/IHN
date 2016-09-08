package dima.config.view.switchconfig;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
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
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import dima.config.common.ConfigContext;
import dima.config.common.ConfigUtils;
import dima.config.common.controls.EnumTableCellRenderer;
import dima.config.common.controls.ListDataTableCellRenderer;
import dima.config.common.models.SwitchDevice;
import dima.config.common.models.SwitchVL;
import dima.config.common.models.SwitchVLCfg;
import dima.config.common.models.SwitchVLPlan;
import dima.config.common.services.ConfigDAO;
import dima.config.common.services.ServiceFactory;
import twaver.Element;
import twaver.ElementAttribute;
import twaver.Node;
import twaver.TDataBox;
import twaver.table.TElementTable;
import twaver.table.TTableModel;
import twaver.tree.ElementNode;
import twaver.tree.TTree;

public class VLConfigPane extends JPanel{

	private static final long serialVersionUID = -4144527399920723319L;

	private JPanel topPanel, mainPanel;
	private JComboBox<String> switchBox;
	private TDataBox tablebox;
	private TElementTable table;
	
	private ConfigDAO dao;
	
	private TDataBox treebox;
	private TTree tree;
	
	private Node treeRoot;
	
	private String currentSwitchName;
	private String rootIconURL;
	private String cfgIconURL;
	private String planIconURL;
	
	private SwitchVL copiedElement;
	
	public VLConfigPane(){
		initUI();
	}
	
	private void initUI(){
		this.setLayout(new BorderLayout());
		
		initService();
		
		initTopPanel();
		initBottomPanel();

		if(switchBox.getSelectedItem()!=null){
			String switchName=switchBox.getSelectedItem().toString();
			this.currentSwitchName=switchName;
			
			fillTree(switchName);
			
			// select the first Plan
			List<?> children = treeRoot.getChildren();
			if(children!=null && children.size()>0){
				Object child=children.get(0);
				Element cfgElement=(Element)child;
				List<?> childrenPlan = cfgElement.getChildren();
				if(childrenPlan!=null && childrenPlan.size()>0){
					Object childPlan=childrenPlan.get(0);
					Element planElement=(Element)childPlan;
					
					planElement.setSelected(true);
					int[] cfgTableIdPlanId = getCurrentCfgTableIdPlanId(planElement);
					if(cfgTableIdPlanId!=null && cfgTableIdPlanId[1]>=0){
						fillTable(switchName, cfgTableIdPlanId[0], cfgTableIdPlanId[1]);
					}
				}
			}
		}
	}
	
	private void initService(){
		dao=ServiceFactory.getService(ConfigDAO.class);
		rootIconURL=ConfigUtils.getImageURLString("root.png");
		cfgIconURL=ConfigUtils.getImageURLString("config.png");
		planIconURL=ConfigUtils.getImageURLString("plan.png");
	}
	
	private void initTopPanel(){
		topPanel=new JPanel(new BorderLayout());
		
		JPanel buttonsPanel=new JPanel(new FlowLayout());
		buttonsPanel.setBorder(BorderFactory.createTitledBorder("显示范围")); 
		
		JLabel label1=new JLabel("选择交换机:");
		buttonsPanel.add(label1);
		
		switchBox=new JComboBox<>();
		
		List<String> allSwitches = getAllSwitchNames();
		for(String switchName : allSwitches){
			switchBox.addItem(switchName);
		}
		
		switchBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Object obj=switchBox.getSelectedItem();
				if(obj==null){
					return;
				}
				
				String switchName=obj.toString();
				currentSwitchName=switchName;
				
				fillTree(switchName);
				
				Element ele = treebox.getLastSelectedElement();
				int[] cfgTableIdPlanId = getCurrentCfgTableIdPlanId(ele);
				if(cfgTableIdPlanId!=null && cfgTableIdPlanId[1]>=0){
					fillTable(switchName, cfgTableIdPlanId[0], cfgTableIdPlanId[1]);
				}
			}
		});
		buttonsPanel.add(switchBox);
		
		JLabel label3=new JLabel("排序方式:");
		buttonsPanel.add(label3);
		
		JComboBox<String> sortingBox=new JComboBox<>();
		sortingBox.addItem("虚拟链路号");
		buttonsPanel.add(sortingBox);
		
		JButton btnAddCfgTable=new JButton("创建配置表");
		buttonsPanel.add(btnAddCfgTable);
		btnAddCfgTable.addActionListener(new ActionListener(){
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
				
				CreateVLCfgTableDialog dialog=new CreateVLCfgTableDialog(ConfigContext.mainFrame,
						"创建配置表", true, 0, 0, 0, VLConfigPane.this);
				dialog.setVisible(true);
			}
		});
		
		JButton btnDeleteCfgTable=new JButton("删除配置表");
		buttonsPanel.add(btnDeleteCfgTable);
		btnDeleteCfgTable.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element ele = treebox.getLastSelectedElement();
				int[] cfgTableIdPlanId = getCurrentCfgTableIdPlanId(ele);
				if(cfgTableIdPlanId!=null && cfgTableIdPlanId[0]>0){
					int confirm=JOptionPane.showConfirmDialog(ConfigContext.mainFrame, "确定要删除吗?", UIManager.getString("OptionPane.titleText"),JOptionPane.YES_NO_OPTION);
					if(confirm!=JOptionPane.OK_OPTION){
						return;
					}
					
					deleteConfigTable(cfgTableIdPlanId[0]);
				}
			}
		});
		
		JLabel label4=new JLabel("                 ");
		buttonsPanel.add(label4);
		
		topPanel.add(buttonsPanel, BorderLayout.WEST);

		this.add(topPanel, BorderLayout.NORTH);
	}
	
	private void initBottomPanel(){
		mainPanel=new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createTitledBorder("编辑配置方案的VL")); 
		
		JPanel buttonPanel=new JPanel(new FlowLayout());

		JButton btn1=new JButton("增加记录");
		btn1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element ele = treebox.getLastSelectedElement();
				int[] cfgTableIdPlanId = getCurrentCfgTableIdPlanId(ele);
				if(cfgTableIdPlanId!=null && cfgTableIdPlanId[0]>0 && cfgTableIdPlanId[1]>0){
					CreateSwitchVLDialog dialog=new CreateSwitchVLDialog(ConfigContext.mainFrame, "增加记录", currentSwitchName,
							cfgTableIdPlanId[0], cfgTableIdPlanId[1], null, VLConfigPane.this);
					dialog.setVisible(true);
				}else{
					JOptionPane.showMessageDialog(ConfigContext.mainFrame, "请选择配置方案");
				}
			}
		});
		buttonPanel.add(btn1);
		
		JButton btn2=new JButton("修改记录");
		btn2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element element = tablebox.getLastSelectedElement();
				if(element==null){
					JOptionPane.showMessageDialog(ConfigContext.mainFrame, "请选中一条记录");
					return;
				}

				openModifySwitchVLDialog((SwitchVL)element);
			}
		});
		buttonPanel.add(btn2);
		
		JButton btn3=new JButton("删除记录");
		btn3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Element element = tablebox.getLastSelectedElement();
				if(element==null){
					JOptionPane.showMessageDialog(ConfigContext.mainFrame, "没有选中任何记录");
					return;
				}
				
				int confirm=JOptionPane.showConfirmDialog(ConfigContext.mainFrame, "确定要删除记录吗?", UIManager.getString("OptionPane.titleText"),JOptionPane.YES_NO_OPTION);
				if(confirm==JOptionPane.OK_OPTION){
					deleteSwitchVL((SwitchVL)element);
				}
			}
		});
		buttonPanel.add(btn3);
		
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
				SwitchVL currentVL=(SwitchVL)element;
				copiedElement=cloneSwitchVL(currentVL, currentVL.getCfgTableId(), currentVL.getPlanId(), currentVL.getVLID());
			}
		});
		
		JButton pasteBtn=new JButton("粘贴记录");
		buttonPanel.add(pasteBtn);
		pasteBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(copiedElement==null){
					JOptionPane.showMessageDialog(ConfigContext.mainFrame, "请先复制一条记录");
					return;
				}
				
				Element treeElement=treebox.getLastSelectedElement();
				int[] cfgPlan=getCurrentCfgTableIdPlanId(treeElement);
				if(cfgPlan[1]>0){
					int nextVLID=getNextVLID();
					SwitchVL newVL=cloneSwitchVL(copiedElement, cfgPlan[0], cfgPlan[1], nextVLID);
					
					addOrUpdateSwitchVL(newVL, null, ConfigContext.mainFrame);
				}
			}
		});

		JButton btn5=new JButton("删除所选交换机的所有VL配置");
		buttonPanel.add(btn5);
		btn5.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int confirm=JOptionPane.showConfirmDialog(ConfigContext.mainFrame, "确定要删除吗?", UIManager.getString("OptionPane.titleText"),JOptionPane.YES_NO_OPTION);
				if(confirm!=JOptionPane.OK_OPTION){
					return;
				}
				
				SwitchDevice swDev = dao.readSwitchDevice(currentSwitchName, true);
				List<SwitchVLCfg> vlCfgs = swDev.getVlCfgs();
				for(SwitchVLCfg vlCfg : vlCfgs){
					List<SwitchVLPlan> plans = vlCfg.getPlans();
					for(SwitchVLPlan plan : plans){
						plan.setVlNum(0);
						plan.setVls(new ArrayList<>());
					}
				}
				dao.saveSwitchDevice(swDev, null);
	
				tablebox.clear();
			}
		});
		
		mainPanel.add(buttonPanel, BorderLayout.NORTH);
		
		initTable();
		JScrollPane tablePanel=new JScrollPane(this.table);

		mainPanel.add(tablePanel, BorderLayout.CENTER);
		
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
				if(node==null){
					return;
				}
				
				Element ele=node.getElement();
				int[] cfgTableIdPlanId = getCurrentCfgTableIdPlanId(ele);
				if(cfgTableIdPlanId!=null && cfgTableIdPlanId[1]>=0){
					fillTable(currentSwitchName, cfgTableIdPlanId[0], cfgTableIdPlanId[1]);
				}else{
					tablebox.clear();
				}
			}});

		JScrollPane treePane=new JScrollPane(this.tree);
		
		JSplitPane bottomPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,treePane,mainPanel );
		bottomPane.setDividerLocation(140);
		
		this.add(bottomPane, BorderLayout.CENTER);
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
		
		table.setElementClass(SwitchVL.class);
		
		List<ElementAttribute> attributes = new ArrayList<ElementAttribute>();
		ElementAttribute attribute = new ElementAttribute();
		attribute.setName("switchName");
		attribute.setDisplayName("交换机名称");
		attributes.add(attribute);
		
		attribute.setName("cfgTableId");
		attribute.setDisplayName("配置表");
		attributes.add(attribute);
		
		attribute.setName("planId");
		attribute.setDisplayName("配置方案");
		attributes.add(attribute);
		
		attribute = new ElementAttribute();
		attribute.setName("VLID");
		attribute.setDisplayName("虚拟链路号");
		attributes.add(attribute);
		attribute = new ElementAttribute();
		attribute.setName("type");
		attribute.setDisplayName("类型");
		attributes.add(attribute);
		attribute = new ElementAttribute();
		attribute.setName("bag");
		attribute.setDisplayName("BAG");
		attributes.add(attribute);
		attribute = new ElementAttribute();
		attribute.setName("jitter");
		attribute.setDisplayName("Jitter");
		attributes.add(attribute);
		attribute = new ElementAttribute();
		attribute.setName("ttInterval");
		attribute.setDisplayName("TT周期");
		attributes.add(attribute);
		attribute = new ElementAttribute();
		attribute.setName("ttWindow");
		attribute.setDisplayName("TT窗口");
		attributes.add(attribute);
		attribute = new ElementAttribute();
		attribute.setName("inputPortNo");
		attribute.setDisplayName("输入端口");
		attributes.add(attribute);
		attribute = new ElementAttribute();
		attribute.setName("outputPortNos");
		attribute.setDisplayName("输出端口列表");
		attributes.add(attribute);
		
		table.registerElementClassAttributes(SwitchVL.class, attributes);
		
		table.getTableModel().sortColumn("VLID", TTableModel.SORT_ASCENDING);
		
		table.getColumnByName("outputPortNos").setCellRenderer(new ListDataTableCellRenderer());

		Map<Object, String> mappingType=new HashMap<>();
		mappingType.put(1,"BE");
		mappingType.put(2, "RC");
		mappingType.put(3, "TT");
		EnumTableCellRenderer rendererType=new EnumTableCellRenderer(mappingType);
		table.getColumnByName("type").setCellRenderer(rendererType);
		
		table.addElementDoubleClickedActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				openModifySwitchVLDialog((SwitchVL)e.getSource());
			}
		});
		
		return table;
	}
	
	private void openModifySwitchVLDialog(SwitchVL data){
		CreateSwitchVLDialog dialog=new CreateSwitchVLDialog(ConfigContext.mainFrame, "修改记录", currentSwitchName,
				data.getCfgTableId(), data.getPlanId(), data, VLConfigPane.this);
		dialog.setVisible(true);
	}

	private List<String> getAllSwitchNames(){
		List<String> list=new ArrayList<String>();
		List<SwitchDevice> switches = dao.readAllSwitchDevices(true);
		for(SwitchDevice sw : switches){
			list.add(sw.getSwitchName());
		}
		return list;
	}
	
	private void fillTable(String switchName, int cfgTableId, int planId){
		tablebox.clear();
		
		SwitchDevice switchDevice = dao.readSwitchDevice(switchName, true);
		if(switchDevice!=null){
			List<SwitchVLCfg> vlCfgs = switchDevice.getVlCfgs();
			for(SwitchVLCfg vlCfg : vlCfgs){
				if(vlCfg.getCfgTableId()==cfgTableId){
					List<SwitchVLPlan> plans = vlCfg.getPlans();
					for(SwitchVLPlan plan : plans){
						if(plan.getPlanId()==planId){
							List<SwitchVL> vls = plan.getVls();
							for(SwitchVL vl : vls){
								tablebox.addElement(vl);
							}
							break;
						}
					}
					break;
				}
			}
		}
	}
	
	private void fillTree(String switchName){
		treebox.clear();

		treeRoot=new Node(switchName);
		treeRoot.setName(switchName);
		treeRoot.setIcon(this.rootIconURL);
		treebox.addElement(treeRoot);
		
		SwitchDevice switchDevice = dao.readSwitchDevice(switchName, true);
		List<SwitchVLCfg> vlCfgs = switchDevice.getVlCfgs();
		for(SwitchVLCfg vlcfg : vlCfgs){
			Node cfg=createCfgTableTreeNode(treeRoot,vlcfg.getCfgTableId(),vlcfg.getPlanNum(),vlcfg.getDefaultPlanId());
			
			List<SwitchVLPlan> plans = vlcfg.getPlans();
			for(SwitchVLPlan plan : plans){
				createPlanTreeNode(cfg,plan.getPlanId(),plan.getVlNum());
			}
		}
		
		tree.expandAll();
	}
	
	private Node createCfgTableTreeNode(Element parentNode, int cfgTableId, int planNum, int defaultPlanId){
		Node cfgNode=new Node("CFG_"+cfgTableId);
		cfgNode.setName("配置表"+cfgTableId);
		cfgNode.setIcon(this.cfgIconURL);
		cfgNode.setParent(parentNode);
		cfgNode.setUserObject(planNum+":"+defaultPlanId);
		treebox.addElement(cfgNode);
		return cfgNode;
	}
	
	private Node createPlanTreeNode(Element parentNode, int planId, int vlNum){
		Node planNode=new Node(parentNode.getID()+":"+"PLAN_"+planId);
		planNode.setName("方案"+planId);
		planNode.setIcon(this.planIconURL);
		planNode.setParent(parentNode);
		treebox.addElement(planNode);
		return planNode;
	}
	
	private void deleteCfgTableTreeNode(int cfgTableId){
		treebox.removeElementByID("CFG_"+cfgTableId);
	}
	
	/**
	 * 
	 * @return [0] cfg table id [1] plan id
	 */
	private int[] getCurrentCfgTableIdPlanId(Element ele){
		if(ele==null || ele.getParent()==null){
			return null; //null or root node
		}
		
		String twaverId = ele.getID().toString();
		int pos=twaverId.indexOf("PLAN_");
		if(pos<0){
			// configuration table node
			String cfgTableIdStr = twaverId.substring(4);
			return new int[]{ Integer.valueOf(cfgTableIdStr), -1};
		}else{
			// plan node CFG_1:PLAN_2
			String planIdStr = twaverId.substring(pos+5);
			String[] fields = twaverId.split(":");
			String cfgTableIdStr = fields[0].substring(4);
			return new int[]{ Integer.valueOf(cfgTableIdStr), Integer.valueOf(planIdStr)};
		}
	}
	
	public boolean addOrUpdateSwitchVL(SwitchVL newVL, SwitchVL oldVL, Window dlg){
		if(oldVL==null){
			try{
				tablebox.addElement(newVL);
			}catch(Exception ex){
				JOptionPane.showMessageDialog(dlg, "添加错误:"+ex.getMessage());
				return false;
			}
			
			SwitchDevice sw = dao.readSwitchDevice(newVL.getSwitchName(), true);
			List<SwitchVLCfg> vlCfgs = sw.getVlCfgs();
			for(SwitchVLCfg vlCfg : vlCfgs){
				if(vlCfg.getCfgTableId()==newVL.getCfgTableId()){
					List<SwitchVLPlan> plans = vlCfg.getPlans();
					for(SwitchVLPlan plan : plans){
						if(newVL.getPlanId()==plan.getPlanId()){
							plan.addVL(newVL);
							
							List<SwitchVL> vls = plan.getVls();
							
							plan.setVlNum(vls.size());
							sortVLs(vls);
							break;
						}
					}
					break;
				}
			}
			
			dao.saveSwitchDevice(sw, null);
		}else{
			// update: remove old VL first, and then add a new VL
			SwitchDevice sw = dao.readSwitchDevice(newVL.getSwitchName(), true);
			List<SwitchVLCfg> vlCfgs = sw.getVlCfgs();
			for(SwitchVLCfg vlCfg : vlCfgs){
				if(vlCfg.getCfgTableId()==newVL.getCfgTableId()){
					List<SwitchVLPlan> plans = vlCfg.getPlans();
					for(SwitchVLPlan plan : plans){
						if(newVL.getPlanId()==plan.getPlanId()){
							List<SwitchVL> vls = plan.getVls();
							for(SwitchVL vl : vls){
								if(vl.getVLID()==oldVL.getVLID()){
									vl.setBag(newVL.getBag());
									vl.setBe(newVL.getBag());
									vl.setJitter(newVL.getJitter());
									vl.setType(newVL.getType());
									vl.setTtInterval(newVL.getTtInterval());
									vl.setTtWindow(newVL.getTtWindow());
									vl.setInputPortNo(newVL.getInputPortNo());
									vl.setOutputPortNos(newVL.getOutputPortNos());
									break;
								}
							}
							break;
						}
					}
					break;
				}
			}
			dao.saveSwitchDevice(sw, null);
			
			tablebox.removeElement(oldVL);
			tablebox.addElement(newVL);
		}
		return true;
	}
	
	public void deleteSwitchVL(SwitchVL currentVL){
		SwitchDevice sw = dao.readSwitchDevice(currentVL.getSwitchName(), true);
		List<SwitchVLCfg> vlCfgs = sw.getVlCfgs();
		for(SwitchVLCfg vlCfg : vlCfgs){
			if(vlCfg.getCfgTableId()==currentVL.getCfgTableId()){
				List<SwitchVLPlan> plans = vlCfg.getPlans();
				for(SwitchVLPlan plan : plans){
					if(currentVL.getPlanId()==plan.getPlanId()){
						List<SwitchVL> vls = plan.getVls();
						Iterator<SwitchVL> it = vls.iterator();
						for(; it.hasNext(); ){
							SwitchVL vl = it.next();
							if(vl.getVLID()==currentVL.getVLID()){
								it.remove();
								break;
							}
						}
						
						plan.setVlNum(vls.size()); //adjust vl number
						sortVLs(vls);
						break; //plan
					}
				}
				break; // cfg
			}
		}
		dao.saveSwitchDevice(sw, null);
		
		tablebox.removeElement(currentVL);
	}

	public void deleteConfigTable(int cfgTableId){
		deleteCfgTableTreeNode(cfgTableId);
		
		SwitchDevice swDev = dao.readSwitchDevice(currentSwitchName, true);
		List<SwitchVLCfg> vlCfgs = swDev.getVlCfgs();
		Iterator<SwitchVLCfg> it = vlCfgs.iterator();
		for(; it.hasNext(); ){
			SwitchVLCfg vlCfg = it.next();
			if(cfgTableId==vlCfg.getCfgTableId()){
				it.remove();
				break;
			}
		}
		dao.saveSwitchDevice(swDev, null);
		
		tablebox.clear();
	}
	
	@SuppressWarnings("unchecked")
	public void addOrUpdateConfigTable(boolean isAdd, int cfgTableId, int planNum, int defaultPlanId){
		if(isAdd){
			tablebox.clear();
			
			Node cfg=createCfgTableTreeNode(treeRoot,cfgTableId, planNum, defaultPlanId);
			Node lastPlan=null;
			for(int i=0; i<planNum; i++){
				lastPlan=createPlanTreeNode(cfg, i+1, 0);
			}
			if(lastPlan!=null){
				tree.clearSelection();
				lastPlan.setSelected(true);
			}
			tree.expandAll();

			SwitchDevice swDev = dao.readSwitchDevice(currentSwitchName, true);
			List<SwitchVLCfg> vlCfgs = swDev.getVlCfgs();
			SwitchVLCfg vlCfg=new SwitchVLCfg(cfgTableId, planNum, defaultPlanId);
			vlCfgs.add(vlCfg);
			for(int i=0; i<planNum; i++){
				SwitchVLPlan plan=new SwitchVLPlan(i+1);
				vlCfg.addPlan(plan);
			}
	
			Collections.sort(vlCfgs);
			
			dao.saveSwitchDevice(swDev, null);
		}
	}
	
	public void addOrUpdatePlan(boolean isAdd, int planId, int vlNum){
		Element ele = treebox.getLastSelectedElement();
		int[] cfgTableIdPlanId = getCurrentCfgTableIdPlanId(ele);
		if(cfgTableIdPlanId!=null && cfgTableIdPlanId[0]>=0 && cfgTableIdPlanId[1]>=0){
			SwitchDevice swDev = dao.readSwitchDevice(currentSwitchName, true);
			List<SwitchVLCfg> vlCfgs = swDev.getVlCfgs();
			for(SwitchVLCfg vlCfg : vlCfgs){
				if(cfgTableIdPlanId[0]==vlCfg.getCfgTableId()){
					List<SwitchVLPlan> plans = vlCfg.getPlans();
					for(SwitchVLPlan plan : plans){
						if(cfgTableIdPlanId[1]==plan.getPlanId()){
							plan.setVlNum(vlNum);
							break;
						}
					}
					break;
				}
			}
			dao.saveSwitchDevice(swDev, null);
		}
	}
	
	private void sortVLs(List<SwitchVL> vls){
		if(vls==null || vls.size()==0){
			return;
		}
		
		Collections.sort(vls, new Comparator<SwitchVL>(){
			@Override
			public int compare(SwitchVL o1, SwitchVL o2) {
				if(o2==null)
					return 1;
				if(o1==null)
					return -1;
				return o1.getVLID()-o2.getVLID();
		}});
	}
	
	private SwitchVL cloneSwitchVL(SwitchVL vl, int cfgTableId, int planId, int VLID){
		SwitchVL newVL=new SwitchVL(vl.getSwitchName(), cfgTableId, planId, VLID);
		newVL.setType(vl.getType());
		newVL.setBe(vl.getBe());
		newVL.setBag(vl.getBag());
		newVL.setJitter(vl.getJitter());
		newVL.setTtInterval(vl.getTtInterval());
		newVL.setTtWindow(vl.getTtWindow());
		newVL.setInputPortNo(vl.getInputPortNo());
		newVL.setOutputPortNos(vl.getOutputPortNos());
		return newVL;
	}
	
	private int getNextVLID(){
		List<?> elements = tablebox.getAllElements();
		if(elements==null || elements.size()==0){
			return 0;
		}
		int maxId=0;
		for(Object obj : elements){
			SwitchVL vl=(SwitchVL)obj;
			if(vl.getVLID()>=maxId){
				maxId=vl.getVLID();
			}
		}
		return maxId+1;
	}
	
}
