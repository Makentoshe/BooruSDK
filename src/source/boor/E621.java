package source.boor;

import source.еnum.Api;
import source.еnum.Format;

/**
 * Singleton.
 * Storage data about E621 API and method for getting request
 */
public class E621 extends AbstractBoor {

    private static final E621 instance = new E621();

    public static E621 get(){
        return instance;
    }

    private Format format = Format.JSON;

    private final Api api = Api.ADVANCED;


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
        return "https://e621.net/post/index." + format.toString().toLowerCase() + "?" + request;
    }

}
