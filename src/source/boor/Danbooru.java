package source.boor;

import source.еnum.Boor;
import source.еnum.Format;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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


    public Item newItemInstance(HashMap<String, String> attributes){
        Item item = new Item(Boor.Danbooru);
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
                case "preview_file_url":{
                    item.setPreview_url("https://danbooru.donmai.us" + pair.getValue());
                    break;
                }
                case "tag_string":{
                    item.setTags(pair.getValue());
                    break;
                }
                case "file_url":{
                    item.setSample_url("https://danbooru.donmai.us" + pair.getValue());
                    break;
                }
                case "large_file_url":{
                    item.setFile_url("https://danbooru.donmai.us" + pair.getValue());
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
