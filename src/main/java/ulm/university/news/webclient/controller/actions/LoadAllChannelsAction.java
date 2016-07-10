package ulm.university.news.webclient.controller.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.api.ChannelAPI;
import ulm.university.news.webclient.controller.context.RequestContextManager;
import ulm.university.news.webclient.controller.interfaces.Action;
import ulm.university.news.webclient.data.Channel;
import ulm.university.news.webclient.data.Lecture;
import ulm.university.news.webclient.data.Moderator;
import ulm.university.news.webclient.data.enums.ChannelType;
import ulm.university.news.webclient.util.ChannelUtil;
import ulm.university.news.webclient.util.Constants;
import ulm.university.news.webclient.util.Translator;
import ulm.university.news.webclient.util.exceptions.APIException;
import ulm.university.news.webclient.util.exceptions.ServerException;
import ulm.university.news.webclient.util.exceptions.SessionIsExpiredException;

import java.util.List;
import java.util.Locale;

/**
 * Action that realizes the loading process for all channels in the system.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class LoadAllChannelsAction implements Action {
    /** The logger instance for the LoadAllChannelsAction. */
    private static final Logger logger = LoggerFactory.getLogger(LoadAllChannelsAction.class);

    /**
     * This method executes the business logic to load all channels that exist in the system.
     *
     * @param requestContext The context of the request for which the execution is triggered.
     * @return Returns the status that is used to determine the view that should be displayed after execution.
     * @throws SessionIsExpiredException If the session of the user is expired.
     */
    @SuppressWarnings("unchecked")
    public String execute(RequestContextManager requestContext) throws SessionIsExpiredException, ServerException {
        String status = Constants.ALL_CHANNELS_LOADED;
        ChannelAPI channelAPI = new ChannelAPI();

        Moderator activeModerator = requestContext.retrieveRequestor();

        if (requestContext.getRequestParameter("channelId") == null) {
            logger.debug("Loading all channels.");
            List<Channel> allChannels = null;
            if (activeModerator != null) {
                try{
                    allChannels = channelAPI.getAllChannels(activeModerator.getServerAccessToken());
                    ChannelUtil.sortChannelsByName(allChannels);
                } catch (APIException ex) {
                    logger.error("Request failed. Error code is {}.", ex.getErrorCode());
                    status = Constants.ALL_CHANNELS_LOADING_FAILED;

                    Translator translator = Translator.getInstance();
                    Locale currentLocale = requestContext.retrieveLocale();
                    String errorMessage;

                    switch (ex.getErrorCode()){
                        case Constants.MODERATOR_FORBIDDEN:
                            errorMessage = translator.getText(currentLocale, "general.message.error.forbidden");
                            break;
                        case Constants.MODERATOR_UNAUTHORIZED:
                            errorMessage = translator.getText(currentLocale, "general.message.error.requiresLogin");
                            break;
                        default:
                            throw new ServerException(ex.getErrorCode(), "Failed to load all channels.");
                    }

                    requestContext.addToRequestContext("allChannelsLoadingFailed", errorMessage);
                }
            }

            if (allChannels != null && allChannels.size() > 0) {
                // First channel will be selected at the beginning.
                // Set the localized type string.
                allChannels.get(0).setLocalizedTypeString(requestContext.retrieveLocale());
                // If it is a lecture, set the faculty string field.
                if (allChannels.get(0).getType() == ChannelType.LECTURE){
                    Lecture lecture = (Lecture) allChannels.get(0);
                    lecture.setFacultyAsLocalizedString(requestContext.retrieveLocale());
                }

                requestContext.storeInSession("allChannels", allChannels);
            }
        } else {
            logger.debug("Specific channel selected.");
            // Specific channel is loaded.
            int channelId = -1;
            try{
                channelId = Integer.parseInt(requestContext.getRequestParameter("channelId"));
            } catch (NumberFormatException ex) {
                logger.warn("Invalid argument passed as parameter string.");
            }

            // Extract current channel.
            List<Channel> channels;
            if (requestContext.retrieveFromSession("allChannels") != null) {
                channels = (List<Channel>)requestContext.retrieveFromSession("allChannels");
                if (channels != null) {
                    Channel currentChannel = null;
                    for (Channel channel : channels) {
                        if (channel.getId() == channelId) {
                            currentChannel = channel;
                            break;
                        }
                    }
                    if (currentChannel != null) {
                        // Set localized type.
                        currentChannel.setLocalizedTypeString(requestContext.retrieveLocale());

                        // Check if current channel is lecture.
                        if (currentChannel.getType() == ChannelType.LECTURE) {
                            Lecture lecture = (Lecture) currentChannel;
                            // Set localized faculty.
                            lecture.setFacultyAsLocalizedString(requestContext.retrieveLocale());
                        }
                    }
                }
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
        return true;
    }
}
