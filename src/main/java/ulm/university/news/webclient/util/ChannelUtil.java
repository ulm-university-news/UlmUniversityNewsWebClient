package ulm.university.news.webclient.util;

import ulm.university.news.webclient.data.Channel;
import ulm.university.news.webclient.data.Moderator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *  This class provides useful functionality to process channel data.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class ChannelUtil {

    /**
     * Sorts the given channels alphabetically according to the channel name.
     *
     * @param channels The channel list.
     */
    public static void sortChannelsByName(List<Channel> channels) {
        Collections.sort(channels, new Comparator<Channel>() {
            public int compare(Channel c1, Channel c2) {
                return c1.getName().compareToIgnoreCase(c2.getName());
            }
        });
    }

}
