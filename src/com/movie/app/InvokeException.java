package com.movie.app;

import com.movie.state.ErrorState;



@SuppressWarnings("serial")
public class InvokeException extends Exception {
	
	private ErrorState status;
	private int state;
	private String message;
	
	public InvokeException(ErrorState status){
		super();
		this.status=status;
	}
	public InvokeException(int  status,String message){
		super();
		this.state=state;
		this.message=message;
	}
	
	public InvokeException(int  status,String message,Throwable throwable){
		super(throwable);
		this.state=state;
		this.message=message;
	}

	public ErrorState getStatus() {
		return status;
	}
	public String toString(){
		return "["+this.status.getState()+":"+this.status.getMessage()+"]";
	}
	public String toStateString(){
		return "["+this.state+":"+this.message+"]";
	}
	public int getState() {
		return state;
	}
	public String getMessage() {
		return message;
	}
	
}
