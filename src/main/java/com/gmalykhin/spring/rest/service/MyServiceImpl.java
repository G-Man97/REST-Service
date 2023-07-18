package com.gmalykhin.spring.rest.service;

import com.gmalykhin.spring.rest.dao.DepartmentDAO;
import com.gmalykhin.spring.rest.dao.EmployeeDAO;
import com.gmalykhin.spring.rest.entity.Department;
import com.gmalykhin.spring.rest.entity.Employee;
import com.gmalykhin.spring.rest.exception_handling.IncorrectFieldData;
import com.gmalykhin.spring.rest.exception_handling.NoSuchEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MyServiceImpl implements MyService {

    private final EmployeeDAO employeeDAO;
    private final DepartmentDAO departmentDAO;

    @Autowired
    public MyServiceImpl(EmployeeDAO employeeDAO, DepartmentDAO departmentDAO) {
        this.employeeDAO = employeeDAO;
        this.departmentDAO = departmentDAO;
    }

    @Override
    @Transactional
    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployees();
    }

    @Override
    @Transactional
    public void saveEmployee(Employee employee) {
        employeeDAO.saveEmployee(employee);
    }

    @Override
    @Transactional
    public List<Department> getAllDepartments() {
        return departmentDAO.getAllDepartments();
    }

    @Override
    @Transactional
    public Employee getEmployee(int id) {
        return employeeDAO.getEmployee(id);
    }

    @Override
    @Transactional
    public void deleteEmployee(int id) {
        employeeDAO.deleteEmployee(id);
    }

    @Override
    @Transactional
    public Department getDepartment(int id) {
        return departmentDAO.getDepartment(id);
    }

    @Override
    @Transactional
    public void deleteDepartment(int id) {
        departmentDAO.deleteDepartment(id);
    }

    @Override
    @Transactional
    public void saveDepartment(Department department) {
        departmentDAO.saveDepartment(department);
    }

    @Override
    @Transactional
    public List<Object[]> getAvgSalaryByDepartment() {
        return departmentDAO.getAvgSalaryByDepartment();
    }

    @Override
    @Transactional
    public List<Object[]> getEmpByDepartment() {
        return departmentDAO.getEmpByDepartment();
    }

    @Override
    @Transactional
    public List<Object[]> searchEmployee(LocalDate fDate, LocalDate sDate) {
        return departmentDAO.searchEmployee(fDate, sDate);
    }

    @Override
    @Transactional
    public Employee checkEmployeesDepartmentFields (Employee employee) {
        int departmentId = employee.getDepartment().getId();
        if (this.getDepartment(departmentId) == null) {
            throw new NoSuchEntityException(departmentId);
        } else if (employee.getDepartment().getDepartmentName() != null
                || employee.getDepartment().getMinSalary() != null
                || employee.getDepartment().getMaxSalary() != null) {
            throw new IncorrectFieldData("Write only the id field for department");
        } else {
            employee.setDepartment(getDepartment(departmentId));
        }
        return employee;
    }

    @Override
    @Transactional
    public List<Employee> employeesInDepartment(int departmentId) {
        return employeeDAO.employeesInDepartment(departmentId);
    }
}
