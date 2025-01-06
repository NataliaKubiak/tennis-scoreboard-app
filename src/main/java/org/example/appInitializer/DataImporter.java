package org.example.appInitializer;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.example.dao.MatchDao;
import org.example.dao.PlayerDao;
import org.example.entity.Match;
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

    private PlayerDao playerDao = new PlayerDao();

    private static final List<String> playerNames = Arrays.asList(
            "R. Federer", "R. Nadal", "N. Djokovic", "S. Williams", "V. Williams",
            "M. Sharapova", "S. Halep", "P. Sampras", "A. Agassi", "S. Graf");
    private static final int MATCHES_AMOUNT = 10;

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
            throw e;
        }
    }

    private static void importMatches() {
        try (Session session =  HibernateUtil.getSessionFactory().openSession()) {

            session.beginTransaction();

            // Извлекаем игроков из базы данных
            List<Player> players = playerDao.getAll(session);
            if (players.size() < 2) {
                log.error("Not enough players to create matches.");
                return;
            }

            // Создаем запрос для вставки матчей
            StringBuilder queryBuilder = new StringBuilder("INSERT INTO matches (player1, player2, winner) VALUES ");
            for (int i = 0; i < MATCHES_AMOUNT; i++) {
                queryBuilder.append("(:player1_" + i + ", :player2_" + i + ", :winner_" + i + ")");
                if (i < 9) {
                    queryBuilder.append(", ");
                }
            }

            // Создаем нативный запрос
            var query = session.createNativeQuery(queryBuilder.toString());

            Random random = new Random();
            // Заполняем параметры для 10 матчей
            for (int i = 0; i < MATCHES_AMOUNT; i++) {
                int randomIndex1 = random.nextInt(players.size());
                int randomIndex2;
                do {
                    randomIndex2 = random.nextInt(players.size());
                } while (randomIndex2 == randomIndex1);

                Player player1 = players.get(randomIndex1);
                Player player2 = players.get(randomIndex2);

                query.setParameter("player1_" + i, player1.getId());
                query.setParameter("player2_" + i, player2.getId());
                query.setParameter("winner_" + i, random.nextBoolean() ? player1.getId() : player2.getId());
            }

            query.executeUpdate();

            session.getTransaction().commit();
            log.info("10 matches have been saved to the database.");

        } catch (Exception e) {
            log.error("Error during match data import", e);
            throw e;
        }
    }
}
