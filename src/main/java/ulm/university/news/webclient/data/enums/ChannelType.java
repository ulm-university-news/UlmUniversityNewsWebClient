package ulm.university.news.webclient.data.enums;

/**
 * The ChannelType defines the type of a Channel as a LECTURE, EVENT, SPORTS or STUDENT_GROUP.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public enum ChannelType {
    LECTURE, EVENT, SPORTS, STUDENT_GROUP, OTHER;

    public static final ChannelType values[] = values();
}
