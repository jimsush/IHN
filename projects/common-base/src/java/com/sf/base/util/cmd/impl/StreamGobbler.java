/*
 * $Id: StreamGobbler.java, 2015-9-17 下午03:40:51 sufeng Exp $
 * 
 * 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.util.cmd.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.sf.base.util.cmd.CmdStreamListener;

/**
 * <p>
 * Title: StreamGobbler
 * </p>
 * <p>
 * Description: 输出流处理
 * </p>
 * 
 * @author sufeng
 * created 2015-9-17 下午03:43:19
 * modified [who date description]
 * check [who date description]
 */
public class StreamGobbler extends Thread{
    
    private CmdStreamListener listener;
    
    InputStream is;
    String type;  //输出流的类型ERROR或OUTPUT
    
    public StreamGobbler(InputStream is, String type, CmdStreamListener listener){
        this.is = is;
        this.type = type;
        this.listener=listener;
    }
    
    public void run(){
        try{
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null){
                System.out.println(type + ">" + line);
                if(listener!=null){
                    listener.outLine(type,line);
                }
                System.out.flush();
            }
        } catch (IOException ioe){
            ioe.printStackTrace(); 
            if(listener!=null){
                listener.outException(ioe);
            }
        }
    }
    
} 

