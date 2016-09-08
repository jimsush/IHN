package dima.config.common.controls;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DevicePortTableCellRenderer extends DefaultTableCellRenderer  {

	private static final long serialVersionUID = 6806767112211966640L;

	public DevicePortTableCellRenderer(){
	}
	
	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(value instanceof Integer){
        	int val=(int)value;
        	if(val==0){
        		setText("");
        	}else{
        		int switchId=(val >> 16) & 0xffff;
        		int portId=val & 0xffff;
        		setText("½»»»»ú:"+switchId+" ¶Ë¿Ú:"+portId);
        	}
        }else if(value==null){
        	setText("");
        }
        return comp;
    }
	
	
}
