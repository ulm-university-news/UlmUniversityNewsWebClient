package ulm.university.news.webclient.util;

import ulm.university.news.webclient.data.Group;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class provides useful functionality to process group data.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class GroupUtil {

    /**
     * Sorts the given groups alphabetically according to the group name.
     *
     * @param groups The group list.
     */
    public static void sortGroupsName(List<Group> groups) {
        Collections.sort(groups, new Comparator<Group>() {
            public int compare(Group g1, Group g2) {
                return g1.getName().compareToIgnoreCase(g2.getName());
            }
        });
    }
}
