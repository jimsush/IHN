package com.ihn.server.internal.launch;

import com.ihn.server.internal.launch.service.BaseService;
import com.ihn.server.internal.launch.service.ExternalProcessMgr;
import com.ihn.server.util.process.ProcessExecute;

public class StopServer implements ProcessExecute {

    public void execute(String[] args) {
        extendStop();
        
        BaseService externalProcessMgr=new ExternalProcessMgr();
        externalProcessMgr.destroy();
    }
    
    public void extendStop(){
        return;
    }
    
}
