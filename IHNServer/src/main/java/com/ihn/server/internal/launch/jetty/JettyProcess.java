package com.ihn.server.internal.launch.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import com.ihn.server.internal.launch.service.ExternalService;

public class JettyProcess implements ExternalService{

	private Thread thread;
	
    @Override
    public String getServiceName() {
        return "Jetty";
    }
    
    @Override
    public boolean checkIsRun() {
        return false;
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
    	thread=new Thread(){
    		
    		public void run(){
		    	Server server = new Server(8080);
		    	 
		        WebAppContext context = new WebAppContext();
		        context.setDescriptor("WEB-INF/classes/web.xml");
		        context.setResourceBase(".");
		        context.setContextPath("/");
		        context.setParentLoaderPriority(true);
		        server.setHandler(context);
		        
		        try{
		        	server.start();
		        	server.join();
		        }catch(Exception ex){
		        	ex.printStackTrace();
		        }
		        
    		}
    	};
    	thread.start();
    }

    @Override
    public void stop() {
    	
    	try{
    		if(thread!=null)
    			thread.interrupt();
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	
    }
    
}
