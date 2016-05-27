package ulm.university.news.webclient.controller;

import ulm.university.news.webclient.controller.context.RequestContextManager;
import ulm.university.news.webclient.controller.interfaces.Action;
import ulm.university.news.webclient.util.exceptions.SessionIsExpiredException;

/**
 * Created by Philipp on 26.05.2016.
 */
public class GetMyChannelsAction implements Action {
    /**
     * This method executes the business logic for retrieving the channels managed by
     * the logged in moderator.
     *
     * @param requestContext The context of the request for which the execution is triggered.
     * @return Returns the identifier of the view that should be displayed after execution.
     * @throws SessionIsExpiredException If the session of the user is expired.
     */
    public String execute(RequestContextManager requestContext) throws SessionIsExpiredException {
        return "channels";
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
