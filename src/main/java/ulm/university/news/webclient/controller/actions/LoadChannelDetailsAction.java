package ulm.university.news.webclient.controller.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.api.ChannelAPI;
import ulm.university.news.webclient.controller.context.RequestContextManager;
import ulm.university.news.webclient.controller.interfaces.Action;
import ulm.university.news.webclient.data.Channel;
import ulm.university.news.webclient.data.Moderator;
import ulm.university.news.webclient.util.Constants;
import ulm.university.news.webclient.util.exceptions.APIException;
import ulm.university.news.webclient.util.exceptions.ServerException;
import ulm.university.news.webclient.util.exceptions.SessionIsExpiredException;

/**
 * Implementation of the action interface in order to load the data of the affected channel and provide it to the view.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class LoadChannelDetailsAction implements Action {
    /** An instance of the Logger class which performs logging for the LoadChannelDetailsAction class. */
    private static final Logger logger = LoggerFactory.getLogger(LoadChannelDetailsAction.class);

    /**
     * This method executes the business logic to load the data for the selected channel and provide it to the view
     * in order to fill the data input fields.
     *
     * @param requestContext The context of the request for which the execution is triggered.
     * @return Returns the status that is used to determine the view that should be displayed after execution.
     * @throws SessionIsExpiredException If the session of the user is expired.
     */
    public String execute(RequestContextManager requestContext) throws SessionIsExpiredException, ServerException {
        String status = Constants.CHANNEL_DETAILS_LOADED;

        Moderator activeModerator = requestContext.retrieveRequestor();
        Channel currentChannel = (Channel) requestContext.retrieveFromSession("currentChannel");

        if (activeModerator != null && currentChannel != null) {
            // Request the channel details to make sure we have the most up to date data.
            try {
                ChannelAPI channelAPI = new ChannelAPI();
                Channel editableChannel = channelAPI.getChannel(activeModerator.getServerAccessToken(),
                        currentChannel.getId());

                // Add the channel to the request context.
                requestContext.addToRequestContext("editableChannel", editableChannel);
            } catch (APIException ex) {
                logger.debug("Error occurred. Error code is {}.", ex.getErrorCode());

                status = Constants.CHANNEL_DETAILS_LOADING_FAILED;
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
