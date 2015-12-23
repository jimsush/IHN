package com.sf.base.cache.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.sf.base.cache.def.Cache;
import com.sf.base.cache.def.CacheManager;
import com.sf.base.cache.def.CacheObjectLoader;

/**
 * <p>
 * Title: DefaultCacheManager
 * </p>
 * <p>
 * Description:
 * CacheManager默认实现类
 * </p>
 * modified [who date description]
 * check [who date description]
 */
public class DefaultCacheManager implements CacheManager {

    @SuppressWarnings("unchecked")
    private ConcurrentMap<Class, Cache> cacheGroups=new ConcurrentHashMap<Class, Cache>();
   
    /**
     * (non-Javadoc)
     * @see com.sf.base.cache.def.CacheManager#createCache(java.lang.Class, com.sf.base.cache.def.CacheObjectLoader, java.lang.Object[])
     */
    @Override
    public <K,T> Cache<K,T> createCache(Class<T> cacheObjectClass,CacheObjectLoader<K,T> cacheObjectLoader,Object...params) {
        if(cacheGroups.containsKey(cacheObjectClass))
            throw new IllegalArgumentException(cacheObjectClass.getSimpleName()+" Cache is existed");
        if(cacheObjectLoader==null)
            throw new NullPointerException(" cacheObjectLoader is a null oject");
        Cache<K,T> cache=new DefaultCache<K,T>();
        cacheGroups.put(cacheObjectClass, cache);
        Map<K,T> cacheObjects=cacheObjectLoader.loadCacheObjects(params);
        for(Entry<K,T> entry:cacheObjects.entrySet()){
            cache.put(entry.getKey(), entry.getValue());
        }
        return cache;
    }

   
    /**
     * (non-Javadoc)
     * @see com.sf.base.cache.def.CacheManager#removeCache(java.lang.Class)
     */
    @Override
    public <T> void removeCache(Class<T> cacheObjectClass) {
        cacheGroups.remove(cacheObjectClass);
    }

    
    /**
     * (non-Javadoc)
     * @see com.sf.base.cache.def.CacheManager#getCache(java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <K,T> Cache<K,T> getCache(Class<T> cacheObjectClass) {
        if(!cacheGroups.containsKey(cacheObjectClass)){
            for(Class<?> claz:cacheGroups.keySet()){
                if(cacheObjectClass.isAssignableFrom(claz))
                    return cacheGroups.get(claz);
            }
            return null;
        }else
            return cacheGroups.get(cacheObjectClass);
    }

}
