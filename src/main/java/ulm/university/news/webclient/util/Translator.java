package ulm.university.news.webclient.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * The Translator class is a Singleton class which offers the possibility to get language-dependent texts from
 * different properties files.
 */
public class Translator {
    /** The logger instance for Translator. */
    private static final Logger logger = LoggerFactory.getLogger(Translator.class);

    /** Reference for the Translator Singleton class. */
    private static Translator _instance;

    /** The name of the resource bundle including the location directory. */
    private String bundleName = "text";

    /**
     * Create an instance of the Translator class and set default locale.
     *
     * @param defaultLocale The default locale which should be set to the the Java Virtual Machine.
     */
    public Translator(Locale defaultLocale) {
        Locale.setDefault(defaultLocale);
    }

    /**
     * Get an instance of the Translator class.
     *
     * @return Instance of Translator.
     */
    public static synchronized Translator getInstance() {
        if (_instance == null) {
            _instance = new Translator(Locale.ENGLISH);
        }
        return _instance;
    }

    /**
     * Searches and returns a specific text in a specific resource bundle in a specific language.
     *
     * @param locale The preferred language. If language isn't supported use English as default.
     * @param key The key of the resource text.
     * @return The resource text identified by key in the preferred language.
     */
    public String getText(Locale locale, String key) {
        try {
            if (locale == null) {
                locale = Locale.ENGLISH;
            }
            ResourceBundle rb = ResourceBundle.getBundle(bundleName, locale);
            return rb.getString(key);
        } catch (MissingResourceException e) {
            logger.error("Couldn't find resource bundle:{}", bundleName);
            return "";
        }
    }

    /**
     * Searches and returns a specific text in a specific resource bundle in a specific language.
     *
     * @param locale The preferred language. If language isn't supported use English as default.
     * @param key The key of the resource text.
     * @param params Additional parameters which fill placeholders in the resource text.
     * @return The resource text identified by key in the preferred language.
     */
    public String getText(Locale locale, String key, Object... params) {
        try {
            if (locale == null) {
                locale = Locale.ENGLISH;
            }
            ResourceBundle rb = ResourceBundle.getBundle(bundleName, locale);
            return MessageFormat.format(rb.getString(key), params);
        } catch (MissingResourceException e) {
            logger.error("Couldn't find resource bundle:{}", bundleName);
            return "";
        }
    }
}
