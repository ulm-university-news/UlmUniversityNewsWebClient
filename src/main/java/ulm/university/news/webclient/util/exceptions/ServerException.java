package ulm.university.news.webclient.util.exceptions;

/**
 * The ServerException is thrown when internal errors make
 * it impossible to further process the request properly. The
 * exception is passed back to the FrontController where it should
 * be handled, e.g. by forwarding to an appropriate error page.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
public class ServerException extends Exception{

    /** The error code. */
    private int errorCode;

    /**
     * Creates a new instance of ServerException.
     *
     * @param errorCode The error code associated with the API failure.
     */
    public ServerException(int errorCode){
        super();
        this.errorCode = errorCode;
    }

    /**
     * Creates an instance of ServerException.
     *
     * @param errorCode The error code associated with the failure.
     * @param message The error message.
     */
    public ServerException(int errorCode, String message)
    {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Creates an instance of ServerException.
     *
     * @param errorCode The error code associated with the failure.
     * @param cause A throwable instance if this exception was caused by other exception.
     */
    public ServerException(int errorCode, Throwable cause)
    {
        super(cause);
        this.errorCode = errorCode;
    }

    /**
     * Creates an instance of ServerException.
    *
    * @param errorCode The error code associated with the failure.
    * @param message The error message.
    * @param cause A throwable instance if this exception was caused by other exception.
    */
    public ServerException(int errorCode, String message, Throwable cause){
        super(message, cause);
        this.errorCode = errorCode;
    }


    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

}
