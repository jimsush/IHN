package com.sf.core.bootstrap.def.module;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sf.base.log.def.Logger;
import com.sf.base.log.def.SimpleLogger;
import com.sf.core.bootstrap.def.ContainerConst;
import com.sf.core.bootstrap.def.ModuleContext;


/**
 * <p>
 * Title: DefaultModule
 * </p>
 * <p>
 * Description: ģ�鶨���ȱʡ����,�ϲ����ܱ���̳иó�����
 * </p>
 * 
 * @author 
 * created 2015-9-17 ����03:06:41
 * modified [who date description]
 * check [who date description]
 */
public abstract class DefaultModule implements Module,Module4Container{

    /**
     * ģ�����
     */
    protected Map<String, String> moduleParams = new ConcurrentHashMap<String, String>();
    
    /**
     * ģ��״̬
     */
    protected String moduleStatus = STATUS_NONE;

    /**
     * ģ���ʶ��
     */
    protected String moduleName;
 
    /**
     * ģ����������
     */
    protected ClassLoader classLoader;
    
    /**
     * ģ�����־
     */
    protected Logger logger;
    
    protected Logger simpleLogger=new SimpleLogger("simple");

    @Override
    public void setModuleName(String name) {
        this.moduleName = name;
    }

    /**
     * @see com.sf.core.bootstrap.def.module.module.def.Module#getModuleParams()
     */
    @Override
    public Map<String, String> getModuleParams() {
        return moduleParams;
    }

    /**
     * @see com.sf.core.bootstrap.def.module.module.def.Module#getModuleStatus()
     */
    @Override
    public String getModuleStatus() {
        return moduleStatus;
    }
    
    /**
     * @see com.sf.core.bootstrap.def.module.module.def.Module#getModuleName()
     */
    @Override
    public String getModuleName() {
        return moduleName;
    }

    @Override
    public void setModuleStatus(String moduleStatus) {
        this.moduleStatus=moduleStatus;
    }
    
    @Override
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    
    @Override
    public Logger getLogger() {
        return logger==null ? simpleLogger : logger;
    }
    
    @Override
    public void setLogger(Logger logger) {
        this.logger=logger;
    }
    
    @Override
    public String toString() {
        return getModuleName();
    }

    @Override
    public String getModulePath(){
    	String homeDir=ModuleContext.getInstance().getSystemParam(ContainerConst.KEY_HOME_DIR);
        String modulePath=homeDir+File.separator+"modules"+File.separator+moduleName+File.separator;
        return modulePath;
    }
    
}
