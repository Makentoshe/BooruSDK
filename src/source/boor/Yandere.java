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

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Singleton.
 * <p>
 * Describe Yandere.
 * <p>
 * Implements <tt>LoginModuleInterface</tt>, <tt>VotingModuleInterface</tt>, <tt>RemotePostModuleInterface</tt>, <tt>CommentModuleInterface</tt>.
 */
/*NOTE:
    Not supported "has_comments" and comment searching.

    Cookie are not static
    csrf-token are not static

    Login is OK
    Commenting is ...
    Post Voting is OK
 */
public class Yandere extends AbstractBoorAdvanced implements LoginModuleInterface, RemotePostModuleInterface, VotingModuleInterface, CommentModuleInterface {

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
                    .setUserAgent(HttpsConnection.getDefaultUserAgent())
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
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
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

    private void setToken(final HttpsConnection connection)throws BooruEngineException {
        String s = connection.getResponse();
        String data = s.split("\"csrf-token\" content=\"")[1]
                .split("\" />")[0]
                .replace("<meta content=", "")
                .replaceAll(Pattern.quote("+"), "%2B");

        loginData.put("authenticity_token", data);
    }

    @Override
    public boolean votePost(int id, String score) throws BooruEngineException {
        String token;
        try{
            token = loginData.get("authenticity_token").replaceAll("%2B", "+");
        } catch (NullPointerException e){
            throw new BooruEngineException("User data not defined.", new AuthenticationException());
        }
        try {
            HttpsConnection connection = new HttpsConnection()
                    .setRequestMethod(Method.POST)
                    .setUserAgent(HttpsConnection.getDefaultUserAgent())
                    .setCookies(loginData.toString().replaceAll(", ", "; "))
                    .setHeader("X-CSRF-Token", token)
                    .setBody("id=" + id + "&score=" + score)
                    .openConnection(getVotePostRequest());

            return connection.getResponse().split("\"success\":")[1].equals("true}");

        } catch (BooruEngineException e) {
            throw new BooruEngineException(e.getCause().getMessage());
        }
    }

    @Override
    public String getVotePostRequest() {
        return getCustomRequest("/post/vote.json");
    }

    @Override
    public boolean commentPost(int id, String body, boolean postAsAnon, boolean bumpPost) throws BooruEngineException {
        boolean out = false;
        HttpsConnection connection;
        StringBuilder cbody = new StringBuilder();
        //create connection for getting token and cookies
        connection = new HttpsConnection()
                .setRequestMethod(Method.GET)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .openConnection(getCustomRequest(""));
        setToken(connection);
        setCookie(connection);

        //create post body
        cbody.append("authenticity_token=").append(loginData.get("authenticity_token"))
                .append("&comment%5Bpost_id%5D=").append(id)
                .append("&comment%5Bbody%5D=").append(body.replaceAll(" ", "+"))
                .append("&commit=Post");

        //send body to server
        connection = new HttpsConnection()
                .setRequestMethod(Method.POST)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .setCookies(loginData.toString().replaceAll(", ", "; "))
                .setBody(cbody.toString())
                .openConnection(getCreateCommentRequest(id));

        //try to get Set-Cookie header
        //if failed(NPE) - catch and throw BEE
        try {
            for (int i = 0; i < connection.getHeader("Set-Cookie").size(); i++) {
                String[] data = connection.getHeader("Set-Cookie").get(i).split("; ")[0].split("=");
                //if notice=Comment+created - OK
                //else throw RE
                if (data[0].equals("notice")) {
                    if (data[1].equals("Comment+created")) {
                        out = true;
                    } else {
                        throw new BooruEngineException(new RuntimeException(data[1].replaceAll(Pattern.quote("+"), " ")));
                    }
                }
                //and of course put cookie in data
                if (data[0].equals("yande.re")) loginData.put(data[0], data[1]);
            }
        } catch (NullPointerException e) {
            throw new BooruEngineException(
                    "Something go wrong and server can't process it.",
                    new NoSuchElementException("Set-Cookie")
            );
        }

        return out;
    }

    @Override
    public String getCreateCommentRequest(int id) {
        return getCustomRequest("/comment/create");
    }

    @Override
    public String getCookieFromLoginData() {
        return getLoginData().toString().replaceAll(", ", "; ").replaceAll("\\{","").replaceAll("\\}", "");
    }
}
