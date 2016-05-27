<!-- Page header. -->
<%@ include file="header.jsp" %>

<!-- Page content. -->
<!-- Main jumbotron for a primary marketing message or call to action -->
<div class="jumbotron">
    <div class="container">
        <h2>University News Ulm</h2>

        <h1>Web Client</h1>
    </div>
</div>

<div class="container">
    <p><fmt:message key="index.welcome"/></p>
</div>

<div class="container">
    <div class="row">
        <div class="col-md-12">
            <h2>UNU App</h2>

            <p><fmt:message key="index.text.app"/></p>
        </div>
    </div>
    <div class="row">
        <div class="col-md-6">
            <h2>Android</h2>
            <p><fmt:message key="index.text.app.android"/></p>
            <p><a class="btn btn-primary" href="#" role="button">
                <fmt:message key="index.button.app.android"/> &raquo;</a></p>
        </div>
        <div class="col-md-6">
            <h2>Windows Phone</h2>
            <p><fmt:message key="index.text.app.windowsPhone"/></p>
            <p><a class="btn btn-primary" href="#" role="button">
                <fmt:message key="index.button.app.windowsPhone"/> &raquo;</a></p>
        </div>
    </div>

    <!-- Page footer. -->
    <%@ include file="footer.jsp" %>
