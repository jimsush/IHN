package dima.config.common.controls;

import java.awt.Component;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class EnumTableCellRenderer extends DefaultTableCellRenderer  {

	private static final long serialVersionUID = 6806767112211966640L;

	private Map<Object, String> mapping;
	public EnumTableCellRenderer(Map<Object, String> metadata){
		this.mapping=metadata;
	}
	
	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        String renderString = mapping.get(value);
        if (renderString != null)
            setText(renderString);
        else
            setText("");
        return comp;
    }
	
	
}
