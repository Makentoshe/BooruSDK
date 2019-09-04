package engine;

/**
 * Default exception for BEL. It will be contain another exception or message, if error can't be classified.
 * Use <code>getCause</code>. to see more about main exception.
 */
public class BooruEngineException extends Exception {

    public BooruEngineException(){
        super();
    }

    public BooruEngineException(String message){
        super(message);
    }

    public BooruEngineException(Throwable cause){
        super(cause);
    }

    public BooruEngineException(String message, Throwable cause){
        super(message, cause);
    }

}
