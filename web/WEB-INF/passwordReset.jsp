<!-- Page header. -->
<%@ include file="header.jsp" %>

<!-- Page content. -->
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <h3><fmt:message key="resetPassword.heading"/></h3>

            <p><fmt:message key="resetPassword.description"/></p>
            <br>
        </div>
    </div>

    <!-- Error, warnings and information alerts. -->
    <c:if test="${param.successful}">
        <div class="row">
            <div class="col-md-12">
                <div class="alert alert-success">
                    <strong><fmt:message key="general.alert.success" /></strong>
                    <fmt:message key="resetPassword.info.success" />
                </div>
            </div>
        </div>
    </c:if>
    <c:if test="${param.successful == false}">
        <div class="row">
            <div class="col-md-12">
                <div class="alert alert-danger">
                    <strong><fmt:message key="general.alert.failure" /></strong>
                    <p><fmt:message key="resetPassword.info.failure" /></p>
                    <p>${errorMsg}</p>
                </div>
            </div>
        </div>
    </c:if>

    <div class="row">
        <div class="col-md-1"></div>
        <div class="col-md-10">
            <form class="form-horizontal" method="post" action="${base}webclient/passwordReset">
                <!-- Username for the password reset. -->
                <c:if test="${resetPasswordNameValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">
                            <fmt:message key="moderator.name" />
                        </label>
                        <div class="col-sm-10">
                            <input class="form-control" name="username" id="name" type="text"
                                   placeholder="<fmt:message key="resetPassword.form.label.name.desc" />"
                                   value="${param.username}">
                        </div>
                    </div>
                </c:if>
                <c:if test="${resetPasswordNameValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="nameInputError">
                            <fmt:message key="moderator.name" />
                        </label>
                        <div class="col-sm-10">
                            <input type="text" name="username" class="form-control" id="nameInputError"
                                   value="${param.username}">
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                            <span class="help-block">${resetPasswordNameValidationError}</span>
                        </div>
                    </div>
                </c:if>

                <br>
                <button class="btn btn-primary pull-right"
                        type="submit" name="task"
                        value="reset"><fmt:message key="resetPassword.resetButton.label"/></button>
            </form>
        </div>
        <div class="col-md-1"></div>
    </div>
</div>

<!-- Page footer. -->
<%@ include file="footer.jsp" %>