package com.gmalykhin.spring.rest.dao;

import com.gmalykhin.spring.rest.dto.AverageSalaryByDepartmentDTO;
import com.gmalykhin.spring.rest.entity.Department;

import java.util.List;

public interface DepartmentDAO {
    List<Department> getAllDepartments();

    Department getDepartment(int id);

    void deleteDepartment(int id);

    void saveDepartment(Department department);

    List<AverageSalaryByDepartmentDTO> getAverageSalaryByDepartment();

    Department getDepartmentByDepartmentName(String departmentName);
}
