<%@ include file="header.jsp" %>

<!-- Hashing library. -->
<script src="https://raw.github.com/bitwiseshiftleft/sjcl/master/sjcl.js"></script>

<!-- Page content. -->
<div class="container">
    <div class="row">
        <div class="col-md-4"></div>
        <div class="col-md-4">
            <form class="form-signin" method="post" onsubmit="return encryptPass()" action="${base}webclient/login">
                <h2 class="form-signin-heading"><fmt:message key="login.label.heading"/></h2>
                <label for="inputUsername" class="sr-only"><fmt:message key="login.label.heading"/></label>
                <input type="text" name="username" id="inputUsername" class="form-control"
                       placeholder="<fmt:message key="login.label.username"/>" required autofocus>
                <br>
                <label for="inputPassword" class="sr-only"><fmt:message key="login.label.password"/></label>
                <input type="password" name="password" id="inputPassword" class="form-control"
                       placeholder="<fmt:message key="login.label.password"/>" required>

                <!-- Validation Error -->
                <c:if test="${loginStatusMsg != null}">
                    <br>
                    <p style="color:red;">${loginStatusMsg}</p>
                </c:if>

                <br>
                <button class="btn btn-primary btn-block"
                        type="submit" name="task" value="login"><fmt:message key="login.button.login"/></button>
            </form>
        </div>
        <div class="col-md-4"></div>
    </div>

    <br>

    <div class="row">
        <div class="col-md-4"></div>
        <div class="col-md-4">
            <p>
                <fmt:message key="login.passwordReset.description"/>
                <br>
                <a href="${base}webclient/passwordReset" >
                    <fmt:message key="login.passwordReset.description.link"/></a>
            </p>
        </div>
        <div class="col-md-4"></div>
    </div>

    <div class="row">
        <div class="col-md-4"></div>
        <div class="col-md-4">
            <h2><fmt:message key="login.noaccount.heading" /> </h2>
            <p><fmt:message key="login.noaccount.description"/></p>
            <p><a class="btn btn-primary btn-block" href="${base}webclient/register" role="button">
                <fmt:message key="login.noaccount.button.label"/></a></p>
        </div>
        <div class="col-md-4"></div>
    </div>
</div>

<script>
    function encryptPass() {

        var password = document.getElementById("inputPassword").value;

        var bitArray = sjcl.hash.sha256.hash(password);
        var digest_sha256 = sjcl.codec.hex.fromBits(bitArray);

        document.getElementById("inputPassword").value = digest_sha256;

        return true;
    }
</script>

<!-- Page footer. -->
<%@ include file="footer.jsp" %>
