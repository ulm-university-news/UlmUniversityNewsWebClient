package ulm.university.news.webclient.data;

/**
 * This class represents an user of the app. The class contains information about the user which is relevant for
 * certain functionalities within the application.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class User {
    /** The id of the user. */
    private Integer id;
    /** The username of the user. The username is used to make users identifiable in a Group. */
    private String name;
    /** The old username of the user after name change. */
    private String oldName;
    /** Identifies weather the users name has changed or not. */
    private Boolean nameChanged;

    /**
     * Creates an instance of the LocalUser class.
     */
    public User() {
    }

    /**
     * Creates an instance of the User class.
     *
     * @param name The username of the user.
     */
    public User(String name) {
        this.name = name;
    }

    /**
     * Creates an instance of the User class.
     *
     * @param id The id of the user.
     * @param name The username of the user.
     */
    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.oldName = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", oldName='" + oldName + '\'' +
                ", nameChanged=" + nameChanged +
                '}';
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public Boolean isNameChanged() {
        return !name.equals(oldName);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
