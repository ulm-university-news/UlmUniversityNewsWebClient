package ulm.university.news.webclient.controller.actions;

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
 * An action that performs the required operations for the loading process of the edit reminder view.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class LoadEditReminderAction implements Action {

    /** An instance of the Logger class which performs logging for the LoadEditReminderAction class. */
    private static final Logger logger = LoggerFactory.getLogger(LoadEditReminderAction.class);

    /**
     * This method executes the business logic for loading the edit reminder view.
     *
     * @param requestContext The context of the request for which the execution is triggered.
     * @return Returns the status that is used to determine the view that should be displayed after execution.
     * @throws SessionIsExpiredException If the session of the user is expired.
     */
    public String execute(RequestContextManager requestContext) throws SessionIsExpiredException, ServerException {
        String status = Constants.EDIT_REMINDER_DIALOG_LOADED;
        ChannelAPI channelAPI = new ChannelAPI();

        Moderator activeModerator = requestContext.retrieveRequestor();
        Channel currentChannel = (Channel) requestContext.retrieveFromSession("currentChannel");
        Reminder currentReminder = (Reminder) requestContext.retrieveFromSession("currentReminder");

        Reminder reminder = null;
        if (activeModerator != null && currentChannel != null && currentReminder != null){
            // Retrieve reminder from server.
            try{
                reminder = channelAPI.getReminder(
                        activeModerator.getServerAccessToken(),
                        currentChannel.getId(),
                        currentReminder.getId());
            } catch (APIException ex){
                status = Constants.EDIT_REMINDER_DIALOG_LOADING_FAILED;
                Translator translator = Translator.getInstance();
                String errorMessage = null;
                Locale currentLocale = requestContext.retrieveLocale();

                switch (ex.getErrorCode()){
                    case Constants.CHANNEL_NOT_FOUND:
                        errorMessage = translator.getText(currentLocale, "general.message.error.channelNotFound");
                        break;
                    case Constants.REMINDER_NOT_FOUND:
                        errorMessage = translator.getText(currentLocale, "general.message.error.reminderNotFound");
                        break;
                    case Constants.MODERATOR_NOT_FOUND:
                        errorMessage = translator.getText(currentLocale, "general.message.error.moderatorNotFound");
                        break;
                    case Constants.MODERATOR_FORBIDDEN:
                        errorMessage = translator.getText(currentLocale, "general.message.error.forbidden");
                        break;
                }

                // No appropriate error message selected?
                if (errorMessage == null){
                    // Pass error to front controller.
                    throw new ServerException(ex.getErrorCode(), "Failed to load reminder.", ex);
                }

                requestContext.addToRequestContext("loadError", errorMessage);
            }

            if (reminder != null){
                logger.debug("Start preparing request parameters for editing dialog.");
                // Prepare request parameters.
                Locale currentLocale = requestContext.retrieveLocale();

                logger.debug("Current locale is: {}.", currentLocale);

                // Start with date and time.
                if (currentLocale.equals(Locale.GERMAN) || currentLocale.equals(Locale.GERMANY)){
                    requestContext.addToRequestContext("initStartDate", reminder.getStartDate().toString("dd.MM.yyyy"));
                    requestContext.addToRequestContext("initEndDate", reminder.getEndDate().toString("dd.MM.yyyy"));
                    requestContext.addToRequestContext("initSelectedTime", reminder.getStartDate().toString("HH:mm"));
                }
                else if (currentLocale.equals(Locale.ENGLISH) || currentLocale.equals(Locale.UK) ||
                        currentLocale.equals(Locale.US)){
                    requestContext.addToRequestContext("initStartDate", reminder.getStartDate().toString("dd/MM/yyyy"));
                    requestContext.addToRequestContext("initEndDate", reminder.getEndDate().toString("dd/MM/yyyy"));
                    requestContext.addToRequestContext("initSelectedTime", reminder.getStartDate().toString("hh:mm " +
                            "aa"));
                }
                else {
                    logger.error("No local matched. Current locale is: {}", currentLocale);
                    logger.warn("Using english per default.");
                    // Try english as default.
                    requestContext.addToRequestContext("initStartDate", reminder.getStartDate().toString("dd/MM/yyyy"));
                    requestContext.addToRequestContext("initEndDate", reminder.getEndDate().toString("dd/MM/yyyy"));
                    requestContext.addToRequestContext("initSelectedTime", reminder.getStartDate().toString("hh:mm " +
                            "aa"));
                }

                // Interval type is determined based on the interval value.
                int intervalInDays = reminder.getInterval() / (60 * 60 * 24);
                if (intervalInDays == 0){
                    // It is a one time reminder.
                    requestContext.addToRequestContext("initIntervalType", "oneTime");
                }
                else if ((intervalInDays % 7) == 0){
                    // It is a weekly reminder.
                    requestContext.addToRequestContext("initIntervalType", "weekly");
                    // Set also interval value.
                    requestContext.addToRequestContext("initInterval", (intervalInDays / 7));
                }
                else {
                    // It is a daily reminder.
                    requestContext.addToRequestContext("initIntervalType", "daily");
                    // Set also interval value.
                    requestContext.addToRequestContext("initInterval", intervalInDays);
                }

                // Announcement related data fields.
                requestContext.addToRequestContext("initAnnouncementTitle", reminder.getTitle());
                requestContext.addToRequestContext("initAnnouncementText", reminder.getText());
                if (reminder.getPriority() == Priority.HIGH){
                    requestContext.addToRequestContext("initPriorityValue", "high");
                } else {
                    requestContext.addToRequestContext("initPriorityValue", "normal");
                }

                String debugLog = String.format("Prepared parameters are: %n -> StartDate: %s %n -> EndDate: %s %n ->" +
                        " SelectedTime: %s %n -> IntervalType: %s %n -> Interval: %s %n -> Title: %s %n -> Text: %s " +
                        "%n -> Priority: %s",
                        requestContext.retrieveFromRequestContext("initStartDate"),
                        requestContext.retrieveFromRequestContext("initEndDate"),
                        requestContext.retrieveFromRequestContext("initSelectedTime"),
                        requestContext.retrieveFromRequestContext("initIntervalType"),
                        requestContext.retrieveFromRequestContext("initInterval"),
                        requestContext.retrieveFromRequestContext("initAnnouncementTitle"),
                        requestContext.retrieveFromRequestContext("initAnnouncementText"),
                        requestContext.retrieveFromRequestContext("initPriorityValue"));
                logger.debug(debugLog);
            }
        }

        return status;
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
