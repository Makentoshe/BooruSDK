package source.boor;

import com.sun.istack.internal.NotNull;
import engine.BooruEngineConnectionException;
import engine.BooruEngineException;
import engine.MultipartConstructor;
import engine.connector.HttpsConnection;
import engine.connector.Method;
import source.interfaces.*;
import source.Post;
import source.еnum.Api;
import source.еnum.Format;
import source.еnum.Rating;

import javax.naming.AuthenticationException;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/*NOTE:
    Cookie are static and not update, when page is upload.
    csrf-token are static and not update.

    Login is OK
    Commenting is OK
    Post Voting is OK
    Posting is OK
*/

/**
 * Singleton which describe Gelbooru. This class can help user to login, vote posts, create posts, comment posts, etc.
 * Default {@code format} is {@code Format.XML}. Default {@code api} is {@code API.Basic}.
 * <p>
 * Implements <code>Login</code>,<code>PostVoting</code>,
 * <code>PostCreator</code>, <code>CommentUploader</code>,
 * <code>PostUploader</code>.
 */
public class Gelbooru extends AbstractBoor
        implements Login, PostVoting, PostCreator, CommentUploader, PostUploader, Autocomplete {

    private static final Gelbooru mInstance = new Gelbooru();

    private final HashMap<String, String> loginData = new HashMap<>();

    /**
     * Get access to Gelbooru.
     *
     * @return self.
     */
    public static Gelbooru get() {
        return mInstance;
    }

    private Gelbooru() {
        format = Format.XML;
        api = Api.BASICS;
    }

    /**
     * Get a host machine name and create custom request.
     *
     * @param request request.
     * @return the host machine address.
     */
    @Override
    public String getCustomRequest(String request) {
        return "https://gelbooru.com" + request;
    }

    /**
     * Remote <code>Post</code> constructor specified on posts from Gelbooru.
     * Implement same as Post#defaultConstructor.
     * <p>
     *
     * @param attributes map of all post attributes.
     * @return the constructed <code>Post</code>.
     */
    @Override
    public Post newPostInstance(final Map<String, String> attributes) {
        Post post = new Post(mInstance);
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
                    post.setPreview_url("https:" + pair.getValue());
                    break;
                }
                case "tags": {
                    post.setTags(pair.getValue());
                    break;
                }
                case "sample_url": {
                    post.setSample_url("https:" + pair.getValue());
                    break;
                }
                case "file_url": {
                    post.setFile_url("https:" + pair.getValue());
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
            post.setComments_url(mInstance.getCommentsByPostIdRequest(post.getId()));
        }
        return post;
    }

    /**
     * Get request for getting comments by post id.
     *
     * @param post_id post, for which comment will be searching.
     * @param format  result format (can be {@code Format.JSON} or {@code Format.XML}).
     * @return the constructed request to server.
     */
    @Override
    public String getCommentsByPostIdRequest(final int post_id, final Format format) {
        return getCustomRequest("/index.php?page=dapi&q=index&s=comment&post_id=" + post_id + (format.equals(Format.JSON) ? "&json=1" : ""));
    }

    /**
     * Get link for post with user friendly interface (as is).
     *
     * @param post_id post, for which the link will be created.
     * @return the link to post with standard user interface.
     */
    @Override
    public String getPostLinkById(int post_id) {
        return getCustomRequest("/index.php?page=post&s=view&id=" + post_id);
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
        //create post body
        String postData = "user=" + login + "&pass=" + password + "&submit=Log+in";
        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.POST)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .setBody(postData)
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
        return getCustomRequest("/index.php?page=account&s=login&code=00");
    }

    /**
     * Get address for getting a list of posts.
     *
     * @param limit  how many posts must be in page.
     * @param tags   the tags to search for.
     * @param page   page index(from zero).
     * @param format format result (can be {@code Format.JSON} or {@code Format.XML}).
     * @return constructed request for getting Post array.
     */
    @Override
    public String getPostsByTagsRequest(int limit, String tags, int page, Format format) {
        return getCustomRequest("/index.php?page=dapi&q=index&s=post&limit=" + limit +
                "&tags=" + tags + "&pid=" + page) + (format.equals(Format.JSON) ? "&json=1" : "");
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
        return getCustomRequest("/index.php?page=dapi&q=index&s=post&id=" + String.valueOf(id) + (format.equals(Format.JSON) ? "json=1" : ""));
    }

    /**
     * Method for voting post with id <code>post_id</code>. Use action can be only "up" for vote up post.
     * If action will be have another value, the <code>IllegalArgumentException</code> will be thrown.
     * <p>
     * Method creating connection and send POST-request.
     *
     * @param post_id post id.
     * @param action  any action.
     * @return connection with post-request response.
     * @throws BooruEngineException when something go wrong. Use <code>getCause</code> to see more details.
     *                              Note that exception can be contain one of:
     *                              <p>{@code IllegalStateException} will be thrown when the user data is not defined.
     *                              <p>{@code BooruEngineConnectionException} will be thrown when something go wrong with connection.
     *                              <p>{@code IllegalArgumentException} will be thrown when {@param action} not contain expected value.
     */
    @Override
    public HttpsConnection votePost(final int post_id, final String action) throws BooruEngineException {
        if (!action.equals("up")) {
            throw new BooruEngineException("Action can be only \"up\".", new IllegalArgumentException(action));
        }
        //check userdata
        if (getCookieFromLoginData() == null) {
            throw new BooruEngineException(new IllegalStateException("User data not defined"));
        }
        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.GET)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .setCookies(getCookieFromLoginData())
                .openConnection(getVotePostRequest(post_id) + "&id=" + post_id + "&type=" + action);

        return connection;
    }

    /**
     * Get address for creating <code>Method.POST</code> request for voting post.
     *
     * @return the constructed request to server.
     */
    @Override
    public String getVotePostRequest(int post_id) {
        return getCustomRequest("/index.php?page=post&s=vote");
    }

    /**
     * Create comment for post with id <code>post_id</code>. Params <code>postAsAnon</code> and
     * <code>bumpPost</code> is useless because they are not supporting.
     * <p>
     * Method creating connection and send POST-request.
     *
     * @param post_id    post id, for which comment will be created.
     * @param body       comment body.
     * @param postAsAnon using for anonymously posting. Useless.
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
                .setHeader("Connection", "keep-alive")
                .openConnection("https://gelbooru.com/index.php?page=post&s=view&id=" + post_id);
        //get token
        String token = connection.getResponse()
                .split("\"/>\t\t<input type=\"hidden\" name=\"csrf-token\" value=\"")[1]
                .split("\"/>\t\t</td>")[0];
        //get PHPSESSID
        for (int i = 0; i < connection.getHeader("Set-Cookie").size(); i++) {
            String[] data = connection.getHeader("Set-Cookie").get(i).split("; ")[0].split("=");
            if (data.length == 2) this.loginData.put(data[0], data[1]);
        }

        String cbody =
                "comment=" + body.replaceAll(" ", "+") +
                        "&post_anonymous=" + (postAsAnon ? "on" : "off") +
                        "&submit=Post+comment&conf=1" +
                        "&csrf-token=" + token
                        .replaceAll(Pattern.quote("+"), "%2B")
                        .replaceAll("/", "%2F")
                        .replaceAll("=", "%3D");

        connection = new HttpsConnection()
                .setRequestMethod(Method.POST)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .setBody(cbody)
                .setCookies(getCookieFromLoginData())
                .openConnection(getCommentRequest(post_id));

        return connection;
    }

    /**
     * Get address for creating <code>Method.POST</code> request for creating comment.
     *
     * @param id post id.
     * @return the constructed request to server.
     */
    @Override
    public String getCommentRequest(final int id) {
        return getCustomRequest("/index.php?page=comment&id=" + id + "&s=save");
    }

    /**
     * Remake user data current format to <code>String</code> which will be contain user cookie.
     *
     * @return the user cookie.
     */
    @Override
    public String getCookieFromLoginData() {
        if (getLoginData().size() == 0) return null;
        return getLoginData().toString().replaceAll(", ", "; ").replaceAll("\\{", "").replaceAll("\\}", "");
    }

    /**
     * Create post on Gelbooru.
     *
     * @param post      file which will be upload. It must be image of gif-animation.
     *                  Also it can be video file with .webm extension.
     * @param tags      tags are describe file content. They separates by spaces,
     *                  so, spaces in title must be replace by underscores.
     * @param title     post title. <strong>Not required in this method.</strong>
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
     *                              <p>{@code IOException} will be thrown when something go wrong with creating
     *                              post data of sending data to server.
     *                              <p>{@code BooruEngineConnectionException} will be thrown when something go wrong
     *                              with connection.
     */
    @Override
    public HttpsConnection createPost(final @NotNull File post, final @NotNull String tags, final String title, final String source, final @NotNull Rating rating, final String parent_id) throws BooruEngineException {
        //check userdata
        if (getCookieFromLoginData() == null) {
            throw new BooruEngineException(new IllegalStateException("User data not defined"));
        }
        //get PHPSESSID
        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.GET)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .setCookies(getCookieFromLoginData())
                .openConnection("https://gelbooru.com/index.php?page=post&s=view&id=1");
        getLoginData().put("PHPSESSID", connection.getHeader("Set-Cookie").get(0).split("=")[1].split("; ")[0]);
        //and write all data with stream to server
        try {
            //create constructor
            MultipartConstructor constructor = new MultipartConstructor()
                    .createFileBlock("upload", post)
                    .createDataBlock("source", source)
                    .createDataBlock("title", title)
                    .createDataBlock("tags", tags)
                    .createDataBlock("rating", "" + rating.toString().toLowerCase().charAt(0))
                    .createDataBlock("submit", "Upload");
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
        return connection;
    }

    /**
     * Get address for creating <code>Method.POST</code> request for creating post.
     *
     * @return the constructed request to server.
     */
    @Override
    public String getPostRequest() {
        return getCustomRequest("/index.php?page=post&s=add");
    }

    //    @Override
//    public String getTagListRequest(int count, int page) {
//        return getCustomRequest("/index.php?page=dapi&s=tag&q=index&limit=" + count + "&page=" + page);
//    }

    @Override
    public String getAutocompleteSearchRequest(String term) {
        return getCustomRequest("/index.php?page=autocomplete&term=" + term);
    }

    @Override
    public String[] getAutocompleteVariations(String term) throws BooruEngineConnectionException {
        String response = new HttpsConnection()
                .setRequestMethod(Method.GET)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .openConnection(getAutocompleteSearchRequest(term))
                .getResponse();

        String[] split = response.split("\"");
        String[] out = new String[(split.length - 1) / 2];

        for (int i = 1, j = 0; i <= split.length - 1; i+=2, j++) {
            out[j] = split[i].replace("\\/", "/");
        }
        return out;
    }
}