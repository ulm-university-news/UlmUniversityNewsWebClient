<%@ include file="header.jsp" %>
<c:set var="reminderId" value="${param.reminderId}" scope="session"/>
<c:set var="priorityHigh" value="HIGH" scope="page"/>

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

    <!-- Create reminder option. -->
    <div class="row">
        <div class="col-md-12">
            <p><fmt:message key="reminders.create.text"/></p>
            <a class="btn btn-primary" href="${base}webclient/reminderCreate" role="button">
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
                                            <p>
                                                <span>${reminder.getTitle()}</span>
                                                <span>&emsp;</span>
                                                <c:choose>
                                                    <c:when test="${reminder.isActive() == true &&
                                                    reminder.isExpired() == false}">
                                                        <c:if test="${reminder.getIgnore() == true}">
                                                            <span class="glyphicon glyphicon-flag pull-right"></span>
                                                            <span>&ensp;</span>
                                                        </c:if>
                                                        <span class="glyphicon glyphicon-time pull-right"></span>
                                                    </c:when>
                                                    <c:when test="${reminder.isActive() == true &&
                                                    reminder.isExpired() == true}">
                                                        <span class="glyphicon glyphicon-exclamation-sign pull-right"></span>
                                                        <span>&ensp;</span>
                                                        <span class="glyphicon glyphicon-time pull-right"></span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="glyphicon glyphicon-ban-circle pull-right"></span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </p>
                                        </a>
                                        <c:set var="currentReminder" value="${reminder}" scope="session"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:if test="${reminder.isActive() == true && reminder.isExpired() == false}">
                                            <a href="?reminderId=${reminder.getId()}" class="list-group-item">
                                                <p>
                                                    <span>${reminder.getTitle()}</span>
                                                    <span>&emsp;</span>
                                                    <c:if test="${reminder.getIgnore() == true}">
                                                        <span class="glyphicon glyphicon-flag pull-right"></span>
                                                        <span>&ensp;</span>
                                                    </c:if>
                                                    <span class="glyphicon glyphicon-time pull-right"></span>
                                                </p>
                                            </a>
                                        </c:if>
                                        <c:if test="${reminder.isActive() == true && reminder.isExpired() == true}">
                                            <a href="?reminderId=${reminder.getId()}"
                                               class="list-group-item">
                                                <p>
                                                    <span>${reminder.getTitle()}</span>
                                                    <span>&emsp;</span>
                                                    <span class="glyphicon glyphicon-exclamation-sign pull-right"></span>
                                                    <span>&ensp;</span>
                                                    <span class="glyphicon glyphicon-time pull-right"></span>
                                                </p>
                                            </a>
                                        </c:if>
                                        <c:if test="${reminder.isActive() == false}">
                                            <a href="?reminderId=${reminder.getId()}"
                                               class="list-group-item">
                                                <p>
                                                    <span>${reminder.getTitle()}</span>
                                                    <span>&emsp;</span>
                                                    <span class="glyphicon glyphicon-ban-circle pull-right"></span>
                                                </p>
                                            </a>
                                        </c:if>
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
                                    <h4><fmt:message key="reminder.skipNextEventFlag"/></h4>

                                    <c:if test="${currentReminder.getIgnore() == true}">
                                        <p><fmt:message key="reminder.skipNextEventFlag.yes" /></p>
                                    </c:if>
                                   <c:if test="${currentReminder.getIgnore() == false}">
                                       <p><fmt:message key="reminder.skipNextEventFlag.no" /></p>
                                   </c:if>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <h4><fmt:message key="reminder.nextDate"/></h4>

                                    <p>
                                        <c:choose>
                                            <c:when test="${currentReminder.isActive() == false}">
                                                <fmt:message key="reminder.deactivated"/>
                                            </c:when>
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
                                <c:if test="${currentReminder.isActive() == true}">
                                    <a class="btn btn-primary buttonMargin" href="${base}webclient/reminderEdit"
                                       role="button">
                                        <fmt:message key="general.edit"/>
                                    </a>

                                    <c:if test="${currentReminder.isExpired() == false &&
                                    currentReminder.getInterval() != 0}">
                                        <!-- Trigger set/reset ignore flag dialog. -->
                                        <button type="button" class="btn btn-primary buttonMargin" data-toggle="modal"
                                                data-target="#confirmIgnoreFlagOperation">
                                            <c:if test="${currentReminder.isIgnore() == true}">
                                                <fmt:message key="reminders.button.resetIgnoreFlag"/>
                                            </c:if>
                                            <c:if test="${currentReminder.isIgnore() == false}">
                                                <fmt:message key="reminders.button.setIgnoreFlag"/>
                                            </c:if>
                                        </button>
                                    </c:if>
                                </c:if>
                                <!-- Trigger activation/deactivation dialog. -->
                                <button type="button" class="btn btn-primary buttonMargin" data-toggle="modal"
                                        data-target="#confirmActivationOrDeactivation">
                                    <c:if test="${currentReminder.isActive() == true}">
                                        <fmt:message key="reminders.button.deactivate"/>
                                    </c:if>
                                    <c:if test="${currentReminder.isActive() == false}">
                                        <fmt:message key="reminders.button.activate"/>
                                    </c:if>
                                </button>
                                <!-- Trigger deletion confirmation dialog. -->
                                <button type="button" class="btn btn-primary buttonMargin" data-toggle="modal"
                                            data-target="#confirmDelete">
                                    <fmt:message key="general.delete"/>
                                </button>
                            </div>
                        </div>
                    </div>

                    <!-- Modal confirm dialog - Delete reminder. -->
                    <div id="confirmDelete" class="modal fade" role="dialog">
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
                    </div> <!-- End modal dialog delete reminder. -->

                    <!-- Modal confirm dialog - Activate/Deactivate reminder. -->
                    <div id="confirmActivationOrDeactivation" class="modal fade" role="dialog">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title"><fmt:message key="general.confirmation.title"/></h4>
                                </div>
                                <div class="modal-body">
                                    <c:if test="${currentReminder.isActive() == true}">
                                        <p>
                                            <fmt:message key="reminders.warning.deactivation">
                                                <fmt:param>${currentReminder.getTitle()}</fmt:param>
                                            </fmt:message>
                                        </p>
                                    </c:if>
                                    <c:if test="${currentReminder.isActive() == false}">
                                        <p>
                                            <fmt:message key="reminders.warning.activation">
                                                <fmt:param>${currentReminder.getTitle()}</fmt:param>
                                            </fmt:message>
                                        </p>
                                    </c:if>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default pull-right" data-dismiss="modal">
                                        <fmt:message key="general.no"/>
                                    </button>
                                    <form name="form" class="form-inline" method="post"
                                          action="${base}webclient/reminders">
                                        <c:if test="${currentReminder.isActive() == true}">
                                            <button type="submit" name="button" value="deactivate"
                                                    class="btn btn-primary pull-left">
                                                <fmt:message key="general.yes"/>
                                            </button>
                                        </c:if>
                                        <c:if test="${currentReminder.isActive() == false}">
                                            <button type="submit" name="button" value="activate"
                                                    class="btn btn-primary pull-left">
                                                <fmt:message key="general.yes"/>
                                            </button>
                                        </c:if>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div> <!-- End modal dialog activation/deactivation. -->

                    <!-- Modal confirm dialog - Set ignore flag / reset ignore flag. -->
                    <div id="confirmIgnoreFlagOperation" class="modal fade" role="dialog">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title"><fmt:message key="general.confirmation.title"/></h4>
                                </div>
                                <div class="modal-body">
                                    <c:if test="${currentReminder.isIgnore() == true}">
                                        <p>
                                            <fmt:message key="reminders.warning.skipNextEvent.reset">
                                                <fmt:param>${currentReminder.getTitle()}</fmt:param>
                                            </fmt:message>
                                        </p>
                                    </c:if>
                                    <c:if test="${currentReminder.isIgnore() == false}">
                                        <p>
                                            <fmt:message key="reminders.warning.skipNextEvent.set">
                                                <fmt:param>${currentReminder.getTitle()}</fmt:param>
                                            </fmt:message>
                                        </p>
                                    </c:if>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default pull-right" data-dismiss="modal">
                                        <fmt:message key="general.no"/>
                                    </button>
                                    <form name="form" class="form-inline" method="post"
                                          action="${base}webclient/reminders">
                                        <c:if test="${currentReminder.isIgnore() == true}">
                                            <button type="submit" name="button" value="resetIgnoreFlag"
                                                    class="btn btn-primary pull-left">
                                                <fmt:message key="general.yes"/>
                                            </button>
                                        </c:if>
                                        <c:if test="${currentReminder.isIgnore() == false}">
                                            <button type="submit" name="button" value="setIgnoreFlag"
                                                    class="btn btn-primary pull-left">
                                                <fmt:message key="general.yes"/>
                                            </button>
                                        </c:if>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div> <!-- End modal dialog set ignore / reset ignore flag. -->
                </c:if>
            </div>
        </div>
    </div>
</div>

<!-- Page footer. -->
<%@ include file="footer.jsp" %>