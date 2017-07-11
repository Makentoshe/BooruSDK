package source.boor;

import source.еnum.Api;
import source.еnum.Format;

/**
 * Singleton.
 * Storage data about Yandere API and method for getting request.
 */
public class Yandere extends AbstractBoor {

    private static final Yandere instance = new Yandere();

    public static Yandere get() {
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
        return "https://yande.re/post." + format.toString().toLowerCase() + "?" + request;
    }

}
