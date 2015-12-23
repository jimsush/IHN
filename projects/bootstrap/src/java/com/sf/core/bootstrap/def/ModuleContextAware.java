/*
 * $Id: ModuleContextAware.java, 2015-3-31 下午01:22:05 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.bootstrap.def;

/**
 * <p>
 * Title: ModuleContextAware
 * </p>
 * <p>
 * Description: 模块可实现该接口，当load module的时候,会将ModuleContext实例设置到当前module中
 * ，这样可以避免用单例ModuleContext.getInstance()获取ModuleContext，方便做单元测试
 * </p>
 * 
 * @author sufeng
 * created 2015-3-31 下午01:22:05
 * modified [who date description]
 * check [who date description]
 */
public interface ModuleContextAware {

    /**
     * 设置module context
     * @param moduleContext 模块上下文
     */
    public void setModuleContext(ModuleContext moduleContext);
    
}
