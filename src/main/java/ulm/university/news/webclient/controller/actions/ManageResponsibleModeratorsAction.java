package ulm.university.news.webclient.controller.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.api.ChannelAPI;
import ulm.university.news.webclient.controller.context.RequestContextManager;
import ulm.university.news.webclient.controller.interfaces.Action;
import ulm.university.news.webclient.data.Channel;
import ulm.university.news.webclient.data.Moderator;
import ulm.university.news.webclient.util.Constants;
import ulm.university.news.webclient.util.Translator;
import ulm.university.news.webclient.util.exceptions.APIException;
import ulm.university.news.webclient.util.exceptions.ServerException;
import ulm.university.news.webclient.util.exceptions.SessionIsExpiredException;

import java.util.List;
import java.util.Locale;

/**
 * Action that can be used to manage the responsible moderators of a channel. The privileges to
 * modify the channel can be revoked or granted. New moderators can be added as responsible persons for the channel.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class ManageResponsibleModeratorsAction implements Action {
    /** An instance of the Logger class which performs logging for the ManageResponsibleModeratorsAction class. */
    private static final Logger logger = LoggerFactory.getLogger(ManageResponsibleModeratorsAction.class);

    /**
     * This method executes the business logic to change the active status of responsible moderators of a channel.
     * Can also add new moderators as responsible persons for the channel.
     *
     * @param requestContext The context of the request for which the execution is triggered.
     * @return Returns the status that is used to determine the view that should be displayed after execution.
     * @throws SessionIsExpiredException If the session of the user is expired.
     */
    public String execute(RequestContextManager requestContext) throws SessionIsExpiredException, ServerException {
        String status = null;
        Moderator activeModerator = requestContext.retrieveRequestor();

        Moderator selectedModerator = null;
        if (requestContext.retrieveFromSession("selectedModerator") != null) {
            selectedModerator = (Moderator) requestContext.retrieveFromSession("selectedModerator");
        }

        Channel currentChannel = null;
        if (requestContext.retrieveFromSession("currentChannel") != null) {
            currentChannel = (Channel) requestContext.retrieveFromSession("currentChannel");
        }

        ChannelAPI channelAPI = new ChannelAPI();
        String task = requestContext.getRequestParameter("task");
        try {
            if (task != null && selectedModerator != null && currentChannel != null) {
                if (task.equals("revoke")) {
                    // Revoke privileges.
                    channelAPI.revokeModeratorResponsibility(activeModerator.getServerAccessToken(),
                            currentChannel.getId(), selectedModerator.getId());
                    status = Constants.RESPONSIBLE_MODERATORS_REVOKED_PRIVILEGES;
                } else if (task.equals("reactivate")) {
                    // Grant privileges again.
                    channelAPI.addModeratorResponsibility(activeModerator.getServerAccessToken(),
                            currentChannel.getId(), selectedModerator.getName());
                    status = Constants.RESPONSIBLE_MODERATORS_REACTIVATED_STATUS;
                } else if (task.equals("addModerator")) {
                    String enteredModeratorUsername = requestContext.getRequestParameter("usernameModerator");
                    if (enteredModeratorUsername == null) {
                        enteredModeratorUsername = "";
                    }

                    // Send request to add moderator as a responsible person.
                    channelAPI.addModeratorResponsibility(activeModerator.getServerAccessToken(),
                            currentChannel.getId(), enteredModeratorUsername);
                    status = Constants.RESPONSIBLE_MODERATORS_ADDED_MODERATOR;
                }
            }
        } catch (APIException ex) {
            logger.error("Error occurred. Error code: {}, Response code: {}, Message: {}.", ex.getErrorCode(),
                    ex.getStatusCode(), ex.getMessage());

            status = Constants.RESPONSIBLE_MODERATORS_OPERATION_FAILED;

            Translator translator = Translator.getInstance();
            Locale currentLocale = requestContext.retrieveLocale();
            String errorMessage = "";

            switch (ex.getErrorCode()) {
                case Constants.CHANNEL_DATA_INCOMPLETE:
                    errorMessage = translator.getText(currentLocale, "manageChannelModerators.channelDataIncomplete");
                    break;
                case Constants.CHANNEL_NOT_FOUND:
                    errorMessage = translator.getText(currentLocale, "manageChannelModerators.channelNotFound");
                    break;
                case Constants.MODERATOR_NOT_FOUND:
                    if (task.equals("addModerator")) {
                        errorMessage = translator.getText(currentLocale, "manageChannelModerators.moderatorNotFound");
                    } else {
                        errorMessage = translator.getText(currentLocale, "general.message.error.moderatorNotFound");
                    }
                    break;
                case Constants.MODERATOR_FORBIDDEN:
                    if (task.equals("revoke")) {
                        // Request responsible moderators.
                        if (requestContext.retrieveFromSession("responsibleModerators") != null) {
                            @SuppressWarnings("unchecked")
                            List<Moderator> responsibleModerators = (List<Moderator>)
                                    requestContext.retrieveFromSession("responsibleModerators");
                            if (responsibleModerators != null && responsibleModerators.size() == 1) {
                                errorMessage = translator.getText(currentLocale,
                                        "manageChannelModerators.moderatorForbidden.moderatorAmount");
                            } else {
                                errorMessage = translator.getText(currentLocale, "general.message.error.forbidden");
                            }
                        }
                    } else {
                        errorMessage = translator.getText(currentLocale, "general.message.error.forbidden");
                    }
                    break;
                case Constants.DATABASE_FAILURE:
                    errorMessage = translator.getText(currentLocale, "general.message.error.databaseFailure");
                    break;
                default:
                    throw new ServerException(ex.getErrorCode(), "Unexpected error occurred.");
            }

            // Add to request context.
            requestContext.addToRequestContext("managingModeratorsOperationFailed", errorMessage);
        }

        // Force reload of manage moderator page.
        requestContext.storeInSession("selectedModerator", null);
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
