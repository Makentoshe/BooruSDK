package engine;

/**
 * Created by Makentoshe on 10.07.2017.
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
