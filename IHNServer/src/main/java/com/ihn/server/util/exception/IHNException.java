package com.ihn.server.util.exception;

public class IHNException extends RuntimeException {

	private static final long serialVersionUID = -8967836388898010436L;

	private int errorCode;
    private String[] source;
    
    public static final int CODE_SCHEDULE=1;
    
    public IHNException(int errorCode, String... source){
        super();
        this.errorCode = errorCode;
        this.source = source;
    }
    
    /**
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
