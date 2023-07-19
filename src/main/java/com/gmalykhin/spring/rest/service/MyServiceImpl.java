package com.gmalykhin.spring.rest.service;

import com.gmalykhin.spring.rest.dao.DepartmentDAO;
import com.gmalykhin.spring.rest.dao.EmployeeDAO;
import com.gmalykhin.spring.rest.dto.AverageSalaryByDepartmentDTO;
import com.gmalykhin.spring.rest.dto.EmployeeDTO;
import com.gmalykhin.spring.rest.entity.Department;
import com.gmalykhin.spring.rest.entity.Employee;
import com.gmalykhin.spring.rest.exception_handling.IncorrectFieldData;
import com.gmalykhin.spring.rest.exception_handling.NoSuchEntityFoundInDBException;
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
    public List<EmployeeDTO> searchEmployee(LocalDate fDate, LocalDate sDate) {
        return employeeDAO.searchEmployee(fDate, sDate);
    }

    @Override
    @Transactional
    public List<AverageSalaryByDepartmentDTO> getAvgSalaryByDepartment() {
        return departmentDAO.getAverageSalaryByDepartment();
    }

    @Override
    @Transactional
    public List<EmployeeDTO> getAllEmployeesByDepartments() {
        return employeeDAO.getEmpByDepartment();
    }

    @Override
    @Transactional
    public List<Employee> employeesInDepartment(int departmentId) {
        return employeeDAO.employeesInDepartment(departmentId);
    }

    @Override
    @Transactional
    public void existenceOfTheDepartmentWithSuchNameInDB(String departmentName) throws IncorrectFieldData {
        if (departmentDAO.getDepartmentByDepartmentName(departmentName) != null) {
            throw new IncorrectFieldData("The value of the departmentName field must be unique");
        }
    }

    /* Проверка - существует ли департамент с таким id, а так же какие поля указаны
     * Для удобства ввода и во избежание ошибок ввода осталено только поле id у департамента,
     * остальные поля заполняются автоматически далее по коду.
     * Поэтому если указаны лишние поля, то выбрасывается исключение
     */
    @Override
    @Transactional
    public Employee checkEmployeesDepartmentFields (Employee employee) {
        int departmentId = employee.getDepartment().getId();
        if (this.getDepartment(departmentId) == null) {
            throw new NoSuchEntityFoundInDBException(departmentId);
        } else if (employee.getDepartment().getDepartmentName() != null
                || employee.getDepartment().getMinSalary() != null
                || employee.getDepartment().getMaxSalary() != null) {
            throw new IncorrectFieldData("Write only the id field for department");
        } else {
            employee.setDepartment(getDepartment(departmentId));
        }
        return employee;
    }

    /* Проверка если поменялось значение minSalary или maxSalary в department, то берется список
     * employee в этом department и у каждого проверяется попадание salary в диапазон minSalary - maxSalary.
     * Если salary меньше, чем minSalary, тогда salary присваивается значение minSalary.
     * Если salary больше, чем maxSalary, тогда salary присваивается значение maxSalary.
     */
    @Override
    @Transactional
    public boolean checkEmpsSalaryIfMinOrMaxSalaryWasEdited(Department department) {

        List<Employee> employeesInDept = this.employeesInDepartment(department.getId());
        boolean minSalaryFlag = false;
        boolean maxSalaryFlag = false;

        if (!employeesInDept.isEmpty()) {
            for (Employee e : employeesInDept) {
                if (e.getSalary() < department.getMinSalary()) {
                    e.setSalary(department.getMinSalary());
                    this.saveEmployee(e);
                    minSalaryFlag = true;
                } else if (e.getSalary() > department.getMaxSalary()) {
                    e.setSalary(department.getMaxSalary());
                    this.saveEmployee(e);
                    maxSalaryFlag = true;
                }
            }
        }
        return minSalaryFlag || maxSalaryFlag;
    }
}
