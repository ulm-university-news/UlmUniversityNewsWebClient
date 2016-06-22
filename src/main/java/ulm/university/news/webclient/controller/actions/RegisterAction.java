package ulm.university.news.webclient.controller.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.api.ModeratorAPI;
import ulm.university.news.webclient.controller.context.RequestContextManager;
import ulm.university.news.webclient.controller.interfaces.Action;
import ulm.university.news.webclient.data.Moderator;
import ulm.university.news.webclient.data.enums.Language;
import ulm.university.news.webclient.util.Constants;
import ulm.university.news.webclient.util.Translator;
import ulm.university.news.webclient.util.exceptions.APIException;
import ulm.university.news.webclient.util.exceptions.ServerException;
import ulm.university.news.webclient.util.exceptions.SessionIsExpiredException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

        String status = "";

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
                status = Constants.VALIDATION_FAILED;
            }

            if (!validateRegistrationParameters(username, firstName, lastName, email, motivation))
            {
                status = Constants.VALIDATION_FAILED;
            }

            if (!status.equals(Constants.VALIDATION_FAILED)) {
                // No validation error. Proceed.
                // Determine Language.
                Language preferredLanguage;
                if (currentLocale == Locale.GERMAN){
                    preferredLanguage = Language.GERMAN;
                } else {
                    preferredLanguage = Language.ENGLISH;
                }

                // Hash the password.
                password = hashPassword(password);

                // Create moderator instance for account request.
                Moderator moderator = new Moderator(username, firstName, lastName, email, null, password, motivation,
                        preferredLanguage, null, null, null, null);

                // Send request for account request.
                ModeratorAPI moderatorAPI = new ModeratorAPI();
                try {
                    moderatorAPI.sendCreateAccountRequest(moderator);
                    // Set status to successful.
                    status = Constants.REGISTRATION_SUCCESSFUL;
                } catch (APIException e) {
                    logger.error("Request for account creation failed. The error code is {}.", e.getErrorCode());
                    if (e.getErrorCode() == Constants.MODERATOR_INVALID_NAME ||
                            e.getErrorCode() == Constants.MODERATOR_INVALID_FIRST_NAME ||
                            e.getErrorCode() == Constants.MODERATOR_INVALID_LAST_NAME ||
                            e.getErrorCode() == Constants.MODERATOR_INVALID_EMAIL ||
                            e.getErrorCode() == Constants.MODERATOR_INVALID_MOTIVATION ||
                            e.getErrorCode() == Constants.MODERATOR_DATA_INCOMPLETE){
                        Translator translator = Translator.getInstance();
                        String errorMsg = translator.getText(requestContext.retrieveLocale(),
                                "register.form.validationError.general.rejected");
                        setValidationError(e.getErrorCode(), errorMsg);
                        status = Constants.VALIDATION_FAILED; // Set this also as a validation failure state.
                    } else if (e.getErrorCode() == Constants.MODERATOR_NAME_ALREADY_EXISTS){
                        Translator translator = Translator.getInstance();
                        String errorMsg = translator.getText(requestContext.retrieveLocale(),
                                "register.form.validationError.nameAlreadyExists");
                        setValidationError(e.getErrorCode(), errorMsg);
                        status = Constants.VALIDATION_FAILED;
                    } else if (e.getErrorCode() == Constants.CONNECTION_FAILURE) {
                        // Don't leave dialog on connection failure.
                        Translator translator = Translator.getInstance();
                        String errorMsg = translator.getText(requestContext.retrieveLocale(),
                                "register.info.connectionFailure");
                        requestContext.addToRequestContext("registerRequestFailure", errorMsg);
                        status = Constants.CONNECTION_FAILED_STATUS;
                    } else {
                        logger.error("Cannot handle the error in RegisterAction. Passing it to FrontController.");
                        // Map to ServerException.
                        throw new ServerException(e.getErrorCode(), e);
                    }
                }
            }
        }

        return status;
    }

    /**
     * Determines whether the passwords entered in both input fields are the same.
     *
     * @param password The password from the first input field.
     * @param reEnteredPassword The password from the second input field.
     * @return Returns true, if the passwords match, otherwise false.
     */
    private boolean doesPasswordMatch(String password, String reEnteredPassword) throws SessionIsExpiredException {
        Translator translator = Translator.getInstance();

        if (password == null || password.trim().length() == 0){
            logger.debug("Validation error regarding password. Password not entered");
            String errorMessage = translator.getText(requestContext.retrieveLocale(),
                    "register.form.validationError.missingPassword");
            setValidationError(Constants.MODERATOR_INVALID_PASSWORD, errorMessage);
            return false;
        }

        if (!password.equals(reEnteredPassword)) {
            logger.debug("Validation error regarding password.");
            String errorMessage = translator.getText(requestContext.retrieveLocale(),
                    "register.form.validationError.passwordMismatch");
            setValidationError(Constants.MODERATOR_INVALID_PASSWORD, errorMessage);
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
                                                   String motivation) throws SessionIsExpiredException {
        Translator translator = Translator.getInstance();
        boolean status = true;

        if (username == null || username.trim().length() == 0) {
            logger.debug("Validation error regarding username.");
            status = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "register.form.validationError.missingName");
            setValidationError(Constants.MODERATOR_INVALID_NAME, errorMsg);
        }

        if (firstName == null || firstName.trim().length() == 0){
            logger.debug("Validation error regarding first name.");
            status = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "register.form.validationError.missingFirstName");
            setValidationError(Constants.MODERATOR_INVALID_FIRST_NAME, errorMsg);
        }

        if (lastName == null || lastName.trim().length() == 0){
            logger.debug("Validation error regarding last name.");
            status = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "register.form.validationError.missingLastName");
            setValidationError(Constants.MODERATOR_INVALID_LAST_NAME, errorMsg);
        }

        if (email == null || email.trim().length() == 0) {
            logger.debug("Validation error regarding email.");
            status = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "register.form.validationError.missingEmail");
            setValidationError(Constants.MODERATOR_INVALID_EMAIL, errorMsg);
        }

        if (motivation == null || motivation.trim().length() == 0){
            logger.debug("Validation error regarding motivation.");
            status = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "register.form.validationError.missingMotivation");
            setValidationError(Constants.MODERATOR_INVALID_MOTIVATION, errorMsg);
        }

        if (username != null && !username.matches(Constants.ACCOUNT_NAME_PATTERN)){
            logger.debug("Validation error regarding username.");
            status = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "register.form.validationError.invalidName");
            setValidationError(Constants.MODERATOR_INVALID_NAME, errorMsg);
        }

        if (firstName != null && firstName.length() > Constants.MODERATOR_NAME_MAX_LENGTH){
            logger.debug("Validation error regarding first name.");
            status = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "register.form.validationError.invalidFirstName");
            setValidationError(Constants.MODERATOR_INVALID_FIRST_NAME, errorMsg);
        }

        if (lastName != null && lastName.length() > Constants.MODERATOR_NAME_MAX_LENGTH){
            logger.debug("Validation error regarding last name.");
            status = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "register.form.validationError.invalidLastName");
            setValidationError(Constants.MODERATOR_INVALID_LAST_NAME, errorMsg);
        }

        if (motivation != null && motivation.length() > Constants.MOTIVATION_TEXT_MAX_LENGTH){
            logger.debug("Validation error regarding motivation.");
            status = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "register.form.validationError.invalidMotivation");
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
            case Constants.MODERATOR_NAME_ALREADY_EXISTS:
                // Add validation error for username.
                requestContext.addToRequestContext("registerNameValidationError", errorMessage);
                break;
            default:
                break;
        }
    }

    /**
     * Helper method to generate a hash for the password. The password is
     * hashed using a SHA256 algorithm.
     *
     * @param password The password as a string.
     * @return The generated hash as a string.
     */
    private String hashPassword(String password){
        String hash = "";
        try {
            byte[] passwordBytes = password.getBytes();

            // Calculate hash on the password's byte sequence.
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] token = sha256.digest(passwordBytes);

            // Transform the bytes (8 bit signed) into a hexadecimal format.
            StringBuilder tokenString = new StringBuilder();
            for (int i = 0; i < token.length; i++) {
                /*
                Format parameters: %[flags][width][conversion]
                Flag '0' - The result will be zero padded.
                Width '2' - The width is 2 as 1 byte is represented by two hex characters.
                Conversion 'x' - Result is formatted as hexadecimal integer, uppercase.
                 */
                tokenString.append(String.format("%02x", token[i]));
            }
            hash = tokenString.toString();
            logger.debug("The generated hash is {}", hash);
        }
        catch (NoSuchAlgorithmException ex)
        {
            logger.error("Couldn't perform hashing. No SHA256 algorithm.");
        }

        return hash;
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
