<!-- Page header. -->
<%@ include file="header.jsp" %>

<c:set var="moderatorId" value="${param.moderatorId}" scope="request" />

<style>
    .row-eq-height {
        display: -webkit-box;
        display: -webkit-flex;
        display: -ms-flexbox;
        display: flex;
    }

    .scrollEnabledList {
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

<!-- PageContent. -->
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <h3><fmt:message key="manageChannelModerators.title"/></h3>

            <p><fmt:message key="manageChannelModerators.info"/></p>
            <br>
        </div>
    </div>

    <!-- Error, warnings and information alerts. -->
    <c:if test="${loadingResponsibleModeratorsFailure != null}">
        <div class="row">
            <div class="col-md-12">
                <div class="alert alert-danger" role="alert">
                    <strong><fmt:message key="general.alert.failure" /></strong>
                    <p>${loadingResponsibleModeratorsFailure}</p>
                </div>
            </div>
        </div>
    </c:if>
    <c:if test="${managingModeratorsOperationFailed != null}">
        <div class="row">
            <div class="col-md-12">
                <div class="alert alert-danger" role="alert">
                    <strong><fmt:message key="general.alert.failure" /></strong>
                    <p>${managingModeratorsOperationFailed}</p>
                </div>
            </div>
        </div>
    </c:if>
    <c:if test="${param.successful != null}">
        <div class="row">
            <div class="col-md-12">
                <div class="alert alert-success" role="alert">
                    <strong><fmt:message key="general.alert.success" /></strong>
                    <p><fmt:message key="general.alert.success.info" /> </p>
                </div>
            </div>
        </div>
    </c:if>

    <!-- Information about selected channel. -->
    <div class="row">
        <div class="col-md-12">
            <p><fmt:message key="manageChannelModerators.info.selectedChannel"/> <b>${currentChannel.getName()}</b></p>
            <br>
        </div>
    </div>

    <!-- Add responsible moderator functionality. -->
    <div class="row">
        <div class="col-md-10">
            <p><fmt:message key="manageChannelModerators.info.addModerator"/></p>
        </div>
        <div class="col-md-2">
            <!-- Trigger add moderator dialog. -->
            <button type="button" class="btn btn-primary pull-right" data-toggle="modal"
                    data-target="#confirmAndAddModeratorDialog">
                <fmt:message key="manageChannelModerators.button.addModerator"/>
            </button>
        </div>

        <!-- Modal add moderator dialog: add moderator. -->
        <div id="confirmAndAddModeratorDialog" class="modal fade" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <!-- Header -->
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">
                            <fmt:message key="manageChannelModerators.dialog.addModerator.title"/>
                        </h4>
                    </div>
                    <!-- Create form for submitting moderator name. -->
                    <form name="addModeratorForm" class="form" method="post"
                          action="${base}webclient/manageChannelModerators">
                        <!-- Body with input field. -->
                        <div class="modal-body">
                            <p>
                                <fmt:message key="manageChannelModerators.dialog.addModerator.description" />
                            </p>
                            <div class="form-group">
                                <label for="moderatorName" class="control-label">
                                    <fmt:message key="manageChannelModerators.dialog.addModerator.inputfield.label" />
                                </label>
                                <input type="text" name="usernameModerator" class="form-control" id="moderatorName"
                                        placeholder="
                                        <fmt:message
                                        key="manageChannelModerators.dialog.addModerator.inputfield.placeholder" />
                                        ">
                            </div>
                        </div>
                        <!-- Footer with submit button. -->
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default pull-right" data-dismiss="modal">
                                <fmt:message key="general.cancel"/>
                            </button>
                            <button type="submit" name="task" value="addModerator"
                                    class="btn btn-primary pull-left">
                                <fmt:message key="manageChannelModerators.dialog.addModerator.submitButton.label"/>
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <br>

    <div class="row row-eq-height">
        <div class="col-md-3">
            <div class="list-group">
                <div class="list-group-item active">
                    <h4 class="list-group-item-heading">
                        <fmt:message key="manageChannelModerators.list.heading"/>
                    </h4>
                </div>

                <!-- Items -->
                <div class="scrollEnabledList">
                    <c:choose>
                        <c:when test="${responsibleModerators != null && !responsibleModerators.isEmpty()}">
                            <c:forEach items="${responsibleModerators}" var="moderator">
                                <c:if test="${moderatorId == null && moderator != null}">
                                    <!-- First item will be set as the active one. -->
                                    <c:set var="moderatorId" value="${moderator.getId()}" />
                                </c:if>
                                <c:choose>
                                    <c:when test="${moderatorId != null && moderatorId == moderator.getId()}">
                                        <a href="?moderatorId=${moderator.getId()}"
                                           class="list-group-item list-group-item-info">
                                            <p>
                                                ${moderator.getLastName()}, ${moderator.getFirstName()}
                                                <c:choose>
                                                    <c:when test="${moderator.isActive() == true}">
                                                        <span class="glyphicon glyphicon-ok-sign pull-right" ></span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="glyphicon glyphicon-minus-sign pull-right" ></span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </p>
                                        </a>
                                        <c:set var="selectedModerator" value="${moderator}" scope="session"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:if test="${moderator.isActive() == true}">
                                            <a href="?moderatorId=${moderator.getId()}" class="list-group-item">
                                                <p>
                                                    ${moderator.getLastName()}, ${moderator.getFirstName()}
                                                    <span class="glyphicon glyphicon-ok-sign pull-right" ></span>
                                                </p>
                                            </a>
                                        </c:if>
                                        <c:if test="${moderator.isActive() == false}">
                                            <a href="?moderatorId=${moderator.getId()}"
                                               class="list-group-item list-group-item-warning">
                                                <p>
                                                    ${moderator.getLastName()}, ${moderator.getFirstName()}
                                                    <span class="glyphicon glyphicon-minus-sign pull-right" ></span>
                                                </p>
                                            </a>
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="list-group-item">
                                <fmt:message key="manageChannelModerators.none" />
                            </div>
                            ${selectedModerator = null}
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <div class="col-md-9">
            <div class="panel panel-info">
                <div class="panel-heading">
                    <c:choose>
                        <c:when test="${selectedModerator != null}">
                            <h4>${selectedModerator.getFirstName()} ${selectedModerator.getLastName()}</h4>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="manageChannelModerators.nodata" />
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="panel-body scrollEnabledPanel">
                    <c:choose>
                        <c:when test="${selectedModerator != null}">
                            <div class="row">
                                <div class="col-md-5">
                                    <h4><fmt:message key="manageChannelModerators.panel.firstName"/> </h4>
                                    <p>${selectedModerator.getFirstName()}</p>
                                </div>
                                <div class="col-md-5">
                                    <h4><fmt:message key="manageChannelModerators.panel.lastName"/> </h4>
                                    <p>${selectedModerator.getLastName()}</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-5">
                                    <h4><fmt:message key="manageChannelModerators.panel.email"/> </h4>
                                    <p>${selectedModerator.getEmail()}</p>
                                </div>
                                <div class="col-md-5">
                                    <h4><fmt:message key="manageChannelModerators.panel.active"/> </h4>
                                    <c:choose>
                                        <c:when test="${selectedModerator.isActive() == true}">
                                            <fmt:message key="manageChannelModerators.panel.active.true" />
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:message key="manageChannelModerators.panel.active.false" />
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="manageChannelModerators.nodata.info" />
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="panel-footer">
                    <div class="row">
                        <div class="col-md-12">
                            <c:if test="${selectedModerator != null}">
                                <c:choose>
                                    <c:when test="${selectedModerator.isActive() == true}">
                                        <!-- Trigger confirmation dialog. -->
                                        <button type="button" class="btn btn-primary pull-right" data-toggle="modal"
                                                data-target="#confirm">
                                            <fmt:message key="manageChannelModerators.confirmation.button.revoke"/>
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <!-- Trigger confirmation dialog. -->
                                        <button type="button" class="btn btn-primary pull-right" data-toggle="modal"
                                                data-target="#confirm">
                                            <fmt:message key="manageChannelModerators.confirmation.button.reactivate"/>
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                        </div>
                    </div>
                </div>
                <!-- Modal confirm dialog -->
                <div id="confirm" class="modal fade" role="dialog">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 class="modal-title"><fmt:message key="general.confirmation.title"/></h4>
                            </div>
                            <div class="modal-body">
                                <c:if test="${selectedModerator.isActive() == true}">
                                    <p><fmt:message key="manageChannelModerators.confirmation.revoke"/></p>
                                </c:if>
                                <c:if test="${selectedModerator.isActive() == false}">
                                    <p><fmt:message key="manageChannelModerators.confirmation.reactivate"/></p>
                                </c:if>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default pull-right" data-dismiss="modal">
                                    <fmt:message key="general.no"/>
                                </button>
                                <form name="form" class="form-inline" method="post"
                                      action="${base}webclient/manageChannelModerators">
                                    <c:if test="${selectedModerator.isActive() == true}">
                                        <button type="submit" name="task" value="revoke"
                                                class="btn btn-primary pull-left">
                                            <fmt:message key="general.yes"/>
                                        </button>
                                    </c:if>
                                    <c:if test="${selectedModerator.isActive() == false}">
                                        <button type="submit" name="task" value="reactivate"
                                                class="btn btn-primary pull-left">
                                            <fmt:message key="general.yes"/>
                                        </button>
                                    </c:if>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>





<!-- Page footer. -->
<%@ include file="footer.jsp" %>