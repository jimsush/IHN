package com.sf.base.concurrent.def.collaboration.producerconsumer;

import java.util.Collection;

/**
 * <p>
 * Title: ConsumerListener
 * </p>
 * <p>
 * Description: �����ߴ���
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public interface ConsumerListener {

    /**
     * �����߶�����д���
     * @param objsInQueue �����ߴ��ݹ���������
     */
    public void handle(Collection<Object> objsInQueue);
    
}
