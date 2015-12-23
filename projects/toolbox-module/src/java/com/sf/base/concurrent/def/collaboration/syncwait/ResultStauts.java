package com.sf.base.concurrent.def.collaboration.syncwait;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Title: ResultStauts
 * </p>
 * <p>
 * Description: 轮询等待的结果状态
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public class ResultStauts implements Serializable{
    
    private static final long serialVersionUID = -3921479442773098246L;
    
    /** 是否结束 */
    private boolean finished;
    /** 是否成功/失败 */
    private boolean successed; 
    /** 是否timeout */
    private boolean timeout;   
    /*** 结果的附加信息 */
    private Map<String,Object> additionalInfo; 

    public ResultStauts(boolean finished){
        this.finished=finished;
    }
    
    public ResultStauts(boolean finished,boolean timeout){
        this.finished=finished;
        
        this.timeout=timeout;
        if(this.timeout)
            this.successed=false;
    }
    public ResultStauts(boolean finished,boolean timeout,boolean successed){
        this.finished=finished;
        this.successed=successed;
        this.timeout=timeout;
    }
    
    /** 是否结束 */
    public boolean isFinished() {
        return finished;
    }
    public void setFinished(boolean finished) {
        this.finished = finished;
    }
    /** 是否执行成功 */
    public boolean isSuccessed() {
        return successed;
    }
    public void setSuccessed(boolean successed) {
        this.successed = successed;
    }
    /** 结果的附加信息 */
    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }
    public void setAdditionalInfo(Map<String, Object> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
    public void addAdditionalInfoItem(String key,Object value){
        if(this.additionalInfo==null)
            this.additionalInfo=new HashMap<String,Object>();
        this.additionalInfo.put(key, value);
    }
    public Object getAdditionalInfoItem(String key){
        if(this.additionalInfo==null)
            return null;
        return this.additionalInfo.get(key);
    }
    public void setTimeout(boolean timeout) {
        this.timeout = timeout;
    }
    /** 是否执行超时 */
    public boolean isTimeout() {
        return timeout;
    };
    
}
