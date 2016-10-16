package dima.config.view.versionconfig;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import dima.config.common.controls.TableLayout;
import dima.config.common.services.ConfigDAO;
import dima.config.common.services.ServiceFactory;
import dima.config.dao.BinFileHandler;
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
            @SuppressWarnings("unchecked")
			@Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source == okBtn) {
                	List<?> selectedElements = devicesBox.getSelectionModel().getAllSelectedElement();
                	if(selectedElements==null || selectedElements.size()<=1){
                		JOptionPane.showMessageDialog(MergeObjectsDialog.this, "至少选择2个设备进行配置文件合并! ");
                		return;
                	}
                	
                	List<String> binFiles=new ArrayList<>();
                	selectedElements.stream().forEach(ele -> binFiles.add(((TwoColumnNode<String,String>)ele).getValue()));
                	try {
	                	switch(code){
	                	case 0:
	                		BinFileHandler.mergeSwitchFiles(binFiles);
	                		break;
	                	case 1:
	                		BinFileHandler.mergeNodeFiles(binFiles);
	                		break;
	                	case 2:
	                		BinFileHandler.mergeSwitchMonitorFiles(binFiles);
	                		break;
	                	default:
	                		break;
	                	}
                	} catch (Exception e1) {
						JOptionPane.showMessageDialog(MergeObjectsDialog.this, "合并Bin配置文件异常， "+e1.getMessage());
						return;
					}
            		
                	JOptionPane.showMessageDialog(MergeObjectsDialog.this, "合并Bin配置文件完成! ");
                	
                	// to show the refreshed files and devices
                	devicesBox.clear();
                	Map<String, String> file2Devices = dao.file2Devices(code);
            		file2Devices.forEach((file, dev)->{
            			TwoColumnNode<String, String> element=new TwoColumnNode<>(file, dev);
            			devicesBox.addElement(element);
            		});
            		
                	//dispose();
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
		
		Map<String, String> file2Devices = dao.file2Devices(code);
		file2Devices.forEach((file, dev)->{
			TwoColumnNode<String, String> element=new TwoColumnNode<>(file, dev);
			devicesBox.addElement(element);
		});
	}
	
	private TElementTable tableInit(TDataBox box){
		TElementTable table = new TElementTable(box);
		
		table.setEditable(false);
		table.setRowHeight(24);
		table.setAutoResizeMode(1);
		table.setTableHeaderPopupMenuFactory(null);
		table.setTableBodyPopupMenuFactory(null);
		
		table.setElementClass(TwoColumnNode.class);
		
		List<ElementAttribute> attributes = new ArrayList<ElementAttribute>();
		
		ElementAttribute attribute = new ElementAttribute();
		attribute.setName("value");
		attribute.setDisplayName("bin文件");
		attributes.add(attribute);
		
		attribute = new ElementAttribute();
		attribute.setName("value2");
		attribute.setDisplayName("包含元素");
		attributes.add(attribute);
		
		table.registerElementClassAttributes(TwoColumnNode.class, attributes);
		return table;
	}
	
}
