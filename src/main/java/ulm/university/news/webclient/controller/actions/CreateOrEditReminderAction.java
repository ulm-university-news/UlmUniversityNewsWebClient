package ulm.university.news.webclient.controller.actions;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.api.ChannelAPI;
import ulm.university.news.webclient.controller.context.RequestContextManager;
import ulm.university.news.webclient.controller.interfaces.Action;
import ulm.university.news.webclient.data.Channel;
import ulm.university.news.webclient.data.Moderator;
import ulm.university.news.webclient.data.Reminder;
import ulm.university.news.webclient.data.enums.Priority;
import ulm.university.news.webclient.util.Constants;
import ulm.university.news.webclient.util.Translator;
import ulm.university.news.webclient.util.exceptions.APIException;
import ulm.university.news.webclient.util.exceptions.ServerException;
import ulm.university.news.webclient.util.exceptions.SessionIsExpiredException;

import java.util.Locale;

/**
 * The action offers functionality to create a new reminder for a specific channel or to edit
 * an existing one.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class CreateOrEditReminderAction implements Action {

    /** A reference to the context data of the current request. */
    private RequestContextManager requestContext;

    /** A reference to an instance of ChannelAPI. */
    private ChannelAPI channelAPI;

    /** An instance of the Logger class which performs logging for the CreateOrEditReminderAction class. */
    private static final Logger logger = LoggerFactory.getLogger(CreateOrEditReminderAction.class);

    /**
     * This method executes the business logic for performing the creation or editing process.
     *
     * @param requestContext The context of the request for which the execution is triggered.
     * @return Returns the status that is used to determine the view that should be displayed after execution.
     * @throws SessionIsExpiredException If the session of the user is expired.
     */
    public String execute(RequestContextManager requestContext) throws SessionIsExpiredException, ServerException {
        String status = null;
        this.requestContext = requestContext;
        channelAPI = new ChannelAPI();

        String task = requestContext.getRequestParameter("task");

        Moderator activeModerator = requestContext.retrieveRequestor();
        Channel currentChannel = (Channel) requestContext.retrieveFromSession("currentChannel");

        if (task != null && task.equals("createReminder") &&
                activeModerator != null && currentChannel != null){
            logger.info("Start creation process for reminder.");
            // Create reminder.
            status = createReminderInstance(currentChannel, activeModerator);
        }

        // Test.
//        status = Constants.CREATION_OF_REMINDER_FAILED;
//        Reminder test = retrieveReminderObjectFromRequestData();
//
//        boolean isValid = validateReminderParameters(test);
//        if (!isValid){
//            logger.debug("Validation of reminder failed.");
//            status = Constants.REMINDER_VALIDATION_ERROR;
//        }

        return status;
    }

    /**
     * Executes the operations for the creation of a new reminder. Collects the entered data
     * and performs a request to the server.
     *
     * @param currentChannel The currently selected channel for which the reminder will be created.
     * @param activeModerator The active moderator that performs the request.
     * @return The status of the creation process.
     * @throws ServerException If the creation process fails and no appropriate error handling is possible within the
     *      method.
     */
    private String createReminderInstance(Channel currentChannel, Moderator activeModerator) throws ServerException {
        String status = null;

        // Create reminder.
        // Get reminder object with entered data first.
        Reminder creatableReminder = retrieveReminderObjectFromRequestData();

        // Second, validate the entered data.
        boolean isValid = validateReminderParameters(creatableReminder);
        if (!isValid){
            logger.debug("Validation of reminder failed. Cannot create reminder.");
            status = Constants.REMINDER_VALIDATION_ERROR;
        } else {
            // Send request to create reminder.
            try{
                Reminder createdReminder = channelAPI.createReminder(activeModerator.getServerAccessToken(),
                        currentChannel.getId(), creatableReminder);

                if (createdReminder != null){
                    logger.info("Created a reminder. It has the id {} and sends announcement with title: {}.",
                            createdReminder.getId(), createdReminder.getTitle());

                    status = Constants.CREATED_REMINDER;
                }
            } catch (APIException ex){
                String errorMessage;
                status = Constants.CREATION_OF_REMINDER_FAILED;

                errorMessage = getErrorMessage(requestContext.retrieveLocale(), ex.getErrorCode());
                if (errorMessage == null) {
                    // No appropriate error message could be generated. Pass error to front controller.
                    throw new ServerException(ex.getErrorCode(), "Failed to create reminder.");
                }

                // Add error message to request context.
                requestContext.addToRequestContext("ReminderActionFailed", errorMessage);
            }
        }

        return status;
    }

    /**
     * Reads the required parameters from the request context and creates
     * an instance of the reminder class. Fills the reminder instance with the
     * data from the request.
     * @return An object of the Reminder class.
     */
    private Reminder retrieveReminderObjectFromRequestData(){
        Reminder reminder = new Reminder();

        String startDate = requestContext.getRequestParameter("startDate");
        String endDate = requestContext.getRequestParameter("endDate");
        String selectedTime = requestContext.getRequestParameter("selectedTime");
        String intervalType = requestContext.getRequestParameter("intervalType");
        String interval = requestContext.getRequestParameter("interval");
        String announcementTitle = requestContext.getRequestParameter("announcementTitle");
        String messagePriority = requestContext.getRequestParameter("priorityValue");
        String announcementText = requestContext.getRequestParameter("announcementText");
        String skipReminderCBValue = requestContext.getRequestParameter("skipReminderFlag");

        String testString = String.format("Parameters: %n -> StartDate: %s %n -> EndDate: %s %n -> SelectedTime: %s " +
                "%n -> IntervalType: %s %n -> Interval: %s %n -> AnnouncementTitle: %s %n -> MessagePriority: %s %n "
                + "-> AnnouncementText: %s ", startDate, endDate, selectedTime, intervalType, interval,
                announcementTitle, messagePriority, announcementText);
        logger.debug(testString);

        Locale currentLocale = requestContext.retrieveLocale();

        // Parse start and end date.
        DateTime reminderStartDate = null;
        DateTime reminderEndDate = null;
        if (startDate != null && startDate.trim().length() > 0 &&
                endDate != null && endDate.trim().length() > 0 &&
                selectedTime != null){
            try{
                if (currentLocale.equals(Locale.GERMAN)){
                    logger.debug("Case GERMAN: ");
                    DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm").withLocale(Locale.GERMAN);
                    reminderStartDate = formatter.parseDateTime(startDate + " " + selectedTime);
                    reminderEndDate = formatter.parseDateTime(endDate + " " + selectedTime);
                }
                else if (currentLocale.equals(Locale.ENGLISH)){
                    logger.debug("Case ENGLISH: ");
                    DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy hh:mm aa").withLocale(Locale.ENGLISH);
                    reminderStartDate = formatter.parseDateTime(startDate + " " + selectedTime);
                    reminderEndDate = formatter.parseDateTime(endDate + " " + selectedTime);
                }
            } catch (IllegalArgumentException ex){
                logger.error("Invalid date format was provided. The provided start date is: {} and the provided end " +
                        "date is {}. Exception message is: '{}'.", startDate, endDate, ex.getMessage());
                logger.warn("Reminder dates will be null so data validation will fail.");
            }
        }

        // Calculate interval.
        int intervalValue = -1;
        if (intervalType != null){
            if (intervalType.equals("daily")){
                int intervalInDays = parseIntervalString(interval);

                if (intervalInDays != -1){
                    // One day has a total amount of 60 (s) * 60 (min) * 24 (h) seconds.
                    intervalValue = intervalInDays * 60 * 60 * 24;
                    logger.debug("Calculated interval: {}", intervalValue);
                }
            }
            else if (intervalType.equals("weekly")){
                int intervalInWeeks = parseIntervalString(interval);

                if (intervalInWeeks != -1){
                    // One week has a total amount of 60 (s) * 60 (min) * 24 (h) * 7 (days) seconds.
                    intervalValue = intervalInWeeks * 60 * 60 * 24 * 7;
                    logger.debug("Calculated interval: {}", intervalValue);
                }
            }
            else if (intervalType.equals("oneTime")){
                intervalValue = 0;
                logger.debug("Calculated interval: It is a one-time reminder.");
            }
        }

        // Check if skip reminder flag is set.
        boolean skipReminder;
        if (skipReminderCBValue == null){
            logger.debug("Skip reminder flag is not set.");
            skipReminder = false;
        }
        else{
            logger.debug("Skip reminder flag is set.");
            skipReminder = true;
        }

        // Determine priority.
        Priority priority = Priority.NORMAL;
        if (messagePriority != null && messagePriority.equals("high")){
            priority = Priority.HIGH;
        }

        // Fill reminder object with data.
        reminder.setStartDate(reminderStartDate);
        reminder.setEndDate(reminderEndDate);
        reminder.setInterval(intervalValue);
        reminder.setTitle(announcementTitle);
        reminder.setPriority(priority);
        reminder.setText(announcementText);
        reminder.setIgnore(skipReminder);

        return reminder;
    }

    /**
     * Performs the validation of the data of the specific Reminder object. Returns the validation status.
     *
     * @param enteredData An object of the class Reminder with the data provided by the requestor.
     * @return Returns true, if the validation is successful. Returns false, if there are validation errors.
     */
    private boolean validateReminderParameters(Reminder enteredData){
        if (enteredData == null){
            logger.error("No valid data object passed to the method.");
            return false;
        }

        boolean isValid = true;
        Translator translator = Translator.getInstance();

        if (enteredData.getStartDate() == null || enteredData.getEndDate() == null){
            isValid = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "reminder.validation.error.dateNotSet");
            setValidationError(Constants.REMINDER_INVALID_DATES, errorMsg);
        }

        if (enteredData.getTitle() == null || enteredData.getTitle().trim().length() == 0){
            isValid = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "reminder.validation.error.titleNotSet");
            setValidationError(Constants.REMINDER_INVALID_TITLE, errorMsg);
        }

        if (enteredData.getText() == null || enteredData.getText().trim().length() == 0){
            isValid = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "reminder.validation.error.textNotSet");
            setValidationError(Constants.REMINDER_INVALID_TEXT, errorMsg);
        }

        // Check range conditions of title and text.
        if (enteredData.getTitle() != null && enteredData.getTitle().length() > Constants.ANNOUNCEMENT_TITLE_MAX_LENGTH){
            isValid = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "reminder.validation.error.titleTooLong");
            setValidationError(Constants.REMINDER_INVALID_TITLE, errorMsg);
        }

        if (enteredData.getText() != null && enteredData.getText().length() > Constants.MESSAGE_MAX_LENGTH){
            isValid = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "reminder.validation.error.textTooLong");
            setValidationError(Constants.REMINDER_INVALID_TEXT, errorMsg);
        }

        // Check date conditions.
        if (enteredData.getStartDate() != null && enteredData.getEndDate() != null){
            if (enteredData.getStartDate().isAfter(enteredData.getEndDate())){
                // Start date is after the end date.
                isValid = false;
                String errorMsg = translator.getText(requestContext.retrieveLocale(),
                        "reminder.validation.error.startDateAfterEndDate");
                setValidationError(Constants.REMINDER_INVALID_DATES, errorMsg);
            }

            if (enteredData.getEndDate().isBefore(DateTime.now())){
                // End date is not in the future.
                isValid = false;
                String errorMsg = translator.getText(requestContext.retrieveLocale(),
                        "reminder.validation.error.endDateNotInFuture");
                setValidationError(Constants.REMINDER_INVALID_DATES, errorMsg);
            }
        }

        // Check interval conditions.
        if (enteredData.getStartDate() != null && enteredData.getEndDate() != null){
            // If start date equals end date it's a one time reminder, so interval has to be 0.
            if (enteredData.getStartDate().equals(enteredData.getEndDate()) && enteredData.getInterval() != 0){
                isValid = false;
                String errorMsg = translator.getText(requestContext.retrieveLocale(),
                        "reminder.validation.error.needsToBeOneTime");
                setValidationError(Constants.REMINDER_INVALID_INTERVAL, errorMsg);
            }

            // Check if the interval is a multiple of a day (86400s = 24h * 60m * 60s).
            if (enteredData.getInterval() % 86400 != 0){
                isValid = false;
                String errorMsg = translator.getText(requestContext.retrieveLocale(),
                        "reminder.validation.error.intervalInvalidFormat");
                setValidationError(Constants.REMINDER_INVALID_INTERVAL, errorMsg);
            }

            // Check if interval is at least one day and no more than 28 days (4 weeks).
            if (enteredData.getInterval() != 0 &&
                    (enteredData.getInterval() < 86400 || enteredData.getInterval() > 2419200)){
                isValid = false;
                String errorMsg = translator.getText(requestContext.retrieveLocale(),
                        "reminder.validation.error.intervalOutOfRange");
                setValidationError(Constants.REMINDER_INVALID_INTERVAL, errorMsg);
            }
        }

        return isValid;
    }

    /**
     * Adds validation errors to the request context based on the error code.
     *
     * @param errorCode The error code of the validation error.
     */
    private void setValidationError (int errorCode, String errorMessage)
    {
        switch (errorCode){
            case Constants.REMINDER_DATA_INCOMPLETE:
                requestContext.addToRequestContext("ReminderActionFailed", errorMessage);
                break;
            case Constants.REMINDER_INVALID_DATES:
                requestContext.addToRequestContext("startDateValidationError", errorMessage);
                requestContext.addToRequestContext("endDateValidationError", errorMessage);
                break;
            case Constants.REMINDER_INVALID_INTERVAL:
                requestContext.addToRequestContext("ReminderActionFailed", errorMessage);
                break;
            case Constants.REMINDER_INVALID_TEXT:
                requestContext.addToRequestContext("announcementTextValidationError", errorMessage);
                break;
            case Constants.REMINDER_INVALID_TITLE:
                requestContext.addToRequestContext("announcementTitleValidationError", errorMessage);
                break;
        }
    }

    /**
     * Retrieves the corresponding error message in the desired language depending
     * on the error code.
     *
     * @param currentLocale The locale object indicating the desired language.
     * @param errorCode The error code.
     * @return The error message.
     */
    private String getErrorMessage(Locale currentLocale, int errorCode) {
        String errorMessage;
        // General error codes.
        switch (errorCode) {
            case Constants.CHANNEL_NOT_FOUND:
                errorMessage = Translator.getInstance().getText(currentLocale,
                        "general.message.error.channelNotFound");
                break;
            case Constants.MODERATOR_FORBIDDEN:
                errorMessage = Translator.getInstance().getText(currentLocale,
                        "general.message.error.forbidden");
                break;
            case Constants.CONNECTION_FAILURE:
                errorMessage = Translator.getInstance().getText(currentLocale,
                        "general.message.error.connectionFailure");
                break;
            case Constants.DATABASE_FAILURE:
                errorMessage = Translator.getInstance().getText(currentLocale,
                        "general.message.error.databaseFailure");
                break;
            default:
                errorMessage = null;
        }

        // Validation related error code.
        if (errorCode == Constants.REMINDER_DATA_INCOMPLETE || errorCode == Constants.REMINDER_INVALID_DATES ||
                errorCode == Constants.REMINDER_INVALID_INTERVAL || errorCode == Constants.REMINDER_INVALID_TITLE ||
                errorCode == Constants.REMINDER_INVALID_TEXT){
            // Set a standard error message.
            errorMessage = Translator.getInstance().getText(currentLocale,
                    "reminder.validation.error.generalError");
            // Set validation error.
            setValidationError(errorCode, errorMessage);
        }

        return errorMessage;
    }
    /**
     * Helper method to parse the provided interval in days/weeks/oneTime to an integer value.
     * @param intervalString The interval string that was retrieved from the request.
     * @return The parsed interval value as an integer.
     */
    private int parseIntervalString(String intervalString){
        int value = -1;
        try{
            value = Integer.parseInt(intervalString);
        } catch (NumberFormatException ex){
            logger.error("Couldn't parse interval value from string. Given string is {}.", intervalString);
        }
        return value;
    }

    /**
     * This method indicates whether an active session is required in order
     * to execute the Action.
     *
     * @return Returns true if an active session is required, otherwise false.
     */
    public boolean requiresSession() {
        return true;
    }

    /**
     * This method indicates whether administrator permissions are required in order
     * to execute the Action.
     *
     * @return Returns true if administrator permissions are required, otherwise false.
     */
    public boolean requiresAdminPermissions() {
        return false;
    }
}
