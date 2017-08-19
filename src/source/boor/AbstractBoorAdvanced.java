package source.boor;

import source.Post;
import source.еnum.Api;
import source.еnum.Format;

import java.util.HashMap;

/**
 * Abstract class fo boor realisation with API v1.13.
 */
public abstract class AbstractBoorAdvanced extends AbstractBoor{

    protected Format format = Format.JSON;

    protected final Api api = Api.ADVANCED;

    @Override
    public final Format getFormat(){
        return format;
    }

    @Override
    public final Api getApi() {
        return api;
    }

    @Override
    public String getPostByIdRequest(int id, Format format) {
        return getCustomRequest("posts/" + String.valueOf(id) + "." + format.toString().toLowerCase());
    }

    @Override
    public String getPackByTagsRequest(int limit, String tags, int page, Format format){
        return getCustomRequest("post/index."+format.toString().toLowerCase()+
                "?tags="+tags+"&limit=" + limit + "&page=" + page);
    }

    @Override
    public Post newPostInstance(HashMap<String, String> attributes){
        return null;
    }
}
