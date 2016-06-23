<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- Internationalization -->
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language :
pageContext.request.locale}" scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="text"/>

<!-- Base-URL -->
<c:set var="req" value="${pageContext.request}"/>
<c:set var="url">${req.requestURL}</c:set>
<c:set var="base" value="${fn:substring(url, 0, fn:length(url) - fn:length(req.requestURI))}${req.contextPath}/"/>

<!DOCTYPE html>
<html lang="${language}">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>University New Ulm Web Client</title>
    <!-- Bootstrap -->
    <link href="../bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <style type="text/css">
        body {
            padding-top: 50px;
        }
    </style>
</head>
<body>
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="../jquery/jquery-1.12.4.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="../bootstrap/js/bootstrap.min.js"></script>

<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="index">UUN</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <!-- Language selection. -->
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false">Language <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="?language=en">English</a></li>
                        <li><a href="?language=de">Deutsch</a></li>
                    </ul>
                </li>
                <c:if test="${activeModerator != null && activeModerator.isAdmin()}">
                    <!-- Logged in as moderator. -->
                    <c:if test="${activeModerator.isAdmin()}">
                        <!-- Logged in as admin. -->
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button"
                               aria-haspopup="true" aria-expanded="false"><fmt:message key="header.nav.label.admin"/>
                                <span class="caret"></span></a>
                            <ul class="dropdown-menu">
                                <li><a href="${base}webclient/applications">
                                    <fmt:message key="header.nav.label.admin.applications"/></a></li>
                                <li><a href="${base}webclient/accounts"><fmt:message
                                        key="header.nav.label.admin.accounts"/></a></li>
                                <li><a href="#"><fmt:message key="header.nav.label.admin.channels"/></a></li>
                                <li><a href="#"><fmt:message key="header.nav.label.admin.groups"/></a></li>
                            </ul>
                        </li>
                    </c:if>
                    <li><a href="${base}webclient/myChannels"><fmt:message key="header.nav.label.myChannels"/></a></li>
                    <li><a href="${base}webclient/index"><fmt:message key="header.nav.label.myAccount"/></a></li>
                </c:if>
            </ul>
            <c:if test="${activeModerator == null}">
                <!-- Not logged in. -->
                <div class="nav navbar-nav navbar-right">
                    <a class="btn btn-primary navbar-btn" href="${base}webclient/login">
                        <fmt:message key="header.nav.button.login"/></a>
                </div>
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="${base}webclient/register"><fmt:message key="login.noaccount.button.label"/></a></li>
                </ul>
            </c:if>
            <c:if test="${activeModerator != null}">
                <!-- Logged in. -->
                <div class="nav navbar-nav navbar-right">
                    <form method="post" action="${base}webclient/logout">
                        <button class="btn btn-primary navbar-btn" type="submit">
                            <fmt:message key="header.nav.button.logout"/></button>
                    </form>
                </div>
                <div class="nav navbar-nav navbar-right hidden-xs hidden-sm">
                    <p class="navbar-text">
                        <fmt:message key="index.text.loggedIn"/> ${activeModerator.getFirstName()}
                            ${activeModerator.getLastName()} &emsp;
                    </p>
                </div>
            </c:if>

        </div>
        <!--/.navbar-collapse -->
    </div>
</nav>

