package com.ihn.server.internal.launch.mysql;

public interface ExternalService {
    
    public String getServiceName();
    
    public boolean checkIsRun();
    
    public void start();
    
    public boolean isInited();
    
    public void init();
    
    public void stop();
    

}
