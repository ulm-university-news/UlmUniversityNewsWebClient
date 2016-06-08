<!-- Page header. -->
<%@ include file="header.jsp" %>
<c:set var="moderatorId" value="${param.moderatorId}" scope="session"/>

<!-- Page content. -->
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <h3><fmt:message key="application.title"/></h3>

            <p><fmt:message key="application.info"/></p>
            <br>
        </div>
    </div>
    <div class="row">
        <div class="col-md-3">
            <div class="list-group">
                <div class="list-group-item active">
                    <h4 class="list-group-item-heading"><fmt:message key="application.list.heading"/></h4>
                </div>
                <c:forEach items="${moderators}" var="moderator">
                    <c:if test="${moderatorId == null && moderator != null}">
                        <c:set var="moderatorId" value="${moderator.getId()}"/>
                    </c:if>
                    <c:choose>
                        <c:when test="${moderatorId != null && moderatorId == moderator.getId()}">
                            <a href="?moderatorId=${moderator.getId()}" class="list-group-item list-group-item-info">
                                    ${moderator.getLastName()}, ${moderator.getFirstName()}
                            </a>
                            <c:set var="currentModerator" value="${moderator}" scope="request"/>
                        </c:when>
                        <c:otherwise>
                            <a href="?moderatorId=${moderator.getId()}" class="list-group-item">
                                    ${moderator.getLastName()}, ${moderator.getFirstName()}
                            </a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </div>
        </div>
        <div class="col-md-9">
            <div class="panel panel-info">
                <div class="panel-heading">
                    <c:choose>
                        <c:when test="${currentModerator != null}">
                            <h4>${currentModerator.getLastName()}, ${currentModerator.getFirstName()}</h4>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="application.nodata"/>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="panel-body">
                    <c:choose>
                        <c:when test="${currentModerator != null}">
                            <h4><fmt:message key="moderator.name"/></h4>
                            ${currentModerator.getName()}
                            <h4><fmt:message key="moderator.email"/></h4>
                            ${currentModerator.getEmail()}
                            <h4><fmt:message key="moderator.motivation"/></h4>
                            ${currentModerator.getMotivation()}
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="application.nodata.info"/>
                        </c:otherwise>
                    </c:choose>
                </div>
                <c:if test="${currentModerator != null}">
                    <div class="panel-footer">
                        <button type="button" class="btn btn-primary"><fmt:message key="application.accept"/></button>
                        <button type="button" class="btn btn-primary"><fmt:message key="application.decline"/></button>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>

<!-- Page footer. -->
<%@ include file="footer.jsp" %>