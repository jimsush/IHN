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
 * Description: thread��Э��API(��������-������ģʽ��)
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public interface CollaborationService {

    /**
     * ���һ��������-������ģ��,��Ӻ����������ɷ��߳�
     * @param producerConsumerName
     * @param queueCapacity
     * @param threadPoolName �̳߳�
     * @return ProducerConsumer ʹ�øö�����У�push,ע��listener�Ĵ���
     */
    public ProducerConsumer addProducerConsumer(String producerConsumerName,int queueCapacity,String threadPoolName);
    
    /**
     * ���һ��������-������ģ��,��Ӻ����������ɷ��̣߳�ʹ��ȱʡ���̳߳أ�
     * @param producerConsumerName
     * @param queueCapacity
     * @return ProducerConsumer ʹ�øö�����У�push,ע��listener�Ĵ���
     */
    public ProducerConsumer addProducerConsumer(String producerConsumerName,int queueCapacity);
    
    /**
     * ɾ��������-������ģ��
     * @param producerConsumerName
     */
    public void removeProducerConsumer(String producerConsumerName);
    
    /**
     * ͬ���ȴ�ĳ���������,���ȴ�timeoutMill����
     * <br>ͬ���ȴ���ָ�ڵ�ǰ�̴߳���ִ��
     * <br>ִ�и÷�����,��ǰ�߳�����,֪��resultHandler����������timeout�Ż����ִ��
     * @param resultHandler �û�ʵ�ֵĽ��������
     * @param checkInterval ���ڼ�����ļ��������
     * @param timeoutMillSec ����
     * @return ִ�еĽ��״̬
     */
    public ResultStauts syncWait(ResultHandler resultHandler,long checkInterval,long timeoutMillSec);
    
    /**
     * �첽�ȴ�ĳ��ִ�н���(ʹ��concurrent��futureģʽ)
     * <br>ִ�е�����������һ��thread(work thread),��work threadִ�н�����᷵��
     * @param future ִ�е�����,ͨ��ExecuteService.submit���ص�Future
     * @param timeoutMillSec ����
     * @return ����ִ�еĽ������Future��Ӧ��Callable�й�
     */
    public Object asyncWait(Future<Object> future,long timeoutMillSec);
    
}
