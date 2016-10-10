package dima.config.view.versionconfig;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import dima.config.common.controls.TableLayout;
import dima.config.common.models.NodeDevice;
import dima.config.common.models.SwitchDevice;
import dima.config.common.services.ConfigDAO;
import dima.config.common.services.ServiceFactory;
import twaver.ElementAttribute;
import twaver.TDataBox;
import twaver.TWaverUtil;
import twaver.table.TElementTable;

public class MergeObjectsDialog extends JDialog{

	private static final long serialVersionUID = -4146472387573302916L;

	private ConfigDAO dao;
	private TDataBox devicesBox;
	private TElementTable devicesTable;
	private int code;

	public MergeObjectsDialog(Window parent, String title, int code){
		super(parent, title, ModalityType.APPLICATION_MODAL);
		setAlwaysOnTop(true);
		
		this.code=code;
		
		initService();
		initUI();
	}

	private void initUI(){
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(500, 200); 
        setResizable(false);
        TWaverUtil.centerWindow(this);
		
		setLayout(new BorderLayout());
		
		initTable();
		
		JScrollPane deviceListPane=new JScrollPane(devicesTable);
		deviceListPane.setBorder(BorderFactory.createTitledBorder("可以被合并的设备列表")); 
		deviceListPane.setPreferredSize(new Dimension(180, 500));
		this.add(deviceListPane, BorderLayout.CENTER);

		JPanel btnPane=createButtonPanel();
		this.add(btnPane, BorderLayout.SOUTH);
	}

	private void initService() {
		dao=ServiceFactory.getService(ConfigDAO.class);
	}
	
	private JPanel createButtonPanel(){
		LayoutManager layout =  new TableLayout(new double[][] {
            { 80,  80, TableLayout.FILL, 80, 80 }, { 4, 22, TableLayout.FILL, 4}});
	    JPanel buttonPane = new JPanel(layout);
	    final JButton okBtn = new JButton("合并");
	    final JButton cancelBtn = new JButton("取消");
	    
	    buttonPane.add(okBtn,"1,1,f,0");
	    buttonPane.add(cancelBtn,"3,1,f,0");
	    
	    ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source == okBtn) {
                	List<?> selectedElements = devicesBox.getSelectionModel().getAllSelectedElement();
                	if(selectedElements==null || selectedElements.size()<=1){
                		JOptionPane.showMessageDialog(MergeObjectsDialog.this, "至少选择2个设备进行配置文件合并! ");
                		return;
                	}
                	JOptionPane.showMessageDialog(MergeObjectsDialog.this, "合并Bin配置文件完成! ");
                	
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

	private void initTable(){
		devicesBox=new TDataBox();
		devicesTable=tableInit(devicesBox);
		
		if(code==0 || code==2){
			List<SwitchDevice> switches = dao.readAllSwitchDevices(true);
			for(SwitchDevice sw : switches){
				SingleColumnNode<String> element=new SingleColumnNode<String>(sw.getSwitchName());
				devicesBox.addElement(element);
			}
		}else if(code==1){
			List<NodeDevice> nodes = dao.readAllNodeDevices(true);
			for(NodeDevice nd : nodes){
				SingleColumnNode<String> element=new SingleColumnNode<String>(nd.getNodeName());
				devicesBox.addElement(element);
			}
		}
	}
	
	private TElementTable tableInit(TDataBox box){
		TElementTable table = new TElementTable(box);
		
		table.setEditable(false);
		table.setRowHeight(24);
		table.setAutoResizeMode(1);
		table.setTableHeaderPopupMenuFactory(null);
		table.setTableBodyPopupMenuFactory(null);
		
		table.setElementClass(SingleColumnNode.class);
		
		List<ElementAttribute> attributes = new ArrayList<ElementAttribute>();
		
		ElementAttribute attribute = new ElementAttribute();
		attribute.setName("value");
		attribute.setDisplayName("设备名称");
		attributes.add(attribute);
		
		table.registerElementClassAttributes(SingleColumnNode.class, attributes);
		return table;
	}
	
}
