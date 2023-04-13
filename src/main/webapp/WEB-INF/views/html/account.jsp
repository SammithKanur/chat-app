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
    <title>My Account</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"> </script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link href="<c:url value="${pageContext.getAttribute('cssUrl')}/account.css?time=${pageContext.getAttribute('date')}"/>" rel="stylesheet" />
    <link href="<c:url value="${pageContext.getAttribute('cssUrl')}/utils.css?time=${pageContext.getAttribute('date')}"/>" rel="stylesheet" />
    <script src="<c:url value="${pageContext.getAttribute('jsUrl')}/utils.js?time=${pageContext.getAttribute('date')}"/>"></script>
    <script src="<c:url value="${pageContext.getAttribute('jsUrl')}/account.js?time=${pageContext.getAttribute('date')}"/>"></script>
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
    <div class="fa fa-list fa-2x" onmouseover="showList(this)" onmouseout="hideList(this)"></div>
    <div class="user">
        <i class="fa fa-user fa-3x" onmouseover="showDropDown(this)" onmouseout="hideDropDown(this)"></i>
    </div>
</div>
<div class="content">
    <div class="user-dropdown" onmouseout="hideDropDown(this)" onmouseover="showDropDown(this)">
        <ul>
            <li onclick="getHome(this)">Home</li>
            <li onclick="logout(this)">Logout</li>
        </ul>
    </div>
    <div class="account-list" onmouseover="showList(this)" onmouseout="hideList(this)">
        <ul>
            <li onclick="loadAccount(this)">Account</li>
            <li onclick="loadList(this)">Friends</li>
            <li onclick="loadList(this)">Pending Invitations</li>
            <li onclick="loadList(this)">Connection Requests</li>
            <li onclick="loadList(this)">Group Requests</li>
        </ul>
    </div>
    <div class="heading"><h2></h2></div>
    <div class="data">
        <div class="account">
            <div class="name">Display name: </div>
            <div class="followers">Followers:</div>
            <div class="groups">Groups:</div>
            <div class="password">Password: <input type="name" name="password" placeholder="new password">
                <button onclick="updatePassword(this)">Update password</button></div>
            <button class="remove" onclick="removeAccount(this)">Remove Account</button>
        </div>
        <div class="info">
            <input type="name" name="search" placeholder="search here" autocomplete="off" onkeyup="infoSearch(this)"/>
            <div class="list"></div>
        </div>
    </div>

</div>
<div class="footer"><p>Copyright &copy 2023 Sammith K A. All rights reserved</p> </div>
</body>
<script>
    const userName = "${userName}";
    const session = "${session}";
    const URL = "${URL}";
    const WSURL = "${WSURL}";
</script>
</html>