/*
 * $Id: SnmpProtocalLoginParam.java, 2015-11-12 上午11:40:47 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.util.net.snmp.model;

import java.io.Serializable;

/**
 * <p>
 * Title: SnmpProtocalLoginParam
 * </p>
 * <p>
 * SNMP协议登陆参数
 * </p>
 * 
 * @author pl
 * created 2015-11-16 上午11:00:52
 * modified [who date description]
 * check [who date description]
 */
public class SnmpProtocalLoginParam implements Serializable {
    
    private static final long serialVersionUID = 4009937448615952967L;
    
    /** Snmp 版本信息 */
    private int snmpVer = 1;//1为V2C 3为V3 0为V1
    /** Snmp端口 */
    private Integer snmpPort = 161;
    /** 超时时间 单位秒 */
    private Integer timeout = 10;
    /** 重试次数 */
    private Integer reties = 0;
    /** 读共同体 */
    private String readCommunity = "public";
    /** 写共同体 */
    private String writeCommunity = "private";
    /** IP地址 */
    private String ip;
    
    public int getSnmpVer() {
        return snmpVer;
    }
    public void setSnmpVer(int snmpVer) {
        this.snmpVer = snmpVer;
    }
    public Integer getSnmpPort() {
        return snmpPort;
    }
    public void setSnmpPort(Integer snmpPort) {
        this.snmpPort = snmpPort;
    }
    public Integer getTimeout() {
        return timeout;
    }
    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
    public Integer getReties() {
        return reties;
    }
    public void setReties(Integer reties) {
        this.reties = reties;
    }
    public String getReadCommunity() {
        return readCommunity;
    }
    public void setReadCommunity(String readCommunity) {
        this.readCommunity = readCommunity;
    }
    public String getWriteCommunity() {
        return writeCommunity;
    }
    public void setWriteCommunity(String writeCommunity) {
        this.writeCommunity = writeCommunity;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    
}
