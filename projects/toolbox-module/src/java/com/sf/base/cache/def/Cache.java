package com.sf.base.cache.def;

import java.util.List;

/**
 * <p>
 * Title: Cache
 * </p>
 * <p>
 * Description:
 * 缓存接口
 * </p>
 * modified [who date description]
 * check [who date description]
 */
public interface Cache<K,T> {

    /**
     * 根据key从Cache中获取对象
     * 
     * @param key
     *        被缓存的对象唯一标示
     * @return
     *        被缓存对象，如果key对应的对象不存在，则返回Null
     */
    public T get(K key);

    /**
     * 向Cache中加入对象
     * 
     * @param key
     *          被缓存的对象唯一标示
     * @param cacheObject
     *          被缓存对象
     */
    public void put(K key, T cacheObject);

    /**
     * 根据key从Cache中删除对象
     * 
     * @param key
     *          被缓存的对象唯一标示
     */
    public void remove(K key);

    /**
     * 清空所有缓存中的数据
     */
    public void clear();
   
    /**
     * 获取缓存中元素总数
     * 
     * @return
     *      缓存中元素总数
     */
    public int size();
    /**
     * 添加CacheListener监听器
     * 
     * @param cacheListener
     *         缓存对象生命周期监听器
     * @see com.sf.base.cache.CacheListener
     */
    public void addCacheListener(CacheListener<T> cacheListener);

    /**
     * 删除CacheListener监听器
     * 
     * @param cacheListener
     *        缓存对象生命周期监听器
     * @see com.sf.base.cache.CacheListener
     */
    public void removeCacheListener(CacheListener<T> cacheListener);
    
    /**
     * 根据被缓存对象的java Class类型获取缓存中所有该类型的缓存对象
     * @param cacheObjectClass
     *        被缓存对象的java Class类型
     * @return
     *        缓存对象对象集合
     */
    public List<T> getCacheObjects(Class<T> cacheObjectClass);

}
