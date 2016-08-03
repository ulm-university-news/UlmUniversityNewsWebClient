package ulm.university.news.webclient.controller.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.api.ModeratorAPI;
import ulm.university.news.webclient.controller.context.RequestContextManager;
import ulm.university.news.webclient.controller.interfaces.Action;
import ulm.university.news.webclient.data.Moderator;
import ulm.university.news.webclient.util.Constants;
import ulm.university.news.webclient.util.ModeratorUtil;
import ulm.university.news.webclient.util.Translator;
import ulm.university.news.webclient.util.exceptions.APIException;
import ulm.university.news.webclient.util.exceptions.ServerException;
import ulm.university.news.webclient.util.exceptions.SessionIsExpiredException;

/**
 * TODO
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class MyAccountEditAction implements Action {

    /** An instance of the Logger class which performs logging for the MyAccountEditAction class. */
    private static final Logger logger = LoggerFactory.getLogger(MyAccountEditAction.class);

    /**
     * Reference to the request context that belongs to the request which has triggered the execution of this action
     * in the first place.
     */
    private RequestContextManager requestContext;

    /**
     * This method executes the business logic to edit account information or change the password of the logged in
     * moderator from the server.
     *
     * @param requestContext The context of the request for which the execution is triggered.
     * @return Returns the status that is used to determine the view that should be displayed after execution.
     * @throws SessionIsExpiredException If the session of the user is expired.
     */
    public String execute(RequestContextManager requestContext) throws SessionIsExpiredException, ServerException {
        logger.debug("In MyAccountEditAction");
        this.requestContext = requestContext;
        Moderator activeModerator = requestContext.retrieveRequestor();
        ModeratorAPI moderatorAPI;

        if (activeModerator != null) {
            moderatorAPI = new ModeratorAPI();
            String message;
            try {
                // Check if user attempts to change the account information or the password.
                String passwordChange = requestContext.getRequestParameter("password");
                if (passwordChange != null) {
                    // *** User wants to change the password. ***
                    String passwordCurrent, passwordNew, passwordNewAgain;
                    passwordCurrent = requestContext.getRequestParameter("passwordCurrent");
                    passwordNew = requestContext.getRequestParameter("passwordNew");
                    passwordNewAgain = requestContext.getRequestParameter("passwordNewAgain");

                    if (validatePasswordParameters(passwordCurrent, passwordNew, passwordNewAgain)) {
                        // Check if current password is correct.
                        moderatorAPI.login(activeModerator.getName(), ModeratorUtil.hashPassword(passwordCurrent));
                        // Hash the new password.
                        Moderator m = new Moderator();
                        m.setId(activeModerator.getId());
                        m.setPassword(ModeratorUtil.hashPassword(passwordNew));
                        moderatorAPI.changeModerator(activeModerator.getServerAccessToken(), m);
                        message = Translator.getInstance().getText(requestContext.retrieveLocale(),
                                "myAccountEdit.password.successful");
                        requestContext.storeInSession("editSuccess", message);
                    } else {
                        return Constants.MY_ACCOUNT_EDIT_PASSWORD_VALIDATION_ERROR;
                    }
                } else {
                    // *** User wants to change the account data. ***
                    // First, extract the sent parameters.
                    String username, firstName, lastName, email, motivation;
                    username = requestContext.getRequestParameter("username");
                    firstName = requestContext.getRequestParameter("firstname");
                    lastName = requestContext.getRequestParameter("lastname");
                    email = requestContext.getRequestParameter("email");
                    motivation = requestContext.getRequestParameter("motivation");
                    boolean edited = false;

                    Moderator m = new Moderator();
                    m.setId(activeModerator.getId());
                    if (!activeModerator.getFirstName().equals(firstName)) {
                        m.setFirstName(firstName);
                        edited = true;
                    }
                    if (!activeModerator.getLastName().equals(lastName)) {
                        m.setLastName(lastName);
                        edited = true;
                    }
                    if (!activeModerator.getEmail().equals(email)) {
                        m.setEmail(email);
                        edited = true;
                    }
                    /*
                    if(!activeModerator.getName().equals(username)){
                        m.setName(username);
                    }
                    if(!activeModerator.getMotivation().equals(motivation)){
                        m.setMotivation(motivation);
                    }
                    */

                    if (edited) {
                        if (validateAccountDataParameters(firstName, lastName, email)) {
                            logger.debug("Edit {}", m.toString());
                            Moderator moderator = moderatorAPI.changeModerator(
                                    activeModerator.getServerAccessToken(), m);
                            moderator.setServerAccessToken(activeModerator.getServerAccessToken());
                            requestContext.storeRequestorInSession(moderator);
                            message = Translator.getInstance().getText(requestContext.retrieveLocale(),
                                    "myAccount.edit.success", moderator.getFirstName() + " " + moderator.getLastName());
                            requestContext.storeInSession("editSuccess", message);
                        } else {
                            return Constants.MY_ACCOUNT_EDIT_EDIT_FAILED;
                        }
                    } else {
                        message = Translator.getInstance().getText(requestContext.retrieveLocale(),
                                "myAccount.edit.notEdited");
                        requestContext.storeInSession("editInfo", message);
                        return Constants.MY_ACCOUNT_EDIT_EDIT_FAILED;
                    }
                }
            } catch (APIException e) {
                logger.error("Edit my account request failed. Error code is {}.", e.getErrorCode());
                String errorMessage;
                switch (e.getErrorCode()) {
                    case Constants.MODERATOR_NOT_FOUND:
                        errorMessage = Translator.getInstance().getText(requestContext.retrieveLocale(),
                                "moderator.notfound");
                        break;
                    case Constants.MODERATOR_FORBIDDEN:
                        errorMessage = Translator.getInstance().getText(requestContext.retrieveLocale(),
                                "general.message.error.forbidden");
                        break;
                    case Constants.MODERATOR_UNAUTHORIZED:
                        errorMessage = Translator.getInstance().getText(requestContext.retrieveLocale(),
                                "myAccountEdit.password.dialog.unauthorized");
                        requestContext.storeInSession("passwordCurrentValidationError", errorMessage);
                        return Constants.MY_ACCOUNT_EDIT_EDIT_FAILED;
                    default:
                        errorMessage = Translator.getInstance().getText(requestContext.retrieveLocale(),
                                "general.message.error.fatal");
                }

                requestContext.storeInSession("editError", errorMessage);
                return Constants.MY_ACCOUNT_EDIT_EDIT_FAILED;
            }
        }

        return Constants.MY_ACCOUNT_EDIT_EDITED;
    }

    /**
     * Executes the validation of the entered password data. Returns the status of the validation.
     *
     * @param currentPassword The entered current password of the moderator.
     * @param newPassword The entered new password of the moderator.
     * @param newPasswordAgain The reentered new password of the moderator.
     * @return Returns true if the validation was successful, otherwise false.
     */
    private boolean validatePasswordParameters(String currentPassword, String newPassword, String
            newPasswordAgain) {
        Translator translator = Translator.getInstance();
        boolean status = true;

        logger.debug("currentPassword: {} and newPassword: {} and newPasswordAgain: {}", currentPassword,
                newPassword, newPasswordAgain);

        if (currentPassword == null || currentPassword.trim().length() == 0) {
            logger.debug("Validation error regarding currentPassword.");
            status = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "myAccountEdit.password.dialog.current.validation.empty");
            requestContext.addToRequestContext("passwordCurrentValidationError", errorMsg);
        }

        if (newPassword == null || newPassword.trim().length() == 0) {
            logger.debug("Validation error regarding newPassword.");
            status = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "myAccountEdit.password.dialog.new.validation.empty");
            requestContext.addToRequestContext("passwordNewValidationError", errorMsg);
        }

        if (newPasswordAgain == null || newPasswordAgain.trim().length() == 0) {
            logger.debug("Validation error regarding newPasswordAgain.");
            status = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "myAccountEdit.password.dialog.newAgain.validation.empty");
            requestContext.addToRequestContext("passwordNewAgainValidationError", errorMsg);
        }

        if (newPassword == null || newPasswordAgain == null || !newPassword.equals(newPasswordAgain)) {
            status = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "myAccountEdit.password.dialog.newAgain.validation.mismatch");
            requestContext.addToRequestContext("passwordNewAgainValidationError", errorMsg);
        }

        return status;
    }

    /**
     * Validates the account data parameters. Returns the validation status.
     *
     * @param firstName The entered first name.
     * @param lastName The entered last name.
     * @param email The entered email.
     * @return Returns true, if validation is successful, returns false if validation errors occur.
     */
    private boolean validateAccountDataParameters(String firstName, String lastName, String email)
            throws SessionIsExpiredException {
        Translator translator = Translator.getInstance();
        boolean status = true;

        if (firstName == null || firstName.trim().length() == 0) {
            logger.debug("Validation error regarding first name.");
            status = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "register.form.validationError.missingFirstName");
            setValidationError(Constants.MODERATOR_INVALID_FIRST_NAME, errorMsg);
        }

        if (lastName == null || lastName.trim().length() == 0) {
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

        if (firstName != null && firstName.length() > Constants.MODERATOR_NAME_MAX_LENGTH) {
            logger.debug("Validation error regarding first name.");
            status = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "register.form.validationError.invalidFirstName");
            setValidationError(Constants.MODERATOR_INVALID_FIRST_NAME, errorMsg);
        }

        if (lastName != null && lastName.length() > Constants.MODERATOR_NAME_MAX_LENGTH) {
            logger.debug("Validation error regarding last name.");
            status = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "register.form.validationError.invalidLastName");
            setValidationError(Constants.MODERATOR_INVALID_LAST_NAME, errorMsg);
        }

        return status;
    }

    /**
     * Adds validation errors to the request context based on the error code.
     *
     * @param errorCode The error code of the validation error.
     */
    private void setValidationError(int errorCode, String errorMessage) {
        switch (errorCode) {
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
        return true;
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
