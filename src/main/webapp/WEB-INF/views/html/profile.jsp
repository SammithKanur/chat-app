<%--suppress XmlPathReference --%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.time.LocalDate"%>
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
    <title>${friend}'s profile</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"> </script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link href="<c:url value="${pageContext.getAttribute('cssUrl')}/profile.css?time=${pageContext.getAttribute('date')}"/>" rel="stylesheet" />
    <link href="<c:url value="${pageContext.getAttribute('cssUrl')}/utils.css?time=${pageContext.getAttribute('date')}"/>" rel="stylesheet" />
    <script src="<c:url value="${pageContext.getAttribute('jsUrl')}/utils.js?time=${pageContext.getAttribute('date')}"/>"></script>
    <script src="<c:url value="${pageContext.getAttribute('jsUrl')}/profile.js?time=${pageContext.getAttribute('date')}"/>"></script>
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
<div class="header">
    <div class="user">
        <i class="fa fa-user" onmouseover="showDropDown(this)" onmouseout="hideDropDown(this)"></i>
    </div>
</div>
<div class="content">
    <div class="user-dropdown" onmouseout="hideDropDown(this)" onmouseover="showDropDown(this)">
        <ul>
            <li onclick="getHome(this)">Home</li>
            <li onclick="logout(this)">Logout</li>
        </ul>
    </div>
    <div class="heading">${friend}'s Profile</div>
    <div class="profile">
        <div class="name">Display name: </div>
        <div class="followers">Followers:</div>
        <div class="groups">Groups:</div>
        <div class="button-area">
            <button class="btn1" onclick="handleConnection(this)">Modify Me!</button>
            <button class="btn2" onclick="handleConnection(this)">Modify Me!</button>
        </div>
    </div>
</div>
<div class="footer"><p>Copyright &copy 2021 Sammith K A. All rights reserved</p> </div>
</body>
<script>
    const userName = "${userName}";
    const session = "${session}";
    const friend = "${friend}";
    const URL = "${URL}";
    const WSURL = "${WSURL}";
</script>
</html>