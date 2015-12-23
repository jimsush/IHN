package com.sf.base.exception;


/**
 * <p>
 * Title: BusinessResult
 * </p>
 * <p>
 * Description: ҵ����Ľ���������쳣�Ͳ���
 * </p>
 * 
 * @author sufeng
 * modified [who date description]
 * check [who date description]
 */
public class BusinessResult {
    
    /**
     * �쳣��Ϣ
     */
    private Throwable th;
    
    /**
     * ��ǰ����ִ�в����Ľ��
     */
    private Object[] objs;
    
    public BusinessResult(Throwable th,Object ... objs){
        this.th=th;
        this.objs=objs;
    }
    
    /**
     * �쳣��Ϣ
     * @return
     */
    public Throwable getTh() {
        return th;
    }
    
    /**
     * ��ǰ����ִ�в����Ľ��
     * @return
     */
    public Object[] getObjs() {
        return objs;
    }

}
