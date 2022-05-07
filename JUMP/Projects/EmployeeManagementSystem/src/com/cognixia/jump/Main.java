package com.cognixia.jump;

import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.cognixia.jump.exceptions.EmployeeNotFoundException;
import com.cognixia.jump.model.Employee;
import com.cognixia.jump.util.EmployeeManagerInMemory;

// CRUD Operations - Create Read Update Delete

// Create an EMS that allows us to:
// 1. view all employees
// 2. view particular employees
// 3. create employees
// 4. delete employees
// 5. update employees
// 6. view all employees in a singular department
// Expect: Console Based Menu

/*
 * Assignment:
 * - finish the EmployeeManagerInMemory {implement rest of methods}
 * - option 3 = create employee section of the menu
 * - send it over through slack (files, zip, or github)
 * 
 * Extra:
 * - Complete the menu
 * - Use files for database access
 * - Employee Addresses
 */

public class Main {

	private static EmployeeManagerInMemory manager;
	private static Scanner sc;

	public static void main(String[] args) {

		// Create EmployeeManager Object
		manager = new EmployeeManagerInMemory();
		
		// Try to load the database
		try {
            // Load database if it exists.
            manager.loadDB();
        } catch (IOException ex) {
            System.err.println("Error accessing the database file.");
            return;
        }
		
		// system scanner
		sc = new Scanner(System.in);

		// Go to main menu
		System.out.println("WELCOME TO THE EMPLOYEE MANAGEMENT SYSTEM (EMS)\n");
		mainMenu();

		// Try to save the database
		try {
            // Save the database before exiting.
            manager.saveDB();
        } catch (IOException ex) {
            System.err.println("Error: The database file could not be saved.");
        }
		
		System.out.println("\nTHANK YOU FOR USING TRISTRAM'S EMPLOYEE MANAGEMENT SYSTEM\n");
		System.out.println("GOODBYE");
	}

	// Method for the view of the main menu
	public static void mainMenu() {

		while (true) {

			try {
				System.out.println("\nPlease enter one of the following options :" + "\n1.) View Employees"
						+ "\n2.) Select Employee By Id" + "\n3.) Create Employee" + "\n4.) Update Employee"
						+ "\n5.) Delete Employee" + "\n6.) Exit");

				int option = sc.nextInt();
				sc.nextLine(); // getting rid of newline char

				switch (option) {
				case 1:
					viewEmployees();
					break;
				case 2:
					selectEmployee();
					break;
				case 3:
					employeeCreation();
					break;
				case 4:
					employeeUpdate();
					break;
				case 5:
					employeeDeletion();
					break;
				case 6:
					// Exiting, do nothing
					break;

				default:
					System.out.println("\nPlease enter a number between 1 and 6");
					break;
				}

				if (option == 6) {
					break;
				}

			} catch (InputMismatchException e) {
				sc.nextLine();
				System.out.println("\nPlease enter a number between 1 and 6");
			}
			
			try {
				manager.saveDB();
				System.out.println("autosave...");
			} catch (IOException e) {
				System.out.println("autosave failed...");
			}

		}

	}

	// Method to choose how to view employees - all or by department
	public static void viewEmployees() {

		while (true) {
			try {
				System.out.println("\nSelect one of the following:" + "\n1. Select all employees"
						+ "\n2. Select employees by department" + "\n3. Exit to return to main menu");

				int option = sc.nextInt();
				sc.nextLine();

				switch (option) {
				case 1:
					viewAllEmployees();
					break;
				case 2:
					viewEmployeeByDept();
					break;
				case 3:
					System.out.println("\nExiting view employees menu...");
					break;
				default:
					System.out.println("\nEnter number between 1 and 3.");
					break;
				}

				if (option == 3) {
					break;
				}

			} catch (InputMismatchException e) {
				System.out.println("\nEnter number between 1 and 3.");
			}
		}

	}

	// Method to show all employees in EMS
	public static void viewAllEmployees() {

		HashMap<Integer, Employee> employees = manager.getAllEmployees();

		if (employees.isEmpty()) {
			System.out.println("\nNo employees currently in EMS.");
		} else {

			for (Map.Entry<Integer, Employee> e : manager.getAllEmployees().entrySet()) {
				System.out.println(e.getValue());
			}
		}
		System.out.println();
	}

	// Method to view all employees in a single department
	public static void viewEmployeeByDept() {
		if (!manager.getAllEmployees().isEmpty()) {
			boolean exit = false;

			while (!exit) {
				try {
					System.out.println("\nCurrent departments:");
					for (String str : manager.getAllDepartments()) {
						System.out.println(str);
					}
					System.out.println("\nInput the department to view:");

					String dept = sc.nextLine();
					List<Employee> byDept = manager.getEmployeesByDepartment(dept);

					if (byDept != null) {
						byDept.forEach(System.out::println);
						System.out.println();
					}

				} catch (InputMismatchException e) {
					System.out.println("Input a valid code\n");
				}
				exit = true;
			}
		} else {
			System.out.println("\nNo employees currently in the EMS");
		}
	}

	// Method to select a single employee - perform possible actions
	public static void selectEmployee() {

		if (manager.getAllEmployees().isEmpty()) {
			System.out.println("\nNo employees in the EMS");
			return;
		}

		System.out.println("\nSELECT EMPLOYEE MENU:\n");

		while (true) {

			try {

				System.out.println("Input ID of employee to be viewed:");
				int upId = sc.nextInt();
				sc.nextLine();

				Employee upEmpl = manager.findEmployeeById(upId);

				System.out.println("The information for the employee is below:");
				System.out.println(upEmpl + "\n");
				System.out.println("Actions to perform on employee: ");
				System.out.println("1. Delete");
				System.out.println("2. Update");
				System.out.println("3. Exit to Main Menu");
				System.out.println("Please choose an action:");

				int choice = sc.nextInt();
				sc.nextLine();

				switch (choice) {
				case 1:
					manager.deleteEmployee(upId);
					break;
				case 2:
					updateInformation(upEmpl);
					break;
				case 3:
					System.out.println("Returning to main menu...\n");
					// Do Nothing
					break;
				default:
					System.out.println("Not a valid input.");
					break;

				}
				if (choice == 3) {
					return;
				}

			} catch (EmployeeNotFoundException e) {
				System.out.println("Employee of that id does not exist");
				System.out.println("Please try again:");
			} catch (InputMismatchException e) {
				sc.nextLine();
				System.out.println("\nPlease enter a number");
				continue;
			}

			if (!repeatAction("find")) {
				return;
			}
		}
	}

	// Method to create a new employee to EMS
	public static void employeeCreation() {

		System.out.println("\nEMPLOYEE CREATION: (enter \"EXIT\" to return to Main Menu)\n");

		while (true) {

			try {
				String name; // Holder for new employee name
				String dept; // Holder for new employee department
				int sal = 0; // Holder for new employee salary

				// New employee id will be set in manager method
				// New employee email will be generated base on first name

				// Prompt user for name, department, salary, and email.
				System.out.println("Input the name of the employee: (Formatted as: \"John\")");
				name = sc.nextLine();
				if (name.equalsIgnoreCase("EXIT")) {
					System.out.println("\nExiting to Main Menu...\n");
					return;
				}

				System.out.println("Input the department employee works: (Letter code)");
				dept = sc.nextLine();
				if (dept.equalsIgnoreCase("EXIT")) {
					System.out.println("\nExiting to Main Menu...\n");
					return;
				}

				System.out.println("Input the salary of the employee: (No commas)");
				if (sc.hasNextInt()) {
					sal = sc.nextInt(); 
					sc.nextLine();
				}
				else {
					if (sc.nextLine().equalsIgnoreCase("EXIT")) {
						System.out.println("\nExiting to Main Menu...");
						return;
					}
					else {
						throw new InputMismatchException();
					}
				}
				
				

				// Generate unique email address
				System.out.println("\nEmployee email is auto-generated.");
				System.out.print("New employee email: ");
				String email = manager.getUniqueEmail(name);
				System.out.println(email);

				boolean success;
				success = manager.createEmployee(new Employee(-1, name, dept.toUpperCase(), sal, email));
				if (success) {
					System.out.println("New employee, " + name + ", was successfully added.\n");
				} else {
					System.out.println("Employee could not be added.");
				}
			} catch (InputMismatchException e) {
				sc.nextLine();
				System.out.println("\nPlease enter a valid salary");
			}

			if (!repeatAction("create")) {
				return;
			}
		} // outer while

	}

	// Method to delete a current employee
	public static void employeeDeletion() {

		if (manager.getAllEmployees().isEmpty()) {
			System.out.println("\nNo Employees in EMS");
			return;
		}

		System.out.println("\nEMPLOYEE DELETION:\n");

		while (true) {

			try {

				System.out.println("Input ID of employee to be deleted:");
				int delId = sc.nextInt();
				sc.nextLine();

				Employee delEmpl = manager.findEmployeeById(delId);

				System.out.println("Is this the employee you wish to delete?");
				System.out.println(delEmpl + "\n");
				System.out.println("1. Yes");
				System.out.println("2. No");
				System.out.println("3. Exit Deletion Wizard");

				int delete = sc.nextInt();
				sc.nextLine();

				switch (delete) {
				case 1:
					System.out.println("Deleting employee...");
					manager.deleteEmployee(delId);
					break;
				case 2:
					// Not the correct employee, try again
					break;
				case 3:
					System.out.println("\nExiting to main menu...");
					break;
				default:
					System.out.println("\nNot a valid input.");
					break;

				}
				if (delete == 2) {
					continue;
				}
				if (delete == 3) {
					return;
				}

			} catch (EmployeeNotFoundException e) {
				System.out.println("Employee of that id does not exist");
				System.out.println("Please try again:");
				// continue;
			} catch (InputMismatchException e) {
				sc.nextLine();
				System.out.println("\nPlease enter a number");
			}

			if (!repeatAction("delete")) {
				return;
			}
		}
	}

	// Method to update a current employee
	public static void employeeUpdate() {

		if (manager.getAllEmployees().isEmpty()) {
			System.out.println("\nNo Employees in EMS");
			return;
		}

		System.out.println("\nEMPLOYEE UPDATE MENU:\n");

		while (true) {

			try {

				System.out.println("\nInput ID of employee to be updated:");
				int upId = sc.nextInt();
				sc.nextLine();

				Employee upEmpl = manager.findEmployeeById(upId);

				System.out.println("Is this the employee you wish to update?");
				System.out.println(upEmpl + "\n");
				System.out.println("1. Yes");
				System.out.println("2. No");
				System.out.println("3. Exit Update Wizard");

				int update = sc.nextInt();
				sc.nextLine();

				switch (update) {
				case 1:
					if (!updateInformation(upEmpl)) {
						update = 2;
					}
					break;
				case 2:
					// Not the correct employee, try again
					break;
				case 3:
					System.out.println("\nExiting to the main menu...");
					break;
				default:
					System.out.println("\nNot a valid input.");
					break;

				}
				if (update == 2) {
					continue;
				}
				if (update == 3) {
					break;
				}

			} catch (EmployeeNotFoundException e) {
				System.out.println("Employee of that id does not exist");
				System.out.println("Please try again:");
				continue;
			} catch (InputMismatchException e) {
				sc.nextLine();
				System.out.println("\nPlease enter a number");
				continue;
			}

			if (!repeatAction("update")) {
				return;
			}
		}
	}

	// Method to get info to update employee
	public static boolean updateInformation(Employee empl) {
		try {

			// Department update
			System.out.println("Input the department " + empl.getName() + " works: (Current department: "
					+ empl.getDepartment() + ")");
			empl.setDepartment(sc.nextLine());

			// Salary update
			System.out
					.println("Input the salary of " + empl.getName() + ": (Current salary: " + empl.getSalary() + ")");
			empl.setSalary(sc.nextInt());
			sc.nextLine();

			// Update employee with new information
			if (manager.updateEmployee(empl)) {
				System.out.println("\n" + empl.getName() + "'s information was updated.");
			} else {
				System.out.println("\n" + empl.getName() + "'s information could not be updated.");
			}
		} catch (InputMismatchException e) {
			sc.nextLine();
			System.out.println("\nPlease enter a valid salary");
			return false;
		}
		return true;
	}

	// Method used to prompt user to repeat action done
	public static boolean repeatAction(String action) {

		while (true) {
			System.out.println("\nWould you like to " + action + " another employee? (Enter Option)");
			System.out.println("1. Yes");
			System.out.println("2. No");

			try {
				int repeat = sc.nextInt();
				sc.nextLine();

				switch (repeat) {
				case 1:
					break;
				case 2:
					System.out.println("Returning to main menu...\n");
					break;
				default:
					System.out.println("Not a valid input.");
					break;
				}
				if (repeat == 1) {
					return true;
				}

				if (repeat == 2) {
					return false;
				}
			} catch (InputMismatchException e) {
				sc.nextLine();
				System.out.println("\nPlease enter 1 or 2");
			}
		}
	}

}
