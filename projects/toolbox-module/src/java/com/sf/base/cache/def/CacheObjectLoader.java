package com.sf.base.cache.def;

import java.util.Map;

/**
 * <p>
 * Title: CacheObjectLoader
 * </p>
 * <p>
 * Description:
 * Cache初始化接口，用于初始化Cache
 * </p>
 * 
 * @param <K> 键
 * @param <T> 值
 */
public interface CacheObjectLoader<K,T> {
    
    /**
     * 加载缓存对象
     * @param K 键
     * @param T 值
     * @param loadParams
     *        初始化参数
     * @return
     *        Map,key=缓存对象唯一标示 value=缓存对象
     */
    public Map<K,T> loadCacheObjects(Object...loadParams);

}
