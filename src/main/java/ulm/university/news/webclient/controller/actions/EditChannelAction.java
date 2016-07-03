package ulm.university.news.webclient.controller.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.api.ChannelAPI;
import ulm.university.news.webclient.controller.context.RequestContextManager;
import ulm.university.news.webclient.controller.interfaces.Action;
import ulm.university.news.webclient.data.*;
import ulm.university.news.webclient.data.enums.ChannelType;
import ulm.university.news.webclient.data.enums.Faculty;
import ulm.university.news.webclient.util.Constants;
import ulm.university.news.webclient.util.Translator;
import ulm.university.news.webclient.util.exceptions.APIException;
import ulm.university.news.webclient.util.exceptions.ServerException;
import ulm.university.news.webclient.util.exceptions.SessionIsExpiredException;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

/**
 * The implementation of the Action interface that realizes the editing process for editing a channel.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class EditChannelAction implements Action {
    /** An instance of the Logger class which performs logging for the EditChannelAction class. */
    private static final Logger logger = LoggerFactory.getLogger(EditChannelAction.class);

    /** The request context of the current request. */
    private RequestContextManager requestContext;

    /**
     * This method executes the business logic to edit a specified channel instance.
     *
     * @param requestContext The context of the request for which the execution is triggered.
     * @return Returns the status that is used to determine the view that should be displayed after execution.
     * @throws SessionIsExpiredException If the session of the user is expired.
     */
    public String execute(RequestContextManager requestContext) throws SessionIsExpiredException, ServerException {
        String status = Constants.CHANNEL_DETAILS_EDITED_CHANNEL;
        this.requestContext = requestContext;
        ChannelAPI channelAPI = new ChannelAPI();
        String task = requestContext.getRequestParameter("task");

        Moderator activeModerator = requestContext.retrieveRequestor();
        Channel currentChannel = (Channel) requestContext.retrieveFromSession("currentChannel");
        if (activeModerator != null && currentChannel != null && task.equals("save")) {
            // Extract parameters and place them in channel object.
            Channel enteredData = extractChannelObjectFromRequestParameters(requestContext);

            // First of all, validate the data.
            // TODO validate subclass properties.
            boolean validationSuccessful = validateRegistrationParameters(enteredData);
            if (!validationSuccessful) {
                // TODO change to not return here. Remove line below.
                requestContext.addToRequestContext("editableChannel", enteredData);
                return Constants.CHANNEL_DETAILS_EDITING_FAILED;
            }

            // Retrieve the latest channel data as the reference from the server.
            Channel referenceChannel = null;
            try {
                referenceChannel = channelAPI.getChannel(activeModerator.getServerAccessToken(),
                        currentChannel.getId());
            } catch (APIException ex) {
                String errorMessage;
                status = Constants.CHANNEL_DETAILS_EDITING_FAILED;

                errorMessage = getErrorMessage(requestContext.retrieveLocale(), ex.getErrorCode());
                if (errorMessage == null) {
                    throw new ServerException(ex.getErrorCode(), "Failed to get latest channel info.");
                }

                // Add error message to request context.
                requestContext.addToRequestContext("channelDetailsEditingFailed", errorMessage);
            }

            // Compare the two instances and create object for update request.
            Channel updatableChannel = createUpdatableChannelObject(referenceChannel, enteredData);
            if (updatableChannel == null) {
                logger.info("No changes detected. No channel update.");
                status = Constants.CHANNEL_DETAILS_NO_UPDATE;

                // Add error to request context.
                String errorMsg = Translator.getInstance().getText(requestContext.retrieveLocale(),
                        "channelDetails.noUpdate.warning");
                requestContext.addToRequestContext("channelDetailsNoUpdate", errorMsg);
            } else {
                try {
                    // Send update request.
                    // Set the entered data object as the updated object to get the data back to the view.
                    enteredData = channelAPI.updateChannel(activeModerator.getServerAccessToken(),
                            currentChannel.getId(), updatableChannel);

                    logger.info("Successfully updated the channel with id {}.", updatableChannel.getId());
                } catch (APIException ex) {
                    String errorMessage;
                    status = Constants.CHANNEL_DETAILS_EDITING_FAILED;

                    errorMessage = getErrorMessage(requestContext.retrieveLocale(), ex.getErrorCode());
                    if (errorMessage == null) {
                        throw new ServerException(ex.getErrorCode(), "Failed to update channel info.");
                    }

                    // Add error message to request context.
                    requestContext.addToRequestContext("channelDetailsEditingFailed", errorMessage);
                }
            }

            // Set object with data to request context. Otherwise the entered data is lost and needs to be reentered.
            requestContext.addToRequestContext("editableChannel", enteredData);
        }

        return status;
    }

    /**
     * Evaluates the data fields of the channel object against the validation rules. Returns
     * the status of the validation. Possible validation errors are stored in the request context.
     *
     * @param enteredData The provided data in form of a channel object.
     * @return Returns true, if no validation error has occurred, otherwise false.
     */
    private boolean validateRegistrationParameters(Channel enteredData){
        boolean validationStatus = true;
        Translator translator = Translator.getInstance();

        if (enteredData.getName() == null || enteredData.getName().trim().length() == 0){
            validationStatus = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "channel.form.validationError.missingName");
            setValidationError(Constants.CHANNEL_INVALID_NAME, errorMsg);
        }

        if (enteredData.getTerm() == null || enteredData.getTerm().trim().length() == 0){
            validationStatus = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "channel.form.validationError.missingTerm");
            setValidationError(Constants.CHANNEL_INVALID_TERM, errorMsg);
        }

        if (enteredData.getContacts() == null || enteredData.getContacts().trim().length() == 0){
            validationStatus = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "channel.form.validationError.missingContacts");
            setValidationError(Constants.CHANNEL_INVALID_CONTACTS, errorMsg);
        }

        if (enteredData.getName() != null && !enteredData.getName().matches(Constants.NAME_PATTERN)) {
            validationStatus = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "channel.form.validationError.invalidName");
            setValidationError(Constants.CHANNEL_INVALID_NAME, errorMsg);
        }

        if (enteredData.getTerm() != null && !enteredData.getTerm().matches(Constants.TERM_PATTERN)) {
            validationStatus = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "channel.form.validationError.invalidTerm");
            setValidationError(Constants.CHANNEL_INVALID_TERM, errorMsg);
        }

        if (enteredData.getContacts() != null &&
                enteredData.getContacts().length() > Constants.CHANNEL_CONTACTS_MAX_LENGTH) {
            validationStatus = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "channel.form.validationError.invalidContacts");
            setValidationError(Constants.CHANNEL_INVALID_CONTACTS, errorMsg);
        }

        if (enteredData.getDescription() != null &&
                enteredData.getDescription().length() > Constants.DESCRIPTION_MAX_LENGTH) {
            validationStatus = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "channel.form.validationError.invalidDescription");
            setValidationError(Constants.CHANNEL_INVALID_DESCRIPTION, errorMsg);
        }

        if (enteredData.getLocations() != null &&
                enteredData.getLocations().length() > Constants.CHANNEL_LOCATIONS_MAX_LENGTH) {
            validationStatus = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "channel.form.validationError.invalidLocations");
            setValidationError(Constants.CHANNEL_INVALID_LOCATIONS, errorMsg);
        }

        if (enteredData.getDates() != null &&
                enteredData.getDates().length() > Constants.CHANNEL_DATES_MAX_LENGTH) {
            validationStatus = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "channel.form.validationError.invalidDates");
            setValidationError(Constants.CHANNEL_INVALID_DATES, errorMsg);
        }

        if (enteredData.getWebsite() != null &&
                enteredData.getWebsite().length() > Constants.CHANNEL_WEBSITE_MAX_LENGTH) {
            validationStatus = false;
            String errorMsg = translator.getText(requestContext.retrieveLocale(),
                    "channel.form.validationError.invalidWebsite");
            setValidationError(Constants.CHANNEL_INVALID_WEBSITE, errorMsg);
        }

        return validationStatus;
    }

    /**
     * Adds validation errors to the request context based on the error code.
     *
     * @param errorCode The error code of the validation error.
     */
    private void setValidationError (int errorCode, String errorMessage)
    {
        switch (errorCode){
            case Constants.CHANNEL_DATA_INCOMPLETE:
                requestContext.addToRequestContext("channelDetailsEditingFailed", errorMessage);
                break;
            case Constants.CHANNEL_INVALID_NAME:
                requestContext.addToRequestContext("channelNameValidationError", errorMessage);
                break;
            case Constants.CHANNEL_INVALID_TERM:
                requestContext.addToRequestContext("termValidationError", errorMessage);
                break;
            case Constants.CHANNEL_INVALID_DESCRIPTION:
                requestContext.addToRequestContext("descriptionValidationError", errorMessage);
                break;
            case Constants.CHANNEL_INVALID_CONTACTS:
                requestContext.addToRequestContext("contactsValidationError", errorMessage);
                break;
            case Constants.CHANNEL_INVALID_LOCATIONS:
                requestContext.addToRequestContext("locationsValidationError", errorMessage);
                break;
            case Constants.CHANNEL_INVALID_DATES:
                requestContext.addToRequestContext("datesValidationError", errorMessage);
                break;
            case Constants.CHANNEL_INVALID_WEBSITE:
                requestContext.addToRequestContext("websiteValidationError", errorMessage);
                break;
            default:
                break;
        }
    }

    /**
     * Retrieves the corresponding error message in the desired language depending
     * on the error code.
     *
     * @param currentLocale The locale object indicating the desired language.
     * @param errorCode The error code.
     * @return The error message.
     */
    private String getErrorMessage(Locale currentLocale, int errorCode) {
        String errorMessage;
        switch (errorCode) {
            case Constants.CHANNEL_DATA_INCOMPLETE:
                errorMessage = Translator.getInstance().getText(currentLocale,
                        "general.message.error.channelDataIncomplete");
                break;
            case Constants.CHANNEL_NOT_FOUND:
                errorMessage = Translator.getInstance().getText(currentLocale,
                        "general.message.error.channelNotFound");
                break;
            case Constants.MODERATOR_FORBIDDEN:
                errorMessage = Translator.getInstance().getText(currentLocale,
                        "general.message.error.forbidden");
                break;
            case Constants.CONNECTION_FAILURE:
                errorMessage = Translator.getInstance().getText(currentLocale,
                        "general.message.error.connectionFailure");
                break;
            case Constants.DATABASE_FAILURE:
                errorMessage = Translator.getInstance().getText(currentLocale,
                        "general.message.error.databaseFailure");
                break;
            default:
                errorMessage = null;
        }

        return errorMessage;
    }

    /**
     * Creates an object of type Channel with all the data
     * that needs to be updated.
     *
     * @param oldChannel The reference channel instance that is used for the comparison.
     * @param newChannel The channel instance with the entered data that may contain new data.
     * @return A channel object that contains all data that requires an update. If no data is changed, the method
     * returns null.
     */
    private Channel createUpdatableChannelObject(Channel oldChannel, Channel newChannel) {
        Channel updatableChannel = null;
        if (oldChannel.getType() == ChannelType.LECTURE) {
            updatableChannel = new Lecture();
        } else if (oldChannel.getType() == ChannelType.EVENT) {
            updatableChannel = new Event();
        } else if (oldChannel.getType() == ChannelType.SPORTS) {
            updatableChannel = new Sports();
        } else {
            updatableChannel = new Channel();
        }

        // Necessary to set the type.
        updatableChannel.setType(oldChannel.getType());

        boolean hasChanged = false;

        if ((oldChannel.getName() == null && newChannel.getName() != null) ||
                (oldChannel.getName() != null && newChannel.getName() != null &&
                !oldChannel.getName().equals(newChannel.getName()))){
            hasChanged = true;
            updatableChannel.setName(newChannel.getName());
        }

        if ((oldChannel.getTerm() == null && newChannel.getTerm() != null) ||
                (oldChannel.getTerm() != null && newChannel.getTerm() != null &&
                !oldChannel.getTerm().equals(newChannel.getTerm()))){
            hasChanged = true;
            updatableChannel.setTerm(newChannel.getTerm());
        }

        if ((oldChannel.getDescription() == null && newChannel.getDescription() != null) ||
                (oldChannel.getDescription() != null && newChannel.getDescription() != null &&
                !oldChannel.getDescription().equals(newChannel.getDescription()))) {
            hasChanged = true;
            updatableChannel.setDescription(newChannel.getDescription());
        }

        if ((oldChannel.getDates() == null && newChannel.getDates() != null) ||
                (oldChannel.getDates() != null && newChannel.getDates() != null &&
                !oldChannel.getDates().equals(newChannel.getDates()))) {
            hasChanged = true;
            updatableChannel.setDates(newChannel.getDates());
        }

        if ((oldChannel.getLocations() == null && newChannel.getLocations() != null) ||
                (oldChannel.getLocations() != null && newChannel.getLocations() != null &&
                !oldChannel.getLocations().equals(newChannel.getLocations()))) {
            hasChanged = true;
            updatableChannel.setLocations(newChannel.getLocations());
        }

        if ((oldChannel.getWebsite() == null && newChannel.getWebsite() != null) ||
                (oldChannel.getWebsite() != null && newChannel.getWebsite() != null &&
                !oldChannel.getWebsite().equals(newChannel.getWebsite()))) {
            hasChanged = true;
            updatableChannel.setWebsite(newChannel.getWebsite());
        }

        if ((oldChannel.getContacts() == null && newChannel.getContacts() != null) ||
                (oldChannel.getContacts() != null && newChannel.getContacts() != null &&
                !oldChannel.getContacts().equals(newChannel.getContacts()))) {
            hasChanged = true;
            updatableChannel.setContacts(newChannel.getContacts());
        }

        if (oldChannel.getType() == ChannelType.LECTURE) {
            Lecture oldLecture = (Lecture) oldChannel;
            Lecture newLecture = (Lecture) newChannel;
            Lecture updatableLecture = (Lecture) updatableChannel;

            if ((oldLecture.getLecturer() == null && newLecture.getLecturer() != null) ||
                    (oldLecture.getLecturer() != null && newLecture.getLecturer() != null &&
                    !oldLecture.getLecturer().equals(newLecture.getLecturer()))) {
                hasChanged = true;
                updatableLecture.setLecturer(newLecture.getLecturer());
            }

            if ((oldLecture.getAssistant() == null && newLecture.getAssistant() != null) ||
                    (oldLecture.getAssistant() != null && newLecture.getAssistant() != null &&
                    !oldLecture.getAssistant().equals(newLecture.getAssistant()))) {
                hasChanged = true;
                updatableLecture.setAssistant(newLecture.getAssistant());
            }

            if ((oldLecture.getStartDate() == null && newLecture.getStartDate() != null) ||
                    (oldLecture.getStartDate() != null && newLecture.getStartDate() != null &&
                    !oldLecture.getStartDate().equals(newLecture.getStartDate()))) {
                hasChanged = true;
                updatableLecture.setStartDate(newLecture.getStartDate());
            }

            if ((oldLecture.getEndDate() == null && newLecture.getEndDate() != null) ||
                    (oldLecture.getEndDate() != null && newLecture.getEndDate() != null &&
                    !oldLecture.getEndDate().equals(newLecture.getEndDate()))) {
                hasChanged = true;
                updatableLecture.setEndDate(newLecture.getEndDate());
            }

            if (!hasChanged) {
                // Set instance to null if no change.
                updatableLecture = null;
            }

            return updatableLecture;
        }

        if (oldChannel.getType() == ChannelType.EVENT) {
            Event oldEvent = null;
            if (oldChannel instanceof Event) {
                oldEvent = (Event) oldChannel;
            } else {
                logger.error("No event object passed.");
            }
            Event newEvent = null;
            if (newChannel instanceof Event) {
                newEvent = (Event) newChannel;
            } else {
                logger.error("No event object passed.");
            }
            Event updatableEvent = (Event) updatableChannel;

            logger.info("cost: old: {} and new: {}.", oldEvent.getCost(), newEvent.getCost());
            logger.info("organizer: old: {} and new: {}.", oldEvent.getOrganizer(), newEvent.getOrganizer());

            if ((oldEvent.getCost() == null && newEvent.getCost() != null) ||
                    (oldEvent.getCost() != null && newEvent.getCost() != null &&
                    !oldEvent.getCost().equals(newEvent.getCost()))) {
                hasChanged = true;
                updatableEvent.setCost(newEvent.getCost());
                logger.info("Setting cost.");
            }

            if ((oldEvent.getOrganizer() == null && newEvent.getOrganizer() != null) ||
                    (oldEvent.getOrganizer() != null && newEvent.getOrganizer() != null &&
                    !oldEvent.getOrganizer().equals(newEvent.getOrganizer()))) {
                hasChanged = true;
                updatableEvent.setOrganizer(newEvent.getOrganizer());
                logger.info("Setting organizer.");
            }

            if (!hasChanged) {
                // Set instance to null if no change.
                updatableEvent = null;
            }

            return updatableEvent;
        }

        if (oldChannel.getType() == ChannelType.SPORTS) {
            Sports oldSports = (Sports) oldChannel;
            Sports newSports = (Sports) newChannel;
            Sports updatableSportsObj = (Sports) updatableChannel;

            if ((oldSports.getCost() == null && newSports.getCost() != null) ||
                    (oldSports.getCost() != null && newSports.getCost() != null &&
                    !oldSports.getCost().equals(newSports.getCost()))) {
                hasChanged = true;
                updatableSportsObj.setCost(newSports.getCost());
            }

            if ((oldSports.getNumberOfParticipants() == null && oldSports.getNumberOfParticipants() != null) ||
                    (oldSports.getNumberOfParticipants() != null && newSports.getNumberOfParticipants() != null &&
                    !oldSports.getNumberOfParticipants().equals(newSports.getNumberOfParticipants()))) {
                hasChanged = true;
                updatableSportsObj.setNumberOfParticipants(newSports.getNumberOfParticipants());
            }

            if (!hasChanged) {
                // Set instance to null if no change.
                updatableSportsObj = null;
            }

            return updatableSportsObj;
        }

        if (!hasChanged) {
            // Set instance to null if no change.
            updatableChannel = null;
        }

        return updatableChannel;
    }

    /**
     * Extracts the entered data from the request parameter and creates a
     * channel object containing the entered data.
     *
     * @param requestContext The request context.
     * @return An object of type Channel which contains the data.
     */
    private Channel extractChannelObjectFromRequestParameters(RequestContextManager requestContext){
        // Extract parameters.
        String channelName = encodeToUTF8(requestContext.getRequestParameter("channelName"));
        ChannelType channelType = ChannelType.valueOf(encodeToUTF8(requestContext.getRequestParameter("channelType")));
        String description = encodeToUTF8(requestContext.getRequestParameter("description"));
        String termInfo = encodeToUTF8(requestContext.getRequestParameter("termPicker"));
        String termYear = encodeToUTF8(requestContext.getRequestParameter("yearPicker"));
        String locations = encodeToUTF8(requestContext.getRequestParameter("locations"));
        String dates = encodeToUTF8(requestContext.getRequestParameter("dates"));
        String contacts = encodeToUTF8(requestContext.getRequestParameter("contacts"));
        String website = encodeToUTF8(requestContext.getRequestParameter("website"));

        // Create channel object with entered data.
        Channel enteredData = new Channel();
        enteredData.setName(channelName);
        enteredData.setType(channelType);
        enteredData.setDescription(description);
        enteredData.setLocations(locations);
        enteredData.setDates(dates);
        enteredData.setContacts(contacts);
        enteredData.setWebsite(website);

        String term = "";
        if (termInfo.trim().toLowerCase().equals("summer")) {
            term = "S" + termYear;
        } else if (termInfo.trim().toLowerCase().equals("winter")) {
            term = "W" + termYear;
        }
        enteredData.setTerm(term.trim());

        if (channelType == ChannelType.LECTURE) {
           // Faculty faculty = Faculty.valueOf(requestContext.getRequestParameter("faculty"));
            String lecturer = encodeToUTF8(requestContext.getRequestParameter("lecturer"));
            String assistant = encodeToUTF8(requestContext.getRequestParameter("assistant"));
            String startDate = encodeToUTF8(requestContext.getRequestParameter("startDate"));
            String endDate = encodeToUTF8(requestContext.getRequestParameter("endDate"));

            Lecture lecture = new Lecture();
            lecture.setTerm(enteredData.getTerm());
            lecture.setName(enteredData.getName());
            lecture.setType(enteredData.getType());
            lecture.setDescription(enteredData.getDescription());
            lecture.setLocations(enteredData.getLocations());
            lecture.setDates(enteredData.getDates());
            lecture.setContacts(enteredData.getContacts());
            lecture.setWebsite(enteredData.getWebsite());

         //   lecture.setFaculty(faculty);
            lecture.setLecturer(lecturer);
            lecture.setAssistant(assistant);
            lecture.setStartDate(startDate);
            lecture.setEndDate(endDate);

            return lecture;
        }

        if (channelType == ChannelType.EVENT) {
            String cost = encodeToUTF8(requestContext.getRequestParameter("eventCost"));
            String organizer = encodeToUTF8(requestContext.getRequestParameter("organizer"));

            Event event = new Event();
            event.setTerm(enteredData.getTerm());
            event.setName(enteredData.getName());
            event.setType(enteredData.getType());
            event.setDescription(enteredData.getDescription());
            event.setLocations(enteredData.getLocations());
            event.setDates(enteredData.getDates());
            event.setContacts(enteredData.getContacts());
            event.setWebsite(enteredData.getWebsite());

            event.setCost(cost);
            event.setOrganizer(organizer);

            return event;
        }

        if (channelType == ChannelType.SPORTS) {
            String cost = encodeToUTF8(requestContext.getRequestParameter("sportsCost"));
            String numberOfParticipants = encodeToUTF8(requestContext.getRequestParameter("numberOfParticipants"));

            Sports sports = new Sports();
            sports.setTerm(enteredData.getTerm());
            sports.setName(enteredData.getName());
            sports.setType(enteredData.getType());
            sports.setDescription(enteredData.getDescription());
            sports.setLocations(enteredData.getLocations());
            sports.setDates(enteredData.getDates());
            sports.setContacts(enteredData.getContacts());
            sports.setWebsite(enteredData.getWebsite());

            sports.setCost(cost);
            sports.setNumberOfParticipants(numberOfParticipants);

            return sports;
        }

        return enteredData;
    }

    /**
     * Encode a string to UTF-8 encoding.
     *
     * @param text The string that should be encoded.
     * @return The UTF-8 encoded string.
     */
    private String encodeToUTF8(String text){
        String output = "";
        try {
            byte[] utf8Bytes = text.getBytes("UTF8");

            output = new String(utf8Bytes, "UTF8");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error("Couldn't encode to UTF-8.");
        }

        return output;
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
