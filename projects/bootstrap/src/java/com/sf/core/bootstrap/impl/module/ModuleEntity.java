/*
 * $Id: ModuleEntity.java, 2015-9-28 ����03:42:15 aaron Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.bootstrap.impl.module;

import java.util.List;
import java.util.Map;

import com.sf.core.bootstrap.impl.ConfigExpress;

/**
 * <p>
 * Title: ModuleEntity
 * </p>
 * <p>
 * Description: module������Ϣ
 * </p>
 * 
 * @author aaron
 * created 2015-9-28 ����03:42:15
 * modified [who date description]
 * check [who date description]
 */
public class ModuleEntity {
    
    /**
     * ģ����
     */
    private String name;
    
    /**
     * ʵ����
     */
    private String moduleClass;

    /**
     * ����������ģ�����б�ʹ��,���ָ�
     */
    private String  depends;

    /**
     * ��ģ��ʹ�õĹ���jar��λ��modules/lib/dir/*.jar��
     */
    private String publicJars;
    
    /**
     * ģ��Ĳ���
     */
    private Map<String,String> moduleParams;
    
    public ModuleEntity(){
    }
    
    public ModuleEntity(String name){
        setName(name);
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the moduleClass
     */
    public String getModuleClass() {
        return moduleClass;
    }
    
    /**
     * @param moduleClass the moduleClass to set
     */
    public void setModuleClass(String moduleClass) {
        this.moduleClass = moduleClass;
    }

    public String getDepends() {
        return depends;
    }

    public void setDepends(String depends) {
        this.depends = depends;
    }

    public Map<String, String> getModuleParams() {
        return moduleParams;
    }
    
    public void setModuleParams(Map<String, String> moduleParams) {
        this.moduleParams = moduleParams;
    }

    /**
     * @param publicJars the publicJars to set
     */
    public void setPublicJars(String publicJars) {
        this.publicJars = publicJars;
    }

    /**
     * @return the publicJars
     */
    public List<String> getPublicJars() {
        return ConfigExpress.express2List(this.publicJars);
    }
    
    @Override
    public String toString() {
        return name;
    }
    
}
