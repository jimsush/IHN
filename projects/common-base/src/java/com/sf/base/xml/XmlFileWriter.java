/*
 * $Id: XmlFileWriter.java, 2015-2-9 ����02:35:57  Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.xml;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Marshaller;
import org.jdom.input.DOMBuilder;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.sf.base.exception.SfException;
import com.sf.base.resource.FileLoader;

/**
 * <p>
 * Title: XmlWriterImpl
 * </p>
 * <p>
 * Castorдʵ��
 * </p>
 * 
 * @author pl
 * created 2015-2-21 ����05:35:01
 * modified [who date description]
 * check [who date description]
 */
public class XmlFileWriter {

    /**
     * ͨ��Castor������д��ָ��Ŀ¼���ļ�
     * @param mappingFileName Mapping·�����ļ���
     * @param dataFile ����д���ļ���·�����ļ���
     * @param obj ��Ҫд�������
     * return true�ɹ�,falseʧ��
     */
    public static boolean writeFile(String mappingFileName, String dataFile,
            Object obj) throws Exception {
        InputStream fis = null;
        OutputStreamWriter osw = null;
        try {
            Mapping mapping = new Mapping(Thread.currentThread().getContextClassLoader());
            fis=FileLoader.getInputStream(mappingFileName);
            InputSource is = new InputSource(fis);
            mapping.loadMapping(is);
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Marshaller ma = new Marshaller(doc);
            ma.setMapping(mapping);
            ma.marshal(obj);
            XMLOutputter outputter = new XMLOutputter("    ", true);
            DOMBuilder domBuilder = new DOMBuilder();
            org.jdom.Document document = domBuilder.build(doc);
            FileOutputStream fos = new FileOutputStream(dataFile);
            osw = new OutputStreamWriter(fos, "UTF-8");
            outputter.output(document, osw);
            return true;
        } catch (Exception ex) {
            throw new SfException(ex);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    throw new SfException(ex);
                }
            }
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException ex) {
                    throw new SfException(ex);
                }
            }
        }
    }
    
    
    /**
     *  ͨ��Castor������д��ָ��Ŀ¼���ļ�
     * @param mappingFileName
     *       
     * @param dataOutPutStream
     * @param obj
     * @return
     * @throws Exception
     */
    public static boolean writeFile(InputStream mappingFileInputStream, OutputStream dataFileOutPutStream,
            Object obj) throws Exception {
        InputStream fis = mappingFileInputStream;
        OutputStreamWriter osw = null;
        try {
            Mapping mapping = new Mapping(Thread.currentThread().getContextClassLoader());
            InputSource is = new InputSource(mappingFileInputStream);
            mapping.loadMapping(is);
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Marshaller ma = new Marshaller(doc);
            ma.setMapping(mapping);
            ma.marshal(obj);
            XMLOutputter outputter = new XMLOutputter("    ", true);
            DOMBuilder domBuilder = new DOMBuilder();
            org.jdom.Document document = domBuilder.build(doc);
            osw = new OutputStreamWriter(dataFileOutPutStream, "UTF-8");
            outputter.output(document, osw);
            return true;
        } catch (Exception ex) {
            throw new SfException(ex);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    throw new SfException(ex);
                }
            }
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException ex) {
                    throw new SfException(ex);
                }
            }
        }
    }
    
    /**
     * ͨ��Castor������д��ָ��Ŀ¼���ļ�
     * @param mappingInputStream mapping�ļ���������
     * @param dataFile ����д���ļ���·�����ļ���
     * @param obj ��Ҫд�������
     * return true�ɹ�,falseʧ��
     */
    public static boolean writeFile(InputStream mappingInputStream, String dataFile,
            Object obj) throws Exception {
        InputStream fis = mappingInputStream;
        OutputStreamWriter osw = null;
        try {
            InputSource is = new InputSource(fis);
            Mapping mapping = new Mapping(Thread.currentThread().getContextClassLoader());
            mapping.loadMapping(is);
            // ��well-formated�ĸ�ʽ���
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Marshaller ma = new Marshaller(doc);
            ma.setMapping(mapping);
            ma.marshal(obj);
            XMLOutputter outputter = new XMLOutputter("    ", true);
            DOMBuilder domBuilder = new DOMBuilder();
            org.jdom.Document document = domBuilder.build(doc);
            FileOutputStream fos = new FileOutputStream(dataFile);
            osw = new OutputStreamWriter(fos, "UTF-8");
            outputter.output(document, osw);
            return true;
        } catch (Exception ex) {
            throw new SfException(ex);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    throw new SfException(ex);
                }
            }
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException ex) {
                    throw new SfException(ex);
                }
            }
        }
    }

}
