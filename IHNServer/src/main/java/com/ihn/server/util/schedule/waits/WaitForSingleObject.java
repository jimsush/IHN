package com.ihn.server.util.schedule.waits;


public class WaitForSingleObject {
    
    private long interval=3000;
    
    private ResultHandler handler;
    
    public WaitForSingleObject(ResultHandler handler){
        this.handler=handler;
    }
    
    public void setInterval(long interval) {
        this.interval = interval;
    }
    
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
                System.out.println("in WaitForSingleObject,getSingleResult"+ex);
            }
            
            try {
                Thread.sleep(interval);
            } catch (Exception x) {}
        }
    }

}
