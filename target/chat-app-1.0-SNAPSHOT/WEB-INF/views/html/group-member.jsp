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
    <title>Group Members</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"> </script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link href="<c:url value="${pageContext.getAttribute('cssUrl')}/group-members.css?time=${pageContext.getAttribute('date')}"/>" rel="stylesheet" />
    <link href="<c:url value="${pageContext.getAttribute('cssUrl')}/utils.css?time=${pageContext.getAttribute('date')}"/>" rel="stylesheet" />
    <script src="<c:url value="${pageContext.getAttribute('jsUrl')}/utils.js?time=${pageContext.getAttribute('date')}"/>"></script>
    <script src="<c:url value="${pageContext.getAttribute('jsUrl')}/group-member.js?time=${pageContext.getAttribute('date')}"/>"></script>
</head>
<body>
<div class="header">
    <div class="fa fa-list fa-2x" onmouseover="showList(this)" onmouseout="hideList(this)"></div>
    <div class="user">
        <i class="fa fa-user fa-3x" onmouseover="showDropDown(this)" onmouseout="hideDropDown(this)"></i>
    </div>
</div>
<div class="content">
    <div class="user-dropdown" onmouseout="hideDropDown(this)" onmouseover="showDropDown(this)">
        <ul>
            <li onclick="logout(this)">Logout</li>
        </ul>
    </div>
    <div class="account-list" onmouseover="showList(this)" onmouseout="hideList(this)">
        <ul>
            <li onclick="loadList(this)">Members</li>
            <li onclick="loadList(this)">Pending Invitations</li>
            <li onclick="deleteMember(this)">Exit Group</li>
        </ul>
    </div>
    <div class="heading">${groupName}</div>
    <div class="navbar">
        <input type="name" name="search" placeholder="Enter friend's name here" autocomplete="off">
        <div class="fa fa-plus fa-3x" onclick="addMember(this)"></div>
    </div>
    <div class="info">
        <div class="title">Members</div>
        <input type="name" name="search" placeholder="search here" autocomplete="off"/>
        <div class="list">
            <div class="item">
                <div>yolo</div>
                <button>hello</button>
            </div>
        </div>
    </div>
</div>
<div class="footer">Copyright &copy 2021 Sammith K A. All rights reserved </div>
</body>
<script>
    const userName = "${userName}";
    const session = "${session}";
    const groupName = "${groupName}";
    const URL = "${URL}";
    const WSURL = "${WSURL}";
</script>
</html>