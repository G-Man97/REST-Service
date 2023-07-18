package com.gmalykhin.spring.rest.dao;

import com.gmalykhin.spring.rest.entity.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class EmployeeDAOImpl implements EmployeeDAO{

    private final SessionFactory sessionFactory;
    @Autowired
    public EmployeeDAOImpl (SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
        public List<Employee> getAllEmployees() {
        Session session = sessionFactory.getCurrentSession();

        Query<Employee> query = session.createQuery("from Employee", Employee.class);

        return query.getResultList();
    }

    @Override
    public void saveEmployee(Employee employee) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(employee);
    }

    @Override
    public Employee getEmployee(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Employee.class, id);
    }

    @Override
    public void deleteEmployee(int id) {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("delete from Employee " +
                "where id = :employeeId");

        query.setParameter("employeeId", id);
        query.executeUpdate();
    }

    @Override
    public List<Employee> employeesInDepartment(int departmentId) {
        Session session = sessionFactory.getCurrentSession();

        Query<Employee> query = session.createQuery("from Employee where department.id = :id", Employee.class);
        query.setParameter("id", departmentId);

        return query.getResultList();
    }
}
