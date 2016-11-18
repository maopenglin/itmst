package com.cmstop;

public class CustomerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5914167708181425529L;

	int code;

	public CustomerException(String msg, int code_) {
		
		super(msg);
		this.code=code_;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
