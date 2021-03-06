package ulm.university.news.webclient.api;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.data.*;
import ulm.university.news.webclient.data.enums.ChannelType;
import ulm.university.news.webclient.util.Constants;
import ulm.university.news.webclient.util.exceptions.APIException;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * The channel API contains methods for sending channel related requests
 * to the REST server.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class ChannelAPI extends MainAPI {
    /** The logger instance for the ChannelAPI. */
    private static final Logger logger = LoggerFactory.getLogger(ChannelAPI.class);

    /** The url pointing to the channel resources. */
    private String channelUrl;

    /**
     * Creates an instance of the ChannelAPI class.
     */
    public ChannelAPI(){
        channelUrl = serverUrl + "channel";
    }

    /**
     * Sends a request to create a new channel with the data passed as a parameter. Returns a channel instance
     * with the data of the created channel in cases of a success.
     *
     * @param accessToken The access token of the requestor.
     * @param channelData The data for the channel creation in form of a Channel instance.
     * @return A Channel instance with the data of the new channel provided by the server.
     * @throws APIException Throws an APIException if the request fails or is rejected by the server.
     */
    public Channel createChannel(String accessToken, Channel channelData) throws APIException{
        Channel createdChannel = null;
        String url = channelUrl;

        // Parse parameter data to json.
        String jsonContent = serializeChannelToJson(channelData);

        try{
            URL obj = new URL(url);
            // Set http method.
            connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Content-Type", "application/json");
            setAuthorization(accessToken);
            connection.setDoOutput(true);

            logger.info("Sending POST request to URL: {} with content {}.", url, jsonContent);

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            out.write(jsonContent);
            out.flush();
            out.close();

            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_CREATED) {
                // Get request response as String.
                String json = getResponse(connection);

                createdChannel = gson.fromJson(json, Channel.class);
            } else {
                String serverResponse = getErrorResponse(connection);
                ServerError se = gson.fromJson(serverResponse, ServerError.class);
                // Map to API exception.
                throw new APIException(se.getErrorCode(), connection.getResponseCode(),
                        "Create channel failed.");
            }
        } catch (MalformedURLException malEx) {
            malEx.printStackTrace();
            logger.error("Malformed URL discovered.");
            throw new APIException(Constants.FATAL_ERROR, "URL malformed.");
        } catch (ProtocolException pe) {
            pe.printStackTrace();
            throw new APIException(Constants.FATAL_ERROR, "Protocol exception.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            logger.error("IO exception occurred. Probably due to a failed connection to the server.");
            throw new APIException(Constants.CONNECTION_FAILURE, "Connection failure. Failed to connect to server.");
        }

        return createdChannel;
    }

    /**
     * Requests all channel resources from the server that are managed by the
     * moderator with the specified moderator id.
     *
     * @param accessToken The access token of the requestor.
     * @param moderatorId The id of the moderator whose channels should be requested.
     * @return A list Channel instances.
     * @throws APIException Throws an API exception, if the request fails or is rejected from the server.
     */
    public List<Channel> getMyChannels(String accessToken, int moderatorId)throws APIException {
        List<Channel> myChannels = null;
        String url = channelUrl;

        // Add parameters to url.
        HashMap<String, String> params = new HashMap<String, String>();
        if (moderatorId >= 0) {
            params.put("moderatorId", Integer.toString(moderatorId));
        }
        url += getUrlParams(params);

        try{
            URL obj = new URL(url);
            // Set http method.
            connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            setAuthorization(accessToken);

            logger.info("Sending GET request to URL: {}", url);

            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                // Get request response as String.
                String json = getResponse(connection);

                // Use a list of channels as deserialization type.
                Type listType = new TypeToken<List<Channel>>() {
                }.getType();

                myChannels = gson.fromJson(json, listType);
            } else {
                String serverResponse = getErrorResponse(connection);
                ServerError se = gson.fromJson(serverResponse, ServerError.class);
                // Map to API exception.
                throw new APIException(se.getErrorCode(), connection.getResponseCode(),
                        "Get my channels failed.");
            }
        } catch (MalformedURLException malEx) {
            malEx.printStackTrace();
            logger.error("Malformed URL discovered.");
            throw new APIException(Constants.FATAL_ERROR, "URL malformed.");
        } catch (ProtocolException pe) {
            pe.printStackTrace();
            throw new APIException(Constants.FATAL_ERROR, "Protocol exception.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            logger.error("IO exception occurred. Probably due to a failed connection to the server.");
            throw new APIException(Constants.CONNECTION_FAILURE, "Connection failure. Failed to connect to server.");
        }

        return myChannels;
    }

    /**
     * Requests all channels from the REST server and returns them.
     *
     * @param accessToken The access token of the requestor.
     * @return A list of channel instances.
     * @throws APIException Throws an API exception, if the request fails or is rejected from the server.
     */
    public List<Channel> getAllChannels(String accessToken) throws APIException {
        List<Channel> channels = null;
        String url = channelUrl;

        try{
            URL obj = new URL(url);
            // Set http method.
            connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            setAuthorization(accessToken);

            logger.info("Sending GET request to URL: {}", url);

            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                // Get request response as String.
                String json = getResponse(connection);

                // Use a list of channels as deserialization type.
                Type listType = new TypeToken<List<Channel>>() {
                }.getType();

                channels = gson.fromJson(json, listType);
            } else {
                String serverResponse = getErrorResponse(connection);
                ServerError se = gson.fromJson(serverResponse, ServerError.class);
                // Map to API exception.
                throw new APIException(se.getErrorCode(), connection.getResponseCode(),
                        "Get all channels failed.");
            }
        } catch (MalformedURLException malEx) {
            malEx.printStackTrace();
            logger.error("Malformed URL discovered.");
            throw new APIException(Constants.FATAL_ERROR, "URL malformed.");
        } catch (ProtocolException pe) {
            pe.printStackTrace();
            throw new APIException(Constants.FATAL_ERROR, "Protocol exception.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            logger.error("IO exception occurred. Probably due to a failed connection to the server.");
            throw new APIException(Constants.CONNECTION_FAILURE, "Connection failure. Failed to connect to server.");
        }

        return channels;
    }

    /**
     * Requests the details of the channel which is identified by the specified id from the server.
     *
     * @param accessToken The access token of the requestor.
     * @param channelId The id of the channel.
     * @return An instance of class Channel with the data of the channel.
     * @throws APIException Throws an API exception, if the request fails or is rejected from the server.
     */
    public Channel getChannel(String accessToken, int channelId) throws APIException{
        Channel channel = null;
        String url = channelUrl + "/" + channelId;

        try{
            URL obj = new URL(url);
            // Set http method.
            connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            setAuthorization(accessToken);

            logger.info("Sending GET request to URL: {}", url);

            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                // Get request response as String.
                String json = getResponse(connection);

                channel = gson.fromJson(json, Channel.class);
            } else {
                String serverResponse = getErrorResponse(connection);
                ServerError se = gson.fromJson(serverResponse, ServerError.class);
                // Map to API exception.
                throw new APIException(se.getErrorCode(), connection.getResponseCode(),
                        "Get channel with id " + channelId + " failed.");
            }
        } catch (MalformedURLException malEx) {
            malEx.printStackTrace();
            logger.error("Malformed URL discovered.");
            throw new APIException(Constants.FATAL_ERROR, "URL malformed.");
        } catch (ProtocolException pe) {
            pe.printStackTrace();
            throw new APIException(Constants.FATAL_ERROR, "Protocol exception.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            logger.error("IO exception occurred. Probably due to a failed connection to the server.");
            throw new APIException(Constants.CONNECTION_FAILURE, "Connection failure. Failed to connect to server.");
        }

        return channel;
    }

    /**
     * Sends a request to update the channel with the specified id. The updated data is sent to the
     * server using a JSON Merge Patch format. It contains only the values that need to be updated.
     *
     * @param accessToken The access token of the requestor.
     * @param channelId The id of the channel that needs to be updated.
     * @param updatableChannel The object with the updated data.
     * @return The channel object with the updated data after the request has been successfully updated.
     * @throws APIException Throws an API exception, if the request fails or is rejected from the server.
     */
    public Channel updateChannel(String accessToken, int channelId, Channel updatableChannel) throws APIException {
        Channel channel = null;
        String url = channelUrl + "/" + channelId;

        String jsonContent = serializeChannelToJson(updatableChannel);

        try{
            URL obj = new URL(url);
            // Set http method.
            connection = (HttpURLConnection) obj.openConnection();
            // PATCH method is not allowed in HttpUrlConnection.
            connection.setRequestMethod("POST");
            // Use X-HTTP-Method-Override header to replace POST with PATCH on the REST server.
            connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            setAuthorization(accessToken);

            logger.info("Sending PATCH request to URL: {} with content: {}", url, jsonContent);

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            out.write(jsonContent);
            out.flush();
            out.close();

            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                // Get request response as String.
                String json = getResponse(connection);

                channel = gson.fromJson(json, Channel.class);
            } else {
                String serverResponse = getErrorResponse(connection);
                ServerError se = gson.fromJson(serverResponse, ServerError.class);
                // Map to API exception.
                throw new APIException(se.getErrorCode(), connection.getResponseCode(),
                        "Update channel with id " + channelId + " failed.");
            }
        } catch (MalformedURLException malEx) {
            malEx.printStackTrace();
            logger.error("Malformed URL discovered.");
            throw new APIException(Constants.FATAL_ERROR, "URL malformed.");
        } catch (ProtocolException pe) {
            pe.printStackTrace();
            throw new APIException(Constants.FATAL_ERROR, "Protocol exception.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            logger.error("IO exception occurred. Probably due to a failed connection to the server.");
            throw new APIException(Constants.CONNECTION_FAILURE, "Connection failure. Failed to connect to server.");
        }

        return channel;
    }

    /**
     * Requests the responsible moderators of the channel that is identified by the specified id from the REST server.
     *
     * @param accessToken The access token of the requestor.
     * @param channelId The id of the channel.
     * @return A list of Moderator instances.
     * @throws APIException Throws APIException if request fails or the server rejects the request.
     */
    public List<Moderator> getResponsibleModerators(String accessToken, int channelId) throws APIException {
        List<Moderator> responsibleModerators = null;
        String url = channelUrl + "/" + channelId + "/moderator";

        try{
            URL obj = new URL(url);
            // Set http method.
            connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            setAuthorization(accessToken);

            logger.info("Sending GET request to URL: {}", url);

            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                // Get request response as String.
                String json = getResponse(connection);

                // Use a list of moderators as deserialization type.
                Type listType = new TypeToken<List<Moderator>>() {
                }.getType();

                responsibleModerators = gson.fromJson(json, listType);
            } else {
                String serverResponse = getErrorResponse(connection);
                ServerError se = gson.fromJson(serverResponse, ServerError.class);
                // Map to API exception.
                throw new APIException(se.getErrorCode(), connection.getResponseCode(),
                        "Get responsible moderators failed.");
            }
        } catch (MalformedURLException malEx) {
            malEx.printStackTrace();
            logger.error("Malformed URL discovered.");
            throw new APIException(Constants.FATAL_ERROR, "URL malformed.");
        } catch (ProtocolException pe) {
            pe.printStackTrace();
            throw new APIException(Constants.FATAL_ERROR, "Protocol exception.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            logger.error("IO exception occurred. Probably due to a failed connection to the server.");
            throw new APIException(Constants.CONNECTION_FAILURE, "Connection failure. Failed to connect to server.");
        }

        return responsibleModerators;
    }

    /**
     * Revokes the responsibility of the moderator who is identified by the specified id for the defined channel.
     * After the successful execution of this operation, the specified moderator does not have the right to perform
     * operations on the channel anymore.
     *
     * @param accessToken The access token of the requestor.
     * @param channelId The id of the channel.
     * @param moderatorId The id of the moderator.
     * @throws APIException Throws an API exception if the request fails or the server rejects the request.
     */
    public void revokeModeratorResponsibility(String accessToken, int channelId, int moderatorId) throws APIException{
        String url = channelUrl + "/" + channelId + "/moderator/" + moderatorId;

        try{
            URL obj = new URL(url);
            // Set http method.
            connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("DELETE");
            setAuthorization(accessToken);

            logger.info("Sending DELETE request to URL: {}", url);

            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_NO_CONTENT) {
                // Do nothing.
                logger.info("Revoked responsibility of moderator with id {} from channel with id {}.", moderatorId,
                        channelId);
            } else {
                String serverResponse = getErrorResponse(connection);
                ServerError se = gson.fromJson(serverResponse, ServerError.class);
                // Map to API exception.
                throw new APIException(se.getErrorCode(), connection.getResponseCode(),
                        "Revoking responsibility of moderator with id " + moderatorId + " from channel with id " +
                                channelId +" failed.");
            }
        } catch (MalformedURLException malEx) {
            malEx.printStackTrace();
            logger.error("Malformed URL discovered.");
            throw new APIException(Constants.FATAL_ERROR, "URL malformed.");
        } catch (ProtocolException pe) {
            pe.printStackTrace();
            throw new APIException(Constants.FATAL_ERROR, "Protocol exception.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            logger.error("IO exception occurred. Probably due to a failed connection to the server.");
            throw new APIException(Constants.CONNECTION_FAILURE, "Connection failure. Failed to connect to server.");
        }
    }

    /**
     * Adds the moderator who is identified by the specified id as a responsible moderator for the defined
     * channel. After the successful execution of the request, the moderator can perform operations on this channel
     * resource.
     *
     * @param accessToken The access token of the requestor.
     * @param channelId The id of the channel.
     * @param moderatorName The name of the moderator who should be added as a responsible moderator.
     * @throws APIException Throws an APIException when the request fails or the server rejects the request.
     */
    public void addModeratorResponsibility(String accessToken, int channelId, String moderatorName)
            throws APIException{
        String url = channelUrl + "/" + channelId + "/moderator";


        // Create content for the password reset.
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", moderatorName);
        String jsonContent = jsonObject.toString();

        try{
            URL obj = new URL(url);
            // Set http method.
            connection = (HttpURLConnection) obj.openConnection();
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");
            setAuthorization(accessToken);
            connection.setDoOutput(true);

            logger.info("Sending POST request to URL: {}", url);

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            out.write(jsonContent);
            out.flush();
            out.close();

            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_CREATED) {
                // Do nothing.
                logger.info("Added responsibility of moderator with name {} to channel with id {}.", moderatorName,
                        channelId);
            } else {
                String serverResponse = getErrorResponse(connection);
                ServerError se = gson.fromJson(serverResponse, ServerError.class);
                // Map to API exception.
                throw new APIException(se.getErrorCode(), connection.getResponseCode(),
                        "Adding responsibility of moderator with name " + moderatorName + " to channel with id " +
                                channelId +" failed.");
            }
        } catch (MalformedURLException malEx) {
            malEx.printStackTrace();
            logger.error("Malformed URL discovered.");
            throw new APIException(Constants.FATAL_ERROR, "URL malformed.");
        } catch (ProtocolException pe) {
            pe.printStackTrace();
            throw new APIException(Constants.FATAL_ERROR, "Protocol exception.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            logger.error("IO exception occurred. Probably due to a failed connection to the server.");
            throw new APIException(Constants.CONNECTION_FAILURE, "Connection failure. Failed to connect to server.");
        }
    }

    /**
     * Deletes the channel with the specified id.
     *
     * @param accessToken The access token of the requestor.
     * @param channelId The id of the channel.
     * @throws APIException Throws an APIException when the request fails or the server rejects the request.
     */
    public void deleteChannel(String accessToken, int channelId) throws APIException {
        String url = channelUrl + "/" + channelId;

        try {
            URL obj = new URL(url);

            // Set http method.
            connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("DELETE");
            setAuthorization(accessToken);

            logger.info("Sending DELETE request to URL: {}", url);

            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_NO_CONTENT) {
                // Do nothing.
                logger.info("Deleted channel with id {}.", channelId);
            } else {
                String serverResponse = getErrorResponse(connection);
                ServerError se = gson.fromJson(serverResponse, ServerError.class);
                // Map to API exception.
                throw new APIException(se.getErrorCode(), connection.getResponseCode(),
                        "Deleting channel with id " + channelId + " failed.");
            }
        } catch (MalformedURLException malEx) {
            malEx.printStackTrace();
            logger.error("Malformed URL discovered.");
            throw new APIException(Constants.FATAL_ERROR, "URL malformed.");
        } catch (ProtocolException pe) {
            pe.printStackTrace();
            throw new APIException(Constants.FATAL_ERROR, "Protocol exception.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            logger.error("IO exception occurred. Probably due to a failed connection to the server.");
            throw new APIException(Constants.CONNECTION_FAILURE, "Connection failure. Failed to connect to server.");
        }
    }

    /**
     * Retrieves the announcements of the channel with the specified id. Requests all announcements
     * with a higher message number than the specified one from the server. The announcements are returned
     * in form of a list.
     *
     * @param accessToken The access token of the requestor.
     * @param channelId The id of the channel.
     * @param messageNr The message number that specifies a lower bound for the request.
     * @return A list of objects of type Announcement.
     * @throws APIException Throws an APIException when the request fails or the server rejects the request.
     */
    public List<Announcement> getAnnouncementsOfChannel(String accessToken, int channelId, int messageNr)
            throws APIException {
        String url = channelUrl + "/" + channelId + "/announcement";
        List<Announcement> announcements = null;

        // Add parameters to url.
        HashMap<String, String> params = new HashMap<String, String>();
        if (messageNr >= 0) {
            params.put("messageNr", Integer.toString(messageNr));
        }
        url += getUrlParams(params);

        try {
            URL obj = new URL(url);
            // Set http method.
            connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            setAuthorization(accessToken);

            logger.info("Sending GET request to URL: {}", url);

            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                // Get request response as String.
                String json = getResponse(connection);

                // Use a list of announcements as deserialization type.
                Type listType = new TypeToken<List<Announcement>>() {
                }.getType();

                announcements = gson.fromJson(json, listType);
            } else {
                String serverResponse = getErrorResponse(connection);
                ServerError se = gson.fromJson(serverResponse, ServerError.class);
                // Map to API exception.
                throw new APIException(se.getErrorCode(), connection.getResponseCode(),
                        "Get responsible moderators failed.");
            }
        } catch (MalformedURLException malEx) {
            malEx.printStackTrace();
            logger.error("Malformed URL discovered.");
            throw new APIException(Constants.FATAL_ERROR, "URL malformed.");
        } catch (ProtocolException pe) {
            pe.printStackTrace();
            throw new APIException(Constants.FATAL_ERROR, "Protocol exception.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            logger.error("IO exception occurred. Probably due to a failed connection to the server.");
            throw new APIException(Constants.CONNECTION_FAILURE, "Connection failure. Failed to connect to server.");
        }

        return announcements;
    }

    /**
     * Sends a request to create a new announcement. The announcement belongs to the specified channel and
     * all subscribers will be notified about the new announcement.
     *
     * @param accessToken The access token of the requestor.
     * @param channelId The id of the channel.
     * @param announcement The data of the new announcements in form of an announcement object.
     * @return An object of type Announcement. It contains the data provided by the server in the response.
     * @throws APIException Throws an APIException if the request fails or the server rejects the request.
     */
    public Announcement sendAnnouncement(String accessToken, int channelId, Announcement announcement)
            throws APIException{
        String url = channelUrl + "/" + channelId + "/announcement";
        String jsonContent = gson.toJson(announcement, Announcement.class);
        Announcement announcementResponse;

        try {
            URL obj = new URL(url);
            // Set http method.
            connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            setAuthorization(accessToken);

            logger.info("Sending POST request to URL: {}", url);

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            out.write(jsonContent);
            out.flush();
            out.close();

            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_CREATED) {
                // Get request response as String.
                String json = getResponse(connection);

                announcementResponse = gson.fromJson(json, Announcement.class);
            } else {
                String serverResponse = getErrorResponse(connection);
                ServerError se = gson.fromJson(serverResponse, ServerError.class);
                // Map to API exception.
                throw new APIException(se.getErrorCode(), connection.getResponseCode(),
                        "Create announcement failed.");
            }
        } catch (MalformedURLException malEx) {
            malEx.printStackTrace();
            logger.error("Malformed URL discovered.");
            throw new APIException(Constants.FATAL_ERROR, "URL malformed.");
        } catch (ProtocolException pe) {
            pe.printStackTrace();
            throw new APIException(Constants.FATAL_ERROR, "Protocol exception.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            logger.error("IO exception occurred. Probably due to a failed connection to the server.");
            throw new APIException(Constants.CONNECTION_FAILURE, "Connection failure. Failed to connect to server.");
        }

        return announcementResponse;
    }

    /**
     * Sends a request to create a new reminder. The reminder belongs to the specified channel.
     * If the server accepts the request, it will fire the announcements as specified in the reminder.
     *
     * @param accessToken The access token of the requestor.
     * @param channelId The if of the channel to which the reminder belongs.
     * @param newReminder The data of the new reminder in form of a Reminder object.
     * @return The reminder object returned by the server in case of a successful request.
     * @throws APIException Throws an APIException if the request fails or the server rejects the request.
     */
    public Reminder createReminder(String accessToken, int channelId, Reminder newReminder) throws APIException {
        String url = channelUrl + "/" + channelId + "/reminder";
        String jsonContent = gson.toJson(newReminder, Reminder.class);
        Reminder reminderResponse;

        logger.debug("Reminder json is: {}", jsonContent);
        // Adjust json.
        jsonContent = adjustDateTimeFormat(jsonContent);

        try{
            URL obj = new URL(url);
            // Set http method.
            connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            setAuthorization(accessToken);

            logger.info("Sending POST request to URL: {}", url);

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            out.write(jsonContent);
            out.flush();
            out.close();

            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_CREATED) {
                // Get request response as String.
                String json = getResponse(connection);

                reminderResponse = gson.fromJson(json, Reminder.class);
            } else {
                String serverResponse = getErrorResponse(connection);
                ServerError se = gson.fromJson(serverResponse, ServerError.class);
                // Map to API exception.
                throw new APIException(se.getErrorCode(), connection.getResponseCode(),
                        "Create reminder failed.");
            }
        } catch (MalformedURLException malEx) {
            malEx.printStackTrace();
            logger.error("Malformed URL discovered.");
            throw new APIException(Constants.FATAL_ERROR, "URL malformed.");
        } catch (ProtocolException pe) {
            pe.printStackTrace();
            throw new APIException(Constants.FATAL_ERROR, "Protocol exception.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            logger.error("IO exception occurred. Probably due to a failed connection to the server.");
            throw new APIException(Constants.CONNECTION_FAILURE, "Connection failure. Failed to connect to server.");
        }

        return reminderResponse;
    }

    /**
     * Requests all reminders of a specific channel from the server.
     *
     * @param accessToken The access token of the requestor.
     * @param channelId The id of the channel whose reminders should be requested.
     * @return A list Reminder instances.
     * @throws APIException Throws an API exception, if the request fails or is rejected from the server.
     */
    public List<Reminder> getReminders(String accessToken, int channelId)throws APIException {
        List<Reminder> reminders;
        String url = channelUrl + "/" + channelId + "/reminder";

        try{
            URL obj = new URL(url);
            // Set http method.
            connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            setAuthorization(accessToken);

            logger.info("Sending GET request to URL: {}", url);

            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                // Get request response as String.
                String json = getResponse(connection);

                // Use a list of reminders as deserialization type.
                Type listType = new TypeToken<List<Reminder>>() {
                }.getType();

                reminders = gson.fromJson(json, listType);
            } else {
                String serverResponse = getErrorResponse(connection);
                ServerError se = gson.fromJson(serverResponse, ServerError.class);
                // Map to API exception.
                throw new APIException(se.getErrorCode(), connection.getResponseCode(),
                        "Get reminders failed.");
            }
        } catch (MalformedURLException malEx) {
            malEx.printStackTrace();
            logger.error("Malformed URL discovered.");
            throw new APIException(Constants.FATAL_ERROR, "URL malformed.");
        } catch (ProtocolException pe) {
            pe.printStackTrace();
            throw new APIException(Constants.FATAL_ERROR, "Protocol exception.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            logger.error("IO exception occurred. Probably due to a failed connection to the server.");
            throw new APIException(Constants.CONNECTION_FAILURE, "Connection failure. Failed to connect to server.");
        }

        return reminders;
    }

    /**
     * Requests the specific reminder which is identified by the defined id. The reminder belongs
     * to the channel which is identified by the defined channel-id.
     *
     * @param accessToken The access token of the requestor.
     * @param channelId The id of the channel to which the reminder belongs.
     * @param reminderId The id of the reminder that should be requested.
     * @return The requested reminder resource as a Reminder object.
     * @throws APIException Throws an API exception, if the request fails or is rejected from the server.
     */
    public Reminder getReminder(String accessToken, int channelId, int reminderId)throws APIException {
        Reminder reminder;
        String url = channelUrl + "/" + channelId + "/reminder/" + reminderId;

        try{
            URL obj = new URL(url);
            // Set http method.
            connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            setAuthorization(accessToken);

            logger.info("Sending GET request to URL: {}", url);

            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                // Get request response as String.
                String json = getResponse(connection);

                reminder = gson.fromJson(json, Reminder.class);
            } else {
                String serverResponse = getErrorResponse(connection);
                ServerError se = gson.fromJson(serverResponse, ServerError.class);
                // Map to API exception.
                throw new APIException(se.getErrorCode(), connection.getResponseCode(),
                        "Get reminder with id " + reminderId + " failed.");
            }
        } catch (MalformedURLException malEx) {
            malEx.printStackTrace();
            logger.error("Malformed URL discovered.");
            throw new APIException(Constants.FATAL_ERROR, "URL malformed.");
        } catch (ProtocolException pe) {
            pe.printStackTrace();
            throw new APIException(Constants.FATAL_ERROR, "Protocol exception.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            logger.error("IO exception occurred. Probably due to a failed connection to the server.");
            throw new APIException(Constants.CONNECTION_FAILURE, "Connection failure. Failed to connect to server.");
        }

        return reminder;
    }

    /**
     * Sends a request to update the reminder with the specified id. The updated data is sent to the
     * server using a JSON Merge Patch format. It contains only the values that need to be updated.
     *
     * @param accessToken The access token of the requestor.
     * @param channelId The id of the channel to which the reminder belongs.
     * @param reminderId The id of the reminder that needs to be updated.
     * @param updatableReminder The object with the updated data.
     * @return The reminder object with the updated data after the request has been successfully updated.
     * @throws APIException Throws an API exception, if the request fails or is rejected from the server.
     */
    public Reminder updateReminder(String accessToken, int channelId, int reminderId, Reminder updatableReminder)
            throws APIException {
        Reminder updatedReminder = null;
        String url = channelUrl + "/" + channelId + "/reminder/" + reminderId;

        String jsonContent = gson.toJson(updatableReminder, Reminder.class);
        jsonContent = adjustDateTimeFormat(jsonContent);

        try{
            URL obj = new URL(url);
            // Set http method.
            connection = (HttpURLConnection) obj.openConnection();
            // PATCH method is not allowed in HttpUrlConnection.
            connection.setRequestMethod("POST");
            // Use X-HTTP-Method-Override header to replace POST with PATCH on the REST server.
            connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            setAuthorization(accessToken);

            logger.info("Sending PATCH request to URL: {} with content: {}", url, jsonContent);

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            out.write(jsonContent);
            out.flush();
            out.close();

            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                // Get request response as String.
                String json = getResponse(connection);

                updatedReminder = gson.fromJson(json, Reminder.class);
            } else {
                String serverResponse = getErrorResponse(connection);
                ServerError se = gson.fromJson(serverResponse, ServerError.class);
                // Map to API exception.
                throw new APIException(se.getErrorCode(), connection.getResponseCode(),
                        "Update reminder with id " + reminderId + " failed.");
            }
        } catch (MalformedURLException malEx) {
            malEx.printStackTrace();
            logger.error("Malformed URL discovered.");
            throw new APIException(Constants.FATAL_ERROR, "URL malformed.");
        } catch (ProtocolException pe) {
            pe.printStackTrace();
            throw new APIException(Constants.FATAL_ERROR, "Protocol exception.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            logger.error("IO exception occurred. Probably due to a failed connection to the server.");
            throw new APIException(Constants.CONNECTION_FAILURE, "Connection failure. Failed to connect to server.");
        }

        return updatedReminder;
    }

    /**
     * Sends a request to change the active status of a specific reminder.
     *
     * @param accessToken The access token of the requestor.
     * @param channelId The if of the channel to which the reminder belongs.
     * @param reminderId The id of the reminder.
     * @param active The new active status of the reminder.
     * @throws APIException Throws an API exception, if the request fails or is rejected from the server.
     */
    public void changeActiveStatusOfReminder(String accessToken, int channelId, int reminderId, boolean active)
            throws APIException {
        String url = channelUrl + "/" + channelId + "/reminder/" + reminderId;

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("active", active);
        String jsonContent = jsonObject.toString();

        try{
            URL obj = new URL(url);
            // Set http method.
            connection = (HttpURLConnection) obj.openConnection();
            // PATCH method is not allowed in HttpUrlConnection.
            connection.setRequestMethod("POST");
            // Use X-HTTP-Method-Override header to replace POST with PATCH on the REST server.
            connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            setAuthorization(accessToken);

            logger.info("Sending PATCH request to URL: {} with content: {}", url, jsonContent);

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            out.write(jsonContent);
            out.flush();
            out.close();

            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                // Get request response as String.
                String json = getResponse(connection);

                Reminder updatedReminder = gson.fromJson(json, Reminder.class);

                logger.info("Successfully changed active status of reminder with id {}, the new value is {}.",
                        updatedReminder.getId(), updatedReminder.isActive());
            } else {
                String serverResponse = getErrorResponse(connection);
                ServerError se = gson.fromJson(serverResponse, ServerError.class);
                // Map to API exception.
                throw new APIException(se.getErrorCode(), connection.getResponseCode(),
                        "Change active status of reminder with id " + reminderId + " failed.");
            }
        } catch (MalformedURLException malEx) {
            malEx.printStackTrace();
            logger.error("Malformed URL discovered.");
            throw new APIException(Constants.FATAL_ERROR, "URL malformed.");
        } catch (ProtocolException pe) {
            pe.printStackTrace();
            throw new APIException(Constants.FATAL_ERROR, "Protocol exception.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            logger.error("IO exception occurred. Probably due to a failed connection to the server.");
            throw new APIException(Constants.CONNECTION_FAILURE, "Connection failure. Failed to connect to server.");
        }
    }

    /**
     * Sends a request to change the ignore flag value of a specific reminder.
     *
     * @param accessToken The access token of the requestor.
     * @param channelId The if of the channel to which the reminder belongs.
     * @param reminderId The id of the reminder.
     * @param ignore The new value of the ignore flag of the reminder.
     * @throws APIException Throws an API exception, if the request fails or is rejected from the server.
     */
    public void changeIgnoreFlagOfReminder(String accessToken, int channelId, int reminderId, boolean ignore)
            throws APIException {
        String url = channelUrl + "/" + channelId + "/reminder/" + reminderId;

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("ignore", ignore);
        String jsonContent = jsonObject.toString();

        try{
            URL obj = new URL(url);
            // Set http method.
            connection = (HttpURLConnection) obj.openConnection();
            // PATCH method is not allowed in HttpUrlConnection.
            connection.setRequestMethod("POST");
            // Use X-HTTP-Method-Override header to replace POST with PATCH on the REST server.
            connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            setAuthorization(accessToken);

            logger.info("Sending PATCH request to URL: {} with content: {}", url, jsonContent);

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            out.write(jsonContent);
            out.flush();
            out.close();

            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                // Get request response as String.
                String json = getResponse(connection);

                Reminder updatedReminder = gson.fromJson(json, Reminder.class);

                logger.info("Successfully changed ignore flag of reminder with id {}, the new value is {}.",
                        updatedReminder.getId(), updatedReminder.getIgnore());
            } else {
                String serverResponse = getErrorResponse(connection);
                ServerError se = gson.fromJson(serverResponse, ServerError.class);
                // Map to API exception.
                throw new APIException(se.getErrorCode(), connection.getResponseCode(),
                        "Change ignore flag value of reminder with id " + reminderId + " failed.");
            }
        } catch (MalformedURLException malEx) {
            malEx.printStackTrace();
            logger.error("Malformed URL discovered.");
            throw new APIException(Constants.FATAL_ERROR, "URL malformed.");
        } catch (ProtocolException pe) {
            pe.printStackTrace();
            throw new APIException(Constants.FATAL_ERROR, "Protocol exception.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            logger.error("IO exception occurred. Probably due to a failed connection to the server.");
            throw new APIException(Constants.CONNECTION_FAILURE, "Connection failure. Failed to connect to server.");
        }
    }

    /**
     * Deletes the reminder with the specified id from the given channel.
     *
     * @param accessToken The access token of the requestor.
     * @param channelId The id of the channel.
     * @param reminderId The id of the reminder.
     * @throws APIException Throws an APIException when the request fails or the server rejects the request.
     */
    public void deleteReminder(String accessToken, int channelId, int reminderId) throws APIException {
        String url = channelUrl + "/" + channelId + "/reminder/" + reminderId;

        try {
            URL obj = new URL(url);

            // Set http method.
            connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("DELETE");
            setAuthorization(accessToken);

            logger.info("Sending DELETE request to URL: {}", url);

            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_NO_CONTENT) {
                // Do nothing.
                logger.info("Deleted reminder with id {}.", channelId);
            } else {
                String serverResponse = getErrorResponse(connection);
                ServerError se = gson.fromJson(serverResponse, ServerError.class);
                // Map to API exception.
                throw new APIException(se.getErrorCode(), connection.getResponseCode(),
                        "Deleting reminder with id " + reminderId + " failed.");
            }
        } catch (MalformedURLException malEx) {
            malEx.printStackTrace();
            logger.error("Malformed URL discovered.");
            throw new APIException(Constants.FATAL_ERROR, "URL malformed.");
        } catch (ProtocolException pe) {
            pe.printStackTrace();
            throw new APIException(Constants.FATAL_ERROR, "Protocol exception.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            logger.error("IO exception occurred. Probably due to a failed connection to the server.");
            throw new APIException(Constants.CONNECTION_FAILURE, "Connection failure. Failed to connect to server.");
        }
    }

    /**
     * Serializes a channel object to a json string. The channel object
     * is serialized depending on the channel type.
     *
     * @param channel The channel object to parse.
     * @return The json string.
     */
    private String serializeChannelToJson(Channel channel){
        String jsonContent = "";
        if (channel.getType() == ChannelType.LECTURE) {
            Lecture updatableLecture = (Lecture) channel;
            jsonContent = gson.toJson(updatableLecture, Lecture.class);
        } else if (channel.getType() == ChannelType.EVENT) {
            Event updatableEvent = (Event) channel;
            jsonContent = gson.toJson(updatableEvent, Event.class);
        } else if (channel.getType() == ChannelType.SPORTS) {
            Sports updatableSports = (Sports) channel;
            jsonContent = gson.toJson(updatableSports, Sports.class);
        } else {
            jsonContent = gson.toJson(channel, Channel.class);
        }

        return jsonContent;
    }

    /**
     * Fixes the problem of the ':' character in the datetime zone information. The ':' character
     * needs to be removed since the server rejects the request otherwise, although the time format
     * is a valid ISO 8601 format.
     *
     * @param reminderJson The json document including start and end date information.
     * @return The modified json string.
     */
    private String adjustDateTimeFormat(String reminderJson) {
        // Adjust date time format to match the servers format: e.g. 2016-11-19T00:00:00.000+0100
        String adjusted = "";
        String[] parts = reminderJson.split(",");
        for (String part : parts) {
            if (part.contains("startDate") || part.contains("endDate")) {
                int remove = part.lastIndexOf(':');
                adjusted += part.substring(0, remove) + part.substring(remove + 1);
            } else {
                adjusted += part;
            }
            adjusted += ",";
        }
        adjusted = adjusted.substring(0, adjusted.length() - 1);
        logger.debug("Adjusted reminderJson: " + adjusted);
        return adjusted;
    }
}
