/*
 * $Id: ClassPathFileSearcher.java, 2015-6-2 下午04:05:32  Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.resource;

import java.net.URL;
import java.util.List;

/**
 * <p>
 * Title: ClassPathFileSearcher
 * </p>
 * <p>
 * Description:
 * 类路径文件搜索接口
 * </p>
 * 
 * @author 
 * created 2015-6-2 下午04:05:32
 * modified [who date description]
 * check [who date description]
 */
public interface ClassPathFileSearcher {
    
    /**
     * 在类路径中查找符合文件名称规则的文件
     * 举例：matchPattern="××/×probablecause.xml",标识在类路径中查找probablecause.xml结尾的文件
     * @param matchPattern
     *        文件名称匹配规则
     * @return 
     *         返回所有符合条件的文件的绝对路径
     */
    public List<String> search(String matchPattern);
    
    /**
     * 在类路径中查找符合文件名称规则资源
     * 举例：matchPattern=××/×probablecause.xml,标识在类路径中查找probablecause.xml结尾的文件
     * @param matchPattern
     *         文件名称匹配规则
     * @return 
     *         返回所有符合条件的文件资源URL
     */
    public List<URL> searchResource(String matchPattern);
    
    /**
     * 搜索资源
     * @param matchPattern
     * @param classLoader
     * @return 找到的URL
     */
    public List<URL> searchResource(String matchPattern,ClassLoader classLoader);
    
    /**
     * 在类路径中查找符合条件的class全名
     * @param matchPattern com.sf.core.smcore.model.*
     * @return
     */
    public List<String> searchClass(String matchPattern);
    
    /**
     * 搜索类
     * @param matchPattern
     * @param classLoader
     * @return
     */
    public List<String> searchClass(String matchPattern,ClassLoader classLoader);

}
