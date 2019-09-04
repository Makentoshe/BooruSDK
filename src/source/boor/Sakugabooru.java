package source.boor;

import com.sun.istack.internal.NotNull;
import engine.BooruEngineException;
import engine.MultipartConstructor;
import engine.connector.HttpsConnection;
import engine.connector.Method;
import engine.parser.JsonParser;
import source.interfaces.*;
import source.Post;
import source.еnum.Api;
import source.еnum.Format;
import source.еnum.Rating;

import javax.naming.AuthenticationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/*
  Note:
    Not supported "has_comments" and comment searching.

    Cookies are not static. for each request need new cookie data.
    csrf-token is not static. for each request need new token.

    Login is OK
    Commenting is OK
    Post Voting is OK
 */

/**
 * Singleton which describe Yandere. This class can help user to login, vote posts, create posts, comment posts, etc.
 * Default {@code format} is {@code Format.XML}. Default {@code api} is {@code API.Basic}.
 * <p>
 * Implements <code>Login</code>,<code>PostVoting</code>,
 * <code>PostCreator</code>, <code>CommentUploader</code>,
 * <code>PostUploader</code>.
 */
public class Sakugabooru extends AbstractBoor
        implements Login, PostCreator, PostVoting, CommentUploader, PostUploader, Autocomplete {

    private final static Sakugabooru instance = new Sakugabooru();

    private final Map<String, String> loginData = new HashMap<>();

    /**
     * Get access to Yandere.
     *
     * @return self.
     */
    public static Sakugabooru get() {
        return instance;
    }

    private Sakugabooru() {
        format = Format.JSON;
        api = Api.ADVANCED;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    /**
     * Get a host machine name and create custom request.
     *
     * @param request request.
     * @return the host machine address.
     */
    @Override
    public String getCustomRequest(String request) {
        return "https://www.sakugabooru.com" + request;
    }

    /**
     * Create request for getting some posts by tags.
     *
     * @param limit  how many items must be in page.
     * @param tags   the tags to search for.
     * @param page   page index(from zero).
     * @param format format result.
     * @return constructed request to this server.
     */
    @Override
    public String getPostsByTagsRequest(int limit, String tags, int page, Format format) {
        return getCustomRequest("/post." + format.toString().toLowerCase() + "" +
                "?tags=" + tags + "&limit=" + limit + "&page=" + page);
    }

    /**
     * <strong>NOT SUPPORTED NOW.</strong>
     *
     * @param post_id post, for which comment will be searching.
     * @param format  result format (can be {@code Format.JSON} or {@code Format.XML}).
     * @return always null.
     */
    @Override
    public String getCommentsByPostIdRequest(int post_id, Format format) {
        return null;
    }

    @Override
    public String getPostLinkById(int post_id) {
        return getCustomRequest("/post/show/" + post_id);
    }

    /**
     * Get address for getting <code>Post</code> by post id.
     *
     * @param id     post id.
     * @param format result format (can be {@code Format.JSON} or {@code Format.XML}).
     * @return the constructed request to server.
     */
    @Override
    public String getPostByIdRequest(int id, Format format) {
        return getCustomRequest("/post." + format.toString().toLowerCase() + "?tags=id:" + id);
    }

    /**
     * Remote <code>Post</code> constructor specified on posts from Sakugabooru.
     * Implement same as Post#defaultConstructor.
     *
     * @param attributes map of all post attributes.
     * @return the constructed <code>Post</code>.
     */
    @Override
    public Post newPostInstance(final Map<String, String> attributes) {
        Post post = new Post(instance);
        //create Entry
        Set<Map.Entry<String, String>> entrySet = attributes.entrySet();
        //for each attribute
        for (Map.Entry<String, String> pair : entrySet) {
            switch (pair.getKey()) {
                case "id": {
                    post.setId(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "md5": {
                    post.setMd5(pair.getValue());
                    break;
                }
                case "rating": {
                    post.setRating(pair.getValue());
                    break;
                }
                case "score": {
                    post.setScore(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "preview_url": {
                    post.setPreview_url(pair.getValue());
                    break;
                }
                case "tags": {
                    post.setTags(pair.getValue());
                    break;
                }
                case "sample_url": {
                    post.setSample_url(pair.getValue());
                    break;
                }
                case "file_url": {
                    post.setFile_url(pair.getValue());
                    break;
                }
                case "source": {
                    post.setSource(pair.getValue());
                    break;
                }
                case "creator_id": {
                    post.setCreator_id(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "created_at": {
                    post.setCreate_Time(pair.getValue());
                    break;
                }
                case "height":{
                    post.setHeight(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "width":{
                    post.setWidth(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "sample_height":{
                    post.setSample_height(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "sample_width":{
                    post.setSample_width(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "preview_height":{
                    post.setPreview_height(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "preview_width":{
                    post.setPreview_width(Integer.parseInt(pair.getValue()));
                    break;
                }
            }
        }
        return post;
    }

    /**
     * Create connection to server and get user data - login cookies.
     * All necessary data will be stored while method is work,
     * so there is no reason try to store data from <code>HttpsConnection</code>.
     *
     * @param login    user login.
     * @param password user pass.
     * @return connection with all data about request.
     * @throws BooruEngineException when something go wrong. Use <code>getCause</code> to see more details.
     *                              Note that exception can be contain one of:
     *                              <p>{@code IllegalStateException} will be thrown when the user data is not defined.
     *                              <p>{@code BooruEngineConnectionException} will be thrown when something go wrong with connection.
     *                              <p>{@code AuthenticationException} will be thrown when the authentication failed
     *                              and response did not contain a login cookies.
     */
    @Override
    public HttpsConnection logIn(String login, String password) throws BooruEngineException {
        if (!loginData.containsKey("sakugabooru") || !loginData.containsKey("authenticity_token")) {
            //get connection
            HttpsConnection connection = new HttpsConnection()
                    .setRequestMethod(Method.GET)
                    .setUserAgent(HttpsConnection.getDefaultUserAgent())
                    .openConnection(getCustomRequest("/user/login"));
            //set cookie
            if (!loginData.containsKey("sakugabooru")) setCookie(connection);
            //set token
            if (!loginData.containsKey("authenticity_token")) setToken(connection);
        }

        //if already have not cookie - throw an exception
        if (!loginData.containsKey("sakugabooru")) {
            throw new BooruEngineException("Can't find \"sakugabooru\" cookie in login data.", new IllegalStateException());
        }
        //if already have not token - throw an exception
        if (!loginData.containsKey("authenticity_token")) {
            throw new BooruEngineException("Can't find \"authenticity_token\" in login data.", new IllegalStateException());
        }
        //create new connection for login
        String postData = "authenticity_token=" + loginData.get("authenticity_token") + "&user%5Bname%5D=" + login +
                "&user%5Bpassword%5D=" + password + "&commit=Login";
        String cookie = "sakugabooru=" + loginData.get("sakugabooru");
        //create connection
        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.POST)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .setBody(postData)
                .setCookies(cookie)
                .openConnection(getAuthenticateRequest());
        //try to parse response
        try {
            for (int i = 0; i < connection.getHeader("Set-Cookie").size(); i++) {
                String[] data = connection.getHeader("Set-Cookie").get(i).split("; ")[0].split("=");
                if (data.length == 2) this.loginData.put(data[0], data[1]);
            }
            //if unsuccessful
        } catch (NullPointerException e) {
            //throw exception
            throw new BooruEngineException(new AuthenticationException("Authentication failed."));
        }
        return connection;
    }

    /**
     * Log off user. Remove all user data.
     */
    @Override
    public void logOff() {
        this.loginData.clear();
    }

    /**
     * Get access to login data. All data storage in <code>Hashmap&lt;String, String&gt;</code>.
     *
     * @return the HashMap which contain a user data.
     */
    @Override
    public HashMap<String, String> getLoginData() {
        return (HashMap<String, String>) this.loginData;
    }

    /**
     * Get address for sending {@code Method.POST} request for authentication to server.
     *
     * @return the constructed request to server.
     */
    @Override
    public String getAuthenticateRequest() {
        return getCustomRequest("/user/authenticate");
    }

    /**
     * Method for voting post with id <code>post_id</code>. Scores can be: "1" for up vote and "2" for up vote too,
     * "3" for vote up and add to favorites, and "0" for remove vote.
     * If <code>score</code> will be another string the <code>IllegalArgumentException</code> will be thrown.
     * <p>
     * Method creating connection and send POST-request.
     *
     * @param post_id post id.
     * @param score   scores to post.
     * @return connection with post-request response.
     * @throws BooruEngineException when something go wrong. Use <code>getCause</code> to see more details.
     *                              Note that exception can be contain one of:
     *                              <p>{@code IllegalStateException} will be thrown when the user data is not defined.
     *                              <p>{@code BooruEngineConnectionException} will be thrown when something go wrong with connection.
     *                              <p>{@code IllegalArgumentException} will be thrown when {@param score} not contain expected value.
     */
    @Override
    public HttpsConnection votePost(int post_id, String score) throws BooruEngineException {
        //check userdata
        String token;
        if (getCookieFromLoginData() == null) {
            throw new BooruEngineException(new IllegalStateException("User data not defined."));
        }
        //validate action
        try {
            int s = Integer.parseInt(score);
            if (s > 3 || s < 0) {
                throw new BooruEngineException("Score can't be more then the 3 and less than the 0", new IllegalArgumentException(score));
            }
        } catch (NumberFormatException e) {
            throw new BooruEngineException(new IllegalArgumentException(score));
        }
        //setup fresh data
        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.GET)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .openConnection(getCustomRequest(""));
        setToken(connection);
        setCookie(connection);
        //setup token
        token = loginData.get("authenticity_token").replaceAll("%2B", "+").replaceAll("%3D", "=").replaceAll("%2F", "/");
        //create request
        connection = new HttpsConnection()
                .setRequestMethod(Method.POST)
                .setHeader("X-CSRF-Token", token)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .setCookies(getCookieFromLoginData())
                .setBody("id=" + post_id + "&score=" + score)
                .openConnection(getVotePostRequest(post_id));

        return connection;
    }

    /**
     * Get address for creating <code>Method.POST</code> request for voting post.
     *
     * @return the constructed request to server.
     */
    @Override
    public String getVotePostRequest(int post_id) {
        return getCustomRequest("/post/vote.json");
    }

    /**
     * Create comment for post with id <code>post_id</code>. Params <code>postAsAnon</code> and
     * <code>bumpPost</code> is useless because they are not supporting.
     * <p>
     * Method creating connection and send POST-request.
     *
     * @param post_id    post id, for which comment will be created.
     * @param body       comment body.
     * @param postAsAnon using for anonymously posting.
     * @param bumpPost   using for bump up post.
     * @return connection with post-request response.
     * @throws BooruEngineException when something go wrong. Use <code>getCause</code> to see more details.
     *                              Note that exception can be contain one of:
     *                              <p>{@code IllegalStateException} will be thrown when user data is not defined.
     *                              <p>{@code BooruEngineConnectionException} will be thrown when something go wrong with connection.
     */
    @Override
    public HttpsConnection createCommentToPost(int post_id, String body, boolean postAsAnon, boolean bumpPost) throws BooruEngineException {
        //check userdata
        if (getCookieFromLoginData() == null) {
            throw new BooruEngineException(new IllegalStateException("User data not defined"));
        }
        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.GET)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .openConnection(getCustomRequest(""));
        setToken(connection);
        setCookie(connection);

        String cbody = "authenticity_token=" + loginData.get("authenticity_token") +
                "&comment%5Bpost_id%5D=" + post_id +
                "&comment%5Bbody%5D=" + body.replaceAll(" ", "+") +
                "&commit=Post";

        connection = new HttpsConnection()
                .setRequestMethod(Method.POST)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .setCookies(getCookieFromLoginData())
                .setBody(cbody)
                .openConnection(getCommentRequest(post_id));

        return connection;
    }

    /**
     * Get address for creating <code>Method.POST</code> request for creating post.
     *
     * @return the constructed request to server.
     */
    @Override
    public String getCommentRequest(int id) {
        return getCustomRequest("/comment/create");
    }

    /**
     * Remake user data current format to <code>String</code> which will be contain user cookie.
     * If data not defined the method will be return <code>null</code>.
     *
     * @return the user cookie.
     */
    @Override
    public String getCookieFromLoginData() {
        if (getLoginData().size() == 0) return null;
        return getLoginData().toString().replaceAll(", ", "; ").replaceAll("\\{", "").replaceAll("\\}", "");
    }

    //TODO: test method

    /**
     * Create upload on Sakugabooru.
     *
     * @param post      file which will be upload. It must be image of gif-animation.
     *                  Also it can be video file with .webm extension.
     * @param tags      tags are describe file content. They separates by spaces,
     *                  so, spaces in title must be replace by underscores.
     * @param title     post title. <strong>Useless in this method.</strong>
     * @param source    source from file was get. It must be URL like "https://sas.com/test.jpg" or something else.
     *                  <strong>Not required in this method.</strong>
     * @param rating    post rating. As usual it can be {@code Rating.SAFE}, {@code Rating.QUESTIONABLE} or
     *                  {@code Rating.EXPLICIT}.
     * @param parent_id also known as Post Relationships, are a means of linking together groups of related posts.
     *                  One post (normally the "best" version) is chosen to be the parent,
     *                  while the other posts are made its children. <strong>Not required in this method.</strong>
     * @return connection with all data about request.
     * @throws BooruEngineException when something go wrong. Use <code>getCause</code> to see more details.
     *                              Note that exception can be contain one of:
     *                              <p>{@code IllegalStateException} will be thrown when user data is not defined.
     *                              <p>{@code IOException} will be thrown when something go wrong with creating post data
     *                              or sending data to server.
     *                              <p>{@code BooruEngineConnectionException} will be thrown when something go wrong with connection.
     */
    @Override
    public HttpsConnection createPost(final @NotNull File post, final @NotNull String tags, final String title,
                                      final String source, final @NotNull Rating rating, final String parent_id)
            throws BooruEngineException {
        String token;
        if (getCookieFromLoginData() == null) {
            throw new BooruEngineException(new IllegalStateException("User data not defined."));
        }
        //set necessary data
        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.GET)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .openConnection(getCustomRequest("/user/login"));
        //set cookie
        if (!loginData.containsKey("sakugabooru")) setCookie(connection);
        //set token
        if (!loginData.containsKey("authenticity_token")) setToken(connection);

        try {
            token = loginData.get("authenticity_token").replaceAll("%2B", "+");
        } catch (NullPointerException e) {
            throw new BooruEngineException(new IllegalStateException("User data not defined."));
        }
        //write all data with stream to server
        try {
            //create constructor
            MultipartConstructor constructor = new MultipartConstructor()
                    .createDataBlock("authenticity_token", token)
                    .createDataBlock("commit", "Upload")
                    .createFileBlock("post[file]", post)
                    .createDataBlock("post[source]", source)
                    .createDataBlock("post[tags]", tags)
                    .createDataBlock("post[parent_id]", parent_id)
                    //capitalise data
                    .createDataBlock("post[rating]", rating.toString().substring(0, 1) + rating.toString().toLowerCase().substring(1));
            //Create connection
            connection = new HttpsConnection()
                    .setRequestMethod(Method.POST)
                    .setUserAgent(HttpsConnection.getDefaultUserAgent())
                    .setHeader("Content-Type", "multipart/form-data; boundary=" + constructor.getBoundary())
                    .setCookies(getCookieFromLoginData())
                    .openConnection(getPostRequest());
            //send data
            constructor.send(connection.getConnection().getOutputStream());
        } catch (IOException e) {
            throw new BooruEngineException(e);
        }
        //example response
        //will be return {"post_id":409015,"location":"https://yande.re/post/show/409015","success":true}
        return connection;
    }

    /**
     * Get address for creating <code>Method.POST</code> request for creating post.
     *
     * @return the constructed request to server.
     */
    @Override
    public String getPostRequest() {
        return getCustomRequest("/post/create");
    }

    protected void setCookie(final HttpsConnection connection) {
        connection
                .getHeader("Set-Cookie")
                .stream()
                .filter(s -> s.contains("sakugabooru"))
                .forEach(s -> {
                    String[] split = s.split("=");
                    loginData.put(split[0], split[1].split("; ")[0]);
                });
    }

    protected void setToken(final HttpsConnection connection) throws BooruEngineException {
        String s = connection.getResponse();
        String data = s.split("\"csrf-token\" content=\"")[1]
                .split("\" />")[0]
                .replace("<meta content=", "")
                .replaceAll(Pattern.quote("+"), "%2B")
                .replaceAll("=", "%3D")
                .replaceAll("/", "%2F");

        loginData.put("authenticity_token", data);
    }

    @Override
    public String getAutocompleteSearchRequest(String term) {
        return getCustomRequest("/tag.json?order=count&name=" + term + "*&limit=10");
    }

    @Override
    public String[] getAutocompleteVariations(String term) throws BooruEngineException {
        String response = new HttpsConnection()
                .setRequestMethod(Method.GET)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .openConnection(getAutocompleteSearchRequest(term))
                .getResponse();

        List<HashMap<String, String>> data = new JsonParser().startParse(response).getResult();

        String[] out = new String[data.size()];
        int i = 0;
        for (HashMap<String, String> map : data) {
            out[i] = map.get("name");
            i++;
        }
        return out;
    }
}
