package ulm.university.news.webclient.api;

import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.data.Moderator;
import ulm.university.news.webclient.data.ServerError;
import ulm.university.news.webclient.util.exceptions.APIException;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * TODO
 *
 * @author Matthias Mak
 */
public class ModeratorAPI extends MainAPI {
    /** The logger instance for Moderator. */
    private static final Logger logger = LoggerFactory.getLogger(ModeratorAPI.class);

    /** The url pointing to the moderator resource. */
    private String moderatorUrl;

    public ModeratorAPI() {
        moderatorUrl = serverUrl + "moderator";
    }

    public List<Moderator> getModerators() throws Exception {
        URL obj = new URL(moderatorUrl);
        // Set http method.
        connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        setAuthorization();

        logger.info("Sending GET request to URL: {}", moderatorUrl);

        // Get request response as String.
        String json = getResponse(connection);
        // Use a list of moderators as deserialization type.
        Type listType = new TypeToken<List<Moderator>>() {
        }.getType();
        return gson.fromJson(json, listType);
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

        URL obj = null;
        try {
            obj = new URL(url);
            // Set http method.
            connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(jsonContent);
            out.flush();
            out.close();


            if (connection.getResponseCode() == 200)
            {
                String serverResponse = getResponse(connection);
                // Load new moderator object.
                m = gson.fromJson(serverResponse, Moderator.class);
            }
            else{
                String serverResponse = getErrorResponse(connection);
                ServerError se = gson.fromJson(serverResponse, ServerError.class);
                // Map to API exception.
                throw new APIException(se.getErrorCode(), "Login request failed.");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // TODO
        } catch (IOException e) {
            e.printStackTrace();
            // TODO
        }

        return m;
    }
}
