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

    protected String getResponse(HttpURLConnection connection) throws Exception {
        int responseCode = connection.getResponseCode();
        logger.info("Response Code: {}", responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

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
