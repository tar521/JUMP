package com.cognixia.jump.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.cognixia.jump.model.Employee;

public abstract class EmployeeManagerFile {

	/**
	 * Default constructor.
	 */
	public EmployeeManagerFile() {
		employeeSet = new HashMap<>();
	}

	/**
	 * Method to generate a unique email for each employee upon employee creation
	 * based on employee's name and ID in the case of duplicate username
	 * @param username
	 * @param id
	 * @return email
	 */
	public String generateUniqueEmail(String username, int id) {
		boolean sameNameOccur = false;
		String email;

		for (Map.Entry<Integer, Employee> e : employeeSet.entrySet()) {
			if (e.getValue().getName().equalsIgnoreCase(username)) {
				sameNameOccur = true;
				break;
			}
		}

		if (sameNameOccur) {
			email = new String(username.toLowerCase() + id + "@email.com");
		} else {
			email = new String(username.toLowerCase() + "@email.com");
		}

		return email;
	}

	/**
	 * Method to load or add a department to the working set
	 * 
	 * @param action
	 */
	public void updateDeptSet(String action) {
		String[] strArr = action.split(" ");

		switch (strArr[0]) {
		case "reload":
		case "load":
			loadDeptSet();
			break;
		case "add":
			deptSet.add(strArr[1]);
			break;
		default:
			System.out.println("Unrecognized action for set of departments.");
			break;
		}
	}

	/**
	 * Load set of departments. Also used to update the set when employees are
	 * removed
	 */
	private void loadDeptSet() {
		deptSet = new HashSet<String>();
		if (!employeeSet.isEmpty()) {
			for (Map.Entry<Integer, Employee> e : employeeSet.entrySet()) {
				deptSet.add(e.getValue().getDepartment());
			}
		}
	}

	/**
	 * Method used to ensure that employees have unique emails when the program is
	 * booted up
	 */
	public void emailDatabaseChecker() {

		Set<String> emails = new HashSet<String>();
		employeeSet.forEach((k, v) -> {
			if (!emails.add(v.getEmail()))
				v.setEmail(v.getName().toLowerCase() + v.getId() + "@email.com");
		});

	}

	/**
	 * Loads employee database from a file into the HashMap
	 * 
	 * @throws IOException
	 */
	public abstract void loadDB() throws IOException;

	/**
	 * Saves the database in the working directory as a text file (database.txt)
	 * 
	 * @throws IOException
	 */
	public abstract void saveDB() throws IOException;

	/**
	 * Getter method for accessing the employee database
	 * 
	 * @return employeeSet
	 */
	public HashMap<Integer, Employee> getEmployeeSet() {
		return employeeSet;
	}

	/**
	 * Getter method for accessing the department set
	 * 
	 * @return deptSet
	 */
	public Set<String> getDeptSet() {
		return deptSet;
	}

	/**
	 * Getter method for accessing idCounter
	 * 
	 * @return
	 */
	public static int getIdCounter() {
		return idCounter;
	}

	/**
	 * Setter method for setting idCounter
	 * 
	 * @param idCounter
	 */
	public static void setIdCounter(int idCounter) {
		EmployeeManagerFile.idCounter = idCounter;
	}

	protected static final String DATA_FILE_NAME = "database.txt";
	private static HashMap<Integer, Employee> employeeSet;
	private Set<String> deptSet;
	private static int idCounter = 0;
}
