package engine;

/**
 * Exception indicate problem with <code>HttpsConnection</code>.
 * Use <code>getCause</code>. to see more about cause.
 */
public class BooruEngineConnectionException extends BooruEngineException {

    public BooruEngineConnectionException(){
        super();
    }

    public BooruEngineConnectionException(String message){
        super(message);
    }

    public BooruEngineConnectionException(Throwable cause){
        super(cause);
    }

    public BooruEngineConnectionException(String message, Throwable cause){
        super(message, cause);
    }

}
