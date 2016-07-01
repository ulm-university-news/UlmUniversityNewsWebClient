package ulm.university.news.webclient.controller.actions;

import ulm.university.news.webclient.api.ChannelAPI;
import ulm.university.news.webclient.controller.context.RequestContextManager;
import ulm.university.news.webclient.controller.interfaces.Action;
import ulm.university.news.webclient.data.Announcement;
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
 * Action that realizes the loading process of the channel details and announcements of the selected channel.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class LoadAnnouncementsAction implements Action {
    /**
     * This method executes the business logic to load channel details and announcements of the selected channel.
     *
     * @param requestContext The context of the request for which the execution is triggered.
     * @return Returns the status that is used to determine the view that should be displayed after execution.
     * @throws SessionIsExpiredException If the session of the user is expired.
     */
    public String execute(RequestContextManager requestContext) throws SessionIsExpiredException, ServerException {
        String status = Constants.ANNOUNCEMENTS_DATA_LOADED;

        Moderator activeModerator = requestContext.retrieveRequestor();
        Channel selectedChannel = (Channel) requestContext.retrieveFromSession("currentChannel");
        try {
            if (activeModerator != null && selectedChannel != null) {
                ChannelAPI channelAPI = new ChannelAPI();
                // Retrieve announcements.
                List<Announcement> announcementList = channelAPI.getAnnouncementsOfChannel(
                        activeModerator.getServerAccessToken(), selectedChannel.getId(), 0);

                // Store announcements in session.
                requestContext.storeInSession("announcements", announcementList);
            }
        } catch (APIException ex) {
            Translator translator = Translator.getInstance();
            Locale currentLocale = requestContext.retrieveLocale();
            String errorMessage;

            status = Constants.CHANNEL_DETAILS_LOADING_FAILED;

            switch (ex.getErrorCode()){
                case Constants.CHANNEL_NOT_FOUND:
                    errorMessage = translator.getText(currentLocale, "general.message.error.channelNotFound");
                    break;
                case Constants.MODERATOR_FORBIDDEN:
                    errorMessage = translator.getText(currentLocale, "general.message.error.forbidden");
                    break;
                default:
                    throw new ServerException(ex.getErrorCode(), "Failed to load channel details.");
            }

            // Add error message to request context.
            requestContext.addToRequestContext("loadingAnnouncementsFailure", errorMessage);
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
        return false;
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
