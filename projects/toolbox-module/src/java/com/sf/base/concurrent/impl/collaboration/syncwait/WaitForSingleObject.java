package com.sf.base.concurrent.impl.collaboration.syncwait;

import com.sf.base.concurrent.def.collaboration.syncwait.ResultHandler;
import com.sf.base.concurrent.def.collaboration.syncwait.ResultStauts;
import com.sf.base.exception.ExceptionUtils;

/**
 * <p>
 * Title: WaitForSingleObject
 * </p>
 * <p>
 * Description: �ȴ�ĳ�������������IPC�е�WaitForSingleObject����
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public class WaitForSingleObject {
    
    /**
     * ִ�еļ��������
     */
    private long interval=3000;
    
    private ResultHandler handler;
    
    public WaitForSingleObject(ResultHandler handler){
        this.handler=handler;
    }
    public void setInterval(long interval) {
        this.interval = interval;
    }
    
    /**
     * �ȴ����ؽ��
     * @param timeout ��ʱʱ��,������ 0��ʾ�ȴ����������,����timeout
     * @return
     */
    public ResultStauts get(long timeout){
        long startTime=System.currentTimeMillis();
        for(;;){
            long now = System.currentTimeMillis();
            if (timeout > 0) {
                long waitTime = now - startTime;
                if (waitTime >= timeout) // ��ʱ��
                    return new ResultStauts(true,true);
            }
            try {
                ResultStauts status = handler.getSingleResult();
                if (status.isFinished())
                    return status;
            } catch (Exception ex) {
                System.out.println(ExceptionUtils.getCommonExceptionInfo(ex));
            }
            
            try {
                Thread.sleep(interval);
            } catch (Exception x) {
                System.out.println(ExceptionUtils.getCommonExceptionInfo(x));
            }
        }
    }

}
