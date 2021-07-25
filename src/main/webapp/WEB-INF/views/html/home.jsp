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
    <title>Home</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"> </script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link href="<c:url value="${pageContext.getAttribute('cssUrl')}/home.css?time=${pageContext.getAttribute('date')}"/>" rel="stylesheet" />
    <link href="<c:url value="${pageContext.getAttribute('cssUrl')}/utils.css?time=${pageContext.getAttribute('date')}"/>" rel="stylesheet" />
    <script src="<c:url value="${pageContext.getAttribute('jsUrl')}/utils.js?time=${pageContext.getAttribute('date')}"/>"></script>
    <script src="<c:url value="${pageContext.getAttribute('jsUrl')}/home.js?time=${pageContext.getAttribute('date')}"/>"></script>
</head>
<body>
<div class="popup">
    <div class="create-group">
        <i class="fa fa-window-close" onclick="closeGroupForm(this)"></i>
        <input type="name" placeholder="Enter group name"/>
        <button onclick="createGroup(this)">Create Group</button>
    </div>
</div>
<div class="buffer">
    <div class="loader"></div>
</div>
<div class="response-message">
    <div>
        <i class="fa fa-window-close" onclick="hideResponseMessage(this)"></i>
        <h2></h2>
    </div>
</div>
<div class="predictive-list">
    <div class="item" onclick="getProfile(this)"></div>
</div>
<div class="header">
    <div class="app-name">CHAT APP</div>
    <input type="name" name="search" id="search" placeholder="Search for users here" autocomplete="off" onkeyup="getPredictions(this)">
    <div class="user">
        <i class="fa fa-user fa-3x" onmouseover="showDropDown(this)" onmouseout="hideDropDown(this)"></i>
    </div>
</div>
<div class="sidebar">
    <div class="draggable" onmousedown="sidebarMouseDown(this)"></div>
    <div class="friends-list">
        <div class="heading"><h3>Friends</h3></div>
        <div class="list"></div>
    </div>
    <div class="groups-list">
        <div class="heading"><h3>Groups</h3></div>
        <div class="list"></div>
    </div>
</div>
<div class="content">
    <div class="user-dropdown" onmouseout="hideDropDown(this)" onmouseover="showDropDown(this)">
        <ul>
            <li onclick="getAccount(this)">Account</li>
            <li onclick="openCreateGroup(this)">Create Group</li>
            <li onclick="logout(this)">Logout</li>
        </ul>
    </div>
    <div class="message-area">
    </div>
    <div class="input-area">
        <div class="draggable" onmousedown="chatareaMouseDown(this)"></div>
        <textarea class="chat-area" rows="10" cols="10"></textarea>
    </div>
    <div class="button-area">
        <button id="send-message" onclick="writeMessage(this)">Send</button>
    </div>
</div>
<div class="footer">Copyright &copy 2021 Sammith K A. All rights reserved </div>
</body>
<script>
    let session = "${session}";
    let userName = "${userName}";
    const URL = "${URL}";
    const WSURL = "${WSURL}";
</script>
</html>