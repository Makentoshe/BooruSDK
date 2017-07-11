package source.boor;

import source.еnum.Api;
import source.еnum.Format;

/**
 * Singleton.
 * Storage data about Danbooru API and method for getting request
 */
public class Danbooru extends AbstractBoor {

    private static final Danbooru instance = new Danbooru();

    private static final Api api = Api.ADVANCED;

    private Format format = Format.XML;

    private final String LINK = "https://danbooru.donmai.us/posts.xml?";


    public static Danbooru get() {
        return instance;
    }

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
    public String getCompleteRequest(int itemCount, String request, int pid) {
        return LINK + "tags="+ request +"&limit="+itemCount+"&page=" + pid;
    }

    @Override
    public String getCustomRequest(String request) {
        return LINK + request;
    }
}
