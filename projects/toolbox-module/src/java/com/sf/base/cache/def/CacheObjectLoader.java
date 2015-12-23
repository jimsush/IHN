package com.sf.base.cache.def;

import java.util.Map;

/**
 * <p>
 * Title: CacheObjectLoader
 * </p>
 * <p>
 * Description:
 * Cache��ʼ���ӿڣ����ڳ�ʼ��Cache
 * </p>
 * 
 * @param <K> ��
 * @param <T> ֵ
 */
public interface CacheObjectLoader<K,T> {
    
    /**
     * ���ػ������
     * @param K ��
     * @param T ֵ
     * @param loadParams
     *        ��ʼ������
     * @return
     *        Map,key=�������Ψһ��ʾ value=�������
     */
    public Map<K,T> loadCacheObjects(Object...loadParams);

}
