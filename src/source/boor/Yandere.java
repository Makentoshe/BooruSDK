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
 * Storage data about Yandere API and methods for getting request.
 * Not supported "has_comments" and comment searching.
 * for each request need new csrf-token
 */
public class Yandere extends AbstractBoorAdvanced implements LoginModule, PostModule {

    private static final Yandere instance = new Yandere();

    private final Map<String, String> loginData = new HashMap<>();

    public static Yandere get() {
        return instance;
    }

    public void setFormat(Format format){
        this.format = format;
    }

    private Yandere(){
        super();
    }

    @Override
    public String getCustomRequest(String request) {
        return "https://yande.re" + request;
    }

    @Override
    public String getPackByTagsRequest(int limit, String tags, int page, Format format) {
        return getCustomRequest("/post."+format.toString().toLowerCase()+
                "?tags="+tags+"&limit=" + limit + "&page=" + page);
    }

    @Override
    public String getPostByIdRequest(int id, Format format) {
        return getCustomRequest("/post." + format.toString().toLowerCase() + "?tags=id:" + id);
    }

    @Override
    public String getCommentsByPostIdRequest(int post_id, Format format) {
        return null;
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
                case "created_at":{
                    post.setCreate_Time(pair.getValue());
                    break;
                }
            }
        }
        return post;
    }

    @Override
    public void logIn(final String login, final String password) throws BooruEngineException {
        if (!loginData.containsKey("yande.re") || !loginData.containsKey("authenticity_token")) {
            //get connection
            HttpsConnection connection = new HttpsConnection()
                    .setRequestMethod(Method.GET)
                    .setUserAgent(HttpsConnection.DEFAULT_USER_AGENT)
                    .openConnection(getCustomRequest("/user/login"));

            //set cookie
            if (!loginData.containsKey("yande.re")) {
                setCookie(connection);
            }
            //set token
            if (!loginData.containsKey("authenticity_token")) {
                setToken(connection);
            }
        }

        //if already have not cookie - throw an exception
        if (!loginData.containsKey("yande.re")) {
            throw new BooruEngineException("Can't find \"yande.re\" cookie in login data.");
        }
        //if already have not token - throw an exception
        if (!loginData.containsKey("authenticity_token")) {
            throw new BooruEngineException("Can't find \"authenticity_token\" in login data.");
        }

        //create new connection for login
        String postData = "authenticity_token=" + loginData.get("authenticity_token") + "&user%5Bname%5D=" + login +
                "&user%5Bpassword%5D=" + password + "&commit=Login";
        String cookie = "yande.re=" + loginData.get("yande.re");

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
                .filter(s -> s.contains("yande.re"))
                .forEach(s -> {
                    String[] split = s.split("=");
                    loginData.put(split[0], split[1].split("; ")[0]);
                });
    }

    private void setToken(final HttpsConnection connection) {
        String s = connection.getResponse();
        String data = s.split("\"csrf-token\" content=\"")[1]
                .split("\" />")[0]
                .replace("<meta content=", "")
                .replaceAll(Pattern.quote("+"), "%2B");

        System.out.println(data);
        loginData.put("authenticity_token", data);
    }
}
