package ulm.university.news.webclient.controller.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.data.Moderator;
import ulm.university.news.webclient.util.exceptions.SessionIsExpiredException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

/**
 * This class is used to represent a request and its corresponding context.
 * It can be used along the processing of the request in order to keep track
 * of request data as well as session data associated with the requestor's session.
 * <p>
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class RequestContextManager {

    /** An instance of the Logger class which performs logging for the RequestContextManager class. */
    private static final Logger logger = LoggerFactory.getLogger(RequestContextManager.class);

    /** Is used as a key to identify the moderator object stored in the session. */
    private final String REQUESTOR_KEY = "activeModerator";

    /** The request object that encapsulates the data of this request. */
    private HttpServletRequest request;

    /** The response object encapsulating data for the response. */
    private HttpServletResponse response;

    /** The http method of this request as a string. */
    private String httpMethod;

    /** The resource path taken from the requests URL as a string. */
    private String urlPath;

    /**
     * Creates a new object of RequestContextManager for a given request.
     *
     * @param request The request object of the request.
     * @param response The response object of the request.
     */
    public RequestContextManager(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;

        logger.debug("Created request context for the request.");
        try{
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            logger.error("Unsupported encoding.");
            ex.printStackTrace();
        }
    }

    /**
     * Adds an object to the context of the request. The object can
     * be retrieved via the context as long as the request is processed.
     *
     * @param key The key to identify the object in the context.
     * @param obj The data object.
     */
    public void addToRequestContext(String key, Object obj) {
        if (request != null) {
            request.setAttribute(key, obj);
        }
    }

    /**
     * Retrieve an object stored in the context of the request. The object
     * is identified by the specified key.
     *
     * @param key The key for the data object.
     * @return The data object, or null if no object is stored using this key.
     */
    public Object retrieveFromRequestContext(String key) {
        if (request != null) {
            return request.getAttribute(key);
        }

        return null;
    }

    /**
     * Retrieve an object stored in the context of the request. The object
     * is identified by the specified key.
     *
     * @return The current locale, or English if not specified.
     */
    public Locale retrieveLocale() {
        if (request != null) {
            // Check if language parameter is set.
            if (request.getParameter("language") != null){
                if (request.getParameter("language").equals("de")){
                    return Locale.GERMAN;
                }
                else if (request.getParameter("language").equals("en")){
                    return Locale.ENGLISH;
                }
            }

            // If the language parameter was not set, or contained a wrong value, retrieve locale from session.
            Object object = retrieveFromSession("language");
            if (object != null) {
                if (object instanceof Locale) {
                    return (Locale) object;
                }
                if (object instanceof String) {
                    String language = (String) object;

                    if (language.contains("de")){
                        return Locale.GERMAN;
                    }
                    else if (language.contains("en")) {
                        return Locale.ENGLISH;
                    }

                    return new Locale(language);
                }
            }
        }
        return Locale.ENGLISH;
    }

    /**
     * Returns the request parameter with the specified name.
     *
     * @param param The name of the parameter.
     * @return The parameter value, or null if the parameter is not specified.
     */
    public String getRequestParameter(String param) {
        if (request != null)
            return request.getParameter(param);

        return null;
    }

    /**
     * Determines whether the requestor is already associated
     * with an active session.
     *
     * @return Returns true if the requestor is associated with an active session,
     * otherwise false.
     */
    public boolean hasActiveSession() {
        HttpSession session = request.getSession(false);
        if (session == null) {
            // No active session for the requestor.
            return false;
        }

        if (session.getAttribute(REQUESTOR_KEY) == null) {
            // No moderator object stored in session.
            return false;
        }

        return true;
    }

    /**
     * Creates a new session for the requestor. If the
     * requestor already has an active session, the current one is
     * invalidated.
     */
    public void createNewSession() {
        // Get the local that is currently sent with the request.
        Locale currentLocale = retrieveLocale();;

//        try {
//            currentLocale = retrieveLocale();;
//        } catch (SessionIsExpiredException e) {
//            logger.warn("Failed to retrieve locale in createNewSession. Set it to english per default.");
//            currentLocale = Locale.ENGLISH;
//        }

        HttpSession session = request.getSession(false);
        if (session != null) {
            // Moderator has an active session. Invalidate the current session.
            session.invalidate();
        }

        // Get new session.
        request.getSession(true);

        // Set the locale again, i.e. the user's language settings keep up to date.
        // Set locale again in new session object.
        storeInSession("language", currentLocale);

//        try {
//            storeInSession("language", currentLocale);
//        } catch (SessionIsExpiredException e) {
//            logger.warn("SessionIsExpiredException occurred in create new session.");
//            logger.error("This should never happen.");
//        }
    }

    /**
     * Invalidates the active session of the requestor.
     */
    public void invalidateSession() {
        HttpSession session = request.getSession(false);
        if (session != null) {
            // Moderator has an active session. Invalidate the current session.
            session.invalidate();
        }
    }

    /**
     * Returns the moderator object representing the requestor. The object
     * is extracted from the active session of the requestor.
     *
     * @return An instance of the Moderator class containing the data of the requestor.
     * @throws SessionIsExpiredException If the session of the requestor is not active anymore.
     */
    public Moderator retrieveRequestor() throws SessionIsExpiredException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            // Moderator has no session. Session is probably expired.
            throw new SessionIsExpiredException("Session is expired");
        }

        Moderator requestor = (Moderator) session.getAttribute(REQUESTOR_KEY);
        if (requestor == null) {
            // No object stored in the session. Seems like the moderator isn't logged in anymore.
            throw new SessionIsExpiredException("Moderator not logged in anymore.");
        }

        return requestor;
    }

    /**
     * Stores the moderator object of the requestor into the session. This enables
     * to identify the moderator during the session.
     *
     * @param moderator The moderator object.
     */
    public void storeRequestorInSession(Moderator moderator){
        HttpSession session = request.getSession(true);
//        if (session == null) {
//            // Moderator has no session. Session is probably expired.
//            throw new SessionIsExpiredException("Session is expired");
//        }

        session.setAttribute(REQUESTOR_KEY, moderator);
    }

    /**
     * Adds a data element to the session context. This makes the data
     * available throughout the session.
     *
     * @param key The key which identifies the data object in the session.
     * @param obj The data object.
     */
    public void storeInSession(String key, Object obj){
        HttpSession session = request.getSession(true);
//        if (session == null) {
//            // Moderator has no session. Session is probably expired.
//            throw new SessionIsExpiredException("Session is expired");
//        }

        session.setAttribute(key, obj);
    }

    /**
     * Retrieves the data object which is identified by the specified key from the session.
     * Can also return null if no data object is stored that is identified by the given key.
     *
     * @param key The key for the data object.
     * @return The data object, or null if data object couldn't be localized.
     */
    public Object retrieveFromSession(String key) {
        HttpSession session = request.getSession(true);
//        if (session == null) {
//            // Moderator has no session. Session is probably expired.
//            throw new SessionIsExpiredException("Session is expired");
//        }

        return session.getAttribute(key);
    }

    /**
     * Returns the path info of the URL.
     *
     * @return The path info.
     */
    public String getUrlPath() {
        return urlPath;
    }

    /**
     * Sets the path info of the current request.
     *
     * @param urlPath The path info.
     */
    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    /**
     * Returns the HTTP method that was used for this request.
     *
     * @return The HTTP method as a string.
     */
    public String getHttpMethod() {
        return httpMethod;
    }

    /**
     * Sets the HTTP method of this request.
     *
     * @param httpMethod The HTTP method as a string.
     */
    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    /**
     * Get the response object for this request.
     *
     * @return The response object.
     */
    public HttpServletResponse getResponse() {
        return response;
    }

    /**
     * Get the request object for this request.
     *
     * @return The request object.
     */
    public HttpServletRequest getRequest() {
        return request;
    }

}
