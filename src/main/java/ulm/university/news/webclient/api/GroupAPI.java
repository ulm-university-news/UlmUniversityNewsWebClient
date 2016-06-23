package ulm.university.news.webclient.api;

import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.data.Group;
import ulm.university.news.webclient.data.ServerError;
import ulm.university.news.webclient.util.Constants;
import ulm.university.news.webclient.util.exceptions.APIException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

/**
 * The group API contains methods for sending group related requests to the REST server.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class GroupAPI extends MainAPI {
    /** The logger instance for the GroupAPI. */
    private static final Logger logger = LoggerFactory.getLogger(GroupAPI.class);

    /** The url pointing to the group resources. */
    private String groupUrl;

    /**
     * Creates an instance of the GroupAPI class.
     */
    public GroupAPI() {
        groupUrl = serverUrl + "group";
    }

    /**
     * Requests all group resources from the server.
     *
     * @param accessToken The access token of the requestor.
     * @return A list of group instances.
     * @throws APIException Throws an API exception, if the request fails or is rejected from the server.
     */
    public List<Group> getGroups(String accessToken) throws APIException {
        List<Group> groups;

        try {
            URL obj = new URL(groupUrl);
            // Set http method.
            connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            setAuthorization(accessToken);

            logger.info("Sending GET request to URL: {}", groupUrl);

            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                // Get request response as String.
                String json = getResponse(connection);

                // Use a list of channels as deserialization type.
                Type listType = new TypeToken<List<Group>>() {
                }.getType();

                groups = gson.fromJson(json, listType);
            } else {
                String serverResponse = getErrorResponse(connection);
                ServerError se = gson.fromJson(serverResponse, ServerError.class);
                // Map to API exception.
                throw new APIException(se.getErrorCode(), connection.getResponseCode(),
                        "Get groups failed.");
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

        return groups;
    }

    /**
     * Deletes the group with given id on the REST server.
     *
     * @param accessToken The access token of the requestor.
     * @param groupId The id of the group which should be deleted.
     * @throws APIException If the request fails or the authentication is rejected by the server.
     */
    public void deleteGroup(String accessToken, int groupId) throws APIException {
        String url = groupUrl + "/" + groupId;

        URL obj;
        try {
            obj = new URL(url);
            // Set http method.
            connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("DELETE");
            setAuthorization(accessToken);

            logger.info("Sending DELETE request to URL: {}", url);
            logger.info("Response Code: {}", connection.getResponseCode());

            if (connection.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT) {
                // Do nothing.
                logger.debug("Group {} has been deleted.", groupId);
            } else {
                String serverResponse = getErrorResponse(connection);
                ServerError se = gson.fromJson(serverResponse, ServerError.class);
                // Map to API exception.
                throw new APIException(se.getErrorCode(), "Delete group request failed.");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            logger.error("Malformed URL discovered.");
            throw new APIException(Constants.FATAL_ERROR, "Url malformed.");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("IO exception occurred. Probably due to a failed connection to the server.");
            throw new APIException(Constants.CONNECTION_FAILURE, "Connection failure.");
        }
    }
}
