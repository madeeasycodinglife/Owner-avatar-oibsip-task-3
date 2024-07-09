package com.madeeasy.dao.impl;

import com.madeeasy.dao.AccountDAO;
import com.madeeasy.entity.Account;
import com.madeeasy.entity.User;
import com.madeeasy.util.JPAUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.NativeQuery;

public class AccountDAOImpl implements AccountDAO {

    private final SessionFactory sessionFactory;

    public AccountDAOImpl() {
        this.sessionFactory = JPAUtil.getSessionFactory();
    }

    @Override
    public User findByUserId(String userId) {
        try (Session session = this.sessionFactory.openSession()) {
            String sql = "SELECT * FROM `user` u WHERE u.id = :userId";
            NativeQuery<User> nativeQuery = session.createNativeQuery(sql, User.class);
            nativeQuery.setParameter("userId", userId);
            return nativeQuery.uniqueResult();
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }
        return null;
    }

    @Override
    public Account findByAccountId(String accountId) {
        try (Session session = this.sessionFactory.openSession()) {
            String sql = "SELECT * FROM account a WHERE a.id = :accountId";
            NativeQuery<Account> nativeQuery = session.createNativeQuery(sql, Account.class);
            nativeQuery.setParameter("accountId", accountId);
            return nativeQuery.uniqueResult();
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }
        return null;
    }

    @Override
    public void save(Account account) {
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            try {
                session.merge(account);
                transaction.commit();
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
