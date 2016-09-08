package dima.config.common;

import java.awt.Dimension;
import java.awt.Window;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.swing.JTextField;

import dima.config.common.controls.NumberDocument;
import dima.config.common.controls.TableLayout;
import dima.config.common.models.SwitchDevice;
import dima.config.common.services.ConfigDAO;
import dima.config.common.services.ServiceFactory;
import twaver.Element;
import twaver.TUIManager;
import twaver.TWaverConst;

public class ConfigUtils {

	private final static String PATH_RESOURCE="dima/config";
	
	public static final int TYPE_NODE=0;
	public static final int TYPE_NMU=1;
	public static final int TYPE_SW=2;
	
	public static final String SYS_PROP_FILE="sys.properties";
	public static final String PROP_KEY_REDUNDANCY="redundancy";
	
	public static final String TYPE_STR_SW="switch";
	public static final String TYPE_STR_NODE="node";
	public static final String TYPE_STR_NMU="nmu";
	
	public static final String PROP_LINK_ADJUST_RATIO="mylink.adjust.ratio";
	
	public static final String[] SHADOW_SCALES={"s","m","l"};
	
	public static String buildPortId(Object deviceId, Object portNo){
		return deviceId.toString()+"_port"+portNo;
	}
	
	@SuppressWarnings("rawtypes")
	public static Object getUserObjectProp(Element element, String key){
		if(element==null){
			return null;
		}
		Object userObj = element.getUserObject();
		if(userObj instanceof Map){
			Map map=(Map)userObj;
			Object val = map.get(key);
			return val;
		}else{
			return userObj;
		}
	}
	
	/**
	 * current configuration table ID
	 * @param element
	 * @return configuration table id
	 */
	public static int getCurrentCfgTableId(Element ele){
		if(ele==null || ele.getParent()==null){
			return 0;
		}
		
		String cfgTableIdStr=ele.getID().toString().substring(4);
		int cfgTableId=Integer.valueOf(cfgTableIdStr);
		return cfgTableId;
	}
	
	public static String getCfgTableTwaverId(int cfgTableId){
		return "CFG_"+cfgTableId;
	}
	
	public static String makeNodeTwaverID(String nodeName, int type){
		return nodeName+"_"+type;
	}
	
	public static String getNodeNameFromTwaverID(String twaverId){
		int pos=twaverId.indexOf("_");
		return twaverId.substring(0, pos);
	}
	
	public static String getSwitchConfigFileName(String switchName){
		return "sw_"+switchName+"_config.bin";
	}
	
	public static String getNMUConfigFileName(String switchName){
		return "sw_"+switchName+"_nmu.bin";
	}
	
	public static String getMonitorConfigFileName(String switchName){
		return "sw_"+switchName+"_mon.bin";
	}
	
	public static String getNodeConfigFileName(String portNo){
		return "fic_"+portNo+"_config.bin";
	}
	
	public static String getSwitchName(String fileName){
		int pos=fileName.lastIndexOf("_");
		return fileName.substring(3, pos);
	}
	
	public static String getPortNo(String fileName){
		int pos=fileName.lastIndexOf("_");
		return fileName.substring(4, pos);
	}
	
	public static String getPortName(int switchId, int portId){
		int id=portId+(switchId<<16);
		String str=Integer.toHexString(id);
		return "0x"+str;
	}
	
	public static String[] getSwitchNamePortId(int portNo){
		int portId=portNo & 0xffff;
		int switchId=(portNo>>16) & 0xffff;
		
		ConfigDAO dao = ServiceFactory.getService(ConfigDAO.class);
		List<SwitchDevice> sws = dao.readAllSwitchDevices(true);
		for(SwitchDevice sw : sws){
			if(sw.getLocalDomainID()==switchId){
				return new String[]{ sw.getSwitchName(), portId+"" };
			}
		}
		
		return new String[]{null, portId+""};
	}
	
	public static void initAttachment(){
		int[] ports={16, 24, 32, 48};
		int dup=2;
		for(int portN=0; portN<ports.length; portN++){
			// put on right
			TUIManager.registerAttachment("shadow_"+dup+"_"+ports[portN]+"-l-r", getImageURLString("shadow_"+dup+"_"+ports[portN]+".png"), TWaverConst.POSITION_INNER_BOTTOMRIGHT, 5*(dup-1), -8);
			TUIManager.registerAttachment("shadow_"+dup+"_"+ports[portN]+"-m-r", getImageURLString("shadow_"+dup+"_"+ports[portN]+"-m.png"), TWaverConst.POSITION_INNER_BOTTOMRIGHT, 5*(dup-1), -8);
			TUIManager.registerAttachment("shadow_"+dup+"_"+ports[portN]+"-s-r", getImageURLString("shadow_"+dup+"_"+ports[portN]+"-s.png"), TWaverConst.POSITION_INNER_BOTTOMRIGHT, 5*(dup-1), -8);
				
			// put on left
			TUIManager.registerAttachment("shadow_"+dup+"_"+ports[portN]+"-l-l", getImageURLString("shadow_"+dup+"_"+ports[portN]+".png"), TWaverConst.POSITION_INNER_BOTTOMLEFT, -5*(dup-1), -8);
			TUIManager.registerAttachment("shadow_"+dup+"_"+ports[portN]+"-m-l", getImageURLString("shadow_"+dup+"_"+ports[portN]+"-m.png"), TWaverConst.POSITION_INNER_BOTTOMLEFT, -5*(dup-1), -8);
			TUIManager.registerAttachment("shadow_"+dup+"_"+ports[portN]+"-s-l", getImageURLString("shadow_"+dup+"_"+ports[portN]+"-s.png"), TWaverConst.POSITION_INNER_BOTTOMLEFT, -5*(dup-1), -8);
		}
	}
	
	public static String buildShadowId(String dupNumber, int portNumber, int scale, boolean left){
		String leftName= (left?"l":"r");
		return "shadow_"+dupNumber+"_"+portNumber+"-"+SHADOW_SCALES[scale%3]+"-"+leftName;
	}
	
	public static String getNewShadowId(String tag, String existingShadowId){
		if(existingShadowId==null || existingShadowId.length()<=12)
			return null;
		
		//"shadow_"+dup+"_"+ports[portN]+"-s-l"
		//shadow_2_12-s-l
		return existingShadowId.substring(0,12)+tag+existingShadowId.substring(13);
	}

	public static URL getImageURL(String imageName){
		URL ipmImage = Thread.currentThread().getContextClassLoader().getResource(PATH_RESOURCE+"/resource2/"+imageName);	
		return ipmImage;
	}
	
	public static String getImageURLString(String imageName){
		URL ipmImage = Thread.currentThread().getContextClassLoader().getResource(PATH_RESOURCE+"/resource2/"+imageName);	
		return ipmImage.toString();
	}
	
	public synchronized static void centerWindow(Window window) {
        Dimension screen = window.getToolkit().getScreenSize();
        window.setLocation((int) (screen.getWidth() - window.getWidth()) / 2,
                (int) (screen.getHeight() - window.getHeight()) / 2);
    }
	
	public static double[] getTableLayoutRowParam(int rowsCount, int topGap) {
        return getTableLayoutRowParam(rowsCount, 6, TableLayout.FILL);
    }

    public static double[] getTableLayoutRowParam(int rowsCount, int topGap, double bottomGap) {
        int size = rowsCount * 2 + 1;
        double[] rows = new double[size];
        rows[0] = topGap;
        for (int i = 1; i < size; i++) {
            if (i % 2 == 1) {
                rows[i] = 22;
            } else {
                rows[i] = 4;
            }
        }
        int length = rows.length;
        rows[length - 1] = bottomGap;
        return rows;
    }
    
    public static JTextField getNumberTextField(){
    	return getNumberTextField(8);
    }
    
    public static JTextField getNumberTextField(int maxLen){
    	JTextField txtField=new JTextField(maxLen);
    	txtField.setDocument(new NumberDocument(maxLen));
    	return txtField;
    }
    
    public static NumberResult<? extends Number> checkRequiredNumberField(String fieldName, JTextField field, Class<?> type){
    	String input=field.getText().trim();
    	if(input.length()==0){
    		return new NumberResult<Integer>(false, 0, fieldName+"为空");
    	}
    	
    	if(type.equals(Integer.class)){
    		int number=0;
    		try{
        		number=Integer.valueOf(input);
        	}catch(Exception ex){
        		return new NumberResult<Integer>(false,0,fieldName+"不是整数");
        	}
    		return new NumberResult<Integer>(true, number, null);
    	}else if(type.equals(Double.class)){
    		double number=0;
    		try{
        		number=Double.valueOf(input);
        	}catch(Exception ex){
        		return new NumberResult<Double>(false, 0.0d, fieldName+"不是数值");
        	}
    		return new NumberResult<Double>(true, number, null);
    	}else if(type.equals(Float.class)){
    		float number=0;
    		try{
        		number=Float.valueOf(input);
        	}catch(Exception ex){
        		return new NumberResult<Float>(false, 0.0f, fieldName+"不是数值");
        	}
    		return new NumberResult<Float>(true, number, null);
    	}else if(type.equals(Short.class)){
    		short number=0;
    		try{
        		number=Short.valueOf(input);
        	}catch(Exception ex){
        		return new NumberResult<Short>(false, (short)0, fieldName+"不是数值");
        	}
    		return new NumberResult<Short>(true, number, null);
    	}
    	
    	return new NumberResult<Integer>(false, 0, fieldName+"不明数据格式");
    }
    
    public static void main(String[] args){
		//String fullName=getSwitchConfigFileName("abc");
		//String switchName=getSwitchName(fullName);
		//System.out.println(switchName);
		String str=getPortName(1,2);
		System.out.println(str);
		System.out.println(Integer.valueOf(str.substring(2), 16));
	}
	
}
