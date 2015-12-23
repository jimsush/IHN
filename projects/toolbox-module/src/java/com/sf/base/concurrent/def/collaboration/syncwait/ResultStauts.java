package com.sf.base.concurrent.def.collaboration.syncwait;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Title: ResultStauts
 * </p>
 * <p>
 * Description: ��ѯ�ȴ��Ľ��״̬
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public class ResultStauts implements Serializable{
    
    private static final long serialVersionUID = -3921479442773098246L;
    
    /** �Ƿ���� */
    private boolean finished;
    /** �Ƿ�ɹ�/ʧ�� */
    private boolean successed; 
    /** �Ƿ�timeout */
    private boolean timeout;   
    /*** ����ĸ�����Ϣ */
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
    
    /** �Ƿ���� */
    public boolean isFinished() {
        return finished;
    }
    public void setFinished(boolean finished) {
        this.finished = finished;
    }
    /** �Ƿ�ִ�гɹ� */
    public boolean isSuccessed() {
        return successed;
    }
    public void setSuccessed(boolean successed) {
        this.successed = successed;
    }
    /** ����ĸ�����Ϣ */
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
    /** �Ƿ�ִ�г�ʱ */
    public boolean isTimeout() {
        return timeout;
    };
    
}
