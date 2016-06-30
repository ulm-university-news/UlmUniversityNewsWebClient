<!-- Page header. -->
<%@ include file="header.jsp" %>
<c:set var="moderatorId" value="${param.moderatorId}" scope="request"/>

<!-- Page content. -->
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <h3><fmt:message key="accounts.title"/></h3>

            <p><fmt:message key="accounts.info"/></p>
            <br>
        </div>
    </div>
    <c:if test="${editSuccess != null}">
        <div class="row">
            <div class="col-md-12">
                <div class="alert alert-success" role="alert">${editSuccess}</div>
            </div>
        </div>
        ${editSuccess = null}
    </c:if>
    <c:if test="${editInfo != null}">
        <div class="row">
            <div class="col-md-12">
                <div class="alert alert-info" role="alert">${editInfo}</div>
            </div>
        </div>
        ${editInfo = null}
    </c:if>
    <c:if test="${editError != null}">
        <div class="row">
            <div class="col-md-12">
                <div class="alert alert-danger" role="alert">${editError}</div>
            </div>
        </div>
        ${editError = null}
    </c:if>
    <c:if test="${loadError != null}">
        <div class="row">
            <div class="col-md-12">
                <div class="alert alert-danger" role="alert">${loadError}</div>
            </div>
        </div>
        ${loadError = null}
    </c:if>
    <div class="row">
        <div class="col-md-3">
            <div class="list-group">
                <div class="list-group-item active">
                    <h4 class="list-group-item-heading"><fmt:message key="accounts.list.heading"/></h4>
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
                            <fmt:message key="general.none"/>
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
                            <fmt:message key="accounts.nodata"/>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="panel-body">
                    <c:choose>
                        <c:when test="${currentModerator != null}">
                            <div class="row">
                                <div class="col-md-6">
                                    <h4><fmt:message key="moderator.firstname"/></h4>
                                        ${currentModerator.getFirstName()}
                                </div>
                                <div class="col-md-6">
                                    <h4><fmt:message key="moderator.lastname"/></h4>
                                        ${currentModerator.getLastName()}
                                </div>
                            </div>
                            <br>

                            <div class="row">
                                <div class="col-md-6">
                                    <h4><fmt:message key="moderator.name"/></h4>
                                        ${currentModerator.getName()}
                                </div>
                                <div class="col-md-6">
                                    <h4><fmt:message key="moderator.email"/></h4>
                                        ${currentModerator.getEmail()}
                                </div>
                            </div>
                            <br>
                            <h4><fmt:message key="moderator.isAdmin"/></h4>
                            <c:choose>
                                <c:when test="${currentModerator.isAdmin()}">
                                    <fmt:message key="general.yes"/>
                                </c:when>
                                <c:otherwise>
                                    <fmt:message key="general.no"/>
                                </c:otherwise>
                            </c:choose>
                            <br><br>
                            <h4><fmt:message key="moderator.motivation"/></h4>
                            ${currentModerator.getMotivation()}
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="accounts.nodata.info"/>
                        </c:otherwise>
                    </c:choose>
                </div>
                <c:if test="${currentModerator != null}">
                    <div class="panel-footer">
                        <form name="form" class="form-inline" method="post" action="${base}webclient/accounts">
                            <c:choose>
                                <c:when test="${currentModerator.isAdmin()}">
                                    <button type="submit" name="button" value="removeRights"
                                            class="btn btn-primary">
                                        <fmt:message key="accounts.adminRights.remove"/>
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <button type="submit" name="button" value="addRights"
                                            class="btn btn-primary">
                                        <fmt:message key="accounts.adminRights.add"/>
                                    </button>
                                </c:otherwise>
                            </c:choose>
                            <button type="submit" name="button" value="lock" class="btn btn-primary">
                                <fmt:message key="accounts.lock"/>
                            </button>
                            <!-- Trigger confirmation dialog. -->
                            <button type="button" class="btn btn-primary pull-right" data-toggle="modal"
                                    data-target="#confirm">
                                <fmt:message key="general.delete"/>
                            </button>
                        </form>
                    </div>
                    <!-- Modal confirm dialog. -->
                    <div id="confirm" class="modal fade" role="dialog">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title"><fmt:message key="general.confirmation.title"/></h4>
                                </div>
                                <div class="modal-body">
                                    <p><fmt:message key="accounts.warning.delete"/></p>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default pull-right" data-dismiss="modal">
                                        <fmt:message key="general.no"/>
                                    </button>
                                    <form name="form" class="form-inline" method="post"
                                          action="${base}webclient/accounts">
                                        <button type="submit" name="button" value="delete"
                                                class="btn btn-primary pull-left">
                                            <fmt:message key="general.yes"/>
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>

<!-- Page footer. -->
<%@ include file="footer.jsp" %>