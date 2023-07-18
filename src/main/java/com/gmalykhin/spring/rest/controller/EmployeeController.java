package com.gmalykhin.spring.rest.controller;

import com.gmalykhin.spring.rest.dto.EmployeeDTO;
import com.gmalykhin.spring.rest.entity.Employee;
import com.gmalykhin.spring.rest.exception_handling.IncorrectFieldData;
import com.gmalykhin.spring.rest.exception_handling.NoSuchEntityException;
import com.gmalykhin.spring.rest.service.MyService;
import com.gmalykhin.spring.rest.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final MyService myService;

    @Autowired
    public EmployeeController(MyService myService) {
        this.myService = myService;
    }

    @GetMapping
    public List<Employee> showAllEmployees() {
        return myService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable int id) {
        Employee employee = myService.getEmployee(id);

        if (employee == null) {
            throw new NoSuchEntityException(id);
        }
        return employee;
    }

    @GetMapping("/by-department")
    public List<EmployeeDTO> getEmpByDept() {
        return myService.getEmpByDepartment();
    }

    @GetMapping("/search-employee/{fDate}/{sDate}")
    public List<EmployeeDTO> searchEmployee(@PathVariable String fDate, @PathVariable String sDate) throws DateTimeParseException {
        LocalDate localFD = LocalDate.parse(fDate);
        LocalDate localSD = LocalDate.parse(sDate);

        if (localFD.isAfter(localSD)) {
            List<EmployeeDTO> temp = myService.searchEmployee(localSD, localFD);
            if (temp.isEmpty()) {
                throw new NoSuchEntityException("No such employees found in Database");
            }
            return temp;
        } else if (myService.searchEmployee(localFD, localSD).isEmpty()) {
            throw new NoSuchEntityException("No such employees found in Database");
        }
        return myService.searchEmployee(localFD, localSD);
    }

    @PostMapping
    public Employee addNewEmployee(@Valid @RequestBody Employee employee, BindingResult bindingResult) {
        if (employee.getId() != 0) {
            throw new IncorrectFieldData("No need to write id field for POST method. Please write JSON without id field");
        }
        if (employee.getDepartment() == null || employee.getDepartment().getId() == 0) {
            throw new IncorrectFieldData("You must write department (only id field) for new employee");
        } else {
            employee = myService.checkEmployeesDepartmentFields(employee);
        }

        if (bindingResult.hasErrors()) {
            throw new IncorrectFieldData(Utils.errorsToString(bindingResult.getFieldErrors()));
        }

        Utils.checkBirthday(employee.getBirthday());
        Utils.checkEmployeesSalary(employee.getSalary(), employee.getDepartment());
        employee.setName(Utils.initCap(employee.getName()));
        employee.setSurname(Utils.initCap(employee.getSurname()));

        myService.saveEmployee(employee);
        return employee;
    }

    @PutMapping
    public Employee updateEmployee(@Valid @RequestBody Employee employee, BindingResult bindingResult) {
        int id = employee.getId();

        if (id == 0) {
            throw new NoSuchEntityException("To edit the employee you need write your employee id. The id can not be 0");
        }
        if (myService.getEmployee(id) == null) {
            throw new NoSuchEntityException(employee.getId());
        } else if (employee.getBirthday() != null) {
            throw new IncorrectFieldData("You can not edit the date of birth");
        }

        Employee repoEmployee = myService.getEmployee(id);

        if (employee.getName() == null) {
            employee.setName(repoEmployee.getName());
        }
        if (employee.getSurname() == null) {
            employee.setSurname(repoEmployee.getSurname());
        } else {
            employee.setSurname(Utils.initCap(employee.getSurname()));
        }
        if (employee.getSalary() == null) {
            employee.setSalary(repoEmployee.getSalary());
        }
        if (employee.getDepartment() == null) {
            if (repoEmployee.getDepartment() != null) {
                employee.setDepartment(repoEmployee.getDepartment());
            }
        } else {
            employee = myService.checkEmployeesDepartmentFields(employee);
        }

        if (bindingResult.hasErrors()) {
            throw new IncorrectFieldData(Utils.errorsToString(bindingResult.getFieldErrors()));
        }

        if (employee.getDepartment() != null
                && repoEmployee.getDepartment() != null
                && employee.getDepartment().getId() != repoEmployee.getDepartment().getId()) {

            if (employee.getDepartment().getMinSalary() > repoEmployee.getDepartment().getMaxSalary()) {
                employee.setSalary(employee.getDepartment().getMinSalary());
            }
            else if (employee.getDepartment().getMaxSalary() < repoEmployee.getDepartment().getMinSalary()) {
                employee.setSalary(employee.getDepartment().getMaxSalary());
            }
        }
        if (employee.getDepartment() != null) {
            Utils.checkEmployeesSalary(employee.getSalary(), employee.getDepartment());
        } else if (Double.compare(employee.getSalary(), repoEmployee.getSalary()) != 0) {
            throw new IncorrectFieldData("You can not edit the salary field because department is null");
        }

        employee.setBirthday(repoEmployee.getBirthday());

        if (!(employee.getName().equals(repoEmployee.getName()))) {
            employee.setName(Utils.initCap(employee.getName()));
        }
        if (!(employee.getSurname().equals(repoEmployee.getSurname()))) {
            employee.setSurname(Utils.initCap(employee.getSurname()));
        }
        if (!(employee.equals(repoEmployee))) {
            myService.saveEmployee(employee);
        }
        return employee;
    }

    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable int id) {
        if (myService.getEmployee(id) == null) {
            throw new NoSuchEntityException(id);
        }
        myService.deleteEmployee(id);
        return "Employee with ID = " + id + " was successfully deleted";
    }
}
