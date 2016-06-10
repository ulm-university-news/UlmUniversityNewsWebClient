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
    <c:if test="${applicationEditSuccess != null}">
        <div class="row">
            <div class="col-md-12">
                <div class="alert alert-success" role="alert">${applicationEditSuccess}</div>
            </div>
        </div>
        ${applicationEditSuccess = null}
    </c:if>
    <c:if test="${applicationEditInfo != null}">
        <div class="row">
            <div class="col-md-12">
                <div class="alert alert-info" role="alert">${applicationEditInfo}</div>
            </div>
        </div>
        ${applicationEditInfo = null}
    </c:if>
    <c:if test="${applicationEditError != null}">
        <div class="row">
            <div class="col-md-12">
                <div class="alert alert-danger" role="alert">${applicationEditError}</div>
            </div>
        </div>
        ${applicationEditError = null}
    </c:if>
    <c:if test="${applicationLoadError != null}">
        <div class="row">
            <div class="col-md-12">
                <div class="alert alert-danger" role="alert">${applicationLoadError}</div>
            </div>
        </div>
        ${applicationLoadError = null}
    </c:if>
    <div class="row">
        <div class="col-md-3">
            <div class="list-group">
                <div class="list-group-item active">
                    <h4 class="list-group-item-heading"><fmt:message key="application.list.heading"/></h4>
                </div>
                <c:choose>
                    <c:when test="${moderators != null && !moderators.isEmpty()}">
                        <c:forEach items="${moderators}" var="moderator">
                            <c:if test="${moderatorId == null && moderator != null}">
                                <c:set var="moderatorId" value="${moderator.getId()}"/>
                            </c:if>
                            <c:choose>
                                <c:when test="${moderatorId != null && moderatorId == moderator.getId()}">
                                    <a href="?moderatorId=${moderator.getId()}"
                                       class="list-group-item list-group-item-info">
                                            ${moderator.getLastName()}, ${moderator.getFirstName()}
                                    </a>
                                    <c:set var="currentModerator" value="${moderator}" scope="session"/>
                                </c:when>
                                <c:otherwise>
                                    <a href="?moderatorId=${moderator.getId()}" class="list-group-item">
                                            ${moderator.getLastName()}, ${moderator.getFirstName()}
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="list-group-item">
                            <fmt:message key="application.none"/>
                        </div>
                        ${currentModerator = null}
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="col-md-9">
            <div class="panel panel-info">
                <div class="panel-heading">
                    <c:choose>
                        <c:when test="${currentModerator != null}">
                            <h4>${currentModerator.getFirstName()} ${currentModerator.getLastName()}</h4>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="application.nodata"/>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="panel-body">
                    <c:choose>
                        <c:when test="${currentModerator != null}">
                            <div class="row">
                                <div class="col-md-5">
                                    <h4><fmt:message key="moderator.firstname"/></h4>
                                        ${currentModerator.getFirstName()}
                                </div>
                                <div class="col-md-4">
                                    <h4><fmt:message key="moderator.lastname"/></h4>
                                        ${currentModerator.getLastName()}
                                </div>
                            </div>
                            <br>

                            <div class="row">
                                <div class="col-md-5">
                                    <h4><fmt:message key="moderator.name"/></h4>
                                        ${currentModerator.getName()}
                                </div>
                                <div class="col-md-4">
                                    <h4><fmt:message key="moderator.email"/></h4>
                                        ${currentModerator.getEmail()}
                                </div>
                            </div>
                            <br>
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
                        <form class="form-inline" method="post" action="${base}webclient/applications">
                            <button type="submit" name="button" value="accept" class="btn btn-primary">
                                <fmt:message key="application.accept"/>
                            </button>
                            <button type="submit" name="button" value="decline" class="btn btn-primary pull-right">
                                <fmt:message key="application.decline"/>
                            </button>
                        </form>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>

<!-- Page footer. -->
<%@ include file="footer.jsp" %>