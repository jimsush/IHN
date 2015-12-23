package com.sf.base.exception;


/**
 * <p>
 * Title: SfException
 * </p>
 * <p>�쳣���࣬����Error Code,Source,cause,message
 * </p>
 * 
 * check [who date description]
 */
public class SfException extends RuntimeException {
    
    private static final long serialVersionUID = 5893725839548086058L;
    
    /**
     * δ������ڲ�����,{0}
     */
    public final static int INTERNAL_ERROR=0;
    
    /** ������ */
    private int errorCode;
    
    /** ������Ϣ��������Է������Դ�� */
    private String[] source;

    public SfException(int errorCode, String... source){
        super(getDefaultDetailMessage(errorCode,source)); //��Ҫ��һ��message,�����ӡ�������쳣��Ϣ���ڼ�,�����ڲ��
        this.errorCode = errorCode;
        this.source = source;
    }
    
    /**
     * ͨ��error code,source�õ�ȱʡ����ʾ��Ϣ
     * @param errorCode
     * @param source
     * @return
     */
    private static String getDefaultDetailMessage(int errorCode, String... source){
        if(source==null||source.length==0){
            return "code="+errorCode;
        }else{
            if(source.length==1){
                return "code="+errorCode+",source="+source[0];
            }else{
                StringBuilder sb=new StringBuilder();
                sb.append("code=").append(errorCode);
                for(int i=0;i<source.length;i++){
                    sb.append(",source=").append(source[i]);
                }
                return sb.toString();
            }
        }
    }
    
    public SfException(int errorCode,Throwable th,String... source) {
        super(th);
        this.errorCode = errorCode;
        this.source = source;
    }

    public SfException(String message) {
        super(message);
    }
    
    /**
     * ���쳣
     * @param th rootCause
     */
    public SfException(Throwable th) {
        super(th);
    }
    
    /**
     * ������
     * @return the errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * ������Ϣ��������Է������Դ��
     * @return the source
     */
    public String[] getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String[] source) {
        this.source = source;
    }
}
