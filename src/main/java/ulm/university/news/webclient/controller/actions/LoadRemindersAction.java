package ulm.university.news.webclient.controller.actions;

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

import java.util.List;

/**
 * A class which is used to load reminders of a channel from the server.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class LoadRemindersAction implements Action {
    /** An instance of the Logger class which performs logging for the LoadRemindersAction class. */
    private static final Logger logger = LoggerFactory.getLogger(LoadRemindersAction.class);

    /**
     * This method executes the business logic to load reminders of a channel from the server.
     *
     * @param requestContext The context of the request for which the execution is triggered.
     * @return Returns the status that is used to determine the view that should be displayed after execution.
     * @throws SessionIsExpiredException If the session of the user is expired.
     */
    public String execute(RequestContextManager requestContext) throws SessionIsExpiredException, ServerException {
        // Get reminders via REST Server.
        List<Reminder> reminders;
        Moderator activeModerator = requestContext.retrieveRequestor();
        Object channelIdObject = requestContext.retrieveFromSession("channelId");
        Integer channelId;
        if (channelIdObject instanceof String) {
            channelId = Integer.valueOf((String) channelIdObject);
        } else {
            channelId = (Integer) channelIdObject;
        }

        // Do not load reminders again if the client clicks on list entries.
        if (channelId != null && requestContext.getRequestParameter("reminderId") == null) {
            try {
                // Request reminders for the currently selected channel.
                reminders = new ChannelAPI().getReminders(activeModerator.getServerAccessToken(), channelId);
                for (Reminder reminder : reminders) {
                    reminder.computeFirstNextDate();
                }
                // Store reminder data in session for later reuse.
                requestContext.storeInSession("reminders", reminders);
            } catch (APIException e) {
                logger.error("Reminder request failed. Error code is {}.", e.getErrorCode());
                String errorMessage;
                switch (e.getErrorCode()) {
                    case Constants.MODERATOR_FORBIDDEN:
                        errorMessage = Translator.getInstance().getText(requestContext.retrieveLocale(),
                                "general.message.error.forbidden");
                        break;
                    default:
                        errorMessage = Translator.getInstance().getText(requestContext.retrieveLocale(),
                                "general.message.error.fatal");
                }

                requestContext.storeInSession("loadError", errorMessage);
                return Constants.REMINDERS_LOAD_FAILED;
            }
        }

        return Constants.REMINDERS_LOADED;
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
        return true;
    }
}
