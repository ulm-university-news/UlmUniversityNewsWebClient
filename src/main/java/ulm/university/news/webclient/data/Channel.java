package ulm.university.news.webclient.data;

import org.joda.time.DateTime;
import ulm.university.news.webclient.data.enums.ChannelType;
import ulm.university.news.webclient.util.Translator;

import java.util.List;
import java.util.Locale;


/**
 * The Channel class is the superclass of Lecture, Event and Sports. It provides information which are used by all
 * kinds of channels. One or more moderators are responsible for a channel. They create new announcements and
 * reminders. Users can subscribe to a channel to receive the announcements.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class Channel {
    /** The unique id of the channel. */
    int id;
    /** The name of the channel. */
    String name;
    /** The description of the channel. */
    String description;
    /** The type of the channel. */
    ChannelType type;
    /** The type of the channel as a string. */
    String typeString;
    /** The date on which the channel was created. */
    DateTime creationDate;
    /** The date on which the channel was modified. */
    DateTime modificationDate;
    /** The term to which the channel corresponds. */
    String term;
    /** The locations which belong to the channel. */
    String locations;
    /** Dates which belong to the channel. */
    String dates;
    /** Contact persons who belong to the channel. */
    String contacts;
    /** The website of the channel. */
    String website;
    /** A list of all announcements of the channel. */
    List<Announcement> announcements;
    /** A list of all reminders of the channel. */
    List<Reminder> reminders;
    /** A list of all moderators of the channel. */
    List<Moderator> moderators;
    /** A list of all subscribers of the channel. */
    List<User> subscribers;
    /** A counter which determine how many unread announcements the channel contains. */
    Integer numberOfUnreadAnnouncements;
    /** Indicates weather the channel is delete on serverUrl or not. */
    Boolean deleted;
    /** Indicates weather the channel deletion dialog was read or not. */
    Boolean deletedRead;

    public Channel() {
    }

    public Channel(int id, String name, String description, ChannelType type, DateTime creationDate,
                   DateTime modificationDate, String term, String locations, String dates, String contacts,
                   String website) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.term = term;
        this.locations = locations;
        this.dates = dates;
        this.contacts = contacts;
        this.website = website;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                ", term='" + term + '\'' +
                ", locations='" + locations + '\'' +
                ", dates='" + dates + '\'' +
                ", contacts='" + contacts + '\'' +
                ", website='" + website + '\'' +
                ", announcements=" + announcements +
                ", reminders=" + reminders +
                ", moderators=" + moderators +
                ", subscribers=" + subscribers +
                ", numberOfUnreadAnnouncements=" + numberOfUnreadAnnouncements +
                ", deleted=" + deleted +
                ", deletedRead=" + deletedRead +
                '}';
    }

    /**
     * Sets the string that describes the channel type depending on the
     * language that is specified with the current locale.
     *
     * @param currentLocale The current locale.
     */
    public void setLocalizedTypeString(Locale currentLocale){
        Translator translator = Translator.getInstance();
        String channelTypeString = "";

        switch (getType()){
            case LECTURE:
                channelTypeString = translator.getText(currentLocale, "general.channelType.lecture");
                break;
            case EVENT:
                channelTypeString = translator.getText(currentLocale, "general.channelType.event");
                break;
            case OTHER:
                channelTypeString = translator.getText(currentLocale, "general.channelType.other");
                break;
            case SPORTS:
                channelTypeString = translator.getText(currentLocale, "general.channelType.sports");
                break;
            case STUDENT_GROUP:
                channelTypeString = translator.getText(currentLocale, "general.channelType.studentGroup");
                break;
        }

        setTypeString(channelTypeString);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ChannelType getType() {
        return type;
    }

    public void setType(ChannelType type) {
        this.type = type;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    public DateTime getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(DateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<Announcement> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(List<Announcement> announcements) {
        this.announcements = announcements;
    }

    public List<Reminder> getReminders() {
        return reminders;
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
    }

    public List<Moderator> getModerators() {
        return moderators;
    }

    public void setModerators(List<Moderator> moderators) {
        this.moderators = moderators;
    }

    public List<User> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<User> subscribers) {
        this.subscribers = subscribers;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean isDeletedRead() {
        return deletedRead;
    }

    public void setDeletedRead(Boolean deletedRead) {
        this.deletedRead = deletedRead;
    }

    public Integer getNumberOfUnreadAnnouncements() {
        return numberOfUnreadAnnouncements;
    }

    public void setNumberOfUnreadAnnouncements(Integer numberOfUnreadAnnouncements) {
        this.numberOfUnreadAnnouncements = numberOfUnreadAnnouncements;
    }

    public String getTypeString() {
        return typeString;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }
}
