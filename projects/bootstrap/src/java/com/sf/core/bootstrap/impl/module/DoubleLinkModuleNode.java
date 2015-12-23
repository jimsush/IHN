
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
 * Description: ģ��������ϵ���е�˫�������е�module�ڵ㣬������ģ��������ģ��֮��Ĺ�ϵ
 * </p>
 * 
 * @author sufeng
 * created 2011-3-1 ����12:17:51
 * modified [who date description]
 * check [who date description]
 */
public class DoubleLinkModuleNode {
    
    /**
     * ��ǰmodule
     */
    private String moduleName;
    
    /**
     * ģ��������������ģ�飨������ֱ����������������
     */
    private Set<String> allDependentModules=new HashSet<String>();
    
    /**
     * ��ģ��ڵ���ӽڵ�
     */
    private List<DoubleLinkModuleNode> children=new ArrayList<DoubleLinkModuleNode>();
    
    /**
     * ��ģ��ڵ�ĸ��׽ڵ㣬�����ж��
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
     * ��ģ���ӡ�ֱ������������ģ��
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
