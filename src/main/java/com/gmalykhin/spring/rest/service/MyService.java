package com.gmalykhin.spring.rest.service;

import com.gmalykhin.spring.rest.entity.Department;
import com.gmalykhin.spring.rest.entity.Employee;

import java.time.LocalDate;
import java.util.List;

public interface MyService {

    List<Employee> getAllEmployees();

    void saveEmployee(Employee employee);

    List<Department> getAllDepartments();

    Employee getEmployee(int id);
    void deleteEmployee(int id);

    Department getDepartment(int id);

    void deleteDepartment(int id);

    void saveDepartment(Department department);

    List<Object[]> getAvgSalaryByDepartment();

    List<Object[]> getEmpByDepartment();

    List<Object[]> searchEmployee(LocalDate fDate, LocalDate sDate);

    Employee checkEmployeesDepartmentFields (Employee employee);

    List<Employee> employeesInDepartment (int departmentId);
}