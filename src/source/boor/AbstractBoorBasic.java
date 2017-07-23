package source.boor;

import source.Post;
import source.еnum.Api;
import source.еnum.Format;

import java.util.HashMap;

/**
 * Abstract class fo boor realisation with Basic API.
 */
public abstract class AbstractBoorBasic extends AbstractBoor{

    protected final Format format = Format.XML;

    protected final Api api = Api.BASICS;

    @Override
    public final Format getFormat(){
        return format;
    }

    @Override
    public final Api getApi() {
        return api;
    }

    @Override
    public String getPostByIdRequest(int id, Format ignored) {
        return getCustomRequest("post&id=" + String.valueOf(id));
    }

    @Override
    public String getPackByTagsRequest(int limit, String tags, int page, Format ignored){
        return getCustomRequest("post&limit=" + limit + "&tags=" + tags + "&pid=" + page);
    }

    @Override
    public Post newPostInstance(HashMap<String, String> attributes){
        return null;
    }
}
