
package com.sf.core.bootstrap.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * Title: ConfigExpress
 * </p>
 * <p>
 * Description:����ģ������xml�ļ��еı��ʽ
 * </p>
 * 
 * @author sufeng
 * created 2011-3-1 ����12:32:52
 * modified [who date description]
 * check [who date description]
 */
public class ConfigExpress {
    
    /** 
     * list,map��һ���ָ��
     */
    public final static String SEP_FIELD=",";
    
    public final static String MAP_START="[";
    
    /**
     * map���key-value�ָ�
     */
    public final static String SEP_MAP="=";
    
    public final static String MAP_END="]";
    
    /**
     * map���value�ָ�
     */
    public final static String SEP_MAP_VALUE=";";
    
    /**
     * ������xml�ļ��е��ַ���ת��Ϊlist
     * @param expressInConfig
     * @return
     */
    public static List<String> express2List(String expressInConfig){
        List<String> list=new ArrayList<String>();
        if(StringUtils.isEmpty(expressInConfig))
            return list;
        String[] fields = expressInConfig.split(SEP_FIELD);
        if(fields==null || fields.length==0)
            return list;
        for(int i=0; i<fields.length; i++){
            if(StringUtils.isNotEmpty(fields[i]))
                list.add(fields[i]);
        }
        return list;
    }
    
    /**
     * ������xml�ļ��е��ַ���ת��Ϊmap
     * @param expressInConfig
     * @return
     */
    public static Map<String,String> express2Map(String expressInConfig){
        Map<String,String> map=new HashMap<String,String>();
        if(StringUtils.isEmpty(expressInConfig))
            return map;
        String[] fields = expressInConfig.split(SEP_FIELD);
        if(fields==null || fields.length==0)
            return map;
            
        for(int i=0; i<fields.length; i++){
            // ����[]��; a=, a=p1, a=[] a=[p1] a=[p1;] a=[p1;p2]
            if(StringUtils.isEmpty(fields[i]))
                continue;
            String[] param = fields[i].split(SEP_MAP);
            if(param==null || param.length==0)
                continue;
            if(param.length==1 && StringUtils.isNotEmpty(param[0])){
                //map.put(param[0], null);
                continue;
            }else if(param.length==2){
                if(param[1].startsWith(MAP_START) && param[1].endsWith(MAP_END)){
                    String content=param[1].substring(1,param[1].length()-1);
                    String[] vals = content.split(SEP_MAP_VALUE);
                    if(vals!=null && vals.length>0){
                        for(int k=0; k<vals.length; k++){
                            if(StringUtils.isNotEmpty(vals[k])){
                                map.put(vals[k],param[0]);
                            }
                        }
                    }
                }else{
                    map.put(param[1], param[0]);
                }
            }
        }
        return map;
    }

}
