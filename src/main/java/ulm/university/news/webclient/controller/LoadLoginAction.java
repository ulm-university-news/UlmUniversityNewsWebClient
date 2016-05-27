package ulm.university.news.webclient.controller;

import ulm.university.news.webclient.controller.context.RequestContextManager;
import ulm.university.news.webclient.controller.interfaces.Action;
import ulm.university.news.webclient.util.Constants;
import ulm.university.news.webclient.util.exceptions.SessionIsExpiredException;

/**
 * Created by Philipp on 27.05.2016.
 */
public class LoadLoginAction implements Action {
    /**
     * This method executes the business logic for loading the login screen.
     *
     * @param requestContext The context of the request for which the execution is triggered.
     * @return Returns the identifier of the view that should be displayed after execution.
     * @throws SessionIsExpiredException If the session of the user is expired.
     */
    public String execute(RequestContextManager requestContext) throws SessionIsExpiredException {
        String status;

        // Check whether the player is currently logged in or not.
        if (requestContext.hasActiveSession()){
            requestContext.addToRequestContext("loginStatus", "1");
            status = Constants.LOGGED_IN;
        } else {
            requestContext.addToRequestContext("loginStatus", "0");
            status = Constants.LOGGED_OUT;
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
