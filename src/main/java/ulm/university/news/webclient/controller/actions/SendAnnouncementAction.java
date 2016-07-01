package ulm.university.news.webclient.controller.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.api.ChannelAPI;
import ulm.university.news.webclient.controller.context.RequestContextManager;
import ulm.university.news.webclient.controller.interfaces.Action;
import ulm.university.news.webclient.data.Announcement;
import ulm.university.news.webclient.data.Channel;
import ulm.university.news.webclient.data.Moderator;
import ulm.university.news.webclient.data.enums.Priority;
import ulm.university.news.webclient.util.Constants;
import ulm.university.news.webclient.util.Translator;
import ulm.university.news.webclient.util.exceptions.APIException;
import ulm.university.news.webclient.util.exceptions.ServerException;
import ulm.university.news.webclient.util.exceptions.SessionIsExpiredException;

import java.util.Locale;

/**
 * An implementation of the Action interface that can be used to create a new announcement in a specified channel.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class SendAnnouncementAction implements Action {

    /** The request context of the current request. */
    private RequestContextManager requestContext;

    /** An instance of the Logger class which performs logging for the SendAnnouncementAction class. */
    private static final Logger logger = LoggerFactory.getLogger(SendAnnouncementAction.class);

    /**
     * This method executes the business logic to create a new announcement that will be distributed to
     * all subscribers of the affected channel-
     *
     * @param requestContext The context of the request for which the execution is triggered.
     * @return Returns the status that is used to determine the view that should be displayed after execution.
     * @throws SessionIsExpiredException If the session of the user is expired.
     */
    public String execute(RequestContextManager requestContext) throws SessionIsExpiredException, ServerException {
        String status = Constants.CHANNEL_DETAILS_ANNOUNCEMENT_CREATED;
        this.requestContext = requestContext;

        Moderator activeModerator = requestContext.retrieveRequestor();
        Channel currentChannel = (Channel) requestContext.retrieveFromSession("currentChannel");

        if (activeModerator != null && currentChannel != null) {
            // Extract request parameters.
            String priority = requestContext.getRequestParameter("priorityValue");
            String text = requestContext.getRequestParameter("announcementText");
            String title = requestContext.getRequestParameter("announcementTitle");

            boolean validateData = validateAnnouncementParameters(title, text);
            if (!validateData) {
                status = Constants.CHANNEL_DETAILS_ANNOUNCEMENT_VALIDATION_ERROR;
            } else {
                // Parse priority value.
                Priority priorityValue = Priority.NORMAL;
                if (priority.equals("normal")) {
                    priorityValue = Priority.NORMAL;
                } else if (priority.equals("high")) {
                    priorityValue = Priority.HIGH;
                }

                // Create announcement object with data.
                Announcement announcement = new Announcement();
                announcement.setTitle(title);
                announcement.setText(text);
                announcement.setPriority(priorityValue);

                ChannelAPI channelAPI = new ChannelAPI();
                try {
                    // Send create announcement request.
                    channelAPI.sendAnnouncement(activeModerator.getServerAccessToken(), currentChannel.getId(),
                            announcement);
                } catch (APIException ex) {
                    Translator translator = Translator.getInstance();
                    Locale currentLocale = requestContext.retrieveLocale();
                    String errorMessage;

                    status = Constants.CHANNEL_DETAILS_ANNOUNCEMENT_CREATION_FAILED;

                    switch (ex.getErrorCode()) {
                        case Constants.ANNOUNCEMENT_DATA_INCOMPLETE:
                            errorMessage = translator.getText(currentLocale,
                                    "channelDetails.createAnnouncement.error.generalValidationError");
                            setValidationError(Constants.ANNOUNCEMENT_DATA_INCOMPLETE, errorMessage);
                            break;
                        case Constants.ANNOUNCEMENT_INVALID_TITLE:
                            errorMessage = translator.getText(currentLocale,
                                    "channelDetails.createAnnouncement.error.titleInvalid");
                            setValidationError(Constants.ANNOUNCEMENT_INVALID_TITLE, errorMessage);
                            break;
                        case Constants.ANNOUNCEMENT_INVALID_TEXT:
                            errorMessage = translator.getText(currentLocale,
                                    "channelDetails.createAnnouncement.error.textInvalid");
                            setValidationError(Constants.ANNOUNCEMENT_INVALID_TEXT, errorMessage);
                            break;
                        case Constants.CHANNEL_NOT_FOUND:
                            errorMessage = translator.getText(currentLocale, "general.message.error.channelNotFound");
                            break;
                        case Constants.MODERATOR_FORBIDDEN:
                            errorMessage = translator.getText(currentLocale, "general.message.error.forbidden");
                            break;
                        case Constants.CONNECTION_FAILURE:
                            errorMessage = translator.getText(currentLocale, "general.message.error.connectionFailure");
                            break;
                        default:
                            throw new ServerException(ex.getErrorCode(), "Failed to load channel details.");
                    }

                    // Add error to request context.
                    requestContext.addToRequestContext("channelDetailsCreateAnnouncementError", errorMessage);
                }
            }
        }

        return status;
    }

    /**
     * Executes the validation of the entered data. Returns the status of the validation.
     *
     * @param title The entered title of the announcement.
     * @param text The entered announcement text.
     * @return Returns true if the validation was successful, otherwise false.
     */
    private boolean validateAnnouncementParameters(String title,  String text) {
        Translator translator = Translator.getInstance();
        boolean status = true;

        logger.debug("title: {} and text: {}", title, text);

        if (title == null || title.trim().length() == 0) {
            logger.debug("Validation error regarding announcement title.");
            status = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "channelDetails.dialog.newMessage.validation.announcementTitle.empty");
            setValidationError(Constants.ANNOUNCEMENT_INVALID_TITLE, errorMsg);
        }

        if (text == null || text.trim().length() == 0) {
            logger.debug("Validation error regarding announcement text.");
            status = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "channelDetails.dialog.newMessage.validation.announcementText.empty");
            setValidationError(Constants.ANNOUNCEMENT_INVALID_TEXT, errorMsg);
        }

        if (title != null && title.length() > Constants.ANNOUNCEMENT_TITLE_MAX_LENGTH) {
            logger.debug("Validation error regarding length of title.");
            status = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "channelDetails.dialog.newMessage.validation.announcementTitle.length");
            setValidationError(Constants.ANNOUNCEMENT_INVALID_TITLE, errorMsg);
        }

        if (text != null && text.length() > Constants.MESSAGE_MAX_LENGTH) {
            logger.debug("Validation error regarding announcement text length.");
            status = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "channelDetails.dialog.newMessage.validation.announcementText.length");
            setValidationError(Constants.ANNOUNCEMENT_INVALID_TEXT, errorMsg);
        }

        return status;
    }

    /**
     * Adds validation errors to the request context based on the error code.
     *
     * @param errorCode The error code of the validation error.
     */
    private void setValidationError (int errorCode, String errorMessage)
    {
        switch (errorCode){
            case Constants.ANNOUNCEMENT_DATA_INCOMPLETE:
                // Add to both
                requestContext.addToRequestContext("announcementTitleValidationError", errorMessage);
                requestContext.addToRequestContext("announcementTextValidationError", errorMessage);
                break;
            case Constants.ANNOUNCEMENT_INVALID_TITLE:
                // Add validation error for announcement title.
                requestContext.addToRequestContext("announcementTitleValidationError", errorMessage);
                break;
            case Constants.ANNOUNCEMENT_INVALID_TEXT:
                requestContext.addToRequestContext("announcementTextValidationError", errorMessage);
                break;
            default:
                break;
        }
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
