package com.gmalykhin.spring.rest.dao;

import com.gmalykhin.spring.rest.entity.Department;

import java.time.LocalDate;
import java.util.List;

public interface DepartmentDAO {
    List<Department> getAllDepartments();

    Department getDepartment(int id);

    void deleteDepartment(int id);

    void saveDepartment(Department department);

    List<Object[]> getAvgSalaryByDepartment();

    List<Object[]> getEmpByDepartment();

    List<Object[]> searchEmployee(LocalDate fDate, LocalDate sDate);
}
