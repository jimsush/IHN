/*
 * $Id: ClassPathXMLFileReader.java, 2015-8-6 ����01:19:59  Exp $
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
 * Title: ClassPathXMLFileReader
 * </p>
 * <p>
 * Description:
 * ��·���ļ���ȡ�ӿ�
 * </p>
 * 
 * @author 
 * created 2015-8-6 ����01:19:59
 * modified [who date description]
 * check [who date description]
 */
public interface ClassPathXMLFileReader {

    /**
     * ��������XML�ļ�����������ļ�XML�ļ�������ȡ����
     * @param <T>
     * @param xmlFileNameMatchPattern
     *        XML�ļ��ļ�����ƥ�������*probablecause.xml,������probablecause.xml��β���ļ�
     * @param xmlFileMapping
     *        ����ȡ��XML�ļ���Mapping�ļ�
     * @param objClass
     *        XML�ļ�ӳ���java Class
     * @return
     *        ���ض����б�
     */
    public <T> List<T> searchAndRead(String xmlFileNameMatchPattern,String xmlFileMapping,Class<? extends T> objClass);
    
    
    /**
     * ��ȡָ��XML�ļ�����
     * @param <T>
     * @param xmlDataFile
     *        XML�����ļ�����
     * @param xmlFileMapping
     *        ����ȡ��XML�ļ���Mapping�ļ�����
     * @param objClass
     *        ����Java����
     * @return
     *        ���ض����б�
     */
    public <T> List<T> read(String xmlDataFile,String xmlFileMapping,Class<? extends T> objClass);
    
}
