package ulm.university.news.webclient.controller.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.controller.context.RequestContextManager;
import ulm.university.news.webclient.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public abstract class RequestDispatcher {

    /** An instance of the Logger class which performs logging for the RequestDispatcher class. */
    private static final Logger logger = LoggerFactory.getLogger(RequestDispatcher.class);

    /** Maps status responses that have been returned by Action objects processing GET requests. */
    private static ConcurrentHashMap<String, String> _getRequestStatusMapping;

    /** Maps status responses that have been returned by Action objects processing POST requests. */
    private static ConcurrentHashMap<String, String> _postRequestStatusMapping;

    /**
     * Maps status responses that have been returned by Action objects to a forwarding status which
     * determines whether the dispatcher should perform a forwarding or a redirect.
     */
    private static ConcurrentHashMap<String, Boolean> _postForwardingStatusMapping;

    /**
     * Initializes the data structures realizing the mappings.
     */
    public static void initialize() {
        _getRequestStatusMapping = new ConcurrentHashMap<String, String>();
        _postRequestStatusMapping = new ConcurrentHashMap<String, String>();
        _postForwardingStatusMapping = new ConcurrentHashMap<String, Boolean>();

        // GET requests.
        // Welcome page.
        _getRequestStatusMapping.put("/welcome:" + Constants.LOAD_INDEX, "index");

        // Login page.
        _getRequestStatusMapping.put("/login:" + Constants.LOGGED_IN, "index"); // Navigate him back to index.
        _getRequestStatusMapping.put("/login:" + Constants.LOGGED_OUT, "login");

        // Register page.
        _getRequestStatusMapping.put("/register:" + Constants.LOGGED_IN, "index"); // Navigate him back to index.
        _getRequestStatusMapping.put("/register:" + Constants.LOGGED_OUT, "register");

        // Password reset page.
        _getRequestStatusMapping.put("/passwordReset:" + Constants.LOGGED_IN, "index"); // Navigate him back to index.
        _getRequestStatusMapping.put("/passwordReset:" + Constants.LOGGED_OUT, "passwordReset");

        // Index/Main page.
        _getRequestStatusMapping.put("/index:" + Constants.LOGGED_OUT, "index");
        _getRequestStatusMapping.put("/index:" + Constants.LOGGED_IN, "index");

        // Applications page.
        _getRequestStatusMapping.put("/applications:" + Constants.APPLICATIONS_LOADED, "applications");
        _getRequestStatusMapping.put("/applications:" + Constants.APPLICATIONS_LOAD_FAILED, "applications");

        // Accounts page.
        _getRequestStatusMapping.put("/accounts:" + Constants.ACCOUNTS_LOADED, "accounts");
        _getRequestStatusMapping.put("/accounts:" + Constants.ACCOUNTS_LOAD_FAILED, "accounts");

        // MyAccount page.
        _getRequestStatusMapping.put("/myAccount:" + Constants.MY_ACCOUNT_LOADED, "myAccount");
        _getRequestStatusMapping.put("/myAccount:" + Constants.MY_ACCOUNT_LOAD_FAILED, "myAccount");

        // MyAccountEdit page.
        _getRequestStatusMapping.put("/myAccountEdit:" + Constants.MY_ACCOUNT_EDIT_LOADED, "myAccountEdit");
        _getRequestStatusMapping.put("/myAccountEdit:" + Constants.MY_ACCOUNT_EDIT_LOAD_FAILED, "myAccountEdit");

        // Groups page.
        _getRequestStatusMapping.put("/groups:" + Constants.GROUPS_LOADED, "groups");
        _getRequestStatusMapping.put("/groups:" + Constants.GROUPS_LOAD_FAILED, "groups");

        // My channels page.
        _getRequestStatusMapping.put("/myChannels:" + Constants.MY_CHANNELS_LOADED, "myChannels");
        _getRequestStatusMapping.put("/myChannels:" + Constants.MY_CHANNELS_LOAD_FAILED, "myChannels");

        // Announcements page.
        _getRequestStatusMapping.put("/announcements:" + Constants.ANNOUNCEMENTS_DATA_LOADED, "announcements");
        _getRequestStatusMapping.put("/announcements:" + Constants.ANNOUNCEMENTS_LOADING_FAILED, "announcements");

        // Channel details page.
        _getRequestStatusMapping.put("/channelDetails:" + Constants.CHANNEL_DETAILS_LOADED, "channelDetails");
        _getRequestStatusMapping.put("/channelDetails:" + Constants.CHANNEL_DETAILS_LOADING_FAILED, "channelDetails");

        // Create channel page.
        _getRequestStatusMapping.put("/createChannel:" + Constants.CREATE_CHANNEL_DIALOG_LOADED, "createChannel");

        // All channels page.
        _getRequestStatusMapping.put("/channels:" + Constants.ALL_CHANNELS_LOADED, "channels");
        _getRequestStatusMapping.put("/channels:" + Constants.ALL_CHANNELS_LOADING_FAILED, "channels");

        // Responsible moderators page.
        _getRequestStatusMapping.put("/manageChannelModerators:" + Constants.RESPONSIBLE_MODERATORS_LOADED,
                "manageChannelModerators");
        _getRequestStatusMapping.put("/manageChannelModerators:" + Constants.RESPONSIBLE_MODERATORS_LOAD_FAILED,
                "manageChannelModerators");

        // Reminders page.
        _getRequestStatusMapping.put("/reminders:" + Constants.REMINDERS_LOADED, "reminders");
        _getRequestStatusMapping.put("/reminders:" + Constants.REMINDERS_LOAD_FAILED, "reminders");

        // Create reminder dialog.
        _getRequestStatusMapping.put("/reminderCreate:" + Constants.CREATE_REMINDER_DIALOG_LOADED, "createReminder");

        // Edit reminder dialog.
        _getRequestStatusMapping.put("/reminderEdit:" + Constants.EDIT_REMINDER_DIALOG_LOADED, "editReminder");
        _getRequestStatusMapping.put("/reminderEdit:" + Constants.EDIT_REMINDER_DIALOG_LOADING_FAILED, "reminders");

        // POST requests.
        // Login page.
        _postRequestStatusMapping.put("/login:" + Constants.LOGIN_SUCCESSFUL, "index");
        _postRequestStatusMapping.put("/login:" + Constants.LOGIN_FAILED, "login");
        _postForwardingStatusMapping.put("/login:" + Constants.LOGIN_SUCCESSFUL, false);
        _postForwardingStatusMapping.put("/login:" + Constants.LOGIN_FAILED, true);

        // Logout.
        _postRequestStatusMapping.put("/logout:" + Constants.LOGGED_OUT, "index");
        _postForwardingStatusMapping.put("/logout:" + Constants.LOGGED_OUT, false);

        // Register page.
        _postRequestStatusMapping.put("/register:" + Constants.VALIDATION_FAILED, "register");
        _postForwardingStatusMapping.put("/register:" + Constants.VALIDATION_FAILED, true);
        _postRequestStatusMapping.put("/register:" + Constants.REGISTRATION_SUCCESSFUL, "register.jsp?successful=true");
        _postForwardingStatusMapping.put("/register:" + Constants.REGISTRATION_SUCCESSFUL, false);  // Redirect
        _postRequestStatusMapping.put("/register:" + Constants.CONNECTION_FAILED_STATUS, "register");
        _postForwardingStatusMapping.put("/register:" + Constants.CONNECTION_FAILED_STATUS, true);

        // Password reset page.
        _postRequestStatusMapping.put("/passwordReset:" + Constants.PASSWORD_RESET_SUCCESSFUL,
                "passwordReset?successful=true");
        _postForwardingStatusMapping.put("/passwordReset:" + Constants.PASSWORD_RESET_SUCCESSFUL, false);  // Redirect.
        _postRequestStatusMapping.put("/passwordReset:" + Constants.PASSWORD_RESET_FAILED, "passwordReset");
        _postForwardingStatusMapping.put("/passwordReset:" + Constants.PASSWORD_RESET_FAILED, true);

        // Applications page.
        _postRequestStatusMapping.put("/applications:" + Constants.APPLICATIONS_EDIT_FAILED, "applications");
        _postForwardingStatusMapping.put("/applications:" + Constants.APPLICATIONS_EDIT_FAILED, true);
        _postRequestStatusMapping.put("/applications:" + Constants.APPLICATIONS_EDITED, "applications");
        _postForwardingStatusMapping.put("/applications:" + Constants.APPLICATIONS_EDITED, false);

        // Accounts page.
        _postRequestStatusMapping.put("/accounts:" + Constants.ACCOUNTS_EDIT_FAILED, "accounts");
        _postForwardingStatusMapping.put("/accounts:" + Constants.ACCOUNTS_EDIT_FAILED, true);
        _postRequestStatusMapping.put("/accounts:" + Constants.ACCOUNTS_EDITED, "accounts");
        _postForwardingStatusMapping.put("/accounts:" + Constants.ACCOUNTS_EDITED, false);

        // MyAccount page.
        _postRequestStatusMapping.put("/myAccount:" + Constants.MY_ACCOUNT_DELETE_FAILED, "myAccount");
        _postForwardingStatusMapping.put("/myAccount:" + Constants.MY_ACCOUNT_DELETE_FAILED, true);
        _postRequestStatusMapping.put("/myAccount:" + Constants.MY_ACCOUNT_DELETED, "index");
        _postForwardingStatusMapping.put("/myAccount:" + Constants.MY_ACCOUNT_DELETED, false);

        // MyAccountEdit page.
        _postRequestStatusMapping.put("/myAccountEdit:" + Constants.MY_ACCOUNT_EDIT_EDIT_FAILED, "myAccountEdit");
        _postForwardingStatusMapping.put("/myAccountEdit:" + Constants.MY_ACCOUNT_EDIT_EDIT_FAILED, true);
        _postRequestStatusMapping.put("/myAccountEdit:" + Constants.MY_ACCOUNT_EDIT_EDITED, "myAccount");
        _postForwardingStatusMapping.put("/myAccountEdit:" + Constants.MY_ACCOUNT_EDIT_EDITED, false);
        _postRequestStatusMapping.put("/myAccountEdit:" +
                Constants.MY_ACCOUNT_EDIT_PASSWORD_VALIDATION_ERROR, "myAccountEdit");
        _postForwardingStatusMapping.put("/myAccountEdit:" +
                Constants.MY_ACCOUNT_EDIT_PASSWORD_VALIDATION_ERROR, true);  // forwarding

        // Groups page.
        _postRequestStatusMapping.put("/groups:" + Constants.GROUPS_EDIT_FAILED, "groups");
        _postForwardingStatusMapping.put("/groups:" + Constants.GROUPS_EDIT_FAILED, true);
        _postRequestStatusMapping.put("/groups:" + Constants.GROUPS_EDITED, "groups");
        _postForwardingStatusMapping.put("/groups:" + Constants.GROUPS_EDITED, false);

        // Reminders page.
        _postRequestStatusMapping.put("/reminders:" + Constants.REMINDERS_EDIT_FAILED, "reminders");
        _postForwardingStatusMapping.put("/reminders:" + Constants.REMINDERS_EDIT_FAILED, true);
        _postRequestStatusMapping.put("/reminders:" + Constants.REMINDERS_EDITED, "reminders");
        _postForwardingStatusMapping.put("/reminders:" + Constants.REMINDERS_EDITED, false);

        // Manage moderators page.
        _postRequestStatusMapping.put("/manageChannelModerators:" + Constants
                .RESPONSIBLE_MODERATORS_REVOKED_PRIVILEGES, "manageChannelModerators?successful=true");
        _postForwardingStatusMapping.put("/manageChannelModerators:" + Constants
                .RESPONSIBLE_MODERATORS_REVOKED_PRIVILEGES, false); // Redirect
        _postRequestStatusMapping.put("/manageChannelModerators:" + Constants
                .RESPONSIBLE_MODERATORS_REACTIVATED_STATUS, "manageChannelModerators?successful=true");
        _postForwardingStatusMapping.put("/manageChannelModerators:" + Constants
                .RESPONSIBLE_MODERATORS_REACTIVATED_STATUS, false); // Redirect
        _postRequestStatusMapping.put("/manageChannelModerators:" + Constants
                .RESPONSIBLE_MODERATORS_ADDED_MODERATOR, "manageChannelModerators?successful=true");
        _postForwardingStatusMapping.put("/manageChannelModerators:" + Constants
                .RESPONSIBLE_MODERATORS_ADDED_MODERATOR, false); // Redirect
        _postRequestStatusMapping.put("/manageChannelModerators:" + Constants
                .RESPONSIBLE_MODERATORS_OPERATION_FAILED, "manageChannelModerators");
        _postForwardingStatusMapping.put("/manageChannelModerators:" + Constants
                .RESPONSIBLE_MODERATORS_OPERATION_FAILED, true); // Forwarding

        // My channels page.
        _postRequestStatusMapping.put("/myChannelsDelete:" + Constants.CHANNELS_DELETED_CHANNEL,
                "myChannels?successful=true");
        _postForwardingStatusMapping.put("/myChannelsDelete:" + Constants.CHANNELS_DELETED_CHANNEL, false); // redirect
        _postRequestStatusMapping.put("/myChannelsDelete:" + Constants.CHANNELS_OPERATION_FAILED, "myChannels");
        _postForwardingStatusMapping.put("/myChannelsDelete:" + Constants.CHANNELS_OPERATION_FAILED, true); // forward

        // Send Announcement page.
        _postRequestStatusMapping.put("/sendAnnouncement:" +
                Constants.SEND_ANNOUNCEMENT_VALIDATION_ERROR, "announcements");
        _postForwardingStatusMapping.put("/sendAnnouncement:" +
                Constants.SEND_ANNOUNCEMENT_VALIDATION_ERROR, true);  // forwarding
        _postRequestStatusMapping.put("/sendAnnouncement:" +
                Constants.ANNOUNCEMENT_CREATED, "announcements?successful=true");
        _postForwardingStatusMapping.put("/sendAnnouncement:" +
                Constants.ANNOUNCEMENT_CREATED, false); // redirect
        _postRequestStatusMapping.put("/sendAnnouncement:" +
                Constants.ANNOUNCEMENT_CREATION_FAILED, "announcements");
        _postForwardingStatusMapping.put("/sendAnnouncement:" +
                Constants.ANNOUNCEMENT_CREATION_FAILED, true);   // forwarding

        // Edit channel details.
        _postRequestStatusMapping.put("/channelDetails:" + Constants.CHANNEL_DETAILS_EDITED_CHANNEL,
                "channelDetails?successful=true");
        _postForwardingStatusMapping.put("/channelDetails:" + Constants.CHANNEL_DETAILS_EDITED_CHANNEL, false);
        _postRequestStatusMapping.put("/channelDetails:" + Constants.CHANNEL_DETAILS_EDITING_FAILED, "channelDetails");
        _postForwardingStatusMapping.put("/channelDetails:" + Constants.CHANNEL_DETAILS_EDITING_FAILED, true);
        _postRequestStatusMapping.put("/channelDetails:" + Constants.CHANNEL_DETAILS_NO_UPDATE, "channelDetails");
        _postForwardingStatusMapping.put("/channelDetails:" + Constants.CHANNEL_DETAILS_NO_UPDATE, true);

        // Create channel.
        _postRequestStatusMapping.put("/createChannel:" + Constants.CREATED_CHANNEL, "myChannels?successful=true");
        _postForwardingStatusMapping.put("/createChannel:" + Constants.CREATED_CHANNEL, false); // Redirect.
        _postRequestStatusMapping.put("/createChannel:" + Constants.CHANNEL_CREATION_FAILED, "createChannel");
        _postForwardingStatusMapping.put("/createChannel:" + Constants.CHANNEL_CREATION_FAILED, true);  // Forward.

        // Delete channel from all channels page.
        _postRequestStatusMapping.put("/channelsDelete:" + Constants.CHANNELS_DELETED_CHANNEL,
                "channels?successful=true");
        _postForwardingStatusMapping.put("/channelsDelete:" + Constants.CHANNELS_DELETED_CHANNEL, false); // Redirect.
        _postRequestStatusMapping.put("/channelsDelete:" + Constants.CHANNELS_OPERATION_FAILED, "channels");
        _postForwardingStatusMapping.put("/channelsDelete:" + Constants.CHANNELS_OPERATION_FAILED, true); // Forward.

        // Create reminder.
        _postRequestStatusMapping.put("/reminderCreate:" + Constants.CREATED_REMINDER, "reminders");
        _postForwardingStatusMapping.put("/reminderCreate:" + Constants.CREATED_REMINDER, false);   // Redirect.
        _postRequestStatusMapping.put("/reminderCreate:" + Constants.CREATION_OF_REMINDER_FAILED, "createReminder");
        _postForwardingStatusMapping.put("/reminderCreate:" + Constants.CREATION_OF_REMINDER_FAILED, true); // Forward.
        _postRequestStatusMapping.put("/reminderCreate:" + Constants.REMINDER_VALIDATION_ERROR, "createReminder");
        _postForwardingStatusMapping.put("/reminderCreate:" + Constants.REMINDER_VALIDATION_ERROR, true); // Forward.

        // Edit reminder.
        _postRequestStatusMapping.put("/reminderEdit:" + Constants.REMINDER_EDITED_SUCCESSFULLY,
                "reminderEdit?successful=true");
        _postForwardingStatusMapping.put("/reminderEdit:" + Constants.REMINDER_EDITED_SUCCESSFULLY, false); // Redirect.
        _postRequestStatusMapping.put("/reminderEdit:" + Constants.REMINDER_EDITING_PROCESS_FAILED, "editReminder");
        _postForwardingStatusMapping.put("/reminderEdit:" + Constants.REMINDER_EDITING_PROCESS_FAILED, true);
        _postRequestStatusMapping.put("/reminderEdit:" + Constants.REMINDER_EDITING_NO_UPDATE, "editReminder");
        _postForwardingStatusMapping.put("/reminderEdit:" + Constants.REMINDER_EDITING_NO_UPDATE, true);
        _postRequestStatusMapping.put("/reminderEdit:" + Constants.REMINDER_VALIDATION_ERROR, "editReminder");
        _postForwardingStatusMapping.put("/reminderEdit:" + Constants.REMINDER_VALIDATION_ERROR, true);
    }

    /**
     * Determine the target view using the request context and the returned status of the
     * request execution. Depending on status either forward the request to its intended destination
     * or redirect to a specific view. Make sure to keep retain the PRG pattern, i.e. successful POST requests
     * should always be answered with a redirection request.
     *
     * @throws ServletException Throws ServletException when dispatching process fails.
     * @throws IOException Can throw IOException.
     */
    public static void dispatch(RequestContextManager context, String status)
            throws ServletException, IOException {
        String viewName;

        String pathInfo = context.getUrlPath();
        // Remove any .jsp and parameter data in the path info.
        if (pathInfo != null && pathInfo.contains(".jsp")) {
            pathInfo = pathInfo.replaceFirst(".jsp*", "");
            logger.debug("Path info after ending is checked: {}.", pathInfo);
        }

        // Determine view name based on status and request context.
        String key = pathInfo + ":" + status;

        logger.debug("Dispatcher: Key for lookup is: {}.", key);

        if (context.getHttpMethod().toUpperCase().equals("GET")) {
            viewName = _getRequestStatusMapping.get(key);

            // Redirect to index page if welcome page is requested.
            if (key.equals("/welcome:" + Constants.LOAD_INDEX)) {
                // Use UlmUniversityNewsWebClient in the URL if application is deployed on the actual server.
                redirectRequest(context.getResponse(), "/UlmUniversityNewsWebClient/webclient/" + viewName);
                // Use the following for local testing.
                // redirectRequest(context.getResponse(), "/webclient/" + viewName);
                return;
            }

            if (viewName != null) {
                // Perform forwarding.
                forwardRequest(context.getRequest(), context.getResponse(), viewName);
            }
        } else {
            viewName = _postRequestStatusMapping.get(key);

            if (viewName != null) {
                if (_postForwardingStatusMapping.get(key) == Boolean.TRUE) {
                    // Perform forwarding.
                    forwardRequest(context.getRequest(), context.getResponse(), viewName);
                } else {
                    // Perform redirect.
                    redirectRequest(context.getResponse(), viewName);
                }
            }
        }

        // If no action has been performed so far.
        if (viewName == null && status != null) {
            logger.debug("Need to check special cases.");

            // Check special status strings.
            if (status.equals(Constants.SESSION_EXPIRED)) {
                viewName = "login";
            } else if (status.equals(Constants.REQUIRES_LOGIN)) {
                viewName = "login";
            }

            if (viewName != null) {
                // Perform forwarding.
                forwardRequest(context.getRequest(), context.getResponse(), viewName);
            }
        }
    }

    /**
     * Forwards the request and its corresponding context and session data
     * to the view identified by the view Name.
     *
     * @param request The request object representing the request.
     * @param response The response object associated with the request.
     * @param viewName The name of the target view.
     * @throws ServletException Throws ServletException when forwarding fails.
     * @throws IOException Can throw IOException.
     */
    public static void forwardRequest(HttpServletRequest request, HttpServletResponse response, String viewName)
            throws ServletException, IOException {
        // Forward
        String path = "";
        if (!viewName.contains(".jsp"))
            path = "/WEB-INF/" + viewName + ".jsp";
        else
            path = "/WEB-INF/" + viewName;

        logger.debug("Forwarding request to {}.", path);

        request.getRequestDispatcher(path).forward(request, response);
    }

    /**
     * Responds with an redirection to the requestor. The target view is
     * specified in the redirection message. Please note: This results in a
     * complete new request from the client, the current request context will be
     * lost.
     *
     * @param response The response object associated with the request.
     * @param viewName The view that should be addressed in the redirected request.
     * @throws IOException Can throw IOException.
     */
    public static void redirectRequest(HttpServletResponse response, String viewName) throws IOException {
        logger.debug("Redirecting view to {}.", viewName);

        // Redirect.
        response.sendRedirect(viewName);
    }

    /**
     * Forwards the request to an error view depending on the specified error code.
     *
     * @param request The request object representing the request.
     * @param response The response object associated with the request.
     * @param errorCode The error code.
     * @throws ServletException Throws ServletException when forwarding fails.
     * @throws IOException Can throw IOException.
     */
    public static void forwardRequestToErrorView(HttpServletRequest request, HttpServletResponse response, int
            errorCode) throws ServletException, IOException {
        String path = "/WEB-INF/ErrorPages/";
        switch (errorCode) {
            case Constants.PAGE_NOT_FOUND:
                path += "notFound.jsp";
                break;
            case Constants.FORBIDDEN:
                path += "forbidden.jsp";
                break;
            case Constants.FATAL_ERROR:
                path += "fatalError.jsp";
                break;
            case Constants.CONNECTION_FAILURE:
                path += "connectionFailure.jsp";
                break;
        }

        logger.debug("Forwarding to error page with path {}.", path);

        request.getRequestDispatcher(path).forward(request, response);
    }
}
