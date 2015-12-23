
package com.sf.core.bootstrap.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.sf.core.bootstrap.impl.loader.ClassLoaderManager;
import com.sf.core.bootstrap.impl.module.ModuleEntity;

/**
 * <p>
 * Title: ConfigParser
 * </p>
 * <p>
 * Description: ����ģ������xml�ļ�
 * </p>
 * 
 * @author sufeng
 * created 2011-2-25 ����10:11:50
 * modified [who date description]
 * check [who date description]
 */
public class ConfigParser {
    

    private String CONFIG_FILE_PATH = null;
    
    /** ȫ��ϵͳ���� */
    private final static String SYSTEM_PARAM_TAG = "system-param";
    
    /** ģ�鿪ʼ��tag */
    private final static String MODULES_TAG = "modules";

    /** property��ֵ�� */
    private final static String PROPERTY_TAG = "property";
    private final static String NAME_ATTRIBUTE = "name";
    private final static String VALUE_ATTRIBUTE = "value";
     
    /**  ģ��ʵ������ */
    private final static String MODULE_CLASS_ATTRIBUTE = "moduleClass";

    /** ��ģ��ֱ��������ģ���б� */
    public static final String MODULE_DEPENDS="depends";
    
    /** ��ģ�����õĹ���jar,��λ��common/libs�� */
    public static final String MODULE_PUBLIC_JARS="publicJars";
    
    /**
     * ���е�ģ��
     */
    private Map<String,ModuleEntity> modules = new ConcurrentHashMap<String,ModuleEntity>();
    
    /**
     * ���е�ȫ�ֲ���
     */
    private Map<String, String> systemParams = new ConcurrentHashMap<String, String>();

    /**
     * ��ȡjar,xml��loader
     */
    private ModuleFileLoader moduleLoaders = new ModuleFileLoader();

    /**
     * �������������
     */
    private ClassLoaderManager classLoaderManager;
    
    public void setClassLoaderManager(ClassLoaderManager classLoaderManager) {
        this.classLoaderManager = classLoaderManager;
    }
    
    /**
     * ��ʼ��XML�ļ�����������XML�ļ�������Document
     */
    public void init(String homeDir,String productName) {
        CONFIG_FILE_PATH = homeDir + "\\modules\\"+productName+".xml";

        SAXBuilder builder = new SAXBuilder(false);
        Document doc = null;
        try {
            doc = builder.build(CONFIG_FILE_PATH);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        parserDocument(doc);
    }

    /**
     * ����jar�ļ�
     */
    public void loadJarAndConf(String homeDir) {
        for (Map.Entry<String, ModuleEntity> entry : modules.entrySet()) {
            String moduleName = entry.getKey();
            ModuleEntity moduleEntity=entry.getValue();

            // �õ���ģ���µ�jar
            List<URL> jarAndConf = moduleLoaders.getJarAndConf(homeDir, moduleName);
            classLoaderManager.addModuleURLs(moduleName,jarAndConf);
            
            // ����modules/lib�µ�jar
            List<URL> publicJars=moduleLoaders.getPublicJars(homeDir, moduleEntity.getPublicJars());
            classLoaderManager.addModulePublicURLs(moduleName, publicJars);
        }
    }

    /**
     * ��������XML�ļ���Document��
     * 
     * @param doc
     */
    @SuppressWarnings("unchecked")
    private void parserDocument(Document doc) {
        Element rootElement = doc.getRootElement();
        List children = rootElement.getChildren();
        if (CollectionUtils.isEmpty(children))
            return;
        for (Object object : children) {
            if (object instanceof Element) {
                Element element = (Element) object;
                String name = element.getName();
                if (SYSTEM_PARAM_TAG.equals(name)) {
                    parserParamElement(element);
                } else if (MODULES_TAG.equals(name)) {
                    List moduleObjects = element.getChildren();
                    if (CollectionUtils.isEmpty(moduleObjects))
                        continue;
                    interpreterModules(moduleObjects);
                }
            }
        }
    }

    /**
     * ����ģ��
     * @param modules
     */
    @SuppressWarnings("unchecked")
    private void interpreterModules(List moduleObjects) {
        for (Object moduleObject : moduleObjects) {
            if (moduleObject instanceof Element) {
                Element moduleElement = (Element) moduleObject;
                Attribute moduleClassAtt = moduleElement.getAttribute(MODULE_CLASS_ATTRIBUTE);
                if (moduleClassAtt == null || StringUtils.isEmpty(moduleClassAtt.getValue()))
                    throw new NullPointerException("Module class is null!");
                ModuleEntity moduleEntity = new ModuleEntity();
                Attribute nameAtt = moduleElement.getAttribute(NAME_ATTRIBUTE);
                if (nameAtt == null || StringUtils.isEmpty(nameAtt.getValue()))
                    throw new NullPointerException("Module name is null!");
                String name = nameAtt.getValue();
                moduleEntity.setName(name);
                String moduleClass = moduleClassAtt.getValue();
                moduleEntity.setModuleClass(moduleClass);
                
                String depends=moduleElement.getAttributeValue(MODULE_DEPENDS);
                moduleEntity.setDepends(depends);
                
                String publicJars=moduleElement.getAttributeValue(MODULE_PUBLIC_JARS);
                moduleEntity.setPublicJars(publicJars);
                
                // ��module������
                modules.put(moduleEntity.getName(),moduleEntity);

                // module�ڲ���service,property����
                List childrens = moduleElement.getChildren();
                if (CollectionUtils.isNotEmpty(childrens)) {
                    interpreterModuleChildren(name, childrens);
                }
            }
        }
    }

    /**
     * ����ģ����ģ�����
     * @param module
     * @param childrens
     */
    @SuppressWarnings("unchecked")
    private void interpreterModuleChildren(String moduleName, List childrens) {
        for (Object child : childrens) {
            if (child instanceof Element) {
                Element childElement = (Element) child;
                String childName = childElement.getName();
                if (PROPERTY_TAG.equals(childName)) {
                    parserModuleParamElement(childElement, moduleName);
                } 
            }
        }
    }

    /**
     * ����ȫ�ֲ���
     * 
     * @param paramElement
     */
    @SuppressWarnings("unchecked")
    private void parserParamElement(Element paramElement) {
        List children = paramElement.getChildren();
        if (CollectionUtils.isEmpty(children))
            return;
        for (Object object : children) {
            if (object instanceof Element) {
                Element element = (Element) object;
                Attribute nameAttr = element.getAttribute(NAME_ATTRIBUTE);
                Attribute valueAttr = element.getAttribute(VALUE_ATTRIBUTE);
                systemParams.put(nameAttr.getValue(), valueAttr.getValue());
            }
        }
    }

    /**
     * ����module����
     * 
     * @param paramElement
     */
    private void parserModuleParamElement(Element paramElement, String moduleName) {
        Attribute nameAttr = paramElement.getAttribute(NAME_ATTRIBUTE);
        Attribute valueAttr = paramElement.getAttribute(VALUE_ATTRIBUTE);
        ModuleEntity moduleEntity = modules.get(moduleName);
        Map<String, String> map = moduleEntity.getModuleParams();
        if (map == null) {
            map = new ConcurrentHashMap<String, String>();
            moduleEntity.setModuleParams(map);
        }
        map.put(nameAttr.getValue(), valueAttr.getValue());
    }
    
    /**
     * @return the global system params
     */
    public Map<String, String> getSystemParams() {
        return systemParams;
    }

    public ModuleEntity getModule(String moduleName) {
        return modules.get(moduleName);
    }
    
    public List<ModuleEntity> getAllModules(){
        List<ModuleEntity> list=new ArrayList<ModuleEntity>();
        list.addAll(modules.values());
        return list;
    }

}
