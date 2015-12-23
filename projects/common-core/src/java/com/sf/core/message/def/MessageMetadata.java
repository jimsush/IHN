/*
 * $Id: MessageMetadata.java, 2015-1-27 ����01:49:33 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.message.def;

import java.io.Serializable;

/**
 * <p>
 * Title: MessageMetadata
 * </p>
 * <p>
 * Description: ��Ϣ��������������Ϣ��������Ϣ����˿ںš�topic name��
 * </p>
 * 
 * @author sufeng
 * created 2015-1-27 ����01:49:33
 * modified [who date description]
 * check [who date description]
 */
public class MessageMetadata implements Serializable{

    private static final long serialVersionUID = -4377814883012923201L;
    
    /**
     * JMS��Ϣ������IP
     */
    private String serverIp;
    
    /**
     * JMS��Ϣ����Port
     */
    private int port;
    
    /**
     * ���ַ���˿�
     */
    private int namingPort;
    
    /**
     * ��ϵͳ�ṩ��topic
     */
    private String topicName;
    
    /**
     * ��JMS��NamingFactory initial����
     */
    private String initial;
    
    public MessageMetadata(){
    }
    
    public void copy(MessageMetadata metadata){
        this.port=metadata.port;
        this.namingPort=metadata.namingPort;
        this.topicName=metadata.topicName;
        this.serverIp=metadata.serverIp;
        this.initial=metadata.initial;
    }
    
    public String getServerIp() {
        return serverIp;
    }
    
    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
    
    public int getPort() {
        return port;
    }
    
    public void setPort(int port) {
        this.port = port;
    }
    
    public int getNamingPort() {
        return namingPort;
    }
    
    public void setNamingPort(int namingPort) {
        this.namingPort = namingPort;
    }
    
    public String getTopicName() {
        return topicName;
    }
    
    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
    
    public String getInitial() {
        return initial;
    }
    
    public void setInitial(String initial) {
        this.initial = initial;
    }
    
}
