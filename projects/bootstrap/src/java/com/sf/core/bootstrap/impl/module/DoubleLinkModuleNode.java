
package com.sf.core.bootstrap.impl.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Title: DoubleLinkModuleNode
 * </p>
 * <p>
 * Description: 模块依赖关系网中的双向链表中的module节点，包含了模块与依赖模块之间的关系
 * </p>
 * 
 * @author sufeng
 * created 2011-3-1 下午12:17:51
 * modified [who date description]
 * check [who date description]
 */
public class DoubleLinkModuleNode {
    
    /**
     * 当前module
     */
    private String moduleName;
    
    /**
     * 模块所依赖的所有模块（包含：直接依赖与间接依赖）
     */
    private Set<String> allDependentModules=new HashSet<String>();
    
    /**
     * 该模块节点的子节点
     */
    private List<DoubleLinkModuleNode> children=new ArrayList<DoubleLinkModuleNode>();
    
    /**
     * 该模块节点的父亲节点，可能有多个
     */
    private Map<String,DoubleLinkModuleNode> parents=new HashMap<String,DoubleLinkModuleNode>();

    public DoubleLinkModuleNode(String moduleName){
        this.moduleName=moduleName;
    }
    
    public String getModuleName() {
        return moduleName;
    }
    
    public void addChild(DoubleLinkModuleNode child){
        children.add(child);
    }
    
    public void addParent(DoubleLinkModuleNode parent){
        parents.put(parent.getModuleName(),parent);
    }
    
    public Map<String, DoubleLinkModuleNode> getParents() {
        return parents;
    }
    
    public List<DoubleLinkModuleNode> getChildren() {
        return children;
    }
    
    /**
     * 该模块间接、直接依赖的所有模块
     * @return
     */
    public Set<String> getAllDependentModules() {
        return allDependentModules;
    }
    
    public void setAllDependentModules(Set<String> allDependentModules) {
        this.allDependentModules = allDependentModules;
    }
    
    @Override
    public String toString() {
        return moduleName;
    }
    
    
}
