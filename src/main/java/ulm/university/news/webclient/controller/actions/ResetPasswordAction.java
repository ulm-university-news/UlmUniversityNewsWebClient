package ulm.university.news.webclient.controller.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.api.ModeratorAPI;
import ulm.university.news.webclient.controller.context.RequestContextManager;
import ulm.university.news.webclient.controller.interfaces.Action;
import ulm.university.news.webclient.util.Constants;
import ulm.university.news.webclient.util.Translator;
import ulm.university.news.webclient.util.exceptions.APIException;
import ulm.university.news.webclient.util.exceptions.ServerException;
import ulm.university.news.webclient.util.exceptions.SessionIsExpiredException;

/**
 * Realizes the password reset functionality.
 * Sends a request to the server in order to reset the password of the
 * specified account.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class ResetPasswordAction implements Action {

    /** An instance of the Logger class which performs logging for the ResetPasswordAction class. */
    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordAction.class);

    /**
     * This method executes the business logic for resetting the password.
     *
     * @param requestContext The context of the request for which the execution is triggered.
     * @return Returns the status that is used to determine the view that should be displayed after execution.
     * @throws SessionIsExpiredException If the session of the user is expired.
     */
    public String execute(RequestContextManager requestContext) throws SessionIsExpiredException, ServerException {
        String status = null;
        // Read request parameters.
        String task = requestContext.getRequestParameter("task");
        String username = requestContext.getRequestParameter("username");

        // Prüfe, ob ein Moderatorenname eingegeben wurde.
        if (username == null || username.trim().length() == 0)
        {
            // Setze Validierungsfehler.
            Translator translator = Translator.getInstance();
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "resetPassword.failure.noAccountName");
            requestContext.addToRequestContext("resetPasswordNameValidationError", errorMsg);
            status = Constants.PASSWORD_RESET_FAILED;
            return status;
        }

        if (task.equals("reset")) {
            try {
                ModeratorAPI moderatorAPI = new ModeratorAPI();

                // Perform request to reset the password.
                moderatorAPI.resetPassword(username);

                logger.info("Password reset performed for moderator account with username {}", username);
                status = Constants.PASSWORD_RESET_SUCCESSFUL;
            } catch (APIException ex) {
                if (ex.getErrorCode() == Constants.MODERATOR_NOT_FOUND) {
                    Translator translator = Translator.getInstance();
                    String errorMsg = translator.getText(requestContext.retrieveLocale(),
                            "resetPassword.failure.accountNotFound");
                    requestContext.addToRequestContext("resetPasswordNameValidationError", errorMsg);
                    status = Constants.PASSWORD_RESET_FAILED;
                } else if (ex.getErrorCode() == Constants.EMAIL_FAILURE) {
                    Translator translator = Translator.getInstance();
                    String errorMsg = translator.getText(requestContext.retrieveLocale(),
                            "resetPassword.failure.emailFailure");
                    requestContext.addToRequestContext("successful", false);
                    requestContext.addToRequestContext("errorMsg", errorMsg);
                    status = Constants.PASSWORD_RESET_FAILED;
                } else {
                    logger.error("Cannot handle the error in RegisterAction. Passing it to FrontController.");
                    // Map to ServerException.
                    throw new ServerException(ex.getErrorCode(), ex);
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
