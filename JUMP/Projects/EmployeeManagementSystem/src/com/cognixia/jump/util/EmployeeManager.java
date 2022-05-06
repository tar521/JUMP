package com.cognixia.jump.util;

import java.util.HashMap;
import java.util.List;

import com.cognixia.jump.exceptions.EmployeeNotFoundException;
import com.cognixia.jump.model.Employee;

public interface EmployeeManager {

	// Change to a HashMap
	public HashMap<Integer, Employee> getAllEmployees();
	
	public Employee findEmployeeById(int id) throws EmployeeNotFoundException;
	
	public boolean createEmployee(Employee empl);
	
	public boolean deleteEmployee(int id);
	
	public boolean updateEmployee(Employee empl);
	
	public List<Employee> getEmployeesByDepartment(String dept);
	
}
