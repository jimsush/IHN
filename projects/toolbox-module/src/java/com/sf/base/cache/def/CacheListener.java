
package com.sf.base.cache.def;

/**
 * <p>
 * Title: CacheListener
 * </p>
 * <p>
 * Description:
 *      ��������������ڼ�����
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public interface CacheListener<T> {
    /**
     * ������󱻴���
     * @param object
     *          �������
     */
    public void create(T object);
    /**
     * �������ɾ��
     * @param object
     *          �������
     */
    public void remove(T object);
    /**
     * ��������޸�
     * @param object
     *          �������
     */
    public void update(T object);

}
