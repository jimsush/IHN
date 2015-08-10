package com.ihn.server.internal.launch;

import com.ihn.server.util.schedule.FixedIntervalSchedulerJob;

public class CheckServerStopFlagJob  extends FixedIntervalSchedulerJob{

    public CheckServerStopFlagJob() {
        super(null,null,5);
    }
    
    @Override
    public void execute() {
        if(BizContext.isServerStopped()){
            StopServer stopServer=new StopServer();
            stopServer.execute(null);
            System.exit(0);
        }
    }

}
