/*
 * $Id: XmlFileReader.java, 2015-2-9 下午02:35:57  Exp $
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
 * Description: xml castor读取工具类
 * </p>
 * 
 * @author 
 * created 2015-2-9 下午02:35:57
 * modified [who date description]
 * check [who date description]
 */
public class XmlFileReader {

    /**
     * 会读取objName-mapping.xml objName-data.xml两个文件
     * 
     * @param mappingInputStream
     *            mapping文件inputStream
     * @param dataInputStream
     *            data文件inputStream
     * @return mapping文件中指定的类的对象
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
     * 会读取objName-mapping.xml objName-data.xml两个文件
     * 
     * @param path data的xml文件的存放路径,比如d:/a/b/
     * @param mappingFilePath mapping的xml文件的存放地址,比如d:/a/b/
     * @param objName 映射对象的文件前缀
     * 
     * @return objName-mapping.xml中指定的类的对象
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
     * 读取castor文件，并转换为对象
     * @param dataPath data文件路径
     * @param mappingFilePath mapping文件路径
     * @return
     */
    public static Object getXmlConfig(String dataPath, String mappingFilePath) {
        InputStream dataInputStream = FileLoader.getInputStream(dataPath);
        Object obj=getXmlConfig(dataInputStream, mappingFilePath);
        return obj;
    }
    
    /**
     * 读取castor文件，并转换为对象
     * @param dataInputStream data文件的输入流
     * @param mappingFilePath mapping文件路径
     * @return
     */
    public static Object getXmlConfig(InputStream dataInputStream, String mappingFilePath) {
        Object obj = null;
        InputStream inputmap = null;
        if(dataInputStream==null)
            return obj;
        
        try {
            // mapping文件处理
            inputmap = FileLoader.getInputStream(mappingFilePath);
            if(inputmap==null)
                return obj;
            InputSource is = new InputSource(inputmap);
            Mapping mapping = new Mapping(Thread.currentThread().getContextClassLoader());
            mapping.loadMapping(is);
            Unmarshaller un = new Unmarshaller(mapping);
            
            // data文件处理
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
     * mapping文件位于系统相对目录下，而data文件可以随便指定，由dataFullName指定
     * 
     * @param path
     *            xml文件的存放路径
     * @param mappingName  映射文件名
     * @param dataFullName 数据文件的全路径
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
