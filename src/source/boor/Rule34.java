package source.boor;

import engine.BooruEngineException;
import engine.HttpsConnection;
import engine.Method;
import module.interfacе.CommentModuleInterface;
import module.interfacе.LoginModuleInterface;
import module.interfacе.RemotePostModuleInterface;
import module.interfacе.VotingModuleInterface;
import source.Post;
import source.еnum.Format;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Singleton.
 * <p>
 * Describe Rule34.
 * <p>
 * Implements <tt>LoginModuleInterface</tt>, <tt>VotingModuleInterface</tt>, <tt>RemotePostModuleInterface</tt>, <tt>CommentModuleInterface</tt>.
 */
/*NOTE:
    Cookie are static.
    csrf-token disable.

    Login is OK
    Commenting is OK
    Post Voting is OK
 */
public class Rule34 extends AbstractBoorBasic implements LoginModuleInterface, VotingModuleInterface, RemotePostModuleInterface, CommentModuleInterface {

    private static final Rule34 instance = new Rule34();

    private Map<String, String> loginData = new HashMap<>(2);

    public static Rule34 get() {
        return instance;
    }

    private Rule34(){
        super();
    }

    @Override
    public String getCustomRequest(String request) {
        return "https://rule34.xxx" + request;
    }

    @Override
    public String getCommentsByPostIdRequest(int post_id, Format format) {
        return getCustomRequest("/index.php?page=dapi&q=index&s=comment&post_id=" + post_id);
    }

    @Override
    public Post newPostInstance(final Map<String, String> attributes){
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
                }case "creator_id": {
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
            post.setComments_url(instance.getCommentsByPostIdRequest(post.getId()));
        }
        return post;
    }

    @Override
    public String getAuthenticateRequest() {
        return getCustomRequest("/index.php?page=account&s=login&code=00");
    }

    @Override
    public void logIn(final String login, final String password) throws BooruEngineException {
        String postData = "user="+login+"&pass="+password+"&submit=Log+in";

        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.POST)
                .setUserAgent(HttpsConnection.DEFAULT_USER_AGENT)
                .setBody(postData)
                .openConnection(getAuthenticateRequest());

        for (int i = 0; i < connection.getHeader("Set-Cookie").size(); i++){
            String[] data = connection.getHeader("Set-Cookie").get(i).split("; ")[0].split("=");
            if (data.length == 2) this.loginData.put(data[0], data[1]);        }
    }

    @Override
    public void logOff(){
        this.loginData.clear();
    }

    @Override
    public Map<String, String> getLoginData(){
        return this.loginData;
    }

    //up of down
    @Override
    public boolean votePost(final int id, final String action) throws BooruEngineException{
        if (!action.equals("up")) return false;

        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.GET)
                .setUserAgent(HttpsConnection.DEFAULT_USER_AGENT)
                .setCookies(loginData.toString().replaceAll(", ", "; "))
                .openConnection(getVotePostRequest() + "&id=" + id + "&type=" + action);

        return !connection.getResponse().equals("");
    }

    @Override
    public String getVotePostRequest() {
        return getCustomRequest("/index.php?page=post&s=vote");
    }

    @Override
    public boolean commentPost(int id, String body, boolean postAsAnon, boolean bumpPost) throws BooruEngineException {
        String cbody =
                "comment="+body.replaceAll(" ", "+")+
                        "&post_anonymous="+ (postAsAnon?"on":"off") +
                        "&submit=Post+comment&conf=2";

        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.POST)
                .setUserAgent(HttpsConnection.DEFAULT_USER_AGENT)
                .setCookies(loginData.toString().replaceAll(", ", "; "))
                .setBody(cbody)
                .openConnection(getCreateCommentRequest(id));

        return connection.getResponse().equals("") && connection.getResponseCode() == 302;
    }

    @Override
    public String getCreateCommentRequest(final int id) {
        return getCustomRequest("/index.php?page=comment&id="+id+"&s=save");
    }

    @Override
    public String getCookieFromLoginData() {
        return getLoginData().toString().replaceAll(", ", "; ").replaceAll("\\{","").replaceAll("\\}", "");
    }
}
