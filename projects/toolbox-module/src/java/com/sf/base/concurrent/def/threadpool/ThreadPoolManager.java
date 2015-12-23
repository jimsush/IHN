package com.sf.base.concurrent.def.threadpool;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * <p>
 * Title: ThreadPoolManager
 * </p>
 * <p>
 * Description: 线程池管理接口
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public interface ThreadPoolManager {
    
    /**
     * 获取线程池
     * @param poolName eg:polltask,batchworker,由用户定义
     * @return 如果poolName不存在，会返回一个默认的线程池
     */
    public ExecutorService getThreadPool(String poolName);
    
    /**
     * 获取缺省的线程池名称
     * @return 线程池名
     */
    public List<String> getDefaultPoolNames();
    
    /**
     * 创建新的线程池
     * @param poolName 线程池名称，重复会抛异常
     * @param threadNum 该线程池固定的线程数
     */
    public void addThreadPool(String poolName,Integer threadNum);
    
    /**
     * 删除一个线程池,不允许其提交新的执行任务,如果该pool有thread在执行，则thread执行结束后thread pool释放资源
     * @param poolName 线程池名
     */
    public void removeThreadPool(String poolName);

}
