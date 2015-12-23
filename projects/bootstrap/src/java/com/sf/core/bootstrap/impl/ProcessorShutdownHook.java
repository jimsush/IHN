
package com.sf.core.bootstrap.impl;

/**
 * <p>
 * Title: ProcessorShutdownHook
 * </p>
 * <p>
 * Description: ע��shutdownʱ�Ļص�����thread
 * </p>
 * 
 * @author sufeng
 * created 2011-2-25 ����11:20:59
 * modified [who date description]
 * check [who date description]
 */
public class ProcessorShutdownHook extends Thread{

    public ProcessorShutdownHook(){
        // shutdownǰ�������ش���
        Runtime.getRuntime().addShutdownHook(this);   
    }
    
    @Override
    public void run() {
        ModuleContextImpl.notifyExit();
        // ��threadִ�н�����,������1С��ʱ��,�����ͱ����clean module�ڵ�ǰ�߳��д���,������main thread.
    }
    
}
