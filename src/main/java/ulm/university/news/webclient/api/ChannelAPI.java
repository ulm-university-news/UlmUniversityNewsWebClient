package ulm.university.news.webclient.api;

import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.data.Channel;
import ulm.university.news.webclient.data.Moderator;
import ulm.university.news.webclient.data.ServerError;
import ulm.university.news.webclient.util.Constants;
import ulm.university.news.webclient.util.exceptions.APIException;

import java.io.IOException;
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
}
