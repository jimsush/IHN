package com.sf.base.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title: SfBatchException
 * </p>
 * <p>
 * Description: �����������쳣�࣬������ܰ����˶���쳣
 * </p>
 * 
 * created 2015-11-24 ����07:21:47
 * modified [who date description]
 * check [who date description]
 */
public class SfBatchException extends RuntimeException {
    
    private static final long serialVersionUID = 474485532847499330L;

    /**
     * �쳣�Ķ���Դ
     */
    private List<Object> keys=new ArrayList<Object>(); // ͨ��list����˳����
    
    /**
     * �쳣�б������Դһһ��Ӧ
     */
    private Map<Object, Throwable> throwables=new HashMap<Object, Throwable>();

    /**
     * �������쳣�б��м�һ���쳣
     * @param key ��
     * @param th  �쳣
     */
    public void addException(Object key,Throwable th){
        keys.add(key);
        throwables.put(key, th);
    }
    
    /**
     * �õ������쳣�еļ�
     * @return
     */
    public List<Object> getKeys() {
        return keys;
    }

    /**
     * �õ�ĳ���쳣
     * @param key
     * @return
     */
    public Throwable getThrowable(Object key){
        return throwables.get(key);
    }
    
    /**
     * �õ������쳣
     * @return
     */
    public Map<Object, Throwable> getThrowables() {
        // ����һ�ݣ���ֹ�������ʵ��µ�����
        Map<Object, Throwable> result=new HashMap<Object, Throwable>();
        result.putAll(throwables);
        return result;
    }
    
}
