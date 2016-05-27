package ulm.university.news.webclient.util.exceptions;

/**
 * This exception is thrown when the requestor doesn't have a
 * valid session anymore. This can happen if the session was inactive
 * for a specified time span.
 *
 * Created by Philipp on 26.05.2016.
 */
public class SessionIsExpiredException extends Exception {

    /**
     * Creates a new instance of SessionIsExpiredException.
     *
     * @param message The error message.
     */
    public SessionIsExpiredException(String message)
    {
        super(message);
    }

    /**
     * Creates a new instance of SessionIsExpiredException.
     *
     * @param cause A throwable instance if this exception was caused by other exception.
     */
    public SessionIsExpiredException(Throwable cause)
    {
        super(cause);
    }

    /**
     * Creates a new instance of SessionIsExpiredException.
     *
     * @param message The error message
     * @param cause A throwable instance if this exception was caused by other exception.
     */
    public SessionIsExpiredException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
