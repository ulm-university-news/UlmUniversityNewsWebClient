package ulm.university.news.webclient.data;

import org.joda.time.DateTime;
import ulm.university.news.webclient.data.enums.Priority;

/**
 * The Announcement class represents a message which is sent within a channel. The message belongs to a certain
 * channel and is sent by a moderator or a reminder icon_channel_event. The message contains the id of the author, the id of the
 * channel and the title of the announcement.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class Announcement extends Message {
    /** The id of the channel to which the announcement belongs. */
    int channelId;
    /** The moderator who created the announcement. */
    int authorModerator;
    /** The title of the announcement. */
    String title;

    /**
     * Creates an instance of the Announcement class.
     */
    public Announcement() {
    }

    /**
     * Creates an instance of the Announcement class.
     *
     * @param text The text of the announcement.
     * @param messageNumber The number of the announcement regarding the given conversation.
     * @param creationDate The date and time when the announcement was created.
     * @param priority The priority of the announcement.
     * @param channelId The id of the channel to which the announcement belongs.
     * @param authorModerator The id of the moderator who is the author of the announcement.
     * @param title The title of the announcement.
     */
    public Announcement(String text, int messageNumber, DateTime creationDate, Priority priority, int channelId,
                        int authorModerator, String title) {
        super(text, messageNumber, creationDate, priority);
        this.channelId = channelId;
        this.authorModerator = authorModerator;
        this.title = title;
    }

    /**
     * Creates an instance of the Announcement class.
     *
     * @param id The id of the announcement.
     * @param text The text of the announcement.
     * @param messageNumber The number of the announcement regarding the given conversation.
     * @param creationDate The date and time when the announcement was created.
     * @param priority The priority of the announcement.
     * @param channelId The id of the channel to which the announcement belongs.
     * @param authorModerator The id of the moderator who is the author of the announcement.
     * @param title The title of the announcement.
     * @param read Weather the message was already read or not.
     */
    public Announcement(int id, String text, int messageNumber, DateTime creationDate, Priority priority, int
            channelId, int authorModerator, String title, boolean read) {
        super(id, text, messageNumber, creationDate, priority, read);
        this.channelId = channelId;
        this.authorModerator = authorModerator;
        this.title = title;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getAuthorModerator() {
        return authorModerator;
    }

    public void setAuthorModerator(int authorModerator) {
        this.authorModerator = authorModerator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Announcement{" +
                "channelId=" + channelId +
                ", authorModerator=" + authorModerator +
                ", title='" + title + '\'' +
                "} " + super.toString();
    }
}
