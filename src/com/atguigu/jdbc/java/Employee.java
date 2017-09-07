package com.atguigu.jdbc.java;

public class Employee {
	private int id;
	private double sal;
	private String email;
	
	
	public Employee() {
		super();
	}

	public Employee(int id, double sal, String email) {
		super();
		this.id = id;
		this.sal = sal;
		this.email = email;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getSal() {
		return sal;
	}
	public void setSal(double sal) {
		this.sal = sal;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", sal=" + sal + ", email=" + email + "]";
	}
	

}
