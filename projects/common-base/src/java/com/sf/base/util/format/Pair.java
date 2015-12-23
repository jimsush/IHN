package com.sf.base.util.format;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * <p>
 * Title: Pair
 * </p>
 * <p>
 * 封装两个对象
 * </p>
 * 
 * @author pl
 * created 2015-3-24 下午05:44:46
 * modified [who date description]
 * check [who date description]
 * @param <K>
 * @param <V>
 */
public class Pair<K, V> implements Serializable{
    private static final long serialVersionUID = -1914045300148394323L;
    
    private K k;
    private V v;

    public Pair(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public K getK() {
        return k;
    }

    public void setK(K k) {
        this.k = k;
    }

    public V getV() {
        return v;
    }

    public void setV(V v) {
        this.v = v;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("k", k).append("v",v).toString();
    }
}
