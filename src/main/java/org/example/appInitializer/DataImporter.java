package org.example.appInitializer;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.example.entity.Player;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Log4j2
@UtilityClass
public class DataImporter {

    private static final List<String> playerNames = Arrays.asList(
            "R. Federer", "R. Nadal", "N. Djokovic", "S. Williams", "V. Williams",
            "M. Sharapova", "S. Halep", "P. Sampras", "A. Agassi", "S. Graf");

    public static void importData() {
        importPlayers();
        importMatches();
    }

    private static void importPlayers() {
        try (Session session =  HibernateUtil.getSessionFactory().openSession()) {

            session.beginTransaction();

            StringBuilder queryBuilder = new StringBuilder("INSERT INTO Player (name) VALUES ");
            for (int i = 0; i < playerNames.size(); i++) {
                queryBuilder.append("(:name" + i + ")");
                if (i < playerNames.size() - 1) {
                    queryBuilder.append(", ");
                }
            }

            var query = session.createNativeQuery(queryBuilder.toString());

            for (int i = 0; i < playerNames.size(); i++) {
                query.setParameter("name" + i, playerNames.get(i));
            }

            query.executeUpdate();
            session.getTransaction().commit();
            log.info("10 players have been saved to the database.");

        } catch (Exception e) {
            log.error("Error during data import", e);
        }
    }

    private static void importMatches() {
        try (Session session =  HibernateUtil.getSessionFactory().openSession()) {

            session.beginTransaction();

            // Извлекаем игроков из базы данных
            List<Player> players = session.createQuery("FROM Player", Player.class).getResultList();
            if (players.size() < 2) {
                log.error("Not enough players to create matches.");
                return;
            }

            // Создаем запрос для вставки матчей
            StringBuilder queryBuilder = new StringBuilder("INSERT INTO matches (player1, player2, winner) VALUES ");
            for (int i = 0; i < 10; i++) {
                queryBuilder.append("(:player1" + i + ", :player2" + i + ", :winner" + i + ")");
                if (i < 9) {
                    queryBuilder.append(", ");
                }
            }

            // Создаем нативный запрос
            var query = session.createNativeQuery(queryBuilder.toString());

            Random random = new Random();
            int randomIndex1 = random.nextInt(10);
            int randomIndex2;
            do {
                randomIndex2 = random.nextInt(10);
            } while (randomIndex2 == randomIndex1);

            // Заполняем параметры для 10 матчей
            for (int i = 0; i < 10; i++) {
                Player player1 = players.get(randomIndex1);
                Player player2 = players.get(randomIndex2);

                query.setParameter("player1" + i, player1.getId());
                query.setParameter("player2" + i, player2.getId());
                query.setParameter("winner" + i, player1.getId());
            }

            query.executeUpdate();

            session.getTransaction().commit();
            log.info("10 matches have been saved to the database.");

        } catch (Exception e) {
            log.error("Error during match data import", e);
        }
    }
}
