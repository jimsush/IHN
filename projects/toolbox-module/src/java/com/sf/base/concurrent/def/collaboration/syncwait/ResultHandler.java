package com.sf.base.concurrent.def.collaboration.syncwait;


/**
 * <p>
 * Title: ResultHandler
 * </p>
 * <p>
 * Description: ��Ҫ����ʵ�����࣬ר��������ȡ��������жϽ���Ƿ����
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public interface ResultHandler {

    /**
     * �������󣬱�������ݿ��ȡһ����¼���·����õ��豸�ȣ������ؽ��
     * @return
     */
    public ResultStauts getSingleResult();
    
    /**
     * �Ժ����������첽�����ⲿ����Ϣ����
     * @param data
     */
    public void postData(Object data);
    
}
