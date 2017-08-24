package source.boor;

import engine.BooruEngineException;
import engine.HttpsConnection;
import engine.Method;
import module.LoginModule;
import module.PostModule;
import source.Post;
import source.еnum.Format;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Singleton.
 * Storage data about E621 API and method for getting request.
 */
public class E621 extends AbstractBoorAdvanced implements LoginModule, PostModule {

    private static final E621 instance = new E621();

    private final Map<String, String> loginData = new HashMap<>();

    public static E621 get(){
        return instance;
    }

    private E621(){
        super();
    }

    public void setFormat(Format format){
        this.format = format;
    }

    @Override
    public String getCustomRequest(String request) {
        return "https://e621.net" + request;
    }

    @Override
    public String getPostByIdRequest(int id, Format format) {
        return getCustomRequest("/post/show."+format.toString().toLowerCase()+"?id=" + id);
    }

    @Override
    public String getCommentsByPostIdRequest(int post_id, Format format) {
        return getCustomRequest("/comment/search."+format.toString().toLowerCase()+"?post_id=" + post_id);
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
            post.setComments_url(instance.getCommentsByPostIdRequest(post.getId()));
        }
        return post;
    }

    @Override
    public void logIn(final String login, final String password) throws BooruEngineException {
        if (!loginData.containsKey("e621") || !loginData.containsKey("authenticity_token")) {
            //get connection
            HttpsConnection connection = new HttpsConnection()
                    .setRequestMethod(Method.GET)
                    .setUserAgent(HttpsConnection.DEFAULT_USER_AGENT)
                    .openConnection(getCustomRequest("/user/login"));

            //set cookie
            if (!loginData.containsKey("e621")) {
                setCookie(connection);
            }
            //set token
            if (!loginData.containsKey("authenticity_token")) {
                setToken(connection);
            }
        }

        //if already have not cookie - throw an exception
        if (!loginData.containsKey("e621")) {
            throw new BooruEngineException("Can't find \"e621\" cookie in login data.");
        }
        //if already have not token - throw an exception
        if (!loginData.containsKey("authenticity_token")) {
            throw new BooruEngineException("Can't find \"authenticity_token\" in login data.");
        }

        //create new connection for login
        String postData = "authenticity_token=" + loginData.get("authenticity_token") + "&user%5Bname%5D=" + login +
                "&user%5Bpassword%5D=" + password + "&user%5Broaming%5D=0&user%5Broaming%5D=1";
        String cookie = "e621=" + loginData.get("e621");

        //create connection
        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.POST)
                .setUserAgent(HttpsConnection.DEFAULT_USER_AGENT)
                .setBody(postData)
                .setCookies(cookie)
                .openConnection(getAuthenticateRequest());

        if (connection.getHeader("Set-Cookie") == null) {
            throw new BooruEngineException(new NullPointerException());
        }

        for (int i = 0; i < connection.getHeader("Set-Cookie").size(); i++) {
            String[] data = connection.getHeader("Set-Cookie").get(i).split("; ")[0].split("=");
            if (data.length == 2) this.loginData.put(data[0], data[1]);
        }
    }

    @Override
    public void logOff() {
        this.loginData.clear();
    }

    @Override
    public Object getLoginData() {
        return this.loginData;
    }

    @Override
    public String getAuthenticateRequest() {
        return getCustomRequest("/user/authenticate");
    }

    private void setCookie(final HttpsConnection connection) {
        connection
                .getHeader("Set-Cookie")
                .stream()
                .filter(s -> s.contains("e621"))
                .forEach(s -> {
                    String[] split = s.split("=");
                    loginData.put(split[0], split[1].split("; ")[0]);
                });
    }

    private void setToken(final HttpsConnection connection) {
        String s = connection.getResponse();
        String data = s.split("<input name=\"authenticity_token\" type=\"hidden\" value=\"")[1]
                .split("\"></div>")[0]
                .replaceAll(Pattern.quote("+"), "%2B");
        loginData.put("authenticity_token", data);
    }
}
