package ulm.university.news.webclient.controller;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.api.ModeratorAPI;
import ulm.university.news.webclient.controller.context.RequestContextManager;
import ulm.university.news.webclient.controller.interfaces.Action;
import ulm.university.news.webclient.data.Moderator;
import ulm.university.news.webclient.util.Constants;
import ulm.university.news.webclient.util.exceptions.APIException;
import ulm.university.news.webclient.util.exceptions.SessionIsExpiredException;

/**
 * Created by Philipp on 26.05.2016.
 */
public class LoginAction implements Action {

    /** An instance of the Logger class which performs logging for the LoginAction class. */
    private static final Logger logger = LoggerFactory.getLogger(LoginAction.class);

    /**
     * This method performs a login procedure. The entered credentials are sent to the
     * REST server in order to validate the data and to retrieve the moderator instance.
     *
     * @param requestContext The context of the request for which the execution is triggered.
     * @return The identifier of the view that should be displayed after execution.
     * @throws SessionIsExpiredException If the session of the user is expired.
     */
    public String execute(RequestContextManager requestContext) throws SessionIsExpiredException {
        logger.debug("In LoginAction.");

        if (requestContext == null)
        {
            logger.error("Invalid request context.");
            return Constants.LOGIN_FAILED;
        }

        // Extract entered credentials.
        String username = requestContext.getRequestParameter("username");
        String password = requestContext.getRequestParameter("password");
        String task = requestContext.getRequestParameter("task");

        if (task != null && task.equals("login"))
        {
            logger.debug("Login called. Parameters are: {} and {}.",
                    username,
                    password);

            // Send request to REST server.
            try{
                ModeratorAPI moderatorAPI = new ModeratorAPI();

                Moderator moderator = moderatorAPI.login(username, password);
                logger.debug("Moderator with name {} successfully logged in.", moderator.getName());

                // Create session and store moderator in it.
                requestContext.createNewSession();
                requestContext.storeRequestorInSession(moderator);
            } catch (APIException ex)
            {
                logger.error("Login request failed. Error code is {}.", ex.getErrorCode());

                // React to rejection due to invalid credentials.
                if (ex.getErrorCode() == Constants.MODERATOR_UNAUTHORIZED){
                    requestContext.addToRequestContext("loginStatusMsg", "Der Nutzername oder das Passwort ist " +
                            "falsch.");
                    return Constants.LOGIN_FAILED;
                }
                if (ex.getErrorCode() == Constants.MODERATOR_LOCKED){
                    requestContext.addToRequestContext("loginStatusMsg", "Der Account wurde noch nicht freigegeben.");
                    return Constants.LOGIN_FAILED;
                }
                if (ex.getErrorCode() == Constants.MODERATOR_DELETED){
                    requestContext.addToRequestContext("loginStatusMsg", "Der Account existiert nicht mehr.");
                    return Constants.LOGIN_FAILED;
                }

                // TODO What about other errors.
                requestContext.addToRequestContext("loginStatusMsg", "Login fehlgeschlagen aus unbekannten Gründen.");
                return Constants.LOGIN_FAILED;
            }
        }

        return Constants.LOGIN_SUCCESSFUL;
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
