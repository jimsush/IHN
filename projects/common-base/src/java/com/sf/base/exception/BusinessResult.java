package com.sf.base.exception;


/**
 * <p>
 * Title: BusinessResult
 * </p>
 * <p>
 * Description: 业务处理的结果，包括异常和参数
 * </p>
 * 
 * @author sufeng
 * modified [who date description]
 * check [who date description]
 */
public class BusinessResult {
    
    /**
     * 异常信息
     */
    private Throwable th;
    
    /**
     * 当前步骤执行产生的结果
     */
    private Object[] objs;
    
    public BusinessResult(Throwable th,Object ... objs){
        this.th=th;
        this.objs=objs;
    }
    
    /**
     * 异常信息
     * @return
     */
    public Throwable getTh() {
        return th;
    }
    
    /**
     * 当前步骤执行产生的结果
     * @return
     */
    public Object[] getObjs() {
        return objs;
    }

}
