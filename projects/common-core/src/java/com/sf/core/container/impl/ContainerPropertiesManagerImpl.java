/*
 * $Id: ContainerPropertiesManagerImpl.java, 2015-10-11 ÏÂÎç12:35:52 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.container.impl;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.sf.core.container.def.ContainerPropertiesManager;

/**
 * <p>
 * Title: ContainerPropertiesManagerImpl
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author sufeng
 * created 2015-10-11 ÏÂÎç12:35:52
 * modified [who date description]
 * check [who date description]
 */
public class ContainerPropertiesManagerImpl implements ContainerPropertiesManager {

    private Map<String,String> props=new ConcurrentHashMap<String, String>();
    
    @Override
    public String get(String key) {
        return props.get(key);
    }

    @Override
    public void put(String key, String value) {
        props.put(key, value);
    }
    
    @Override
    public Properties getProps() {
        Properties ret=new Properties();
        for(Map.Entry<String,String> entry : props.entrySet()){
            ret.put(entry.getKey(),entry.getValue());
        }
        return ret;
    }


}
