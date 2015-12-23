package com.sf.base.cache.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.sf.base.cache.def.Cache;
import com.sf.base.cache.def.CacheListener;
import com.sf.base.util.format.SfObjectUtils;

/**
 * <p>
 * Title: DefaultCache
 * </p>
 * <p>
 * Description:
 * 缓存接口Cache默认实现
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 * @param <K> 键
 * @param <T> 值
 */
public class DefaultCache<K,T> implements Cache<K,T> {

    /**
     * cache listener
     */
    private List<CacheListener<T>> cacheListeners=new CopyOnWriteArrayList<CacheListener<T>>();
   
    /**
     * cache
     */
    private ConcurrentMap<K, T> cache=new ConcurrentHashMap<K, T>();
   
    @Override
    public T get(K key) {
        return cache.get(key);
    }
  

    @Override
    public void put(K key, T cacheObject) {
        if(!cache.containsKey(key)){
            cache.put(key, cacheObject);
            firedCreateCacheObject(cacheObject);
        }
        else{
          T updatedCacheObject= cache.get(key);
          SfObjectUtils.copyProperities(updatedCacheObject, cacheObject);
          firedUpdateCacheObject(updatedCacheObject);
        }
            
    }
 
    @Override
    public void remove(K key) {
        T removedcacheObject=cache.remove(key);
        firedRemoveCacheObject(removedcacheObject);
    }
   
    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public int size() {
        return cache.size();
    }


    @Override
    public void addCacheListener(CacheListener<T> cacheListener) {
        cacheListeners.add(cacheListener);
    }

    @Override
    public void removeCacheListener(CacheListener<T> cacheListener) {
        cacheListeners.remove(cacheListener);
    }
 
    @Override
    public List<T> getCacheObjects(Class<T> valueClass){
        List<T> values=new ArrayList<T>();
        for(T value:cache.values()){
            if(valueClass.isInstance(value))
                values.add(value);
        }
        return new ArrayList<T>(cache.values());
    }
    
    
    private void firedRemoveCacheObject(T cacheObject){
        for(CacheListener<T> cacheListener:cacheListeners){
            cacheListener.remove(cacheObject);
        }
    }
    
    private void firedCreateCacheObject(T cacheObject){
        for(CacheListener<T> cacheListener:cacheListeners){
            cacheListener.create(cacheObject);
        }
    }
    
    private void firedUpdateCacheObject(T cacheObject){
        for(CacheListener<T> cacheListener:cacheListeners){
            cacheListener.update(cacheObject);
        } 
    }

  

}
