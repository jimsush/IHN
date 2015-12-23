/*
 * $Id: ResourceFactory.java, 2015-2-21 ����05:43:12  Exp $
 * 
 * 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.resource;

import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import com.sf.base.exception.ExceptionUtils;

/**
 * <p>
 * Title: ResourceFactory
 * </p>
 * <p>
 * Description: ��Դ�ļ���ȡ����
 * </p>
 * 
 * @author 
 * created 2015-2-21 ����05:43:12
 * modified [who date description]
 * check [who date description]
 */
public class ResourceFactory {

	/**
	 * ���ػ�
	 */
	private Locale locale = Locale.getDefault();
	
	/**
	 * ��ǰ��OEMǰ׺
	 */
	private String oemName;
	
	/**
	 * ��ϵͳcoreģ����
	 */
	private String coreModuleName;
	
	/** 
	 * �����ļ��ĸ�Ŀ¼modules/
	 */
	private String rootPath = "modules/";
	
	private static final String CONF_PATH = "conf/";
	private static final String IMAGES_PATH = "images/";
	private static final String SOUNDS_PATH = "sounds/";

	/** 
	 * �����Ѿ����ص�ͼ���ļ���ͼ��url
	 */
	private Map<String, Icon> iconName_Icon = new ConcurrentHashMap<String, Icon>();

	/**
	 * ������ص���ԴURL
	 */
	public final static Map<String, URL> resourceCache = new ConcurrentHashMap<String, URL>();
	
	/**
     * ��Դ����
     */
    public ResourceFactory() {
    }
    
	/**
	 * ��Դ����
	 * @param defaultLocale Locale
	 */
	public ResourceFactory(Locale defaultLocale) {
        this.locale = defaultLocale;
    }
	
	/**
	 * ��Դ����
	 * @param coreModuleName ����ϵͳ��coreģ������
	 */
	public ResourceFactory(String coreModuleName) {
		this.coreModuleName=coreModuleName;
		if(this.coreModuleName==null){
		    this.coreModuleName="core"; //set to default name
		}
		
	    oemName = null;
	}
	
	/**
	 * coreģ����
	 * @return
	 */
	public String getCoreModuleName() {
        return coreModuleName;
    }
	
	/**
	 * ��ȡconf�ļ�,��modules/core/conf/Ŀ¼��ȡ
	 * @param fileName �ļ���
     * @return �ļ���URL
	 */
	public URL getConfFile(String fileName){
	    return getConfFile(this.coreModuleName, fileName);
	}
	
	/**
	 * ��ȡconf�ļ�����modules/module(����ģ��)/soundsĿ¼��ȡ
	 * @param module ����ģ��
	 * @param fileName �ļ���
	 * @return �ļ���URL
	 */
	public URL getConfFile(String module,String fileName){
        return getResource(new StringBuilder(rootPath).append(module).append("/").append(CONF_PATH)
                .append(fileName).toString());
    }

	/**
	 * ��ȡ�����ļ�·��
	 * 
	 * @param module
	 *            ģ������ eg. topo
	 * @param name
	 *            �ļ����ƣ����modules/module(����ģ��)/soundsĿ¼
	 * @return ·��URL
	 */
	public URL getSoundFile(String module, String fileName) {
		return getResource(new StringBuilder(rootPath).append(module).append("/").append(CONF_PATH).append(SOUNDS_PATH)
				.append(fileName).toString());
	}

	/**
	 * ��ȡiconName��·��
	 * 
	 * iconNameΪmodules/core/imagesĿ¼�µ����·��
	 * 
	 * @param iconName
	 *            �к�׺���磺device.png, framework/logo.jpg
	 * @return ·��
	 */
	public URL getIconUrl(String iconName) {
		URL url = getIconUrl(this.coreModuleName, iconName);
		if (url == null) {
			url = getDefaultIconUrl();
		}
		return url;
	}
	
	private URL getDefaultIconUrl(){
	    return getResource(new StringBuilder(rootPath).append(this.coreModuleName).append("/").append(CONF_PATH).append(IMAGES_PATH)
                .append("logo.gif").toString());
	}

	/**
	 * ͨ��ģ���ͼƬ���ֻ�ȡͼƬ·��,���modules/module(����ģ��)/imagesĿ¼
	 * 
	 * @param module
	 *            ģ��
	 * @param iconName
	 *            ͼƬ��
	 * @return ·��
	 */
	public URL getIconUrl(String module, String iconName) {
	    if(StringUtils.isEmpty(module))
	        module = this.coreModuleName;
		// ��modulesĿ¼�»�ȡ
		URL url = getResource(new StringBuilder(rootPath).append(module).append("/").append(CONF_PATH).append(IMAGES_PATH)
				.append(iconName).toString());
		if (url == null) {
			url = getDefaultIconUrl();
		}
		return url;
	}

	/**
	 * ��ȡ�ļ���·��
	 * 
	 * @param mapping
	 *            
	 * @return ·��
	 */
	/**
	 * ��ȡmapping��URL
	 * @param filePath �ļ�·�� "modules/Core/"��ʼ�������ļ���path
	 * @param fileName �ļ���
	 * @return �ļ���URL
	 */
	public URL getFile(String filePath,String fileName) {
		return getFile(this.coreModuleName, filePath, fileName);
	}

	/**
	 * ��ȡ�ļ���URL
     * @param filePath �ļ�·�� "modules/module������ģ�飩/"��ʼ�������ļ���path
     * @param fileName �ļ���
     * @param module ģ����
	 * @return  �ļ���URL
	 */
	public URL getFile(String module, String filePath,String fileName) {
	    return getResource(new StringBuilder(rootPath).append(module).append("/").append(CONF_PATH).append(filePath)
                .append(fileName).toString());
	}

	/**
	 * ��ȡIcon��iconNameΪmodules/core/imagesĿ¼�µ����·������
	 * 
	 * @param iconName
	 *            �к�׺���磺device.png,logo.jpg
	 * @return Icon
	 */
	public Icon getIcon(String iconName) {
	    return getIcon(this.coreModuleName, iconName);
	}

	/**
	 * ͨ��ģ���ͼƬ���ֻ�ȡͼƬ,���modules/module������ģ�飩/imagesĿ¼
	 * 
	 * @param module
	 *            ģ��
	 * @param iconName
	 *            ͼƬ��
	 * @return Icon
	 */
	public Icon getIcon(String module, String iconName) {
		// 1,�Ƿ��Ѿ�����
		if (iconName_Icon.containsKey(iconName)) {
			return iconName_Icon.get(iconName);
		}
		// ��META-INFĿ¼�»�ȡ
		Icon icon = new ImageIcon(getIconUrl(module, iconName));
		if (icon != null) {
			iconName_Icon.put(iconName, icon);
		}
		return icon;
	}

	/**
	 * ͨ��ģ���ͼƬ���ֻ�ȡͼƬ
	 * 
	 * @param module
	 *            ģ��
	 * @param iconName
	 *            ͼƬ��
	 * @return Image
	 */
	public Image getImage(String name) {
	    Icon icon = getIcon(this.coreModuleName, name);
		if (icon != null && icon instanceof ImageIcon) {
			return ((ImageIcon) icon).getImage();
		}
		return null;
	}

	/**
	 * ͨ��ģ���ͼƬ���ֻ�ȡͼƬ
	 * 
	 * @param module
	 *            ģ��
	 * @param iconName
	 *            ͼƬ��
	 * @return Image
	 */
	public Image getImage(String module, String iconName) {
		Icon icon = getIcon(module, iconName);
		if (icon != null && icon instanceof ImageIcon) {
			return ((ImageIcon) icon).getImage();
		}
		return null;
	}

	/**
	 * ���resource cache
	 */
	public void clearResourceCache() {
		resourceCache.clear();
	}

	/**
	 * ��ȡ��Դ���ļ����������ж�OEM��Ȼ���жϵ������ԡ�ÿ�γ����Ƿ���ڶ����ж��ļ�ϵͳ��Ȼ����classpath
	 * 
	 * @param name
	 *            �ļ�����
	 * @return ���ȵõ����ļ�url
	 */
	public URL getResource(String name) {
		URL url = resourceCache.get(name);
		if (url != null)
			return url;

		int filenameIndex = name.lastIndexOf('/') + 1;
		int extIndex = name.lastIndexOf('.');
		String rootPath = name.substring(0, filenameIndex);
		if (extIndex != -1) {
			String filename = name.substring(filenameIndex, extIndex);
			String ext = name.substring(extIndex);

			if (!ext.equals(".xml")) {
				url = getUrl(rootPath, ext, filename, "_", locale.getLanguage());
				if (url != null) {
					resourceCache.put(name, url);
					return url;
				}
			}
			url = getUrl(rootPath, ext, filename);
			if (url != null) {
				resourceCache.put(name, url);
				return url;
			}
			System.out.println("[info] ResourceFactory.getResource: unknown resource " + name);
		} else {
			url = toUrl(rootPath);
			if (url != null) {
				resourceCache.put(name, url);
				return url;
			}
			System.out.println("[info] ResourceFactory.getResource: unknown resource " + name);
		}
		return url;
	}

	/**
	 * ��class path(jar,classpath...)���ҳ����з�����������Դ�ļ�(class,xml...)
	 * @param springSupportedPathExpress spring֧�ֵ�·��ƥ���,eg: "classpath*:meta-inf/*.xml"
	 * @return ����������Resource,����ͨ��Resource��ȡ��inputStream, url...
	 */
	private List<Resource> scanResourceFromClassPath(String ... springSupportedPathExpress){
	    List<Resource> resources=new ArrayList<Resource>();
	    if(springSupportedPathExpress==null || springSupportedPathExpress.length==0)
            return resources;
	    
	    ResourcePatternResolver resourceResolver=new PathMatchingResourcePatternResolver();
	    for(int i=0; i<springSupportedPathExpress.length; i++){
            Resource[] curResources=null;
            try{
                curResources=resourceResolver.getResources(springSupportedPathExpress[i]);
            }catch(Exception ex){
                System.err.println(ExceptionUtils.getCommonExceptionInfo(ex));
            }
            if(curResources==null || curResources.length==0)
                continue;
            
            CollectionUtils.addAll(resources, curResources);
	    }
	    return resources;
	}
	
	/**
     * ��class path(jar,classpath...)���ҳ����з���������class
     * @param springSupportedPathExpress spring֧�ֵ�·��ƥ���,eg: "classpath*:meta-inf/*.xml"
     * @return ����������class full name,����com.sf.common.Class1
     */
	public List<String> scanClassNameFromClassPath(String ... springSupportedPathExpress){
	    List<String> clazzNames=new ArrayList<String>();
	    List<Resource> resources = scanResourceFromClassPath(springSupportedPathExpress);
	    if(CollectionUtils.isEmpty(resources))
	        return clazzNames;
	    //����spring�Ĺ������������ļ�
	    ResourcePatternResolver resourceResolver=new PathMatchingResourcePatternResolver();
	    MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourceResolver);
	    for(Resource res : resources){
	        try{
	            MetadataReader reader = readerFactory.getMetadataReader(res);
	            String clazzName = reader.getClassMetadata().getClassName();
	            clazzNames.add(clazzName);
	        }catch(Exception ex){
	            throw new RuntimeException(ex);
	        }
	    }
        return clazzNames;
	}
	
	/**
	 * ת�����Ƶ�URL
	 * 
	 * @param name
	 * @return
	 */
	private URL toUrl(String name) {
		URL url = null;
		try {
			File file = new File(name);
			if (file.exists()) {
				url = file.toURI().toURL();
			}
		} catch (Exception e) {
		    System.err.println(ExceptionUtils.getCommonExceptionInfo(e));
		}
		if (url == null) {
			url = ClassLoader.getSystemResource(name);
		}
		if (url == null) {
			url = getClass().getClassLoader().getResource(name);
			if (url == null && getClass().getClassLoader().getParent() != null) {
				url = getClass().getClassLoader().getParent().getResource(name);
			}
		}
		if (url == null) {
			url = Thread.currentThread().getContextClassLoader().getResource(
					name);
			if (url == null
					&& Thread.currentThread().getContextClassLoader()
							.getParent() != null) {
				url = Thread.currentThread().getContextClassLoader()
						.getParent().getResource(name);
			}
		}
		return url;
	}

	/**
	 * ��ȡ��Դ��url��������oem��
	 * @param root
	 * @param ext
	 * @param strs
	 * @return
	 */
	private URL getUrl(String root, String ext, String... strs) {
		URL url = null;
		StringBuilder basename = new StringBuilder(root);
		if (oemName != null) {
			basename.append(oemName).append("/");
		}
		for (String str : strs) {
			basename.append(str);
		}
		basename.append(ext);
		url = toUrl(basename.toString());
		if (url != null) {
			return url;
		}
		if (oemName != null) {
			basename = new StringBuilder(root);
			for (String str : strs) {
				basename.append(str);
			}
			basename.append(ext);
			url = toUrl(basename.toString());
			if (url != null) {
				return url;
			}
		}
		return url;
	}

    /**
     * ���ó���ı���Locale����
     * @param locale
     *            the locale to set
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * OEM����
     * @return the oemName
     */
    public String getOemName() {
        return oemName;
    }

    /**
     * @param oemName
     *            the oemName to set
     */
    public void setOemName(String oemName) {
        this.oemName = oemName;
    }

    /** 
     * ���icon����
     */
    public void reset() {
        iconName_Icon.clear();
    }
    
    /**
     * ��·��
     * @param rootPath
     */
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }
	
}
