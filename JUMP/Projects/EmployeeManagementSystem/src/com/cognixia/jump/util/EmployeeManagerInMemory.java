package com.cognixia.jump.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import com.cognixia.jump.exceptions.EmployeeNotFoundException;
import com.cognixia.jump.model.Employee;

public class EmployeeManagerInMemory extends EmployeeManagerFile implements EmployeeManager {
	
	/**
	 * Loads employee database from a file into the HashMap
	 */
	@Override
	public void loadDB() throws IOException {
		System.out.print("Reading Database...");
        
        File dataFile = new File(DATA_FILE_NAME);
        if (!dataFile.exists()) {
            System.out.println("No " + DATA_FILE_NAME + " file found. A new empty "
                    + DATA_FILE_NAME + " will be created.");
            return;
        }
        
        Scanner inFile = new Scanner(new FileReader(DATA_FILE_NAME));
        
        String line = "";
        
        while (inFile.hasNextLine()) {
            line = inFile.nextLine();
            String[] lineParts = line.split("_");
            int id = Integer.parseInt(lineParts[0]);
            String name = lineParts[1];
            String dept = lineParts[2];
            int salary = Integer.parseInt(lineParts[3]);
            String email = lineParts[4];
            
            int idBuffer = 0; // for duplicate id #'s
            
            // Import employees with unique ID's
            if (getEmployeeSet().containsKey(id)) {
            	idBuffer = 1;
            }
            Employee empl = new Employee(id + idBuffer, name, dept, salary, email);
            getEmployeeSet().put(id + idBuffer, empl);
        }
        
        // Email check in event that 'database.txt' was updated outside EMS
        emailDatabaseChecker();
        
        // Set the reviewIdCounter to be one greater than the largest id in the database().
        if (getEmployeeSet().keySet().size() > 0)
            setIdCounter(Collections.max(getEmployeeSet().keySet()).intValue());
        
        inFile.close();
        updateDeptSet("load");
        
        System.out.println("Done.\n");
	}

	/**
	 * Save the database in the working directory as a text file (database.txt)
	 */
	@Override
	public void saveDB() throws IOException {
		PrintWriter out = new PrintWriter(DATA_FILE_NAME);
        
        for (Employee e : getEmployeeSet().values()) {
            out.println(e.getId() + "_" +
                               e.getName() + "_" +
                               e.getDepartment() + "_" +
                               e.getSalary() + "_" +
                               e.getEmail());
        }
        
        out.close();
		
	}

	/**
	 * Getter method for accessing the employee database
	 * @return employeeSet
	 */
	@Override
	public HashMap<Integer, Employee> getAllEmployees() {
		return getEmployeeSet();
	}
	
	/**
	 * Getter method for accessing the department set
	 * @return deptSet
	 */
	public Set<String> getAllDepartments() {
		return getDeptSet();
	}

	/**
	 * Method that searches for an employee by id
	 * @param id
	 * @return employee
	 */
	@Override
	public Employee findEmployeeById(int id) throws EmployeeNotFoundException {

		for (Map.Entry<Integer, Employee> e : getEmployeeSet().entrySet()) {
			if (e.getValue().getId() == id) {
				return e.getValue();
			}
		}
		throw new EmployeeNotFoundException(id);
	}

	/**
	 * Method to create an employee in the database with given parameters
	 * @param empl
	 * @return boolean
	 */
	@Override
	public boolean createEmployee(Employee empl) {

		// If department is updated, try to add department to set of departments
		String temp = new String("add " + empl.getDepartment().trim());
		updateDeptSet(temp);
		
		// reset id to be unique using the counter
		setIdCounter(getIdCounter() + 1);
		empl.setId(getIdCounter());

		getEmployeeSet().put(empl.getId(), empl);

		return getEmployeeSet().containsKey(empl.getId());
	}

	/**
	 * Method to remove an employee from the database
	 * @param id
	 * @return boolean
	 */
	@Override
	public boolean deleteEmployee(int id) {

		Employee temp = getEmployeeSet().get(id);

		if (temp != null) {

			temp = getEmployeeSet().remove(id);
			System.out.println("Employee: " + temp.toString() + "\nSuccessfully removed");
			updateDeptSet("reload");
			return true;
		}

		System.out.println("Employee with id: " + id + " does not exist.");

		return false;
	}

	/**
	 * Method that updates an existing employee.
	 * User can only update department field and salary field.
	 * @param empl
	 * @return boolean
	 */
	@Override
	public boolean updateEmployee(Employee empl) {

		// If department is updated, try to add department to set of departments
		String temp = new String("add " + empl.getDepartment().trim());
		updateDeptSet(temp);
		
		Employee comp = getEmployeeSet().get(empl.getId());

		if (comp != null) {
			if (empl.getName().equalsIgnoreCase(comp.getName())) {
				getEmployeeSet().put(empl.getId(), empl);

				return getEmployeeSet().containsKey(empl.getId());
			} else {
				System.out.print("Employee could not be updated: ");
				System.out.println("Cannot change employee name entirely.\n");
				System.out.println("If you wish to correct employee's name, first");
				System.out.println("delete employee then reenter employee information.");
			}
		} else {
			System.out.print("Employee couldn't not be updated: ");
			System.out.println("Employee of ID: " + empl.getId() + " : does not exist");
		}
		return false;
	}

	/**
	 * Method to return a list of employees in a certain department
	 * @param dept
	 * @return List<Employee>
	 */
	@Override
	public List<Employee> getEmployeesByDepartment(String dept) {

		if (getDeptSet().contains(dept.toUpperCase()) && !getDeptSet().isEmpty()) {

			List<Employee> byDept = new ArrayList<Employee>();
			for (Map.Entry<Integer, Employee> e : getEmployeeSet().entrySet()) {
				byDept.add(e.getValue());
			}

			List<Employee> ret = byDept.stream().filter((e) -> dept.equalsIgnoreCase(e.getDepartment()))
					.collect(Collectors.toList());
			return ret;
		} else if (getDeptSet().isEmpty()) {
			System.out.println("There aren't any employees in the system");
		} else {
			System.out.println("Department does not exist\n");
		}

		return null;
	}
	
	/**
	 * Method that calls an email generator
	 * @param username
	 * @return email
	 */
	public String getUniqueEmail(String username) {
		return generateUniqueEmail(username, getIdCounter() + 1);
	}

	
}
