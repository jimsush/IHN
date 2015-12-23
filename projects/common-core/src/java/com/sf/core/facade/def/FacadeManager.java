/*
 * $Id: FacadeManager.java, 2015-1-30 下午04:30:27 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.facade.def;

import java.util.List;


/**
 * <p>
 * Title: FacadeManager
 * </p>
 * <p>
 * Description:管理facade
 * </p>
 * 
 * @author sufeng
 * created 2015-1-30 下午04:30:27
 * modified [who date description]
 * check [who date description]
 */
public interface FacadeManager {

    /**
     * 得到before处理的所有处理器
     * @return
     */
    public List<FacadeProcessor> getFacadeProcessorByBeforeOrder();
    
    /**
     * 得到after处理的所有处理器
     * @return
     */
    public List<FacadeProcessor> getFacadeProcessorByAfterOrder();
    
    /**
     * 得到所有的facade处理器名称
     * @return
     */
    public List<String> getAllFacadeProcessorNames();
    
    /**
     * 放facade处理器在职责链的最后
     * @param processor facade处理器
     */
    public void appendFacadeProcessor(FacadeProcessor processor);
    
    /**
     * 添加一个facade处理器到职责链的指定位置
     * @param previousProcessorName  上一个处理器名称，null表示放在职责链的最前面
     * @param processor              facade处理器
     */
    public void insertFacadeProcessor(String previousProcessorName,FacadeProcessor processor);
    
    /**
     * 删除一个facade处理器
     * @param processorName
     */
    public void removeFacadeProcessor(String processorName);
    
}
