package org.example.tennisscoreboard.dao;

import lombok.Cleanup;
import lombok.experimental.UtilityClass;
import org.example.tennisscoreboard.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@UtilityClass
public class DataImporter {

    public static void importData() {

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            session.beginTransaction();



            session.getTransaction().commit();
        }
    }
}
