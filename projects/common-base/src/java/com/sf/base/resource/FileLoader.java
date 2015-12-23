/*
 * $Id: FileLoader.java, 2015-2-9 ����01:07:41  Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.ResourceUtils;

import com.sf.base.exception.ExceptionUtils;
import com.sf.base.util.sys.SysUtils;

/**
 * <p>
 * Title: FileLoader
 * </p>
 * <p>
 * Description: ��ȡ�ļ����ļ�stream�Ĺ�����
 * </p>
 * 
 * @author 
 * created 2015-2-9 ����01:07:41
 * modified [who date description]
 * check [who date description]
 */
public class FileLoader {
    
    /**
     * config�ļ���λ��
     */
    public static final String CONF_DIR = "conf/";
    
    public static final String ROOT_DIR = "modules/";

    /**
     * "images/"
     */
    public static final String ICON_DIR = "images/";
    
    
    public static final String I18N_DIR = "i18n/";
    /**
     * "sound/"
     */
    public static final String AUDIO_DIR = "sound/";

    private static Resource base;
    static {
        URL url = FileLoader.class.getResource("/");
        if (url != null) {
            base = new UrlResource(url);
        }
    }

    /**
     * ��ȡ�ļ���
     * 
     * @param filePath
     * �ɴ�3��·����
     * 1,�ļ�·��,d:/config/data/deviceinfo-data.xml
     * 2,URL·��,file:/d:/config/data/deviceinfo-data.xml
     * 3,classpath:��Ϊǰ׺��·��,classpath:config/data/deviceinfo-data.xml
     * @return
     */
    public static InputStream getInputStream(final String filePath) {
        // 1�� ��Ŀ¼�����
        InputStream is = null;
        try{
            is = getInputStreamFromFile(filePath);
        }catch(Exception ex){
            //System.err.println("getInputStreamFromFile not found:"+filePath);
        }
        if (is != null)
            return is;
        
        // 2�� ��url�����
        try{
            is = getInputStreamFromUrl(filePath);
        }catch(Exception ex){
            //System.err.println("getInputStreamFromUrl not found:"+filePath);
        }
        if (is != null) 
            return is;

        // 3����classpath(classpath·����jar��)�ж�ȡ
        try{
            is = getInputStreamFromClasspath(filePath);
        }catch(Exception ex){
            //System.err.println("getInputStreamFromClasspath not found:"+filePath);
        }
        if (is != null) 
            return is;
        
        // 4�����ؽ��
        System.err.println("����Դ " + filePath + " ���ļ���InputStreamΪnull��");
        return null;
    }

    /**
     * ��jar��ȡinputstream
     * @param jarFilePath ���� d:/a.jar
     * @param filePath    jar�ڵ��ļ�,���� com/sf/Test.class
     * @return InputStream
     */
    public static InputStream getInputStreamFromJar(final String jarFilePath,final String filePath) throws Exception  {
        JarFile file=new JarFile(jarFilePath);
        JarEntry entry=new JarEntry(filePath);
        InputStream inputStream = file.getInputStream(entry);
        return inputStream;
    }

    /**
     * ���ļ���ȡInputStream
     * @param filePath ����d:/resource/config.xml
     * @return
     * @throws Exception
     */
    public static InputStream getInputStreamFromFile(String fileFullPath) throws Exception {
        URL url=getUrlFromFile(fileFullPath);
        if(url==null)
            return null;
        return getInputStreamFromUrl(url.toString());
    }
    
    /**
     * ��URL·����ȡInputStream
     * 
     * @param fileUrl �ļ�·����URL����file:/D:/resource/config.xml
     * @return
     */
    public static InputStream getInputStreamFromUrl(String fileUrl) throws Exception {
        if (base != null) {
            Resource res = base.createRelative(fileUrl);
            if (res.exists()) {
                return res.getInputStream();
            }
        }
        return null;
    }

    /**
     * ��classpath(classpath��·������jar�ж��ܶ�ȡ��)�ж�ȡ�ļ�
     * @param filePath ��classpath��ͷ,����classpath:meta-inf/config/a.xml
     * @return
     */
    public static InputStream getInputStreamFromClasspath(String filePath) throws Exception {
        URL url=ResourceUtils.getURL(filePath);
        if(url!=null){
            Resource res=new UrlResource(url);
            if(res.exists())
                return res.getInputStream();
        }
        return null;
    }

    /**
     * �����ļ����Ե�·��,��user.dirΪ���
     * 
     * @param filePath
     *            �ļ����·�������� modules/sf.xml
     * @return
     */
    public static String getAbsoluteFilePath(final String filePath) {
        String homeDir = SysUtils.getUserDir();
        return homeDir+File.separator+filePath;
    }

    /**
     * �õ�URL
     * 
     * @param filePath
     * �ɴ�3��·����
     * 1,�����ļ�·��,d:/config/data/deviceinfo-data.xml
     * 2,���·��,deviceinfo-data.xml
     * 3,classpath:��Ϊǰ׺��·��,classpath:config/data/deviceinfo-data.xml
     * @return
     */
    public static URL getUrl(final String filePath) {
        URL url = null;
        // 1�� �Ӿ���Ŀ¼��
        try {
            url = getUrlFromFile(filePath);
        } catch (Exception e) {
            //System.err.println("getUrlFromFile not found:"+filePath);
        }
        if (url != null) 
            return url;
        
        // 2,������������·�������·������
        String fullFilePath = getAbsoluteFilePath(filePath);
        try {
            url = getUrlFromFile(fullFilePath);
        } catch (Exception e) {
            //System.err.println("getUrlFromFile not found:"+filePath);
        }
        if (url != null) 
            return url;

        // 3����classpath�ж�ȡ
        try{
            url = getUrlFromClasspath(filePath);
        } catch (Exception e) {
            //System.err.println("getUrlFromClasspath not found:"+filePath);
        }
        
        // 4�����ؽ��
        if (url == null) {
            System.out.println("��Դ " + filePath + " ��URL�����ڣ�");
            return null;
        }
        return url;
    }

    /**
     * ��file pathת��ΪURL
     * @param fileFullPath ·�� eg:d:\resource\conf.xml
     * @return
     */
    public static URL getUrlFromFile(String fileFullPath)
            throws Exception {
        File f = new File(fileFullPath);
        if (f.exists()) {
            URL url = f.toURI().toURL();
            return url;
        }
        return null;
    }

    /**
     * ��classpath(classpath��·������jar�ж��ܶ�ȡ��)�еõ�url
     * @param filePath ����classpath:meta-inf/config/a.xml
     * @return
     */
    public static URL getUrlFromClasspath(String filePath) throws Exception{
        URL url=ResourceUtils.getURL(filePath);
        return url;
    }
    
    /**
     * jar���ж�ȡ�ļ���ʱ��filePath��Ҫ��"/"��ʼ
     * 
     * @param filePath
     *          ����classpath:meta-inf/config/a.xml
     * @return
     */
    private static String initFilePathForJar(final String filePath) {
        if (!filePath.startsWith("/")) {
            return "/" + filePath;
        }
        return filePath;
    }
    
    /**
     * @param filePath
     *            ���METF-INFĿ¼��·��
     * @return
     */
    public static Properties getProperties(String filePath) {
        if (filePath == null)
            throw new IllegalArgumentException();
        InputStream fis = null;
        try {
            Properties result = new Properties();
            // ��Ŀ¼��ȡ�ļ���
            String apexFilePath = getAbsoluteFilePath(filePath);
            if (new File(apexFilePath).exists()) {
                fis = new FileInputStream(apexFilePath);
            } else {
                // ��ȡʧ�ܣ���jar����ȡ�ļ���
                filePath = initFilePathForJar(filePath);
                fis = getInputStream(filePath);
            }
            if (fis != null)
                result.load(fis);
            return result;
        } catch (Exception e) {
            System.err.println(ExceptionUtils.getCommonExceptionInfo(e));
        } finally {
            if (fis != null)
                try {
                    fis.close();
                } catch (IOException e) {
                    System.err.println(ExceptionUtils.getCommonExceptionInfo(e));
                }
        }
        return null;
    }

    /**
     * @param prop ����
     * @param filePath
     *            �ļ��洢�����·����ָ���Ŀ¼��·��
     */
    public static void storeProperties(Properties prop, String filePath) {
        if (prop == null || filePath == null)
            throw new IllegalArgumentException();
        
        FileOutputStream fos = null;
        try {
            String saveFilePath = getAbsoluteFilePath(filePath);
            File file = new File(filePath);
            if (!file.exists()) {
                File file2 = new File(saveFilePath);
                if (!file2.exists()){
                    if(!file2.createNewFile())
                        return;
                }
            }
            
            fos = new FileOutputStream(saveFilePath);
            prop.store(fos, null);
            fos.close();
        } catch (Exception e) {
            System.err.println(ExceptionUtils.getCommonExceptionInfo(e));
        } finally {
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                    System.err.println(ExceptionUtils.getCommonExceptionInfo(e));
                }
        }
    }
    
}
