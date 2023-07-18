package com.gmalykhin.spring.rest.dto;

public class EmployeeDTO {
    private final int id;
    private final String name;
    private final String surname;
    private final double salary;
    private final String departmentName;

    public EmployeeDTO(int id, String name, String surname, double salary, String departmentName) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.salary = salary;
        this.departmentName = departmentName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public double getSalary() {
        return salary;
    }

    public String getDepartmentName() {
        return departmentName;
    }
}
