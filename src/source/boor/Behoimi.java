package source.boor;

import source.еnum.Boor;
import source.еnum.Format;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Singleton.
 * Storage data about Behoimi API, method for getting request and resolving data type.
 */
public class Behoimi extends AbstractBoorAdvanced {

    private static final Behoimi instance = new Behoimi();

    public static Behoimi get() {
        return instance;
    }

    private Format format = Format.JSON;

    public void setFormat(Format format) {
        this.format = format;
    }

    public Format getFormat() {
        return format;
    }

    @Override
    public String getCustomRequest(String request) {
        return "http://behoimi.org/" + request;
    }

    @Override
    public String getIdRequest(int id, Format format) {
        return getCustomRequest("post/index." + format.toString().toLowerCase() + "?tags=id:" + id);
    }

    @Override
    public Post newPostInstance(HashMap<String, String> attributes){
        Post post = new Post(Boor.Behoimi);
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
