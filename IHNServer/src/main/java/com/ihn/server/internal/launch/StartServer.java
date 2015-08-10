package com.ihn.server.internal.launch;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ihn.server.internal.launch.mysql.ExternalProcessMgr;
import com.ihn.server.internal.launch.mysql.ExternalService;
import com.ihn.server.internal.launch.mysql.MysqlProcess;
import com.ihn.server.internal.service.BizContext;
import com.ihn.server.util.SysUtils;
import com.ihn.server.util.logging.IHNLogFactory;
import com.ihn.server.util.process.CheckServerStopFlagJob;
import com.ihn.server.util.process.ProcessExecute;
import com.ihn.server.util.schedule.SchedulerExecutor;

public class StartServer implements ProcessExecute{

    public static void main(String[] args) {
        new StartServer().execute(args);
    }
    
    @Override
    public void execute(String[] args) {
        String userDir = System.getProperty("user.dir");
        if(userDir.endsWith("IHNServer")){
            BizContext.runHomeDir=userDir+"/support/";
        }else{ 
            BizContext.runHomeDir=userDir+"/../";
        }
        
        beforeStart();
        
        // start mysql
        boolean needWait=startServiceProcess();
        if(needWait){
            SysUtils.sleepNotException(5000);
        }
        
        ApplicationContext ctx=new ClassPathXmlApplicationContext(
                new String[]{"classpath:applicationContext.xml"});
        BizContext.setCtx(ctx);
        
        IHNLogFactory logFactory=(IHNLogFactory)ctx.getBean("ihnLogFactory");
        Logger logger=logFactory.getLogger("server");
        BizContext.setLogger(logger);
        
        // start others
        afterStart();
        
        // start stop check job
        CheckServerStopFlagJob job=new CheckServerStopFlagJob();
        SchedulerExecutor.getInstance().startScheduleJob(job);
    }
    
    /**
     * 供扩展
     */
    public void beforeStart(){
        
    }
    
    /**
     * 供扩展
     */
    public void afterStart(){
        //MessageService messageService=(MessageService)BizContext.getBean("messageService");
        //messageService.sendMessage("pmTopic", new BaseMessage(""));
        return;
    }
    
    private boolean startServiceProcess(){
        ExternalProcessMgr externalProcessMgr=new ExternalProcessMgr();
        List<ExternalService> externalServices=new ArrayList<ExternalService>();
        externalProcessMgr.setExternalServices(externalServices);
        
        MysqlProcess mysqld=new MysqlProcess();
        externalServices.add(mysqld);
        
        return externalProcessMgr.init();
    }
    
}