package com.sf.base.concurrent.def.collaboration;

import java.util.concurrent.Future;

import com.sf.base.concurrent.def.collaboration.producerconsumer.ProducerConsumer;
import com.sf.base.concurrent.def.collaboration.syncwait.ResultHandler;
import com.sf.base.concurrent.def.collaboration.syncwait.ResultStauts;

/**
 * <p>
 * Title: CollaborationService
 * </p>
 * <p>
 * Description: thread间协作API(比如消费-生产者模式等)
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public interface CollaborationService {

    /**
     * 添加一个生产者-消费者模型,添加后立即启动派发线程
     * @param producerConsumerName
     * @param queueCapacity
     * @param threadPoolName 线程池
     * @return ProducerConsumer 使用该对象进行：push,注册listener的处理
     */
    public ProducerConsumer addProducerConsumer(String producerConsumerName,int queueCapacity,String threadPoolName);
    
    /**
     * 添加一个生产者-消费者模型,添加后立即启动派发线程（使用缺省的线程池）
     * @param producerConsumerName
     * @param queueCapacity
     * @return ProducerConsumer 使用该对象进行：push,注册listener的处理
     */
    public ProducerConsumer addProducerConsumer(String producerConsumerName,int queueCapacity);
    
    /**
     * 删除生产者-消费者模型
     * @param producerConsumerName
     */
    public void removeProducerConsumer(String producerConsumerName);
    
    /**
     * 同步等待某个结果返回,最多等待timeoutMill毫秒
     * <br>同步等待是指在当前线程串行执行
     * <br>执行该方法后,当前线程阻塞,知道resultHandler处理结果或者timeout才会继续执行
     * @param resultHandler 用户实现的结果处理器
     * @param checkInterval 定期检查结果的间隔，毫秒
     * @param timeoutMillSec 毫秒
     * @return 执行的结果状态
     */
    public ResultStauts syncWait(ResultHandler resultHandler,long checkInterval,long timeoutMillSec);
    
    /**
     * 异步等待某个执行结束(使用concurrent的future模式)
     * <br>执行的任务在另外一个thread(work thread),当work thread执行结束后会返回
     * @param future 执行的任务,通过ExecuteService.submit返回的Future
     * @param timeoutMillSec 毫秒
     * @return 任务执行的结果，与Future对应的Callable有关
     */
    public Object asyncWait(Future<Object> future,long timeoutMillSec);
    
}
