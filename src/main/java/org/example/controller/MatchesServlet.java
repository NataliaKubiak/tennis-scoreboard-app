package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.example.dto.MatchDto;
import org.example.entity.Match;
import org.example.service.SearchMatchService;

import java.io.IOException;
import java.util.List;

@Log4j2
@WebServlet("/matches")
public class MatchesServlet extends HttpServlet {

    private static final int PAGE_SIZE = 5;
    private SearchMatchService searchMatchService = new SearchMatchService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.info("Processing GET request for Matches page.");

        String filterByPlayerName = request.getParameter("filter_by_player_name");
        String pageParam = request.getParameter("page");

        int pageNo;
        try {
            pageNo = (pageParam != null) ? Integer.parseInt(pageParam.trim()) : 1;
        } catch (NumberFormatException e) {
            log.warn("Invalid page number format: '{}'. Defaulting to page 1.", pageParam);
            pageNo = 1;
        }

        List<MatchDto> matches = searchMatchService.searchMatchesByPlayerName(filterByPlayerName, pageNo, PAGE_SIZE);
        int totalRecords = searchMatchService.getTotalRecordsAmount(filterByPlayerName);
        int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);

        log.info("Found {} total records, divided into {} pages.", totalRecords, totalPages);

        request.setAttribute("matches", matches);
        request.setAttribute("filter_by_player_name", filterByPlayerName);
        request.setAttribute("currentPage", pageNo);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("WEB-INF/matches.jsp").forward(request, response);
        log.info("Forwarded to 'matches.jsp'");
    }
}
