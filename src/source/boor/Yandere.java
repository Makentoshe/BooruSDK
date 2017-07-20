package source.boor;

import source.еnum.Api;
import source.еnum.Boor;
import source.еnum.Format;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Singleton.
 * Storage data about Yandere API and method for getting request.
 */
public class Yandere extends AbstractBoorAdvanced {

    private static final Yandere instance = new Yandere();

    public static Yandere get() {
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
        return "https://yande.re/" + request;
    }

    @Override
    public String getPackByTagsRequest(int limit, String tags, int page, Format format) {
        return getCustomRequest("post."+format.toString().toLowerCase()+"?tags="+tags+"&limit=" + limit + "&page=" + page);
    }

    @Override
    public String getIdRequest(int id, Format format) {
        return getCustomRequest("post." + format.toString().toLowerCase() + "?tags=id:" + id);
    }

    public Item newItemInstance(HashMap<String, String> attributes){
        Item item = new Item(Boor.Yandere);
        //create Entry
        Set<Map.Entry<String, String>> entrySet = attributes.entrySet();
        //for each attribute
        for (Map.Entry<String, String> pair : entrySet) {
            switch (pair.getKey()){
                case "id":{
                    item.setId(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "md5":{
                    item.setMd5(pair.getValue());
                    break;
                }
                case "rating":{
                    item.setRating(pair.getValue());
                    break;
                }
                case "score":{
                    item.setScore(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "preview_url":{
                    item.setPreview_url(pair.getValue());
                    break;
                }
                case "tags":{
                    item.setTags(pair.getValue());
                    break;
                }
                case "sample_url":{
                    item.setSample_url(pair.getValue());
                    break;
                }
                case "file_url":{
                    item.setFile_url(pair.getValue());
                    break;
                }
                case "source":{
                    item.setSource(pair.getValue());
                    break;
                }
            }
        }
        return item;
    }

}
