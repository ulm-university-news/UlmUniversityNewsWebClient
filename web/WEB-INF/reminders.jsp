<!-- Page header. -->
<%@ include file="header.jsp" %>
<c:set var="reminderId" value="${param.reminderId}" scope="session"/>
<c:set var="priorityHigh" value="HIGH" scope="page"/>

<style>
    .heightRestrictedList {
        min-height: 100px;
        max-height: 60vh;
        position: relative;
    }

    .scrollEnabled {
        min-height: 100px;
        max-height: 60vh;
        overflow-y: auto;
    }

    .scrollEnabledPanel {
        min-height: 100px;
        max-height: 50vh;
        overflow-y: auto;
    }
</style>

<!-- Page content. -->
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <h3><fmt:message key="reminders.title"/></h3>

            <p><fmt:message key="reminders.info"/></p>
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

    <!-- Create channel option. -->
    <div class="row">
        <div class="col-md-12">
            <p><fmt:message key="reminders.create.text"/></p>
            <a class="btn btn-primary" href="${base}webclient/createReminder" role="button">
                <fmt:message key="reminders.create.button"/>
            </a>
        </div>
    </div>

    <br>
    <br>

    <div class="row">
        <div class="col-md-3">
            <div class="list-group">
                <div class="list-group-item active">
                    <h4 class="list-group-item-heading"><fmt:message key="reminders.list.heading"/></h4>
                </div>
                <!-- Make content scrollable. -->
                <div class="scrollEnabled">
                    <c:choose>
                        <c:when test="${reminders != null && !reminders.isEmpty()}">
                            <c:forEach items="${reminders}" var="reminder">
                                <c:if test="${reminderId == null && reminder != null}">
                                    <c:set var="reminderId" value="${reminder.getId()}"/>
                                </c:if>
                                <c:choose>
                                    <c:when test="${reminderId != null && reminderId == reminder.getId()}">
                                        <a href="?reminderId=${reminder.getId()}"
                                           class="list-group-item list-group-item-info">
                                                ${reminder.getTitle()}
                                        </a>
                                        <c:set var="currentReminder" value="${reminder}" scope="session"/>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="?reminderId=${reminder.getId()}" class="list-group-item">
                                                ${reminder.getTitle()}
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="list-group-item">
                                <fmt:message key="general.none"/>
                            </div>
                            ${currentReminder = null}
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        <div class="col-md-9">
            <div class="panel panel-info">
                <div class="panel-heading">
                    <c:choose>
                        <c:when test="${currentReminder != null}">
                            <h4>${currentReminder.getTitle()}</h4>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="reminders.nodata"/>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="panel-body scrollEnabledPanel">
                    <c:choose>
                        <c:when test="${currentReminder != null}">
                            <div class="row">
                                <div class="col-md-12">
                                    <h4><fmt:message key="reminder.title"/></h4>

                                    <p>${currentReminder.getTitle()}</p>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <h4><fmt:message key="reminder.text"/></h4>

                                    <p>${currentReminder.getText()}</p>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <h4><fmt:message key="reminder.priority"/></h4>

                                    <p>
                                        <c:choose>
                                            <c:when test="${currentReminder.getPriority() == priorityHigh}">
                                                <fmt:message key="reminder.priority.high"/>
                                            </c:when>
                                            <c:otherwise>
                                                <fmt:message key="reminder.priority.normal"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <h4><fmt:message key="reminder.interval"/></h4>

                                    <p>${currentReminder.getIntervalText(language)}</p>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <h4><fmt:message key="reminder.nextDate"/></h4>

                                    <p>
                                        <c:choose>
                                            <c:when test="${currentReminder.isExpired()}">
                                                <fmt:message key="reminder.expired"/>
                                            </c:when>
                                            <c:otherwise>
                                                <joda:format value="${currentReminder.getNextDate()}"
                                                             pattern="${dateTimePattern}"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <h4><fmt:message key="reminder.startDate"/></h4>

                                    <p><joda:format value="${currentReminder.getStartDate()}"
                                                    pattern="${dateTimePattern}"/></p>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <h4><fmt:message key="reminder.endDate"/></h4>

                                    <p><joda:format value="${currentReminder.getEndDate()}"
                                                    pattern="${dateTimePattern}"/></p>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <h4><fmt:message key="general.creationDate"/></h4>

                                    <p><joda:format value="${currentReminder.getCreationDate()}"
                                                    pattern="${dateTimePattern}"/></p>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <h4><fmt:message key="general.modificationDate"/></h4>

                                    <p><joda:format value="${currentReminder.getModificationDate()}"
                                                    pattern="${dateTimePattern}"/></p>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="reminders.nodata.info"/>
                        </c:otherwise>
                    </c:choose>
                </div>
                <c:if test="${currentReminder != null}">
                    <div class="panel-footer">
                        <div class="row">
                            <div class="col-md-12">
                                <form name="form" class="form-inline" method="post" action="${base}webclient/reminders">
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
                                    <p><fmt:message key="reminders.warning.delete"/></p>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default pull-right" data-dismiss="modal">
                                        <fmt:message key="general.no"/>
                                    </button>
                                    <form name="form" class="form-inline" method="post"
                                          action="${base}webclient/reminders">
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