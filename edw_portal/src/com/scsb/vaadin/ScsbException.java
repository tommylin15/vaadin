package com.scsb.vaadin;

/**
* 拋出子類異常，會自動被ErrorHandler處理
* @author Tommy
*
*/
public class ScsbException extends RuntimeException {
	private static final long serialVersionUID = -4012193489041611874L;

	public ScsbException() {
		super();
		// 	TODO Auto-generated constructor stub
	}
	
	public ScsbException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
	
	public ScsbException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	public ScsbException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}