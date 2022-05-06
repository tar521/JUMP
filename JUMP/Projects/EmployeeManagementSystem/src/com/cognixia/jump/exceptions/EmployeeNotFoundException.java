package com.cognixia.jump.exceptions;

public class EmployeeNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public EmployeeNotFoundException(int id) {
		super("Employee with id = " + id + " was not found.");
	}

}
