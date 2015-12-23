/*
 * $Id: RemoteComunicateObject.java, 2015-9-27 下午04:02:57 aaron Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.container.def;


/**
 * <p>
 * Title: RemoteComunicateObject
 * </p>
 * <p>
 * Description: 远程通信对象
 * </p>
 * 
 * @author aaron
 * created 2015-9-27 下午04:02:57
 * modified [who date description]
 * check [who date description]
 * @see CommunicateObject
 * @see RemoteCommunicate4Container
 */
public interface RemoteCommunicateObject extends CommunicateObject {

    /**
     * 结束与远端子系统的交互
     */
    public void cleanup();
    
    /**
     * 远端服务器IP
     * @return ip地址
     */
    public String getRemoteIp();
    
    /**
     * 远程服务器的port
     * @return
     */
    public int getRemoteServerPort();
    
    /**
     * 重置远端子系统的ip,port
     * @param ip
     * @param port
     */
    public void resetRemoteServer(String ip,int port);
    
    /**
     * 与远端子系统的连接状态
     * @return
     */
    public int getLinkStatus();
    
}
