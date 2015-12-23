package com.sf.base.cache.impl;

import com.sf.base.cache.def.CacheManager;
import com.sf.core.bootstrap.def.module.DefaultModule;
import com.sf.core.container.def.CoreContext;

/**
 * <p>
 * Title: CacheModule
 * </p>
 * <p>
 * Description:»º´æÄ£¿é
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public class CacheModule extends DefaultModule {

    @Override
    public void start() {
        DefaultCacheManager defaultCacheManager=new DefaultCacheManager();
        CoreContext.getInstance().setLocalService("cacheManager", CacheManager.class, defaultCacheManager);
    }

    @Override
    public void stop() {
        
    }

}
