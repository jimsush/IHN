package com.sf.base.concurrent.def.collaboration.producerconsumer;

import java.util.Collection;

/**
 * <p>
 * Title: ConsumerListener
 * </p>
 * <p>
 * Description: 消费者处理
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public interface ConsumerListener {

    /**
     * 消费者对其进行处理
     * @param objsInQueue 生产者传递过来的数据
     */
    public void handle(Collection<Object> objsInQueue);
    
}
