/*
 * $Id: ExtendClassPathXmlApplicationContext.java, 2015-12-14 ����12:04:35 sufeng Exp $
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
 * Description:֧������thread context class loader��application context
 * </p>
 * 
 * @author sufeng
 * created 2015-12-14 ����12:04:35
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
            // spring�е���Щ��ʹ�õ���Thread��ClassLoader���˴��������
            if(oldClassLoader!=cl){
                Thread.currentThread().setContextClassLoader(cl);
                setClassLoader(cl);
            }
        }
        
        try{
            refresh();
        }finally{
            // �ָ��̵߳�class loader
            if(oldClassLoader!=cl){
                Thread.currentThread().setContextClassLoader(oldClassLoader);
            }
        }
        
    }

}
