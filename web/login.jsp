<!-- Page header. -->
<%@ include file="header.jsp" %>

<!-- Page content. -->
<div class="container">
    <div class="row">
        <div class="col-md-4"></div>
        <div class="col-md-4">
            <form class="form-signin">
                <h2 class="form-signin-heading"><fmt:message key="login.label.heading"/></h2>
                <label for="inputEmail" class="sr-only"><fmt:message key="login.label.heading"/></label>
                <input type="email" id="inputEmail" class="form-control"
                       placeholder="<fmt:message key="login.label.email"/>" required autofocus>
                <br>
                <label for="inputPassword" class="sr-only"><fmt:message key="login.label.password"/></label>
                <input type="password" id="inputPassword" class="form-control"
                       placeholder="<fmt:message key="login.label.password"/>" required>
                <br>
                <button class="btn btn-lg btn-primary btn-block"
                        type="submit"><fmt:message key="login.button.login"/></button>
            </form>
        </div>
        <div class="col-md-4"></div>
    </div>
</div>

<!-- Page footer. -->
<%@ include file="footer.jsp" %>
