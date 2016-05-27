package ulm.university.news.webclient.api;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.data.Channel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Properties;

/**
 * TODO
 *
 * @author Matthias Mak
 */
public abstract class MainAPI {
    /** The logger instance for Moderator. */
    private static final Logger logger = LoggerFactory.getLogger(MainAPI.class);

    /** The url of the REST Server. */
    protected String serverUrl;

    /** The http connection to the REST Server. */
    protected HttpURLConnection connection;

    /** The Gson object used to parse from an to JSON. */
    protected Gson gson;

    public MainAPI() {
        // Make sure, channel class or appropriate channel subclass is (de)serialized properly.
        ChannelDeserializer cd = new ChannelDeserializer();
        // Make sure, dates are (de)serialized properly.
        gson = Converters.registerDateTime(new GsonBuilder()).registerTypeAdapter(Channel.class, cd)
                .create();
        // Load server url from properties file.
        initServerAddress();
    }

    /**
     * Used to extract a server's response in cases the request has been performed successfully.
     * This method works only with status codes indicating a successful request.
     *
     * @param connection The connection to the server.
     * @return The server's response as a string.
     * @throws IOException If IO operation fails.
     */
    protected String getResponse(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        logger.info("Response Code: {}", responseCode);

        StringBuffer response = new StringBuffer();
        if (responseCode < 300) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } else {
            logger.error("Cannot parse an error message via input stream.");
        }


        logger.info("Response: {}", response.toString());
        return response.toString();
    }

    /**
     * Used to extract a server's response in cases the request has been rejected by the server.
     * This method works only with status codes indicating a non-successful request.
     *
     * @param connection The connection to the server.
     * @return The server's response as a string.
     * @throws IOException If IO operation fails.
     */
    protected String getErrorResponse(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        logger.info("Response Code: {}", responseCode);

        StringBuffer response = new StringBuffer();
        if (responseCode > 400) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } else {
            logger.error("Cannot parse an non-error message via error stream.");
        }

        logger.info("Response: {}", response.toString());
        return response.toString();
    }

    /**
     * Reads the properties file which contains information about the REST serverUrl. Sets the REST servers internet
     * root address.
     */
    private void initServerAddress() {
        Properties pushCredentials = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream("Server.properties");
        if (input == null) {
            logger.error("PushManager could not localize the file PushManager.properties.");
            return;
        }
        try {
            pushCredentials.load(input);
        } catch (IOException e) {
            logger.error("Failed to load the properties of the PushManager credentials.");
            return;
        }
        serverUrl = pushCredentials.getProperty("restServerAddress");
        logger.info("REST server url loaded: {}", serverUrl);
    }

    protected void setAuthorization(){
        connection.setRequestProperty("Authorization", "510e4f3dafa2568c59d94787030292f81a37e5a4baf6a727cd5274db79d0b17d");
    }
}
