package source.boor;

import source.еnum.Api;
import source.еnum.Format;

/**
 * Abstract class fo boor realisation with Basic API.
 */
public abstract class AbstractBoorBasic {

    private Format format = Format.XML;

    public Format getFormat(){
        return format;
    }

    private final Api api = Api.ADVANCED;

    public Api getApi() {
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
     * Construct request by id.
     *
     * @param id     - item id.
     * @return complete request for item with id.
     */
    public String getIdRequest(int id) {
        return getCustomRequest("post&id=" + String.valueOf(id));
    }

    /**
     * Create request for getting some items by tags.
     *
     * @param limit - how items must be in page.
     * @param request - request to server.
     * @param page - page index(from zero).
     * @return constructed request to this server.
     */
    public String getPackRequest(int limit, String request, int page){
        return getCustomRequest("post&limit=" + limit + "&tags=" + request + "&pid=" + page);
    }
}
