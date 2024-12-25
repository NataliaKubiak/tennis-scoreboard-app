package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.example.dao.MatchDao;
import org.example.dto.NewPlayerDto;
import org.example.entity.Player;
import org.example.service.PlayerService;
import org.example.util.Validator;

import java.io.IOException;
import java.util.Locale;

@Log4j2
@WebServlet("/new-match")
public class NewMatchServlet extends HttpServlet {

    private final PlayerService playerService = new PlayerService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.info("Processing GET request for New Match page.");

        request.getRequestDispatcher("WEB-INF/new-match.jsp").forward(request, response);
        log.info("Forwarded to 'new-match.jsp'");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        log.info("Processing POST request for new match.");

        //взять имена игроков из тела
        String playerOneName = request.getParameter("playerOne").toUpperCase(Locale.ROOT);
        String playerTwoName = request.getParameter("playerTwo").toUpperCase(Locale.ROOT);
        log.info("Received player names: Player 1 = {}, Player 2 = {}", playerOneName, playerTwoName);

        //если имена одинаковые - показываем ошибку красным
        if (Validator.isSameName(playerOneName, playerTwoName)) {
            log.warn("Players have the same name: {}", playerOneName);

            request.setAttribute("errorMessage", "Players have the same name: " + playerOneName);

            request.getRequestDispatcher("/WEB-INF/new-match.jsp").forward(request, response);
            log.info("Rendered 'new-match' template with error message.");
            return;
        }

        NewPlayerDto playerOneDto = NewPlayerDto.builder()
                .name(playerOneName)
                .build();
        NewPlayerDto playerTwoDto = NewPlayerDto.builder()
                .name(playerTwoName)
                .build();

        //передать в PlayerService Player1DTO и Player2DTO - получить игрока или создать его в БД
        Player playerOne = playerService.getOrSavePlayer(playerOneDto);
        Player playerTwo = playerService.getOrSavePlayer(playerTwoDto);
        log.info("Players saved or retrieved: Player 1 = {}, Player 2 = {}", playerOne, playerTwo);

        // TODO: 16/12/2024 тут написать логику создания матча

        // TODO: 16/12/2024 исправить редирект на endpoint /match-score?uuid=$match_id
        response.sendRedirect("/match-score");
        log.info("Redirecting to '/match-score'.");
    }
}
