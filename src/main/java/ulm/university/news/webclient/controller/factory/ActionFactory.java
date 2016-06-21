package ulm.university.news.webclient.controller.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.controller.actions.*;
import ulm.university.news.webclient.controller.interfaces.Action;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This factory class is used to retrieve specific implementations of
 * the Action interface based on the request.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public abstract class ActionFactory {

    /** An instance of the Logger class which performs logging for the ActionFactory class. */
    private static final Logger logger = LoggerFactory.getLogger(ActionFactory.class);

    /** Maps Action implementations onto keys. */
    private static ConcurrentHashMap<String, Action> _actionMap;

    /**
     * Initializes the ActionFactory. The factory cannot return
     * Action instances before the initialization has been performed.
     */
    public static void initialize() {
        _actionMap = new ConcurrentHashMap<String, Action>();

        // Login page.
        _actionMap.put("POST/login", new LoginAction());
        _actionMap.put("GET/login", new LoadLoginAction());

        // Logout.
        _actionMap.put("POST/logout", new LogoutAction());

        // Register page.
        _actionMap.put("GET/register", new LoadRegisterFormAction());
        _actionMap.put("POST/register", new RegisterAction());

        // Password reset page.
        _actionMap.put("GET/passwordReset", new LoadPasswordResetAction());
        _actionMap.put("POST/passwordReset", new ResetPasswordAction());

        // Main page / index.
        _actionMap.put("GET/index", new HomeAction());

        // Applications page.
        _actionMap.put("GET/applications", new LoadApplicationsAction());
        _actionMap.put("POST/applications", new ApplicationsAction());

        // Accounts page.
        _actionMap.put("GET/accounts", new LoadAccountsAction());
        _actionMap.put("POST/accounts", new AccountsAction());

        _actionMap.put("GET/channels", new GetMyChannelsAction());

        logger.info("ActionFactory is initialized.");
    }

    /**
     * Returns the appropriate Action implementation based on the
     * request.
     *
     * @param request The request object.
     * @return A specific implementation of the Action interface.
     */
    public static Action getAction(HttpServletRequest request){
        String pathInfo = request.getPathInfo();
        // Remove any .jsp and parameter data in the path info.
        if (pathInfo != null && pathInfo.contains(".jsp")){
            pathInfo = pathInfo.replaceFirst(".jsp*", "");
            logger.debug("Path info after ending is checked: {}.", pathInfo);
        }

        String key = request.getMethod() + pathInfo;
        Action extractedAction = _actionMap.get(key);

        if (extractedAction != null)
            logger.debug("Retrieved Action of type {} for key {}.", extractedAction.getClass(), key);

        return extractedAction;
    }

}
