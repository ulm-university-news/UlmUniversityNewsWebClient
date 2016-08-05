<%@include file="header.jsp" %>


<div class="container">
  <div class="row">
    <div class="col-md-12">
      <h3><fmt:message key="editReminder.title"/></h3>

      <p><fmt:message key="editReminder.info"/></p>
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
  <c:if test="${ReminderActionFailed != null}">
    <div class="row">
      <div class="col-md-12">
        <div class="alert alert-danger" role="alert">
          <strong><fmt:message key="general.alert.failure"/></strong>

          <p>${ReminderActionFailed}</p>
        </div>
      </div>
    </div>
  </c:if>
  <c:if test="${reminderDetailsNoUpdate != null}">
      <div class="row">
          <div class="col-md-12">
              <div class="alert alert-warning" role="alert">
                  <strong><fmt:message key="general.alert.warning"/></strong>

                  <p>${reminderDetailsNoUpdate}</p>
              </div>
          </div>
      </div>
    </c:if>

  <!-- Row for form. -->
  <div class="row">

    <!-- Dummy column -->
    <div class="col-md-1"></div>

    <!-- Column with data fields.-->
    <div class="col-md-10">
      <!-- Input form for reminder data. -->
      <form class="form-horizontal" method="post" action="${base}webclient/reminderEdit"
            accept-charset="UTF-8">

        <% // Prepare selected startDate value. If an init value is set on first load of the page, take this value.
           // Otherwise take the value that has been stored previously in the request parameters. %>
        <c:choose>
            <c:when test="${initStartDate != null}">
                <c:set var="selectedStartDateValue" value="${initStartDate}" scope="page" />
            </c:when>
            <c:otherwise>
                <c:set var="selectedStartDateValue" value="${param.startDate}" scope="page" />
            </c:otherwise>
        </c:choose>
        <!-- Start date of reminder -->
        <c:if test="${startDateValidationError == null}">
          <div class="form-group">
            <label class="col-sm-2 control-label" for="startDatePicker">
              <fmt:message key="reminder.general.panel.startDate"/>
            </label>

            <div class="col-md-10">
              <div class="input-group date">
                <input type='text' class="form-control" name="startDate" id="startDatePicker"
                       value="${selectedStartDateValue}" autocomplete="off" />
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
              </div>
            </div>
          </div>
        </c:if>
        <c:if test="${startDateValidationError != null}">
          <div class="form-group has-error has-feedback">
            <label class="col-sm-2 control-label" for="startDatePickerValidationError">
              <fmt:message key="reminder.general.panel.startDate"/>
            </label>

            <div class="col-md-10">
              <div class="input-group date">
                <input type='text' class="form-control" name="startDate"
                       id="startDatePickerValidationError" value="${selectedStartDateValue}" autocomplete="off" />
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
              </div>
              <p><span class="help-block">${startDateValidationError}</span></p>
            </div>
          </div>
        </c:if>

        <% // Prepare selected endDate value. If an init value is set on first load of the page, take this value.
           // Otherwise take the value that has been stored previously in the request parameters. %>
        <c:choose>
            <c:when test="${initEndDate != null}">
                <c:set var="selectedEndDateValue" value="${initEndDate}" scope="page" />
            </c:when>
            <c:otherwise>
                <c:set var="selectedEndDateValue" value="${param.endDate}" scope="page" />
            </c:otherwise>
        </c:choose>
        <!-- End date of reminder -->
        <c:if test="${endDateValidationError == null}">
          <div class="form-group">
            <label class="col-sm-2 control-label" for="endDatePicker">
              <fmt:message key="reminder.general.panel.endDate"/>
            </label>

            <div class="col-md-10">
              <div class="input-group date">
                <input type='text' class="form-control" name="endDate" id="endDatePicker"
                       value="${selectedEndDateValue}" autocomplete="off" />
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
              </div>
            </div>
          </div>
        </c:if>
        <c:if test="${endDateValidationError != null}">
          <div class="form-group has-error has-feedback">
            <label class="col-sm-2 control-label" for="endDatePickerValidationError">
              <fmt:message key="reminder.general.panel.endDate"/>
            </label>

            <div class="col-md-10">
              <div class="input-group date">
                <input type='text' class="form-control" name="endDate"
                       id="endDatePickerValidationError" value="${selectedEndDateValue}" autocomplete="off" />
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
              </div>
              <p><span class="help-block">${endDateValidationError}</span></p>
            </div>
          </div>
        </c:if>

        <% // Prepare selected time value. If an init value is set on first load of the page, take this value.
           // Otherwise take the value that has been stored previously in the request parameters. %>
        <c:choose>
            <c:when test="${initSelectedTime != null}">
                <c:set var="selectedTimeValue" value="${initSelectedTime}" scope="page" />
            </c:when>
            <c:otherwise>
                <c:set var="selectedTimeValue" value="${param.selectedTime}" scope="page" />
            </c:otherwise>
        </c:choose>
        <!-- Time of reminder event. -->
        <div class="form-group">
            <label class="col-sm-2 control-label" for="selectedTime">
                <fmt:message key="reminder.general.panel.time"/>
            </label>

            <div class="col-md-10">
                <input class="form-control" name="selectedTime" id="selectedTime"
                       value="${selectedTimeValue}" />
            </div>
        </div>

        <br>
        <p><fmt:message key="reminder.general.panel.interval.desc" /></p>
        <br>

        <% // Prepare selected intervalType value. If an init value is set on first load of the page, take this value.
           // Otherwise take the value that has been stored previously in the request parameters. %>
        <c:choose>
            <c:when test="${initIntervalType != null}">
                <c:set var="selectedIntervalTypeValue" value="${initIntervalType}" scope="page" />
            </c:when>
            <c:otherwise>
                <c:set var="selectedIntervalTypeValue" value="${param.intervalType}" scope="page" />
            </c:otherwise>
        </c:choose>
        <!-- Interval type. -->
        <div class="form-group">
          <label class="col-sm-2 control-label" for="intervalType">
            <fmt:message key="reminder.general.panel.intervaltype"/>
          </label>

          <div class="col-md-10">
            <select class="form-control"
                    name="intervalType"
                    id="intervalType">
              <c:if test="${selectedIntervalTypeValue != null}">
                <!-- Set to previous defined intervalType. -->
                <c:choose>
                  <c:when test="${selectedIntervalTypeValue == 'daily'}">
                    <option value="daily" selected>
                      <fmt:message key="reminder.general.panel.intervaltype.daily" />
                    </option>
                    <option value="weekly">
                      <fmt:message key="reminder.general.panel.intervaltype.weekly" />
                    </option>
                    <option value="oneTime">
                      <fmt:message key="reminder.general.panel.intervaltype.oneTime" />
                    </option>
                  </c:when>
                  <c:when test="${selectedIntervalTypeValue == 'weekly'}">
                    <option value="daily">
                      <fmt:message key="reminder.general.panel.intervaltype.daily" />
                    </option>
                    <option value="weekly" selected>
                      <fmt:message key="reminder.general.panel.intervaltype.weekly" />
                    </option>
                    <option value="oneTime">
                      <fmt:message key="reminder.general.panel.intervaltype.oneTime" />
                    </option>
                  </c:when>
                  <c:when test="${selectedIntervalTypeValue == 'oneTime'}">
                    <option value="daily">
                      <fmt:message key="reminder.general.panel.intervaltype.daily" />
                    </option>
                    <option value="weekly">
                      <fmt:message key="reminder.general.panel.intervaltype.weekly" />
                    </option>
                    <option value="oneTime" selected>
                      <fmt:message key="reminder.general.panel.intervaltype.oneTime" />
                    </option>
                  </c:when>
                </c:choose>
              </c:if>
              <c:if test="${selectedIntervalTypeValue == null}">
                <!-- Default interval type. -->
                <option value="daily" selected>
                  <fmt:message key="reminder.general.panel.intervaltype.daily" />
                </option>
                <option value="weekly">
                  <fmt:message key="reminder.general.panel.intervaltype.weekly" />
                </option>
                <option value="oneTime">
                  <fmt:message key="reminder.general.panel.intervaltype.oneTime" />
                </option>
              </c:if>
            </select>
          </div>
        </div>

        <% // Prepare selected interval value. If an init value is set on first load of the page, take this value.
           // Otherwise take the value that has been stored previously in the request parameters. %>
        <c:choose>
            <c:when test="${initInterval != null}">
                <c:set var="selectedIntervalValue" value="${initInterval}" scope="page" />
            </c:when>
            <c:otherwise>
                <c:set var="selectedIntervalValue" value="${param.interval}" scope="page" />
            </c:otherwise>
        </c:choose>
        <!-- Interval -->
        <div class="form-group">
          <label class="col-sm-2 control-label" for="interval">
            <span id="labelDaily"><fmt:message key="reminder.general.panel.interval.labelDaily"/></span>
            <span id="labelWeekly"><fmt:message key="reminder.general.panel.interval.labelWeekly"/></span>
            <span id="labelOneTime"><fmt:message key="reminder.general.panel.interval.labelOneTime"/></span>
          </label>
          <div class="col-md-10">
            <c:if test="${selectedIntervalValue != null}">
              <!-- Hack to determine whether a value has already been defined in a previous page call! -->
              <input type="hidden" id="hiddenIntervalValue" value="${selectedIntervalValue}"/>
            </c:if>
            <select class="form-control"
                    name="interval"
                    id="interval">
              <!-- filled with jquery -->
            </select>
          </div>
        </div>

        <br>
        <p><fmt:message key="reminder.general.panel.message.desc" /></p>
        <br>

        <%
           // Prepare selected announcement title value. If an init value is set on first load of the page, take this value.
           // Otherwise take the value that has been stored previously in the request parameters. %>
        <c:choose>
            <c:when test="${initAnnouncementTitle != null}">
                <c:set var="selectedAnnouncementTitleValue" value="${initAnnouncementTitle}" scope="page" />
            </c:when>
            <c:otherwise>
                <c:set var="selectedAnnouncementTitleValue" value="${param.announcementTitle}" scope="page" />
            </c:otherwise>
        </c:choose>
        <!-- Title -->
        <c:if test="${announcementTitleValidationError == null}">
          <!-- No validation error on title -->
          <div class="form-group">
            <label class="col-sm-2 control-label" for="announcementTitle" >
              <fmt:message key="announcements.dialog.newMessage.title.textField.label" />
            </label>

            <div class="col-md-10">
              <input type="text" name="announcementTitle" class="form-control"
                     id="announcementTitle"
                     placeholder="<fmt:message
                                        key="announcements.dialog.newMessage.title.textField.placeholder" />"
                     value="${selectedAnnouncementTitleValue}" />
            </div>
          </div>
        </c:if>
        <c:if test="${announcementTitleValidationError != null}">
          <!-- Validation error on title. -->
          <div class="form-group has-error has-feedback">
            <label class="col-sm-2 control-label" for="announcementTitleError">
              <fmt:message key="announcements.dialog.newMessage.title.textField.label" />
            </label>

            <div class="col-md-10">
              <input type="text" name="announcementTitle" class="form-control"
                     id="announcementTitleError"
                     value="${selectedAnnouncementTitleValue}" />
              <span class="glyphicon glyphicon-remove form-control-feedback"></span>
              <span class="help-block">${announcementTitleValidationError}</span>
            </div>
          </div>
        </c:if>

        <% // Prepare selected priority value. If an init value is set on first load of the page, take this value.
           // Otherwise take the value that has been stored previously in the request parameters. %>
        <c:choose>
            <c:when test="${initPriorityValue != null}">
                <c:set var="selectedPriorityValue" value="${initPriorityValue}" scope="page" />
            </c:when>
            <c:otherwise>
                <c:set var="selectedPriorityValue" value="${param.priorityValue}" scope="page" />
            </c:otherwise>
        </c:choose>
        <!-- Priority -->
        <div class="form-group">
          <label class="col-md-2 control-label" for="priorityComboBox">
            <fmt:message key="announcements.dialog.newMessage.priorityComboBox.label" />
          </label>

          <div class="col-md-10">
            <select name="priorityValue" class="form-control" id="priorityComboBox">
              <c:if test="${selectedPriorityValue == null}">
                <!-- Default priority options. -->
                <option value="normal" selected>
                  <fmt:message
                          key="announcements.dialog.newMessage.priorityComboBox.value.normal" />
                </option>
                <option value="high">
                  <fmt:message
                          key="announcements.dialog.newMessage.priorityComboBox.value.high" />
                </option>
              </c:if>
              <c:if test="${selectedPriorityValue != null}">
                <!-- Select previously chosen priority. -->
                <c:choose>
                  <c:when test="${selectedPriorityValue == 'normal'}">
                    <option value="normal" selected>
                      <fmt:message
                              key="announcements.dialog.newMessage.priorityComboBox.value.normal" />
                    </option>
                    <option value="high">
                      <fmt:message
                              key="announcements.dialog.newMessage.priorityComboBox.value.high" />
                    </option>
                  </c:when>
                  <c:when test="${selectedPriorityValue == 'high'}">
                    <option value="normal">
                      <fmt:message
                              key="announcements.dialog.newMessage.priorityComboBox.value.normal" />
                    </option>
                    <option value="high" selected>
                      <fmt:message
                              key="announcements.dialog.newMessage.priorityComboBox.value.high" />
                    </option>
                  </c:when>
                </c:choose>
              </c:if>
            </select>
          </div>
        </div>

        <%
           // Prepare selected announcement text value. If an init value is set on first load of the page, take this value.
           // Otherwise take the value that has been stored previously in the request parameters. %>
        <c:choose>
            <c:when test="${initAnnouncementText != null}">
                <c:set var="selectedAnnouncementTextValue" value="${initAnnouncementText}" scope="page" />
            </c:when>
            <c:otherwise>
                <c:set var="selectedAnnouncementTextValue" value="${param.announcementText}" scope="page" />
            </c:otherwise>
        </c:choose>
        <!-- Message content -->
        <c:if test="${announcementTextValidationError == null}">
          <div class="form-group">
            <!-- No validation error on announcement text. -->
            <label class="col-md-2 control-label" for="announcementText" >
              <fmt:message key="announcements.dialog.newMessage.textField.label"/>
            </label>

            <div class="col-md-10">
                <textarea name="announcementText" class="form-control" rows="4"
                          id="announcementText"
                          placeholder="<fmt:message key="announcements.dialog.newMessage.textField.placeholder" />"
                          style="resize: none">${selectedAnnouncementTextValue}</textarea>
            </div>
          </div>
        </c:if>
        <c:if test="${announcementTextValidationError != null}">
          <div class="form-group has-error has-feedback">
            <!-- Validation error on announcement text. -->
            <label class="col-md-2 control-label" for="announcementTextError" >
              <fmt:message key="announcements.dialog.newMessage.textField.label"/>
            </label>

            <div class="col-md-10">
                <textarea name="announcementText" class="form-control" rows="4"
                          id="announcementTextError"
                          placeholder="<fmt:message key="announcements.dialog.newMessage.textField.placeholder" />"
                          style="resize: none;">${selectedAnnouncementTextValue}</textarea>
              <span class="glyphicon glyphicon-remove form-control-feedback"></span>
              <span class="help-block">${announcementTextValidationError}</span>
            </div>
          </div>
        </c:if>

        <br>
        <!-- Submit button. -->
        <button class="btn btn-primary pull-right"
                type="submit" name="task" value="editReminder">
          <fmt:message key="editReminder.submitButton.label"/>
        </button>

        <br>
        <br>

      </form>
    </div>

    <!-- Dummy column -->
    <div class="col-md-1"></div>

  </div> <!-- End of main form row div -->

  <script type="text/javascript">
    // Execute when document loaded.
    $(document).ready(function(){

      // For combining EL and javascript:
      // http://stackoverflow.com/questions/24559149/reading-a-jstl-variable-in-javascript-code
      // http://stackoverflow.com/questions/2547814/mixing-jsf-el-in-a-javascript-file
      var lang = '${language}';

      if (lang == 'en' || lang == 'en-EN' || lang == 'en-GB' || lang == 'en_EN'){
        $.datepicker.setDefaults( $.datepicker.regional[ "en-GB" ] );
        Globalize.culture('en-EN');
      }
      else if (lang == 'de' || lang == 'de-DE' || lang == 'de_DE' || lang == 'de-de'){
        $.datepicker.setDefaults( $.datepicker.regional[ "de" ] );
        Globalize.culture('de');
      }
      else{
          // Default is english.
        $.datepicker.setDefaults( $.datepicker.regional[ "en-GB" ] );
        Globalize.culture('en-EN');
      }

      // StartDate.
      $(function () {
        $('#startDatePicker').datepicker();
      });
      $(function () {
        $('#startDatePickerValidationError').datepicker();
      });

      // EndDate.
      $(function () {
        $('#endDatePicker').datepicker();
      });
      $(function () {
        $('#endDatePickerValidationError').datepicker();
      });

      // Time
      $( function() {
        $.widget( "ui.timespinner", $.ui.spinner, {
          options: {
            // seconds
            step: 60 * 1000,
            // hours
            page: 60
          },

          _parse: function( value ) {
            if ( typeof value === "string" ) {
              // already a timestamp
              if ( Number( value ) == value ) {
                return Number( value );
              }
              return +Globalize.parseDate( value );
            }
            return value;
          },

          _format: function( value ) {
            return Globalize.format( new Date(value), "t" );
          }
        });

        var inputElementSelectedTime = $( "#selectedTime" );
        inputElementSelectedTime.timespinner();

        if (lang == 'de' || lang == 'de-DE' || lang == 'de_DE' || lang == 'de-de'){
          var currentDE = inputElementSelectedTime.timespinner( "value" );
          if (currentDE == '0'){
            currentDE = Date.now().toString();
          }
          Globalize.culture('de');
          inputElementSelectedTime.timespinner( "value", currentDE );
        } else if (lang == 'en' || lang == 'en-EN' || lang == 'en-GB' || lang == 'en_EN'){
          var currentEN = inputElementSelectedTime.timespinner( "value" );
          if (currentEN == '0'){
            currentEN = Date.now().toString();
          }
          Globalize.culture('en-EN');
          inputElementSelectedTime.timespinner( "value", currentEN );
        }
          else {
            var currentDefault = inputElementSelectedTime.timespinner( "value" );
            if (currentDefault == '0'){
                currentDefault = Date.now().toString();
            }
            Globalize.culture('en-EN');
            inputElementSelectedTime.timespinner( "value", currentDefault );
        }

      } );

      // Interval type handling:
      var intervalTypeElement = $('#intervalType');
      // Call when element ready.
      intervalTypeElement.ready(function(){
        generateIntervalSelection();
      });

      // Call when element changed.
      intervalTypeElement.change(function(){
        generateIntervalSelection();
      });

      // function for setting options in interval selection element.
      function generateIntervalSelection(){
        var intervalType = $('#intervalType');
        var labelDaily = $('#labelDaily');
        var labelWeekly = $('#labelWeekly');
        var labelOneTime = $('#labelOneTime');
        var intervalElement = $('#interval');

        var hiddenIntervalValue = $('#hiddenIntervalValue');

        if (intervalType.length){
          if (intervalType.val() == 'daily'){
            labelDaily.show();
            labelWeekly.hide();
            labelOneTime.hide();

            intervalElement.attr("disabled", false);

            // Fill select element for interval. 28 days as options.
            intervalElement.empty();
            for (var i = 1; i <= 28; i++){
              if ((!hiddenIntervalValue.length || hiddenIntervalValue.val() == '') && i == 1){
                // No value set so far. Select value 1 per default.
                intervalElement.append($('<option selected />').val(i).html(i));
              }
              else if (hiddenIntervalValue.length && hiddenIntervalValue.val() == i){
                // There seems to be a value already set.
                intervalElement.append($('<option selected />').val(i).html(i));
              }
              else {
                intervalElement.append($('<option />').val(i).html(i));
              }
            }
          }
          else if (intervalType.val() == 'weekly'){
            labelDaily.hide();
            labelWeekly.show();
            labelOneTime.hide();

            intervalElement.attr("disabled", false);

            // What if value is greater than 4.
            if (hiddenIntervalValue.length && parseInt(hiddenIntervalValue.val()) > 4){
              hiddenIntervalValue.val(1);
            }

            // Fill select element for interval. 4 weeks as options.
            intervalElement.empty();
            for (var j = 1; j <= 4; j++){
              if ((!hiddenIntervalValue.length || hiddenIntervalValue.val() == '') && j == 1){
                // No value set so far. Select value 1 per default.
                intervalElement.append($('<option selected />').val(j).html(j));
              }
              else if (hiddenIntervalValue.length && hiddenIntervalValue.val() == j){
                // There seems to be a value already set.
                intervalElement.append($('<option selected />').val(j).html(j));
              }
              else {
                intervalElement.append($('<option />').val(j).html(j));
              }
            }
          }
          else if (intervalType.val() == 'oneTime'){
            labelDaily.hide();
            labelWeekly.hide();
            labelOneTime.show();

            intervalElement.attr("disabled", true);

            // Fill select element for interval. 1 dummy as options.
            intervalElement.empty();
            intervalElement.append($('<option selected />').val(1).html(1));
          }
        }
      }

    });
  </script>

</div> <!-- end of container div -->