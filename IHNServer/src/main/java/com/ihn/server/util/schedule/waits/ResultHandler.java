package com.ihn.server.util.schedule.waits;


public interface ResultHandler {
    
    public ResultStauts getSingleResult();
    
    public void postData(Object data);
    
}
