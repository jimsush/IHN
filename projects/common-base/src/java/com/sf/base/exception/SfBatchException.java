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
 * Description: 批量操作的异常类，里面可能包含了多个异常
 * </p>
 * 
 * created 2015-11-24 下午07:21:47
 * modified [who date description]
 * check [who date description]
 */
public class SfBatchException extends RuntimeException {
    
    private static final long serialVersionUID = 474485532847499330L;

    /**
     * 异常的对象源
     */
    private List<Object> keys=new ArrayList<Object>(); // 通过list保持顺序性
    
    /**
     * 异常列表，与对象源一一对应
     */
    private Map<Object, Throwable> throwables=new HashMap<Object, Throwable>();

    /**
     * 往批量异常列表中加一条异常
     * @param key 键
     * @param th  异常
     */
    public void addException(Object key,Throwable th){
        keys.add(key);
        throwables.put(key, th);
    }
    
    /**
     * 得到批量异常中的键
     * @return
     */
    public List<Object> getKeys() {
        return keys;
    }

    /**
     * 得到某条异常
     * @param key
     * @return
     */
    public Throwable getThrowable(Object key){
        return throwables.get(key);
    }
    
    /**
     * 得到所有异常
     * @return
     */
    public Map<Object, Throwable> getThrowables() {
        // 拷贝一份，防止并发访问导致的问题
        Map<Object, Throwable> result=new HashMap<Object, Throwable>();
        result.putAll(throwables);
        return result;
    }
    
}
