package ulm.university.news.webclient.data;

/**
 * Created by Philipp on 26.05.2016.
 */
public class ServerError {

    /** Determines the HTTP status code which should be returned if this error has occurred. */
    private int httpStatusCode;
    /** An application specific error code which identifies the error that has occurred. */
    private int errorCode;
    /** The message provides an description of the occurred error. */
    private String message;

    /**
     * Creates an instance of ServerError.
     */
    public ServerError(){

    }

    /**
     * Creates an instance of ServerError.
     *
     * @param httpStatusCode The HTTP status code which should be returned within the HTTP response;
     * @param errorCode The application specific error code.
     * @param message The error message.
     */
    public ServerError(int httpStatusCode, int errorCode, String message){
        this.httpStatusCode = httpStatusCode;
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
