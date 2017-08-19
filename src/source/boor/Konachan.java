package source.boor;

import module.LoginModule;
import source.Post;
import source.еnum.Format;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Singleton.
 * Storage data about Konachan API and method for getting request.
 */
public class Konachan extends AbstractBoorAdvanced implements LoginModule{

    private static final Konachan instance = new Konachan();

    private String login;
    private String pass_hash;

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
    public String getCommentsByPostIdRequest(int post_id, Format format) {
        return getCustomRequest("comment."+format.toString().toLowerCase()+"?post_id=" + post_id);
    }

    @Override
    public Post newPostInstance(HashMap<String, String> attributes){
        Post post = new Post(instance);
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
                case "creator_id": {
                    post.setCreator_id(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "created_at":{
                    post.setCreate_Time(pair.getValue());
                    break;
                }
            }
        }
        post.setComments_url(getCommentsByPostIdRequest(post.getId()));
        return post;
    }

    @Override
    public void setUserData(String identify, String pass) {
        this.login = identify;
        this.pass_hash = pass;
    }

    @Override
    public String getUserData() {
        if (this.pass_hash != null && this.login != null){
            return "pass_hash=" + this.pass_hash + "; login=" + this.login;
        }
        return null;
    }

    @Override
    public String getIdentify() {
        return this.login;
    }

    @Override
    public String getPass() {
        return this.pass_hash;
    }
}
