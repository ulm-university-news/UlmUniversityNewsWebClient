<%@ include file="../header.jsp" %>

<!-- Page content. -->
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <h3><fmt:message key="general.message.error.notFound"/></h3>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <p><fmt:message key="general.message.error.notFound.desc"/></p>
        </div>
    </div>

    <br>
    <br>

    <div class="row">
        <!-- Dummy col -->
        <div class="col-md-1"></div>

        <div class="col-md-10">
            <img src="${base}../img/error-1349562_1280.png" class="img-rounded img-responsive" alt="404 not found">
        </div>

        <!-- Dummy col -->
        <div class="col-md-1"></div>
    </div>

</div>

<!-- Page footer. -->
<%@ include file="../footer.jsp" %>
