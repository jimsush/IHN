/*
 * $Id: SessionState.java, 2015-2-20 ����02:05:08  Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.session.def;

/**
 * <p>
 * Title: SessionState
 * <p>
 * �ԻỰ״̬��ö������, ƽ̨ͨ��<code>SessionState</code>�����˻Ự�е����п��ܵ�״̬.
 * <p>
 * �Ự״̬�������¼���:<br>
 * ����״̬(Linup)
 * �״̬(Active,���Ӳ���ʼ����ϣ����ڹ���״̬)
 * ���״̬(Deactive)
 * ʧ��״̬(Linkdown):
 * 
 * @author 
 * created 2015-2-20 ����02:05:08
 * 
 * modified [who date description]
 * check [who date description]
 */
public enum SessionState {
   
    /** ����״̬ */
    Linkup,
    
    /**
     * ��Ծ�ģ�����ͨ����ϵͳ����֤������Ծ��
     */
    Active,
    
    /**
     * ����Ծ
     */
    Deactive,
    
    /** ȥ���� */
    Linkdown;
    
}
