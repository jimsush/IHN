/*
 * $Id: ArrayListItems.java, 2015-2-19 ����11:58:29 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.xml;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * Title: ArrayListItems
 * </p>
 * <p>
 * Description: Ϊ��ʹ��castor�ļ��ϣ���list���а�װ
 * </p>
 * 
 * @author sufeng
 * created 2015-2-19 ����11:58:29
 * modified [who date description]
 * check [who date description]
 */
public class ArrayListItems<T> implements Serializable{
    
    private static final long serialVersionUID = -5107393535117241810L;
    
    /**
     * ������item
     */
    private List<T> items=null;
    
    public List<T> getItems(){
        return this.items;
    }
    
    public void setItems(List<T> items){
        this.items=items;
    }
    
}
