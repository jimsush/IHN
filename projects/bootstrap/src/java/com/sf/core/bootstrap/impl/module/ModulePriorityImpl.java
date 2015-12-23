
package com.sf.core.bootstrap.impl.module;

import java.util.ArrayList;
import java.util.List;

import com.sf.core.bootstrap.def.module.Module;
import com.sf.core.bootstrap.def.module.ModulePriority;

/**
 * <p>
 * Title: ModulePriorityImpl
 * </p>
 * <p>
 * Description: ģ�����ȼ�����
 * </p>
 * 
 * @author sufeng
 * created 2011-3-1 ����03:01:35
 * modified [who date description]
 * check [who date description]
 */
public class ModulePriorityImpl implements ModulePriority {

    /**
     * ����˳��
     */
    private List<Module> startOrder=new ArrayList<Module>();
    
    /**
     * ֹͣ��˳��
     */
    private List<Module> shutdownOrder=new ArrayList<Module>();

    public ModulePriorityImpl(List<Module> modules){
        startOrder.addAll(modules);
        
        if(startOrder.size()==0)
            return;
        
        int maxIndex=startOrder.size()-1;
        for(int i=maxIndex; i>=0; i--){
            shutdownOrder.add(startOrder.get(i));
        }
    }
    
    @Override
    public List<Module> getStartOrder() {
        return startOrder;
    }
    
    @Override
    public List<Module> getShutdownOrder() {
        return shutdownOrder;
    }
    
}
