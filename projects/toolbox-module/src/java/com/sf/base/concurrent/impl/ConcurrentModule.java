package com.sf.base.concurrent.impl;

import com.sf.base.concurrent.def.collaboration.CollaborationService;
import com.sf.base.concurrent.def.threadpool.ThreadPoolManager;
import com.sf.base.concurrent.impl.collaboration.CollaborationServiceImpl;
import com.sf.base.concurrent.impl.collaboration.producerconsumer.ProducerConsumerManager;
import com.sf.base.concurrent.impl.threadpool.ThreadPoolManagerImpl;
import com.sf.core.bootstrap.def.module.DefaultModule;
import com.sf.core.container.def.CoreContext;

/**
 * <p>
 * Title: ConcurrentModule
 * </p>
 * <p>
 * Description:并发处理模块，包含线程池ThreadPoolManager，任务间协调CollaborationService的服务
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public class ConcurrentModule extends DefaultModule{

    private ThreadPoolManagerImpl threadPoolManager;
    private CollaborationServiceImpl collaborationService;
    
    @Override
    public void start() {
        threadPoolManager=new ThreadPoolManagerImpl();
        CoreContext.getInstance().setLocalService("threadPoolManager",ThreadPoolManager.class, threadPoolManager);
        
        ProducerConsumerManager pcManager=new ProducerConsumerManager();
        pcManager.setThreadPoolManager(threadPoolManager);
        collaborationService=new CollaborationServiceImpl(pcManager);
        CoreContext.getInstance().setLocalService("collaborationService",CollaborationService.class, collaborationService);
        
    }

    @Override
    public void stop() {
        threadPoolManager.destroy();
    }

}
