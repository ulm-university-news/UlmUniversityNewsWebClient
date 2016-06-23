<!-- Page header. -->
<%@include file="header.jsp"%>
<c:set var="channelId" value="${param.channelId}" scope="session"/>
<c:set var="typeLecture" value="LECTURE" scope="page" />
<c:set var="typeEvent" value="EVENT" scope="page" />
<c:set var="typeSports" value="SPORTS" scope="page" />
<c:set var="typeStudentGroup" value="STUDENT_GROUP" scope="page" />
<c:set var="typeOther" value="OTHER" scope="page" />

<style>
    .heightRestrictedList {
        min-height: 100px;
        max-height: 60vh;
        position: relative;
    }

    .scrollEnabled {
        min-height: 100px;
        max-height: 60vh;
        overflow-y: auto;
    }

    .scrollTestEnabled {
        position: absolute;
        height: 100%;
        overflow-y: auto;
    }
</style>

<!-- Page content -->
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <h3><fmt:message key="myChannels.title" /></h3>
            <p><fmt:message key="myChannels.info" /> </p>
            <br>
        </div>
    </div>

    <!-- TODO error messages and warnings -->
    <c:if test="${myChannelsLoadingFailure != null}">
        <div class="row">
            <div class="col-md-12">
                <div class="alert alert-danger" role="alert">${applicationLoadError}</div>
            </div>
        </div>
        ${myChannelsLoadingFailure = null}
    </c:if>

    <div class="row">
        <div class="col-md-3">
            <div class="list-group">
                <div class="list-group-item active">
                    <h4 class="list-group-item-heading">
                        <fmt:message key="myChannels.list.heading"/>
                    </h4>
                </div>
                <!-- Make content scrollable. -->
                <div class="scrollEnabled">
                    <c:choose>
                        <c:when test="${myChannels != null && !myChannels.isEmpty()}">
                            <c:forEach items="${myChannels}" var="channel">
                                <c:if test="${channelId == null && channel != null}">
                                    <!-- First item will be set as the active one. -->
                                    <c:set var="channelId" value="${channel.getId()}"/>
                                </c:if>
                                <c:choose>
                                    <c:when test="${channelId != null && channelId == channel.getId()}">
                                        <a href="?channelId=${channel.getId()}"
                                           class="list-group-item list-group-item-info">
                                                ${channel.getName()}
                                        </a>
                                        <c:set var="currentChannel" value="${channel}" scope="session"/>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="?channelId=${channel.getId()}" class="list-group-item">
                                                ${channel.getName()}
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="list-group-item">
                                <fmt:message key="myChannels.none"/>
                            </div>
                            ${currentChannel = null}
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        <div class="col-md-9">
            <div class="panel panel-info">
                <div class="panel-heading">
                    <c:choose>
                        <c:when test="${currentChannel != null}">
                            <h4>${currentChannel.getName()}</h4>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="myChannels.nodata"/>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="panel-body scrollEnabled">
                    <c:choose>
                        <c:when test="${currentChannel != null}">
                            <!-- Type and term -->
                            <div class="row">
                                <div class="col-md-5">
                                    <h4><fmt:message key="myChannel.panel.channelType"/> </h4>
                                    <p>${currentChannel.getTypeString()}</p>
                                </div>
                                <div class="col-md-5">
                                    <h4><fmt:message key="myChannel.panel.term"/> </h4>
                                    <p>${currentChannel.getTerm()}</p>
                                </div>
                            </div>
                            <!-- Locations and dates -->
                            <div class="row">
                                <div class="col-md-5">
                                    <h4><fmt:message key="myChannel.panel.locations"/> </h4>
                                    <p>${currentChannel.getLocations()}</p>
                                </div>
                                <div class="col-md-5">
                                    <h4><fmt:message key="myChannel.panel.dates"/> </h4>
                                    <p>${currentChannel.getDates()}</p>
                                </div>
                            </div>
                            <!-- Contact information and website -->
                            <div class="row">
                                <div class="col-md-5">
                                    <h4><fmt:message key="myChannel.panel.contactInformation"/> </h4>
                                    <p>${currentChannel.getContacts()}</p>
                                </div>
                                <div class="col-md-5">
                                    <h4><fmt:message key="myChannel.panel.website"/> </h4>
                                    <p>${currentChannel.getWebsite()}</p>
                                </div>
                            </div>
                            <!-- Creation date and Modification date -->
                            <div class="row">
                                <div class="col-md-5">
                                    <h4><fmt:message key="myChannel.panel.creationDate"/> </h4>
                                    <p>${currentChannel.getCreationDate()}</p>
                                </div>
                                <div class="col-md-5">
                                    <h4><fmt:message key="myChannel.panel.modificationDate"/> </h4>
                                    <p>${currentChannel.getModificationDate()}</p>
                                </div>
                            </div>
                            <c:if test="${currentChannel.getType() == typeLecture}" >
                                <!-- Additional lecture fields: -->
                                <!-- Lecturer and assistant -->
                                <div class="row">
                                    <div class="col-md-5">
                                        <h4><fmt:message key="myChannel.panel.lecture.lecturer"/> </h4>
                                        <p>${currentChannel.getLecturer()}</p>
                                    </div>
                                    <div class="col-md-5">
                                        <h4><fmt:message key="myChannel.panel.lecture.assistant"/> </h4>
                                        <p>${currentChannel.getAssistant()}</p>
                                    </div>
                                </div>
                                <!-- Lecture start and end date -->
                                <div class="row">
                                    <div class="col-md-5">
                                        <h4><fmt:message key="myChannel.panel.lecture.startDate"/> </h4>
                                        <p>${currentChannel.getStartDate()}</p>
                                    </div>
                                    <div class="col-md-5">
                                        <h4><fmt:message key="myChannel.panel.lecture.endDate"/> </h4>
                                        <p>${currentChannel.getEndDate()}</p>
                                    </div>
                                </div>
                                <!-- Faculty -->
                                <div class="row">
                                    <div class="col-md-10">
                                        <h4><fmt:message key="myChannel.panel.lecture.faculty"/> </h4>
                                        <p>${currentChannel.getFacultyString()}</p>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${currentChannel.getType() == typeEvent}">
                                <!-- Additional event fields: -->
                                <!-- Entree fee (cost) and organizer -->
                                <div class="row">
                                    <div class="col-md-5">
                                        <h4><fmt:message key="myChannel.panel.event.cost"/> </h4>
                                        <p>${currentChannel.getCost()}</p>
                                    </div>
                                    <div class="col-md-5">
                                        <h4><fmt:message key="myChannel.panel.event.organizer"/> </h4>
                                        <p>${currentChannel.getOrganizer()}</p>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${currentChannel.getType() == typeSports}">
                                <!-- Additional sport fields: -->
                                <!-- Cost and number of participants -->
                                <div class="row">
                                    <div class="col-md-5">
                                        <h4><fmt:message key="myChannel.panel.sports.cost"/> </h4>
                                        <p>${currentChannel.getCost()}</p>
                                    </div>
                                    <div class="col-md-5">
                                        <h4><fmt:message key="myChannel.panel.sports.numberOfParticipants"/> </h4>
                                        <p>${currentChannel.getNumberOfParticipants()}</p>
                                    </div>
                                </div>
                            </c:if>
                            <!-- Description -->
                            <div class="row">
                                <div class="col-md-10">
                                    <h4><fmt:message key="myChannel.panel.description"/> </h4>
                                    <p>${currentChannel.getDescription()}</p>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="myChannels.nodata.into" />
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

</div>

<!-- Page footer. -->
<%@ include file="footer.jsp" %>