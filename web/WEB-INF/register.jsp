<!-- Page header. -->
<%@ include file="header.jsp" %>

<div class="container">
    <div class="row">
        <div class="col-md-2"></div>

        <div class="col-md-8">
            <h2><fmt:message key="register.heading" /></h2>
            <p><fmt:message key="register.description" /></p>

            <br>

            <form class="form-horizontal" role="form" method="post" action="${base}webclient/register">
                <!-- Name of the new moderator account -->
                <c:if test="${registerNameValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">
                            <fmt:message key="register.form.label.name" />
                        </label>
                        <div class="col-sm-10">
                            <input class="form-control" id="name" type="text" value="<fmt:message
                            key="register.form.label.name.desc" />">
                        </div>
                    </div>
                </c:if>
                <c:if test="${registerNameValidiationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="nameInputError">
                            <fmt:message key="register.form.label.name" />
                        </label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="nameInputError" value="">
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                        </div>
                    </div>
                </c:if>

                <!-- Firstname of the requestor. -->
                <c:if test="${registerFirstNameValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">
                            <fmt:message key="register.form.label.firstname" />
                        </label>
                        <div class="col-sm-10">
                            <input class="form-control" id="firstname" type="text" value="<fmt:message
                            key="register.form.label.firstname.desc" />">
                        </div>
                    </div>
                </c:if>
                <c:if test="${registerFirstNameValidiationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="firstnameInputError">
                            <fmt:message key="register.form.label.firstname" />
                        </label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="firstnameInputError" value="">
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                        </div>
                    </div>
                </c:if>

                <!-- Lastname of the requestor. -->
                <c:if test="${registerLastNameValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">
                            <fmt:message key="register.form.label.lastname" />
                        </label>
                        <div class="col-sm-10">
                            <input class="form-control" id="lastname" type="text" value="<fmt:message
                            key="register.form.label.lastname.desc" />">
                        </div>
                    </div>
                </c:if>
                <c:if test="${registerLastNameValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="lastnameInputError">
                            <fmt:message key="register.form.label.lastname" />
                        </label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="lastnameInputError" value="">
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                        </div>
                    </div>
                </c:if>

                <!-- Email of the requestor. -->
                <c:if test="${registerEmailValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">
                            <fmt:message key="register.form.label.email" />
                        </label>
                        <div class="col-sm-10">
                            <input class="form-control" id="email" type="email" value="<fmt:message
                            key="register.form.label.email.desc" />">
                        </div>
                    </div>
                </c:if>
                <c:if test="${registerEmailValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="emailInputError">
                            <fmt:message key="register.form.label.email" />
                        </label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="emailInputError" value="">
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                        </div>
                    </div>
                </c:if>

                <!-- Password for the account -->
                <div class="form-group">
                    <label class="col-sm-2 control-label">
                        <fmt:message key="register.form.label.password" />
                    </label>
                    <div class="col-sm-10">
                        <input class="form-control" id="password" type="password" value="<fmt:message
                            key="register.form.label.password.desc" />">
                    </div>
                </div>

                <!-- Re-enter password -->
                <c:if test="${registerPasswordValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">
                            <fmt:message key="register.form.label.reenterpassword" />
                        </label>
                        <div class="col-sm-10">
                            <input class="form-control" id="reEnteredPassword" type="password" value="<fmt:message
                            key="register.form.label.reenterpassword.desc" />">
                        </div>
                    </div>
                </c:if>
                <c:if test="${registerPasswordValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="passwordInputError">
                            <fmt:message key="register.form.label.reenterpassword" />
                        </label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="passwordInputError" value="">
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                        </div>
                    </div>
                </c:if>

                <!-- Motivation -->
                <c:if test="${registerMotivationValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">
                            <fmt:message key="register.form.label.motivation" />
                        </label>
                        <div class="col-sm-10">
                            <input class="form-control" id="motivation" type="text" value="<fmt:message
                            key="register.form.label.motivation.desc" />">
                        </div>
                    </div>
                </c:if>
                <c:if test="${registerMotivationValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="motivationInputError">
                            <fmt:message key="register.form.label.motivation" />
                        </label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="motivationInputError" value="">
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
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