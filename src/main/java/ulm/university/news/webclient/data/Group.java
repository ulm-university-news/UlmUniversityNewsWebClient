package ulm.university.news.webclient.data;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.data.enums.GroupType;

import static ulm.university.news.webclient.util.Constants.TIME_ZONE;

/**
 * The Group class represents a group. It contains the relevant data for the group and provides methods to access this
 * data. A group is an enclosed area protected by a password. Only the participants of the group have access to the
 * contents and resources within this enclosed area.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class Group {

    /** An instance of the Logger class which performs logging for the Group class. */
    private static final Logger logger = LoggerFactory.getLogger(Group.class);

    /** The unique id of the group. */
    private int id;
    /** The name of the group. */
    private String name;
    /** The description of the group. */
    private String description;
    /** The type of the group. Defines whether the group is a tutorial group or a working group. */
    private GroupType groupType;
    /** The date and time when the group was created. */
    private DateTime creationDate;
    /** The date and time when the group was updated the last time. */
    private DateTime modificationDate;
    /** The specified term. */
    private String term;
    /** The id of the user which assumes the role of the group administrator for the group. */
    private int groupAdmin;

    /**
     * Creates an instance of the Group class.
     */
    public Group(){
    }

    /**
     * Creates an instance of the Group class.
     *
     * @param name The name of the group.
     * @param description The description of the group.
     * @param groupType The type of the group.
     * @param creationDate The date and time when the group was created.
     * @param modificationDate The date and time when the group was modified the last time.
     * @param term The specified term.
     * @param groupAdmin The id of the user which assumes the role of the group administrator.
     */
    public Group(String name, String description, GroupType groupType, DateTime creationDate, DateTime
            modificationDate, String term, int groupAdmin){
        this.name = name;
        this.description = description;
        this.groupType = groupType;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.term = term;
        this.groupAdmin = groupAdmin;
    }

    /**
     * Creates an instance of the Group class.
     *
     * @param id The unique id of the group.
     * @param name The name of the group.
     * @param description The description of the group.
     * @param groupType The type of the group.
     * @param creationDate The date and time when the group was created.
     * @param modificationDate The date and time when the group was modified the last time.
     * @param term The specified term.
     * @param groupAdmin The id of the user which assumes the role of the group administrator.
     */
    public Group(int id, String name, String description, GroupType groupType, DateTime creationDate, DateTime
            modificationDate, String term, int groupAdmin){
        this.id = id;
        this.name = name;
        this.description = description;
        this.groupType = groupType;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.term = term;
        this.groupAdmin = groupAdmin;
    }

    /**
     * If the creation date of the group has not been set so far, this method computes and sets the current date as
     * the creation date of the group.
     */
    public void computeCreationDate(){
        if (creationDate == null) {
            creationDate = DateTime.now(TIME_ZONE);
        }
    }

    /**
     * Sets the current date and time as the new value for the modification date.
     */
    public void updateModificationDate(){
        modificationDate = DateTime.now(TIME_ZONE);
    }

    /**
     * Checks if an user with the specified id is the group administrator of this group.
     *
     * @param userId The id of the user.
     * @return Returns true if the user is the group administrator, false otherwise.
     */
    public boolean isGroupAdmin(int userId){
        return userId == groupAdmin;
    }

    public int getGroupAdmin() {
        return groupAdmin;
    }

    public void setGroupAdmin(int groupAdmin) {
        this.groupAdmin = groupAdmin;
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

    public GroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(GroupType groupType) {
        this.groupType = groupType;
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

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", groupType=" + groupType +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                ", term='" + term + '\'' +
                ", groupAdmin=" + groupAdmin +
                '}';
    }
}
