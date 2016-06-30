package ulm.university.news.webclient.api;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.data.Channel;
import ulm.university.news.webclient.data.Moderator;
import ulm.university.news.webclient.data.ServerError;
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

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
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
}
