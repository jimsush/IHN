package com.sf.core.bootstrap.def.module;

import java.util.List;

/**
 * <p>
 * Title: CoreContext4Container
 * </p>
 * <p>
 * Description:ģ��������ӿڶ������Ĺ���ӿ�
 * </p>
 * 
 * @author sufeng
 * modified [who date description]
 * check [who date description]
 */
public interface ModuleContext4Container {

    /**
     * ��ʼ������
     * @param homeDir ���еĸ�·��
     * @param productName ��Ʒ��
     * @return ģ������,���Ⱥ�˳������
     */
    public List<String> initContainer(String homeDir,String productName);

    /**
     * ���ļ�ϵͳ�м���ģ�飨��ʱģ�黹���������У�
     * @param moduleName ģ����
     * @return
     */
    public Module loadModule(String moduleName);
    
    /**
     * ��ʼ��module���ȼ�������
     * @param modules ģ��
     */
    public void initModulePriority(List<Module> modules);
    
    /**
     * ��ģ����뵽������
     * @param <T> ģ������
     * @param moduleClass ģ����
     * @param module ģ��ʵ��
     */
    public <T extends Module> void putModule2Container(Class<T> moduleClass, Module module);
    
    /**
     * ����ģ��
     * @param module ģ��
     */
    public void startModule(Module module);

    /**
     * ֹͣģ��
     * @param module ģ��
     */
    public void stopModule(Module module);
    
    /**
     * ж��ģ��
     * @param module ģ��
     */
    public void unloadModule(Module module);

}
