package ulm.university.news.webclient.api;


import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.*;
import ulm.university.news.webclient.data.Channel;
import ulm.university.news.webclient.data.Event;
import ulm.university.news.webclient.data.Lecture;
import ulm.university.news.webclient.data.Sports;
import ulm.university.news.webclient.data.enums.ChannelType;

import java.lang.reflect.Type;


/**
 * The ChannelDeserializer is used to deserialize channel class or appropriate channel subclass. The correct
 * deserialization is based on the channel type included in the JSON String.
 *
 * @author Matthias Mak
 */
public class ChannelDeserializer implements JsonDeserializer<Channel> {
    /** The Gson object used to parse from JSON with default deserializer. */
    private Gson gson;

    /**
     * Creates an instance of ChannelDeserializer and initialises Gson.
     */
    public ChannelDeserializer() {
        // Make sure, dates are deserialized properly.
        gson = Converters.registerDateTime(new GsonBuilder()).create();
    }

    public Channel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws
            JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        // Deserialize as Channel class or appropriate Channel subclass.
        if (jsonObject.has("type")) {
            ChannelType type = ChannelType.valueOf(jsonObject.get("type").getAsString());
            switch (type) {
                case LECTURE:
                    return context.deserialize(jsonObject, Lecture.class);
                case EVENT:
                    return context.deserialize(jsonObject, Event.class);
                case SPORTS:
                    return context.deserialize(jsonObject, Sports.class);
                default:
                    // Deserialize OTHER and STUDENT_GROUP as normal Channel object.
                    return gson.fromJson(json, Channel.class);
            }
        }
        throw new RuntimeException("Deserialization failed. No channel type found.");
    }
}
