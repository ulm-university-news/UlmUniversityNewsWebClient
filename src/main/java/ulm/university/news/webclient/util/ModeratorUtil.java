package ulm.university.news.webclient.util;

import ulm.university.news.webclient.data.Moderator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class provides useful functionality to process moderator data.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class ModeratorUtil {

    /**
     * Sorts the given moderators alphabetically according to the last name.
     *
     * @param moderators The channel list.
     */
    public static void sortModeratorsName(List<Moderator> moderators) {
        Collections.sort(moderators, new Comparator<Moderator>() {
            public int compare(Moderator m1, Moderator m2) {
                return m1.getLastName().compareToIgnoreCase(m2.getLastName());
            }
        });
    }
}
