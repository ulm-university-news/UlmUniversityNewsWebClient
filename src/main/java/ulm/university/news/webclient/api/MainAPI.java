package ulm.university.news.webclient.api;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.data.Channel;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
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


        logger.debug("Response: {}", response.toString());
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
        if (responseCode >= 400) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } else {
            logger.error("Cannot parse an non-error message via error stream.");
        }

        logger.debug("Response: {}", response.toString());
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

    /**
     * Adds the authorization header to the request that is sent via the connection
     * object. The access token is set as the content of the header.
     *
     * @param accessToken The access token.
     */
    protected void setAuthorization(String accessToken) {
        connection.setRequestProperty("Authorization", accessToken);
    }

    protected String getUrlParams(HashMap<String, String> params) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
                result.append("?");
            } else {
                result.append("&");
            }
            try {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                logger.error("getUrlParams: UnsupportedEncodingException");
                return null;
            }
        }
        return result.toString();
    }
}
