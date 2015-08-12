package com.ihn.server.internal.launch;

import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;





import com.ihn.server.util.logging.IHNLogFactory;
import com.ihn.server.util.logging.SimpleLogger;

public class BizContext {

	/** spring context */
    private static ApplicationContext ctx = null;
    
    private static IHNLogFactory ihnLogFactory;
    private static Logger serverLogger=null;
    private static Logger simpleLogger=new SimpleLogger();
    
    private static boolean stopFlag=false;
    
    public static String runHomeDir;
    
    public static ApplicationContext getCtx() {
        return ctx;
    }
    
    public static void setCtx(ApplicationContext appCtx) {
        if(ctx==null){
        	ctx = appCtx;
        	init();
        }
    }
    
    
    private static void init(){
    	getLogFactory();
    	Logger logger = ihnLogFactory.getLogger(IHNLogFactory.DEFAULT_LOGGER);
    	logger.info("init server...");
    	
    }
    
    public static IHNLogFactory getLogFactory() {
        if (ihnLogFactory == null) {
        	ihnLogFactory = (IHNLogFactory) getBean("ihnLogFactory");
        }
        return ihnLogFactory;
    }
    
    public static Logger getLogger(){
    	if(serverLogger!=null)
    		return serverLogger;
    	return simpleLogger;
    }
    
    public static void setLogger(Logger logger){
    	serverLogger=logger;
    }
    
    @SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName, Class<T> beanClazz) {
        Object obj=getBean(beanName);
        return (T)obj;
    }
    
    public static Object getBean(String beanName) {
        return ctx.getBean(beanName);
    }
    
    public static synchronized boolean isServerStopped(){
    	return stopFlag;
    }
    
    public static synchronized void setServerStop(){
    	stopFlag=true;
    }
    
    
}
