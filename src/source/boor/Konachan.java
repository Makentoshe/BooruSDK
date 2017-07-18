package source.boor;

import source.еnum.Api;
import source.еnum.Format;

/**
 * Singleton.
 * Storage data about Konachan API and method for getting request.
 */
public class Konachan extends AbstractBoorAdvanced {

    private static final Konachan instance = new Konachan();

    public static Konachan get() {
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
        return "https://konachan.com/" + request;
    }

    @Override
    public String getIdRequest(int id, Format format) {
        return getCustomRequest("post."+format.toString().toLowerCase()+"?tags=id:" + id);
    }
}
