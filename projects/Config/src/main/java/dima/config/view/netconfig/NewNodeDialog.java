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

import dima.config.common.controls.TableLayout;
import dima.config.common.models.NodeDevice;
import dima.config.common.services.ConfigDAO;
import dima.config.common.services.ServiceFactory;
import twaver.TWaverUtil;

public class NewNodeDialog extends JDialog{

	private static final long serialVersionUID = 528482491531889040L;

	private UpdateCallback updateCallback;
	private NodeDevice oldData;
	private NewNodeOrNMUPanel nodePanel;
	private boolean isForNode;
	private String switchName;
	
	public NewNodeDialog(Window parent, String title, NodeDevice nodeDevice, UpdateCallback updateCallback, boolean isForNode, String switchName){
		super(parent, title, ModalityType.APPLICATION_MODAL);
		setAlwaysOnTop(true);
		
		this.updateCallback=updateCallback;
		this.oldData=nodeDevice;
		this.isForNode=isForNode;
		this.switchName=switchName;
		initView();
	}
	
	private void initView(){
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		if(isForNode){
			setSize(414, 276);
		}else{
			setSize(414, 236);
		}
        setResizable(false);
        TWaverUtil.centerWindow(this);
        
        Container mainPane = getContentPane();
        mainPane.setLayout(new BorderLayout());
        
        nodePanel=new NewNodeOrNMUPanel(this, isForNode, oldData, switchName);
        mainPane.add(nodePanel, BorderLayout.CENTER);
        
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
                	NodeDevice data = nodePanel.getData();
                	if(data==null)
                		return;
                	
                	NodeDevice updatedData=data;
                	try{
                		if(updateCallback!=null){
                			if(oldData!=null){
                				updatedData=updateCallback.addNode(data, oldData.getNodeName());
                			}else{
                				updatedData=updateCallback.addNode(data, null);
                			}
                		}
                		
	                	ConfigDAO dao=ServiceFactory.getService(ConfigDAO.class);
	                	
	                	dao.saveNodeDevice(updatedData, oldData);
                	}catch(Exception ex){
                		JOptionPane.showMessageDialog(NewNodeDialog.this, "添加失败:"+ex.getMessage());
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
	
}
