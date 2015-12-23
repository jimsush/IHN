package com.sf.base.concurrent.impl.collaboration;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.sf.base.concurrent.def.collaboration.CollaborationService;
import com.sf.base.concurrent.def.collaboration.producerconsumer.ProducerConsumer;
import com.sf.base.concurrent.def.collaboration.syncwait.ResultHandler;
import com.sf.base.concurrent.def.collaboration.syncwait.ResultStauts;
import com.sf.base.concurrent.impl.collaboration.producerconsumer.ProducerConsumerManager;
import com.sf.base.concurrent.impl.collaboration.syncwait.WaitForSingleObject;


/**
 * <p>
 * Title: CollaborationServiceImpl
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public class CollaborationServiceImpl implements CollaborationService{

    /**
     * 生产者-消费者管理器
     */
    private ProducerConsumerManager producerConsumerManager;
    public CollaborationServiceImpl(ProducerConsumerManager producerConsumerManager) {
        this.producerConsumerManager = producerConsumerManager;
    }
    
    @Override
    public ProducerConsumer addProducerConsumer(String producerConsumerName, int queueCapacity, String threadPoolName) {
        return producerConsumerManager.addProducerConsumer(producerConsumerName, queueCapacity,threadPoolName);
    }

    @Override
    public ProducerConsumer addProducerConsumer(String producerConsumerName, int queueCapacity) {
        return producerConsumerManager.addProducerConsumer(producerConsumerName, queueCapacity);
    }

    @Override
    public void removeProducerConsumer(String producerConsumerName) {
        producerConsumerManager.removeProducerConsumer(producerConsumerName);
    }

    @Override
    public ResultStauts syncWait(ResultHandler resultHandler,long checkInterval,long timeoutMillSec) {
        WaitForSingleObject waitObject=new WaitForSingleObject(resultHandler);
        waitObject.setInterval(checkInterval);
        ResultStauts resStatus = waitObject.get(timeoutMillSec);
        return resStatus;
    }

    @Override
    public Object asyncWait(Future<Object> future, long timeoutMillSec) {
        try{
            return future.get(timeoutMillSec, TimeUnit.MILLISECONDS);
        }catch(Exception e){
            System.err.println(e);
        }
        return null;
    }
    
}
