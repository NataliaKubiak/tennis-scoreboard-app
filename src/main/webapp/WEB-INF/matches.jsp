<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tennis Scoreboard | Finished Matches</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/css/style.css">

    <script src="/js/app.js"></script>
</head>

<body>
<header class="header">
    <section class="nav-header">
        <div class="brand">
            <div class="nav-toggle">
                <img src="images/menu.png" alt="Logo" class="logo">
            </div>
            <span class="logo-text">TennisScoreboard</span>
        </div>
        <div>
            <nav class="nav-links">
                <a class="nav-link" href="/">Home</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/matches">Matches</a>
            </nav>
        </div>
    </section>
</header>
<main>
    <div class="container">
        <h1>Matches</h1>

        <div class="input-container">
            <form method="get" action="${pageContext.request.contextPath}/matches" class="form-matches">
                <input class="input-filter"
                       placeholder="Filter by name"
                       type="text"
                       name="filter_by_player_name"
                       value="${filter_by_player_name}"/>
            </form>
            <a href="${pageContext.request.contextPath}/matches">
                <button class="btn-filter">Reset Filter</button>
            </a>
        </div>

        <table class="table-matches">
            <tr>
                <th>Player One</th>
                <th>Player Two</th>
                <th>Winner</th>
            </tr>
            <c:forEach var="match" items="${matches}">
                <tr>
                    <td>${match.player1.name}</td>
                    <td>${match.player2.name}</td>
                    <td><span class="winner-name-td">${match.winner.name}</span></td>
                </tr>
            </c:forEach>
        </table>

        <div class="pagination">
            <!-- Кнопка "Назад" -->
            <c:if test="${currentPage > 1}">
                <a class="prev"
                   href="${pageContext.request.contextPath}/matches?page=${currentPage - 1}
                   <c:if test="${not empty filter_by_player_name}">
           &filter_by_player_name=${filter_by_player_name}
       </c:if>">
                    < </a>
            </c:if>

            <!-- Номера страниц -->
            <c:forEach begin="1" end="${totalPages}" var="i">
                <a class="num-page ${i == currentPage ? 'current' : ''}"
                   href="${pageContext.request.contextPath}/matches?page=${i}<c:if test='${not empty filter_by_player_name}'>&filter_by_player_name=${filter_by_player_name}</c:if>">
                        ${i}
                </a>
            </c:forEach>

            <!-- Кнопка "Вперёд" -->
            <c:if test="${currentPage < totalPages}">
                <a class="next"
                   href="${pageContext.request.contextPath}/matches?page=${currentPage + 1}
       <c:if test='${not empty filter_by_player_name}'>&filter_by_player_name=${filter_by_player_name}</c:if>">
                    >
                </a>
            </c:if>
        </div>
    </div>
</main>
<footer>
    <div class="footer">
        <p>&copy; Tennis Scoreboard, project from <a href="https://zhukovsd.github.io/java-backend-learning-course/">zhukovsd/java-backend-learning-course</a>
            roadmap.</p>
    </div>
</footer>
</body>
</html>
