/*
 * $Id: CmdStreamListener.java, 2015-11-15 下午12:02:52 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.util.cmd;

/**
 * <p>
 * Title: CmdStreamListener
 * </p>
 * <p>
 * Description: 命令执行输出流的监听器(供在执行cmd进行扩展)
 * </p>
 * 
 * @author sufeng
 * created 2015-11-15 下午12:02:52
 * modified [who date description]
 * check [who date description]
 */
public interface CmdStreamListener {

    /**
     * 执行结果一行一行地输出
     * @param lineMessage
     */
    public void outLine(String type,String lineMessage);
    
    /**
     * 执行结果中有异常
     * @param ex
     */
    public void outException(Exception ex);
    
    
}
