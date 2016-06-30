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

import java.util.Locale;

/**
 * Action that handles incoming POST requests for the myChannels view. Offers functionality to
 * delete existing channels.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class MyChannelsAction implements Action {

    /** The logger instance for the MyChannelsAction. */
    private static final Logger logger = LoggerFactory.getLogger(MyChannelsAction.class);

    /**
     * This method executes the business logic to handle incoming POST requests for the myChannels view.
     *
     * @param requestContext The context of the request for which the execution is triggered.
     * @return Returns the status that is used to determine the view that should be displayed after execution.
     * @throws SessionIsExpiredException If the session of the user is expired.
     */
    public String execute(RequestContextManager requestContext) throws SessionIsExpiredException, ServerException {
        String task = requestContext.getRequestParameter("task");
        String status = null;

        Moderator activeModerator = requestContext.retrieveRequestor();
        Channel selectedChannel = (Channel) requestContext.retrieveFromSession("currentChannel");
        try {
            if (selectedChannel != null) {
                if (task != null && task.equals("delete")) {
                    int channelId = selectedChannel.getId();
                    logger.debug("Delete channel with id {}.", channelId);

                    ChannelAPI channelAPI = new ChannelAPI();
                    channelAPI.deleteChannel(activeModerator.getServerAccessToken(), channelId);
                    status = Constants.MY_CHANNELS_DELETED_CHANNEL;
                }
            }
        } catch (APIException ex) {
            logger.error("Error occurred. Error code: {}, Response code: {}, Message: {}.", ex.getErrorCode(),
                    ex.getStatusCode(), ex.getMessage());

            status = Constants.MY_CHANNELS_OPERATION_FAILED;

            Translator translator = Translator.getInstance();
            Locale currentLocale = requestContext.retrieveLocale();
            String errorMessage;

            switch (ex.getErrorCode()) {
                case Constants.CHANNEL_NOT_FOUND:
                    errorMessage = translator.getText(currentLocale, "general.message.error.channelNotFound");
                    break;
                case Constants.MODERATOR_FORBIDDEN:
                    errorMessage = translator.getText(currentLocale, "general.message.error.moderatorNotFound");
                    break;
                default:
                    throw new ServerException(ex.getErrorCode(), "Unexpected error occurred.");
            }

            requestContext.addToRequestContext("myChannelsOperationFailure", errorMessage);
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
