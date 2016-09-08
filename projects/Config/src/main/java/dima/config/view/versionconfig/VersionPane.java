package dima.config.view.versionconfig;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dima.config.common.ConfigContext;
import dima.config.common.ConfigUtils;
import dima.config.common.controls.TableLayout;
import dima.config.common.models.NodeDevice;
import dima.config.common.models.SwitchDevice;
import dima.config.common.models.SwitchMonitor;
import dima.config.common.services.ConfigDAO;
import dima.config.common.services.ServiceFactory;

public class VersionPane extends JPanel{

	private static final long serialVersionUID = -4950859739959600444L;

	private ConfigDAO dao;
	private JTextField versionField;
	private JTextField dateField;
	private JTextField fileNoField;
	
	public VersionPane(){
		initService();
		initUI();
	}
	
	private void initUI(){
		this.setLayout(new BorderLayout());
		
		JPanel topPane = createInputPanel();
		JPanel buttonPane = createButtonPanel();
		this.add(topPane, BorderLayout.NORTH);
		this.add(buttonPane, BorderLayout.CENTER);
	}
	
	private void initService(){
		dao=ServiceFactory.getService(ConfigDAO.class);
		System.out.println(dao.getClass().getSimpleName());
	}
	
	private JPanel createInputPanel(){
		LayoutManager layout =  new TableLayout(new double[][] {
	        { 10,  180, 20, 180, 10 }, 
	        ConfigUtils.getTableLayoutRowParam(3, 4) });
		JPanel pane=new JPanel(layout);
		pane.setBorder(BorderFactory.createTitledBorder("版本信息"));
	    
		JLabel label1=new JLabel("配置文件版本号(16进制)");
		pane.add(label1,"1,1,f,0");
		
		versionField=ConfigUtils.getNumberTextField(8);
		pane.add(versionField,"3,1,f,0");
		
		JLabel label2=new JLabel("配置文件生成日期(16进制)");
		pane.add(label2,"1,3,f,0");
		
		dateField=ConfigUtils.getNumberTextField(8);
		pane.add(dateField,"3,3,f,0");
		
		JLabel label3=new JLabel("配置文件编号");
		pane.add(label3,"1,5,f,0");
		
		fileNoField=ConfigUtils.getNumberTextField(8);
		pane.add(fileNoField,"3,5,f,0");
		
		List<SwitchDevice> switches = dao.readAllSwitchDevices(true);
		if(switches!=null && switches.size()>0){
			SwitchDevice sw = switches.get(0);
			versionField.setText(Integer.toHexString(sw.getVersion()));
			dateField.setText(Integer.toHexString(sw.getDate()));
			fileNoField.setText(sw.getFileNo()+"");
		}
	
		return pane;
	}
	
	private JPanel createButtonPanel(){
		LayoutManager layout =  new TableLayout(new double[][] {
            { 80,  120, 80 }, { 4, 22, TableLayout.FILL, 4}});
	    JPanel buttonPane = new JPanel(layout);
	    final JButton okBtn = new JButton("更新版本信息");
	    
	    buttonPane.add(okBtn,"1,1,f,0");
	    
	    ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Object source = e.getSource();
                if (source == okBtn) {
                	try{
                		changeVersion();
                	}catch(Exception ex){
                		JOptionPane.showMessageDialog(VersionPane.this, "修改版本信息出错: "+ex.getMessage());
                	}
                }
            }
	    };
	    
	    okBtn.addActionListener(l);
        
		return buttonPane;
	}
	
	private void changeVersion(){
		String verStr=versionField.getText().trim();
    	if(verStr.length()<=0){
    		JOptionPane.showMessageDialog(VersionPane.this, "配置文件版本不能为空");
    		return;
    	}
    	String dateStr=dateField.getText().trim();
    	if(dateStr.length()<=0){
    		JOptionPane.showMessageDialog(VersionPane.this, "配置文件生成日期不能为空");
    		return;
    	}
    	String fileNoStr=fileNoField.getText().trim();
    	if(fileNoStr.length()<=0){
    		JOptionPane.showMessageDialog(VersionPane.this, "配置文件编号不能为空");
    		return;
    	}
    	
    	int ver=0;
    	int date=0;
    	int fileNo=0;
    	try{
	    	ver=Integer.valueOf(verStr, 16);
	    	date=Integer.valueOf(dateStr, 16);
	    	fileNo=Integer.valueOf(fileNoStr);
    	}catch(Exception ex){
    		JOptionPane.showMessageDialog(VersionPane.this, "输入数值格式不正确，"+ex.getMessage());
    		return;
    	}
    	
    	ConfigContext.version=ver;
    	ConfigContext.date=date;    
    	ConfigContext.fileNo=fileNo;
    	
    	List<SwitchDevice> switches = dao.readAllSwitchDevices(true);
    	for(SwitchDevice sw : switches){
    		sw.setVersion(ver);
    		sw.setDate(date);
    		sw.setFileNo(fileNo);
    		dao.saveSwitchDevice(sw, null);
    		
    		SwitchMonitor mon = dao.readSwitchMonitor(sw.getSwitchName());
    		if(mon!=null){
    			mon.setVersion(ver);
    			mon.setDate(date);
    			mon.setFileNo(fileNo);
    			dao.saveSwitchMonitor(mon);
    		}
    	}
    	
    	List<NodeDevice> nodes = dao.readAllNodeDevices(true);
    	for(NodeDevice node : nodes){
    		node.setVersion(ver);
    		node.setDate(date);
    		node.setFileNo(fileNo);
    		dao.saveNodeDevice(node, null);
    	}
	}
	
}
