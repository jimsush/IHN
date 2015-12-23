/*
 * $Id: JarPathFileSearcher.java, 2015-1-7 下午02:21:44 Owner Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.resource;

import java.util.List;

/**
 * <p>
 * Title: JarPathFileSearcher
 * </p>
 * <p>
 * Description:
 * <br>从Jar包中搜索符合条件的文件
 * </p>
 * 
 * @author 
 * created 2015-1-7 下午02:21:44
 * modified [who date description]
 * check [who date description]
 */
public interface JarPathFileSearcher {
    /**
     * 从指定Jar包中搜索文件
     * @param jarPath
     *        jar包路径
     * @param fileNameMatchPattern
     *        文件名称匹配规则,比如**sf*.txt
     * @return
     */
    public List<String> search(String jarPath,String fileNameMatchPattern);

}
