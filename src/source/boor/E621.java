package source.boor;

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


/*NOTE:
    Cookie are static and not update, when page is upload.
    csrf-token are static and not update.

    Login is OK
    Commenting is ...
    Post Voting is OK
*/

/**
 * Singleton which describe E621. This class can help user to login, vote posts, create posts, comment posts, etc.
 * Default {@code format} is {@code Format.JSON}. Default {@code api} is {@code API.Advanced}.
 * <p>
 * Implements <code>Login</code>,<code>PostVoting</code>,
 * <code>PostCreator</code>, <code>CommentUploader</code>,
 * <code>PostUploader</code>.
 */
public class E621 extends AbstractBoor
        implements Login, PostCreator, PostVoting, CommentUploader, PostUploader, Autocomplete {

    private static final E621 instance = new E621();

    private final HashMap<String, String> loginData = new HashMap<>();

    /**
     * Get access to E621.
     *
     * @return self.
     */
    public static E621 get() {
        return instance;
    }

    protected E621() {
        format = Format.JSON;
        api = Api.ADVANCED;
    }

    /**
     * Get a host machine name and create custom request.
     *
     * @param request request.
     * @return the host machine address.
     */
    @Override
    public String getCustomRequest(String request) {
        return "https://e621.net" + request;
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
        return getCustomRequest("/post/show." + format.toString().toLowerCase() + "?id=" + id);
    }

    @Override
    public String getPostsByTagsRequest(int limit, String tags, int page, Format format) {
        return getCustomRequest("/post/index." + format.toString().toLowerCase() +
                "?tags=" + tags + "&limit=" + limit + "&page=" + page);
    }

    /**
     * Get request for getting comments by post id.
     *
     * @param post_id post, for which comment will be searching.
     * @param format  result format (can be {@code Format.JSON} or {@code Format.XML}).
     * @return the constructed request to server.
     */
    @Override
    public String getCommentsByPostIdRequest(int post_id, Format format) {
        return getCustomRequest("/comment/search." + format.toString().toLowerCase() + "?post_id=" + post_id);
    }

    @Override
    public String getPostLinkById(int post_id) {
        return getCustomRequest("/post/show/" + post_id);
    }

    /**
     * Remote <code>Post</code> constructor specified on posts from E621.
     * Implement same as Post#defaultConstructor.
     * <p>
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
                case "has_comments": {
                    if ("true".equals(pair.getValue())) {
                        post.setHas_comments(true);
                    } else {
                        post.setHas_comments(false);
                    }
                    break;
                }
                case "created_at": {
                    post.setCreate_Time(pair.getValue());
                    break;
                }
                case "height": {
                    post.setHeight(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "width": {
                    post.setWidth(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "sample_height": {
                    post.setSample_height(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "sample_width": {
                    post.setSample_width(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "preview_height": {
                    post.setPreview_height(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "preview_width": {
                    post.setPreview_width(Integer.parseInt(pair.getValue()));
                    break;
                }
            }
        }
        //after all check comments flag
        if (post.isHas_comments()) {
            //and if true - setup comments url.
            post.setComments_url(instance.getCommentsByPostIdRequest(post.getId()));
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
        if (!loginData.containsKey("e621") || !loginData.containsKey("authenticity_token")) {
            //get connection
            HttpsConnection connection = new HttpsConnection()
                    .setRequestMethod(Method.GET)
                    .setUserAgent(HttpsConnection.getDefaultUserAgent())
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
            throw new BooruEngineException("Can't find \"e621\" cookie.", new IllegalStateException());
        }
        //if already have not token - throw an exception
        if (!loginData.containsKey("authenticity_token")) {
            throw new BooruEngineException("Can't find \"authenticity_token\".", new IllegalStateException());
        }

        //create new connection for login
        String postData = "authenticity_token=" + loginData.get("authenticity_token") + "&user%5Bname%5D=" + login +
                "&user%5Bpassword%5D=" + password + "&user%5Broaming%5D=0&user%5Broaming%5D=1";
        String cookie = "e621=" + loginData.get("e621");

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
        return this.loginData;
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
     * Method for voting post with id <code>post_id</code>. Scores can be: "1" for up vote and "-1" for down vote.
     * If action will be another string the <code>IllegalArgumentException</code> will be thrown.
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
    public HttpsConnection votePost(final int post_id, final String score) throws BooruEngineException {
        //check userdata
        if (getCookieFromLoginData() == null) {
            throw new BooruEngineException(new IllegalStateException("User data not defined."));
        }
        //validate action
        if (!score.equals("-1") && !score.equals("1")) {
            throw new BooruEngineException("Score can be only \"1\" ot \"-1\".", new IllegalArgumentException(score));
        }

        String body = "id=" + post_id + "&score=" + score + "&_=";
        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.POST)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .setCookies(loginData.toString().replaceAll(", ", "; "))
                .setBody(body)
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
     * Remake user data current format to <code>String</code> which will be contain user cookie.
     *
     * @return the user cookie.
     */
    @Override
    public String getCookieFromLoginData() {
        if (getLoginData().size() == 0) return null;
        return getLoginData().toString()
                .replaceAll(", ", "; ")
                .replaceAll("\\{", "")
                .replaceAll("\\}", "");
    }

    /**
     * Create comment for post with id <code>post_id</code>. Params <code>postAsAnon</code> and
     * <code>bumpPost</code> is useless because they are not supporting.
     *
     * @param post_id    post id.
     * @param body       comment body.
     * @param postAsAnon for anonymously posting.
     * @param bumpPost   for bump up post.
     * @return connection with post-request response.
     * @throws BooruEngineException when something go wrong. Use <code>getCause</code> to see more details.
     *                              Note that exception can be contain one of:
     *                              <p>{@code IllegalStateException} will be thrown when the user data is not defined.
     *                              <p>{@code BooruEngineConnectionException} will be thrown when something go wrong with connection.
     */
    @Override
    public HttpsConnection createCommentToPost(int post_id, String body, boolean postAsAnon, boolean bumpPost) throws BooruEngineException {
        StringBuilder cbody = new StringBuilder();
        //check userdata
        if (getCookieFromLoginData() == null) {
            throw new BooruEngineException(new IllegalStateException("User data not defined"));
        }
        //create post body
        cbody.append("authenticity_token=").append(loginData.get("authenticity_token"))
                .append("&comment%5Bpost_id%5D=").append(post_id)
                .append("&origin_controller=post&origin_action=show")
                .append("&comment%5Bbody%5D=").append(body);

        //send body to server
        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.POST)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .setCookies(getCookieFromLoginData())
                .setBody(cbody.toString())
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
     * Create post on E621.
     *
     * @param post        file which will be upload. It must be image of gif-animation.
     *                    Also it can be video file with .webm extension.
     * @param tags        tags are describe file content. They separates by spaces,
     *                    so, spaces in title must be replace by underscores.
     * @param description post description.
     * @param source      source from file was get. It must be URL like "https://sas.com/test.jpg" or something else.
     *                    <strong>Not required in this method.</strong>
     * @param rating      post rating. As usual it can be {@code Rating.SAFE}, {@code Rating.QUESTIONABLE} or
     *                    {@code Rating.EXPLICIT}.
     * @param parent_id   also known as Post Relationships, are a means of linking together groups of related posts.
     *                    One post (normally the "best" version) is chosen to be the parent,
     *                    while the other posts are made its children. <strong>Not required in this method.</strong>
     * @return connection with all data about request.
     * @throws BooruEngineException when something go wrong. Use <code>getCause</code> to see more details.
     *                              Note that exception can be contain one of:
     *                              <p>{@code IllegalStateException} will be thrown when user data is not defined.
     *                              <p>{@code IOException} will be thrown when something go wrong with creating post data of sending data to server.
     *                              <p>{@code BooruEngineConnectionException} will be thrown when something go wrong with connection.
     */
    @Override
    public HttpsConnection createPost(File post, String tags, String description, String source, Rating rating, String parent_id) throws BooruEngineException {
        HttpsConnection connection;
        String token;
        //check userdata
        if (getCookieFromLoginData() == null) {
            throw new BooruEngineException(new IllegalStateException("User data not defined."));
        }
        //get token
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
                    .createFileBlock("post[file]", post)
                    .createDataBlock("post[upload_url]", "")
                    .createDataBlock("post[tags]", tags)
                    .createDataBlock("post[source]", source)
                    .createDataBlock("post[description]", description)
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
        //get response
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
                .filter(s -> s.contains("e621"))
                .forEach(s -> {
                    String[] split = s.split("=");
                    loginData.put(split[0], split[1].split("; ")[0]);
                });
    }

    protected void setToken(final HttpsConnection connection) throws BooruEngineException {
        String s = connection.getResponse();
        String data = s.split("<input name=\"authenticity_token\" type=\"hidden\" value=\"")[1]
                .split("\"></div>")[0]
                .replaceAll(Pattern.quote("+"), "%2B");
        loginData.put("authenticity_token", data);
    }

    @Override
    public String getAutocompleteSearchRequest(String term) {
        return getCustomRequest("/tag/index.json?order=count&limit=10&name=" + term + "*");
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
        for (HashMap<String, String> map: data) {
            out[i] =  map.get("name");
            i++;
        }
        return out;
    }
}
