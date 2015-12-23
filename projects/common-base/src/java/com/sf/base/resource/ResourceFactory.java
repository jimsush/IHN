/*
 * $Id: ResourceFactory.java, 2015-2-21 下午05:43:12  Exp $
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
 * Description: 资源文件读取工厂
 * </p>
 * 
 * @author 
 * created 2015-2-21 下午05:43:12
 * modified [who date description]
 * check [who date description]
 */
public class ResourceFactory {

	/**
	 * 本地化
	 */
	private Locale locale = Locale.getDefault();
	
	/**
	 * 当前的OEM前缀
	 */
	private String oemName;
	
	/**
	 * 子系统core模块名
	 */
	private String coreModuleName;
	
	/** 
	 * 配置文件的根目录modules/
	 */
	private String rootPath = "modules/";
	
	private static final String CONF_PATH = "conf/";
	private static final String IMAGES_PATH = "images/";
	private static final String SOUNDS_PATH = "sounds/";

	/** 
	 * 缓存已经加载的图标文件和图标url
	 */
	private Map<String, Icon> iconName_Icon = new ConcurrentHashMap<String, Icon>();

	/**
	 * 缓存加载的资源URL
	 */
	public final static Map<String, URL> resourceCache = new ConcurrentHashMap<String, URL>();
	
	/**
     * 资源工厂
     */
    public ResourceFactory() {
    }
    
	/**
	 * 资源工厂
	 * @param defaultLocale Locale
	 */
	public ResourceFactory(Locale defaultLocale) {
        this.locale = defaultLocale;
    }
	
	/**
	 * 资源工厂
	 * @param coreModuleName 本子系统的core模块名称
	 */
	public ResourceFactory(String coreModuleName) {
		this.coreModuleName=coreModuleName;
		if(this.coreModuleName==null){
		    this.coreModuleName="core"; //set to default name
		}
		
	    oemName = null;
	}
	
	/**
	 * core模块名
	 * @return
	 */
	public String getCoreModuleName() {
        return coreModuleName;
    }
	
	/**
	 * 获取conf文件,从modules/core/conf/目录获取
	 * @param fileName 文件名
     * @return 文件的URL
	 */
	public URL getConfFile(String fileName){
	    return getConfFile(this.coreModuleName, fileName);
	}
	
	/**
	 * 获取conf文件，从modules/module(具体模块)/sounds目录获取
	 * @param module 具体模块
	 * @param fileName 文件名
	 * @return 文件的URL
	 */
	public URL getConfFile(String module,String fileName){
        return getResource(new StringBuilder(rootPath).append(module).append("/").append(CONF_PATH)
                .append(fileName).toString());
    }

	/**
	 * 获取声音文件路径
	 * 
	 * @param module
	 *            模块名称 eg. topo
	 * @param name
	 *            文件名称，相对modules/module(具体模块)/sounds目录
	 * @return 路径URL
	 */
	public URL getSoundFile(String module, String fileName) {
		return getResource(new StringBuilder(rootPath).append(module).append("/").append(CONF_PATH).append(SOUNDS_PATH)
				.append(fileName).toString());
	}

	/**
	 * 获取iconName的路径
	 * 
	 * iconName为modules/core/images目录下的相对路径
	 * 
	 * @param iconName
	 *            有后缀，如：device.png, framework/logo.jpg
	 * @return 路径
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
	 * 通过模块和图片名字获取图片路径,相对modules/module(具体模块)/images目录
	 * 
	 * @param module
	 *            模块
	 * @param iconName
	 *            图片名
	 * @return 路径
	 */
	public URL getIconUrl(String module, String iconName) {
	    if(StringUtils.isEmpty(module))
	        module = this.coreModuleName;
		// 从modules目录下获取
		URL url = getResource(new StringBuilder(rootPath).append(module).append("/").append(CONF_PATH).append(IMAGES_PATH)
				.append(iconName).toString());
		if (url == null) {
			url = getDefaultIconUrl();
		}
		return url;
	}

	/**
	 * 获取文件的路径
	 * 
	 * @param mapping
	 *            
	 * @return 路径
	 */
	/**
	 * 获取mapping的URL
	 * @param filePath 文件路径 "modules/Core/"开始到具体文件的path
	 * @param fileName 文件名
	 * @return 文件的URL
	 */
	public URL getFile(String filePath,String fileName) {
		return getFile(this.coreModuleName, filePath, fileName);
	}

	/**
	 * 获取文件的URL
     * @param filePath 文件路径 "modules/module（具体模块）/"开始到具体文件的path
     * @param fileName 文件名
     * @param module 模块名
	 * @return  文件的URL
	 */
	public URL getFile(String module, String filePath,String fileName) {
	    return getResource(new StringBuilder(rootPath).append(module).append("/").append(CONF_PATH).append(filePath)
                .append(fileName).toString());
	}

	/**
	 * 获取Icon，iconName为modules/core/images目录下的相对路径名称
	 * 
	 * @param iconName
	 *            有后缀，如：device.png,logo.jpg
	 * @return Icon
	 */
	public Icon getIcon(String iconName) {
	    return getIcon(this.coreModuleName, iconName);
	}

	/**
	 * 通过模块和图片名字获取图片,相对modules/module（具体模块）/images目录
	 * 
	 * @param module
	 *            模块
	 * @param iconName
	 *            图片名
	 * @return Icon
	 */
	public Icon getIcon(String module, String iconName) {
		// 1,是否已经加载
		if (iconName_Icon.containsKey(iconName)) {
			return iconName_Icon.get(iconName);
		}
		// 从META-INF目录下获取
		Icon icon = new ImageIcon(getIconUrl(module, iconName));
		if (icon != null) {
			iconName_Icon.put(iconName, icon);
		}
		return icon;
	}

	/**
	 * 通过模块和图片名字获取图片
	 * 
	 * @param module
	 *            模块
	 * @param iconName
	 *            图片名
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
	 * 通过模块和图片名字获取图片
	 * 
	 * @param module
	 *            模块
	 * @param iconName
	 *            图片名
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
	 * 清除resource cache
	 */
	public void clearResourceCache() {
		resourceCache.clear();
	}

	/**
	 * 获取资源的文件名，首先判断OEM，然后判断地域、语言。每次尝试是否存在都先判断文件系统，然后是classpath
	 * 
	 * @param name
	 *            文件名称
	 * @return 优先得到的文件url
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
	 * 从class path(jar,classpath...)中找出所有符合条件的资源文件(class,xml...)
	 * @param springSupportedPathExpress spring支持的路径匹配符,eg: "classpath*:meta-inf/*.xml"
	 * @return 符合条件的Resource,可以通过Resource获取到inputStream, url...
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
     * 从class path(jar,classpath...)中找出所有符合条件的class
     * @param springSupportedPathExpress spring支持的路径匹配符,eg: "classpath*:meta-inf/*.xml"
     * @return 符合条件的class full name,比如com.sf.common.Class1
     */
	public List<String> scanClassNameFromClassPath(String ... springSupportedPathExpress){
	    List<String> clazzNames=new ArrayList<String>();
	    List<Resource> resources = scanResourceFromClassPath(springSupportedPathExpress);
	    if(CollectionUtils.isEmpty(resources))
	        return clazzNames;
	    //调用spring的工具类来搜索文件
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
	 * 转换名称到URL
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
	 * 获取资源的url（考虑了oem）
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
     * 设置程序的本地Locale参数
     * @param locale
     *            the locale to set
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * OEM名称
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
     * 清除icon缓存
     */
    public void reset() {
        iconName_Icon.clear();
    }
    
    /**
     * 根路径
     * @param rootPath
     */
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }
	
}
