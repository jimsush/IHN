package com.ihn.server.util.process;

import java.io.InputStream;

import org.apache.commons.lang.StringUtils;


public class RunCmdService implements RunService{
    
	private String startCmdLine;
	
	private String stopCmdLine;
	
	private int startupMode; 
	public static final int OUTSIDE_THREAD=0; 
	public static final int INSIDE_THREAD=1;
	
	private static String osName ;
    private static boolean isWin; 
    
    static{
    	osName= System.getProperty("os.name");
    	//userDir=System.getProperty("user.dir");
    	//fileSep= File.separator;
    	
    	if(osName.startsWith("Windows") || osName.startsWith("windows"))
    		isWin=true;
    }
	
	public String getStartCmdLine() {
		return startCmdLine;
	}

	public void setStartCmdLine(String startCmdLine) {
		this.startCmdLine = startCmdLine;
	}
	
	public String getStopCmdLine() {
		return stopCmdLine;
	}

	public void setStopCmdLine(String stopCmdLine) {
		this.stopCmdLine = stopCmdLine;
	}

	public int getStartupMode() {
		return startupMode;
	}

	public void setStartupMode(int startupMode) {
		this.startupMode = startupMode;
	}

	public boolean initService() {
		return true;
	}

	public boolean startService() {
		return exeCmd(startCmdLine);
	}

	public boolean stopService() {
		return exeCmd(stopCmdLine);
	}
	
	private boolean exeCmd(String cmd){
		if(cmd==null)
			return false;
		
		String command=null;
		
		if(isWin){
			command="cmd /c \""+cmd+"\"";
		}else{
			// linux,solaris...
			command="sh "+cmd;
		}
		
		if(startupMode==INSIDE_THREAD){
			//���ܻ�����,ȡ��cmd�������������α�д��
			return exeProcess(command);
		}else if(startupMode==OUTSIDE_THREAD){
			//out side thread,�Ƽ�ʹ�����ַ�ʽ,������������
			final String cmdLine=command;
			new Thread(){
				public void run(){
					exeProcess(cmdLine);
				}
			}.start();
		}
		
		return true;
	}
	
	public InputStream getOutStreamWithExeCmd(int osType,String cmd){
		if(StringUtils.isEmpty(cmd))
			return null;
		
		String command=null;
		Process proc =null;
		
		if(osType==0){
			// windows
			command="cmd /c \""+cmd+"\"";
		}else{
			// linux
			command="sh "+cmd;
		}	
				
		try{
			proc=Runtime.getRuntime().exec(command);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(proc!=null)
			return proc.getInputStream();
		else
			return null;
	}
	
	private boolean exeProcess(String cmd)  {
		try{
		    System.out.println("exec cmd:"+cmd);
			Process proc = Runtime.getRuntime().exec(cmd);
			// any error message
	        StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");            
	        
	        // any output
	        StreamGobbler outputGobbler = new  StreamGobbler(proc.getInputStream(), "OUTPUT");
	            
	        // kick them off
	        errorGobbler.start();
	        outputGobbler.start();
	                                
	        // any error
	        int exitVal = proc.waitFor();
	        System.out.println("exeProcess,"+cmd+" exitvalue:" + exitVal); 
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

}
