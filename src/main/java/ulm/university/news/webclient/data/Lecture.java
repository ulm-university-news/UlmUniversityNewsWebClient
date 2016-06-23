package ulm.university.news.webclient.data;

import org.joda.time.DateTime;
import ulm.university.news.webclient.data.enums.ChannelType;
import ulm.university.news.webclient.data.enums.Faculty;
import ulm.university.news.webclient.util.Translator;

import java.util.Locale;

/**
 * The Lecture class is a sub class of Channel. This class adds fields to describe a Lecture.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class Lecture extends Channel {
    /** The faculty to which the Lecture belongs. */
    Faculty faculty;
    /** The faculty in form of a string object. */
    String facultyString;
    /** The date on which the Lecture starts represented as text. */
    String startDate;
    /** The date on which the Lecture ends represented as text. */
    String endDate;
    /** The lecturer who gives the lecture. */
    String lecturer;
    /** The person who assists the lecture. */
    String assistant;

    public Lecture() {
    }

    public Lecture(int id, String name, String description, ChannelType type, DateTime creationDate, DateTime
            modificationDate, String term, String locations, String dates, String contacts, String website, Faculty
                           faculty, String startDate, String endDate, String lecturer, String assistant) {
        super(id, name, description, type, creationDate, modificationDate, term, locations, dates, contacts, website);
        this.faculty = faculty;
        this.startDate = startDate;
        this.endDate = endDate;
        this.lecturer = lecturer;
        this.assistant = assistant;
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "faculty=" + faculty +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", lecturer='" + lecturer + '\'' +
                ", assistant='" + assistant + '\'' +
                "} " + super.toString();
    }

    /**
     * Parses the faculty value into a localized string depending on the
     * specified locale and sets the faculty string field.
     *
     * @param currentLocale The current locale.
     */
    public void setFacultyAsLocalizedString(Locale currentLocale){
        Translator translator = Translator.getInstance();
        String facultyString = "";

        System.out.println("getFacultyAsLocalizedString: Method is called.");

        switch (getFaculty()){
            case ENGINEERING_COMPUTER_SCIENCE_PSYCHOLOGY:
                facultyString =  translator.getText(currentLocale,
                    "general.faculty.computerScienceAndEngineering");
                break;
            case MATHEMATICS_ECONOMICS:
                facultyString =  translator.getText(currentLocale,
                        "general.faculty.mathematicsAndEconomics");
                break;
            case MEDICINES:
                facultyString =  translator.getText(currentLocale,
                        "general.faculty.medicines");
                break;
            case NATURAL_SCIENCES:
                facultyString =  translator.getText(currentLocale,
                        "general.faculty.naturalSciences");
                break;
        }
        System.out.println("Result is: " + facultyString);
       this.setFacultyString(facultyString);
    }


    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getAssistant() {
        return assistant;
    }

    public void setAssistant(String assistant) {
        this.assistant = assistant;
    }

    public String getFacultyString() {
        return facultyString;
    }

    public void setFacultyString(String facultyString) {
        this.facultyString = facultyString;
    }
}
