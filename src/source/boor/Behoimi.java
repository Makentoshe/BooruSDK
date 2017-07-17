package source.boor;

import source.еnum.Api;
import source.еnum.Format;

/**
 * Singleton.
 * Storage data about Behoimi API, method for getting request and resolving data type.
 */
public class Behoimi extends AbstractBoorAdvanced {

    private static final Behoimi instance = new Behoimi();

    public static Behoimi get() {
        return instance;
    }

    private Format format = Format.JSON;

    public void setFormat(Format format) {
        this.format = format;
    }

    public Format getFormat() {
        return format;
    }

    @Override
    public String getCustomRequest(String request) {
        return "https://behoimi.org/" + request;
    }

}
