package ulm.university.news.webclient.api;

import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * The moderator API is responsible for setting up and executing
 * requests to the REST server which cover moderator resources.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class ModeratorAPI extends MainAPI {
    /** The logger instance for Moderator. */
    private static final Logger logger = LoggerFactory.getLogger(ModeratorAPI.class);

    /** The url pointing to the moderator resource. */
    private String moderatorUrl;

    public ModeratorAPI() {
        moderatorUrl = serverUrl + "moderator";
    }

    /**
     * Requests all moderator accounts from the RESt server. Returns the list of moderator
     * resources.
     *
     * @param accessToken The access token of the requestor.
     * @param isLocked Only request moderators who are unlocked? Passing null will ignore the parameter.
     * @param isAdmin Only request moderators who are administrators? Passing null will ignore the parameter.
     * @return A list of Moderator objects.
     * @throws APIException Throws API exception if the request fails or is rejected from the server.
     */
    public List<Moderator> getModerators(String accessToken, Boolean isLocked, Boolean isAdmin) throws APIException {
        List<Moderator> moderators;
        String url = moderatorUrl;
        // Add parameters to url.
        HashMap<String, String> params = new HashMap<String, String>();
        if (isLocked != null) {
            params.put("isLocked", isLocked.toString());
        }
        if (isAdmin != null) {
            params.put("isAdmin", isAdmin.toString());
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

                // Use a list of moderators as deserialization type.
                Type listType = new TypeToken<List<Moderator>>() {
                }.getType();

                moderators = gson.fromJson(json, listType);
            } else {
                String serverResponse = getErrorResponse(connection);
                ServerError se = gson.fromJson(serverResponse, ServerError.class);
                // Map to API exception.
                throw new APIException(se.getErrorCode(), "Get moderators failed.");
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
            logger.error("IO exception occurred.");
            throw new APIException(Constants.FATAL_ERROR, "IO Exception");
        }

        return moderators;
    }

    /**
     * Performs a request to the server in order to authenticate the
     * moderator at the server. If the authentication is successful, the
     * method will return a moderator object containing the data of the logged in
     * moderator.
     *
     * @param name The username used for the authentication.
     * @param password The password used for the authentication.
     * @return A moderator object.
     * @throws APIException If the request fails or the authentication is rejected by the server.
     */
    public Moderator login(String name, String password) throws APIException {
        // Create URL for login.
        String url = moderatorUrl + "/authentication";

        // Parse user credentials.
        Moderator m = new Moderator();
        m.setName(name);
        m.setPassword(password);
        String jsonContent = gson.toJson(m, Moderator.class);

        URL obj;
        try {
            obj = new URL(url);
            // Set http method.
            connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            logger.info("Sending POST request to URL: {}", url);

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(jsonContent);
            out.flush();
            out.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String serverResponse = getResponse(connection);
                // Load new moderator object.
                m = gson.fromJson(serverResponse, Moderator.class);
            } else {
                String serverResponse = getErrorResponse(connection);
                ServerError se = gson.fromJson(serverResponse, ServerError.class);
                // Map to API exception.
                throw new APIException(se.getErrorCode(), "Login request failed.");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            logger.error("Malformed URL discovered.");
            throw new APIException(Constants.FATAL_ERROR, "Url malformed.");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("IO exception occurred.");
            throw new APIException(Constants.FATAL_ERROR, "IO exception occurred.");
        }

        return m;
    }

    /**
     * Send an account request to the REST server in order to create a new moderator resource.
     *
     * @param moderator The data of the new moderator.
     * @throws APIException Throws an API exception if the request to the server fails or is rejected.
     */
    public void sendCreateAccountRequest(Moderator moderator) throws APIException {
        // Create URL for login.
        String url = moderatorUrl;
        try {
            URL obj = new URL(url);

            // Parse moderator to json.
            String jsonContent = gson.toJson(moderator, Moderator.class);
            logger.debug("Send request to {} with content {}.", url, jsonContent);

            // Set http method.
            connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            logger.info("Sending POST request to URL: {}", url);

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(jsonContent);
            out.flush();
            out.close();

            // Check response.
            if (connection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                // Request successful.
            } else {
                String serverResponse = getErrorResponse(connection);
                ServerError se = gson.fromJson(serverResponse, ServerError.class);
                // Map to API exception.
                throw new APIException(se.getErrorCode(), "Account request failed.");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            logger.error("Malformed URL discovered.");
            throw new APIException(Constants.FATAL_ERROR, "Url malformed.");
        } catch (ProtocolException e) {
            e.printStackTrace();
            throw new APIException(Constants.FATAL_ERROR, "Protocol exception.");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("IO exception occurred.");
            throw new APIException(Constants.FATAL_ERROR, "IO exception occurred.");
        }
    }

    /**
     * Updates the given moderator object on the REST server.
     *
     * @param accessToken The access token of the requestor.
     * @param moderator The moderator object containing the values which should be changed.
     * @return The updated moderator object.
     * @throws APIException If the request fails or the authentication is rejected by the server.
     */
    public Moderator changeModerator(String accessToken, Moderator moderator) throws APIException {
        // Create URL for login.
        String url = moderatorUrl + "/" + moderator.getId();
        String jsonContent = gson.toJson(moderator, Moderator.class);

        URL obj;
        try {
            obj = new URL(url);
            // Set http method.
            connection = (HttpURLConnection) obj.openConnection();
            // PATCH method is not allowed in HttpUrlConnection.
            connection.setRequestMethod("POST");
            // Use X-HTTP-Method-Override header to replace POST with PATCH on the REST server.
            connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            setAuthorization(accessToken);

            logger.info("Sending PATCH request to URL: {}", url);

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(jsonContent);
            out.flush();
            out.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String serverResponse = getResponse(connection);
                // Load updated moderator object.
                moderator = gson.fromJson(serverResponse, Moderator.class);
            } else {
                String serverResponse = getErrorResponse(connection);
                ServerError se = gson.fromJson(serverResponse, ServerError.class);
                // Map to API exception.
                throw new APIException(se.getErrorCode(), "Change moderator request failed.");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            logger.error("Malformed URL discovered.");
            throw new APIException(Constants.FATAL_ERROR, "Url malformed.");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("IO exception occurred.");
            throw new APIException(Constants.FATAL_ERROR, "IO exception occurred.");
        }

        return moderator;
    }

    /**
     * Deletes the moderator with given id on the REST server.
     *
     * @param accessToken The access token of the requestor.
     * @param moderatorId The id of the moderator who should be deleted.
     * @throws APIException If the request fails or the authentication is rejected by the server.
     */
    public void deleteModerator(String accessToken, int moderatorId) throws APIException {
        // Create URL for login.
        String url = moderatorUrl + "/" + moderatorId;

        URL obj;
        try {
            obj = new URL(url);
            // Set http method.
            connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("DELETE");
            connection.addRequestProperty("Content-Type", "application/json");
            setAuthorization(accessToken);

            logger.info("Sending DELETE request to URL: {}", url);
            logger.info("Response Code: {}", connection.getResponseCode());

            if (connection.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT) {
                // Do nothing.
            } else {
                String serverResponse = getErrorResponse(connection);
                ServerError se = gson.fromJson(serverResponse, ServerError.class);
                // Map to API exception.
                throw new APIException(se.getErrorCode(), "Delete moderator request failed.");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            logger.error("Malformed URL discovered.");
            throw new APIException(Constants.FATAL_ERROR, "Url malformed.");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("IO exception occurred.");
            throw new APIException(Constants.FATAL_ERROR, "IO exception occurred.");
        }
    }
}
