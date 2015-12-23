/*
 * $Id: XmlParseUtil.java, 2015-2-9 下午02:35:57 victor Exp $
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
import java.net.URL;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.sf.base.exception.SfException;

/**
 * <p>
 * Title: XmlParseUtil
 * </p>
 * <p>
 * Description: XML解析工具类,能从file,url,inputstream中读取XML文件
 * </p>
 * 
 * @author victor
 * created 2008-12-16 上午10:53:56
 * modified [who date description]
 * check [who date description]
 */
public class XmlParseUtil {
    
    private URL xmlFile = null;
    private InputStream inputStream=null;
    
    private Element root = null;

    /**
     * @param root
     * @param nameAttr
     * @return
     */
    public static String getNodeAttr(Element node, String nameAttr) {
        return node.getAttributeValue(nameAttr);
    }

    /**
     * @param node
     * @param nameAttr
     * @param string
     * @return
     */
    public static String getNodeAttr(Element node, String nameAttr, String defaultValue) {
        return node.getAttributeValue(nameAttr, defaultValue);
    }
    
    /**
     * xml file url
     * 
     * @param file
     */
    public XmlParseUtil(URL file) {
        this.xmlFile = file;
        parseByFile();
    }

    public XmlParseUtil(InputStream inputStream){
        this.inputStream=inputStream;
        parseByInputStream();
    }
    

    private void parseByFile() {
        
        SAXBuilder builder = new SAXBuilder();
        try {
            Document doc = builder.build(xmlFile);
            root = doc.getRootElement();
        } catch (JDOMException ex) {
            throw new SfException(ex);
        } catch (IOException ex) {
            throw new SfException(ex);
        }
    }
    
    private void parseByInputStream() {

        SAXBuilder builder = new SAXBuilder();
        try {
            Document doc = builder.build(inputStream);
            root = doc.getRootElement();
        } catch (JDOMException ex) {
            throw new SfException(ex);
        } catch (IOException ex) {
            throw new SfException(ex);
        }
    }

    /**
     * @return
     */
    public Element getDocumentElement() {
        return root;
    }
    
    /**
     * 关闭XML文件
     */
    public void close(){
        if(inputStream!=null){
            try{
                inputStream.close();
            }catch(Exception ex){
                throw new SfException(ex);
            }
        }
    }
    
}
