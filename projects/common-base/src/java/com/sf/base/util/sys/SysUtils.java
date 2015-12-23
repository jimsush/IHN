/*
 * $Id: SysUtils.java, 2015-11-12 上午11:08:46 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.util.sys;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Title: SysUtils
 * </p>
 * <p>
 * Description: 系统相关的工具类
 * </p>
 * 
 * @author sufeng
 * created 2015-11-12 上午11:08:46
 * modified [who date description]
 * check [who date description]
 */
public class SysUtils {

    /**
     * windows平台
     */
    public static final String OS_WIN = "windows";
    
    /**
     * linux平台
     */
    public static final String OS_LINUX = "linux";

    /**
     * 当前子系统运行的平台
     */
    private static String platForm = null;

    /**
     * 得到当前机器运行操作系统平台
     * 
     * @return windows/linux
     */
    public synchronized static String getPlatForm() {
        if (platForm == null) {
            String property = System.getProperty("os.name");
            if (property.startsWith("Windows"))
                platForm = OS_WIN;
            else if (property.startsWith("Linux"))
                platForm = OS_LINUX;
        }
        return platForm;
    }
    
    /**
     * 等待并不抛异常
     * @param millis 毫秒
     */
    public static void sleepNotException(long millis){
        try{
            Thread.sleep(millis);
        }catch(Exception ex){
            System.out.println("sleep ex,"+ex.getClass().getSimpleName());
        }
    }
    
    /**
     * 得到当前用户运行目录
     * @return 路径
     */
    public static String getUserDir(){
        String userDir = null;
        try{
            userDir=System.getProperty("user.dir");
        }catch(Exception ex){
            System.err.println("getUserDir ex:"+ex.getMessage());
        }
        return userDir;
    }
    
    /**
     * 解析main函数的参数，并放入一个map中，可使用-来分隔参数，支持类似run.exe daemon -D servermode -p c:\abc的格式
     * @param args
     * @return
     */
    public static Map<String,String> parseArg(String[] args){
       Map<String,String> map=new HashMap<String, String>();
       if(args==null || args.length==0)
           return map;
       
       String lastKey=null;
       for(int i=0; i<args.length; i++){
           if(args[i]==null || args[i].equals(""))
               continue;
           if(args[i].startsWith("-")){
               lastKey=args[i].substring(1);
           }else{
               if(lastKey!=null){
                   map.put(lastKey,args[i]);
                   lastKey=null;
               }else{
                   map.put(args[i],null);
               }
           }
       }
       if(lastKey!=null)
           map.put(lastKey, null);
       return map;
    }
    
}
