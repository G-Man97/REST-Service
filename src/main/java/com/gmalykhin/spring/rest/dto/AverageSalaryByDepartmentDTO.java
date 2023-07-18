package com.gmalykhin.spring.rest.dto;

public class AverageSalaryByDepartmentDTO {
    private final String departmentName;
    private final double averageSalary;
    public AverageSalaryByDepartmentDTO (String departmentName, double averageSalary) {
        this.departmentName = departmentName;
        this.averageSalary = (double) Math.round(averageSalary * 100) / 100;
    }
    public String getDepartmentName() {
        return departmentName;
    }
    public double getAverageSalary() {
        return averageSalary;
    }
}
