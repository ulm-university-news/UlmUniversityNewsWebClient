package ulm.university.news.webclient.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.controller.context.RequestContextManager;
import ulm.university.news.webclient.controller.dispatcher.RequestDispatcher;
import ulm.university.news.webclient.controller.factory.ActionFactory;
import ulm.university.news.webclient.controller.interfaces.Action;
import ulm.university.news.webclient.util.Constants;
import ulm.university.news.webclient.util.exceptions.SessionIsExpiredException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The FrontController class is the servlet that takes incoming requests. Its the
 * central entry point to our web application. Due to the centralization, it can provide
 * access control and authorization without the need to specify it in each Action handler.
 * The class will delegate the request to the appropriate Action handler if there is no
 * violation of any rights.
 *
 * Created by Philipp on 26.05.2016.
 */
@WebServlet("/webclient/*")
public class FrontController extends HttpServlet {

    /** An instance of the Logger class which performs logging for the FrontController class. */
    private static final Logger logger = LoggerFactory.getLogger(FrontController.class);

    /**
     * Processes an incoming request. Performs access control and authorization
     * before actually delegating the request to the appropriate Action handler.
     *
     * @param request The request object.
     * @param response The response object.
     * @throws ServletException Throws ServletException if the execution fails.
     * @throws IOException Can throw IOException.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        logger.debug("Incoming request. Request with method {} and path info {}.",
                request.getMethod(),
                request.getPathInfo());

        // Create context object.
        RequestContextManager contextManager = new RequestContextManager(request, response);

        // Extract HTTP method and path info.
        contextManager.setHttpMethod(request.getMethod());
        contextManager.setUrlPath(request.getPathInfo());

        try{
            // Get Action implementation with appropriate handling logic.
            Action action = ActionFactory.getAction(request);

            if (action == null)
            {
                logger.error("Couldn't find corresponding Action instance to handle the request.");
                // Redirect to 404 Page.
                RequestDispatcher.forwardRequestToErrorView(request, response, Constants.PAGE_NOT_FOUND);
                return;
            }

            // Handle page requests where no session is necessary.
            if (!action.requiresSession())
            {

            }
            else{
                // Handle requests where session is necessary.
                // First, check whether the requestor has an active session.
                if (!contextManager.hasActiveSession()){
                    logger.info("Requestor has no active session.");
                    // Redirect request directly to login page.
                    // Display error message on the login page.
                    request.setAttribute("loginStatusMsg", "You need to be logged in to execute this task.");
                    request.getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
                    return;
                }
                else {
                    logger.debug("User seems to have an active session.");
                    // TODO Check rights/permissions of the requestor.
                }


            }

            // Execute the logic to handle the request.
            String status = action.execute(contextManager);

            // Pass request context and status to dispatcher object.
            RequestDispatcher.dispatch(contextManager, status);


//            // Trigger navigation to view.
//            if (view.equals(request.getPathInfo().substring(1))) {
//                // The target view has not changed during execution.
//                contextManager.forwardRequestToView();
//            }
//            else
//            {
//                // Redirect to new target view.
//                contextManager.redirectRequestToTarget(view);
//            }

        }
        catch (SessionIsExpiredException sessionEx){
            // Display error message on the login page.
            request.setAttribute("loginStatusMsg", "It looks like your session has expired. You need to login again.");
            RequestDispatcher.dispatch(contextManager, Constants.SESSION_EXPIRED);
        }
        catch (Exception ex){
            logger.error(ex.getMessage());
            ex.printStackTrace();
            throw new ServletException("Execution failed.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

}
