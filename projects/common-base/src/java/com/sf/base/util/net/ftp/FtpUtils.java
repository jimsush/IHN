/*
 * $Id: FtpUtils.java, 2015-11-12 ����11:46:42 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.util.net.ftp;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;

import com.sf.base.exception.SfException;

/**
 * <p>
 * Title: FtpUtils
 * </p>
 * <p>
 * Description: ftp����
 * </p>
 * 
 * @author sufeng
 * created 2015-11-12 ����11:46:42
 * modified [who date description]
 * check [who date description]
 */
public class FtpUtils {
    
    /**
     * ���Ӳ���¼��ftp��������
     * @param ip
     * @param user
     * @param password
     * @return ftp�ͻ��˲�������
     */
    public static FTPClient connect2Ftp(String ip,String user,String password){
        FTPClient client = new FTPClient();
        try{
            client.connect(ip);
            client.login(user, password);
        }catch(Exception ex ){
            throw new SfException(ex);
        }
        return client;
    }
    
    /**
     * ��ftp������������һ���ļ�
     * @param ip
     * @param user
     * @param password
     * @param ftpFullPathFileName Զ��ftp�������ϵ��ļ�·�����ļ���������: /mydir/readme.txt
     * @param localFullPathFileName ���ص����ص��ļ�·�����ļ��������� d:/download,d:/download/,d:/download/readme.txt
     * 
     */
    public static void downFile(String ip,String user,String password
            ,String ftpFullPathFileName,String localFullPathFileName){
        FTPClient client = connect2Ftp(ip,user,password);
        FileOutputStream fileOutputStream = null;
        try{
            String[] dirFile=parseFtpFileAndDir(ftpFullPathFileName);
            client.changeWorkingDirectory(dirFile[0]);
            
            String tempLocalFile=localFullPathFileName;
            if(localFullPathFileName.endsWith("/"))
                tempLocalFile=localFullPathFileName+dirFile[1];
            File localFile=new File(tempLocalFile);
            if(localFile.isDirectory()){
                if(tempLocalFile.endsWith("/"))
                    tempLocalFile=localFullPathFileName+dirFile[1];
                else
                    tempLocalFile=localFullPathFileName+"/"+dirFile[1];
            }

            fileOutputStream = new FileOutputStream(tempLocalFile);
            String codecFileName=new String(dirFile[1].getBytes("GBK"),"ISO-8859-1");  //�������غ���ļ�����Ϊ��
            client.retrieveFile(codecFileName, fileOutputStream);
        }catch(Exception ex){
            throw new SfException(ex);
        }finally{
            if(fileOutputStream!=null){
                try{
                    fileOutputStream.close();
                    fileOutputStream=null;
                }catch(Exception ex2){
                    throw new SfException(ex2);
                }
            }
        }
    }
    
    /**
     * ����һ��ftp·��
     * @param ftpFullPathFileName
     * @return ·�����ļ���
     */
    public static String[] parseFtpFileAndDir(String ftpFullPathFileName){
        String[] dirFile=new String[2];
        if(StringUtils.isEmpty(ftpFullPathFileName)){
            dirFile[0]="/";
            return dirFile;
        }
        if("/".equals(ftpFullPathFileName)){
            dirFile[0]="/";
            return dirFile;
        }
        
        String tempPath=ftpFullPathFileName;
        if(!ftpFullPathFileName.startsWith("/"))
            tempPath="/"+ftpFullPathFileName;
        int pos=tempPath.lastIndexOf("/");
        dirFile[0]=tempPath.substring(0,pos+1);
        dirFile[1]=tempPath.substring(pos+1);
        return dirFile;
    }

}
