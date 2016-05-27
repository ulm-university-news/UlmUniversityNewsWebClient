package ulm.university.news.webclient.controller.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.controller.GetMyChannelsAction;
import ulm.university.news.webclient.controller.HomeAction;
import ulm.university.news.webclient.controller.LoadLoginAction;
import ulm.university.news.webclient.controller.LoginAction;
import ulm.university.news.webclient.controller.interfaces.Action;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This factory class is used to retrieve specific implementations of
 * the Action interface based on the request.
 *
 * Created by Philipp on 26.05.2016.
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
    public static void initialize(){
        _actionMap = new ConcurrentHashMap<String, Action>();

        // Login page.
        _actionMap.put("POST/login", new LoginAction());
        _actionMap.put("GET/login", new LoadLoginAction());

        // Main page / index.
        _actionMap.put("GET/index", new HomeAction());

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
        String key = request.getMethod() + request.getPathInfo();
        Action extractedAction = _actionMap.get(key);

        if (extractedAction != null)
            logger.debug("Retrieved Action of type {} for key {}.", extractedAction.getClass(), key);

        return extractedAction;
    }

}
