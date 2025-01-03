package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.example.dto.PlayerDto;
import org.example.service.OngoingMatchesService;
import org.example.service.PlayerService;
import org.example.util.Validator;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@WebServlet("/new-match")
public class NewMatchServlet extends HttpServlet {

    private final PlayerService playerService = new PlayerService();
    private final OngoingMatchesService ongoingMatchesService = OngoingMatchesService.getInstance();

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
            log.info("Rendered 'new-match.jsp' with error message.");
            return;
        }

        PlayerDto newPlayerOneDto = PlayerDto.builder()
                .name(playerOneName)
                .build();
        PlayerDto newPlayerTwoDto = PlayerDto.builder()
                .name(playerTwoName)
                .build();

        //передать в PlayerService Player1DTO и Player2DTO - получить игрока или создать его в БД
        PlayerDto playerOne = playerService.getOrSavePlayer(newPlayerOneDto);
        PlayerDto playerTwo = playerService.getOrSavePlayer(newPlayerTwoDto);
        log.info("Players saved or retrieved: Player 1 = {}, Player 2 = {}", playerOne, playerTwo);

        //достаем UUID уже сущ-его матча (по именам игроков) либо создаем новый матч
        UUID matchId;
        Optional<UUID> maybeMatchId = ongoingMatchesService.getMatchIdByPlayers(playerOne, playerTwo);

        if (maybeMatchId.isEmpty()) {
            matchId = ongoingMatchesService.addNewMatchToMap(playerOne, playerTwo);
            log.info("Match with Players: {}, {} doesn't exist. Created a New Match with Id: {}",
                    playerOne, playerTwo, matchId);
        } else {
            matchId = maybeMatchId.get();
            log.info("Match with Players: {}, {} already exist. Got existed Match with Id: {}",
                    playerOne, playerTwo, matchId);
        }

        response.sendRedirect("/match-score?uuid=" + matchId);
        log.info("Redirecting to '/match-score'.");
    }
}
