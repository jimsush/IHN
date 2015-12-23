/*
 * $Id: JarPathFileSearcherImpl.java, 2015-1-7 下午02:23:47 Owner Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.resource;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ResourceUtils;

import com.sf.base.exception.SfException;

/**
 * <p>
 * Title: JarPathFileSearcherImpl
 * </p>
 * <p>
 * Description: <br>
 * JarPathFileSearcher接口实现类
 * </p>
 * 
 * @author 
 */
public class JarPathFileSearcherImpl implements JarPathFileSearcher {

    /**
     * 
     * (non-Javadoc)
     * @see com.sf.base.resource.JarPathFileSearcher#search(java.lang.String, java.lang.String)
     */
    @Override
    public List<String> search(String jarPath, String fileNameMatchPattern) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        Resource rootDirResource = new FileSystemResource(jarPath);

        JarFile jarFile;
        String jarFileUrl;
        String rootEntryPath;
        boolean newJarFile;

        jarFile = null;
        jarFileUrl = null;
        rootEntryPath = null;
        newJarFile = false;
        List result = new ArrayList(8);
        try {
            java.net.URLConnection con = rootDirResource.getURL().openConnection();
            if (con instanceof JarURLConnection) {
                JarURLConnection jarCon = (JarURLConnection) con;
                jarCon.setUseCaches(false);
                jarFile = jarCon.getJarFile();
                jarFileUrl = jarCon.getJarFileURL().toExternalForm();
                JarEntry jarEntry = jarCon.getJarEntry();
                rootEntryPath = jarEntry == null ? "" : jarEntry.getName();
            } else {
                String urlFile = rootDirResource.getURL().getFile();
                int separatorIndex = urlFile.indexOf("!/");
                if (separatorIndex != -1) {
                    jarFileUrl = urlFile.substring(0, separatorIndex);
                    rootEntryPath = urlFile.substring(separatorIndex + "!/".length());
                    jarFile = getJarFile(jarFileUrl);
                } else {
                    jarFile = new JarFile(jarPath);
                    jarFileUrl = urlFile;
                    rootEntryPath = "";
                }
                newJarFile = true;
            }
            Enumeration entries;
            if (!"".equals(rootEntryPath) && !rootEntryPath.endsWith("/"))
                rootEntryPath = rootEntryPath + "/";

            entries = jarFile.entries();
            do {
                if (!entries.hasMoreElements())
                    break;
                JarEntry entry = (JarEntry) entries.nextElement();
                String entryPath = entry.getName();
                if (entryPath.startsWith(rootEntryPath)) {
                    String relativePath = entryPath.substring(rootEntryPath.length());
                    if (antPathMatcher.match(fileNameMatchPattern, relativePath)) {
                        Resource rootResource = rootDirResource.createRelative(relativePath);
                        result.add(rootResource.getURI().toString());
                    }

                }
            } while (true);
        } catch (Exception ex) {
            throw new SfException(ex);
        } finally {
            try {
                if (newJarFile)
                    jarFile.close();
            } catch (Exception ex) {
                throw new SfException(ex);
            }
        }
        return result;
    }

    /**
     * 从url获取JarFile
     * @param jarFileUrl
     * @return
     * @throws IOException
     */
    private JarFile getJarFile(String jarFileUrl) throws IOException {
        if (jarFileUrl.startsWith("file:"))
            try {
                return new JarFile(ResourceUtils.toURI(jarFileUrl).getSchemeSpecificPart());
            } catch (URISyntaxException ex) {
                return new JarFile(jarFileUrl.substring("file:".length()));
            }
        else
            return new JarFile(jarFileUrl);
    }

}
