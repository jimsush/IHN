
package com.sf.core.bootstrap.impl.loader;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;

import com.sf.core.bootstrap.impl.BootstrapUtils;

/**
 * <p>
 * Title: ModuleClassLoader
 * </p>
 * <p>
 * Description:ģ���class loader,�ȼ����Լ�����ContainerClassLoader
 * </p>
 * 
 * @author sufeng
 * created 2010-12-10 ����05:24:43
 * modified [who date description]
 * check [who date description]
 */
public class ModuleClassLoader extends URLClassLoader {

    /**
     * ������class loader,��module class loader�ĸ���
     */
    private ContainerClassLoader containerClassLoader;
    
    /**
     * ��ǰģ������
     */
    private String currentModuleName;
    
    /**
     * ģ���class loader
     * @param urls
     * @param parent ������class loader
     */
    public ModuleClassLoader(URL[] urls, ContainerClassLoader parent,String currentModuleName) {
        super(urls, parent);
        this.containerClassLoader=parent;
        this.currentModuleName=currentModuleName;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        // �����Լ���class���ٵ���ClassLoader.loadClass���ȸ������Լ�
        return findAndLoadClass(name,true);
    }
    
    /**
     * ������������(cache,self class,container class except self class)
     * @param name           ����������com.sf.base.exception.SfException
     * @param findFromParent �Ƿ�Ӹ�ContainerClassLoader�м�����
     * @return
     * @throws ClassNotFoundException
     */
    public Class<?> findAndLoadClass(String name,boolean findFromParent) throws ClassNotFoundException {
        // �����Լ���class���ٵ���ClassLoader.loadClass���ȸ������Լ�
        //if(name.endsWith("AserviceImpl") || name.endsWith("AService"))
        //    System.out.println(name+","+currentModuleName+","+findFromParent);
        
        Class<?> clz=findAndLoadClass_Internal(name, findFromParent);
        //if(name.endsWith("AserviceImpl") || name.endsWith("AService"))
        //    System.out.println(name+","+currentModuleName+","+findFromParent+",found="+(clz!=null));
        return clz;
    }
    
    private Class<?> findAndLoadClass_Internal(String name,boolean findFromParent) throws ClassNotFoundException {
        // �����Լ���class���ٵ���ClassLoader.loadClass���ȸ������Լ�
        Class<?> c = findLoadedClass(name);
        if(c!=null)
            return c;
        
        // ���Լ���class
        ClassNotFoundException rootCauseEx=null;
        try{
            c=this.findClass(name);
        }catch(NoClassDefFoundError ex2){
            rootCauseEx=throwClassNotFound(name,ex2);
        }catch(ClassNotFoundException ex){
            rootCauseEx=ex;
            if(!name.equals(rootCauseEx.getMessage()))//��ǰclass�̳е�����û�ҵ�
                BootstrapUtils.getLogger().warn("ClassNotFoundException:"+rootCauseEx.getMessage()+",refered by "+name);
        }
        
        if(c==null){
            if(findFromParent){ //��������class,���������Լ���class
                return containerClassLoader.findAndLoadClass(name, true, currentModuleName);
            }else{
                if(rootCauseEx!=null)
                    throw rootCauseEx;
            }
        }
        
        return c;
    }
    
    private ClassNotFoundException throwClassNotFound(String name,NoClassDefFoundError ex2){
        ClassNotFoundException rootCauseEx;
        Throwable cause2 = ex2.getCause();
        if(cause2!=null){
            if(cause2.getClass().equals(ClassNotFoundException.class)){
                rootCauseEx=(ClassNotFoundException)cause2;
                if(!name.equals(rootCauseEx.getMessage()))//��ǰclass�̳е�����û�ҵ�
                    BootstrapUtils.getLogger().warn("ClassNotFoundException:"+rootCauseEx.getMessage()+",refered by "+name);
            }else{
                throw ex2;
            }
        }else{
            throw ex2;
        }
        return rootCauseEx;
    }
    
    @Override
    public URL findResource(String name) {
        return findResource2(name,true);
    }
    
    /**
     * ����resource
     * @param name
     * @param findFromParent  ���Լ�������classpath��������
     * @return
     */
    public URL findResource2(String name,boolean findFromParent) {
        URL url=super.findResource(name);
        if(findFromParent){
            url=super.findResource(name);
            if(url!=null)
                return url;
            else
                return containerClassLoader.findResourceFromDependModule(name,currentModuleName);
        }else{
            return url;
        }
    }
    
    @Override
    public Enumeration<URL> findResources(String name) throws IOException {
        return findResources2(name, true);
    }
    
    /**
     * ����resource
     * @param name
     * @param findFromParent ���Լ�������classpath��������
     * @return
     * @throws IOException
     */
    public Enumeration<URL> findResources2(String name,boolean findFromParent) throws IOException{
        if(findFromParent){
            Enumeration<URL> res = super.findResources(name);
            if(res!=null && res.hasMoreElements())
                return res;
            else
                return containerClassLoader.findResourcesFromDependModule(name,currentModuleName);
        }else{
            return super.findResources(name);
        }
    }
    
    @Override
    public String toString() {
        return "ModuleClassLoader:"+currentModuleName;
    }

    
}
