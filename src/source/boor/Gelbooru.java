package source.boor;

import engine.BooruEngineException;
import engine.HttpsConnection;
import engine.Method;
import module.LoginModule;
import source.Post;
import source.еnum.Format;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Singleton.
 * Storage data about Gelbooru API and method for getting request
 */
public class Gelbooru extends AbstractBoorBasic implements LoginModule{

    private static final Gelbooru mInstance = new Gelbooru();

    private final Map<String, String> loginData = new HashMap<>(2);

    public static Gelbooru get() {
        return mInstance;
    }

    @Override
    public String getCustomRequest(String request) {
        return "https://gelbooru.com/" + request;
    }

    @Override
    public Post newPostInstance(final HashMap<String, String> attributes){
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
                    post.setCreate_Time(pair.getValue());
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
    public String getCommentsByPostIdRequest(final int post_id, final Format ignored) {
        return getCustomRequest("index.php?page=dapi&q=index&s=comment&post_id=" + post_id);
    }

    public void logIn(final String login, final String password) throws BooruEngineException{
        String gelbooruLogIn = getCustomRequest("index.php?page=account&s=login&code=00");
        String postData = "user="+login+"&pass="+password+"&submit=Log+in";

        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.POST)
                .setUserAgent(HttpsConnection.DEFAULT_USER_AGENT)
                .setBody(postData)
                .openConnection(gelbooruLogIn);


        for (int i = 0; i < connection.getHeader("Set-Cookie").size(); i++){
            String[] data = connection.getHeader("Set-Cookie").get(i).split("; ")[0].split("=");
            this.loginData.put(data[0], data[1]);
        }
    }

    @Override
    public void logOff(){
        this.loginData.clear();
    }

    @Override
    public Map<String, String> getLoginData(){
        return this.loginData;
    }

    public String votePost(final int id, final String action){
        return getCustomRequest("index.php?page=post&s=vote&id="+id+"&type=" + action);
    }
}