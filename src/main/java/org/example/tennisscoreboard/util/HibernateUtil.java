package org.example.tennisscoreboard.util;

import lombok.experimental.UtilityClass;
import org.example.tennisscoreboard.entity.Match;
import org.example.tennisscoreboard.entity.Player;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {

        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());

        //сюда вставляем классы-модели
        //configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Player.class);
        configuration.addAnnotatedClass(Match.class);

        configuration.configure();
        return configuration.buildSessionFactory();
    }
}
