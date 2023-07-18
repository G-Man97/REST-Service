package com.gmalykhin.spring.rest.util;

import com.gmalykhin.spring.rest.entity.Department;
import com.gmalykhin.spring.rest.exception_handling.IncorrectFieldData;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.util.List;

public final class Utils {

    private Utils() {
        throw new UnsupportedOperationException("This is an utility class and cannot be instantiated");
    }

    private static final LocalDate LOW_BOUND_DATE = LocalDate.now().minusYears(60);

    private static final LocalDate UP_BOUND_DATE = LocalDate.now().minusYears(18).plusDays(1);

    public static String initCap (String str) {
        return str.substring(0,1).toUpperCase()
                + str.substring(1).toLowerCase();
    }

    public static void checkBirthday (LocalDate birthday) {
        if(!(birthday.isBefore(UP_BOUND_DATE) && birthday.isAfter(LOW_BOUND_DATE))) {
            throw new IncorrectFieldData("The employee must be over 18 years old and under 60 years old");
        }
    }

    public static String errorsToString(List<FieldError> fieldErrors) {
        String errorMessage = "";
        for (FieldError fE: fieldErrors) {
            errorMessage += fE.getDefaultMessage() + "\n";
        }
        return errorMessage;
    }

    public static void checkEmployeesSalary (Double salary, Department department) {
        if (salary < department.getMinSalary()) {
            throw new IncorrectFieldData("Salary must be > value of minSalary field of employee's department");
        } else if (salary > department.getMaxSalary()) {
            throw new IncorrectFieldData("Salary must be < value of maxSalary field of employee's department");
        }
    }

    public static void checkDepartmentMinMaxSalary (Double minSalary, Double maxSalary) {
        if ((minSalary >= maxSalary) || (maxSalary - minSalary) < 500) {
            throw new IncorrectFieldData("The value of minSalary field must be > then the value of maxSalary field. " +
                    "The range between these two values must be at least 500");
        }
    }

}
