<!-- Page header. -->
<%@include file="header.jsp"%>

<style>
    html, body {
        height: 100%;
    }

    .container {
        min-height: 100%;
        overflow: hidden;
    }

    #messageCol {
        margin-bottom: -9999px;
        padding-bottom: 9999px;
    }

    #channelDetailsCol {
        margin-bottom: -9999px;
        padding-bottom: 9999px;
    }

    #announcementList {
        overflow: auto;
        max-height: 65vh;
    }

</style>

<div class="container">
    <div class="row">
        <div class="col-md-12">
            <h3><fmt:message key="channelDetails.title" /></h3>
            <p><fmt:message key="channelDetails.info" /> </p>
            <br>
        </div>
    </div>

    <!-- Error, warnings and information alerts. -->
    <c:if test="${loadingChannelDetailsFailure != null}">
        <div class="row">
            <div class="col-md-12">
                <div class="alert alert-danger" role="alert">
                    <strong><fmt:message key="general.alert.failure" /></strong>
                    <p>${loadingChannelDetailsFailure}</p>
                </div>
            </div>
        </div>
    </c:if>
    <c:if test="${channelDetailsCreateAnnouncementError != null}">
        <div class="row">
            <div class="col-md-12">
                <div class="alert alert-danger" role="alert">
                    <strong><fmt:message key="general.alert.failure" /></strong>
                    <p>${channelDetailsCreateAnnouncementError}</p>
                </div>
            </div>
        </div>
    </c:if>
    <c:if test="${param.successful != null}">
        <div class="row">
            <div class="col-md-12">
                <div class="alert alert-success" role="alert">
                    <strong><fmt:message key="general.alert.success" /></strong>
                    <p><fmt:message key="general.alert.success.info" /></p>
                </div>
            </div>
        </div>
    </c:if>

    <div class="row">
        <!-- Messages -->
        <div class="col-md-12" id="messageCol">
            <div class="wrapper">
                <div class="panel panel-info">
                    <!-- Header -->
                    <div class="panel-heading">
                        <h4><fmt:message key="channelDetails.messages.panel.header" /></h4>
                    </div>

                    <!-- Body -->
                    <div class="panel-body" id="announcementList">
                        <div class="list-group">
                            <c:choose>
                                <c:when test="${announcements != null && !announcements.isEmpty()}">
                                    <c:forEach items="${announcements}" var="announcement">
                                        <div class="list-group-item">
                                            <p><b>${announcement.getTitle()}</b>
                                                <span class="pull-right">
                                                    <joda:format value="${announcement.getCreationDate()}"
                                                                 pattern="yyyy-MM-dd HH:mm:ss" />
                                                </span>
                                            </p>
                                            <p>${announcement.getText()}</p>
                                        </div>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <div class="list-group-item">
                                        <fmt:message key="channelDetails.noAnnouncements"/>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <!-- Footer -->
                    <div class="panel-footer">
                        <div class="row">
                            <div class="col-md-12">
                                <button class="btn btn-primary pull-right" type="button" name="newMessageBtn"
                                        data-toggle="modal" data-target="#newAnnouncementDialog">
                                    <fmt:message key="channelDetails.button.newMessage" />
                                </button>
                            </div>
                        </div>
                    </div>

                    <!-- New announcement dialog. -->
                    <div id="newAnnouncementDialog" class="modal fade" role="dialog">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <!-- Header -->
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title">
                                        <fmt:message key="channelDetails.dialog.newMessage.header"/>
                                    </h4>
                                </div>

                                <!-- Form for submitting message content -->
                                <form name="sendAnnouncementForm" class="form" method="post"
                                      action="${base}webclient/sendAnnouncement">
                                    <!-- Body -->
                                    <div class="modal-body">
                                        <p>
                                            <fmt:message key="channelDetails.dialog.newMessage.description"/>
                                        </p>
                                        <!-- Title -->
                                        <c:if test="${announcementTitleValidationError == null}">
                                            <!-- No validation error on title -->
                                            <div class="form-group">
                                                <label for="announcementTitle" class="control-label">
                                                    <fmt:message
                                                            key="channelDetails.dialog.newMessage.title.textField.label" />
                                                </label>
                                                <input type="text" name="announcementTitle" class="form-control"
                                                       id="announcementTitle"
                                                       placeholder="<fmt:message
                                                            key="channelDetails.dialog.newMessage.title.textField.placeholder" />"
                                                        value="${param.announcementTitle}">
                                            </div>
                                        </c:if>
                                        <c:if test="${announcementTitleValidationError != null}">
                                            <!-- Validation error on title. -->
                                            <div class="form-group has-error has-feedback">
                                                <label class="control-label" for="announcementTitleError">
                                                    <fmt:message
                                                            key="channelDetails.dialog.newMessage.title.textField.label"
                                                            />
                                                </label>
                                                <input type="text" name="announcementTitle" class="form-control"
                                                       id="announcementTitleError"
                                                       value="${param.announcementTitle}" />
                                                <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                                                <span class="help-block">${announcementTitleValidationError}</span>
                                            </div>
                                        </c:if>
                                        <!-- Priority -->
                                        <div class="form-group">
                                            <label for="priorityComboBox" class="control-label">
                                                <fmt:message
                                                        key="channelDetails.dialog.newMessage.priorityComboBox.label" />
                                            </label>
                                            <select name="priorityValue" class="form-control" id="priorityComboBox">
                                                <option value="normal">
                                                    <fmt:message
                                                            key="channelDetails.dialog.newMessage.priorityComboBox.value.normal" />
                                                </option>
                                                <option value="high">
                                                    <fmt:message
                                                            key="channelDetails.dialog.newMessage.priorityComboBox.value.high" />
                                                </option>
                                            </select>
                                        </div>
                                        <!-- Message content -->
                                        <c:if test="${announcementTextValidationError == null}">
                                            <div class="form-group">
                                                <!-- No validation error on announcement text. -->
                                                <label for="announcementText" class="control-label">
                                                    <fmt:message
                                                            key="channelDetails.dialog.newMessage.textField.label"/>
                                                </label>
                                                <input type="text" name="announcementText" class="form-control"
                                                       id="announcementText"
                                                       placeholder="<fmt:message
                                                            key="channelDetails.dialog.newMessage.textField.placeholder" />"
                                                       value="${param.announcementText}"/>
                                            </div>
                                        </c:if>
                                        <c:if test="${announcementTextValidationError != null}">
                                            <div class="form-group has-error has-feedback">
                                                <!-- Validation error on announcement text. -->
                                                <label for="announcementTextError" class="control-label">
                                                    <fmt:message
                                                            key="channelDetails.dialog.newMessage.textField.label"/>
                                                </label>
                                                <input type="text" name="announcementText" class="form-control"
                                                       id="announcementTextError"
                                                       placeholder="<fmt:message
                                                      key="channelDetails.dialog.newMessage.textField.placeholder" />"
                                                       value="${param.announcementText}"/>
                                                <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                                                <span class="help-block">${announcementTextValidationError}</span>
                                            </div>
                                        </c:if>

                                    </div>

                                    <!-- Footer -->
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-default pull-right" data-dismiss="modal">
                                            <fmt:message key="general.cancel"/>
                                        </button>
                                        <button type="submit" name="task" value="sendAnnouncement"
                                                class="btn btn-primary pull-left">
                                            <fmt:message key="channelDetails.dialog.newMessage.submitButton.label"/>
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>

        <!-- Edit channel details form. -->
        <%--<div class="col-md-6" id="channelDetailsCol">--%>
            <%--<div class="panel panel-info">--%>
                <%--<!-- Header -->--%>
                <%--<div class="panel-heading">--%>
                    <%--<h4><fmt:message key="channelDetails.details.panel.header" /></h4>--%>
                <%--</div>--%>

                <%--<!-- Body -->--%>
                <%--<div class="panel-body">--%>

                <%--</div>--%>


                <%--<!-- Footer -->--%>
                <%--<div class="panel-footer">--%>

                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>
    </div>
</div>

<script>
    $(document).ready(function(){
        var d = $('#announcementList');
        d.scrollTop(d.prop("scrollHeight"));

        if ($('#announcementTitleError').length || $('#announcementTextError').length) {
            $('#newAnnouncementDialog').modal("show");
        }
    });
</script>

<!-- Page footer. -->
<%@ include file="footer.jsp" %>