<%@ include file="../header.jsp" %>

<!-- Page content. -->
<div class="container">

    <div class="row">
        <div class="col-md-12">
            <h3><fmt:message key="general.message.error.forbidden"/></h3>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <c:if test="${forbiddenDescription != null}">
                <p>${forbiddenDescription}</p>
            </c:if>
            <c:if test="${forbiddenDescription == null}">
                <p><fmt:message key="general.message.error.forbidden.noDesc"/></p>
            </c:if>
        </div>
    </div>

</div>

<!-- Page footer. -->
<%@ include file="../footer.jsp" %>
