/*
 * $Id: JarPathFileSearcher.java, 2015-1-7 ����02:21:44 Owner Exp $
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
 * <br>��Jar�������������������ļ�
 * </p>
 * 
 * @author 
 * created 2015-1-7 ����02:21:44
 * modified [who date description]
 * check [who date description]
 */
public interface JarPathFileSearcher {
    /**
     * ��ָ��Jar���������ļ�
     * @param jarPath
     *        jar��·��
     * @param fileNameMatchPattern
     *        �ļ�����ƥ�����,����**sf*.txt
     * @return
     */
    public List<String> search(String jarPath,String fileNameMatchPattern);

}
