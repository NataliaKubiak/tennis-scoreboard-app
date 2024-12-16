package org.example.util;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

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
            // TODO: 16/12/2024 тут логгируем ошибку
            throw new RuntimeException();
        }
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
