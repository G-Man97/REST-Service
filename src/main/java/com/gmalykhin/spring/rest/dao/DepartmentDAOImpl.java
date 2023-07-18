package com.gmalykhin.spring.rest.dao;

import com.gmalykhin.spring.rest.entity.Department;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
@Repository
public class DepartmentDAOImpl implements DepartmentDAO{

    private final SessionFactory sessionFactory;
    @Autowired
    public DepartmentDAOImpl (SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Department> getAllDepartments() {
        Session session = sessionFactory.getCurrentSession();
        Query<Department> query = session.createQuery("from Department ", Department.class);

        return query.getResultList();
    }

    @Override
    public Department getDepartment(int id) {
        Session session = sessionFactory.getCurrentSession();

        return session.get(Department.class, id);
    }

    @Override
    public void deleteDepartment(int id) {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("delete from Department " +
                "where id = :departmentId");

        query.setParameter("departmentId", id);
        query.executeUpdate();
    }

    @Override
    public void saveDepartment(Department department) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(department);
    }

    @Override
    public List<Object[]> getAvgSalaryByDepartment() {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("select d.departmentName, round(avg(e.salary), 2) from Department d " +
                "join Employee e ON (d.id = e.department) group by d.departmentName");

        return query.getResultList();
    }

    @Override
    public List<Object[]> getEmpByDepartment() {
        Session session = sessionFactory.getCurrentSession();

        Query<Object[]> query = session.createQuery("select e.id, e.name, e.surname, e.salary, d.departmentName from Department d" +
                " join Employee e ON (d.id = e.department) order by d.departmentName", Object[].class);

        return query.list();
    }


    @Override
    public List<Object[]> searchEmployee(LocalDate fDate, LocalDate sDate) {
        Session session = sessionFactory.getCurrentSession();

        Query<Object[]> query = session.createQuery("select e.id, e.name, e.surname, e.salary, d.departmentName from Department d" +
                " join Employee e ON (d.id = e.department) where e.birthday between :fD and :sD order by d.departmentName", Object[].class);
        query.setParameter("fD", fDate);
        query.setParameter("sD", sDate);

        return query.list();
    }
}
