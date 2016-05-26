<!-- Page header. -->
<%@ include file="header.jsp" %>

<!-- Page content. -->
<h3>Moderators</h3>
<c:forEach items="${moderators}" var="moderator">
    <fmt:message key="moderators.label.name"/>:  ${moderator.getName()}<br>
    <fmt:message key="moderators.label.firstName"/>:  ${moderator.getFirstName()}<br>
    <fmt:message key="moderators.label.lastName"/>:  ${moderator.getLastName()}<br><br>
</c:forEach>

<!-- Page footer. -->
<%@ include file="footer.jsp" %>