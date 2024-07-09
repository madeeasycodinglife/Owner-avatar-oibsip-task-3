package com.madeeasy.dao.impl;

import com.madeeasy.dao.UserDAO;
import com.madeeasy.entity.User;
import com.madeeasy.util.JPAUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

public class UserDAOImpl implements UserDAO {

    private SessionFactory sessionFactory = JPAUtil.getSessionFactory();

    @Override
    public void save(User user) {

        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            try {
                session.persist(user);
                transaction.commit();
                System.out.println("User saved successfully !!");
            } catch (ConstraintViolationException exception) {
                System.out.println("Constraint Violation : " + exception.getMessage());
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
            }
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        }
    }
}
