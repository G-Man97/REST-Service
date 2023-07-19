package com.gmalykhin.spring.rest.dao;

import com.gmalykhin.spring.rest.dto.AverageSalaryByDepartmentDTO;
import com.gmalykhin.spring.rest.entity.Department;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
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

        Query<?> query = session.createQuery("delete from Department " +
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
    public List<AverageSalaryByDepartmentDTO> getAverageSalaryByDepartment() {
        Session session = sessionFactory.getCurrentSession();

        Query<AverageSalaryByDepartmentDTO> query = session.createQuery("select new com.gmalykhin.spring.rest.dto" +
                ".AverageSalaryByDepartmentDTO(d.departmentName, avg(e.salary)) " +
                "from Department d join Employee e ON (d.id = e.department) group by d.departmentName", AverageSalaryByDepartmentDTO.class);

        return query.getResultList();
    }

    @Override
    public Department getDepartmentByDepartmentName(String name) {
        Session session = sessionFactory.getCurrentSession();

        Query<Department> query = session.createQuery("from Department where departmentName = :name", Department.class);
        query.setParameter("name", name);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
