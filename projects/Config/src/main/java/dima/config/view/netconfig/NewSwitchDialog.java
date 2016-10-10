package dima.config.view.netconfig;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dima.config.common.ConfigContext;
import dima.config.common.controls.TableLayout;
import dima.config.common.models.SwitchDevice;
import dima.config.common.services.ConfigDAO;
import dima.config.common.services.ServiceFactory;
import twaver.TWaverUtil;

public class NewSwitchDialog extends JDialog{

	private static final long serialVersionUID = 528482491531889040L;

	private SwitchDevice oldData;
	private UpdateCallback updateCallback;
	private NewSwitchPanel switchPanel;
	
	public NewSwitchDialog(Window parent, String title, SwitchDevice data, UpdateCallback updateCallback){
		super(parent, title, ModalityType.APPLICATION_MODAL);
		setAlwaysOnTop(true);
		
		this.updateCallback=updateCallback;
		this.oldData=data;
		
		initView();
	}
	
	private void initView(){
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(360, 418);
        setResizable(false);
        TWaverUtil.centerWindow(this);
        
        Container mainPane = getContentPane();
        mainPane.setLayout(new BorderLayout());
        
        switchPanel=new NewSwitchPanel(this.oldData);
        mainPane.add(switchPanel, BorderLayout.CENTER);
        
        JPanel buttonPane=createButtonPanel();
        mainPane.add(buttonPane, BorderLayout.SOUTH);
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
                	SwitchDevice data = switchPanel.getData();
                	if(data==null){
                		return;
                	}
                	
                	try{
                		if(updateCallback!=null){
                			if(oldData!=null){
                				updateCallback.addSwitch(data, oldData.getSwitchName());
                			}else{
                				updateCallback.addSwitch(data, null);
                			}
                		}
                		
	                	ConfigDAO dao=ServiceFactory.getService(ConfigDAO.class);
	                	dao.saveSwitchDevice(data, oldData);
                	}catch(Exception ex){
                		JOptionPane.showMessageDialog(NewSwitchDialog.this, "添加交换机失败:"+ex.getMessage());
                		return;
                	}
                	dispose();
                	
                	// open NMU dialog to create a new Network Management Unit for this switch
                	if(oldData==null){
                		NewNodeDialog nmuDialog=new NewNodeDialog(ConfigContext.mainFrame, "添加交换机网络管理单元", null, null, false, data.getSwitchName());
                		nmuDialog.setVisible(true);
                	}
                }else if(source==cancelBtn){
                	dispose();
                }
            }
	    };
	    
	    okBtn.addActionListener(l);
        cancelBtn.addActionListener(l);
        
		return buttonPane;
	}
	
}
