package ulm.university.news.webclient.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.data.Moderator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    /** An instance of the Logger class which performs logging for the ModeratorUtil class. */
    private static final Logger logger = LoggerFactory.getLogger(ModeratorUtil.class);

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

    /**
     * Helper method to generate a hash for the given moderator password. The password is hashed using a SHA256
     * algorithm.
     *
     * @param password The password as a string.
     * @return The generated hash as a string.
     */
    public static String hashPassword(String password){
        String hash = "";
        try {
            byte[] passwordBytes = password.getBytes();

            // Calculate hash on the password's byte sequence.
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] token = sha256.digest(passwordBytes);

            // Transform the bytes (8 bit signed) into a hexadecimal format.
            StringBuilder tokenString = new StringBuilder();
            for (int i = 0; i < token.length; i++) {
                /*
                Format parameters: %[flags][width][conversion]
                Flag '0' - The result will be zero padded.
                Width '2' - The width is 2 as 1 byte is represented by two hex characters.
                Conversion 'x' - Result is formatted as hexadecimal integer, uppercase.
                 */
                tokenString.append(String.format("%02x", token[i]));
            }
            hash = tokenString.toString();
            logger.debug("The generated hash is {}", hash);
        }
        catch (NoSuchAlgorithmException ex)
        {
            logger.error("Couldn't perform hashing. No SHA256 algorithm.");
        }

        return hash;
    }
}
