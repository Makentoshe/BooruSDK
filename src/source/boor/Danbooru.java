package source.boor;

import source.еnum.Api;
import source.еnum.Format;

/**
 * Singleton.
 * Storage data about Danbooru API and method for getting request
 */
public class Danbooru extends AbstractBoorAdvanced {

    private static final Danbooru instance = new Danbooru();

    public static Danbooru get() {
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
        return "https://danbooru.donmai.us/" + request;
    }

    @Override
    public String getPackByTagsRequest(int limit, String tags, int page, Format format){
        return getCustomRequest("posts."+format.toString().toLowerCase()+"?tags="+tags+"&limit=" + limit + "&page=" + page);
    }
}
