<%@ include file="header.jsp" %>
<c:set var="moderatorId" value="${param.moderatorId}" scope="request"/>

<!-- Page content. -->
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <h3><fmt:message key="myAccount.title"/></h3>

            <p><fmt:message key="myAccount.info"/></p>
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
        <div class="col-md-12">
            <div class="panel panel-info">
                <div class="panel-heading">
                    <c:choose>
                        <c:when test="${activeModerator != null}">
                            <h4>${activeModerator.getFirstName()} ${activeModerator.getLastName()}</h4>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="myAccount.nodata"/>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="panel-body">
                    <c:choose>
                        <c:when test="${activeModerator != null}">
                            <div class="row">
                                <div class="col-md-6">
                                    <h4><fmt:message key="moderator.firstname"/></h4>
                                        ${activeModerator.getFirstName()}
                                </div>
                                <div class="col-md-6">
                                    <h4><fmt:message key="moderator.lastname"/></h4>
                                        ${activeModerator.getLastName()}
                                </div>
                            </div>
                            <br>

                            <div class="row">
                                <div class="col-md-6">
                                    <h4><fmt:message key="moderator.name"/></h4>
                                        ${activeModerator.getName()}
                                </div>
                                <div class="col-md-6">
                                    <h4><fmt:message key="moderator.email"/></h4>
                                        ${activeModerator.getEmail()}
                                </div>
                            </div>
                            <br>
                            <h4><fmt:message key="moderator.isAdmin"/></h4>
                            <c:choose>
                                <c:when test="${activeModerator.isAdmin()}">
                                    <fmt:message key="general.yes"/>
                                </c:when>
                                <c:otherwise>
                                    <fmt:message key="general.no"/>
                                </c:otherwise>
                            </c:choose>
                            <br><br>
                            <h4><fmt:message key="moderator.motivation"/></h4>
                            ${activeModerator.getMotivation()}
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="myAccount.nodata.info"/>
                        </c:otherwise>
                    </c:choose>
                </div>
                <c:if test="${activeModerator != null}">
                    <div class="panel-footer">
                        <div class="row">
                            <div class="col-md-12">
                                <a class="btn btn-primary buttonMargin" href="${base}webclient/myAccountEdit">
                                    <fmt:message key="general.edit"/></a>

                                <!-- Trigger confirmation dialog. -->
                                <button type="button" class="btn btn-primary buttonMargin" data-toggle="modal"
                                        data-target="#confirm">
                                    <fmt:message key="general.delete"/>
                                </button>
                            </div>
                        </div>
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
                                    <p><fmt:message key="myAccount.warning.delete"/></p>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default pull-right" data-dismiss="modal">
                                        <fmt:message key="general.no"/>
                                    </button>
                                    <form name="form" class="form-inline" method="post"
                                          action="${base}webclient/myAccount">
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