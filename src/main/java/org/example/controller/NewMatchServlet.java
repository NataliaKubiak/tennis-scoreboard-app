package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.example.dto.NewPlayerDto;
import org.example.entity.Player;
import org.example.service.PlayerService;
import org.example.util.Validator;
import org.example.view.ThymeleafConfigListener;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;

@Log4j2
@WebServlet("/new-match")
public class NewMatchServlet extends HttpServlet {

    private final PlayerService playerService = new PlayerService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.info("Processing GET request for New Match page.");

        request.getRequestDispatcher("WEB-INF/templates/new-match.html").forward(request, response);
        log.info("Forwarded to 'new-match.html' template.");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        log.info("Processing POST request for new match.");

        //взять имена игроков из тела
        String playerOneName = request.getParameter("playerOne");
        String playerTwoName = request.getParameter("playerTwo");
        log.info("Received player names: Player 1 = {}, Player 2 = {}", playerOneName, playerTwoName);

        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(
                ThymeleafConfigListener.TEMPLATE_ENGINE_ATTR);
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(request, response);
        WebContext context = new WebContext(webExchange);

        //если имена одинаковые - показываем ошибку красным
        if (Validator.isSameName(playerOneName, playerTwoName)) {
            log.warn("Players have the same name: {}", playerOneName);

            context.setVariable("errorMessage", "Players have the same name: " + playerOneName);

            response.setContentType("text/html");
            templateEngine.process("new-match", context, response.getWriter());

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
