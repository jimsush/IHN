package com.sf.core.bootstrap.def.module;

/**
 * <p>
 * Title: ModuleConstants
 * </p>
 * <p>
 * Description: 模块名称的常量类，比如模块的状态
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public interface ModuleConstants {
    
    /**
     * 最初的状态
     */
    String STATUS_NONE = "NONE";
    
    /**
     * 模块已被识别并加载,但尚未启动
     */
    String STATUS_LOADED="LOADED";
    
    /**
     * 正在启动中
     */
    String STATUS_STARTING = "STARTING";
    
    /**
     * 启动失败了
     */
    String STATUS_FAILED="FAILED";
    
    /**
     * 启动完毕，可以提供服务
     */
    String STATUS_ACTIVE = "ACTIVE";
    
    /**
     * 正在停止中
     */
    String STATUS_STOPPING = "STOPPING";
    
    /**
     * 停止完毕，处于deactive状态，无法提供服务
     */
    String STATUS_DEACTIVE = "DEACTIVE";
    
}
