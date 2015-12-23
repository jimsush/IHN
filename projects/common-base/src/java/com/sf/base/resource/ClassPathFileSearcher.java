/*
 * $Id: ClassPathFileSearcher.java, 2015-6-2 ����04:05:32  Exp $
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
 * ��·���ļ������ӿ�
 * </p>
 * 
 * @author 
 * created 2015-6-2 ����04:05:32
 * modified [who date description]
 * check [who date description]
 */
public interface ClassPathFileSearcher {
    
    /**
     * ����·���в��ҷ����ļ����ƹ�����ļ�
     * ������matchPattern="����/��probablecause.xml",��ʶ����·���в���probablecause.xml��β���ļ�
     * @param matchPattern
     *        �ļ�����ƥ�����
     * @return 
     *         �������з����������ļ��ľ���·��
     */
    public List<String> search(String matchPattern);
    
    /**
     * ����·���в��ҷ����ļ����ƹ�����Դ
     * ������matchPattern=����/��probablecause.xml,��ʶ����·���в���probablecause.xml��β���ļ�
     * @param matchPattern
     *         �ļ�����ƥ�����
     * @return 
     *         �������з����������ļ���ԴURL
     */
    public List<URL> searchResource(String matchPattern);
    
    /**
     * ������Դ
     * @param matchPattern
     * @param classLoader
     * @return �ҵ���URL
     */
    public List<URL> searchResource(String matchPattern,ClassLoader classLoader);
    
    /**
     * ����·���в��ҷ���������classȫ��
     * @param matchPattern com.sf.core.smcore.model.*
     * @return
     */
    public List<String> searchClass(String matchPattern);
    
    /**
     * ������
     * @param matchPattern
     * @param classLoader
     * @return
     */
    public List<String> searchClass(String matchPattern,ClassLoader classLoader);

}
