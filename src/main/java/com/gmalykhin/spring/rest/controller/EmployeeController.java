package com.gmalykhin.spring.rest.controller;

import com.gmalykhin.spring.rest.dto.EmployeeDTO;
import com.gmalykhin.spring.rest.entity.Employee;
import com.gmalykhin.spring.rest.exception_handling.IdFieldIsZero;
import com.gmalykhin.spring.rest.exception_handling.IdFieldInPostMethod;
import com.gmalykhin.spring.rest.exception_handling.IncorrectFieldData;
import com.gmalykhin.spring.rest.exception_handling.NoSuchEntityFoundInDBException;
import com.gmalykhin.spring.rest.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import static com.gmalykhin.spring.rest.util.Utils.*;

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

        List<Employee> allEmployee = myService.getAllEmployees();
        if (allEmployee.isEmpty()) {
            throw new NoSuchEntityFoundInDBException();
        }
        return allEmployee;
    }

    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable String id) throws NumberFormatException {
        int employeeId = Integer.parseInt(id.trim());
        Employee employee = myService.getEmployee(employeeId);

        if (employee == null) {
            throw new NoSuchEntityFoundInDBException(employeeId);
        }
        return employee;
    }

    @GetMapping("/by-department")
    public List<EmployeeDTO> getEmpByDept() {

        List<EmployeeDTO> result = myService.getAllEmployeesByDepartments();

        if (result.isEmpty()) {
            throw new NoSuchEntityFoundInDBException();
        }
        return result;
    }

    @GetMapping("/search-for-employees-born-in/{birthDate}")
    public List<EmployeeDTO> searchEmployee(@PathVariable String birthDate) throws DateTimeParseException {

        LocalDate localBirthDate = LocalDate.parse(birthDate.trim());
        List<EmployeeDTO> result = myService.searchEmployee(localBirthDate, localBirthDate);

        if (result.isEmpty()) {
            throw new NoSuchEntityFoundInDBException();
        }
        return result;
    }

    @GetMapping("/search-for-employees-born-in/{firstDate}/{secondDate}")
    public List<EmployeeDTO> searchEmployee(@PathVariable String firstDate, @PathVariable String secondDate) throws DateTimeParseException {

        LocalDate localFirstDate = LocalDate.parse(firstDate.trim());
        LocalDate localSecondDate = LocalDate.parse(secondDate.trim());

        // Проверка - если первая дата в параметре позже, чем вторая, то передаем их в другом порядке далее в метод
        if (localFirstDate.isAfter(localSecondDate)) {

            List<EmployeeDTO> employees = myService.searchEmployee(localSecondDate, localFirstDate);

            if (employees.isEmpty()) {
                throw new NoSuchEntityFoundInDBException();
            }
            return employees;
        }

        List<EmployeeDTO> employees = myService.searchEmployee(localFirstDate, localSecondDate);

        if (employees.isEmpty()) {
            throw new NoSuchEntityFoundInDBException();
        }
        return employees;
    }

    @PostMapping
    public ResponseEntity<Employee> addNewEmployee(@Valid @RequestBody Employee employee, BindingResult bindingResult) {

        if (employee.getId() != 0) {
            throw new IdFieldInPostMethod();
        }

        checkEntityFieldsIfNull(employee);

        if (employee.getDepartment().getId() == 0) {
            throw new IncorrectFieldData("You must write the department (only id field) for a new employee");
        } else {
            employee = myService.checkEmployeesDepartmentFields(employee);
        }

        // Проверка на ошибки валидации полей entity
        if (bindingResult.hasErrors()) {
            throw new IncorrectFieldData(errorsToString(bindingResult.getFieldErrors()));
        }

        checkBirthday(employee.getBirthday());
        checkEmployeesSalary(employee.getSalary(), employee.getDepartment());
        employee.setName(initCap(employee.getName()));
        employee.setSurname(initCap(employee.getSurname()));

        myService.saveEmployee(employee);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @PutMapping
    public Employee updateEmployee(@Valid @RequestBody Employee employee, BindingResult bindingResult) {

        int id = employee.getId();

        if (id <= 0) {
            throw new IdFieldIsZero(employee);
        }
        if (myService.getEmployee(id) == null) {
            throw new NoSuchEntityFoundInDBException(employee.getId());
        } else if (employee.getBirthday() != null) {
            throw new IncorrectFieldData("You can not edit the date of birth");
        }

        if (bindingResult.hasErrors()) {
            throw new IncorrectFieldData(errorsToString(bindingResult.getFieldErrors()));
        }

        Employee repoEmployee = myService.getEmployee(id);

        checkEntityFieldsIfNullThenFill(employee, repoEmployee);

        if (employee.getDepartment() != null) {

            if (employee.getDepartment() != repoEmployee.getDepartment()) {
                employee = myService.checkEmployeesDepartmentFields(employee);
            }

            if (repoEmployee.getDepartment() != null
                    && employee.getDepartment().getId() != repoEmployee.getDepartment().getId()) {

                // Если работнику поменяли департамент на новый и минимальная зарплата нового департамента
                // больше, чем максимальная зарплата предыдущего департамента, то работнику устанавливается зарплата,
                // соответствущая минимальной зарплпте нового департамента (например, повышение по службе)
                if (employee.getDepartment().getMinSalary() > repoEmployee.getDepartment().getMaxSalary()
                        && employee.getSalary() < employee.getDepartment().getMinSalary()) {
                    employee.setSalary(employee.getDepartment().getMinSalary());
                }

                // Если работнику поменяли департамент на новый и максимальная зарплата нового департамента
                // меньше, чем минимальная зарплата предыдущего департамента, то работнику устанавливается зарплата,
                // соответствущая максимальной зарплпте нового департамента (например, понижение по службе)
                else if (employee.getDepartment().getMaxSalary() < repoEmployee.getDepartment().getMinSalary()
                        && employee.getSalary() > employee.getDepartment().getMaxSalary()) {
                    employee.setSalary(employee.getDepartment().getMaxSalary());
                }
            }

            checkEmployeesSalary(employee.getSalary(), employee.getDepartment());

        } else if (Double.compare(employee.getSalary(), repoEmployee.getSalary()) != 0) {
            throw new IncorrectFieldData("You can not edit the salary field because the department field is null");
        }

        employee.setName(initCap(employee.getName()));
        employee.setSurname(initCap(employee.getSurname()));

        // Проверка на то, были ли вообще произведены какие-либо изменения с редактируемым объектом
        // Если да, то происходит запись в БД, иначе - возвращется редактируемый объект
        if (!(employee.equals(repoEmployee))) {
            myService.saveEmployee(employee);
        }
        return employee;
    }

    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable String id) throws NumberFormatException {

        int intId = Integer.parseInt(id.trim());
        Employee employee = myService.getEmployee(intId);

        if (employee == null) {
            throw new NoSuchEntityFoundInDBException(intId);
        }
        myService.deleteEmployee(intId);
        return "Employee with ID = " + id + " was successfully deleted";
    }
}
