package source.boor;

import source.еnum.Boor;
import source.еnum.Format;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Singleton.
 * Storage data about E621 API and method for getting request
 */
public class E621 extends AbstractBoorAdvanced {

    private static final E621 instance = new E621();

    public static E621 get(){
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
        return "https://e621.net/" + request;
    }

    @Override
    public String getIdRequest(int id, Format format) {
        return getCustomRequest("post/show."+format.toString().toLowerCase()+"?id=" + id);
    }

    public Post newPostInstance(HashMap<String, String> attributes){
        Post post = new Post(Boor.E621);
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
