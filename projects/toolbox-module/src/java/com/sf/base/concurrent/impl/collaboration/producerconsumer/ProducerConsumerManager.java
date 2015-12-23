package com.sf.base.concurrent.impl.collaboration.producerconsumer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sf.base.concurrent.def.collaboration.producerconsumer.ProducerConsumer;
import com.sf.base.concurrent.def.threadpool.ThreadPoolManager;
import com.sf.base.concurrent.impl.threadpool.InsidePoolName;


/**
 * <p>
 * Title: ProducerConsumberManager
 * </p>
 * <p>
 * Description: 管理多个生产者-消费者模型
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public class ProducerConsumerManager {

    private Map<String,ProducerConsumer> allProducerConsumbers=new ConcurrentHashMap<String,ProducerConsumer>();

    private ThreadPoolManager threadPoolManager;
    
    public void setThreadPoolManager(ThreadPoolManager threadPoolManager) {
        this.threadPoolManager = threadPoolManager;
    }
    
    /**
     * 添加一个生产者-消费者模型
     * @param producerConsumerName 
     * @param queueCapacity 共享队列大小
     * @return ProducerConsumer 使用该对象进行：push,注册listener的处理
     */
    public ProducerConsumer addProducerConsumer(String producerConsumerName,int queueCapacity){
        return addProducerConsumer(producerConsumerName,queueCapacity,InsidePoolName.POOL_NAME_POLLTASK);
    }
    
    /**
     * 添加一个生产者-消费者模型
     * @param producerConsumerName
     * @param queueCapacity  共享队列大小
     * @param threadPoolName 指定的线程池名称
     * @return 使用该对象进行：push,注册listener的处理
     */
    public ProducerConsumer addProducerConsumer(String producerConsumerName,int queueCapacity,String threadPoolName){
        if(allProducerConsumbers.containsKey(producerConsumerName)){
            return null;
        }
        ProducerConsumer pc=new ProducerConsumer(producerConsumerName,queueCapacity);
        allProducerConsumbers.put(producerConsumerName, pc);
        
        threadPoolManager.getThreadPool(threadPoolName).execute(pc);
        return pc;
    }
    
    /**
     * 删除生产者-消费者模型
     * @param producerConsumerName
     */
    public void removeProducerConsumer(String producerConsumerName){
        ProducerConsumer pc = allProducerConsumbers.remove(producerConsumerName);
        if(pc==null){
            return;
        }
        pc.stopDispatch();
    }
    
}
