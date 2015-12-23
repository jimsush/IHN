/*
 * $Id: CmdStreamListener.java, 2015-11-15 ����12:02:52 sufeng Exp $
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
 * Description: ����ִ��������ļ�����(����ִ��cmd������չ)
 * </p>
 * 
 * @author sufeng
 * created 2015-11-15 ����12:02:52
 * modified [who date description]
 * check [who date description]
 */
public interface CmdStreamListener {

    /**
     * ִ�н��һ��һ�е����
     * @param lineMessage
     */
    public void outLine(String type,String lineMessage);
    
    /**
     * ִ�н�������쳣
     * @param ex
     */
    public void outException(Exception ex);
    
    
}
