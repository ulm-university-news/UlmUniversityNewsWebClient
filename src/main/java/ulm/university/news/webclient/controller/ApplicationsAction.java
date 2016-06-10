package ulm.university.news.webclient.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.api.ModeratorAPI;
import ulm.university.news.webclient.controller.context.RequestContextManager;
import ulm.university.news.webclient.controller.interfaces.Action;
import ulm.university.news.webclient.data.Moderator;
import ulm.university.news.webclient.util.Constants;
import ulm.university.news.webclient.util.ModeratorUtil;
import ulm.university.news.webclient.util.Translator;
import ulm.university.news.webclient.util.exceptions.APIException;
import ulm.university.news.webclient.util.exceptions.ServerException;
import ulm.university.news.webclient.util.exceptions.SessionIsExpiredException;

import java.util.List;

/**
 * TODO
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class ApplicationsAction implements Action {
    /** An instance of the Logger class which performs logging for the ApplicationAction class. */
    private static final Logger logger = LoggerFactory.getLogger(ApplicationsAction.class);

    /**
     * This method executes the business logic to load applications (moderator resources) from the server.
     *
     * @param requestContext The context of the request for which the execution is triggered.
     * @return Returns the status that is used to determine the view that should be displayed after execution.
     * @throws SessionIsExpiredException If the session of the user is expired.
     */
    public String execute(RequestContextManager requestContext) throws SessionIsExpiredException, ServerException {
        // Get moderators via REST Server.
        List<Moderator> moderators;
        Moderator activeModerator = requestContext.retrieveRequestor();

        // Do not load moderators again if the client clicks on list entries.
        if (requestContext.getRequestParameter("moderatorId") == null) {
            try {
                // Request only moderators who are not accepted yet.
                moderators = new ModeratorAPI().getModerators(activeModerator.getServerAccessToken(), true, null);
                ModeratorUtil.sortModeratorsName(moderators);
                // Store moderator data in session for later reuse.
                requestContext.storeInSession("moderators", moderators);
            } catch (APIException e) {
                logger.error("Application request failed. Error code is {}.", e.getErrorCode());
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

                requestContext.addToRequestContext("applicationLoadError", errorMessage);
                return Constants.APPLICATIONS_LOAD_FAILED;
            }
        }

        return Constants.APPLICATIONS_LOADED;
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
