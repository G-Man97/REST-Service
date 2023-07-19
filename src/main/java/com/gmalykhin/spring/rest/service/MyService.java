package com.gmalykhin.spring.rest.service;

import com.gmalykhin.spring.rest.dto.AverageSalaryByDepartmentDTO;
import com.gmalykhin.spring.rest.dto.EmployeeDTO;
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

    List<AverageSalaryByDepartmentDTO> getAvgSalaryByDepartment();

    List<EmployeeDTO> getAllEmployeesByDepartments();

    List<EmployeeDTO> searchEmployee(LocalDate fDate, LocalDate sDate);

    void existenceOfTheDepartmentWithSuchNameInDB(String departmentName);

    Employee checkEmployeesDepartmentFields (Employee employee);

    List<Employee> employeesInDepartment (int departmentId);

    boolean checkEmpsSalaryIfMinOrMaxSalaryWasEdited(Department department);
}
