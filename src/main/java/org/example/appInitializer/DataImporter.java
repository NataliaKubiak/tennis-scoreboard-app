package org.example.appInitializer;

import lombok.experimental.UtilityClass;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@UtilityClass
public class DataImporter {

    public static void importData() {

        try (SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
             Session session = sessionFactory.openSession()) {

            session.beginTransaction();



            session.getTransaction().commit();
        }
    }
}
