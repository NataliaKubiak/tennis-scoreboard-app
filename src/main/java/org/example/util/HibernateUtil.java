package org.example.util;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

@Log4j2
@UtilityClass
public class HibernateUtil {

    @Getter
    private static SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration()
                    .setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy())
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();
        } catch (Throwable ex) {
            log.error("Initial SessionFactory creation failed: {}", String.valueOf(ex));
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
