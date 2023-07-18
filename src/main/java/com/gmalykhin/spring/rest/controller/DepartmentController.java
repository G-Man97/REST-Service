package com.gmalykhin.spring.rest.controller;

import com.gmalykhin.spring.rest.dto.AverageSalaryByDepartmentDTO;
import com.gmalykhin.spring.rest.entity.Department;
import com.gmalykhin.spring.rest.entity.Employee;
import com.gmalykhin.spring.rest.exception_handling.IncorrectFieldData;
import com.gmalykhin.spring.rest.exception_handling.NoSuchEntityException;
import com.gmalykhin.spring.rest.service.MyService;
import com.gmalykhin.spring.rest.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final MyService myService;

    @Autowired
    public DepartmentController(MyService myService) {
        this.myService = myService;
    }

    @GetMapping
    public List<Department> showAllDepartments() {
        return myService.getAllDepartments();
    }

    @GetMapping("/{id}")
    public Department getDepartment(@PathVariable int id) {
        Department department = myService.getDepartment(id);

        if (department == null) {
            throw new NoSuchEntityException(id);
        }
        return department;
    }

    @GetMapping("/average-salary-by-department")
    public List<AverageSalaryByDepartmentDTO> getAvgSalaryByDept() {
        return myService.getAvgSalaryByDepartment();
    }

    @PostMapping
    public Department addNewDepartment(@Valid @RequestBody Department department, BindingResult bindingResult) {
        if (department.getId() != 0) {
            throw new IncorrectFieldData("No need to write id field for POST method. Please write JSON without id field");
        }

        if (bindingResult.hasErrors()) {
            throw new IncorrectFieldData(Utils.errorsToString(bindingResult.getFieldErrors()));
        }

        Utils.checkDepartmentMinMaxSalary(department.getMinSalary(), department.getMaxSalary());
        department.setDepartmentName(department.getDepartmentName().toUpperCase());

        try {
            myService.saveDepartment(department);
            return department;
        } catch (Exception e) {
            throw new IncorrectFieldData("Value of departmentName field must be unique");
        }
    }

    @PutMapping
    public String updateDepartment(@Valid @RequestBody Department department, BindingResult bindingResult) {

        int id = department.getId();
        boolean minSalaryFlag = true, maxSalaryFlag = true;

        if (id == 0) {
            throw new NoSuchEntityException("To edit department you need write department id. Id can not be 0");
        }
        if (myService.getDepartment(id) == null) {
            throw new NoSuchEntityException(department.getId());
        }

        Department repoDepartment = myService.getDepartment(id);

        if (department.getDepartmentName() == null) {
            department.setDepartmentName(repoDepartment.getDepartmentName());
        }
        if (department.getMinSalary() == null) {
            department.setMinSalary(repoDepartment.getMinSalary());
            minSalaryFlag = false;
        }
        if (department.getMaxSalary() == null) {
            department.setMaxSalary(repoDepartment.getMaxSalary());
            maxSalaryFlag = false;
        }

        if (bindingResult.hasErrors()) {
            throw new IncorrectFieldData(Utils.errorsToString(bindingResult.getFieldErrors()));
        }

        if (minSalaryFlag || maxSalaryFlag) {
            Utils.checkDepartmentMinMaxSalary(department.getMinSalary(), department.getMaxSalary());
        }

        department.setDepartmentName(department.getDepartmentName().toUpperCase());
        String info = "The department was successfully updated.";

        if (!(department.equals(repoDepartment))) {
            myService.saveDepartment(department);

            if (Double.compare(repoDepartment.getMinSalary(), department.getMinSalary()) != 0
                    || Double.compare(repoDepartment.getMaxSalary(), department.getMaxSalary()) != 0) {

                List<Employee> empsInDept = myService.employeesInDepartment(id);
                minSalaryFlag = false;
                maxSalaryFlag = false;

                for (Employee e : empsInDept) {
                    if (e.getSalary() < department.getMinSalary()) {
                        e.setSalary(department.getMinSalary());
                        myService.saveEmployee(e);
                        minSalaryFlag = true;
                    } else if (e.getSalary() > department.getMaxSalary()) {
                        e.setSalary(department.getMaxSalary());
                        myService.saveEmployee(e);
                        maxSalaryFlag = true;
                    }
                }
                if (minSalaryFlag || maxSalaryFlag) {
                    info += " One or more employees had their salary changed in accordance " +
                            "with the minimum and maximum salaries for this department";
                }
            }
        }
        return info;
    }

    @DeleteMapping("/{id}")
    public String deleteDepartment(@PathVariable int id) {
        if (myService.getDepartment(id) == null) {
            throw new NoSuchEntityException(id);
        }
        myService.deleteDepartment(id);
        return "Department with ID = " + id + " was successfully deleted";
    }
}
