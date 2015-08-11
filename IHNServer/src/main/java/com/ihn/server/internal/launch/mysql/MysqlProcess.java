package com.ihn.server.internal.launch.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import com.ihn.server.internal.launch.BizContext;
import com.ihn.server.internal.launch.service.ExternalService;
import com.ihn.server.util.process.RunCmdService;
import com.mysql.jdbc.CommunicationsException;



public class MysqlProcess implements ExternalService{

    @Override
    public String getServiceName() {
        return "MYSQL";
    }
    
    @Override
    public boolean checkIsRun() {
        return checkDB(7788,"root","",new ArrayList<Exception>());
    }
    
    private boolean checkDB(int dbPort,String userName,String password,List<Exception> exceptionList) {
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url ="jdbc:mysql://localhost:"+dbPort+"/ihn?user="+userName+"&password="+password+"&useUnicode=true&characterEncoding=8859_1";
            Connection ihnConn = DriverManager.getConnection(url);
            if(ihnConn==null){
                return false;
            }else{
            	ihnConn.close(); 
                return true;
            }
        }catch(CommunicationsException ex1){
            return false;
        }catch(Exception ex){
            exceptionList.add(ex);
            return false;
        }
    }

    @Override
    public boolean isInited() {
        return true;
    }

    @Override
    public void init() {
    }

    @Override
    public void start() {
        RunCmdService service=new RunCmdService();
        service.setStartupMode(RunCmdService.OUTSIDE_THREAD);
        
        String cmd=BizContext.runHomeDir+"mysql/run-mysql.bat";
        service.setStartCmdLine(cmd);
        service.startService();
    }

    @Override
    public void stop() {
        RunCmdService service=new RunCmdService();
        service.setStartupMode(RunCmdService.OUTSIDE_THREAD);
        
        String cmd=BizContext.runHomeDir+"mysql/shutdown-mysql.bat";
        service.setStartCmdLine(cmd);
        service.startService();
    }

}
