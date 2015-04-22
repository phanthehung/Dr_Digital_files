package com.khoisang.khoisanglibary.exception;

public class OverrideException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1414086567861825856L;

	@Override
	public String getMessage() {
		return "Missing some methods, please check in package Protocol";
	}
}
