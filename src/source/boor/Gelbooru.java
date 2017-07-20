package source.boor;

import source.еnum.Boor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Singleton.
 * Storage data about Gelbooru API and method for getting request
 */
public class Gelbooru extends AbstractBoorBasic {

    private static final Gelbooru instance = new Gelbooru();

    public static Gelbooru get() {
        return instance;
    }

    @Override
    public String getCustomRequest(String request) {
        return "https://gelbooru.com/index.php?page=dapi&q=index&s=" + request;
    }

    @Override
    public Item newItemInstance(HashMap<String, String> attributes){
        Item item = new Item(Boor.Gelbooru);
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
                    item.setPreview_url("https:" + pair.getValue());
                    break;
                }
                case "tags":{
                    item.setTags(pair.getValue());
                    break;
                }
                case "sample_url":{
                    item.setSample_url("https:" + pair.getValue());
                    break;
                }
                case "file_url":{
                    item.setFile_url("https:" + pair.getValue());
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