/*
 * $Id: LocalComunicateObjectImpl.java, 2015-9-27 下午04:03:09 aaron Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.container.impl;

import com.sf.core.container.def.CommunicateObject;
import com.sf.core.message.def.MessageReceiver;

/**
 * <p>
 * Title: LocalComunicateObjectImpl
 * </p>
 * <p>
 * Description: 本地通信对象，实现同一JVM内的服务获取，消息订阅
 * </p>
 * 
 * @author aaron
 * created 2015-9-27 下午04:03:09
 * modified [who date description]
 * check [who date description]
 */
public class LocalCommunicateObjectImpl implements CommunicateObject{
    
    private CoreContextImpl coreContext;
    
    LocalCommunicateObjectImpl(CoreContextImpl coreContext){
        this.coreContext=coreContext;
    }
    
    /**
     * @see com.sf.core.container.def.CommunicateObject#getService(java.lang.String, java.lang.Class)
     */
    @Override
    public <T> T getService(String serviceName, Class<T> serviceItf) {
        return coreContext.getLocalService(serviceName, serviceItf);
    }

    /**
     * @see com.sf.core.container.def.CommunicateObject#subscribe(java.lang.String, com.sf.core.message.def.MessageReceiver)
     */
    @Override
    public void subscribe(String name, MessageReceiver receiver) {
        coreContext.messageServiceManager.subscribe(name, receiver);
    }

    /**
     * @see com.sf.core.container.def.CommunicateObject#unsubscribe(java.lang.String, com.sf.core.message.def.MessageReceiver)
     */
    @Override
    public void unsubscribe(String name, MessageReceiver receiver) {
        coreContext.messageServiceManager.unsubscribe(name, receiver);
    }

}
