/*
 * $Id: XmlFileReader.java, 2015-2-9 ����02:35:57  Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.xml;

import java.io.IOException;
import java.io.InputStream;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import com.sf.base.exception.SfException;
import com.sf.base.resource.FileLoader;

/**
 * <p>
 * Title: XmlFileReader
 * </p>
 * <p>
 * Description: xml castor��ȡ������
 * </p>
 * 
 * @author 
 * created 2015-2-9 ����02:35:57
 * modified [who date description]
 * check [who date description]
 */
public class XmlFileReader {

    /**
     * ���ȡobjName-mapping.xml objName-data.xml�����ļ�
     * 
     * @param mappingInputStream
     *            mapping�ļ�inputStream
     * @param dataInputStream
     *            data�ļ�inputStream
     * @return mapping�ļ���ָ������Ķ���
     */
    public static Object getXmlConfig(InputStream mappingInputStream, InputStream dataInputStream) {
        Object obj = null;
        try {
            InputSource is = new InputSource(mappingInputStream);
            Mapping mapping = new Mapping(Thread.currentThread().getContextClassLoader());
            mapping.loadMapping(is);
            Unmarshaller un = new Unmarshaller(mapping);
            InputSource dataIs = new InputSource(dataInputStream);
            obj = un.unmarshal(dataIs);
        } catch (Exception e) {
            throw new SfException(e);
        } finally {
            if (mappingInputStream != null) {
                try {
                    mappingInputStream.close();
                } catch (IOException e) {
                    throw new SfException(e);
                }
            }
            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    throw new SfException(e);
                }
            }
        }
        return obj;
    }

    /**
     * ���ȡobjName-mapping.xml objName-data.xml�����ļ�
     * 
     * @param path data��xml�ļ��Ĵ��·��,����d:/a/b/
     * @param mappingFilePath mapping��xml�ļ��Ĵ�ŵ�ַ,����d:/a/b/
     * @param objName ӳ�������ļ�ǰ׺
     * 
     * @return objName-mapping.xml��ָ������Ķ���
     */
    public static Object getXmlConfig(String path, String mappingFilePath, String objName) {
        Object obj = null;
        InputStream inputmap = null;
        InputStream inputdata = null;
        try {
            inputmap = FileLoader.getInputStream(mappingFilePath + objName + "-mapping.xml");
            if(inputmap==null)
                return obj;
            InputSource is = new InputSource(inputmap);
            Mapping mapping = new Mapping(Thread.currentThread().getContextClassLoader());
            mapping.loadMapping(is);
            Unmarshaller un = new Unmarshaller(mapping);
            
            inputdata = FileLoader.getInputStream(path + objName + "-data.xml");
            if(inputdata==null)
                return obj;
            InputSource dataIs = new InputSource(inputdata);
            obj = un.unmarshal(dataIs);
        } catch (Exception e) {
            throw new SfException(e);
        } finally {
            if (inputmap != null) {
                try {
                    inputmap.close();
                } catch (IOException e) {
                    throw new SfException(e);
                }
            }
            if (inputdata != null) {
                try {
                    inputdata.close();
                } catch (IOException e) {
                    throw new SfException(e);
                }
            }
        }
        return obj;
    }
    
    /**
     * ��ȡcastor�ļ�����ת��Ϊ����
     * @param dataPath data�ļ�·��
     * @param mappingFilePath mapping�ļ�·��
     * @return
     */
    public static Object getXmlConfig(String dataPath, String mappingFilePath) {
        InputStream dataInputStream = FileLoader.getInputStream(dataPath);
        Object obj=getXmlConfig(dataInputStream, mappingFilePath);
        return obj;
    }
    
    /**
     * ��ȡcastor�ļ�����ת��Ϊ����
     * @param dataInputStream data�ļ���������
     * @param mappingFilePath mapping�ļ�·��
     * @return
     */
    public static Object getXmlConfig(InputStream dataInputStream, String mappingFilePath) {
        Object obj = null;
        InputStream inputmap = null;
        if(dataInputStream==null)
            return obj;
        
        try {
            // mapping�ļ�����
            inputmap = FileLoader.getInputStream(mappingFilePath);
            if(inputmap==null)
                return obj;
            InputSource is = new InputSource(inputmap);
            Mapping mapping = new Mapping(Thread.currentThread().getContextClassLoader());
            mapping.loadMapping(is);
            Unmarshaller un = new Unmarshaller(mapping);
            
            // data�ļ�����
            InputSource dataIs = new InputSource(dataInputStream);
            obj = un.unmarshal(dataIs);
        } catch (Exception e) {
            throw new SfException(e);
        } finally {
            if (inputmap != null) {
                try {
                    inputmap.close();
                } catch (IOException e) {
                    throw new SfException(e);
                }
            }
            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    throw new SfException(e);
                }
            }
        }
        return obj;
    }

    /**
     * mapping�ļ�λ��ϵͳ���Ŀ¼�£���data�ļ��������ָ������dataFullNameָ��
     * 
     * @param path
     *            xml�ļ��Ĵ��·��
     * @param mappingName  ӳ���ļ���
     * @param dataFullName �����ļ���ȫ·��
     * @return
     */
    public static Object getXmlConfigByDataFullName(String path, String mappingName, String dataFullName) {
        Object obj = null;
        InputStream input = null;
        try {
            input = FileLoader.getInputStream(path + mappingName + "-mapping.xml");
            InputSource is = new InputSource(input);
            Mapping mapping = new Mapping(Thread.currentThread().getContextClassLoader());
            mapping.loadMapping(is);
            Unmarshaller un = new Unmarshaller(mapping);
            input = FileLoader.getInputStream(dataFullName);
            InputSource dataIs = new InputSource(input);
            obj = un.unmarshal(dataIs);
        } catch (Exception e) {
            throw new SfException(e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    throw new SfException(e);
                }
            }
        }
        return obj;
    }

    
}
