package com.sf.base.concurrent.def.collaboration.producerconsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;



/**
 * <p>
 * Title: ProducerConsumer
 * </p>
 * <p>
 * Description: 生产者-消费者模型,采用1:1的模型进行处理（1个生产者-消费者群,使用1个thread派发消费者的处理）
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public class ProducerConsumer implements Runnable{

    /**
     * 唯一标识
     */
    private String name;
    
    private volatile boolean stopFlag=false;

    /**
     * 消费者,生产者间的共享队列
     */
    @SuppressWarnings("unchecked")
    private BlockingQueue queue=null;
    
    /**
     * 消费者listener
     */
    private List<ConsumerListener> consumberListeners=new ArrayList<ConsumerListener>();
    
    /**
     * 
     * @param name 标识
     * @param queueCapacity 队列容量
     */
    @SuppressWarnings("unchecked")
    public ProducerConsumer(String name,int queueCapacity){
        this.name=name;
        queue=new ArrayBlockingQueue(queueCapacity);
    }
    
    /**
     * 消费者调用：增加一个消费者监听者
     * @param listener
     */
    public void addConsumerListener(ConsumerListener listener){
        consumberListeners.add(listener);
    }
    
    /**
     * 消费者调用：删除消费者监听者
     * @param listener
     */
    public void removeConsumerListener(ConsumerListener listener){
        consumberListeners.remove(listener);
    }
    
    /**
     * 生产者调用：产生一个数据，放入队列中
     * @param obj
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean produce(Object obj){
        if(obj==null)
            return false;
        return queue.offer(obj);
    }
    
    /**
     * 停止产生、消费处理
     */
    public synchronized void stopDispatch(){
        stopFlag=true;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        List<Object> list=new ArrayList<Object>();
        while(!stopFlag){
            try{
                list.clear();
                Object first = queue.take(); //先阻塞方式取一个
                list.add(first);
                queue.drainTo(list);  // 如果队列中有多个,则把剩下的也获取出来
            }catch(Exception e){
                System.err.println(e);
            }
            
            for(ConsumerListener listener : consumberListeners){
                try{
                    listener.handle(list);
                }catch(Exception ex){
                    System.err.println(ex);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "name="+name+",queue size="+queue.size();
    }
    
}
