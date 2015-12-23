/*
 * $Id: CommonPathFileSearcher.java, 2015-1-7 下午02:17:31 Owner Exp $
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
 * Title: CommonPathFileSearcher
 * </p>
 * <p>
 * Description:
 * <br>从指定文件目录中搜索文件
 * </p>
 * 
 * @author 
 * created 2015-1-7 下午02:17:31
 * modified [who date description]
 * check [who date description]
 */
public interface CommonPathFileSearcher {
    
    /**
     * 从指定文件目录中搜索文件
     * @param filePath
     *        文件目录
     * @param fileNameMatchPattern
     *        文件名称匹配规则,比如**sf*.txt
     * @return
     *        符合条件的文件
     */
    public List<String> search(String filePath,String fileNameMatchPattern);
    
}
