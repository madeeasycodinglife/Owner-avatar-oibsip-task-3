package com.madeeasy.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class JPAUtil {

    public static SessionFactory getSessionFactory() {
        return new Configuration().configure().buildSessionFactory();
    }
}
