package ulm.university.news.webclient.controller.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.api.ChannelAPI;
import ulm.university.news.webclient.controller.context.RequestContextManager;
import ulm.university.news.webclient.controller.interfaces.Action;
import ulm.university.news.webclient.data.Channel;
import ulm.university.news.webclient.data.Moderator;
import ulm.university.news.webclient.util.Constants;
import ulm.university.news.webclient.util.ModeratorUtil;
import ulm.university.news.webclient.util.Translator;
import ulm.university.news.webclient.util.exceptions.APIException;
import ulm.university.news.webclient.util.exceptions.ServerException;
import ulm.university.news.webclient.util.exceptions.SessionIsExpiredException;

import java.util.List;
import java.util.Locale;

/**
 * The action is responsible for loading responsible moderators for a certain channel.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class LoadResponsibleModeratorsAction implements Action {
    /** An instance of the Logger class which performs logging for the LoadResponsibleModeratorsAction class. */
    private static final Logger logger = LoggerFactory.getLogger(LoadResponsibleModeratorsAction.class);

    /**
     * This method executes the business logic to load a the responsible moderators for a specific channel.
     *
     * @param requestContext The context of the request for which the execution is triggered.
     * @return Returns the status that is used to determine the view that should be displayed after execution.
     * @throws SessionIsExpiredException If the session of the user is expired.
     */
    public String execute(RequestContextManager requestContext) throws SessionIsExpiredException, ServerException {
        String status = Constants.RESPONSIBLE_MODERATORS_LOADED;
        Moderator activeModerator = requestContext.retrieveRequestor();

        // Get the selected channel.
        Channel currentChannel = null;
        if (requestContext.retrieveFromSession("currentChannel") != null) {
            currentChannel = (Channel) requestContext.retrieveFromSession("currentChannel");
        }

        // Perform the loading operation.
        if (currentChannel != null && requestContext.getRequestParameter("moderatorId") == null) {
            try{
                ChannelAPI channelAPI = new ChannelAPI();
                List<Moderator> responsibleModerators = channelAPI.getResponsibleModerators(
                        activeModerator.getServerAccessToken(), currentChannel.getId());

                // Sort and add to session context.
                ModeratorUtil.sortModeratorsName(responsibleModerators);
                requestContext.storeInSession("responsibleModerators", responsibleModerators);

            } catch (APIException ex) {
                status = Constants.RESPONSIBLE_MODERATORS_LOAD_FAILED;

                if (ex.getErrorCode() == Constants.MODERATOR_UNAUTHORIZED) {
                    // There seems to be something wrong with the moderator's access token.
                    // Reauthorize.
                    throw new SessionIsExpiredException("Reauthorize");
                }
                else if (ex.getErrorCode() == Constants.MODERATOR_FORBIDDEN) {
                    setErrorMessage(requestContext, "general.message.error.forbidden");
                }
                else if (ex.getErrorCode() == Constants.MODERATOR_NOT_FOUND) {
                    setErrorMessage(requestContext, "manageChannelModerators.moderatorNotFound");
                }
                else if (ex.getErrorCode() == Constants.CHANNEL_NOT_FOUND) {
                    setErrorMessage(requestContext, "manageChannelModerators.channelNotFound");
                }
            }
        } else {
            logger.debug("Specific moderator selected. No need to load responsible moderators again.");
        }

        return status;
    }

    /**
     * Retrieves a localized error message using the specified key and stores the error message in the
     * request context.
     *
     * @param contextManager The request context for this request.
     * @param errorMessageKey The error message key for the retrieval of a localized error message.
     */
    private void setErrorMessage(RequestContextManager contextManager, String errorMessageKey) {
        Translator translator = Translator.getInstance();
        String errorMsg = translator.getText(contextManager.retrieveLocale(), errorMessageKey);
        contextManager.addToRequestContext("loadingResponsibleModeratorsFailure", errorMsg);
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
