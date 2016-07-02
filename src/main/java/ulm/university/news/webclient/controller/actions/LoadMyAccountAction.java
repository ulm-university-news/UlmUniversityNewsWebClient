package ulm.university.news.webclient.controller.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.controller.context.RequestContextManager;
import ulm.university.news.webclient.controller.interfaces.Action;
import ulm.university.news.webclient.data.Moderator;
import ulm.university.news.webclient.util.Constants;
import ulm.university.news.webclient.util.Translator;
import ulm.university.news.webclient.util.exceptions.ServerException;
import ulm.university.news.webclient.util.exceptions.SessionIsExpiredException;

/**
 * TODO
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class LoadMyAccountAction implements Action {
    /** An instance of the Logger class which performs logging for the LoadMyAccountAction class. */
    private static final Logger logger = LoggerFactory.getLogger(LoadMyAccountAction.class);

    /**
     * This method executes the business logic to show account information of the logged in moderator from the server.
     *
     * @param requestContext The context of the request for which the execution is triggered.
     * @return Returns the status that is used to determine the view that should be displayed after execution.
     * @throws SessionIsExpiredException If the session of the user is expired.
     */
    public String execute(RequestContextManager requestContext) throws SessionIsExpiredException, ServerException {
        // Get logged in moderator.
        Moderator activeModerator = requestContext.retrieveRequestor();

        // This should never happen since this page is only shown if the moderator is logged in.
        if (activeModerator == null) {
                String errorMessage = Translator.getInstance().getText(requestContext.retrieveLocale(),
                        "general.message.error.fatal");
                requestContext.storeInSession("loadError", errorMessage);
                return Constants.MY_ACCOUNT_LOAD_FAILED;
        }

        return Constants.MY_ACCOUNT_LOADED;
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
