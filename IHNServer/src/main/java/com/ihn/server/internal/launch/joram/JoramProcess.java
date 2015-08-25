
package com.ihn.server.internal.launch.joram;



import org.apache.commons.lang.StringUtils;

import com.ihn.server.internal.launch.service.ExternalService;

/*
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Properties;
import org.objectweb.joram.client.jms.Topic;
import org.objectweb.joram.client.jms.admin.AdminModule;
import org.objectweb.joram.client.jms.admin.User;
import org.objectweb.joram.client.jms.tcp.QueueTcpConnectionFactory;
import org.objectweb.joram.client.jms.tcp.TcpConnectionFactory;
import org.objectweb.joram.client.jms.tcp.TopicTcpConnectionFactory;

import org.slf4j.Logger;

import com.ihn.server.internal.launch.BizContext;

import com.ihn.server.util.SysUtils;
import com.ihn.server.util.exception.ExceptionUtils;
import com.ihn.server.util.process.RunCmdService;
*/

public class JoramProcess implements ExternalService {

    private String servicePort; 
    private int    servicePortNumber;
    private boolean checkStartFlag=false;
    private static final String serverTopicName="msgTopic";
    private String namingPort="16400";
    
    /** tcp port,default is 16010 */
    public void setServicePort(String servicePort){
        this.servicePort=servicePort;
    }
    
    @Override
    public String getServiceName() {
        return "Joram(JMS Provider)";
    }
    
    
    @Override
    public boolean checkIsRun() {
    	System.out.println("Joram checkIsRun...");

        if(StringUtils.isEmpty(this.servicePort))
        	this.servicePort="16010";
        this.servicePortNumber=Integer.valueOf(this.servicePort);
        System.out.println(this.servicePortNumber+""+checkStartFlag+serverTopicName+namingPort);
        return true;
    }
        /*
        Boolean[] result=SysUtils.checkPorts("TCP", new String[]{this.servicePort});
        checkStartFlag=result==null || result.length==0 ? false : result[0];
        return checkStartFlag;
    }

    @Override
    public boolean isInited() {
        return checkStartFlag;
    }
    
    @Override
    public void init() {
    	createTopic(BizContext.getServerRunConfig().getServerBindAddress());
    }

    @Override
    public void start() {
    	replaceServerIpInJndiFile();
    	
        //joram service
        RunCmdService service=new RunCmdService();
        service.setStartupMode(RunCmdService.OUTSIDE_THREAD);
        
        //joram
        String cmd=BizContext.runHomeDir+"joram/run/startup_server.bat";
        service.setStartCmdLine(cmd);
        service.startService();
    }
    
    private void replaceServerIpInJndiFile(){
    	String serverIp="127.0.0.1";
    	String bindIp=System.getProperty(BizContext.PROP_BIND_IP_ADDRESS);
    	if(bindIp==null || bindIp.equals("0.0.0.0") || bindIp.equals("") || bindIp.equals("127.0.0.1")){
    		String[] localIps = SysUtils.getLocalIpsInWindows();
    		if(localIps==null || localIps.length==0){
    			//
    		}else{
    			int count=0;
    			String firstIp=null;
    			for(int i=0;i<localIps.length;i++){
    				if(localIps[i].equals("") || localIps[i].equals("127.0.0.1")){
    					// 
    				}else{
    					if(count==0)
    						firstIp=localIps[i];
    					count++;
    				}
    			}
    			if(count>1){
    				String fip=BizContext.getFirstLocalIpAddress(); 
    				if(fip!=null && fip.length()>0)
    					firstIp=fip;
    				System.out.println("first ip is "+firstIp);
    			}
    			if(firstIp!=null)
    				serverIp=firstIp;
    		}
    	}else{
    		serverIp=bindIp;
    	}
    	
    	if(serverIp==null || serverIp.equals("") || serverIp.equals("127.0.0.1"))
    		return;
    	
    	
    	// set back to context 
    	BizContext.setServerBindIp(serverIp);
    	
    	// 
    	String jndiFile=BizContext.runHomeDir+"joram/config/jndi.properties";
    	BufferedWriter writer=null;
    	try{
    		writer=new BufferedWriter(new FileWriter(jndiFile));
    		writer.write("java.naming.factory.initial fr.dyade.aaa.jndi2.client.NamingContextFactory");
    		writer.newLine();
    		writer.write("java.naming.factory.host "+serverIp);
    		writer.newLine();
    		writer.write("java.naming.factory.port 16400");
    		writer.newLine();
    		writer.flush();
    	}catch(Exception ex){
    		System.err.println("write failed,"+jndiFile);
    	}finally{
    		if(writer!=null){
    			try{
    				writer.close();
    			}catch(Exception ex){
    				System.err.println("write close failed,"+jndiFile);
    			}
    		}
    	}
    }
    
    @Override
    public void stop() {
        try{
            AdminModule.connect("root", "root", 60);
            AdminModule.stopServer();
        } catch (Exception ex){
        	BizContext.getLogger().warn("",ex);
        } 
    }

    private boolean createTopic(String serverIp) {
    	Logger logger = BizContext.getLogger();
        javax.naming.Context ictx =null;
        try {
        	String actualIp;
            if(serverIp==null)
            	actualIp="localhost";
            else
            	actualIp=serverIp;
            
            AdminModule.connect(actualIp,servicePortNumber,"root", "root", 10);
            
            Properties env = new Properties();
            env.setProperty("java.naming.factory.initial","fr.dyade.aaa.jndi2.client.NamingContextFactory");
            env.setProperty("java.naming.factory.host", actualIp);
            env.setProperty("java.naming.factory.port", namingPort);
            ictx = new javax.naming.InitialContext(env);
            // cf,tcf,qcf,cf
            try {
            	javax.jms.ConnectionFactory cf =TcpConnectionFactory.create(actualIp, servicePortNumber);
            	ictx.bind("cf", cf);
            	
                javax.jms.TopicConnectionFactory tcf = TopicTcpConnectionFactory.create(actualIp, servicePortNumber);
                ictx.bind("tcf", tcf);
                
                javax.jms.QueueConnectionFactory qcf =QueueTcpConnectionFactory.create(actualIp, servicePortNumber);
                ictx.bind("qcf", qcf);
            } catch (Exception e) {
                String info=ExceptionUtils.getCommonExceptionInfo(e);
                logger.error(info);
                throw new RuntimeException(e);
            }
            logger.info("JORAM create connection factory [cf,tcf,qcf] completed.");
  
            // create user
            User.create("anonymous", "anonymous");
            
            // create topic
            try{
                ictx.unbind(serverTopicName);
            }catch(Exception ex){
                System.out.println("[info] unbind server topic failed.");
            }
            Topic serverTopic=Topic.create(serverTopicName);
            serverTopic.setFreeReading();
            serverTopic.setFreeWriting();
            ictx.bind(serverTopicName,serverTopic);
            return true;
        } catch (Exception ex) {
            String info=ExceptionUtils.getCommonExceptionInfo(ex);
            System.err.println("[error] "+info);
            return false;
        }finally{
            if(ictx!=null){
                try{
                    ictx.close();
                }catch(Exception ex){
                    String info=ExceptionUtils.getCommonExceptionInfo(ex);
                    System.err.println("[error] "+info);
                }
            }
            AdminModule.disconnect();           
        }
    }
    */

	@Override
	public void start() {
	}

	@Override
	public boolean isInited() {
		return false;
	}

	@Override
	public void init() {
	}

	@Override
	public void stop() {
	}

}
