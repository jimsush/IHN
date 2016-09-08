package dima.config.view.netconfig;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dima.config.common.ConfigUtils;
import dima.config.common.controls.TableLayout;
import dima.config.common.models.NodeDevice;
import dima.config.common.models.SwitchDevice;
import dima.config.common.services.ConfigDAO;
import dima.config.common.services.ServiceFactory;
import twaver.TWaverUtil;

public class ChooseSwitchPortDialog extends JDialog{

	private static final long serialVersionUID = 8261712492490708610L;
	
	private NewNodeOrNMUPanel parentPane;
	
	private boolean isANet;
	private int oldPortNo;
	
	private String oldSwName;
	private int oldSwId;
	private int oldPortId;
	
	private ConfigDAO dao;
	
	private JComboBox<String> switchBox;
	private JTextField switchIdField;
	private JComboBox<String> portBox;
	
	public ChooseSwitchPortDialog(Window parent, String title, boolean isANet,int portNo, NewNodeOrNMUPanel parentPane){
		super(parent, title, ModalityType.APPLICATION_MODAL);
		
		setSize(360, 150);
        setResizable(false);
        TWaverUtil.centerWindow(this);
        
		setAlwaysOnTop(true);
		
		this.isANet=isANet;
		this.parentPane=parentPane;
		this.oldPortNo=portNo;
		
		initView();
	}
	
	private void initView(){
		dao = ServiceFactory.getService(ConfigDAO.class);

		if(this.oldPortNo>0){
			oldPortId=this.oldPortNo & 0xffff;
			oldSwId=(this.oldPortNo>>16) & 0xffff;
			
			List<SwitchDevice> switches = dao.readAllSwitchDevices(true);
			for(SwitchDevice sw : switches){
				if(sw.getLocalDomainID()==oldSwId){
					oldSwName=sw.getSwitchName();
					break;
				}
			}
		}
		
		Container mainPane = getContentPane();
		JPanel formPane=createFormPanel();
		mainPane.add(formPane, BorderLayout.CENTER);
		
		JPanel buttonPane=createButtonPanel();
		mainPane.add(buttonPane, BorderLayout.SOUTH);
	}
	
	private JPanel createFormPanel(){
		LayoutManager layout =  new TableLayout(new double[][] {
            { 10,  160, TableLayout.FILL, 160, 10 }, 
            ConfigUtils.getTableLayoutRowParam(4, 4) });
		JPanel pane=new JPanel(layout);
	    
		JLabel label1=new JLabel("交换机名称");
		pane.add(label1,"1,1,f,0");
		switchBox=new JComboBox<>();
		List<SwitchDevice> switches = dao.readAllSwitchDevices(true);
		for(SwitchDevice sw : switches){
			switchBox.addItem(sw.getSwitchName());
			
			if(oldSwName!=null && oldSwName.equals(sw.getSwitchName())){
				switchBox.setSelectedItem(sw.getSwitchName());
			}
		}
		pane.add(switchBox,"3,1,f,0");
		switchBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Object obj=switchBox.getSelectedItem();
				if(obj==null){
					return;
				}
				List<SwitchDevice> switches = dao.readAllSwitchDevices(true);
				for(SwitchDevice sw : switches){
					if(sw.getSwitchName().equals(obj)){
						switchIdField.setText(sw.getLocalDomainID()+"");
						
						fillSwitchUnusedPorts(sw);
						break;
					}
				}
			}
		});
		
		JLabel label2=new JLabel("交换机ID");
		pane.add(label2,"1,3,f,0");
		switchIdField=new JTextField();
		switchIdField.setEnabled(false);
		if(this.oldSwId>0){
			switchIdField.setText(this.oldSwId+"");
		}
		pane.add(switchIdField,"3,3,f,0");
		
		JLabel label3=new JLabel("端口号");
		pane.add(label3,"1,5,f,0");
		portBox=new JComboBox<>();
		pane.add(portBox,"3,5,f,0");
		
		Object curSwName=switchBox.getSelectedItem();
		if(curSwName!=null){
			List<SwitchDevice> switches2 = dao.readAllSwitchDevices(true);
			for(SwitchDevice sw : switches2){
				if(sw.getSwitchName().equals(curSwName)){
					switchIdField.setText(sw.getLocalDomainID()+"");
					fillSwitchUnusedPorts(sw);
					break;
				}
			}
		}
		if(this.oldPortId>0){
			portBox.setSelectedItem(this.oldPortId+"");
		}
		
		return pane;
	}
	
	private JPanel createButtonPanel(){
		LayoutManager layout =  new TableLayout(new double[][] {
            { 30,  80, 20, 80, 20, 80, 80, TableLayout.FILL }, { 4, 22, TableLayout.FILL, 4}});
	    JPanel buttonPane = new JPanel(layout);
	    final JButton okBtn = new JButton("确定");
	    final JButton clearBtn = new JButton("设为空");
	    final JButton cancelBtn = new JButton("取消");
	    
	    buttonPane.add(okBtn,"1,1,f,0");
	    buttonPane.add(clearBtn,"3,1,f,0");
	    buttonPane.add(cancelBtn,"5,1,f,0");
	    
	    ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Object source = e.getSource();
                if (source == okBtn) {
                	if(switchIdField.getText().length()>0){
                		parentPane.updateNetPortButton(isANet, switchBox.getSelectedItem().toString(),
                			Integer.valueOf(switchIdField.getText()), Integer.valueOf(portBox.getSelectedItem().toString()));
                	}
                	
                	dispose();
                }else if(source==clearBtn){
                	parentPane.updateNetPortButton(isANet, null, 0, 0);
                	
                	dispose();
                }else if(source==cancelBtn){
                	dispose();
                }
            }
	    };
	    
	    okBtn.addActionListener(l);
	    clearBtn.addActionListener(l);
        cancelBtn.addActionListener(l);
        
		return buttonPane;
	}
	
	private void fillSwitchUnusedPorts(SwitchDevice sw){
		portBox.removeAllItems();
		
		Set<Integer> eportIds = sw.getEportFESet();
		Set<Integer> usedPortNos = new HashSet<>();
		for(Integer portId : eportIds){
			usedPortNos.add(portId & 0xffff); // low 2 bytes
		}
		// other nodes used this switch's ports
		int swId=sw.getLocalDomainID();
		List<NodeDevice> nodes = dao.readAllNodeDevices(true);
		for(NodeDevice node : nodes){
			int swId1=(node.getPortNoToA() >> 16) & 0xffff;
			if(swId==swId1){
				usedPortNos.add(node.getPortNoToA() & 0xffff); // low 2 bytes
			}
			int swId2=(node.getPortNoToB() >> 16) & 0xffff;
			if(swId==swId2){
				usedPortNos.add(node.getPortNoToB() & 0xffff); // low 2 bytes
			}
		}
		if(oldPortId>0 && sw.getLocalDomainID()==oldSwId){
			usedPortNos.remove(oldPortId);
		}
		
		int maxPort=sw.getPortNumber();
		for(int i=1; i<=maxPort; i++){
			if(!usedPortNos.contains(i)){
				portBox.addItem(i+"");
			}
		}
	}
	
}
