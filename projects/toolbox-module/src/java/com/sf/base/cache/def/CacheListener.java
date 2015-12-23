
package com.sf.base.cache.def;

/**
 * <p>
 * Title: CacheListener
 * </p>
 * <p>
 * Description:
 *      缓存对象生命周期监听器
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public interface CacheListener<T> {
    /**
     * 缓存对象被创建
     * @param object
     *          缓存对象
     */
    public void create(T object);
    /**
     * 缓存对象被删除
     * @param object
     *          缓存对象
     */
    public void remove(T object);
    /**
     * 缓存对象被修改
     * @param object
     *          缓存对象
     */
    public void update(T object);

}
