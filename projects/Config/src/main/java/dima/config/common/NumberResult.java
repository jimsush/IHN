package dima.config.common;

public class NumberResult<T extends Number> {
	public T number;
	public boolean result;
	public String errorMsg;
	public NumberResult(){
	}
	public NumberResult(boolean result,T number,String errorMsg){
		this.result=result;
		this.number=number;
		this.errorMsg=errorMsg;
	}
}
