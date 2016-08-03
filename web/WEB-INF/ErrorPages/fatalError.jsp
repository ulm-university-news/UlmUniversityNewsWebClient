<%@ include file="../header.jsp" %>

<!-- Page content. -->
<div class="container">

  <div class="row">
    <div class="col-md-12">
      <h3><fmt:message key="general.message.error.fatal"/></h3>
    </div>
  </div>

  <div class="row">
    <div class="col-md-12">
      <c:if test="${fatalErrorDescription != null}">
        <p>${fatalErrorDescription}</p>
      </c:if>
      <c:if test="${fatalErrorDescription == null}">
        <p><fmt:message key="general.message.error.fatal.noDesc"/></p>
      </c:if>
    </div>
  </div>

</div> <!-- End of container div -->

<!-- Page footer. -->
<%@ include file="../footer.jsp" %>
