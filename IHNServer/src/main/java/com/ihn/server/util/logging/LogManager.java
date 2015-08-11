package com.ihn.server.util.logging;

public class LogManager {

	private static IHNLogFactory logFactory;
    
    public static void reset(IHNLogFactory logFactory1){
    	LogManager.logFactory=logFactory1;
    }
    
    /**
     * get log factory
     * @return
     */
    public static IHNLogFactory getLogFactory(){
        return logFactory;
    }
    
}
