package ulm.university.news.webclient.controller.actions;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.api.ChannelAPI;
import ulm.university.news.webclient.controller.context.RequestContextManager;
import ulm.university.news.webclient.controller.interfaces.Action;
import ulm.university.news.webclient.data.Moderator;
import ulm.university.news.webclient.data.Reminder;
import ulm.university.news.webclient.util.Constants;
import ulm.university.news.webclient.util.Translator;
import ulm.university.news.webclient.util.exceptions.APIException;
import ulm.university.news.webclient.util.exceptions.ServerException;
import ulm.university.news.webclient.util.exceptions.SessionIsExpiredException;

/**
 * An implementation of the Action interface which realizes different features that
 * can be executed from within the reminders.jsp view. This includes the deletion of reminders, the
 * activation/deactivation of reminders and the setting or resetting of the ignore flag in order to skip upcoming
 * reminder events..
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class RemindersAction implements Action {

    /** An instance of the Logger class which performs logging for the RemindersAction class. */
    private static final Logger logger = LoggerFactory.getLogger(RemindersAction.class);

    /**
     * This method executes the business logic to perform specific tasks on a selected reminder depending on the
     * specified parameters.
     *
     * @param requestContext The context of the request for which the execution is triggered.
     * @return Returns the status that is used to determine the view that should be displayed after execution.
     * @throws SessionIsExpiredException If the session of the user is expired.
     */
    public String execute(RequestContextManager requestContext) throws SessionIsExpiredException, ServerException {
        logger.debug("In RemindersAction");
        Moderator activeModerator = requestContext.retrieveRequestor();
        Object channelIdObject = requestContext.retrieveFromSession("channelId");
        Integer channelId;
        if (channelIdObject instanceof String) {
            channelId = Integer.valueOf((String) channelIdObject);
        } else {
            channelId = (Integer) channelIdObject;
        }

        // Get currently selected reminder.
        Reminder currentReminder = (Reminder) requestContext.retrieveFromSession("currentReminder");
        if (currentReminder != null) {
            String button = requestContext.getRequestParameter("button");
            try {
                if (button != null) {
                    String message;

                    // Task delete:
                    if (button.equals("delete")) {
                        logger.info("Delete {}", currentReminder.toString());
                        new ChannelAPI().deleteReminder(activeModerator.getServerAccessToken(), channelId,
                                currentReminder.getId());
                        message = Translator.getInstance().getText(requestContext.retrieveLocale(),
                                "reminders.delete.success", currentReminder.getTitle());
                        requestContext.storeInSession("editSuccess", message);
                    }

                    // Task activate reminder:
                    if (button.equals("activate")){
                        logger.info("Activate {}", currentReminder.toString());

                        new ChannelAPI().changeActiveStatusOfReminder(activeModerator.getServerAccessToken(),
                                channelId, currentReminder.getId(), true);

                        message = Translator.getInstance().getText(requestContext.retrieveLocale(),
                                "reminders.activation.success", currentReminder.getTitle());
                        requestContext.storeInSession("editSuccess", message);
                    }

                    // Task deactivate reminder:
                    if (button.equals("deactivate")){
                        logger.info("Deactivate {}", currentReminder.toString());

                        new ChannelAPI().changeActiveStatusOfReminder(activeModerator.getServerAccessToken(),
                                channelId, currentReminder.getId(), false);
                    }

                    // Task set ignore flag:
                    if (button.equals("setIgnoreFlag")){
                        logger.info("Set ignore flag on {}", currentReminder.toString());

                        new ChannelAPI().changeIgnoreFlagOfReminder(activeModerator.getServerAccessToken(),
                                channelId, currentReminder.getId(), true);
                    }

                    // Task reset ignore flag:
                    if (button.equals("resetIgnoreFlag")){
                        logger.info("Reset ignore flag on {}", currentReminder.toString());

                        new ChannelAPI().changeIgnoreFlagOfReminder(activeModerator.getServerAccessToken(),
                                channelId, currentReminder.getId(), false);
                    }
                }
            } catch (APIException e) {
                logger.error("Edit application request failed. Error code is {}.", e.getErrorCode());
                String errorMessage;
                switch (e.getErrorCode()) {
                    case Constants.REMINDER_NOT_FOUND:
                        errorMessage = Translator.getInstance().getText(requestContext.retrieveLocale(),
                                "reminder.notfound");
                        break;
                    case Constants.MODERATOR_FORBIDDEN:
                        errorMessage = Translator.getInstance().getText(requestContext.retrieveLocale(),
                                "general.message.error.forbidden");
                        break;
                    default:
                        errorMessage = Translator.getInstance().getText(requestContext.retrieveLocale(),
                                "general.message.error.fatal");
                }

                requestContext.storeInSession("editError", errorMessage);
                return Constants.REMINDERS_EDIT_FAILED;
            }
        }

        return Constants.REMINDERS_EDITED;
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
