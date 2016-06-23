package ulm.university.news.webclient.controller.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.api.GroupAPI;
import ulm.university.news.webclient.controller.context.RequestContextManager;
import ulm.university.news.webclient.controller.interfaces.Action;
import ulm.university.news.webclient.data.Group;
import ulm.university.news.webclient.data.Moderator;
import ulm.university.news.webclient.util.Constants;
import ulm.university.news.webclient.util.GroupUtil;
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
public class LoadGroupsAction implements Action {
    /** An instance of the Logger class which performs logging for the LoadGroupsAction class. */
    private static final Logger logger = LoggerFactory.getLogger(LoadGroupsAction.class);

    /**
     * This method executes the business logic to load groups from the server.
     *
     * @param requestContext The context of the request for which the execution is triggered.
     * @return Returns the status that is used to determine the view that should be displayed after execution.
     * @throws SessionIsExpiredException If the session of the user is expired.
     */
    public String execute(RequestContextManager requestContext) throws SessionIsExpiredException, ServerException {
        // Get groups via REST Server.
        List<Group> groups;
        Moderator activeModerator = requestContext.retrieveRequestor();

        // Do not load groups again if the client clicks on list entries.
        if (requestContext.getRequestParameter("groupId") == null) {
            try {
                // Request only groups who are not accepted yet.
                groups = new GroupAPI().getGroups(activeModerator.getServerAccessToken());
                GroupUtil.sortGroupsName(groups);
                // Store group data in session for later reuse.
                requestContext.storeInSession("groups", groups);
            } catch (APIException e) {
                logger.error("Group request failed. Error code is {}.", e.getErrorCode());
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

                requestContext.storeInSession("loadError", errorMessage);
                return Constants.GROUPS_LOAD_FAILED;
            }
        }

        return Constants.GROUPS_LOADED;
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
