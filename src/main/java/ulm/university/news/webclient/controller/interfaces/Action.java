package ulm.university.news.webclient.controller.interfaces;

import ulm.university.news.webclient.controller.context.RequestContextManager;
import ulm.university.news.webclient.util.exceptions.ServerException;
import ulm.university.news.webclient.util.exceptions.SessionIsExpiredException;

/**
 * Created by Philipp on 26.05.2016.
 */
public interface Action {
    /**
     * This method executes the business logic contained in the specific action object.
     *
     * @param requestContext The context of the request for which the execution is triggered.
     * @return Returns the status that is used to determine the view that should be displayed after execution.
     * @throws SessionIsExpiredException If the session of the user is expired.
     */
    String execute(RequestContextManager requestContext) throws SessionIsExpiredException, ServerException;

    /**
     * This method indicates whether an active session is required in order
     * to execute the Action.
     *
     * @return Returns true if an active session is required, otherwise false.
     */
    boolean requiresSession();

    /**
     * This method indicates whether administrator permissions are required in order
     * to execute the Action.
     *
     * @return Returns true if administrator permissions are required, otherwise false.
     */
    boolean requiresAdminPermissions();
}
