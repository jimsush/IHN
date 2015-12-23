package com.sf.base.concurrent.def.collaboration.syncwait;


/**
 * <p>
 * Title: ResultHandler
 * </p>
 * <p>
 * Description: 需要各自实现子类，专门用来获取结果，并判断结果是否结束
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public interface ResultHandler {

    /**
     * 处理请求，比如从数据库获取一条记录，下发配置到设备等，并返回结果
     * @return
     */
    public ResultStauts getSingleResult();
    
    /**
     * 以后用来进行异步接受外部的消息数据
     * @param data
     */
    public void postData(Object data);
    
}
