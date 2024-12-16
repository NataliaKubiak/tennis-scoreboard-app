package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

@WebServlet("/new-match")
public class NewMatchServlet extends HttpServlet {

    private final PlayerService playerService = new PlayerService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/templates/new-match.html").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //взять имена игроков из тела
        String playerOneName = request.getParameter("playerOne");
        String playerTwoName = request.getParameter("playerTwo");

        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(
                ThymeleafConfigListener.TEMPLATE_ENGINE_ATTR);
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(request, response);
        WebContext context = new WebContext(webExchange);

        //если имена одинаковые - показываем ошибку красным
        if (Validator.isSameName(playerOneName, playerTwoName)) {

            context.setVariable("errorMessage", "Players have the same name: " + playerOneName);

            response.setContentType("text/html");
            templateEngine.process("new-match", context, response.getWriter());
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

        // Если имена разные — редирект на другую страницу
        response.sendRedirect("/match-score");
    }
}
