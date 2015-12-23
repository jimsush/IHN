/*
 * $Id: ExtendClassPathXmlApplicationContext.java, 2015-12-14 下午12:04:35 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.spring.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <p>
 * Title: ExtendClassPathXmlApplicationContext
 * </p>
 * <p>
 * Description:支持设置thread context class loader的application context
 * </p>
 * 
 * @author sufeng
 * created 2015-12-14 下午12:04:35
 * modified [who date description]
 * check [who date description]
 */
public class ExtendClassPathXmlApplicationContext extends ClassPathXmlApplicationContext{
    
    public ExtendClassPathXmlApplicationContext(ClassLoader cl,ApplicationContext parent,String ... locations){
        super(locations, false);
        if (parent != null)
            setParent(parent);
        
        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        if (cl != null){
            // spring中的有些类使用的是Thread的ClassLoader，此处必须控制
            if(oldClassLoader!=cl){
                Thread.currentThread().setContextClassLoader(cl);
                setClassLoader(cl);
            }
        }
        
        try{
            refresh();
        }finally{
            // 恢复线程的class loader
            if(oldClassLoader!=cl){
                Thread.currentThread().setContextClassLoader(oldClassLoader);
            }
        }
        
    }

}
