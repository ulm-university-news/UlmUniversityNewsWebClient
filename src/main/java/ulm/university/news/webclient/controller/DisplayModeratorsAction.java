package ulm.university.news.webclient.controller;

import ulm.university.news.webclient.api.ModeratorAPI;
import ulm.university.news.webclient.controller.context.RequestContextManager;
import ulm.university.news.webclient.controller.interfaces.Action;
import ulm.university.news.webclient.data.Moderator;
import ulm.university.news.webclient.util.Constants;
import ulm.university.news.webclient.util.exceptions.APIException;
import ulm.university.news.webclient.util.exceptions.ServerException;
import ulm.university.news.webclient.util.exceptions.SessionIsExpiredException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Philipp on 28.05.2016.
 */
public class DisplayModeratorsAction implements Action{
    /**
     * This method executes the business logic to load moderator resources from the server.
     *
     * @param requestContext The context of the request for which the execution is triggered.
     * @return Returns the status that is used to determine the view that should be displayed after execution.
     * @throws SessionIsExpiredException If the session of the user is expired.
     */
    public String execute(RequestContextManager requestContext) throws SessionIsExpiredException, ServerException {
        // Get moderators via REST Server.
        List<Moderator> moderators = new ArrayList<Moderator>();
        Moderator activeModerator = requestContext.retrieveRequestor();

        try {
            moderators = new ModeratorAPI().getModerators(activeModerator.getServerAccessToken());
        } catch (APIException ex) {
            ex.printStackTrace();
            // TODO
        }

        // Store moderator data in request context.
        requestContext.addToRequestContext("moderators", moderators);

        return Constants.MODERATORS_LOADED;
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
