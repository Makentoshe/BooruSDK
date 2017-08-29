package engine;

/**
 * Exception for BEL.
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
