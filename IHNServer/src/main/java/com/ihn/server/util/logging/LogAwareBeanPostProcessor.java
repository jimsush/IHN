package com.ihn.server.util.logging;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * spring systemic bean
 * @author tong
 *
 */
public class LogAwareBeanPostProcessor implements BeanPostProcessor {

    private IHNLogFactory ihnLogFactory;
    public void setIhnLogFactory(IHNLogFactory logFactory){
        this.ihnLogFactory=logFactory;
    }
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        if(bean instanceof LogAware){
            LogAware logAware=(LogAware)bean;
            logAware.setIhnLogFactory(ihnLogFactory);
        }
        return bean;
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

}
