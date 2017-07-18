package source.boor;

import source.еnum.Api;
import source.еnum.Format;

/**
 * Abstract class fo boor realisation with API v1.13.
 */
public abstract class AbstractBoorAdvanced {

    public abstract Format getFormat();

    private final Api api = Api.ADVANCED;

    public final Api getApi() {
        return api;
    }


    /**
     * Construct custom request.
     *
     * @param request - request.
     * @return complete get request
     */
    public abstract String getCustomRequest(String request);

    /**
     * Construct request by id. The format is getting from the getFormat() method.
     * As default there will be JSON format.
     *
     * @param id - item id.
     * @return complete request for item with id.
     */
    public final String getIdRequest(int id) {
        return getIdRequest(id, getFormat());
    }

    /**
     * Construct request by id.
     *
     * @param id     - item id.
     * @param format - result format.
     * @return complete request for item with id.
     */
    public String getIdRequest(int id, Format format) {
        return getCustomRequest("posts/" + String.valueOf(id) + "." + format.toString().toLowerCase());
    }


    /**
     * Create request for getting some items by tags.
     *
     * @param limit - how items must be in page.
     * @param tags - the tags to search for.
     * @param page - page index(from zero).
     * @param format - format result(Can be JSON or XML)
     * @return constructed request to this server.
     */
    public String getPackByTagsRequest(int limit, String tags, int page, Format format){
        return getCustomRequest("post/index."+format.toString().toLowerCase()+"?tags="+tags+"&limit=" + limit + "&page=" + page);
    }

    /**
     * Create request for getting some items by tags. The format is getting from the getFormat() method.
     * As default there will be JSON format.
     *
     * @param limit - how items must be in page.
     * @param tags - the tags to search for.
     * @param page - page index(from zero).
     * @return constructed request to this server.
     */
    public final String getPackByTagsRequest(int limit, String tags, int page){
        return getPackByTagsRequest(limit, tags, page, getFormat());
    }
}
