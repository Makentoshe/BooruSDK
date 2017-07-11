package source.boor;

import source.еnum.Api;
import source.еnum.Format;

/**
 * Singleton.
 * Storage data about Behoimi API, method for getting request and resolving data type.
 */
public class Behoimi extends AbstractBoor {

    private static final Behoimi instance = new Behoimi();

    public static Behoimi get() {
        return instance;
    }



    private final Api api = Api.ADVANCED;

    @Override
    public Api getApi() {
        return api;
    }



    private Format format = Format.XML;

    public void setFormat(Format format) {
        this.format = format;
    }

    public Format getFormat() {
        return format;
    }

    @Override
    public String getCustomRequest(String request, Format format) {
        return "http://behoimi.org/post/index." + format.toString().toLowerCase() + "?" + request;
    }
}
