
package com.sf.base.log.def;


/**
 * <p>
 * Title: SimpleLogger
 * </p>
 * <p>
 * Description:使用system.out对logger的实现,方便在logger未初始化前和单元测试中使用
 * </p>
 * 
 * @author sufeng
 * modified [who date description]
 * check [who date description]
 */
public class SimpleLogger implements Logger{

    /**
     * 日志名
     */
    private String name;
    public SimpleLogger(String name){
        this.name=name;
    }
    
    @Override
    public void debug(String s) {
        out(s);
    }

    @Override
    public void debug(String s, Object obj) {
        out(s,obj);
    }

    @Override
    public void debug(String s, Object[] aobj) {
        out(s,aobj);
    }

    @Override
    public void debug(String s, Throwable throwable) {
        out(s,throwable);
    }

    @Override
    public void debug(String s, Object obj, Object obj1) {
        out(s,obj,obj1);
    }

    @Override
    public void error(String s) {
        out(s);
    }

    @Override
    public void error(String s, Object obj) {
        out(s,obj);
    }

    @Override
    public void error(String s, Object[] aobj) {
        out(s,aobj);
    }

    @Override
    public void error(String s, Throwable throwable) {
        out(s,throwable);
    }

    @Override
    public void error(String s, Object obj, Object obj1) {
        out(s,obj,obj1);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void info(String s) {
        out(s);
    }

    @Override
    public void info(String s, Object obj) {
        out(s,obj);
    }

    @Override
    public void info(String s, Object[] aobj) {
        out(s,aobj);
    }

    @Override
    public void info(String s, Throwable throwable) {
        out(s,throwable);
    }

    @Override
    public void info(String s, Object obj, Object obj1) {
        out(s,obj,obj1);
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public void trace(String s) {
        out(s);
    }

    @Override
    public void trace(String s, Object obj) {
        out(s,obj);
    }

    @Override
    public void trace(String s, Object[] aobj) {
        out(s,aobj);
    }

    @Override
    public void trace(String s, Throwable throwable) {
        out(s,throwable);
    }

    @Override
    public void trace(String s, Object obj, Object obj1) {
        out(s,obj,obj1);
    }

    @Override
    public void warn(String s) {
        out(s);
    }

    @Override
    public void warn(String s, Object obj) {
        out(s,obj);
    }

    @Override
    public void warn(String s, Object[] aobj) {
        out(s,aobj);
    }

    @Override
    public void warn(String s, Throwable throwable) {
        out(s,throwable);
    }

    @Override
    public void warn(String s, Object obj, Object obj1) {
        out(s,obj,obj1);
    }
    
    private void out(String s){
        System.out.println(s);
    }
    
    private void out(String s,Object obj){
        out(s+","+obj);
    }
    
    private void out(String s,Object[] obj){
        if(obj==null || obj.length==0){
            out(s);
            return;
        }
        StringBuilder sb=new StringBuilder();
        sb.append(s);
        for(int i=0;i<obj.length;i++)
            sb.append(",").append(obj[i]);
        
        out(sb.toString());
    }
    
    private void out(String s,Object obj,Object obj1){
        out(s+","+obj+","+obj1);
    }
    
    private void out(String s, Throwable throwable){
        out(s);
        throwable.printStackTrace();
    }

}
