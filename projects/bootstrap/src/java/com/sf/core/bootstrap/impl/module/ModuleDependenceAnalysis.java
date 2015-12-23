/*
 * $Id: ModuleDependenceAnalysis.java, 2015-3-1 下午12:20:31 sufeng Exp $
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
 * Description: 解析模块间的直接依赖关系，间接依赖关系
 * </p>
 * 
 * @author sufeng
 * created 2015-3-1 下午12:20:31
 * modified [who date description]
 * check [who date description]
 */
public class ModuleDependenceAnalysis {

    /**
     * 没有任何依赖的模块
     */
    private List<DoubleLinkModuleNode> heads=new ArrayList<DoubleLinkModuleNode>(); 
    
    /**
     * 模块直接依赖的模块，<module,[直接依赖的module集合]>
     */
    private Map<String,Set<String>> refCountMap=new HashMap<String, Set<String>>();
    
    private Map<String,DoubleLinkModuleNode> allModuleNodeMap=new HashMap<String,DoubleLinkModuleNode>(); 

    /**
     * 检测双向依赖的threshold
     */
    private static int MAX_DEP_MODULE=500;
    
    /**
     * 得到模块的依赖关系详细信息
     * @param moduleName
     * @return
     */
    public DoubleLinkModuleNode getModule(String moduleName){
        return allModuleNodeMap.get(moduleName);
    }
    
    /**
     * 建立模块依赖图（网状的依赖关系）
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
                // 无依赖的模块
                DoubleLinkModuleNode moduleInMap = allModuleNodeMap.get(module.getName());
                if(moduleInMap==null){
                    DoubleLinkModuleNode indepNode=new DoubleLinkModuleNode(module.getName());
                    allModuleNodeMap.put(module.getName(),indepNode);
                    heads.add(indepNode);
                }else{
                    heads.add(moduleInMap);
                }
            }else{
                //有依赖的模块
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
                    // 建立到父模块的双向链表
                    parentModule.addChild(currentNode);
                    currentNode.addParent(parentModule);
                }
            }
        }

        //计算每个module所依赖的所有模块，包括间接依赖
        for(ModuleEntity module : sourceModules){
            DoubleLinkModuleNode moduleNode = allModuleNodeMap.get(module.getName());
            Set<String> dependenceModules=new HashSet<String>();
            List<String> count=new ArrayList<String>();
            getAllDependence(moduleNode.getModuleName(),moduleNode,dependenceModules,count);
            moduleNode.setAllDependentModules(dependenceModules);
        }
    }

    /**
     * 计算依赖关系
     * @param currentModule
     * @param module
     * @param dependenceModules
     * @param count
     */
    private void getAllDependence(String currentModule,DoubleLinkModuleNode module,Set<String> dependenceModules,List<String> count){
        if(count.size()>=MAX_DEP_MODULE){
            // 估计存在循环依赖
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
     * 分析所有模块的依赖关系，无依赖的core放在第一个位置
     * @return
     */
    public List<String> getAllModulesByOrder(){
        List<String> sortedModuleNames=new ArrayList<String>();
        for(DoubleLinkModuleNode rootNode : heads)
            calculateDependenceRefCount(rootNode); //不知道是否有问题，需要测试
        
        // 每次找没有任何dependence的模块
        Set<String>  fetchedModuleNames=new HashSet<String>();
        DoubleLinkModuleNode node=null;
        while(true){
            node=getIndependentModule(fetchedModuleNames);
            if(node==null)
                break;
            // 有结果就放入集合中
            sortedModuleNames.add(node.getModuleName());
        }
        return sortedModuleNames;
    }
    
    public Set<String> getAllModuleNames(){
        return allModuleNodeMap.keySet();
    }
    
    /**
     * 计算模块依赖的引用次数
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
     * 得到下一个不依赖的模块
     * @param fetchedModuleNames
     * @return
     */
    private DoubleLinkModuleNode getIndependentModule(Set<String> fetchedModuleNames){
        for(Map.Entry<String,Set<String>> entry : refCountMap.entrySet()){
            if(entry.getValue().size()==0){
                // 已经遍历过了
                if(fetchedModuleNames.contains(entry.getKey())) 
                    continue;
                
                // 加入到遍历过的集合中
                fetchedModuleNames.add(entry.getKey()); 
                
                DoubleLinkModuleNode rootNode = allModuleNodeMap.get(entry.getKey());
                
                // 把它儿子的parent都减一个
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
