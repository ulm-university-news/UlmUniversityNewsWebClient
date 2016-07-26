<!-- Page header. -->
<%@ include file="header.jsp" %>
<c:set var="groupId" value="${param.groupId}" scope="session"/>
<c:set var="typeTutorial" value="TUTORIAL" scope="page"/>

<!-- Page content. -->
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <h3><fmt:message key="groups.title"/></h3>

            <p><fmt:message key="groups.info"/></p>
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
                    <h4 class="list-group-item-heading"><fmt:message key="groups.list.heading"/></h4>
                </div>
                <c:choose>
                    <c:when test="${groups != null && !groups.isEmpty()}">
                        <c:forEach items="${groups}" var="group">
                            <c:if test="${groupId == null && group != null}">
                                <c:set var="groupId" value="${group.getId()}"/>
                            </c:if>
                            <c:choose>
                                <c:when test="${groupId != null && groupId == group.getId()}">
                                    <a href="?groupId=${group.getId()}"
                                       class="list-group-item list-group-item-info">
                                            ${group.getName()}
                                    </a>
                                    <c:set var="currentGroup" value="${group}" scope="session"/>
                                </c:when>
                                <c:otherwise>
                                    <a href="?groupId=${group.getId()}" class="list-group-item">
                                            ${group.getName()}
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="list-group-item">
                            <fmt:message key="general.none"/>
                        </div>
                        ${currentGroup = null}
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="col-md-9">
            <div class="panel panel-info">
                <div class="panel-heading">
                    <c:choose>
                        <c:when test="${currentGroup != null}">
                            <h4>${currentGroup.getName()}</h4>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="groups.nodata"/>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="panel-body">
                    <c:choose>
                        <c:when test="${currentGroup != null}">
                            <div class="row">
                                <div class="col-md-12">
                                    <h4><fmt:message key="group.type"/></h4>

                                    <p>
                                        <c:choose>
                                            <c:when test="${currentGroup.getGroupType() == typeTutorial}">
                                                <fmt:message key="group.type.tutorial"/>
                                            </c:when>
                                            <c:otherwise>
                                                <fmt:message key="group.type.working"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <h4><fmt:message key="general.description"/></h4>

                                    <p>${currentGroup.getDescription()}</p>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <h4><fmt:message key="general.term"/></h4>

                                    <p>${currentGroup.getTerm()}</p>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <h4><fmt:message key="general.creationDate"/></h4>

                                    <p><joda:format value="${currentGroup.getCreationDate()}"
                                                    pattern="${dateTimePattern}"/></p>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <h4><fmt:message key="general.modificationDate"/></h4>

                                    <p><joda:format value="${currentGroup.getModificationDate()}"
                                                    pattern="${dateTimePattern}"/></p>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="groups.nodata.info"/>
                        </c:otherwise>
                    </c:choose>
                </div>
                <c:if test="${currentGroup != null}">
                    <div class="panel-footer">
                        <div class="row">
                            <div class="col-md-12">
                                <form name="form" class="form-inline" method="post" action="${base}webclient/groups">
                                    <!-- Trigger confirmation dialog. -->
                                    <button type="button" class="btn btn-primary" data-toggle="modal"
                                            data-target="#confirm">
                                        <fmt:message key="general.delete"/>
                                    </button>
                                </form>
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
                                    <p><fmt:message key="groups.warning.delete"/></p>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default pull-right" data-dismiss="modal">
                                        <fmt:message key="general.no"/>
                                    </button>
                                    <form name="form" class="form-inline" method="post"
                                          action="${base}webclient/groups">
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