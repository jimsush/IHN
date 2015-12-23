/*
 * $Id: SessionMessage.java, 2015-2-28 下午05:00:46 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.session.def;

import java.io.Serializable;

import com.sf.core.container.def.RemoteCommunicateObject;
import com.sf.core.message.def.BaseMessage;

/**
 * <p>
 * Title: SessionMessage
 * </p>
 * <p>
 * Description: 会话变化的消息：连接、断连
 * messageSource内可以是Session或RemoteCommunicateObject
 * </p>
 * 
 * @author sufeng
 * created 2015-2-28 下午05:00:46
 * modified [who date description]
 * check [who date description]
 */
public class SessionMessage extends BaseMessage{

    private static final long serialVersionUID = 4763627922328244816L;
    
    public static final String NAME="session.msg";
    
    /**
     * 是否是服务子系统的session方，true表示服务子系统，是session提供者，false表示为客户子系统，是session消费者
     */
    private boolean sessionProvider;
    
    /**
     * 附加信息
     */
    private String additionalInfo;

    public SessionMessage(Session session) {
        super(NAME, session);
        this.sessionProvider=true;
    }
    
    public SessionMessage(RemoteCommunicateObject remote) {
        super(NAME, (remote instanceof Serializable) ? (Serializable)remote : null);
        this.sessionProvider=false;
    }

    /**
     * 消息体内包含的session
     * @return
     */
    public Session getSession(){
        if(sessionProvider)
            return (Session)getMessageSource();
        //System.out.println("this message is not a session provider subsystem");
        return null;
    }

    /**
     * 消息体内包含的remote对象
     * @return
     */
    public RemoteCommunicateObject getRemoteCommunicateObject(){
        if(sessionProvider){
            //System.out.println("this message is not a session provider subsystem");
            return null;
        }
        return (RemoteCommunicateObject)getMessageSource();
    }
    
    public String getAdditionalInfo() {
        return additionalInfo;
    }
    
    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

}
