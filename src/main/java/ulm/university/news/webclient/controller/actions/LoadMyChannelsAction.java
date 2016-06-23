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

/**
 * The action is responsible for loading all the channels that are managed by the requestor.
 *
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class LoadMyChannelsAction implements Action {
    /** An instance of the Logger class which performs logging for the LoadMyChannelsAction class. */
    private static final Logger logger = LoggerFactory.getLogger(LoadMyChannelsAction.class);

    /**
     * This method executes the business logic to load the channels that are managed
     * by the requestor.
     *
     * @param requestContext The context of the request for which the execution is triggered.
     * @return Returns the status that is used to determine the view that should be displayed after execution.
     * @throws SessionIsExpiredException If the session of the user is expired.
     */
    @SuppressWarnings("unchecked")
    public String execute(RequestContextManager requestContext) throws SessionIsExpiredException, ServerException {
        String status = Constants.MY_CHANNELS_LOADED;
        List<Channel> myChannels;
        Moderator activeModerator = requestContext.retrieveRequestor();

        if (requestContext.getRequestParameter("channelId") == null) {
            // Request managed channels from server.
            ChannelAPI channelAPI = new ChannelAPI();
            try{
                myChannels = channelAPI.getMyChannels(activeModerator.getServerAccessToken(),activeModerator.getId());
                ChannelUtil.sortChannelsByName(myChannels);

                // The first channel will be shown at the beginning.
                if (myChannels != null && myChannels.size() > 0) {
                    // Set the localized type string.
                    myChannels.get(0).setLocalizedTypeString(requestContext.retrieveLocale());
                    // If it is a lecture, set the faculty string field.
                    if (myChannels.get(0).getType() == ChannelType.LECTURE){
                        Lecture lecture = (Lecture) myChannels.get(0);
                        lecture.setFacultyAsLocalizedString(requestContext.retrieveLocale());
                    }
                }

                // Add to session context.
                requestContext.storeInSession("myChannels", myChannels);
            } catch (APIException ex) {
                String errorMessage;
                switch (ex.getErrorCode()) {
                    case Constants.MODERATOR_FORBIDDEN:
                        errorMessage = Translator.getInstance().getText(requestContext.retrieveLocale(),
                                "general.message.error.forbidden");
                        break;
                    default:
                        errorMessage = Translator.getInstance().getText(requestContext.retrieveLocale(),
                                "general.message.error.fatal");
                }

                requestContext.addToRequestContext("myChannelsLoadingFailure", errorMessage);
                status = Constants.MY_CHANNELS_LOAD_FAILED;
            }
        } else {
            // Specific channel is loaded.
            int channelId = -1;
            try{
               channelId = Integer.parseInt(requestContext.getRequestParameter("channelId"));
            } catch (NumberFormatException ex) {
                logger.warn("Invalid argument passed as parameter string.");
            }

            // Extract current channel.
            List<Channel> channels;
            if (requestContext.retrieveFromSession("myChannels") != null) {
                channels = (List<Channel>)requestContext.retrieveFromSession("myChannels");
                if (channels != null) {
                    Channel currentChannel = null;
                    for (int i=0; i < channels.size(); i++) {
                        if (channels.get(i).getId() == channelId) {
                            currentChannel = channels.get(i);
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
        return false;
    }
}
