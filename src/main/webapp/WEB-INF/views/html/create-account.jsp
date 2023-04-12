<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Long currentTime = System.currentTimeMillis();
    pageContext.setAttribute("date", currentTime);
    Object URL = request.getAttribute("URL");
    String cssUrl = URL + "/css-resources";
    String jsUrl = URL + "/js-resources";
    pageContext.setAttribute("cssUrl", cssUrl);
    pageContext.setAttribute("jsUrl", jsUrl);
%>
<!DOCTYPE html>
<html>
<head>
    <title>Create account </title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"> </script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link href="<c:url value="${pageContext.getAttribute('cssUrl')}/create-account.css?time=${pageContext.getAttribute('date')}"/>" rel="stylesheet" />
    <link href="<c:url value="${pageContext.getAttribute('cssUrl')}/utils.css?time=${pageContext.getAttribute('date')}"/>" rel="stylesheet" />
    <script src="<c:url value="${pageContext.getAttribute('jsUrl')}/create-account.js?time=${pageContext.getAttribute('date')}"/>"></script>
    <script src="<c:url value="${pageContext.getAttribute('jsUrl')}/utils.js?time=${pageContext.getAttribute('date')}"/>"></script>
</head>
<body>
<div class="buffer">
    <div class="loader"></div>
</div>
<div class="response-message">
    <div>
        <i class="fa fa-window-close" onclick="hideResponseMessage(this)"></i>
        <h2></h2>
    </div>
</div>
<div class="content">
    <form class="create-account">
        <h1>Create Account</h1>
        <input type="name" name="username" placeholder="username"/>
        <input type="password" name="password" placeholder="password"/>
        <div id="buttons">
            <button id="create" onclick="handleCreate(this)"> Create Account </button>
        </div>
    </form>
</div>
<div class="footer"><p>Copyright &copy 2021 Sammith K A. All rights reserved</p></div>
</body>
<script>
    const URL = "${URL}";
    const WSURL = "${WSURL}";
</script>
</html>