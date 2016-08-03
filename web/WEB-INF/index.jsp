<%@ include file="header.jsp" %>

<!-- Page content. -->
<!-- Main jumbotron for a primary marketing message or call to action -->
<div class="jumbotron">
    <div class="container">
        <div class="row">
            <div class="col-md-3">
                <img src="../img/unuLogo.png" alt="UNU Logo" width="150" height="150">
            </div>
            <div class="col-md-9">
                <h2>University News Ulm</h2>

                <h1>Web Client</h1>
            </div>
        </div>

    </div>
</div>

<div class="container">
    <div class="row">
        <div class="col-md-12">
            <p><fmt:message key="index.welcome"/></p>
        </div>
        <div class="col-md-12">
            <h2><fmt:message key="index.about.title"/></h2>

            <p><fmt:message key="index.about.text1"/></p>

            <p><fmt:message key="index.about.text2"/></p>

            <p><fmt:message key="index.about.text3"/></p>

            <p><fmt:message key="index.about.text4"/></p>

            <p><fmt:message key="index.about.text5"/></p>

            <p><fmt:message key="index.about.text6"/>
                <a href="mailto:ulm.university.news@gmail.com">ulm.university.news@gmail.com</a>!
            </p>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <h2>UNU App</h2>

            <p><fmt:message key="index.text.app"/></p>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <h3>Android</h3>

            <p><fmt:message key="index.text.app.android"/></p>

            <p><a class="btn btn-primary disabled" href="#" role="button">
                <fmt:message key="index.button.app.android"/> &raquo;</a></p>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <h3>Windows Phone</h3>

            <p><fmt:message key="index.text.app.windowsPhone"/></p>

            <p><a class="btn btn-primary disabled" href="#" role="button">
                <fmt:message key="index.button.app.windowsPhone"/> &raquo;</a></p>
        </div>
    </div>
</div>
    <!-- Page footer. -->
    <%@ include file="footer.jsp" %>
