package ulm.university.news.webclient.util.exceptions;

/**
 * Created by Philipp on 26.05.2016.
 */
public class APIException extends Exception{

    /** The error code associated with the API failure. */
    private int errorCode;

    /**
     * Creates an instance of APIException.
     *
     * @param errorCode The error code associated with the API failure.
     */
    public APIException(int errorCode)
    {
        super();
        this.errorCode = errorCode;
    }

    /**
     * Creates an instance of APIException.
     *
     * @param errorCode The error code associated with the API failure.
     * @param message The error message.
     */
    public APIException(int errorCode, String message)
    {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Creates an instance of APIException.
     *
     * @param errorCode The error code associated with the API failure.
     * @param cause A throwable instance if this exception was caused by other exception.
     */
    public APIException(int errorCode, Throwable cause)
    {
        super(cause);
        this.errorCode = errorCode;
    }

    /**
     * Creates an instance of APIException.
     *
     * @param errorCode The error code associated with the API failure.
     * @param message The error message.
     * @param cause A throwable instance if this exception was caused by other exception.
     */
    public APIException(int errorCode, String message, Throwable cause){
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
