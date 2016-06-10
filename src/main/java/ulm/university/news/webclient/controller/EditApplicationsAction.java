package ulm.university.news.webclient.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.api.ModeratorAPI;
import ulm.university.news.webclient.controller.context.RequestContextManager;
import ulm.university.news.webclient.controller.interfaces.Action;
import ulm.university.news.webclient.data.Moderator;
import ulm.university.news.webclient.util.Constants;
import ulm.university.news.webclient.util.Translator;
import ulm.university.news.webclient.util.exceptions.APIException;
import ulm.university.news.webclient.util.exceptions.ServerException;
import ulm.university.news.webclient.util.exceptions.SessionIsExpiredException;

/**
 * TODO
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class EditApplicationsAction implements Action {

    /** An instance of the Logger class which performs logging for the EditApplicationAction class. */
    private static final Logger logger = LoggerFactory.getLogger(EditApplicationsAction.class);

    /**
     * This method executes the business logic to load applications (moderator resources) from the server.
     *
     * @param requestContext The context of the request for which the execution is triggered.
     * @return Returns the status that is used to determine the view that should be displayed after execution.
     * @throws SessionIsExpiredException If the session of the user is expired.
     */
    public String execute(RequestContextManager requestContext) throws SessionIsExpiredException, ServerException {
        logger.debug("In EditApplicationsAction");
        Moderator activeModerator = requestContext.retrieveRequestor();

        // Get currently selected moderator.
        Moderator currentModerator = (Moderator) requestContext.retrieveFromSession("currentModerator");
        if (currentModerator != null) {
            String button = requestContext.getRequestParameter("button");
            try {
                if (button != null) {
                    Moderator m = new Moderator();
                    String message;
                    if (button.equals("accept")) {
                        logger.debug("Accept {}", currentModerator.toString());
                        m.setId(currentModerator.getId());
                        m.setLocked(false);
                        Moderator moderator = new ModeratorAPI().changeModerator(
                                activeModerator.getServerAccessToken(), m);
                        message = Translator.getInstance().getText(requestContext.retrieveLocale(),
                                "application.accept.success", moderator.getFirstName() + " " + moderator.getLastName());
                        requestContext.storeInSession("applicationEditSuccess", message);
                    } else if (button.equals("decline")) {
                        logger.debug("Decline {}", currentModerator.toString());
                        new ModeratorAPI().deleteModerator(activeModerator.getServerAccessToken(),
                                currentModerator.getId());
                        message = Translator.getInstance().getText(requestContext.retrieveLocale(),
                                "application.decline.success", currentModerator.getFirstName() + " "
                                        + currentModerator.getLastName());
                        requestContext.storeInSession("applicationEditInfo", message);
                    }
                }
            } catch (APIException e) {
                logger.error("Edit application request failed. Error code is {}.", e.getErrorCode());
                String errorMessage;
                switch (e.getErrorCode()) {
                    case Constants.MODERATOR_NOT_FOUND:
                        errorMessage = Translator.getInstance().getText(requestContext.retrieveLocale(),
                                "moderator.notfound");
                        break;
                    case Constants.MODERATOR_FORBIDDEN:
                        errorMessage = Translator.getInstance().getText(requestContext.retrieveLocale(),
                                "general.message.error.forbidden");
                        break;
                    default:
                        errorMessage = Translator.getInstance().getText(requestContext.retrieveLocale(),
                                "general.message.error.fatal");
                }

                requestContext.addToRequestContext("applicationEditError", errorMessage);
                return Constants.APPLICATIONS_EDIT_FAILED;
            }
        }

        return Constants.APPLICATIONS_EDITED;
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
