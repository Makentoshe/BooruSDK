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

    private final Api api = Api.BASICS;

    private final Format format = Format.XML;



    public Api getApi() {
        return api;
    }

    public Format getFormat() {
        return format;
    }

    @Override
    public String getCustomRequest(String request) {
        return "https://gelbooru.com/index.php?page=dapi&q=index&s=" + request;
    }

}