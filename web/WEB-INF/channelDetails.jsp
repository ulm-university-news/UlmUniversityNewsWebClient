<!-- Page header. -->
<%@include file="header.jsp" %>

<c:set var="editableChannel" value="${editableChannel}" scope="page" />

<c:set var="typeLecture" value="LECTURE" scope="page" />
<c:set var="typeEvent" value="EVENT" scope="page" />
<c:set var="typeSports" value="SPORTS" scope="page" />
<c:set var="typeStudentGroup" value="STUDENT_GROUP" scope="page" />
<c:set var="typeOther" value="OTHER" scope="page" />

<div class="container">
    <div class="row">
        <div class="col-md-12">
            <h3><fmt:message key="channelDetails.title"/></h3>

            <p><fmt:message key="channelDetails.info"/></p>
            <br>
        </div>
    </div>

    <!-- Error, warnings and information alerts. -->
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
        <!-- Channel data. -->
        <div class="col-md-2" ></div>

        <div class="col-md-8">
            <!-- Input form for channel data. -->
            <form class="form-horizontal" role="form" method="post" action="${base}webclient/channelDetails">

                <!-- Name of the channel. -->
                <c:if test="${channelNameValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">
                            <fmt:message key="myChannel.panel.channelName" />
                        </label>
                        <div class="col-sm-10">
                            <input class="form-control" name="channelName" id="name" type="text"
                                   value="${editableChannel.getName()}">
                        </div>
                    </div>
                </c:if>
                <c:if test="${channelNameValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="nameInputError">
                            <fmt:message key="myChannel.panel.channelName" />
                        </label>
                        <div class="col-sm-10">
                            <input type="text" name="channelName" class="form-control" id="nameInputError"
                                   value="${editableChannel.getName()}">
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                            <span class="help-block">${channelNameValidationError}</span>
                        </div>
                    </div>
                </c:if>

                <!-- Type of the channel. -->
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="channelType">
                        <fmt:message key="myChannel.panel.channelType" />
                    </label>
                    <div class="col-sm-10">
                        <select class="form-control" name="channelType" id="channelType" disabled>
                            <option value="lecture">
                                <fmt:message key="general.channelType.lecture" />
                            </option>
                            <option value="event">
                                <fmt:message key="general.channelType.event" />
                            </option>
                            <option value="sports">
                                <fmt:message key="general.channelType.sports" />
                            </option>
                            <option value="studentGroup">
                                <fmt:message key="general.channelType.studentGroup" />
                            </option>
                            <option value="other">
                                <fmt:message key="general.channelType.other" />
                            </option>
                        </select>
                    </div>
                </div>

                <!-- Description. -->
                <c:if test="${descriptionValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">
                            <fmt:message key="general.description" />
                        </label>
                        <div class="col-sm-10">
                            <textarea class="form-control" name="description" id="description" rows="4"
                                    style="resize: none">
                                    ${param.description}
                            </textarea>
                        </div>
                    </div>
                </c:if>
                <c:if test="${descriptionValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="descriptionInputError">
                            <fmt:message key="general.description" />
                        </label>
                        <div class="col-sm-10">
                            <textarea name="description" class="form-control" id="descriptionInputError"
                                   style="resize: none;">
                                    ${param.description}
                            </textarea>
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

                        <div class="col-sm-10">
                            <div class="row">
                                <div class="col-sm-6">
                                    <select name="termPicker" class="form-control" id="termPicker">
                                        <option value="summer">
                                            <fmt:message key="general.term.summer"/>
                                        </option>
                                        <option value="winter">
                                            <fmt:message key="general.term.winter"/>
                                        </option>
                                    </select>
                                </div>
                                <div class="col-sm-6">
                                    <input type="hidden" id="hiddenYearPickerValue"
                                           value="${fn:substring(editableChannel.getTerm(), 1, 6)}" />
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
                            <fmt:message key="general.description" />
                        </label>
                        <div class="col-sm-10">
                            <div class="row">
                                <div class="col-sm-6">
                                    <select name="termPicker" class="form-control" id="termPickerInputError">
                                        <option value="summer">
                                            <fmt:message key="general.term.summer" />
                                        </option>
                                        <option value="winter">
                                            <fmt:message key="general.term.winter" />
                                        </option>
                                    </select>
                                </div>
                                <div class="col-sm-6">
                                    <input type="hidden" id="hiddenYearPickerInputErrorValue"
                                           value="${fn:substring(editableChannel.getTerm(), 1, 6)}" />
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
                            <fmt:message key="myChannel.panel.locations" />
                        </label>
                        <div class="col-sm-10">
                            <input class="form-control" name="locations" id="locations" type="text"
                                   value="${param.locations}">
                        </div>
                    </div>
                </c:if>
                <c:if test="${locationsValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="locationsInputError">
                            <fmt:message key="myChannel.panel.channelName" />
                        </label>
                        <div class="col-sm-10">
                            <input type="text" name="locations" class="form-control" id="locationsInputError"
                                   value="${param.locations}">
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                            <span class="help-block">${locationsValidationError}</span>
                        </div>
                    </div>
                </c:if>

                <!-- Dates. -->
                <c:if test="${datesValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="dates">
                            <fmt:message key="myChannel.panel.dates" />
                        </label>
                        <div class="col-sm-10">
                            <input class="form-control" name="dates" id="dates" type="text"
                                   value="${param.dates}">
                        </div>
                    </div>
                </c:if>
                <c:if test="${datesValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="datesInputError">
                            <fmt:message key="myChannel.panel.dates" />
                        </label>
                        <div class="col-sm-10">
                            <input type="text" name="dates" class="form-control" id="datesInputError"
                                   value="${param.locations}">
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                            <span class="help-block">${datesValidationError}</span>
                        </div>
                    </div>
                </c:if>

                <!-- Contact. -->
                <c:if test="${contactsValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="contact">
                            <fmt:message key="myChannel.panel.contactInformation" />
                        </label>
                        <div class="col-sm-10">
                            <input class="form-control" name="contact" id="contact" type="text"
                                   value="${param.contact}">
                        </div>
                    </div>
                </c:if>
                <c:if test="${contactsValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="contactInputError">
                            <fmt:message key="myChannel.panel.contactInformation" />
                        </label>
                        <div class="col-sm-10">
                            <input type="text" name="contact" class="form-control" id="contactInputError"
                                   value="${param.contact}">
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                            <span class="help-block">${contactsValidationError}</span>
                        </div>
                    </div>
                </c:if>

                <!-- Website -->
                <c:if test="${websiteValidationError == null}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="website">
                            <fmt:message key="myChannel.panel.website" />
                        </label>
                        <div class="col-sm-10">
                            <input class="form-control" name="website" id="website" type="text"
                                   value="${param.website}">
                        </div>
                    </div>
                </c:if>
                <c:if test="${websiteValidationError != null}">
                    <div class="form-group has-error has-feedback">
                        <label class="col-sm-2 control-label" for="websiteInputError">
                            <fmt:message key="myChannel.panel.dates" />
                        </label>
                        <div class="col-sm-10">
                            <input type="text" name="website" class="form-control" id="websiteInputError"
                                   value="${param.website}">
                            <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                            <span class="help-block">${websiteValidationError}</span>
                        </div>
                    </div>
                </c:if>

                <!-- Lecture related fields. -->
                <c:if test="${editableChannel != null && editableChannel.getType() == typeLecture}">

                    <!-- Faculty -->
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="faculty">
                            <fmt:message key="myChannel.panel.lecture.faculty" />
                        </label>
                        <div class="col-sm-10">
                            <select class="form-control" name="faculty" id="faculty" disabled >
                                <option value="computerScience">
                                    <fmt:message key="general.faculty.computerScienceAndEngineering" />
                                </option>
                                <option value="mathematics">
                                    <fmt:message key="general.faculty.mathematicsAndEconomics" />
                                </option>
                                <option value="medicines">
                                    <fmt:message key="general.faculty.medicines" />
                                </option>
                                <option value="naturalSciences">
                                    <fmt:message key="general.faculty.naturalSciences" />
                                </option>
                            </select>
                        </div>
                    </div>

                    <!-- Lecturer -->
                    <c:if test="${lecturerValidationError == null}">
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="lecturer">
                                <fmt:message key="myChannel.panel.lecture.lecturer" />
                            </label>
                            <div class="col-sm-10">
                                <input class="form-control" name="lecturer" id="lecturer" type="text"
                                       value="${param.lecturer}">
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${lecturerValidationError != null}">
                        <div class="form-group has-error has-feedback">
                            <label class="col-sm-2 control-label" for="lecturerInputError">
                                <fmt:message key="myChannel.panel.lecture.lecturer" />
                            </label>
                            <div class="col-sm-10">
                                <input type="text" name="lecturer" class="form-control" id="lecturerInputError"
                                       value="${param.lecturer}">
                                <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                                <span class="help-block">${lecturerValidationError}</span>
                            </div>
                        </div>
                    </c:if>

                    <!-- Assistant -->
                    <c:if test="${assistantValidationError == null}">
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="assistant">
                                <fmt:message key="myChannel.panel.lecture.assistant" />
                            </label>
                            <div class="col-sm-10">
                                <input class="form-control" name="assistant" id="assistant" type="text"
                                       value="${param.assistant}">
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${assistantValidationError != null}">
                        <div class="form-group has-error has-feedback">
                            <label class="col-sm-2 control-label" for="assistantInputError">
                                <fmt:message key="myChannel.panel.lecture.assistant" />
                            </label>
                            <div class="col-sm-10">
                                <input type="text" name="assistant" class="form-control" id="assistantInputError"
                                       value="${param.assistant}">
                                <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                                <span class="help-block">${assistantValidationError}</span>
                            </div>
                        </div>
                    </c:if>

                    <!-- Start date of lecture. -->
                    <c:if test="${startDateValidationError == null}">
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="startDate">
                                <fmt:message key="myChannel.panel.lecture.startDate" />
                            </label>
                            <div class="col-sm-10">
                                <input class="form-control" name="startDate" id="startDate" type="text"
                                       value="${param.startDate}">
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${startDateValidationError != null}">
                        <div class="form-group has-error has-feedback">
                            <label class="col-sm-2 control-label" for="startDateInputError">
                                <fmt:message key="myChannel.panel.lecture.startDate" />
                            </label>
                            <div class="col-sm-10">
                                <input type="text" name="startDate" class="form-control" id="startDateInputError"
                                       value="${param.assistant}">
                                <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                                <span class="help-block">${startDateValidationError}</span>
                            </div>
                        </div>
                    </c:if>

                    <!-- End date of lecture. -->
                    <c:if test="${endDateValidationError == null}">
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="endDate">
                                <fmt:message key="myChannel.panel.lecture.endDate" />
                            </label>
                            <div class="col-sm-10">
                                <input class="form-control" name="endDate" id="endDate" type="text"
                                       value="${param.endDate}">
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${endDateValidationError != null}">
                        <div class="form-group has-error has-feedback">
                            <label class="col-sm-2 control-label" for="endDateInputError">
                                <fmt:message key="myChannel.panel.lecture.startDate" />
                            </label>
                            <div class="col-sm-10">
                                <input type="text" name="endDate" class="form-control" id="endDateInputError"
                                       value="${param.endDate}">
                                <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                                <span class="help-block">${endDateValidationError}</span>
                            </div>
                        </div>
                    </c:if>

                </c:if>

                <!-- Event based fields -->
                <c:if test="${editableChannel != null && editableChannel.getType() == typeEvent}">

                    <!-- Cost -->
                    <c:if test="${eventCostValidationError == null}">
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="eventCost">
                                <fmt:message key="myChannel.panel.event.cost" />
                            </label>
                            <div class="col-sm-10">
                                <input class="form-control" name="eventCost" id="eventCost" type="text"
                                       value="${param.eventCost}">
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${eventCostValidationError != null}">
                        <div class="form-group has-error has-feedback">
                            <label class="col-sm-2 control-label" for="eventCostInputError">
                                <fmt:message key="myChannel.panel.event.cost" />
                            </label>
                            <div class="col-sm-10">
                                <input type="text" name="eventCost" class="form-control" id="eventCostInputError"
                                       value="${param.eventCost}">
                                <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                                <span class="help-block">${eventCostValidationError}</span>
                            </div>
                        </div>
                    </c:if>

                    <!-- Organizer -->
                    <c:if test="${organizerValidationError == null}">
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="organizer">
                                <fmt:message key="myChannel.panel.event.organizer" />
                            </label>
                            <div class="col-sm-10">
                                <input class="form-control" name="organizer" id="organizer" type="text"
                                       value="${param.organizer}">
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${organizerValidationError != null}">
                        <div class="form-group has-error has-feedback">
                            <label class="col-sm-2 control-label" for="organizerInputError">
                                <fmt:message key="myChannel.panel.event.organizer" />
                            </label>
                            <div class="col-sm-10">
                                <input type="text" name="organizer" class="form-control" id="organizerInputError"
                                       value="${param.organizer}">
                                <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                                <span class="help-block">${organizerValidationError}</span>
                            </div>
                        </div>
                    </c:if>

                </c:if>

                <!-- Event based fields -->
                <c:if test="${editableChannel != null && editableChannel.getType() == typeSports}">

                    <!-- Cost -->
                    <c:if test="${sportsCostValidationError == null}">
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="sportsCost">
                                <fmt:message key="myChannel.panel.sports.cost" />
                            </label>
                            <div class="col-sm-10">
                                <input class="form-control" name="sportsCost" id="sportsCost" type="text"
                                       value="${param.sportsCost}">
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${sportsCostValidationError != null}">
                        <div class="form-group has-error has-feedback">
                            <label class="col-sm-2 control-label" for="sportsCostInputError">
                                <fmt:message key="myChannel.panel.sports.cost" />
                            </label>
                            <div class="col-sm-10">
                                <input type="text" name="sportsCost" class="form-control" id="sportsCostInputError"
                                       value="${param.sportsCost}">
                                <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                                <span class="help-block">${sportsCostValidationError}</span>
                            </div>
                        </div>
                    </c:if>

                    <!-- Number of participants -->
                    <c:if test="${numberOfParticipantsValidationError == null}">
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="numberOfParticipants">
                                <fmt:message key="myChannel.panel.sports.numberOfParticipants" />
                            </label>
                            <div class="col-sm-10">
                                <input class="form-control" name="numberOfParticipants" id="numberOfParticipants"
                                       type="text"
                                       value="${param.numberOfParticipants}">
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${numberOfParticipantsValidationError != null}">
                        <div class="form-group has-error has-feedback">
                            <label class="col-sm-2 control-label" for="numberOfParticipantsInputError">
                                <fmt:message key="myChannel.panel.sports.numberOfParticipants" />
                            </label>
                            <div class="col-sm-10">
                                <input type="text" name="numberOfParticipants" class="form-control"
                                       id="numberOfParticipantsInputError"
                                       value="${param.numberOfParticipants}">
                                <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                                <span class="help-block">${numberOfParticipantsValidationError}</span>
                            </div>
                        </div>
                    </c:if>

                </c:if>

                <br>
                <button class="btn btn-lg btn-primary btn-block"
                        type="submit" name="task" value="save">
                    <fmt:message key="channelDetails.submitButton.label"/>
                </button>
                <br>

            </form>
        </div>

        <div class="col-md-2" ></div>
    </div>
</div>

<script>
    $(document).ready(function(){
            for (var i = new Date().getFullYear() + 50; i > 1950; i--)
            {
                var hiddenYearPicker = $('#hiddenYearPickerValue');
                var hiddenYearPickerError = $('#hiddenYearPickerInputErrorValue');
                if (hiddenYearPicker.length) {
                    if (hiddenYearPicker.val() == i) {
                        $('#yearPicker').append($('<option selected />').val(i).html(i));
                    }
                    else {
                        $('#yearPicker').append($('<option />').val(i).html(i));
                    }
                } else if (hiddenYearPickerError.length) {
                    if (hiddenYearPickerError.val() == i) {
                        $('#yearPickerError').append($('<option selected />').val(i).html(i));
                    }
                    else {
                        $('#yearPickerError').append($('<option />').val(i).html(i));
                    }
                }
            }

            // alert("1: " + hiddenYearPicker.length + ", 2: " + hiddenYearPickerError.length);
    });
</script>

<!-- Page footer. -->
<%@ include file="footer.jsp" %>