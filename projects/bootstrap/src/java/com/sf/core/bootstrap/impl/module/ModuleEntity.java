/*
 * $Id: ModuleEntity.java, 2015-9-28 下午03:42:15 aaron Exp $
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
 * Description: module基本信息
 * </p>
 * 
 * @author aaron
 * created 2015-9-28 下午03:42:15
 * modified [who date description]
 * check [who date description]
 */
public class ModuleEntity {
    
    /**
     * 模块名
     */
    private String name;
    
    /**
     * 实现类
     */
    private String moduleClass;

    /**
     * 依赖的其他模块名列表，使用,来分隔
     */
    private String  depends;

    /**
     * 本模块使用的公用jar（位于modules/lib/dir/*.jar）
     */
    private String publicJars;
    
    /**
     * 模块的参数
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
