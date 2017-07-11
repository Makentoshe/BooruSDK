package source.boor;

import source.еnum.Api;
import source.еnum.Format;

/**
 * Singleton.
 * Storage data about Gelbooru API and method for getting request
 */
public class Gelbooru extends AbstractBoor {

    private static final Gelbooru instance = new Gelbooru();

    public static Gelbooru get() {
        return instance;
    }

    private final Api api = Api.BASICS;

    private final Format format = Format.XML;

    private final String LINK = "https://gelbooru.com/index.php?page=dapi&s=post&q=index&";

    @Override
    public Api getApi() {
        return api;
    }

    public Format getFormat() {
        return format;
    }

    @Override
    public String getCompleteRequest(int itemCount, String request, int pid) {
        return LINK + "limit=" + itemCount + "&tags=" + request + "&pid=" + pid;
    }

    @Override
    public String getCustomRequest(String request) {
        return LINK + request;
    }
}