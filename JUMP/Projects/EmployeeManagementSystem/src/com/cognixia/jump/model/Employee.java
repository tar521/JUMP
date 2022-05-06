package com.cognixia.jump.model;

public class Employee {
	
//	public class Address {
//		private String street;
//		private String street2;
//		private String city;
//		private String state;
//		private String zipCode;
//		
//		public Address() {
//			street = new String();
//			street2 = new String();
//			city = new String();
//			state = new String();
//			zipCode = new String();
//		}
//
//		public Address(String street, String city, String state, String zipCode) {
//			super();
//			this.street = street;
//			street2 = new String("");
//			this.city = city;
//			this.state = state;
//			this.zipCode = zipCode;
//		}
//
//		public Address(String street, String street2, String city, String state, String zipCode) {
//			super();
//			this.street = street;
//			this.street2 = street2;
//			this.city = city;
//			this.state = state;
//			this.zipCode = zipCode;
//		}
//
//		public String getStreet() {
//			return street;
//		}
//
//		public void setStreet(String street) {
//			this.street = street;
//		}
//
//		public String getStreet2() {
//			return street2;
//		}
//
//		public void setStreet2(String street2) {
//			this.street2 = street2;
//		}
//
//		public String getCity() {
//			return city;
//		}
//
//		public void setCity(String city) {
//			this.city = city;
//		}
//
//		public String getState() {
//			return state;
//		}
//
//		public void setState(String state) {
//			this.state = state;
//		}
//
//		public String getZipCode() {
//			return zipCode;
//		}
//
//		public void setZipCode(String zipCode) {
//			this.zipCode = zipCode;
//		}
//
//		public boolean isEmpty() {
//			if (street.isBlank() && street2.isBlank() && city.isBlank() && state.isBlank() && zipCode.isBlank()) {
//				return true;
//			}
//			return false;
//		}
//		
//		@Override
//		public String toString() {
//			return "Address: " + street + ", " + street2 + ", " + city + ", " + state
//					+ " " + zipCode;
//		}
//		
//		
//	}
	
	// EMPLOYEE CLASS
	
	private int id;
	private String name;
	private String department;
	private int salary;
	private String email;
	//private Address address;
	
	public Employee(int id, String name, String department, int salary, String email) {
		super();
		this.id = id;
		this.name = name;
		this.department = department;
		this.salary = salary;
		this.email = email;
		//address = new Address();
	}

//	public Employee(int id, String name, String department, int salary, String email, Address address) {
//		super();
//		this.id = id;
//		this.name = name;
//		this.department = department;
//		this.salary = salary;
//		this.email = email;
//		this.address = address;
//	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Employee [ID: " + id + ", Name: " + name + ", Department: " + department + ", Salary: " + salary
				+ ", Email: " + email + "]" ;
	}

//	@Override
//	public String toString() {
//		return "Employee [ID: " + id + ", Name: " + name + ", Department: " + department + ", Salary: " + salary
//				+ ", Email: " + email + "]" + "\n" + name + "'s " + address.toString();
//	}
	
}
