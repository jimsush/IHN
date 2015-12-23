package com.sf.base.cache.def;

/**
 * <p>
 * Title: CacheManager
 * </p>
 * <p>
 * Description:
 * 缓存管理接口
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public interface CacheManager {

   /**
    * 创建Cache
    * @param <K>
    * @param <T>
    * @param cacheObjectClass
    *        缓存对象Java Class类型
    * @param cacheObjectLoader
    *        缓存对象初始化接口
    * @param params
    *        缓存对象初始化参数
    * @return
    *        Cache
    */
    public <K,T> Cache<K,T> createCache(Class<T> cacheObjectClass,CacheObjectLoader<K,T> cacheObjectLoader,Object...params);
    /**
     * 删除Cache
     * @param cacheObjectClass
     *        缓存对象java Class类型
     */
    public <T> void removeCache(Class<T> cacheObjectClass);
    /**
     * 根据类型获取Cache
     * @param <T>
     * @param cacheObjectClass
     *        缓存对象java Class类型
     * @return
     *        Cache
     */
    public <K,T> Cache<K,T> getCache(Class<T> cacheObjectClass);
    
}
