package source.boor;

import source.еnum.Api;
import source.еnum.Format;

import java.util.HashMap;

/**
 * Abstract class fo boor realisation with Basic API.
 */
public abstract class AbstractBoorBasic {

    private final Format format = Format.XML;

    public final Format getFormat(){
        return format;
    }

    private final Api api = Api.BASICS;

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
     * @param tags - the tags to search for.
     * @param page - page index(from zero).
     * @return constructed request to this server.
     */
    public String getPackByTagsRequest(int limit, String tags, int page){
        return getCustomRequest("post&limit=" + limit + "&tags=" + tags + "&pid=" + page);
    }


    /**
     * Empty method for boor.
     * Here we can create Item and create remote "constructor".
     *
     * @param attributes - list of all post attributes
     * @return Item entity with setted data.
     */
    public Item newItemInstance(HashMap<String, String> attributes){
        return null;
    }
}
