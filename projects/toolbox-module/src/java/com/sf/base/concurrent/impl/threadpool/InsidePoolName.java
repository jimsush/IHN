package com.sf.base.concurrent.impl.threadpool;

/**
 * <p>
 * Title: InsidePoolName
 * </p>
 * <p>
 * Description: 缺省的几个线程池名称
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public class InsidePoolName {

    /**
     * 缺省pool,一般临时用一下就释放
     */
    public static final String POOL_NAME_DEFAULT="default";
    
    /**
     * 定期轮询任务
     */
    public static final String POOL_NAME_POLLTASK="polltask";
    
    /**
     * 批量处理的pool
     */
    public static final String POOL_NAME_BATCH="batch";
    
}
