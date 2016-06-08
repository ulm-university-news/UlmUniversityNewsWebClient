<!-- Page header. -->
<%@ include file="header.jsp" %>

<div class="container">
    <div class="row">
        <div class="col-md-2"></div>

        <div class="col-md-8">
            <h2><fmt:message key="register.heading" /></h2>
            <p><fmt:message key="register.description" /></p>

            <br>

            <c:if test="${param.successful}">
                <div class="alert alert-success">
                    <strong><fmt:message key="general.alert.success" /></strong>
                    <fmt:message key="register.info.success" />
                </div>
            </c:if>

            <form class="form-horizontal" role="form" method="post" action="${base}webclient/register">
                <!-- Name of the new moderator account -->
                <c:if test="${registerNameValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">
                            <fmt:message key="moderator.name" />
                        </label>
                        <div class="col-sm-10">
                            <input class="form-control" name="username" id="name" type="text"
                                   placeholder="<fmt:message key="register.form.label.name.desc" />"
                                    value="${param.username}">
                        </div>
                    </div>
                </c:if>
                <c:if test="${registerNameValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="nameInputError">
                            <fmt:message key="moderator.name" />
                        </label>
                        <div class="col-sm-10">
                            <input type="text" name="username" class="form-control" id="nameInputError"
                                   value="${param.username}">
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                            <span class="help-block">${registerNameValidationError}</span>
                        </div>
                    </div>
                </c:if>

                <!-- Firstname of the requestor. -->
                <c:if test="${registerFirstNameValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">
                            <fmt:message key="moderator.firstname" />
                        </label>
                        <div class="col-sm-10">
                            <input class="form-control" name="firstname" id="firstname" type="text"
                                   placeholder="<fmt:message key="register.form.label.firstname.desc" />"
                                    value="${param.firstname}">
                        </div>
                    </div>
                </c:if>
                <c:if test="${registerFirstNameValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="firstnameInputError">
                            <fmt:message key="moderator.firstname" />
                        </label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="firstname" id="firstnameInputError"
                                   value="${param.firstname}">
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                            <span class="help-block">${registerFirstNameValidationError}</span>
                        </div>
                    </div>
                </c:if>

                <!-- Lastname of the requestor. -->
                <c:if test="${registerLastNameValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">
                            <fmt:message key="moderator.lastname" />
                        </label>
                        <div class="col-sm-10">
                            <input class="form-control" name="lastname" id="lastname" type="text"
                                   placeholder="<fmt:message key="register.form.label.lastname.desc" />"
                                    value="${param.lastname}">
                        </div>
                    </div>
                </c:if>
                <c:if test="${registerLastNameValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="lastnameInputError">
                            <fmt:message key="moderator.lastname" />
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
                            <fmt:message key="moderator.email" />
                        </label>
                        <div class="col-sm-10">
                            <input class="form-control" name="email" id="email" type="email"
                                   placeholder="<fmt:message key="register.form.label.email.desc" />"
                                   value="${param.email}">
                        </div>
                    </div>
                </c:if>
                <c:if test="${registerEmailValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="emailInputError">
                            <fmt:message key="moderator.email" />
                        </label>
                        <div class="col-sm-10">
                            <input type="email" class="form-control" name="email" id="emailInputError"
                                   value="${param.email}">
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                            <span class="help-block">${registerEmailValidationError}</span>
                        </div>
                    </div>
                </c:if>

                <!-- Password for the account -->
                <div class="form-group">
                    <label class="col-sm-2 control-label">
                        <fmt:message key="register.form.label.password" />
                    </label>
                    <div class="col-sm-10">
                        <input class="form-control" name="password" id="password" type="password"
                               placeholder="<fmt:message key="register.form.label.password.desc" />"
                                value="${param.password}">
                    </div>
                </div>

                <!-- Re-enter password -->
                <c:if test="${registerPasswordValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">
                            <fmt:message key="register.form.label.reenterpassword" />
                        </label>
                        <div class="col-sm-10">
                            <input class="form-control" name="reEnteredPassword" id="reEnteredPassword" type="password"
                                   placeholder="<fmt:message key="register.form.label.reenterpassword.desc" />"
                                    value="${param.reEnteredPassword}">
                        </div>
                    </div>
                </c:if>
                <c:if test="${registerPasswordValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="passwordInputError">
                            <fmt:message key="register.form.label.reenterpassword" />
                        </label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" name="reEnteredPassword" id="passwordInputError"
                                   value="${param.reEnteredPassword}">
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                            <span class="help-block">${registerPasswordValidationError}</span>
                        </div>
                    </div>
                </c:if>

                <!-- Motivation -->
                <c:if test="${registerMotivationValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">
                            <fmt:message key="moderator.motivation" />
                        </label>
                        <div class="col-sm-10">
                            <input class="form-control" name="motivation" id="motivation" type="text"
                                   placeholder="<fmt:message key="register.form.label.motivation.desc" />"
                                   value="${param.motivation}">
                        </div>
                    </div>
                </c:if>
                <c:if test="${registerMotivationValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="motivationInputError">
                            <fmt:message key="moderator.motivation" />
                        </label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="motivation" id="motivationInputError"
                                   value="${param.motivation}">
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                            <span class="help-block">${registerMotivationValidationError}</span>
                        </div>
                    </div>
                </c:if>

                <br>
                <button class="btn btn-lg btn-primary btn-block"
                        type="submit" name="task" value="register"><fmt:message key="register.form.button"/></button>

            </form>
        </div>

        <div class="col-md-2"></div>
    </div>

</div>

<!-- Page footer. -->
<%@ include file="footer.jsp" %>