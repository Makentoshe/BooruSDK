package source.boor;

import source.Post;
import source.еnum.Format;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Singleton.
 * Storage data about Gelbooru API and method for getting request
 */
public class Gelbooru extends AbstractBoorBasic {

    private static final Gelbooru mInstance = new Gelbooru();

    public static Gelbooru get() {
        return mInstance;
    }

    private String mPassHash;
    private int mUserId = -1;

    public void setCookies(String passHash, int userId){
        this.mPassHash = passHash;
        this.mUserId = userId;
    }

    public String getCookies(){
        if (this.mPassHash != null && !this.mPassHash.equals("") && this.mUserId != -1){
            return "pass_hash=" + this.mPassHash + "; user_id=" + this.mUserId;
        }
        return null;
    }

    @Override
    public String getCustomRequest(String request) {
        return "https://gelbooru.com/" + request;
    }

    @Override
    public Post newPostInstance(HashMap<String, String> attributes){
        Post post = new Post(mInstance);
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
                case "creator_id": {
                    post.setCreator_id(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "has_comments": {
                    if ("true".equals(pair.getValue())) {
                        post.setHas_comments(true);
                    } else {
                        post.setHas_comments(false);
                    }
                    break;
                }
                case "created_at":{
                    post.setCreate_time(pair.getValue());
                    break;
                }
            }
        }
        //after all check comments flag
        if (post.isHas_comments()){
            //and if true - setup comments url.
            post.setComments_url(mInstance.getCommentsByPostIdRequest(post.getId()));
        }
        return post;
    }

    @Override
    public String getCommentsByPostIdRequest(int post_id, Format ignored) {
        return getCustomRequest("index.php?page=dapi&q=index&s=comment&post_id=" + post_id);
    }
}