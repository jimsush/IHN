/*
 * $Id: RunCmdService.java, 2015-9-17 下午03:40:51 sufeng Exp $
 * 
 * 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.util.cmd.impl;

import java.io.InputStream;

import org.apache.commons.lang.StringUtils;

import com.sf.base.util.cmd.CmdStreamListener;
import com.sf.base.util.cmd.RunService;

/**
 * 在当前独立的进程里面执行外部cmd命令启动service
 * <p>有2种方式:inside thread,outside thread
 * <p>永久不考虑支持windows95/98
 * <p>对linux,solaris..等非windows系统目前没有支持,但将来会扩充
 * @author sufeng
 * @since 
 *
 */
public class RunCmdService implements RunService{
    
	/** start命令行*/
	private String startCmdLine;
	
	/**stop命令行*/
	private String stopCmdLine;
	
	private CmdStreamListener cmdStreamListener;
	
	/**启动cmd的方式*/
	private int startupMode; 
	
	/**
	 * 线程外执行（新开线程执行）
	 */
	public static final int OUTSIDE_THREAD=0; 
	
	/**
	 * 线程内执行（当前线程内） 
	 */
	public static final int INSIDE_THREAD=1;
	
	/**
	 * 操作系统
	 */
	private static String osName ;
	
	/**
	 * 是否是windows
	 */
    private static boolean isWin; 
    
    static{
    	osName= System.getProperty("os.name");
    	//是否windows
    	if(osName.startsWith("Windows") || osName.startsWith("windows"))
    		isWin=true;
    }
	
	public String getStartCmdLine() {
		return startCmdLine;
	}

	/**设置start命令行,比如:ping 192.168.0.1*/
	public void setStartCmdLine(String startCmdLine) {
		this.startCmdLine = startCmdLine;
	}
	
	public void setCmdStreamListener(CmdStreamListener cmdStreamListener) {
        this.cmdStreamListener = cmdStreamListener;
    }
	
	/**
	 * 停止cmd
	 * @return
	 */
	public String getStopCmdLine() {
		return stopCmdLine;
	}

	/**设置stop命令行,比如:stop.bat tomcat*/
	public void setStopCmdLine(String stopCmdLine) {
		this.stopCmdLine = stopCmdLine;
	}

	public int getStartupMode() {
		return startupMode;
	}

	/**
	 * 设置启动cmd的模式:0 当前thread内启动,会阻塞 1 另开一个thread执行,默认为1
	 */
	public void setStartupMode(int startupMode) {
		this.startupMode = startupMode;
	}

	public boolean initService() {
		return true;
	}

	/**
	 * 启动一个cmd或者shell命令
	 * <p>必须先设置startCmdLine
	 */
	public boolean startService() {
		return exeCmd(startCmdLine);
	}

	public boolean stopService() {
		return exeCmd(stopCmdLine);
	}
	
	/**
	 * 执行一个命令行
	 * @param cmd 命令行参数
	 * @return
	 */
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
			//可能会阻塞,取决cmd里面的内容是如何编写的
			return exeProcess(command);
		}else if(startupMode==OUTSIDE_THREAD){
			//out side thread,推荐使用这种方式,这样不会阻塞
			final String cmdLine=command;
			new Thread(){
				public void run(){
					exeProcess(cmdLine);
				}
			}.start();
		}
		
		return true;
	}
	
	/***
	 * 执行cmd并返回InputStream,inside_thread
	 * @param osType 操作系统类别 0 windows, 1 linux
	 * @param cmd
	 * @return 可能为null,如果是linux则返回到重定向到文件的stream
	 */
	public InputStream getOutStreamWithExeCmd(int osType,String cmd){
		if(StringUtils.isEmpty(cmd))
			return null;
		
		String command=null;
		Process proc =null;
		
		if(osType==0){
			// windows下
			command="cmd /c \""+cmd+"\"";
		}else{
			// linux下处理不同
			command="sh "+cmd;
		}	
				
		try{
			proc=Runtime.getRuntime().exec(command);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		// 返回
		if(proc!=null)
			return proc.getInputStream();
		else
			return null;
	}
	
	/**
	 * 调用Runtime 调用外部native命令
	 * @param cmd
	 * @return true/false
	 */
	private boolean exeProcess(String cmd)  {
		try{
		    System.out.println("exec cmd:"+cmd);
			Process proc = Runtime.getRuntime().exec(cmd);
			// any error message
	        StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR", cmdStreamListener);            
	        
	        // any output
	        StreamGobbler outputGobbler = new  StreamGobbler(proc.getInputStream(), "OUTPUT", cmdStreamListener);
	            
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
