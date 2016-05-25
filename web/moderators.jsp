<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>University New Ulm Web Client</title>
</head>
<body>
<h3>Moderators</h3>
<c:forEach items="${moderators}" var="moderator">
    Name:  ${moderator.getName()}<br>
    First name:  ${moderator.getFirstName()}<br>
    Last name:  ${moderator.getLastName()}<br><br>
</c:forEach>
</body>
</html>
