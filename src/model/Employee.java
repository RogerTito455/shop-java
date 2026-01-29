/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import main.Logable;

/**
 *
 * @author crtit
 */
public class Employee extends Person implements Logable {

    final private int employeeId = 123;
    final private String password = "test";

    public Employee(String name) {
        super(name);
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean login(int employeeId, String password) {
        return this.employeeId == employeeId && this.password.equals(password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Employee{" + "employeeId=" + employeeId + ", password=" + password + '}';
    }

}
