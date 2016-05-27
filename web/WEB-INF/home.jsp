<%--
  Created by IntelliJ IDEA.
  User: Philipp
  Date: 26.05.2016
  Time: 14:34
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- Base-URL -->
<c:set var="req" value="${pageContext.request}" />
<c:set var="url">${req.requestURL}</c:set>
<c:set var="base" value="${fn:substring(url, 0, fn:length(url) - fn:length(req.requestURI))}${req.contextPath}/" />

<html>
<head>
    <title>University News Ulm - Web Client</title>
    <!-- Hashing library. -->
    <script src="https://raw.github.com/bitwiseshiftleft/sjcl/master/sjcl.js"></script>
</head>
<body>
  <h1>Web - Client</h1>

  <p>Hallo Freunde. Das ist der Homescreen von unserem Web-Client.</p>

    <!-- Login form -->
    <form method="post" onsubmit="return encryptPass()" action="${base}webclient/home">
        <table>
            <tbody>
                <!-- Username -->
                <tr>
                    <th>
                        <label for="username">Nutzername</label>
                    </th>
                    <td>
                        <input id="username" name="username">
                    </td>
                </tr>
                <!-- Password -->
                <tr>
                    <th>
                        <label for="password">Passwort </label>
                    </th>
                    <td>
                        <input type="password" id="password" name="password">
                    </td>
                </tr>
                <!-- Login button -->
                <tr>
                    <td />
                    <td>
                        <button name="task" value="login">Login</button>
                    </td>
                </tr>
            </tbody>
        </table>
    </form>

    <span style="color: red">${loginStatusMsg}</span>
</body>

<script>
    function encryptPass() {
        var password = document.getElementById("password").value;
        var bitArray = sjcl.hash.sha256.hash(password);
        var digest_sha256 = sjcl.codec.hex.fromBits(bitArray);

        document.getElementById("password").value = digest_sha256;

        return true;
    }
</script>

</html>
