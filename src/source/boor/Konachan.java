package source.boor;

import source.еnum.Api;
import source.еnum.Format;

/**
 * Singleton.
 * Storage data about Konachan API and method for getting request.
 */
public class Konachan extends AbstractBoor {

    private static final Konachan instance = new Konachan();

    public static Konachan get() {
        return instance;
    }

    private final Api api = Api.ADVANCED;

    private Format format = Format.JSON;



    public void setFormat(Format format){
        this.format = format;
    }

    @Override
    public Api getApi() {
        return api;
    }

    public Format getFormat() {
        return format;
    }

    @Override
    public String getCustomRequest(String request, Format format) {
        return "https://konachan.com/post." + format.toString().toLowerCase() + "?" + request;
    }

}
