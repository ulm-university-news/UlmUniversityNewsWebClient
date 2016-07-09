<!-- Page header. -->
<%@include file="header.jsp" %>

<c:set var="enteredChannelData" value="${enteredChannelData}" scope="page"/>

<c:set var="typeLecture" value="LECTURE" scope="page"/>
<c:set var="typeEvent" value="EVENT" scope="page"/>
<c:set var="typeSports" value="SPORTS" scope="page"/>
<c:set var="typeStudentGroup" value="STUDENT_GROUP" scope="page"/>
<c:set var="typeOther" value="OTHER" scope="page"/>

<c:set var="facultyComputerScience" value="ENGINEERING_COMPUTER_SCIENCE_PSYCHOLOGY" scope="page"/>
<c:set var="facultyEconomics" value="MATHEMATICS_ECONOMICS" scope="page"/>
<c:set var="facultyMedicine" value="MEDICINES" scope="page"/>
<c:set var="facultyNaturalSciences" value="NATURAL_SCIENCES" scope="page"/>

<div class="container">
    <div class="row">
        <div class="col-md-12">
            <h3><fmt:message key="createChannel.title"/></h3>

            <p><fmt:message key="createChannel.info"/></p>
            <br>
        </div>
    </div>

    <!-- Error, warnings and information alerts. -->
    <c:if test="${createChannelFailed != null}">
        <div class="row">
            <div class="col-md-12">
                <div class="alert alert-danger" role="alert">
                    <strong><fmt:message key="general.alert.failure"/></strong>

                    <p>${createChannelFailed}</p>
                </div>
            </div>
        </div>
    </c:if>
    <c:if test="${param.successful != null}">
        <div class="row">
            <div class="col-md-12">
                <div class="alert alert-success" role="alert">
                    <strong><fmt:message key="general.alert.success"/></strong>

                    <p><fmt:message key="general.alert.success.info"/></p>
                </div>
            </div>
        </div>
    </c:if>

    <div class="row">
        <!-- Channel data - dummy col. -->
        <div class="col-md-2"></div>

        <!-- Column with data fields.-->
        <div class="col-md-8">

            <!-- Input form for channel data. -->
            <form class="form-horizontal" role="form" method="post" action="${base}webclient/createChannel"
                  accept-charset="UTF-8">

                <!-- Name of the channel. -->
                <c:if test="${channelNameValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="channelName">
                            <fmt:message key="myChannel.panel.channelName"/>
                        </label>

                        <div class="col-sm-10">
                            <input class="form-control" name="channelName" id="channelName" type="text"
                                   value="${enteredChannelData != null ? enteredChannelData.getName() : ''}">
                        </div>
                    </div>
                </c:if>
                <c:if test="${channelNameValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="nameInputError">
                            <fmt:message key="myChannel.panel.channelName"/>
                        </label>

                        <div class="col-sm-10">
                            <input type="text" name="channelName" class="form-control" id="nameInputError"
                                   value="${enteredChannelData != null ? enteredChannelData.getName() : ''}">
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                            <span class="help-block">${channelNameValidationError}</span>
                        </div>
                    </div>
                </c:if>

                <!-- Type of the channel. -->
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="channelType">
                        <fmt:message key="myChannel.panel.channelType"/>
                    </label>

                    <div class="col-sm-10">
                        <select class="form-control" name="channelType" id="channelType">
                            <c:choose>
                                <c:when test="${enteredChannelData != null ?
                                enteredChannelData.getType() == typeLecture : false}">
                                    <option value="${typeLecture}" selected>
                                        <fmt:message key="general.channelType.lecture"/>
                                    </option>
                                </c:when>
                                <c:when test="${enteredChannelData != null ?
                                enteredChannelData.getType() == typeEvent : false}">
                                    <option value="${typeEvent}" selected>
                                        <fmt:message key="general.channelType.event"/>
                                    </option>
                                </c:when>
                                <c:when test="${enteredChannelData != null ?
                                enteredChannelData.getType() == typeSports : false}">
                                    <option value="${typeSports}" selected>
                                        <fmt:message key="general.channelType.sports"/>
                                    </option>
                                </c:when>
                                <c:when test="${enteredChannelData != null ?
                                enteredChannelData.getType() == typeStudentGroup : false}">
                                    <option value="${typeStudentGroup}" selected>
                                        <fmt:message key="general.channelType.studentGroup"/>
                                    </option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${typeOther}" selected>
                                        <fmt:message key="general.channelType.other"/>
                                    </option>
                                </c:otherwise>
                            </c:choose>
                                <c:if test="${enteredChannelData != null ?
                                enteredChannelData.getType() != typeLecture : true}">
                                    <option value="${typeLecture}">
                                        <fmt:message key="general.channelType.lecture"/>
                                    </option>
                                </c:if>
                                <c:if test="${enteredChannelData != null ?
                                enteredChannelData.getType() != typeEvent : true}">
                                    <option value="${typeEvent}">
                                        <fmt:message key="general.channelType.event"/>
                                    </option>
                                </c:if>
                                <c:if test="${enteredChannelData != null ?
                                enteredChannelData.getType() != typeSports : true}">
                                    <option value="${typeSports}">
                                        <fmt:message key="general.channelType.sports"/>
                                    </option>
                                </c:if>
                                <c:if test="${enteredChannelData != null ?
                                enteredChannelData.getType() != typeStudentGroup : true}">
                                    <option value="${typeStudentGroup}">
                                        <fmt:message key="general.channelType.studentGroup"/>
                                    </option>
                                </c:if>
                                <c:if test="${enteredChannelData != null ?
                                enteredChannelData.getType() != typeOther : false}">
                                    <option value="${typeOther}">
                                        <fmt:message key="general.channelType.other"/>
                                    </option>
                                </c:if>
                        </select>
                    </div>
                </div>

                <!-- Description. -->
                <c:if test="${descriptionValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="description">
                            <fmt:message key="general.description"/>
                        </label>

                        <div class="col-sm-10">
                            <textarea class="form-control" name="description" id="description" rows="4"
                                      style="resize: none;">${enteredChannelData != null ?
                                      enteredChannelData.getDescription() : ''}</textarea>
                        </div>
                    </div>
                </c:if>
                <c:if test="${descriptionValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="descriptionInputError">
                            <fmt:message key="general.description"/>
                        </label>

                        <div class="col-sm-10">
                            <textarea name="description" class="form-control" id="descriptionInputError" rows="4"
                                      style="resize: none;">${enteredChannelData != null ?
                                    enteredChannelData.getDescription() : ''}</textarea>
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                            <span class="help-block">${descriptionValidationError}</span>
                        </div>
                    </div>
                </c:if>

                <!-- Term -->
                <c:if test="${termValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="termPicker">
                            <fmt:message key="general.term"/>
                        </label>
                        <label class="sr-only" for="yearPicker">
                            <fmt:message key="myChannel.panel.term.year" />
                        </label>
                        <div class="col-sm-10">
                            <div class="row">
                                <div class="col-sm-6">
                                    <select name="termPicker" class="form-control" id="termPicker">
                                        <c:if test="${enteredChannelData != null}">
                                            <c:if test="${fn:startsWith(enteredChannelData.getTerm(), 'S')}">
                                                <option value="summer" selected>
                                                    <fmt:message key="general.term.summer"/>
                                                </option>
                                                <option value="winter">
                                                    <fmt:message key="general.term.winter"/>
                                                </option>
                                            </c:if>
                                            <c:if test="${fn:startsWith(enteredChannelData.getTerm(), 'W')}">
                                                <option value="summer">
                                                    <fmt:message key="general.term.summer"/>
                                                </option>
                                                <option value="winter" selected>
                                                    <fmt:message key="general.term.winter"/>
                                                </option>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${enteredChannelData == null}">
                                            <option value="summer" selected>
                                                <fmt:message key="general.term.summer"/>
                                            </option>
                                            <option value="winter">
                                                <fmt:message key="general.term.winter"/>
                                            </option>
                                        </c:if>
                                    </select>
                                </div>
                                <div class="col-sm-6">
                                    <!-- Hack! -->
                                    <c:if test="${enteredChannelData != null}">
                                        <input type="hidden" id="hiddenYearPickerValue"
                                               value="${fn:substring(enteredChannelData.getTerm(), 1, 6)}"/>
                                    </c:if>
                                    <select name="yearPicker" class="form-control" id="yearPicker">
                                        <!-- filled with jquery -->
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
                <c:if test="${termValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="termPickerInputError">
                            <fmt:message key="general.term"/>
                        </label>
                        <label class="sr-only" for="yearPickerError">
                            <fmt:message key="myChannel.panel.term.year" />
                        </label>
                        <div class="col-sm-10">
                            <div class="row">
                                <div class="col-sm-6">
                                    <select name="termPicker" class="form-control" id="termPickerInputError">
                                        <c:if test="${enteredChannelData != null}">
                                            <c:if test="${fn:startsWith(enteredChannelData.getTerm(), 'S')}">
                                                <option value="summer" selected>
                                                    <fmt:message key="general.term.summer"/>
                                                </option>
                                                <option value="winter">
                                                    <fmt:message key="general.term.winter"/>
                                                </option>
                                            </c:if>
                                            <c:if test="${fn:startsWith(enteredChannelData.getTerm(), 'W')}">
                                                <option value="summer">
                                                    <fmt:message key="general.term.summer"/>
                                                </option>
                                                <option value="winter" selected>
                                                    <fmt:message key="general.term.winter"/>
                                                </option>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${enteredChannelData == null}">
                                            <option value="summer" selected>
                                                <fmt:message key="general.term.summer"/>
                                            </option>
                                            <option value="winter">
                                                <fmt:message key="general.term.winter"/>
                                            </option>
                                        </c:if>
                                    </select>
                                </div>
                                <div class="col-sm-6">
                                    <!-- Hack! -->
                                    <c:if test="${enteredChannelData != null}">
                                        <input type="hidden" id="hiddenYearPickerInputErrorValue"
                                               value="${fn:substring(enteredChannelData.getTerm(), 1, 6)}"/>
                                    </c:if>
                                    <select name="yearPicker" class="form-control" id="yearPickerError">
                                        <!-- filled with jquery -->
                                    </select>
                                </div>
                            </div>
                            <span class="help-block">${termValidationError}</span>
                        </div>
                    </div>
                </c:if>

                <!-- Locations. -->
                <c:if test="${locationsValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="locations">
                            <fmt:message key="myChannel.panel.locations"/>
                        </label>

                        <div class="col-sm-10">
                            <textarea class="form-control" name="locations" id="locations" rows="2"
                                      style="resize:none;">${enteredChannelData != null ?
                                      enteredChannelData.getLocations() : ''}</textarea>
                        </div>
                    </div>
                </c:if>
                <c:if test="${locationsValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="locationsInputError">
                            <fmt:message key="myChannel.panel.channelName"/>
                        </label>

                        <div class="col-sm-10">
                            <textarea name="locations" class="form-control" id="locationsInputError" rows="2"
                                      style="resize: none">${enteredChannelData != null ?
                                    enteredChannelData.getLocations() : ''}</textarea>
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                            <span class="help-block">${locationsValidationError}</span>
                        </div>
                    </div>
                </c:if>

                <!-- Dates. -->
                <c:if test="${datesValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="dates">
                            <fmt:message key="myChannel.panel.dates"/>
                        </label>

                        <div class="col-sm-10">
                            <textarea class="form-control" name="dates" id="dates" rows="2"
                                      style="resize:none;">${enteredChannelData != null ?
                                      enteredChannelData.getDates() : ''}</textarea>
                        </div>
                    </div>
                </c:if>
                <c:if test="${datesValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="datesInputError">
                            <fmt:message key="myChannel.panel.dates"/>
                        </label>

                        <div class="col-sm-10">
                            <textarea name="dates" class="form-control" id="datesInputError"
                                      style="resize: none;">${enteredChannelData != null ?
                                    enteredChannelData.getDates() : ''}</textarea>
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                            <span class="help-block">${datesValidationError}</span>
                        </div>
                    </div>
                </c:if>

                <!-- Contact. -->
                <c:if test="${contactsValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="contact">
                            <fmt:message key="myChannel.panel.contactInformation"/>
                        </label>

                        <div class="col-sm-10">
                            <textarea class="form-control" name="contacts" id="contact" rows="2"
                                      style="resize:none;">${enteredChannelData != null ?
                                       enteredChannelData.getContacts() : ''}</textarea>
                        </div>
                    </div>
                </c:if>
                <c:if test="${contactsValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="contactInputError">
                            <fmt:message key="myChannel.panel.contactInformation"/>
                        </label>

                        <div class="col-sm-10">
                            <textarea name="contacts" class="form-control" id="contactInputError" rows="2"
                                      style="resize: none">${enteredChannelData != null ?
                                    enteredChannelData.getContacts() : ''}</textarea>
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                            <span class="help-block">${contactsValidationError}</span>
                        </div>
                    </div>
                </c:if>

                <!-- Website -->
                <c:if test="${websiteValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="website">
                            <fmt:message key="myChannel.panel.website"/>
                        </label>

                        <div class="col-sm-10">
                            <textarea class="form-control" name="website" id="website" rows="2"
                                      style="resize:none;">${enteredChannelData != null ?
                                      enteredChannelData.getWebsite() : ''}</textarea>
                        </div>
                    </div>
                </c:if>
                <c:if test="${websiteValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="websiteInputError">
                            <fmt:message key="myChannel.panel.dates"/>
                        </label>

                        <div class="col-sm-10">
                            <textarea name="website" class="form-control" id="websiteInputError" rows="2"
                                      style="resize: none">${enteredChannelData != null ?
                                    enteredChannelData.getWebsite() : ''}</textarea>
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                            <span class="help-block">${websiteValidationError}</span>
                        </div>
                    </div>
                </c:if>

                <!-- Lecture related fields. -->
                <div id="lectureRelatedFields">

                    <!-- Faculty -->
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="faculty">
                            <fmt:message key="myChannel.panel.lecture.faculty"/>
                        </label>

                        <div class="col-sm-10">
                            <select class="form-control" name="faculty" id="faculty">
                                <c:if test="${enteredChannelData != null}">
                                    <c:choose>
                                        <c:when test="${enteredChannelData.getType() == typeLecture &&
                                        enteredChannelData.getFaculty() == facultyComputerScience}">
                                            <option value="${facultyComputerScience}" selected>
                                                <fmt:message key="general.faculty.computerScienceAndEngineering"/>
                                            </option>
                                        </c:when>
                                        <c:when test="${enteredChannelData.getType() == typeLecture &&
                                        enteredChannelData.getFaculty() == facultyEconomics}">
                                            <option value="${facultyEconomics}" selected>
                                                <fmt:message key="general.faculty.mathematicsAndEconomics"/>
                                            </option>
                                        </c:when>
                                        <c:when test="${enteredChannelData.getType() == typeLecture &&
                                        enteredChannelData.getFaculty() == facultyMedicine}">
                                            <option value="${facultyMedicine}" selected>
                                                <fmt:message key="general.faculty.medicines"/>
                                            </option>
                                        </c:when>
                                        <c:when test="${enteredChannelData.getType() == typeLecture &&
                                        enteredChannelData.getFaculty() == facultyNaturalSciences}">
                                            <option value="${facultyNaturalSciences}" selected>
                                                <fmt:message key="general.faculty.naturalSciences"/>
                                            </option>
                                        </c:when>
                                    </c:choose>
                                    <c:if test="${enteredChannelData.getType() == typeLecture &&
                                    enteredChannelData.getFaculty() != facultyComputerScience}">
                                        <option value="${facultyComputerScience}">
                                            <fmt:message key="general.faculty.computerScienceAndEngineering"/>
                                        </option>
                                    </c:if>
                                    <c:if test="${enteredChannelData.getType() == typeLecture &&
                                    enteredChannelData.getFaculty() != facultyEconomics}">
                                        <option value="${facultyEconomics}">
                                            <fmt:message key="general.faculty.mathematicsAndEconomics"/>
                                        </option>
                                    </c:if>
                                    <c:if test="${enteredChannelData.getType() == typeLecture &&
                                    enteredChannelData.getFaculty() != facultyMedicine}">
                                        <option value="${facultyMedicine}">
                                            <fmt:message key="general.faculty.medicines"/>
                                        </option>
                                    </c:if>
                                    <c:if test="${enteredChannelData.getType() == typeLecture &&
                                    enteredChannelData.getFaculty() != facultyNaturalSciences}">
                                        <option value="${facultyNaturalSciences}">
                                            <fmt:message key="general.faculty.naturalSciences"/>
                                        </option>
                                    </c:if>
                                </c:if>
                                <c:if test="${enteredChannelData == null}">
                                    <option value="${facultyComputerScience}" selected>
                                        <fmt:message key="general.faculty.computerScienceAndEngineering"/>
                                    </option>
                                    <option value="${facultyEconomics}">
                                        <fmt:message key="general.faculty.mathematicsAndEconomics"/>
                                    </option>
                                    <option value="${facultyMedicine}">
                                        <fmt:message key="general.faculty.medicines"/>
                                    </option>
                                    <option value="${facultyNaturalSciences}">
                                        <fmt:message key="general.faculty.naturalSciences"/>
                                    </option>
                                </c:if>
                            </select>
                        </div>
                    </div>

                    <!-- Lecturer -->
                    <c:if test="${lecturerValidationError == null}">
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="lecturer">
                                <fmt:message key="myChannel.panel.lecture.lecturer"/>
                            </label>

                            <div class="col-sm-10">
                                <input class="form-control" name="lecturer" id="lecturer" type="text"
                                       value="${enteredChannelData != null &&
                                       enteredChannelData.getType() == typeLecture ?
                                       enteredChannelData.getLecturer() : ''}">
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${lecturerValidationError != null}">
                        <div class="form-group has-error has-feedback">
                            <label class="col-sm-2 control-label" for="lecturerInputError">
                                <fmt:message key="myChannel.panel.lecture.lecturer"/>
                            </label>

                            <div class="col-sm-10">
                                <input type="text" name="lecturer" class="form-control" id="lecturerInputError"
                                       value="${enteredChannelData != null &&
                                       enteredChannelData.getType() == typeLecture ?
                                       enteredChannelData.getLecturer() : ''}">
                                <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                                <span class="help-block">${lecturerValidationError}</span>
                            </div>
                        </div>
                    </c:if>

                    <!-- Assistant -->
                    <c:if test="${assistantValidationError == null}">
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="assistant">
                                <fmt:message key="myChannel.panel.lecture.assistant"/>
                            </label>

                            <div class="col-sm-10">
                                <input class="form-control" name="assistant" id="assistant" type="text"
                                       value="${enteredChannelData != null &&
                                        enteredChannelData.getType() == typeLecture ?
                                        enteredChannelData.getAssistant() : ''}">
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${assistantValidationError != null}">
                        <div class="form-group has-error has-feedback">
                            <label class="col-sm-2 control-label" for="assistantInputError">
                                <fmt:message key="myChannel.panel.lecture.assistant"/>
                            </label>

                            <div class="col-sm-10">
                                <input type="text" name="assistant" class="form-control" id="assistantInputError"
                                       value="${enteredChannelData != null &&
                                        enteredChannelData.getType() == typeLecture ?
                                        enteredChannelData.getAssistant() : ''}">
                                <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                                <span class="help-block">${assistantValidationError}</span>
                            </div>
                        </div>
                    </c:if>

                    <!-- Start date of lecture. -->
                    <c:if test="${startDateValidationError == null}">
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="startDate">
                                <fmt:message key="myChannel.panel.lecture.startDate"/>
                            </label>

                            <div class="col-sm-10">
                                <input class="form-control" name="startDate" id="startDate" type="text"
                                       value="${enteredChannelData != null &&
                                       enteredChannelData.getType() == typeLecture ?
                                       enteredChannelData.getStartDate() : ''}">
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${startDateValidationError != null}">
                        <div class="form-group has-error has-feedback">
                            <label class="col-sm-2 control-label" for="startDateInputError">
                                <fmt:message key="myChannel.panel.lecture.startDate"/>
                            </label>

                            <div class="col-sm-10">
                                <input type="text" name="startDate" class="form-control" id="startDateInputError"
                                       value="${enteredChannelData != null &&
                                       enteredChannelData.getType() == typeLecture ?
                                       enteredChannelData.getStartDate() : ''}">
                                <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                                <span class="help-block">${startDateValidationError}</span>
                            </div>
                        </div>
                    </c:if>

                    <!-- End date of lecture. -->
                    <c:if test="${endDateValidationError == null}">
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="endDate">
                                <fmt:message key="myChannel.panel.lecture.endDate"/>
                            </label>

                            <div class="col-sm-10">
                                <input class="form-control" name="endDate" id="endDate" type="text"
                                       value="${enteredChannelData != null &&
                                       enteredChannelData.getType() == typeLecture ?
                                       enteredChannelData.getEndDate() : ''}">
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${endDateValidationError != null}">
                        <div class="form-group has-error has-feedback">
                            <label class="col-sm-2 control-label" for="endDateInputError">
                                <fmt:message key="myChannel.panel.lecture.startDate"/>
                            </label>

                            <div class="col-sm-10">
                                <input type="text" name="endDate" class="form-control" id="endDateInputError"
                                       value="${enteredChannelData != null &&
                                       enteredChannelData.getType() == typeLecture ?
                                       enteredChannelData.getEndDate() : ''}">
                                <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                                <span class="help-block">${endDateValidationError}</span>
                            </div>
                        </div>
                    </c:if>

                </div>

                <!-- Event based fields -->
                <div id="eventRelatedFields">

                    <!-- Cost -->
                    <c:if test="${eventCostValidationError == null}">
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="eventCost">
                                <fmt:message key="myChannel.panel.event.cost"/>
                            </label>

                            <div class="col-sm-10">
                                <input class="form-control" name="eventCost" id="eventCost" type="text"
                                       value="${enteredChannelData != null &&
                                        enteredChannelData.getType() == typeEvent ?
                                        enteredChannelData.getCost() : ''}">
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${eventCostValidationError != null}">
                        <div class="form-group has-error has-feedback">
                            <label class="col-sm-2 control-label" for="eventCostInputError">
                                <fmt:message key="myChannel.panel.event.cost"/>
                            </label>

                            <div class="col-sm-10">
                                <input type="text" name="eventCost" class="form-control" id="eventCostInputError"
                                       value="${enteredChannelData != null &&
                                        enteredChannelData.getType() == typeEvent ?
                                        enteredChannelData.getCost() : ''}">
                                <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                                <span class="help-block">${eventCostValidationError}</span>
                            </div>
                        </div>
                    </c:if>

                    <!-- Organizer -->
                    <c:if test="${organizerValidationError == null}">
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="organizer">
                                <fmt:message key="myChannel.panel.event.organizer"/>
                            </label>

                            <div class="col-sm-10">
                                <input class="form-control" name="organizer" id="organizer" type="text"
                                       value="${enteredChannelData != null &&
                                       enteredChannelData.getType() == typeEvent ?
                                       enteredChannelData.getOrganizer() : ''}">
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${organizerValidationError != null}">
                        <div class="form-group has-error has-feedback">
                            <label class="col-sm-2 control-label" for="organizerInputError">
                                <fmt:message key="myChannel.panel.event.organizer"/>
                            </label>

                            <div class="col-sm-10">
                                <input type="text" name="organizer" class="form-control" id="organizerInputError"
                                       value="${enteredChannelData != null &&
                                       enteredChannelData.getType() == typeEvent ?
                                       enteredChannelData.getOrganizer() : ''}">
                                <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                                <span class="help-block">${organizerValidationError}</span>
                            </div>
                        </div>
                    </c:if>

                </div>

                <!-- sports based fields -->
                <div id="sportsRelatedFields">

                    <!-- Cost -->
                    <c:if test="${sportsCostValidationError == null}">
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="sportsCost">
                                <fmt:message key="myChannel.panel.sports.cost"/>
                            </label>

                            <div class="col-sm-10">
                                <input class="form-control" name="sportsCost" id="sportsCost" type="text"
                                       value="${enteredChannelData != null &&
                                        enteredChannelData.getType() == typeSports ?
                                        enteredChannelData.getCost() : ''}">
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${sportsCostValidationError != null}">
                        <div class="form-group has-error has-feedback">
                            <label class="col-sm-2 control-label" for="sportsCostInputError">
                                <fmt:message key="myChannel.panel.sports.cost"/>
                            </label>

                            <div class="col-sm-10">
                                <input type="text" name="sportsCost" class="form-control" id="sportsCostInputError"
                                       value="${enteredChannelData != null &&
                                        enteredChannelData.getType() == typeSports ?
                                        enteredChannelData.getCost() : ''}">
                                <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                                <span class="help-block">${sportsCostValidationError}</span>
                            </div>
                        </div>
                    </c:if>

                    <!-- Number of participants -->
                    <c:if test="${numberOfParticipantsValidationError == null}">
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="numberOfParticipants">
                                <fmt:message key="myChannel.panel.sports.numberOfParticipants"/>
                            </label>

                            <div class="col-sm-10">
                                <input class="form-control" name="numberOfParticipants" id="numberOfParticipants"
                                       type="text"
                                       value="${enteredChannelData != null &&
                                       enteredChannelData.getType() == typeSports ?
                                       enteredChannelData.getNumberOfParticipants() : ''}">
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${numberOfParticipantsValidationError != null}">
                        <div class="form-group has-error has-feedback">
                            <label class="col-sm-2 control-label" for="numberOfParticipantsInputError">
                                <fmt:message key="myChannel.panel.sports.numberOfParticipants"/>
                            </label>

                            <div class="col-sm-10">
                                <input type="text" name="numberOfParticipants" class="form-control"
                                       id="numberOfParticipantsInputError"
                                       value="${enteredChannelData != null &&
                                       enteredChannelData.getType() == typeSports ?
                                       enteredChannelData.getNumberOfParticipants() : ''}">
                                <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                                <span class="help-block">${numberOfParticipantsValidationError}</span>
                            </div>
                        </div>
                    </c:if>

                </div>

                <br>
                <button class="btn btn-lg btn-primary btn-block"
                        type="submit" name="task" value="createChannel">
                    <fmt:message key="createChannel.submitButton.label"/>
                </button>
                <br>

            </form>
            <!-- End of form block. -->

        </div>

        <div class="col-md-2"></div>
    </div>
</div>

<script>
    $(document).ready(function () {
        // Fill year pickers.
        for (var i = new Date().getFullYear() + 50; i > 1950; i--) {

            var hiddenYearPicker = $('#hiddenYearPickerValue');
            var hiddenYearPickerError = $('#hiddenYearPickerInputErrorValue');

            if (hiddenYearPicker.length) {
                if (hiddenYearPicker.val() == i) {
                    $('#yearPicker').append($('<option selected />').val(i).html(i));
                }
                else {
                    $('#yearPicker').append($('<option />').val(i).html(i));
                }
            }
            else {
                if (new Date().getFullYear() == i) {
                    $('#yearPicker').append($('<option selected />').val(i).html(i));
                }
                else {
                    $('#yearPicker').append($('<option />').val(i).html(i));
                }
            }

            if (hiddenYearPickerError.length) {
                if (hiddenYearPickerError.val() == i) {
                    $('#yearPickerError').append($('<option selected />').val(i).html(i));
                }
                else {
                    $('#yearPickerError').append($('<option />').val(i).html(i));
                }
            }
            else {
                if (new Date().getFullYear() == i) {
                    $('#yearPickerError').append($('<option selected />').val(i).html(i));
                }
                else {
                    $('#yearPickerError').append($('<option />').val(i).html(i));
                }
            }
        }

        // Display or hide subclass data fields.
        var channelType = $('#channelType');
        var lectureFields = $('#lectureRelatedFields');
        var eventFields = $('#eventRelatedFields');
        var sportsFields = $('#sportsRelatedFields');

        lectureFields.hide();
        eventFields.hide();
        sportsFields.hide();
        if (channelType != null) {
            if (channelType.val() == 'LECTURE') {
                lectureFields.show();
            }
            else if (channelType.val() == 'EVENT') {
                eventFields.show();
            }
            else if (channelType.val() == 'SPORTS') {
                sportsFields.show();
            }
        }
    });

    // Change div visibility depending on selected type.
    $('#channelType').change(function(){
        // Display or hide subclass data fields.
        var channelType = $('#channelType');
        var lectureFields = $('#lectureRelatedFields');
        var eventFields = $('#eventRelatedFields');
        var sportsFields = $('#sportsRelatedFields');

        lectureFields.hide();
        eventFields.hide();
        sportsFields.hide();
        if (channelType != null) {
            if (channelType.val() == 'LECTURE') {
                lectureFields.show();
            }
            else if (channelType.val() == 'EVENT') {
                eventFields.show();
            }
            else if (channelType.val() == 'SPORTS') {
                sportsFields.show();
            }
        }
    });
</script>

<!-- Page footer. -->
<%@ include file="footer.jsp" %>