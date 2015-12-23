package com.sf.base.concurrent.impl.threadpool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.collections.MapUtils;

import com.sf.base.concurrent.def.threadpool.ThreadPoolManager;

/**
 * <p>
 * Title: ThreadPoolManagerImpl
 * </p>
 * <p>
 * Description:线程池管理实现类(会配置为bean)
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public class ThreadPoolManagerImpl implements ThreadPoolManager {

    public final static int MAX_THREAD_NUM=300;
    private int totalThreadNum=0;
    
    private Map<String,ExecutorService> threadPoolFactory=new ConcurrentHashMap<String,ExecutorService>();
    private Map<String,Integer> threadPoolThreadNum=new ConcurrentHashMap<String, Integer>();
    private List<String> defaultPoolNames=new ArrayList<String>();
    
    /**
     * 用户初始化的线程池:key poolName, value为线程数量
     * @param initThreadPoolConfig
     */
    public void setInitThreadPoolConfig(Map<String, Integer> initThreadPoolConfig) {
        if(MapUtils.isEmpty(initThreadPoolConfig))
            return;
        for(Map.Entry<String, Integer> entry : initThreadPoolConfig.entrySet()){
            String poolName=entry.getKey();
            Integer threadNum=entry.getValue();
            addThreadPool(poolName,threadNum);
        }
    }
    
    public ThreadPoolManagerImpl(){
        addThreadPool(InsidePoolName.POOL_NAME_DEFAULT,5);
        defaultPoolNames.add(InsidePoolName.POOL_NAME_DEFAULT);
        
        addThreadPool(InsidePoolName.POOL_NAME_BATCH,5);
        defaultPoolNames.add(InsidePoolName.POOL_NAME_BATCH);
        
        addThreadPool(InsidePoolName.POOL_NAME_POLLTASK,10);
        defaultPoolNames.add(InsidePoolName.POOL_NAME_POLLTASK);
    }
    
    /**
     * 销毁
     */
    public void destroy() {
        for(Iterator<String> it=threadPoolFactory.keySet().iterator();it.hasNext();){
            String poolName=it.next();
            removeThreadPool(poolName);
        }
    }

    @Override
    public ExecutorService getThreadPool(String poolName) {
        ExecutorService threadPool = threadPoolFactory.get(poolName);
        if(threadPool==null)
            threadPool=threadPoolFactory.get(InsidePoolName.POOL_NAME_DEFAULT);
        return threadPool;
    }

    @Override
    public void addThreadPool(String poolName, Integer threadNum) {
        if(threadPoolFactory.containsKey(poolName))
            throw new RuntimeException("the pool name has existed in system");
        
        int tempNum=totalThreadNum+threadNum;
        if(tempNum>=MAX_THREAD_NUM)
            throw new RuntimeException("too many threads in this system,the limit is "+MAX_THREAD_NUM+",current thread num is "+totalThreadNum);
        
        ExecutorService threadPool=Executors.newFixedThreadPool(threadNum);
        threadPoolFactory.put(poolName, threadPool);
        
        threadPoolThreadNum.put(poolName, threadNum);
        totalThreadNum=tempNum;
    }

    @Override
    public void removeThreadPool(String poolName) {
        ExecutorService threadPool = getThreadPool(poolName);
        if(threadPool!=null){
            threadPool.shutdown(); //只禁止提交新任务shutdownNow会终止已提交的任务
            threadPoolFactory.remove(poolName);
            
            Integer num = threadPoolThreadNum.get(poolName);
            totalThreadNum-=num;
            threadPoolThreadNum.remove(poolName);
        }
    }
    
    @Override
    public List<String> getDefaultPoolNames() {
        return defaultPoolNames;
    }

}
