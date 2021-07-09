<%--suppress XmlPathReference --%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.time.LocalDate"%>
<%
    Long currentTime = System.currentTimeMillis();
    pageContext.setAttribute("date", currentTime);
    request.setAttribute("URL", "http://localhost:8080/chat-app");
    request.setAttribute("WSURL", "ws://localhost:8080/chat-app/websocket-server");
    Object URL = request.getAttribute("URL");
    String cssUrl = URL + "/css-resources";
    String jsUrl = URL + "/js-resources";
    String createAccount = URL + "/user/create-account-page";
    pageContext.setAttribute("cssUrl", cssUrl);
    pageContext.setAttribute("jsUrl", jsUrl);
    pageContext.setAttribute("createAccount", createAccount);
%>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"> </script>
    <link href="<c:url value="${pageContext.getAttribute('cssUrl')}/login.css?time=${pageContext.getAttribute('date')}"/>" rel="stylesheet" />
    <link href="<c:url value="${pageContext.getAttribute('cssUrl')}/utils.css?time=${pageContext.getAttribute('date')}"/>" rel="stylesheet" />
    <script src="<c:url value="${pageContext.getAttribute('jsUrl')}/utils.js?time=${pageContext.getAttribute('date')}"/>"></script>
    <script src="<c:url value="${pageContext.getAttribute('jsUrl')}/login.js?time=${pageContext.getAttribute('date')}"/>"></script>
</head>
<body>
<div class="content">
    <div class="heading">
        <h2>Welcome To Chat App</h2>
    </div>
    <div class="message"><h2>Hello world</h2></div>
    <div class="form">
        <form class="login-form">
            <h1>Login</h1>
            <input type="name" name="username" placeholder="username"/>
            <input type="password" name="password" placeholder="password"/>
            <div id="buttons">
                <button id="login" onclick="handleLogin(this)"> Login </button>
                <a id="create" href="${pageContext.getAttribute('createAccount')}"> Create Account </a>
            </div>
        </form>
    </div>
</div>
<div class="footer">Copyright &copy 2021 Sammith K A. All rights reserved </div>
</body>
<script>
    const URL = "${URL}";
    const WSURL = "${WSURL}";
</script>
</html>