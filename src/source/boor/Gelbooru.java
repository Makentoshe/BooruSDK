package source.boor;

import source.еnum.Api;
import source.еnum.Format;

/**
 * Singleton.
 * Storage data about Gelbooru API and method for getting request
 */
public class Gelbooru extends AbstractBoorBasic {

    private static final Gelbooru instance = new Gelbooru();

    public static Gelbooru get() {
        return instance;
    }


    @Override
    public String getCustomRequest(String request) {
        return "https://gelbooru.com/index.php?page=dapi&q=index&s=" + request;
    }

}