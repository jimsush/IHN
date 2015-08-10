package com.ihn.server.internal.launch.mysql;

import java.util.List;

//import org.apache.commons.collections.CollectionUtils;


import org.springframework.util.CollectionUtils;

import com.ihn.server.internal.launch.service.BaseService;
import com.ihn.server.internal.service.BizContext;


public class ExternalProcessMgr implements BaseService{
    
    private List<ExternalService> externalServices;
    public void setExternalServices(List<ExternalService> externalServices) {
        this.externalServices = externalServices;
    }
    
    @Override
    public boolean init() {
    	boolean needWait=false;
        if(CollectionUtils.isEmpty(externalServices))
            return needWait;
        
        for(ExternalService service : externalServices){
            BizContext.getLogger().info("will start "+service.getServiceName());
            try{
                if(!service.checkIsRun()){
                	needWait=true;
                    service.start();
                    BizContext.getLogger().info(service.getServiceName()+" start finished.");
                }else{
                    BizContext.getLogger().info(service.getServiceName()+" have running.");
                }
                
                if(!service.isInited()){
                	needWait=true;
                    service.init();
                    BizContext.getLogger().info(service.getServiceName()+" init finished.");
                }else{
                    BizContext.getLogger().info(service.getServiceName()+" have inited.");
                }
                
                BizContext.getLogger().info(service.getServiceName()+" start ok!");
            }catch(Exception ex){
                BizContext.getLogger().warn("",ex);
            }
        }
        
        BizContext.getLogger().info("External process finished.");
        return needWait;
    }
    
    @Override
    public void destroy() {
        if(CollectionUtils.isEmpty(externalServices))
            return;
        for(ExternalService service : externalServices){
            try{
                service.stop();
            }catch(Exception ex){
                BizContext.getLogger().warn("",ex);
            }
        }
    }


    
    
    
    
}
