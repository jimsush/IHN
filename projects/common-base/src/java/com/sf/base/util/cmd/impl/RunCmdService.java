/*
 * $Id: RunCmdService.java, 2015-9-17 ����03:40:51 sufeng Exp $
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
 * �ڵ�ǰ�����Ľ�������ִ���ⲿcmd��������service
 * <p>��2�ַ�ʽ:inside thread,outside thread
 * <p>���ò�����֧��windows95/98
 * <p>��linux,solaris..�ȷ�windowsϵͳĿǰû��֧��,������������
 * @author sufeng
 * @since 
 *
 */
public class RunCmdService implements RunService{
    
	/** start������*/
	private String startCmdLine;
	
	/**stop������*/
	private String stopCmdLine;
	
	private CmdStreamListener cmdStreamListener;
	
	/**����cmd�ķ�ʽ*/
	private int startupMode; 
	
	/**
	 * �߳���ִ�У��¿��߳�ִ�У�
	 */
	public static final int OUTSIDE_THREAD=0; 
	
	/**
	 * �߳���ִ�У���ǰ�߳��ڣ� 
	 */
	public static final int INSIDE_THREAD=1;
	
	/**
	 * ����ϵͳ
	 */
	private static String osName ;
	
	/**
	 * �Ƿ���windows
	 */
    private static boolean isWin; 
    
    static{
    	osName= System.getProperty("os.name");
    	//�Ƿ�windows
    	if(osName.startsWith("Windows") || osName.startsWith("windows"))
    		isWin=true;
    }
	
	public String getStartCmdLine() {
		return startCmdLine;
	}

	/**����start������,����:ping 192.168.0.1*/
	public void setStartCmdLine(String startCmdLine) {
		this.startCmdLine = startCmdLine;
	}
	
	public void setCmdStreamListener(CmdStreamListener cmdStreamListener) {
        this.cmdStreamListener = cmdStreamListener;
    }
	
	/**
	 * ֹͣcmd
	 * @return
	 */
	public String getStopCmdLine() {
		return stopCmdLine;
	}

	/**����stop������,����:stop.bat tomcat*/
	public void setStopCmdLine(String stopCmdLine) {
		this.stopCmdLine = stopCmdLine;
	}

	public int getStartupMode() {
		return startupMode;
	}

	/**
	 * ��������cmd��ģʽ:0 ��ǰthread������,������ 1 ��һ��threadִ��,Ĭ��Ϊ1
	 */
	public void setStartupMode(int startupMode) {
		this.startupMode = startupMode;
	}

	public boolean initService() {
		return true;
	}

	/**
	 * ����һ��cmd����shell����
	 * <p>����������startCmdLine
	 */
	public boolean startService() {
		return exeCmd(startCmdLine);
	}

	public boolean stopService() {
		return exeCmd(stopCmdLine);
	}
	
	/**
	 * ִ��һ��������
	 * @param cmd �����в���
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
	
	/***
	 * ִ��cmd������InputStream,inside_thread
	 * @param osType ����ϵͳ��� 0 windows, 1 linux
	 * @param cmd
	 * @return ����Ϊnull,�����linux�򷵻ص��ض����ļ���stream
	 */
	public InputStream getOutStreamWithExeCmd(int osType,String cmd){
		if(StringUtils.isEmpty(cmd))
			return null;
		
		String command=null;
		Process proc =null;
		
		if(osType==0){
			// windows��
			command="cmd /c \""+cmd+"\"";
		}else{
			// linux�´���ͬ
			command="sh "+cmd;
		}	
				
		try{
			proc=Runtime.getRuntime().exec(command);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		// ����
		if(proc!=null)
			return proc.getInputStream();
		else
			return null;
	}
	
	/**
	 * ����Runtime �����ⲿnative����
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
