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
    public Post newPostInstance(HashMap<String, String> attributes){
        Post post = new Post(Boor.Gelbooru);
        //create Entry
        Set<Map.Entry<String, String>> entrySet = attributes.entrySet();
        //for each attribute
        for (Map.Entry<String, String> pair : entrySet) {
            switch (pair.getKey()){
                case "id":{
                    post.setId(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "md5":{
                    post.setMd5(pair.getValue());
                    break;
                }
                case "rating":{
                    post.setRating(pair.getValue());
                    break;
                }
                case "score":{
                    post.setScore(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "preview_url":{
                    post.setPreview_url("https:" + pair.getValue());
                    break;
                }
                case "tags":{
                    post.setTags(pair.getValue());
                    break;
                }
                case "sample_url":{
                    post.setSample_url("https:" + pair.getValue());
                    break;
                }
                case "file_url":{
                    post.setFile_url("https:" + pair.getValue());
                    break;
                }
                case "source":{
                    post.setSource(pair.getValue());
                    break;
                }
            }
        }
        return post;
    }

}