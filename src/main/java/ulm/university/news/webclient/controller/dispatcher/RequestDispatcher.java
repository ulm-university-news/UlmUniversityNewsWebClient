package ulm.university.news.webclient.controller.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.controller.context.RequestContextManager;
import ulm.university.news.webclient.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public abstract class RequestDispatcher {

    /** An instance of the Logger class which performs logging for the RequestDispatcher class. */
    private static final Logger logger = LoggerFactory.getLogger(RequestDispatcher.class);

    /** Maps status responses that have been returned by Action objects processing GET requests. */
    private static ConcurrentHashMap<String, String> _getRequestStatusMapping;

    /** Maps status responses that have been returned by Action objects processing POST requests. */
    private static ConcurrentHashMap<String, String> _postRequestStatusMapping;

    /**
     * Maps status responses that have been returned by Action objects to a forwarding status which
     * determines whether the dispatcher should perform a forwarding or a redirect.
     */
    private static ConcurrentHashMap<String, Boolean> _postForwardingStatusMapping;

    /**
     * Initializes the data structures realizing the mappings.
     */
    public static void initialize() {
        _getRequestStatusMapping = new ConcurrentHashMap<String, String>();
        _postRequestStatusMapping = new ConcurrentHashMap<String, String>();
        _postForwardingStatusMapping = new ConcurrentHashMap<String, Boolean>();

        // GET requests.
        // Login page.
        _getRequestStatusMapping.put("/login:" + Constants.LOGGED_IN, "index"); // Navigate him back to index.
        _getRequestStatusMapping.put("/login:" + Constants.LOGGED_OUT, "login");

        // Register page.
        _getRequestStatusMapping.put("/register:" + Constants.LOGGED_IN, "index"); // Navigate him back to index.
        _getRequestStatusMapping.put("/register:" + Constants.LOGGED_OUT, "register");

        // Password reset page.
        _getRequestStatusMapping.put("/passwordReset:" + Constants.LOGGED_IN, "index"); // Navigate him back to index.
        _getRequestStatusMapping.put("/passwordReset:" + Constants.LOGGED_OUT, "resetPassword");

        // Index/Main page.
        _getRequestStatusMapping.put("/index:" + Constants.LOGGED_OUT, "index");
        _getRequestStatusMapping.put("/index:" + Constants.LOGGED_IN, "index");

        // Applications page.
        _getRequestStatusMapping.put("/applications:" + Constants.APPLICATIONS_LOADED, "applications");
        _getRequestStatusMapping.put("/applications:" + Constants.APPLICATIONS_LOAD_FAILED, "applications");

        // POST requests.
        // Login page.
        _postRequestStatusMapping.put("/login:" + Constants.LOGIN_SUCCESSFUL, "index");
        _postRequestStatusMapping.put("/login:" + Constants.LOGIN_FAILED, "login");
        _postForwardingStatusMapping.put("/login:" + Constants.LOGIN_SUCCESSFUL, false);
        _postForwardingStatusMapping.put("/login:" + Constants.LOGIN_FAILED, true);

        // Logout.
        _postRequestStatusMapping.put("/logout:" + Constants.LOGGED_OUT, "index");
        _postForwardingStatusMapping.put("/logout:" + Constants.LOGGED_OUT, false);

        // Register page.
        _postRequestStatusMapping.put("/register:" + Constants.VALIDATION_FAILED, "register");
        _postForwardingStatusMapping.put("/register:" + Constants.VALIDATION_FAILED, true);
        _postRequestStatusMapping.put("/register:" + Constants.REGISTRATION_SUCCESSFUL, "register.jsp?successful=true");
        _postForwardingStatusMapping.put("/register:" + Constants.REGISTRATION_SUCCESSFUL, false);  // Redirect

        // Password reset page.
        _postRequestStatusMapping.put("/passwordReset:" + Constants.PASSWORD_RESET_SUCCESSFUL,
                "resetPassword.jsp?successful=true");
        _postForwardingStatusMapping.put("/passwordReset:" + Constants.PASSWORD_RESET_SUCCESSFUL, false);  // Redirect.
        _postRequestStatusMapping.put("/passwordReset:" + Constants.PASSWORD_RESET_FAILED, "resetPassword");
        _postForwardingStatusMapping.put("/passwordReset:" + Constants.PASSWORD_RESET_FAILED, true);

        // Applications page.
        _postRequestStatusMapping.put("/applications:" + Constants.APPLICATIONS_EDIT_FAILED, "applications");
        _postForwardingStatusMapping.put("/applications:" + Constants.APPLICATIONS_EDIT_FAILED, true);
        _postRequestStatusMapping.put("/applications:" + Constants.APPLICATIONS_EDITED, "applications");
        _postForwardingStatusMapping.put("/applications:" + Constants.APPLICATIONS_EDITED, false);
    }

    /**
     * Determine the target view using the request context and the returned status of the
     * request execution. Depending on status either forward the request to its intended destination
     * or redirect to a specific view. Make sure to keep retain the PRG pattern, i.e. successful POST requests
     * should always be answered with a redirection request.
     *
     * @throws ServletException Throws ServletException when dispatching process fails.
     * @throws IOException Can throw IOException.
     */
    public static void dispatch(RequestContextManager context, String status)
            throws ServletException, IOException {
        String viewName;

        String pathInfo = context.getUrlPath();
        // Remove any .jsp and parameter data in the path info.
        if (pathInfo != null && pathInfo.contains(".jsp")){
            pathInfo = pathInfo.replaceFirst(".jsp*", "");
            logger.debug("Path info after ending is checked: {}.", pathInfo);
        }

        // Determine view name based on status and request context.
        String key = pathInfo + ":" + status;

        logger.debug("Dispatcher: Key for lookup is: {}.", key);

        if (context.getHttpMethod().toUpperCase().equals("GET")) {
            viewName = _getRequestStatusMapping.get(key);

            if (viewName != null) {
                // Perform forwarding.
                forwardRequest(context.getRequest(), context.getResponse(), viewName);
            }
        } else {
            viewName = _postRequestStatusMapping.get(key);

            if (viewName != null) {
                if (_postForwardingStatusMapping.get(key) == Boolean.TRUE) {
                    // Perform forwarding.
                    forwardRequest(context.getRequest(), context.getResponse(), viewName);
                } else {
                    // Perform redirect.
                    redirectRequest(context.getResponse(), viewName);
                }
            }
        }

        // If no action has been performed so far.
        if (viewName == null && status != null) {
            logger.debug("Need to check special cases.");

            // Check special status strings.
            if (status.equals(Constants.SESSION_EXPIRED)) {
                    viewName = "login";
            }
            else if (status.equals(Constants.REQUIRES_LOGIN)) {
                    viewName = "login";
            }

            if (viewName != null) {
                // Perform forwarding.
                forwardRequest(context.getRequest(), context.getResponse(), viewName);
            }
        }
    }

    /**
     * Forwards the request and its corresponding context and session data
     * to the view identified by the view Name.
     *
     * @param request The request object representing the request.
     * @param response The response object associated with the request.
     * @param viewName The name of the target view.
     * @throws ServletException Throws ServletException when forwarding fails.
     * @throws IOException Can throw IOException.
     */
    public static void forwardRequest(HttpServletRequest request, HttpServletResponse response, String viewName)
            throws ServletException, IOException {
        // Forward
        String path = "";
        if (!viewName.contains(".jsp"))
            path = "/WEB-INF/" + viewName + ".jsp";
        else
            path = "/WEB-INF/" + viewName;

        logger.debug("Forwarding request to {}.", path);

        request.getRequestDispatcher(path).forward(request, response);
    }

    /**
     * Responds with an redirection to the requestor. The target view is
     * specified in the redirection message. Please note: This results in a
     * complete new request from the client, the current request context will be
     * lost.
     *
     * @param response The response object associated with the request.
     * @param viewName The view that should be addressed in the redirected request.
     * @throws IOException Can throw IOException.
     */
    public static void redirectRequest(HttpServletResponse response, String viewName) throws IOException {
        logger.debug("Redirecting view to {}.", viewName);

        // Redirect.
        response.sendRedirect(viewName);
    }

    /**
     * Forwards the request to an error view depending on the specified error code.
     *
     * @param request The request object representing the request.
     * @param response The response object associated with the request.
     * @param errorCode The error code.
     * @throws ServletException Throws ServletException when forwarding fails.
     * @throws IOException Can throw IOException.
     */
    public static void forwardRequestToErrorView(HttpServletRequest request, HttpServletResponse response, int
            errorCode) throws ServletException, IOException {
        String path = "/WEB-INF/ErrorPages/";
        switch (errorCode) {
            case Constants.PAGE_NOT_FOUND:
                path += "notFound.jsp";
                break;
            case Constants.FORBIDDEN:
                path += "forbidden.jsp";
                break;
            case Constants.FATAL_ERROR:
                path += "fatalError.jsp";
                break;
        }

        logger.debug("Forwarding to error page with path {}.", path);

        request.getRequestDispatcher(path).forward(request, response);
    }
}
