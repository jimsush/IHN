/*
 * $Id: ClassPathXMLFileReaderImpl.java, 2015-8-6 ÏÂÎç01:26:40  Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.sf.base.xml.ArrayListItems;
import com.sf.base.xml.XmlFileReader;

/**
 * <p>
 * Title: ClassPathXMLFileReaderImpl
 * </p>
 * <p>
 * Description: ¶ÁÈ¡xml
 * </p>
 * 
 * @author 
 * created 2015-8-6 ÏÂÎç01:26:40
 * modified [who date description]
 * check [who date description]
 */
public class ClassPathXMLFileReaderImpl implements ClassPathXMLFileReader {

    private ClassPathFileSearcher classPathFileSearcher=new ClassPathFileSearcherImpl();
    
    /**
     * 
     * @see com.sf.base.resource.ClassPathXMLFileReader#read(java.lang.String, java.lang.String, java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> read(String xmlDataFile, String xmlFileMapping,Class<? extends T> objClass) {
        ArrayListItems<T> data = (ArrayListItems<T>) XmlFileReader.getXmlConfig(xmlDataFile, xmlFileMapping);
        return data.getItems();
    }
    
    /**
     * 
     * @see com.sf.base.resource.ClassPathXMLFileReader#searchAndRead(java.lang.String, java.lang.String, java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> searchAndRead(String xmlFileNameMatchPattern,String xmlFileMapping, Class<? extends T> objClass) {
        List<URL> filePathResources = classPathFileSearcher.searchResource(xmlFileNameMatchPattern);
       
        List<T> fileData=new ArrayList<T>();
        
        for (URL filePathResource : filePathResources) { 
            InputStream inputStream = null;
            try {
                inputStream = filePathResource.openStream();
            } catch (IOException ex) {
                throw new IllegalStateException("getInputStream failure", ex);
            }
            ArrayListItems<T> data = (ArrayListItems<T>) XmlFileReader.getXmlConfig(inputStream, xmlFileMapping);
            if(data!=null)
                 fileData.addAll(data.getItems());
        }
        
        return fileData;
    }

}
