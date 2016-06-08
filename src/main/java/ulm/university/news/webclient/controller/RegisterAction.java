package ulm.university.news.webclient.controller;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.controller.context.RequestContextManager;
import ulm.university.news.webclient.controller.interfaces.Action;
import ulm.university.news.webclient.util.Constants;
import ulm.university.news.webclient.util.exceptions.ServerException;
import ulm.university.news.webclient.util.exceptions.SessionIsExpiredException;

import java.awt.*;
import java.util.Locale;

/**
 * Handles incoming registration requests. Users who want to create an moderator account
 * can use the registration page to perform such an request.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class RegisterAction implements Action {

    /** An instance of the Logger class which performs logging for the RegisterAction class. */
    private static final Logger logger = LoggerFactory.getLogger(RegisterAction.class);

    /** Reference to the request context that belongs to the request which has triggered the execution of this action
     *  in the first place.*/
    private RequestContextManager requestContext;

    /**
     * This method executes the business logic to perform registration, i.e. handle an incoming
     * account creation request.
     *
     * @param requestContext The context of the request for which the execution is triggered.
     * @return Returns the status that is used to determine the view that should be displayed after execution.
     * @throws SessionIsExpiredException If the session of the user is expired.
     */
    public String execute(RequestContextManager requestContext) throws SessionIsExpiredException, ServerException {
        this.requestContext = requestContext;

        if (requestContext.getRequestParameter("task").equals("register")){
            // First, extract the sent parameters.
            String username,firstName, lastName, email, password, reEnteredPassword, motivation;
            username = requestContext.getRequestParameter("username");
            firstName = requestContext.getRequestParameter("firstname");
            lastName = requestContext.getRequestParameter("lastname");
            email = requestContext.getRequestParameter("email");
            password = requestContext.getRequestParameter("password");
            reEnteredPassword = requestContext.getRequestParameter("reEnteredPassword");
            motivation = requestContext.getRequestParameter("motivation");

            // Get locale.
            Locale currentLocale = requestContext.retrieveLocale();

            logger.debug("New account request: {}, {}, {}, {}, {}", username, firstName, lastName, email, motivation);

            if (!doesPasswordMatch(password, reEnteredPassword))
            {
                return Constants.VALIDATION_FAILED;
            }

            if (!validateRegistrationParameters(username, firstName, lastName, email, motivation))
            {
                return Constants.VALIDATION_FAILED;
            }
        }



        return null;
    }

    /**
     * Determines whether the passwords entered in both input fields are the same.
     *
     * @param password The password from the first input field.
     * @param reEnteredPassword The password from the second input field.
     * @return Returns true, if the passwords match, otherwise false.
     */
    private boolean doesPasswordMatch(String password, String reEnteredPassword)
    {
        if (!password.equals(reEnteredPassword)) {
            logger.debug("Validation error regarding password.");
            String errorMsg = "Passwords do not match.";
            setValidationError(Constants.MODERATOR_INVALID_PASSWORD, errorMsg);
            return false;
        }

        return true;
    }

    /**
     * Validates the registration parameters. Returns the validation status.
     *
     * @param username The entered username.
     * @param firstName The entered first name.
     * @param lastName The entered last name.
     * @param email The entered email.
     * @param motivation The entered motivation.
     * @return Returns true, if validation is successful, returns false if validation errors occur.
     */
    private boolean validateRegistrationParameters(String username, String firstName, String lastName, String email,
                                                   String motivation)
    {
        boolean status = true;

        if (username == null || username.trim().length() == 0) {
            logger.debug("Validation error regarding username.");
            status = false;
            String errorMsg = "Username must be set.";
            setValidationError(Constants.MODERATOR_INVALID_NAME, errorMsg);
        }

        if (firstName == null || firstName.trim().length() == 0){
            logger.debug("Validation error regarding first name.");
            status = false;
            String errorMsg = "First name must be set.";
            setValidationError(Constants.MODERATOR_INVALID_FIRST_NAME, errorMsg);
        }

        if (lastName == null || lastName.trim().length() == 0){
            logger.debug("Validation error regarding last name.");
            status = false;
            String errorMsg = "Last name must be set.";
            setValidationError(Constants.MODERATOR_INVALID_LAST_NAME, errorMsg);
        }

        if (email == null || email.trim().length() == 0) {
            logger.debug("Validation error regarding email.");
            status = false;
            String errorMsg = "Email address must be set.";
            setValidationError(Constants.MODERATOR_INVALID_EMAIL, errorMsg);
        }

        if (motivation == null || motivation.trim().length() == 0){
            logger.debug("Validation error regarding motivation.");
            status = false;
            String errorMsg = "Motivation must be set.";
            setValidationError(Constants.MODERATOR_INVALID_MOTIVATION, errorMsg);
        }

        if (!username.matches(Constants.ACCOUNT_NAME_PATTERN)){
            logger.debug("Validation error regarding username.");
            status = false;
            String errorMsg = "Invalid name";
            setValidationError(Constants.MODERATOR_INVALID_NAME, errorMsg);
        }

        if (firstName.length() > Constants.MODERATOR_NAME_MAX_LENGTH){
            logger.debug("Validation error regarding first name.");
            status = false;
            String errorMsg = "Invalid first name";
            setValidationError(Constants.MODERATOR_INVALID_FIRST_NAME, errorMsg);
        }

        if (lastName.length() > Constants.MODERATOR_NAME_MAX_LENGTH){
            logger.debug("Validation error regarding last name.");
            status = false;
            String errorMsg = "Invalid last name";
            setValidationError(Constants.MODERATOR_INVALID_LAST_NAME, errorMsg);
        }

        if (motivation.length() > Constants.MOTIVATION_TEXT_MAX_LENGTH){
            logger.debug("Validation error regarding motivation.");
            status = false;
            String errorMsg = "Invalid motivation";
            setValidationError(Constants.MODERATOR_INVALID_MOTIVATION, errorMsg);
        }

        return status;
    }

    /**
     * Adds validation errors to the request context based on the error code.
     *
     * @param errorCode The error code of the validation error.
     */
    private void setValidationError (int errorCode, String errorMessage)
    {
        switch (errorCode){
            case Constants.MODERATOR_DATA_INCOMPLETE:
                // TODO
                break;
            case Constants.MODERATOR_INVALID_EMAIL:
                // Add validation error for email.
                requestContext.addToRequestContext("registerEmailValidationError", errorMessage);
                break;
            case Constants.MODERATOR_INVALID_FIRST_NAME:
                // Add validation error for first name.
                requestContext.addToRequestContext("registerFirstNameValidationError", errorMessage);
                break;
            case Constants.MODERATOR_INVALID_LAST_NAME:
                // Add validation error for last name.
                requestContext.addToRequestContext("registerLastNameValidationError", errorMessage);
                break;
            case Constants.MODERATOR_INVALID_MOTIVATION:
                // Add validation error for motivation.
                requestContext.addToRequestContext("registerMotivationValidationError", errorMessage);
                break;
            case Constants.MODERATOR_INVALID_NAME:
                // Add validation error for username.
                requestContext.addToRequestContext("registerNameValidationError", errorMessage);
                break;
            case Constants.MODERATOR_INVALID_PASSWORD:
                // Provide validation error message.
                requestContext.addToRequestContext("registerPasswordValidationError", errorMessage);
                break;
            default:
                break;
        }
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
