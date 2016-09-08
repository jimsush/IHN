package dima.config.view;

public interface ConfigTableCallback {
	
	public void addOrUpdateConfigTable(boolean isAdd, int cfgTableId);

	public void deleteConfigTable(int cfgTableId);
	
}
