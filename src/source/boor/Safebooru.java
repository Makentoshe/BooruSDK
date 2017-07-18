package source.boor;

import source.еnum.Api;
import source.еnum.Format;

/**
 * Singleton.
 * Storage data about Safebooru API and method for getting request
 */
public class Safebooru extends AbstractBoorBasic{

    private static final Safebooru instance = new Safebooru();

    public static Safebooru get() {
        return instance;
    }

    @Override
    public String getCustomRequest(String request) {
        return "https://safebooru.org/index.php?page=dapi&q=index&s=" + request;
    }

}
