package ulm.university.news.webclient.api;

import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.data.Moderator;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
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
        connection = (HttpURLConnection) obj.openConnection();
        setAuthorization();

        // Set http method.
        connection.setRequestMethod("GET");

        logger.info("Sending GET request to URL: {}", moderatorUrl);

        // Get request response as String.
        String json = getResponse(connection);
        // Use a list of moderators as deserialization type.
        Type listType = new TypeToken<List<Moderator>>() {
        }.getType();
        return gson.fromJson(json, listType);
    }
}
