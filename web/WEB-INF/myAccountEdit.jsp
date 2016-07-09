<!-- Page header. -->
<%@ include file="header.jsp" %>

<!-- Page content. -->
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <h3><fmt:message key="myAccountEdit.title"/></h3>

            <p><fmt:message key="myAccountEdit.info"/></p>
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
            <form class="form-horizontal" role="form" method="post" action="${base}webclient/myAccountEdit">
                <!-- Name of the moderator account -->
                <!--
                <c:if test="${registerNameValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">
                            <fmt:message key="moderator.name"/>
                        </label>

                        <div class="col-sm-10">
                            <c:choose>
                                <c:when test="${param.username != null}">
                                    <input class="form-control" name="username" id="name" type="text"
                                           placeholder="<fmt:message key="register.form.label.name.desc" />"
                                           value="${param.username}">
                                </c:when>
                                <c:otherwise>
                                    <input class="form-control" name="username" id="name" type="text"
                                           placeholder="<fmt:message key="register.form.label.name.desc" />"
                                           value="${activeModerator.getName()}">
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:if>
                <c:if test="${registerNameValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="nameInputError">
                            <fmt:message key="moderator.name"/>
                        </label>

                        <div class="col-sm-10">
                            <input type="text" name="username" class="form-control" id="nameInputError"
                                   value="${param.username}">
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                            <span class="help-block">${registerNameValidationError}</span>
                        </div>
                    </div>
                </c:if>
                -->

                <!-- First name of the requestor. -->
                <c:if test="${registerFirstNameValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">
                            <fmt:message key="moderator.firstname"/>
                        </label>

                        <div class="col-sm-10">
                            <c:choose>
                                <c:when test="${param.firstname != null}">
                                    <input class="form-control" name="firstname" id="firstname" type="text"
                                           placeholder="<fmt:message key="register.form.label.firstname.desc" />"
                                           value="${param.firstname}">
                                </c:when>
                                <c:otherwise>
                                    <input class="form-control" name="firstname" id="firstname" type="text"
                                           placeholder="<fmt:message key="register.form.label.firstname.desc" />"
                                           value="${activeModerator.getFirstName()}">
                                </c:otherwise>
                            </c:choose>

                        </div>
                    </div>
                </c:if>
                <c:if test="${registerFirstNameValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="firstnameInputError">
                            <fmt:message key="moderator.firstname"/>
                        </label>

                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="firstname" id="firstnameInputError"
                                   value="${param.firstname}">
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                            <span class="help-block">${registerFirstNameValidationError}</span>
                        </div>
                    </div>
                </c:if>

                <!-- Last name of the requestor. -->
                <c:if test="${registerLastNameValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">
                            <fmt:message key="moderator.lastname"/>
                        </label>

                        <div class="col-sm-10">
                            <c:choose>
                                <c:when test="${param.lastname != null}">
                                    <input class="form-control" name="lastname" id="lastname" type="text"
                                           placeholder="<fmt:message key="register.form.label.lastname.desc" />"
                                           value="${param.lastname}">
                                </c:when>
                                <c:otherwise>
                                    <input class="form-control" name="lastname" id="lastname" type="text"
                                           placeholder="<fmt:message key="register.form.label.lastname.desc" />"
                                           value="${activeModerator.getLastName()}">
                                </c:otherwise>
                            </c:choose>

                        </div>
                    </div>
                </c:if>
                <c:if test="${registerLastNameValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="lastnameInputError">
                            <fmt:message key="moderator.lastname"/>
                        </label>

                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="lastname" id="lastnameInputError"
                                   value="${param.lastname}">
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                            <span class="help-block">${registerLastNameValidationError}</span>
                        </div>
                    </div>
                </c:if>

                <!-- Email of the requestor. -->
                <c:if test="${registerEmailValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">
                            <fmt:message key="moderator.email"/>
                        </label>

                        <div class="col-sm-10">
                            <c:choose>
                                <c:when test="${param.email != null}">
                                    <input class="form-control" name="email" id="email" type="email"
                                           placeholder="<fmt:message key="register.form.label.email.desc" />"
                                           value="${param.email}">
                                </c:when>
                                <c:otherwise>
                                    <input class="form-control" name="email" id="email" type="email"
                                           placeholder="<fmt:message key="register.form.label.email.desc" />"
                                           value="${activeModerator.getEmail()}">
                                </c:otherwise>
                            </c:choose>

                        </div>
                    </div>
                </c:if>
                <c:if test="${registerEmailValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="emailInputError">
                            <fmt:message key="moderator.email"/>
                        </label>

                        <div class="col-sm-10">
                            <input type="email" class="form-control" name="email" id="emailInputError"
                                   value="${param.email}">
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                            <span class="help-block">${registerEmailValidationError}</span>
                        </div>
                    </div>
                </c:if>

                <!-- Motivation -->
                <!--
                <c:if test="${registerMotivationValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">
                            <fmt:message key="moderator.motivation"/>
                        </label>

                        <div class="col-sm-10">
                            <c:choose>
                                <c:when test="${param.motivation != null}">
                                    <input class="form-control" name="motivation" id="motivation" type="text"
                                           placeholder="<fmt:message key="register.form.label.motivation.desc" />"
                                           value="${param.motivation}">
                                </c:when>
                                <c:otherwise>
                                    <input class="form-control" name="motivation" id="motivation" type="text"
                                           placeholder="<fmt:message key="register.form.label.motivation.desc" />"
                                           value="${activeModerator.getMotivation()}">
                                </c:otherwise>
                            </c:choose>

                        </div>
                    </div>
                </c:if>
                <c:if test="${registerMotivationValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="motivationInputError">
                            <fmt:message key="moderator.motivation"/>
                        </label>

                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="motivation" id="motivationInputError"
                                   value="${param.motivation}">
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                            <span class="help-block">${registerMotivationValidationError}</span>
                        </div>
                    </div>
                </c:if>
                -->

                <br>

                <div class="row">
                    <div class="col-md-12">
                        <div class="text-center">
                            <button class="btn btn-primary btn-block"
                                    type="submit" name="task" value="register"><fmt:message key="general.edit"/>
                            </button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <h3><fmt:message key="myAccountEdit.password.title"/></h3>

            <p><fmt:message key="myAccountEdit.password.info"/></p>
        </div>
    </div>
    <br>

    <div class="row">
        <div class="col-md-12">
            <button class="btn btn-primary btn-block" type="button" name="newMessageBtn"
                    data-toggle="modal" data-target="#newPasswordDialog">
                <fmt:message key="myAccountEdit.password.change"/>
            </button>
        </div>
    </div>

    <!-- New password dialog. -->
    <div id="newPasswordDialog" class="modal fade" role="dialog">
        <div class="modal-dialog">
            <div class="modal-content">
                <!-- Header -->
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">
                        <fmt:message key="myAccountEdit.password.dialog.header"/>
                    </h4>
                </div>

                <!-- Form for submitting message content -->
                <form name="sendAnnouncementForm" class="form" method="post"
                      action="${base}webclient/myAccountEdit">
                    <!-- Body -->
                    <div class="modal-body">
                        <p>
                            <fmt:message key="myAccountEdit.password.dialog.description"/>
                        </p>
                        <!-- Current password -->
                        <c:if test="${passwordCurrentValidationError == null}">
                            <!-- No validation error -->
                            <div class="form-group">
                                <label for="passwordCurrent" class="control-label">
                                    <fmt:message
                                            key="myAccountEdit.password.dialog.current"/>
                                </label>
                                <input type="password" name="passwordCurrent" class="form-control"
                                       id="passwordCurrent"
                                       placeholder="<fmt:message
                                                            key="myAccountEdit.password.dialog.current.placeholder" />"
                                       value="${param.passwordCurrent}">
                            </div>
                        </c:if>
                        <c:if test="${passwordCurrentValidationError != null}">
                            <!-- Validation error -->
                            <div class="form-group has-error has-feedback">
                                <label class="control-label" for="passwordCurrentError">
                                    <fmt:message
                                            key="myAccountEdit.password.dialog.current"/>
                                </label>
                                <input type="password" name="passwordCurrent" class="form-control"
                                       id="passwordCurrentError"
                                       value="${param.passwordCurrent}"/>
                                <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                                <span class="help-block">${passwordCurrentValidationError}</span>
                            </div>
                            ${passwordCurrentValidationError = null}
                        </c:if>

                        <!-- New password -->
                        <c:if test="${passwordNewValidationError == null}">
                            <!-- No validation error -->
                            <div class="form-group">
                                <label for="passwordNew" class="control-label">
                                    <fmt:message
                                            key="myAccountEdit.password.dialog.new"/>
                                </label>
                                <input type="password" name="passwordNew" class="form-control"
                                       id="passwordNew"
                                       placeholder="<fmt:message
                                                            key="myAccountEdit.password.dialog.new.placeholder" />"
                                       value="${param.passwordNew}">
                            </div>
                        </c:if>
                        <c:if test="${passwordNewValidationError != null}">
                            <!-- Validation error -->
                            <div class="form-group has-error has-feedback">
                                <label class="control-label" for="passwordNewError">
                                    <fmt:message key="myAccountEdit.password.dialog.new"/>
                                </label>
                                <input type="password" name="passwordNew" class="form-control"
                                       id="passwordNewError"
                                       value="${param.passwordNew}"/>
                                <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                                <span class="help-block">${passwordNewValidationError}</span>
                            </div>
                            ${passwordNewValidationError = null}
                        </c:if>

                        <!-- New password again -->
                        <c:if test="${passwordNewAgainValidationError == null}">
                            <!-- No validation error -->
                            <div class="form-group">
                                <label for="passwordNewAgain" class="control-label">
                                    <fmt:message
                                            key="myAccountEdit.password.dialog.newAgain"/>
                                </label>
                                <input type="password" name="passwordNewAgain" class="form-control"
                                       id="passwordNewAgain"
                                       placeholder="<fmt:message
                                                            key="myAccountEdit.password.dialog.newAgain.placeholder" />"
                                       value="${param.passwordNewAgain}">
                            </div>
                        </c:if>
                        <c:if test="${passwordNewAgainValidationError != null}">
                            <!-- Validation error -->
                            <div class="form-group has-error has-feedback">
                                <label class="control-label" for="passwordNewAgainError">
                                    <fmt:message key="myAccountEdit.password.dialog.newAgain"/>
                                </label>
                                <input type="password" name="passwordNewAgain" class="form-control"
                                       id="passwordNewAgainError"
                                       value="${param.passwordNewAgain}"/>
                                <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                                <span class="help-block">${passwordNewAgainValidationError}</span>
                            </div>
                            ${passwordNewAgainValidationError = null}
                        </c:if>

                        <input type="hidden" value="password" name="password">
                    </div>

                    <!-- Footer -->
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default pull-right" data-dismiss="modal">
                            <fmt:message key="general.cancel"/>
                        </button>
                        <button type="submit" name="task" value="sendAnnouncement"
                                class="btn btn-primary pull-left">
                            <fmt:message key="myAccountEdit.password.change.submit"/>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {
        if ($('#passwordCurrentError').length || $('#passwordNewError').length || $('#passwordNewAgainError').length) {
            $('#newPasswordDialog').modal("show");
        }
    });
</script>

<!-- Page footer. -->
<%@ include file="footer.jsp" %>