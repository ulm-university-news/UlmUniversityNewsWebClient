package ulm.university.news.webclient.controller.actions;

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
public class MyAccountAction implements Action {

    /** An instance of the Logger class which performs logging for the MyAccountEditAction class. */
    private static final Logger logger = LoggerFactory.getLogger(MyAccountAction.class);

    /**
     * This method executes the business logic to delete the account of the logged in moderator from the server.
     *
     * @param requestContext The context of the request for which the execution is triggered.
     * @return Returns the status that is used to determine the view that should be displayed after execution.
     * @throws SessionIsExpiredException If the session of the user is expired.
     */
    public String execute(RequestContextManager requestContext) throws SessionIsExpiredException, ServerException {
        logger.debug("In MyAccountAction");
        Moderator activeModerator = requestContext.retrieveRequestor();

        if (activeModerator != null) {
            String button = requestContext.getRequestParameter("button");
            try {
                if (button != null) {
                    String message;
                    if (button.equals("delete")) {
                        logger.debug("Delete {}", activeModerator.toString());
                        new ModeratorAPI().deleteModerator(activeModerator.getServerAccessToken(),
                                activeModerator.getId());
                        message = Translator.getInstance().getText(requestContext.retrieveLocale(),
                                "myAccount.delete.success", activeModerator.getFirstName() + " "
                                        + activeModerator.getLastName());
                        requestContext.storeInSession("editInfo", message);
                    }
                }
            } catch (APIException e) {
                logger.error("Delete my account request failed. Error code is {}.", e.getErrorCode());
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

                requestContext.storeInSession("editError", errorMessage);
                return Constants.MY_ACCOUNT_DELETE_FAILED;
            }
        }

        requestContext.invalidateSession();
        return Constants.MY_ACCOUNT_DELETED;
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
