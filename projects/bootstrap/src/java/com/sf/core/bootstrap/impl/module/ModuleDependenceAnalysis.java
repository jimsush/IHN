/*
 * $Id: ModuleDependenceAnalysis.java, 2015-3-1 ����12:20:31 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.bootstrap.impl.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.sf.core.bootstrap.impl.BootstrapUtils;
import com.sf.core.bootstrap.impl.ConfigExpress;

/**
 * <p>
 * Title: ModuleDependenceAnalysis
 * </p>
 * <p>
 * Description: ����ģ����ֱ��������ϵ�����������ϵ
 * </p>
 * 
 * @author sufeng
 * created 2015-3-1 ����12:20:31
 * modified [who date description]
 * check [who date description]
 */
public class ModuleDependenceAnalysis {

    /**
     * û���κ�������ģ��
     */
    private List<DoubleLinkModuleNode> heads=new ArrayList<DoubleLinkModuleNode>(); 
    
    /**
     * ģ��ֱ��������ģ�飬<module,[ֱ��������module����]>
     */
    private Map<String,Set<String>> refCountMap=new HashMap<String, Set<String>>();
    
    private Map<String,DoubleLinkModuleNode> allModuleNodeMap=new HashMap<String,DoubleLinkModuleNode>(); 

    /**
     * ���˫��������threshold
     */
    private static int MAX_DEP_MODULE=500;
    
    /**
     * �õ�ģ���������ϵ��ϸ��Ϣ
     * @param moduleName
     * @return
     */
    public DoubleLinkModuleNode getModule(String moduleName){
        return allModuleNodeMap.get(moduleName);
    }
    
    /**
     * ����ģ������ͼ����״��������ϵ��
     * @param sourceModules
     */
    public void initModuleDepenceMesh(Collection<ModuleEntity> sourceModules){
        if(sourceModules==null)
            return;
        
        Set<String> moduleNames=new HashSet<String>();
        for(ModuleEntity entity : sourceModules)
            moduleNames.add(entity.getName());
        
        for(ModuleEntity module : sourceModules){
            if(StringUtils.isEmpty(module.getDepends())){
                // ��������ģ��
                DoubleLinkModuleNode moduleInMap = allModuleNodeMap.get(module.getName());
                if(moduleInMap==null){
                    DoubleLinkModuleNode indepNode=new DoubleLinkModuleNode(module.getName());
                    allModuleNodeMap.put(module.getName(),indepNode);
                    heads.add(indepNode);
                }else{
                    heads.add(moduleInMap);
                }
            }else{
                //��������ģ��
                DoubleLinkModuleNode currentNode=allModuleNodeMap.get(module.getName());
                if(currentNode==null){
                    currentNode=new DoubleLinkModuleNode(module.getName());
                    allModuleNodeMap.put(module.getName(),currentNode);
                }
                List<String> depModules = ConfigExpress.express2List(module.getDepends());
                for(String depModule : depModules){
                    DoubleLinkModuleNode parentModule = allModuleNodeMap.get(depModule);
                    if(parentModule==null){
                        if(!moduleNames.contains(depModule))
                            throw new RuntimeException("["+module.getName()+"] dependent module ["+depModule+"] not existed");
                        
                        parentModule=new DoubleLinkModuleNode(depModule);
                        allModuleNodeMap.put(depModule,parentModule);
                    }
                    // ��������ģ���˫������
                    parentModule.addChild(currentNode);
                    currentNode.addParent(parentModule);
                }
            }
        }

        //����ÿ��module������������ģ�飬�����������
        for(ModuleEntity module : sourceModules){
            DoubleLinkModuleNode moduleNode = allModuleNodeMap.get(module.getName());
            Set<String> dependenceModules=new HashSet<String>();
            List<String> count=new ArrayList<String>();
            getAllDependence(moduleNode.getModuleName(),moduleNode,dependenceModules,count);
            moduleNode.setAllDependentModules(dependenceModules);
        }
    }

    /**
     * ����������ϵ
     * @param currentModule
     * @param module
     * @param dependenceModules
     * @param count
     */
    private void getAllDependence(String currentModule,DoubleLinkModuleNode module,Set<String> dependenceModules,List<String> count){
        if(count.size()>=MAX_DEP_MODULE){
            // ���ƴ���ѭ������
            String info="maybe exist loop dependence,please check product xml,["+currentModule+"]";
            BootstrapUtils.getLogger().warn(info);
            throw new RuntimeException(info);
        }
        
        Map<String, DoubleLinkModuleNode> parents = module.getParents();
        if(MapUtils.isNotEmpty(parents)){
            for(Map.Entry<String,DoubleLinkModuleNode> entry : parents.entrySet()){
                dependenceModules.add(entry.getKey());
                count.add("");
                DoubleLinkModuleNode parentModule = entry.getValue();
                getAllDependence(currentModule,parentModule, dependenceModules,count);
            }
        }
    }
    
    /**
     * ��������ģ���������ϵ����������core���ڵ�һ��λ��
     * @return
     */
    public List<String> getAllModulesByOrder(){
        List<String> sortedModuleNames=new ArrayList<String>();
        for(DoubleLinkModuleNode rootNode : heads)
            calculateDependenceRefCount(rootNode); //��֪���Ƿ������⣬��Ҫ����
        
        // ÿ����û���κ�dependence��ģ��
        Set<String>  fetchedModuleNames=new HashSet<String>();
        DoubleLinkModuleNode node=null;
        while(true){
            node=getIndependentModule(fetchedModuleNames);
            if(node==null)
                break;
            // �н���ͷ��뼯����
            sortedModuleNames.add(node.getModuleName());
        }
        return sortedModuleNames;
    }
    
    public Set<String> getAllModuleNames(){
        return allModuleNodeMap.keySet();
    }
    
    /**
     * ����ģ�����������ô���
     * @param curNode
     */
    private void calculateDependenceRefCount(DoubleLinkModuleNode curNode){
        Set<String> parentModules=new HashSet<String>();
        Map<String, DoubleLinkModuleNode> parents = curNode.getParents();
        parentModules.addAll(parents.keySet());
        refCountMap.put(curNode.getModuleName(), parentModules);
        
        List<DoubleLinkModuleNode> children = curNode.getChildren();
        for(DoubleLinkModuleNode node : children){
            calculateDependenceRefCount(node);
        }
    }
    
    /**
     * �õ���һ����������ģ��
     * @param fetchedModuleNames
     * @return
     */
    private DoubleLinkModuleNode getIndependentModule(Set<String> fetchedModuleNames){
        for(Map.Entry<String,Set<String>> entry : refCountMap.entrySet()){
            if(entry.getValue().size()==0){
                // �Ѿ���������
                if(fetchedModuleNames.contains(entry.getKey())) 
                    continue;
                
                // ���뵽�������ļ�����
                fetchedModuleNames.add(entry.getKey()); 
                
                DoubleLinkModuleNode rootNode = allModuleNodeMap.get(entry.getKey());
                
                // �������ӵ�parent����һ��
                List<DoubleLinkModuleNode> children = rootNode.getChildren();
                for(DoubleLinkModuleNode child : children){
                    Set<String> count = refCountMap.get(child.getModuleName());
                    count.remove(entry.getKey());
                }
                return rootNode;
            }
        }
        return null;
    }

}
