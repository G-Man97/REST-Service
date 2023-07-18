package com.gmalykhin.spring.rest.entity;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;


    @Column(name = "name" , unique = true)
    @Pattern(regexp = "([A-Za-z_]+)", message = "The name field must contains only A-Z, a-z or underscore symbols")
    @Size(min = 2, max = 25, message = " The name field must have min 2 symbols max 25 symbols ")
    private String departmentName;

    @Column(name = "min_salary")
    @DecimalMin(value = "500", message = " The minSalary field must have a minimum value of 500 ")
    @DecimalMax(value = "1000000", inclusive = false, message = " The minSalary field must have a minimum value of 999999.99 ")
    private Double minSalary;

    @Column(name = "max_salary")
    @DecimalMin(value = "500", message = " The maxSalary field must have a minimum value of 500 ")
    @DecimalMax(value = "1000000", inclusive = false, message = " The maxSalary field must have a minimum value of 999999.99 ")
    private Double maxSalary;

    @OneToMany(cascade = CascadeType.ALL , mappedBy = "department", orphanRemoval=true)
    @JsonIgnore
//  @JsonIgnoreProperties("department")
    private List<Employee> employee;

    public Department() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Double getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(Double minSalary) {
        this.minSalary = minSalary;
    }

    public Double getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(Double maxSalary) {
        this.maxSalary = maxSalary;
    }

    public List<Employee> getEmployee() {
        return employee;
    }

    public void setEmployee(List<Employee> employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return id == that.id && departmentName.equals(that.departmentName)
                && (Double.compare(minSalary, that.minSalary) == 0)
                && (Double.compare(maxSalary, that.maxSalary) == 0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, departmentName, minSalary, maxSalary);
    }
}
