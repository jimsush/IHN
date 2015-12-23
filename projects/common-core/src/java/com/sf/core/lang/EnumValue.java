/*
 * $Id: EnumValue.java, 2015-4-15 下午02:18:36 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.lang;

/**
 * <p>
 * Title: EnumValue
 * </p>
 * <p>
 * Description:枚举类的接口
 * </p>
 * 
 * @author sufeng
 * created 2015-4-15 下午02:18:36
 * modified [who date description]
 * check [who date description]
 */
public interface EnumValue {

    /**
     * 枚举对象的唯一标识id
     * @return
     */
    public int getValue();
    
}
