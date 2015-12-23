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
 * Description: ������������-������ģ��
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
     * ���һ��������-������ģ��
     * @param producerConsumerName 
     * @param queueCapacity ������д�С
     * @return ProducerConsumer ʹ�øö�����У�push,ע��listener�Ĵ���
     */
    public ProducerConsumer addProducerConsumer(String producerConsumerName,int queueCapacity){
        return addProducerConsumer(producerConsumerName,queueCapacity,InsidePoolName.POOL_NAME_POLLTASK);
    }
    
    /**
     * ���һ��������-������ģ��
     * @param producerConsumerName
     * @param queueCapacity  ������д�С
     * @param threadPoolName ָ�����̳߳�����
     * @return ʹ�øö�����У�push,ע��listener�Ĵ���
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
     * ɾ��������-������ģ��
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
