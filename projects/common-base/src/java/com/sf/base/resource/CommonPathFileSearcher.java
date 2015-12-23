/*
 * $Id: CommonPathFileSearcher.java, 2015-1-7 ����02:17:31 Owner Exp $
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
 * <br>��ָ���ļ�Ŀ¼�������ļ�
 * </p>
 * 
 * @author 
 * created 2015-1-7 ����02:17:31
 * modified [who date description]
 * check [who date description]
 */
public interface CommonPathFileSearcher {
    
    /**
     * ��ָ���ļ�Ŀ¼�������ļ�
     * @param filePath
     *        �ļ�Ŀ¼
     * @param fileNameMatchPattern
     *        �ļ�����ƥ�����,����**sf*.txt
     * @return
     *        �����������ļ�
     */
    public List<String> search(String filePath,String fileNameMatchPattern);
    
}
