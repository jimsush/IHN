package dima.config.view.nodeconfig;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import dima.config.common.models.NodeVL;
import twaver.TWaverUtil;

public class CreateNodeVLDialog extends JDialog{

	private static final long serialVersionUID = 4478166130836141013L;
	
	private String nodeName;
	private NodeVL oldData;
	
	private JComboBox<String> nodeBox;
	private JTextField vlIDTxtField;
	private JComboBox<String> typeBox;
	
	private JComboBox<String> networkBox;
	private JComboBox<String> redudanceBox;
	private JComboBox<String> linkBox;
	private JComboBox<String> completeCheckBox;
	private JTextField rtcField;
	
	private JPanel rcPane;
	private JLabel bagLabel;
	private JLabel jitterLabel;
	private JTextField bagTxtField;
	private JTextField jitterTxtField;
	
	private JPanel ttPane;
	private JLabel intervalLabel;
	private JLabel windowLabel;
	private JTextField intervalTxtField;
	private JTextField windowTxtField;

	private boolean isTx;
	
	private NodeVLConfigPanel tablePanel;
	
	public CreateNodeVLDialog(Window parent, String title, String nodeName, 
			NodeVL data, NodeVLConfigPanel tablePanel, boolean isTx){
		super(parent, title, ModalityType.APPLICATION_MODAL);
		setAlwaysOnTop(true);
		
		this.isTx=isTx;
		this.nodeName=nodeName;
		this.oldData=data;
		this.tablePanel=tablePanel;
		
		initView();
	}
	
	private void initView(){
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(536, 300);
        setResizable(false);
        TWaverUtil.centerWindow(this);
        
		LayoutManager borderLayout = new BorderLayout(10,10);
        JPanel contentPane = new JPanel(borderLayout);
        
        JPanel inputPane = createTopPanel();
        contentPane.add(inputPane, BorderLayout.NORTH);
        
        JPanel portPane = createBottomPanel();
        contentPane.add(portPane, BorderLayout.CENTER);
        
        switchTypeSelection();
        
        JPanel btnPanel = createButtonPanel();
        contentPane.add(btnPanel, BorderLayout.SOUTH);
        
        getContentPane().add(contentPane);
        
        if(!isTx){
        	// receive, RX
	    	rtcField.setEnabled(false);
	    	bagTxtField.setEnabled(false);
	    	jitterTxtField.setEnabled(false);
	    	intervalTxtField.setEnabled(false);
	    	windowTxtField.setEnabled(false);
	    	networkBox.setEnabled(false);
	    }else{
	    	completeCheckBox.setEnabled(false);
	    }
	}
	
	private JPanel createTopPanel(){
		LayoutManager layout =  new TableLayout(new double[][] {
            { 6,  120, 4, 120, TableLayout.FILL, 120, 4, 120, 6 }, 
            { 4, 22, 4, 22, 10, 50, TableLayout.FILL, 4} });
	    JPanel inputPane = new JPanel(layout);
	    
	    JLabel label1=new JLabel("源终端名称:");
	    inputPane.add(label1, "1,1,f,0");
	    
	    nodeBox=new JComboBox<>();
	    nodeBox.addItem(nodeName);
	    nodeBox.setEditable(false);
	    inputPane.add(nodeBox, "3,1,f,0");
	    
	    JLabel label2=new JLabel("虚拟链路号:");
	    inputPane.add(label2, "5,1,f,0");
	    
	    vlIDTxtField=ConfigUtils.getNumberTextField();
	    vlIDTxtField.setText("0");
	    vlIDTxtField.setToolTipText("0~4095");
	    if(oldData!=null){
	    	vlIDTxtField.setText(oldData.getVLID()+"");
	    	vlIDTxtField.setEnabled(false);
	    }
	    inputPane.add(vlIDTxtField, "7,1,f,0");
	    
	    JLabel label3=new JLabel("类型:");
	    inputPane.add(label3, "1,3,f,0");
	    
	    typeBox=new JComboBox<>();
	    typeBox.addItem("BE"); //1
	    typeBox.addItem("RC"); //2 by default
	    typeBox.addItem("TT"); //3
	    if(oldData!=null){
	    	typeBox.setSelectedIndex(oldData.getType()-1);
	    }else{
	    	typeBox.setSelectedIndex(1);
	    }
	    inputPane.add(typeBox, "3,3,f,0");
	    typeBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				switchTypeSelection();
			}
		});

	    // RC
	    LayoutManager rclayout =  new TableLayout(new double[][] {
            { 6,  80, TableLayout.FILL, 80, 6 }, 
            { 4, 22, 4, 22, TableLayout.FILL, 4} });
	    rcPane = new JPanel(rclayout);
	    rcPane.setBorder(BorderFactory.createTitledBorder("RC属性设置")); 
	    
	    bagLabel=new JLabel("BAG:");
	    rcPane.add(bagLabel, "1,1,f,0");
	    
	    bagTxtField=ConfigUtils.getNumberTextField();
	    bagTxtField.setText("500");
	    if(oldData!=null){
	    	bagTxtField.setText(oldData.getBag()+"");
	    }
	    rcPane.add(bagTxtField, "3,1,f,0");
	    
	    jitterLabel=new JLabel("Jitter:");
	    rcPane.add(jitterLabel, "1,3,f,0");
	    
	    jitterTxtField=ConfigUtils.getNumberTextField();
	    jitterTxtField.setText("50");
	    if(oldData!=null){
	    	jitterTxtField.setText(oldData.getJitter()+"");
	    }
	    rcPane.add(jitterTxtField, "3,3,f,0");
	    
	    inputPane.add(rcPane, "1,5,3,7");

	    // TT
	    ttPane = new JPanel(rclayout);
	    ttPane.setBorder(BorderFactory.createTitledBorder("TT属性设置")); 
	    ttPane.setEnabled(false);
	    intervalLabel=new JLabel("周期:");
	    ttPane.add(intervalLabel, "1,1,f,0");
	    
	    intervalTxtField=ConfigUtils.getNumberTextField();
	    intervalTxtField.setText("0");
	    intervalTxtField.setEnabled(false);
	    ttPane.add(intervalTxtField, "3,1,f,0");
	    if(oldData!=null){
	    	intervalTxtField.setText(oldData.getTtInterval()+"");
	    }
	    
	    windowLabel=new JLabel("窗口:");
	    ttPane.add(windowLabel, "1,3,f,0");
	    
	    windowTxtField=ConfigUtils.getNumberTextField();
	    windowTxtField.setText("0");
	    windowTxtField.setEnabled(false);
	    ttPane.add(windowTxtField, "3,3,f,0");
	    if(oldData!=null){
	    	windowTxtField.setText(oldData.getTtWindow()+"");
	    }
	    
	    inputPane.add(ttPane, "5,5,7,7");
	    
	    return inputPane;
	}
	
	private JPanel createBottomPanel(){
		LayoutManager layout =  new TableLayout(new double[][] {
			{ 6,  120, 4, 120, TableLayout.FILL, 120, 4, 120, 6 } 
			, { 4, 22, 4, 22, 4, 22, 4}});
	    JPanel pane = new JPanel(layout);
	    
	    JLabel label1=new JLabel("网络选择");
	    pane.add(label1, "1,1,f,c");
	    
	    networkBox=new JComboBox<String>();
	    networkBox.addItem("A网");
	    networkBox.addItem("B网");
	    networkBox.addItem("BOTH");
	    pane.add(networkBox, "3,1,f,c");
	    if(oldData!=null){
	    	if(isTx){
	    		networkBox.setSelectedIndex(oldData.getNetworkType()-1);
	    	}else{
	    		// rx, does not have network type, so select the first one by default
	    		networkBox.setSelectedIndex(0);
	    	}
	    }else{
	    	networkBox.setSelectedIndex(0);
	    }
	    
	    JLabel label2=new JLabel("冗余管理");
	    pane.add(label2, "5,1,f,c");
	    
	    this.redudanceBox=new JComboBox<String>();
	    redudanceBox.addItem("不进行冗余管理");
	    redudanceBox.addItem("进行冗余管理");
	    pane.add(redudanceBox, "7,1,f,c");
	    if(oldData!=null){
	    	redudanceBox.setSelectedIndex(oldData.getRedudanceType());
	    }else{
	    	redudanceBox.setSelectedIndex(1);
	    }
	    
	    JLabel label3=new JLabel("链路用途");
	    pane.add(label3, "1,3,f,c");
	    
	    this.linkBox=new JComboBox<String>();
	    linkBox.addItem("正常通信");
	    linkBox.addItem("RTC");
	    pane.add(linkBox, "3,3,f,c");
	    if(oldData!=null){
	    	linkBox.setSelectedIndex(oldData.getUseOfLink());
	    }else{
	    	linkBox.setSelectedIndex(0);
	    }
	    
	    JLabel label4=new JLabel("RTC周期");
	    pane.add(label4, "5,3,f,c");
	    
	    rtcField=ConfigUtils.getNumberTextField();
	    pane.add(rtcField, "7,3,f,c");
	    if(oldData!=null){
	    	rtcField.setText(oldData.getRtcInterval()+"");
	    }else{
	    	rtcField.setText("0");
	    }
	    
	    JLabel label5=new JLabel("完整性检测");
	    pane.add(label5, "1,5,f,c");
	    
	    completeCheckBox=new JComboBox<String>();
	    completeCheckBox.addItem("不使能");
	    completeCheckBox.addItem("使能");
	    pane.add(completeCheckBox, "3,5,f,c");
	    if(oldData!=null){
	    	completeCheckBox.setSelectedIndex(oldData.getCompleteCheck());
	    }else{
	    	completeCheckBox.setSelectedIndex(0);
	    }
	    
	    return pane;
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
                	NodeVL newVL=getData();
                	if(newVL==null){
                		return;
                	}
                	
                	try{
	                	if(isTx){
	                		tablePanel.addOrUpdateTxNodeVL(newVL, oldData); 
	                	}else{
	                		tablePanel.addOrUpdateRxNodeVL(newVL, oldData);
	                	}
                	}catch(Exception ex){
                		JOptionPane.showMessageDialog(CreateNodeVLDialog.this, "输入格式有误:"+ex.getMessage());
                		return;
                	}
                	
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
	
	private void switchTypeSelection(){
		if(isTx){
			// send, tx
			switch(typeBox.getSelectedIndex()){
			case 0: // be
				this.rcPane.setEnabled(false);
				this.bagLabel.setEnabled(false);
				this.bagTxtField.setEnabled(false);
				this.jitterLabel.setEnabled(false);
				this.jitterTxtField.setEnabled(false);
				this.ttPane.setEnabled(false);
				this.intervalLabel.setEnabled(false);
				this.intervalTxtField.setEnabled(false);
				this.windowLabel.setEnabled(false);
				this.windowTxtField.setEnabled(false);
				break;
			case 1: // rc
				this.rcPane.setEnabled(true);
				this.bagLabel.setEnabled(true);
				this.bagTxtField.setEnabled(true);
				this.jitterLabel.setEnabled(true);
				this.jitterTxtField.setEnabled(true);
				this.ttPane.setEnabled(false);
				this.intervalLabel.setEnabled(false);
				this.intervalTxtField.setEnabled(false);
				this.windowLabel.setEnabled(false);
				this.windowTxtField.setEnabled(false);
				break;
			case 2: // tt
				this.rcPane.setEnabled(false);
				this.bagLabel.setEnabled(false);
				this.bagTxtField.setEnabled(false);
				this.jitterLabel.setEnabled(false);
				this.jitterTxtField.setEnabled(false);
				this.ttPane.setEnabled(true);
				this.intervalLabel.setEnabled(true);
				this.intervalTxtField.setEnabled(true);
				this.windowLabel.setEnabled(true);
				this.windowTxtField.setEnabled(true);
				break;
			}
		}else{
			// receive, rx
			this.rcPane.setEnabled(false);
			this.bagLabel.setEnabled(false);
			this.bagTxtField.setEnabled(false);
			this.jitterLabel.setEnabled(false);
			this.jitterTxtField.setEnabled(false);
			this.ttPane.setEnabled(false);
			this.intervalLabel.setEnabled(false);
			this.intervalTxtField.setEnabled(false);
			this.windowLabel.setEnabled(false);
			this.windowTxtField.setEnabled(false);
		}
	}
	
	public NodeVL getData(){
		String nodeName=nodeBox.getSelectedItem().toString();
		NodeVL vl=null;
		try{
			String vlIDStr=vlIDTxtField.getText().trim();
			if(vlIDStr.length()==0){
				JOptionPane.showMessageDialog(this, "没有输入VL_ID");
				return null;
			}
			
			vl=new NodeVL(nodeName, Integer.valueOf(vlIDStr));
			
			vl.setType(typeBox.getSelectedIndex()+1);
			vl.setBag(Integer.valueOf(bagTxtField.getText().trim()));
			vl.setJitter(Integer.valueOf(jitterTxtField.getText().trim()));
			vl.setTtInterval(Integer.valueOf(intervalTxtField.getText().trim()));
			vl.setTtWindow(Integer.valueOf(windowTxtField.getText().trim()));
			vl.setNetworkType(this.networkBox.getSelectedIndex()+1);
			vl.setUseOfLink(this.linkBox.getSelectedIndex());
			vl.setRedudanceType(this.redudanceBox.getSelectedIndex());
			vl.setRtcInterval(Integer.valueOf(this.rtcField.getText()));
			vl.setCompleteCheck(completeCheckBox.getSelectedIndex());
		}catch(Exception ex){
			JOptionPane.showMessageDialog(this, "输入格式有误:"+ex.getMessage());
			return null;
		}
		return vl;
	}
	
}
