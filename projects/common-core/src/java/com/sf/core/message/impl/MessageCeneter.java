/*
 * $Id: MessageCeneter.java, 2015-9-27 下午05:59:19 aaron Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.message.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.sf.core.container.def.CoreContext;
import com.sf.core.message.def.MessageReceiver;

/**
 * <p>
 * Title: MessageCeneter
 * </p>
 * <p>
 * Description: 本地消息（按同步方式进行处理）处理中心
 * </p>
 * 
 * @author aaron
 * created 2015-9-27 下午05:59:19
 * modified [who date description]
 * check [who date description]
 */
public class MessageCeneter {

    private HashMap<String, ArrayList<MessageReceiver>> map = new HashMap<String, ArrayList<MessageReceiver>>();

    /**
     * 发消息
     * @param name 消息名
     * @param msg 消息体
     */
    public void publish(String name, Serializable msg) {
        ArrayList<MessageReceiver> curReceivers = null;
        synchronized (map) {
            ArrayList<MessageReceiver> receivers = map.get(name);
            if (null == receivers)
                return;
            curReceivers = new ArrayList<MessageReceiver>();
            for (int i = 0; i < receivers.size(); i++) {
                curReceivers.add(receivers.get(i));
            }
        }
        for (int i = 0; i < curReceivers.size(); i++) {
            curReceivers.get(i).receive(CoreContext.getInstance().local(), name, msg);
        }
    }

    /**
     * 订阅消息
     * @param name 消息名
     * @param receiver 订阅回调
     */
    public void subscribe(String name, MessageReceiver receiver) {
        if ((null == name) || (null == receiver) || (name.equals("")))
            throw new IllegalArgumentException();
        synchronized (map) {
            ArrayList<MessageReceiver> receivers = map.get(name);
            if (null == receivers) {
                receivers = new ArrayList<MessageReceiver>();
                receivers.add(receiver);
                map.put(name, receivers);
            } else {
                for (int i = 0; i < receivers.size(); i++) {
                    if (receivers.get(i) == receiver)
                        throw new RuntimeException("Message receiver '" + receiver + "' repeat.");
                }
                receivers.add(receiver);
            }
        }
    }

    /**
     * 退订消息
     * @param name 消息名
     * @param receiver 订阅回调
     */
    public void unsubscribe(String name, MessageReceiver receiver) {
        if ((null == name) || (null == receiver) || (name.equals("")))
            throw new IllegalArgumentException();
        synchronized (map) {
            ArrayList<MessageReceiver> receivers = map.get(name);
            if (null != receivers) {
                for (int i = 0; i < receivers.size(); i++) {
                    if (receivers.get(i) == receiver) {
                        receivers.remove(i);
                        if (0 == receivers.size())
                            map.remove(name);
                        return;
                    }
                }
            }
        }
    }
    
}
