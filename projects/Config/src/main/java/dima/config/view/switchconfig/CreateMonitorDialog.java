package dima.config.view.switchconfig;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dima.config.common.ConfigUtils;
import dima.config.common.controls.TableLayout;
import dima.config.common.models.SwitchDevice;
import dima.config.common.models.SwitchMonitorPort;
import dima.config.common.models.SwitchVL;
import dima.config.common.models.SwitchVLCfg;
import dima.config.common.models.SwitchVLPlan;
import dima.config.common.services.ConfigDAO;
import dima.config.common.services.ServiceFactory;
import twaver.TWaverUtil;

public class CreateMonitorDialog extends JDialog{

	private static final long serialVersionUID = -31949476570247773L;
	
	private ConfigDAO dao=null;
	
	private String switchName;
	private int currentCfgTableId;
	
	private SwitchMonitorPort oldData;
	private JTextField switchField;
	private JTextField portIdField;
	
	private JComboBox<String> portEnableBox;
	private JComboBox<String> portModeBox;
	
	private MonitorConfigPane listPanel; //parent panel
	
	private List<Integer> portInputPorts=new ArrayList<Integer>();
	private List<Integer> portOutputPorts=new ArrayList<Integer>();
	private List<Integer> portVLs=new ArrayList<Integer>();
	
	private JLabel label3;
	private JLabel label4;
	private JLabel label5;
	private JButton btnVL;
	private JButton btnInputPort;
	private JButton btnOutputPort;
	
	private int selectedPlanId;
	
	public CreateMonitorDialog(Window parent, String title, String switchName, int currentCfgTableId, SwitchMonitorPort data, MonitorConfigPane listPanel){
		super(parent, title, ModalityType.APPLICATION_MODAL);
		setAlwaysOnTop(true);
		
		this.switchName=switchName;
		this.currentCfgTableId=currentCfgTableId;
		this.oldData=data;
		this.listPanel=listPanel;

		initView();
	}
	
	private void initView(){
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(480, 226);
        setResizable(false);
        TWaverUtil.centerWindow(this);
        
        dao=ServiceFactory.getService(ConfigDAO.class);
        
		LayoutManager borderLayout = new BorderLayout(10,10);
        JPanel contentPane = new JPanel(borderLayout);
        
        JPanel inputPane = createTopPanel();
        contentPane.add(inputPane, BorderLayout.NORTH);
        
        JPanel portPane = createMonitorPortPanel();
        contentPane.add(portPane, BorderLayout.CENTER);
        
        JPanel btnPanel = createButtonPanel();
        contentPane.add(btnPanel, BorderLayout.SOUTH);
        
        getContentPane().add(contentPane);
	}

	private JPanel createButtonPanel(){
		LayoutManager layout =  new TableLayout(new double[][] {
            { 80,  80, TableLayout.FILL, 80, 80 }, { 4, 22, TableLayout.FILL, 4}});
	    JPanel buttonPane = new JPanel(layout);
	    final JButton okBtn = new JButton("确定");
	    final JButton cancelBtn = new JButton("取消");
	    
	    buttonPane.add(okBtn,"1,1,f,0");
	    buttonPane.add(cancelBtn,"3,1,f,0");
	    
	    ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Object source = e.getSource();
                if (source == okBtn) {
                	SwitchMonitorPort newMonitor = getData();
                	if(newMonitor==null){
                		JOptionPane.showMessageDialog(CreateMonitorDialog.this, "请输入完整的监控信息");
                		return;
                	}
                	
                	listPanel.addOrUpdateSwitchMonitor(newMonitor, oldData);
                	
                	dispose();
                }else if(source==cancelBtn){
                	dispose();
                }
            }
	    };
	    
	    okBtn.addActionListener(l);
        cancelBtn.addActionListener(l);
        
		return buttonPane;
	}

	private JPanel createMonitorPortPanel() {
		JPanel panel=new JPanel(new BorderLayout());
		JPanel pane0 = createMonitorPortPanel0();
		panel.add(pane0, BorderLayout.CENTER);
		return panel;
	}
	
	private JPanel createMonitorPortPanel0() {
		LayoutManager layout =  new TableLayout(new double[][] {
            { 6,  100, 4, 100, TableLayout.FILL, 100, 4, 100, 6 }, 
            { 4, 22, 4, 22, 4, 22, 4} });
	    JPanel pane=new JPanel(layout);
		pane.setBorder(BorderFactory.createTitledBorder("监控口设置")); 
		
		JLabel label1=new JLabel("捕获使能:");
		pane.add(label1, "1,1,f,0");
	    
		portEnableBox=new JComboBox<>();
		portEnableBox.addItem("不使能");
		portEnableBox.addItem("使能");
		pane.add(portEnableBox, "3,1,f,0");
		portEnableBox.setSelectedIndex(1);
		
		JLabel label2=new JLabel("捕获模式:");
		pane.add(label2, "5,1,f,0");
	    
		portModeBox=new JComboBox<>();
		portModeBox.addItem("端口捕获");
		portModeBox.addItem("VL捕获");
		portModeBox.setSelectedIndex(0);
		portModeBox.setEditable(false);
		pane.add(portModeBox, "7,1,f,0");

		label3=new JLabel("输入端口列表:");
		pane.add(label3, "1,3,f,0");
		
		btnInputPort=new JButton("选择...");
		pane.add(btnInputPort, "3,3,f,0");
		btnInputPort.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				List<Integer> allPorts=new ArrayList<Integer>();
				
				int portNum=16;
				SwitchDevice swDevice = dao.readSwitchDevice(switchName, true);
				if(swDevice!=null){
					portNum=swDevice.getPortNumber();
				}else{
					System.out.println("No such switch device, port number is "+portNum+" by default");
				}
				for(int i=0; i<=portNum; i++){
					allPorts.add(i);
				}
				ChooseObjectDialog dlg=new ChooseObjectDialog(CreateMonitorDialog.this,
						"输入端口列表", "端口", ChooseObjectDialog.CODE_IN_PORT, allPorts, 
						oldData==null ? new ArrayList<>() : oldData.getPortInputPortList());
				dlg.setVisible(true);
			}
		});
		
		label4=new JLabel("VL列表:");
		pane.add(label4, "5,3,f,0");
		btnVL=new JButton("选择...");
		pane.add(btnVL, "7,3,f,0");
		btnVL.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				SwitchDevice swDevice = dao.readSwitchDevice(switchName, true);
				if(swDevice==null){
					System.out.println("No such switch device");
					return;
				}
				
				List<SwitchVL> vls=null;
				List<SwitchVLPlan> plans=null;
				List<SwitchVLCfg> vlCfgs = swDevice.getVlCfgs();
				for(SwitchVLCfg vlCfg : vlCfgs){
					if(currentCfgTableId==vlCfg.getCfgTableId()){
						plans = vlCfg.getPlans();
						int size=plans.size();
						switch(size){
						case 0:
							break;
						case 1:
							SwitchVLPlan plan = plans.get(0);
							vls=plan.getVls();
							break;
						default:
							break;
						}
					}
				}
				if(vls==null && (plans==null || plans.size()==0)){
					System.out.println("No VL in this switch device for config table "+currentCfgTableId);
					JOptionPane.showMessageDialog(CreateMonitorDialog.this, "对应的配置表"+currentCfgTableId+"中还没有配置VL");
					return;
				}else if(vls==null && plans.size()>=2){
					// 2+ plans, so have to select one by clients
					selectedPlanId=0;
					ChoosePlanDialog planDlg=new ChoosePlanDialog(CreateMonitorDialog.this, "选择配置方案", plans, CreateMonitorDialog.this);
					planDlg.setVisible(true);
					if(selectedPlanId<=0){
						return;
					}
					for(SwitchVLPlan p : plans){
						if(selectedPlanId==p.getPlanId()){
							List<Integer> allVLs=new ArrayList<Integer>();
							List<SwitchVL> vls2 = p.getVls();
							for(SwitchVL vl : vls2){
								allVLs.add(vl.getVLID());
							}
							ChooseObjectDialog dlg=new ChooseObjectDialog(CreateMonitorDialog.this,
									"VL列表", "VL", ChooseObjectDialog.CODE_VL, allVLs, 
									oldData==null ? new ArrayList<>() : oldData.getPortVLList());
							dlg.setVisible(true);
							break;
						}
					}
				}else{
					// only 1 plan
					List<Integer> allVLs=new ArrayList<Integer>();
					for(SwitchVL vl : vls){
						allVLs.add(vl.getVLID());
					}
					ChooseObjectDialog dlg=new ChooseObjectDialog(CreateMonitorDialog.this,
							"VL列表", "VL", ChooseObjectDialog.CODE_VL, allVLs, 
							oldData==null ? new ArrayList<>() : oldData.getPortVLList());
					dlg.setVisible(true);
				}
			}
		});

		label5=new JLabel("输出端口列表:");
		pane.add(label5, "1,5,f,0");
		btnOutputPort=new JButton("选择...");
		pane.add(btnOutputPort, "3,5,f,0");
		btnOutputPort.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				List<Integer> allPorts=new ArrayList<Integer>();
				
				int portNum=16;
				SwitchDevice swDevice = dao.readSwitchDevice(switchName, true);
				if(swDevice!=null){
					portNum=swDevice.getPortNumber();
				}else{
					System.out.println("No such switch device, port number is "+portNum+" by default");
				}
				for(int i=0; i<=portNum; i++){
					allPorts.add(i);
				}
				
				ChooseObjectDialog dlg=new ChooseObjectDialog(CreateMonitorDialog.this,
						"输出端口列表", "端口", ChooseObjectDialog.CODE_OUT_PORT, allPorts, 
						oldData==null ? new ArrayList<>() : oldData.getPortOutputPortList());
				dlg.setVisible(true);
			}
		});
		
		if(oldData!=null){
			portEnableBox.setSelectedIndex(oldData.getPortEnableMonitor());
			portModeBox.setSelectedIndex(oldData.getPortMonitorMode());
			
			if(oldData.getPortMonitorMode()==0){//0 port, 1 vl
				label4.setEnabled(false);
				btnVL.setEnabled(false);
			}else{
				label3.setEnabled(false);
				btnInputPort.setEnabled(false);
				label5.setEnabled(false);
				btnOutputPort.setEnabled(false);
			}
			
			portInputPorts=oldData.getPortInputPortList();
			portOutputPorts=oldData.getPortOutputPortList();
			portVLs=oldData.getPortVLList();
		}else{
			// by default, capture mode is port instead of VL
			btnVL.setEnabled(false);
		}
		
		if(portEnableBox.getSelectedIndex()==0){
			disableCapture();
		}
		
		portEnableBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int enable=portEnableBox.getSelectedIndex();
				if(enable==0){
					disableCapture();
				}else{
					portModeBox.setEnabled(true);
					changeCaptureMode();
				}
			}
		});
		
		portModeBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				changeCaptureMode();
			}
		});
		return pane;
	}

	private void changeCaptureMode(){
		int mode=portModeBox.getSelectedIndex();
		if(mode==0){//0 port, 1 vl
			label4.setEnabled(false);
			btnVL.setEnabled(false);
			
			label3.setEnabled(true);
			btnInputPort.setEnabled(true);
			label5.setEnabled(true);
			btnOutputPort.setEnabled(true);
		}else{
			label4.setEnabled(true);
			btnVL.setEnabled(true);
			
			label3.setEnabled(false);
			btnInputPort.setEnabled(false);
			label5.setEnabled(false);
			btnOutputPort.setEnabled(false);
		}
	}
	
	private void disableCapture(){
		portModeBox.setEnabled(false);
		btnInputPort.setEnabled(false);
		btnOutputPort.setEnabled(false);
		btnVL.setEnabled(false);
	}
	
	private JPanel createTopPanel() {
		LayoutManager layout =  new TableLayout(new double[][] {
            { 6,  106, 4, 100, TableLayout.FILL, 100, 4, 100, 6 }, 
            { 4, 22, 4} });
	    JPanel inputPane = new JPanel(layout);
	    
	    JLabel label1=new JLabel("交换机(配置表):");
	    inputPane.add(label1, "1,1,f,0");
	    
	    switchField=new JTextField();
	    switchField.setText(switchName+"("+currentCfgTableId+")");
	    switchField.setEditable(false);
	    inputPane.add(switchField, "3,1,f,0");
	    
	    JLabel label2=new JLabel("监控口编号:");
	    inputPane.add(label2, "5,1,f,0");
	    
	    portIdField=ConfigUtils.getNumberTextField(2);
	    inputPane.add(portIdField, "7,1,f,0");
	    if(oldData!=null){
	    	portIdField.setText(oldData.getPortId()+"");
	    	portIdField.setEditable(false);
	    }

	    return inputPane;
	}
	
	public void choosePortInputPorts(List<Integer> inputPorts){
		if(inputPorts!=null){
			portInputPorts=inputPorts;
		}else{
			portInputPorts=new ArrayList<Integer>();
		}
	}
	
	public void choosePortOutputPorts(List<Integer> outputPorts){
		if(outputPorts!=null){
			portOutputPorts=outputPorts;
		}else{
			portOutputPorts=new ArrayList<Integer>();
		}
	}

	public void choosePortVLs(List<Integer> vls){
		if(vls!=null){
			portVLs=vls;
		}else{
			portVLs=new ArrayList<Integer>();
		}
	}

	private SwitchMonitorPort getData(){
		if(portIdField.getText().trim().length()<=0){
			return null;
		}
		
		SwitchMonitorPort monitor=new SwitchMonitorPort(switchName, currentCfgTableId, 
				Integer.valueOf(portIdField.getText().trim()));
		monitor.setPortEnableMonitor(portEnableBox.getSelectedIndex());
		monitor.setPortMonitorMode(portModeBox.getSelectedIndex());
		if(monitor.getPortMonitorMode()==0){
			monitor.setPortInputPortList(portInputPorts);
			monitor.setPortOutputPortList(portOutputPorts);
			monitor.setPortVLList(new ArrayList<>());
		}else{
			monitor.setPortInputPortList(new ArrayList<>());
			monitor.setPortOutputPortList(new ArrayList<>());
			monitor.setPortVLList(portVLs);
		}
		return monitor;
	}
	
	public void setSelectedPlanId(int selectedPlanId){
		this.selectedPlanId=selectedPlanId;
	}

}
