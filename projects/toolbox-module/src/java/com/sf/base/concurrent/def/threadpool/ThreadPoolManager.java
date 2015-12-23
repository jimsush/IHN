package com.sf.base.concurrent.def.threadpool;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * <p>
 * Title: ThreadPoolManager
 * </p>
 * <p>
 * Description: �̳߳ع���ӿ�
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public interface ThreadPoolManager {
    
    /**
     * ��ȡ�̳߳�
     * @param poolName eg:polltask,batchworker,���û�����
     * @return ���poolName�����ڣ��᷵��һ��Ĭ�ϵ��̳߳�
     */
    public ExecutorService getThreadPool(String poolName);
    
    /**
     * ��ȡȱʡ���̳߳�����
     * @return �̳߳���
     */
    public List<String> getDefaultPoolNames();
    
    /**
     * �����µ��̳߳�
     * @param poolName �̳߳����ƣ��ظ������쳣
     * @param threadNum ���̳߳ع̶����߳���
     */
    public void addThreadPool(String poolName,Integer threadNum);
    
    /**
     * ɾ��һ���̳߳�,���������ύ�µ�ִ������,�����pool��thread��ִ�У���threadִ�н�����thread pool�ͷ���Դ
     * @param poolName �̳߳���
     */
    public void removeThreadPool(String poolName);

}
