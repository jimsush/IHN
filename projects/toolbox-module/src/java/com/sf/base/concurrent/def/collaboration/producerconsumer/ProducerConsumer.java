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
 * Description: ������-������ģ��,����1:1��ģ�ͽ��д���1��������-������Ⱥ,ʹ��1��thread�ɷ������ߵĴ���
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public class ProducerConsumer implements Runnable{

    /**
     * Ψһ��ʶ
     */
    private String name;
    
    private volatile boolean stopFlag=false;

    /**
     * ������,�����߼�Ĺ������
     */
    @SuppressWarnings("unchecked")
    private BlockingQueue queue=null;
    
    /**
     * ������listener
     */
    private List<ConsumerListener> consumberListeners=new ArrayList<ConsumerListener>();
    
    /**
     * 
     * @param name ��ʶ
     * @param queueCapacity ��������
     */
    @SuppressWarnings("unchecked")
    public ProducerConsumer(String name,int queueCapacity){
        this.name=name;
        queue=new ArrayBlockingQueue(queueCapacity);
    }
    
    /**
     * �����ߵ��ã�����һ�������߼�����
     * @param listener
     */
    public void addConsumerListener(ConsumerListener listener){
        consumberListeners.add(listener);
    }
    
    /**
     * �����ߵ��ã�ɾ�������߼�����
     * @param listener
     */
    public void removeConsumerListener(ConsumerListener listener){
        consumberListeners.remove(listener);
    }
    
    /**
     * �����ߵ��ã�����һ�����ݣ����������
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
     * ֹͣ���������Ѵ���
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
                Object first = queue.take(); //��������ʽȡһ��
                list.add(first);
                queue.drainTo(list);  // ����������ж��,���ʣ�µ�Ҳ��ȡ����
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
