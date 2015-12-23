/*
 * $Id: Session.java, 2015-2-20 下午02:02:57  Exp $
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
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;

import com.sf.base.util.format.DateFormatter;

/**
 * <p>
 * Title: Session
 * </p>
 * <p>
 * Description: 会话
 * </p>
 * 
 * @author 
 * created 2015-2-20 下午02:02:57
 * modified [who date description]
 * check [who date description]
 */
public class Session implements Serializable {
    
    private static final long serialVersionUID = -4390385071438174439L;
    
    /** Session 唯一标识 */
    private Long sessionId;
    /**
     * 客户子系统的ip
     */
    private String ip;
    
    /**
     * 服务子系统的ip
     */
    private String serverIp;
    
    /** Session状态 */
    private volatile SessionState sessionState = SessionState.Linkup;
    
    /** 当前Session上次活动时间 */
    private Long lastActivetime;
    
    /** 登录时间 */
    private Long loginTime;
    
    /**
     * 拥有者，可以为null
     */
    private String owner;
    
    /**
     * 附加信息，用于扩展
     */
    private Map<String,Serializable> additionalInfo;

    /**
     * @param sessionId
     * @param owner
     */
    public Session(Long sessionId) {
        super();
        this.sessionId = sessionId;
        this.loginTime=System.currentTimeMillis();
    }
    
    public Session(Long sessionId, String clientIp) {
        this(sessionId);
        this.ip=clientIp;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public SessionState getSessionState() {
        return sessionState;
    }

    public void setSessionState(SessionState sessionState) {
        this.sessionState = sessionState;
    }

    public Long getLastActivetime() {
        return lastActivetime;
    }

    public void setLastActivetime(Long lastActivetime) {
        this.lastActivetime = lastActivetime;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public String getIp() {
        return this.ip;
    }
    
    public Long getLoginTime(){
        return this.loginTime;
    }

    public String getServerIp() {
        return serverIp;
    }
    
    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
    
    /**
     * 
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ObjectUtils.hashCode(sessionId);
        result = prime * result ;
        return result;
    }
    
    /**
     * 
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Session other = (Session) obj;
        return ObjectUtils.equals(this.getSessionId(), other.getSessionId());
    }
    
    public String getOwner() {
        return owner;
    }
    
    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    /**
     * 
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("Session:");
        sb.append(sessionId);
        sb.append("\n");
        sb.append("ip:");
        sb.append(ip);
        sb.append("\n");
        sb.append("State:");
        sb.append(sessionState);
        sb.append("\n");
        sb.append("Last active time:");
        sb.append(DateFormatter.getLongDate(lastActivetime));
        sb.append("\n");
        sb.append("login Time:");
        sb.append(DateFormatter.getLongDate(loginTime));
        sb.append("\n");
        return sb.toString();
    }

    /**
     * 附加信息
     * @param additionalInfo the additionalInfo to set
     */
    public void setAdditionalInfo(Map<String,Serializable> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    /**
     * 附加信息
     * @return the additionalInfo
     */
    public Map<String,Serializable> getAdditionalInfo() {
        return additionalInfo;
    }

    

}
