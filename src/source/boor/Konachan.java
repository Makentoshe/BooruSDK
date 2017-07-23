package source.boor;

import source.Post;
import source.еnum.Boor;
import source.еnum.Format;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Singleton.
 * Storage data about Konachan API and method for getting request.
 */
public class Konachan extends AbstractBoorAdvanced {

    private static final Konachan instance = new Konachan();

    public static Konachan get() {
        return instance;
    }



    public void setFormat(Format format){
        this.format = format;
    }



    @Override
    public String getCustomRequest(String request) {
        return "https://konachan.com/" + request;
    }

    @Override
    public String getPostByIdRequest(int id, Format format) {
        return getCustomRequest("post."+format.toString().toLowerCase()+"?tags=id:" + id);
    }

    @Override
    public Post newPostInstance(HashMap<String, String> attributes){
        Post post = new Post(Boor.Konachan);
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
                    post.setPreview_url(pair.getValue());
                    break;
                }
                case "tags":{
                    post.setTags(pair.getValue());
                    break;
                }
                case "sample_url":{
                    post.setSample_url(pair.getValue());
                    break;
                }
                case "file_url":{
                    post.setFile_url(pair.getValue());
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
