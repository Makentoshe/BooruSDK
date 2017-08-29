package source.boor;

import source.еnum.Api;
import source.еnum.Format;


/**
 * Abstract class fo boor realisation with API v1.13.
 */
public abstract class AbstractBoorAdvanced extends AbstractBoor{

    public AbstractBoorAdvanced(){
        format = Format.JSON;
        api = Api.ADVANCED;
    }

    @Override
    public String getPostByIdRequest(int id, Format format) {
        return getCustomRequest("/posts/" + String.valueOf(id) + "." + format.toString().toLowerCase());
    }

    @Override
    public String getPackByTagsRequest(int limit, String tags, int page, Format format){
        return getCustomRequest("/post/index."+format.toString().toLowerCase()+
                "?tags="+tags+"&limit=" + limit + "&page=" + page);
    }
}
