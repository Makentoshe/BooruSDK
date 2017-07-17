package source.boor;

import source.еnum.Api;
import source.еnum.Format;

/**
 * Singleton.
 * Storage data about Yandere API and method for getting request.
 */
public class Yandere extends AbstractBoorAdvanced {

    private static final Yandere instance = new Yandere();

    public static Yandere get() {
        return instance;
    }

    private Format format = Format.JSON;


    public void setFormat(Format format){
        this.format = format;
    }


    public Format getFormat() {
        return format;
    }



    @Override
    public String getCustomRequest(String request) {
        return "https://yande.re/" + request;
    }

}
