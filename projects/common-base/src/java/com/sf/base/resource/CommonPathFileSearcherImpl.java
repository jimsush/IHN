/*
 * $Id: CommonPathFileSearcherImpl.java, 2015-1-7 下午02:23:05 Owner Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.resource;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.AntPathMatcher;

/**
 * <p>
 * Title: CommonPathFileSearcherImpl
 * </p>
 * <p>
 * Description: <br>
 * CommonPathFileSearcher接口实现类
 * </p>
 * 
 * @author 
 * created 2015-1-7 下午02:23:05
 * modified [who date description]
 * check [who date description]
 */
public class CommonPathFileSearcherImpl implements CommonPathFileSearcher {
    /**
     * 
     * (non-Javadoc)
     * @see com.sf.base.resource.CommonPathFileSearcher#search(java.lang.String, java.lang.String)
     */
    @Override
    public List<String> search(String searchFilePath, String fileNameMatchPattern) {
        List<String> results=new ArrayList<String>();
        search(searchFilePath,fileNameMatchPattern,results);
        return results;
    }

    private static class FileSearcherFilter implements FileFilter {
        private String fileNameMatchPattern;
        private AntPathMatcher antPathMatcher=new AntPathMatcher();
        public FileSearcherFilter(String fileNameMatchPattern) {
            this.fileNameMatchPattern = fileNameMatchPattern;
        }

        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
               return true;
            }
            String path=file.toString().replace("\\", "/");
            return antPathMatcher.match(fileNameMatchPattern, path);
        }
    }
    
    
    private void search(String filePath, String fileNameMatchPattern,List<String> results){
        File file = new File(filePath);
        File[] files = file.listFiles(new FileSearcherFilter(fileNameMatchPattern));
        for(File result:files){
            if(!result.isDirectory())
                results.add(result.toURI().getPath());
            else{
                search(result.getPath(),fileNameMatchPattern,results);
            }
                
        }
    }

}
