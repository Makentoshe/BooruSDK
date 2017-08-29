package source.boor;

import source.еnum.Api;
import source.еnum.Format;

/**
 * Abstract class fo boor realisation with Basic API.
 */
public abstract class AbstractBoorBasic extends AbstractBoor{

    public AbstractBoorBasic(){
        format = Format.XML;
        api = Api.BASICS;
    }

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
        return getCustomRequest("/index.php?page=dapi&q=index&s=post&id=" + String.valueOf(id));
    }

    @Override
    public String getPackByTagsRequest(int limit, String tags, int page, Format ignored){
        return getCustomRequest("/index.php?page=dapi&q=index&s=post&limit=" + limit +
                "&tags=" + tags + "&pid=" + page);
    }
}
