package source.boor;

import source.еnum.Api;
import source.еnum.Format;

/**
 * Singleton.
 * Storage data about Danbooru API and method for getting request
 */
public class Danbooru extends AbstractBoor {

    private static final Danbooru instance = new Danbooru();

    public static Danbooru get() {
        return instance;
    }



    private static final Api api = Api.ADVANCED;

    @Override
    public Api getApi() {
        return api;
    }



    private Format format = Format.JSON;

    public void setFormat(Format format){
        this.format = format;
    }

    public Format getFormat() {
        return format;
    }

    @Override
    public String getCustomRequest(String request, Format format) {
        return "https://danbooru.donmai.us/posts." + format.toString().toLowerCase() + "?" + request;
    }

}
