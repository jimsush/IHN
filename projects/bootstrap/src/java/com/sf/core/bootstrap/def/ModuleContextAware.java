
package com.sf.core.bootstrap.def;

/**
 * <p>
 * Title: ModuleContextAware
 * </p>
 * <p>
 * Description: ģ���ʵ�ָýӿڣ���load module��ʱ��,�ὫModuleContextʵ�����õ���ǰmodule��
 * ���������Ա����õ���ModuleContext.getInstance()��ȡModuleContext����������Ԫ����
 * </p>
 * 
 * @author sufeng
 * created 2011-3-31 ����01:22:05
 * modified [who date description]
 * check [who date description]
 */
public interface ModuleContextAware {

    /**
     * ����module context
     * @param moduleContext ģ��������
     */
    public void setModuleContext(ModuleContext moduleContext);
    
}
