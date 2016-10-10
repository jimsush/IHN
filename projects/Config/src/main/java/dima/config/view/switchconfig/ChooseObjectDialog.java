package dima.config.view.switchconfig;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import dima.config.common.controls.TableLayout;
import twaver.Element;
import twaver.ElementAttribute;
import twaver.TDataBox;
import twaver.TWaverUtil;
import twaver.table.TElementTable;
import twaver.table.TTableModel;

public class ChooseObjectDialog extends JDialog{

	private static final long serialVersionUID = -4146472387573302916L;
	
	public static final int CODE_IN_PORT=0;
	public static final int CODE_OUT_PORT=1;
	public static final int CODE_VL=2;
	
	private String objectType;
	private List<Integer> allObjects;
	private List<Integer> choosenObjects;
	
	private TDataBox leftBox;
	private TElementTable leftTable;
	private TDataBox rightBox;
	private TElementTable rightTable;
	private CreateMonitorDialog monDialog;
	private int code;

	public ChooseObjectDialog(CreateMonitorDialog parent, String title, String objectType, int code, List<Integer> allObjects, List<Integer> choosenObjects){
		super(parent, title, ModalityType.APPLICATION_MODAL);
		setAlwaysOnTop(true);
		
		this.monDialog=parent;
		this.objectType=objectType;
		this.allObjects=allObjects;
		this.choosenObjects=choosenObjects;
		this.code=code;
		
		initUI();
	}
	
	@SuppressWarnings("unchecked")
	private void initUI(){
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(500, 520); //1100
        setResizable(false);
        TWaverUtil.centerWindow(this);
		
		setLayout(new BorderLayout());
		
		initTable();
		
		JScrollPane leftPane=new JScrollPane(leftTable);
		leftPane.setBorder(BorderFactory.createTitledBorder("未被监控的"+objectType)); 
		leftPane.setPreferredSize(new Dimension(180, 500));
		this.add(leftPane, BorderLayout.WEST);
		
		JScrollPane rightPane=new JScrollPane(rightTable);
		rightPane.setBorder(BorderFactory.createTitledBorder("已被监控的"+objectType));
		rightPane.setPreferredSize(new Dimension(180, 500));
		this.add(rightPane, BorderLayout.EAST);
		
		LayoutManager midLayout =  new TableLayout(new double[][] {
            { 30,  80, 30 }, { 80, 22, TableLayout.FILL, 22, 80}});
		JPanel moveBtnPane=new JPanel(midLayout);
		JButton moveToRightBtn=new JButton(">");
		moveBtnPane.add(moveToRightBtn, "1,1,f,c");
		moveToRightBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<Element> elements = leftBox.getSelectionModel().getAllSelectedElement();
				if(elements!=null){
					for(Element element : elements){
						leftBox.removeElement(element);
						rightBox.addElement(element);
					}
				}
			}
		});
		JButton moveToLeftBtn=new JButton("<");
		moveToLeftBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<Element> elements = rightBox.getSelectionModel().getAllSelectedElement();
				if(elements!=null){
					for(Element element : elements){
						rightBox.removeElement(element);
						leftBox.addElement(element);
					}
				}
			}
		});
		moveBtnPane.add(moveToLeftBtn, "1,3,f,c");
		this.add(moveBtnPane, BorderLayout.CENTER);

		JPanel btnPane=createButtonPanel();
		this.add(btnPane, BorderLayout.SOUTH);
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
                	List<Integer> ports = getData();
                	switch(code){
                	case CODE_IN_PORT:
                		monDialog.choosePortInputPorts(ports);
                		break;
                	case CODE_OUT_PORT:
                		monDialog.choosePortOutputPorts(ports);
                		break;
                	case CODE_VL:
                		monDialog.choosePortVLs(ports);
                		break;
                	default:
                		break;
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
	
	@SuppressWarnings("rawtypes")
	public List<Integer> getData(){
		List<Integer> result=new ArrayList<Integer>();
		List elements = rightBox.getAllElements();
		for(Object obj : elements){
			if(obj instanceof IntegerNode){
				IntegerNode node=(IntegerNode)obj;
				result.add(node.getNumber());
			}
		}
		Collections.sort(result);
		return result;
	}
	
	private void initTable(){
		leftBox=new TDataBox();
		leftTable=tableInit(leftBox);
		
		rightBox=new TDataBox();
		rightTable=tableInit(rightBox);
		int totalRows=allObjects.size();
		int chooseRows=choosenObjects.size();
		int emptyRows=totalRows-chooseRows;
		
		for(Integer num : choosenObjects){
			rightBox.addElement(new IntegerNode(num));
		}
		for(int i=0; i<emptyRows; i++){
			rightBox.addElement(null);
		}
		
		for(Integer num : allObjects){
			if(!choosenObjects.contains(num)){
				leftBox.addElement(new IntegerNode(num));
			}
		}
		for(int i=0; i<chooseRows; i++){
			leftBox.addElement(null);
		}
	}
	
	private TElementTable tableInit(TDataBox box){
		TElementTable table = new TElementTable(box);
		
		table.setEditable(false);
		table.setRowHeight(24);
		table.setAutoResizeMode(1);
		table.setTableHeaderPopupMenuFactory(null);
		table.setTableBodyPopupMenuFactory(null);
		
		table.setElementClass(IntegerNode.class);
		
		List<ElementAttribute> attributes = new ArrayList<ElementAttribute>();
		ElementAttribute attribute = new ElementAttribute();
		attribute.setName("number");
		attribute.setDisplayName(objectType);
		attributes.add(attribute);
		
		table.registerElementClassAttributes(IntegerNode.class, attributes);
		table.getTableModel().sortColumn("number", TTableModel.SORT_ASCENDING);
		return table;
	}
	
}
