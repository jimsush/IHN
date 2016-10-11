package dima.config.view.netconfig;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;

import dima.config.common.ConfigContext;
import dima.config.common.ConfigUtils;
import dima.config.common.controls.TableLayout;
import dima.config.common.models.NodeDevice;
import dima.config.common.models.SwitchDevice;
import dima.config.common.services.ConfigDAO;
import dima.config.common.services.ServiceFactory;
import dima.config.view.importfile.ImportFileActions;
import dima.config.view.netconfig.topo.SetRedundancyDialog;
import dima.config.view.netconfig.topo.TopoServiceImpl;
import twaver.Element;
import twaver.PopupMenuGenerator;
import twaver.TDataBox;
import twaver.TView;
import twaver.network.TNetwork;

public class TopoView extends JPanel implements UpdateCallback{
	
	private static final long serialVersionUID = 8726899116761882991L;
	private TopoServiceImpl topoService;
	private ConfigDAO dao;
	private TDataBox box;
	private TNetwork network;
	
	public TopoView(){
		initService();
		initUI();
		
		initPopupMenu();
		
		ConfigContext.topoView=this;
	}
	
	private void initService(){
		 dao= ServiceFactory.getService(ConfigDAO.class);
	}
	
	private void initUI(){
		box = new TDataBox("mida");
		network=new TNetwork(box);
		
		ConfigUtils.initAttachment();
		
		topoService=new TopoServiceImpl(box, network);
		
		this.setLayout(new BorderLayout());
		
		LayoutManager layout =  new TableLayout(new double[][] {
            { 10,  100, 10 }, { 20, 22, 4, 22, 4, 22, 4, 22, 4, 22, TableLayout.FILL}});
	    JPanel buttonPane=new JPanel(layout);
	    
		JButton addSwitchBtn=new JButton("添加交换机");
		JButton addNodeBtn=new JButton("添加节点");
		JButton deleteBtn=new JButton("删除");
		JButton saveBtn=new JButton("保存");
		JButton cancelBtn=new JButton("取消");
		
		buttonPane.add(addSwitchBtn, "1,1,f,0");
		buttonPane.add(addNodeBtn, "1,3,f,0");
		buttonPane.add(deleteBtn, "1,5,f,0");
		buttonPane.add(saveBtn, "1,7,f,0");
		buttonPane.add(cancelBtn, "1,9,f,0");
		
		addSwitchBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showCreateSwitchDialog();
			}
		});
		
		addNodeBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showCreateNodeDialog();
			}
		});
		
		deleteBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				deleteAction();
			}
		});
				
		JPanel topoPane=new JPanel(new BorderLayout());
		topoPane.add(network, BorderLayout.CENTER);
		
		this.add(buttonPane, BorderLayout.WEST);
		this.add(topoPane, BorderLayout.CENTER);
		
		initSwitchAndNodes();
	}
	
	private void initPopupMenu() {
		PopupMenuGenerator popupMenuGenerator = new PopupMenuGenerator() {
			@Override
			public JPopupMenu generate(TView arg0, MouseEvent arg1) {
				JPopupMenu popMenu = new JPopupMenu();
				if(!box.getSelectionModel().isEmpty()){
					JMenuItem item=new JMenuItem("删除");
			    	  item.addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent arg0) {
								deleteAction();
							}
				      });
			    	popMenu.add(item); 
				}else{
					JMenuItem item=new JMenuItem("网络冗余设置");
			    	  item.addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent arg0) {
								openRedudancyAction();
							}
				      });
			    	popMenu.add(item);
			    	popMenu.addSeparator();
			    	
			    	item=new JMenuItem("导入topo_ctrl");
			    	  item.addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent arg0) {
								ConfigDAO dao=ServiceFactory.getService(ConfigDAO.class);
								dao.clearAll();
								
								ImportFileActions.openImportTopoCtrlAction();
							}
				      });
			    	popMenu.add(item);
	
			    	item=new JMenuItem("导入config_ctrl");
			    	  item.addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent arg0) {
								ImportFileActions.openImportConfigCtrlAction();
							}
				      });
			    	popMenu.add(item);
			    	popMenu.addSeparator();
			    	
			    	item=new JMenuItem("导入ICD");
			    	  item.addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent arg0) {
								//ConfigDAO dao=ServiceFactory.getService(ConfigDAO.class);
								//dao.clearAll();
								
								ImportFileActions.openImportADCAction();
							}
				      });
			    	popMenu.add(item);
			    	
			    	popMenu.addSeparator();
			    	item=new JMenuItem("清空所有设备");
			    	  item.addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent arg0) {
								ConfigDAO dao=ServiceFactory.getService(ConfigDAO.class);
								dao.clearAll();
							}
				      });
			    	popMenu.add(item);
				}
				return popMenu;
			}
		};
		
		network.setPopupMenuGenerator(popupMenuGenerator);
	}
	
	private void initSwitchAndNodes(){
		List<SwitchDevice> switches = getSortedSwitches();
		for(SwitchDevice switchDev : switches){
			try{
				addSwitch(switchDev, null);
			}catch(Exception ex){
				System.out.println("addSwitch "+switchDev.getSwitchName()+"("+switchDev.getLocalDomainID()+") failed due to "+ex.getMessage());
			}
		}
		
		List<NodeDevice> nodes = dao.readAllNodeDevices(true);
		for(NodeDevice nodeDev : nodes){
			if(ConfigUtils.TYPE_NODE==nodeDev.getType()){
				addNode(nodeDev, null);
			}
		}
	}
	
	private List<SwitchDevice> getSortedSwitches(){
		List<SwitchDevice> sortedSwitches=new ArrayList<>();
		List<SwitchDevice> switches = dao.readAllSwitchDevices(true);
		if(switches==null || switches.size()==0){
			return sortedSwitches;
		}
		
		Map<Integer, SwitchDevice> id2Switches=new HashMap<>();
		Map<String, List<Integer>> switch2EportNos=new HashMap<>();
		// prepare
		for(SwitchDevice curSw : switches){
			id2Switches.put(curSw.getLocalDomainID(), curSw);
			switch2EportNos.put(curSw.getSwitchName(), curSw.getEportNos());
		}
		
		// find the first and second switches
		for(SwitchDevice curSw : switches){
			List<Integer> eportFullNos = switch2EportNos.get(curSw.getSwitchName());
			if(eportFullNos.size()>0){
				for(Integer peerPortFullNo : eportFullNos){
					int peerSwitchId=(peerPortFullNo>>16) & 0xffff;
					SwitchDevice peerSw = id2Switches.get(peerSwitchId);
					if(peerSw!=null){
						if(curSw.getLocalDomainID()<peerSw.getLocalDomainID()){
							sortedSwitches.add(curSw);
							sortedSwitches.add(peerSw);
						}else{
							sortedSwitches.add(peerSw);
							sortedSwitches.add(curSw);
						}
						break;
					}
				}
			}
		}
		
		// add other switches into the tail of the list
		for(SwitchDevice curSw : switches){
			if(!sortedSwitches.contains(curSw)){
				sortedSwitches.add(curSw);
			}
		}
		return sortedSwitches;
	}
	
	private void openRedudancyAction(){
		SetRedundancyDialog dlg=new SetRedundancyDialog(ConfigContext.mainFrame,"网络冗余设置", this);
		dlg.setVisible(true);
	}
	
	private void deleteAction(){
		Element element = box.getLastSelectedElement();
		if(element==null){
			JOptionPane.showMessageDialog(ConfigContext.mainFrame, "没有选中任何要删除的交换机或节点");
			return;
		}
		
		int confirm=JOptionPane.showConfirmDialog(ConfigContext.mainFrame, "确定要删除记录吗?", UIManager.getString("OptionPane.titleText"), JOptionPane.YES_NO_OPTION);
		if(confirm==JOptionPane.OK_OPTION){
			Object typeObj=ConfigUtils.getUserObjectProp(element, "type");
			String t = (String)typeObj;
			
			if(ConfigUtils.TYPE_STR_SW.equals(t)){
				deleteDevice(element.getID().toString(), ConfigUtils.TYPE_SW);
			}else if(ConfigUtils.TYPE_STR_NODE.equals(t)){
				deleteDevice(element.getID().toString(), ConfigUtils.TYPE_NODE);
			}else{
				deleteDevice(element.getID().toString(), ConfigUtils.TYPE_NMU);
			}
		}
	}
	
	@Override
	public SwitchDevice addSwitch(SwitchDevice switchDev, String oldId){
		return topoService.addSwitch(switchDev, oldId);
	}
	
	@Override
	public NodeDevice addNode(NodeDevice nodeDev, String oldId){
		return topoService.addNode(nodeDev, oldId);
	}
	
	private void showCreateSwitchDialog(){
		List<SwitchDevice> switches = dao.readAllSwitchDevices(true);
		if(switches.size()>=ConfigContext.MAX_NUM_SWITCH){
			JOptionPane.showMessageDialog(ConfigContext.mainFrame, "已有"+ConfigContext.MAX_NUM_SWITCH+"个交换机，不能继续添加交换机");
			return;
		}
		
		NewSwitchDialog dialog=new NewSwitchDialog(ConfigContext.mainFrame, "添加交换机", null, this);
		dialog.setVisible(true);
	}
	
	private void showCreateNodeDialog(){
		NewNodeDialog dialog=new NewNodeDialog(ConfigContext.mainFrame, "添加节点", null, this, true, null);
		dialog.setVisible(true);
	}

	@Override
	public void deleteDevice(String twaverId, int type) {
		topoService.deleteDevice(twaverId, type);
	}
	
	public void updateAttachment(int newRedundancy){
		topoService.updateAttachment(newRedundancy);
	}

	public void clearAll() {
		this.box.clear();
	}
	

}
