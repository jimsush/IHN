package dima.config.common.controls;

import java.awt.Component;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ListDataTableCellRenderer extends DefaultTableCellRenderer  {

	private static final long serialVersionUID = 6806767112211966640L;

	@SuppressWarnings("rawtypes")
	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(value==null){
        	setText("");
        }else{
        	List list=(List)value;
        	if(list.size()==0){
        		setText("");
        	}else{
        		StringBuilder sb=new StringBuilder();
        		int i=0;
        		for(Object obj : list){
        			if(i==0){
        				sb.append(obj);
        			}else{
        				sb.append(",").append(obj);
        			}
        			i++;
        		}
        		setText(sb.toString());
        	}
        }
        return comp;
    }
	
	
}
