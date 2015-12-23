package com.sf.base.cache.def;

/**
 * <p>
 * Title: CacheManager
 * </p>
 * <p>
 * Description:
 * �������ӿ�
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public interface CacheManager {

   /**
    * ����Cache
    * @param <K>
    * @param <T>
    * @param cacheObjectClass
    *        �������Java Class����
    * @param cacheObjectLoader
    *        ��������ʼ���ӿ�
    * @param params
    *        ��������ʼ������
    * @return
    *        Cache
    */
    public <K,T> Cache<K,T> createCache(Class<T> cacheObjectClass,CacheObjectLoader<K,T> cacheObjectLoader,Object...params);
    /**
     * ɾ��Cache
     * @param cacheObjectClass
     *        �������java Class����
     */
    public <T> void removeCache(Class<T> cacheObjectClass);
    /**
     * �������ͻ�ȡCache
     * @param <T>
     * @param cacheObjectClass
     *        �������java Class����
     * @return
     *        Cache
     */
    public <K,T> Cache<K,T> getCache(Class<T> cacheObjectClass);
    
}
