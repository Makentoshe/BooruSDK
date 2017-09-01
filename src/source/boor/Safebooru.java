package source.boor;

import com.sun.istack.internal.NotNull;
import engine.BooruEngineException;
import engine.MultipartConstructor;
import engine.connector.HttpsConnection;
import engine.connector.Method;
import module.interfacе.*;
import source.Post;
import source.еnum.Format;
import source.еnum.Rating;

import javax.naming.AuthenticationException;
import java.io.*;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*NOTE:
    Cookie are static
    csrf-token disable.

    Login is OK
    Uploading is OK
    Commenting is OK
    Post Voting is OK
*/

/**
 * Singleton which describe Safebooru. This class can help user to login, vote posts, create posts, comment posts, etc.
 * Default {@code format} is {@code Format.XML}. Default {@code api} is {@code API.Basic}.
 * <p>
 * Implements <code>LoginModuleInterface</code>,<code>VotingModuleInterface</code>,
 * <code>RemotePostModuleInterface</code>, <code>CommentModuleInterface</code>,
 * <code>UploadModuleInterface</code>.
 */
public class Safebooru extends AbstractBoorBasic implements LoginModuleInterface, VotingModuleInterface,
        RemotePostModuleInterface, CommentModuleInterface, UploadModuleInterface {

    private static final Safebooru instance = new Safebooru();

    private Map<String, String> loginData = new HashMap<>();

    private Safebooru() {
        super();
    }

    /**
     * Get access to Safebooru.
     *
     * @return self.
     */
    public static Safebooru get() {
        return instance;
    }

    /**
     * Get a host machine name and create custom request.
     *
     * @param request request.
     * @return the host machine address.
     */
    @Override
    public String getCustomRequest(@NotNull final String request) {
        return "https://safebooru.org" + request;
    }

    /**
     * Get request for getting comments by post id.
     *
     * @param post_id post, for which comment will be searching.
     * @param format  result format (can be {@code Format.JSON} or {@code Format.XML}).
     * @return the constructed request to server.
     */
    @Override
    public String getCommentsByPostIdRequest(int post_id, @NotNull Format format) {
        return getCustomRequest("/index.php?page=dapi&q=index&s=comment&post_id=" + post_id + (format.equals(Format.JSON) ? "&json=1" : ""));
    }

    /**
     * Remote <code>Post</code> constructor specified on posts from Safebooru.
     * Implement same as Post#defaultConstructor.
     *
     * @param attributes map of all post attributes.
     * @return the constructed <code>Post</code>.
     */
    @Override
    public Post newPostInstance(@NotNull final Map<String, String> attributes) {
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
     * Get address for sending {@code Method.POST} request for authentication to server.
     *
     * @return the constructed request to server.
     */
    @Override
    public String getAuthenticateRequest() {
        return getCustomRequest("/index.php?page=account&s=login&code=00");
    }

    /**
     * Authenticate user by login and pass.
     *
     * @param login    user login
     * @param password user pass
     * @throws BooruEngineException    will be contain <code>AuthenticationException</code>.
     * @throws AuthenticationException will be thrown when authentication was failed.
     */
    @Override
    public void logIn(@NotNull final String login, @NotNull final String password) throws BooruEngineException {
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
    public Map<String, String> getLoginData() {
        return this.loginData;
    }

    /**
     * Voting post.
     * <p>
     * If user data not defined the method will be throw <code>IllegalStateException</code>.
     * <p>
     * Use action can be "up" or "down".
     *
     * @param post_id post id.
     * @param action  any action.
     * @return true if success.
     * @throws BooruEngineException  when something go wrong. Use <code>getCause</code> to see more details.
     * @throws IllegalStateException will be thrown when the user data not defined.
     * @exception UnsupportedOperationException will be thrown when action is not supporting.
     */
    @Override
    public boolean votePost(final int post_id, @NotNull final String action) throws BooruEngineException {
        if (!action.equals("up") && !action.equals("down")) throw new BooruEngineException(new UnsupportedOperationException(action));

        //check userdata
        if (getCookieFromLoginData() == null) {
            throw new BooruEngineException(new IllegalStateException("User data not defined"));
        }

        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.GET)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .setCookies(loginData.toString().replaceAll(", ", "; "))
                .openConnection(getVotePostRequest() + "&id=" + post_id + "&type=" + action);

        return !connection.getResponse().equals("");
    }

    /**
     * Get address for creating <code>Method.POST</code> request for voting post.
     *
     * @return the constructed request to server.
     */
    @Override
    public String getVotePostRequest() {
        return getCustomRequest("/index.php?page=post&s=vote");
    }

    /**
     * Create comment for post with id: post_id.
     *
     * @param post_id    post id.
     * @param body       comment body.
     * @param postAsAnon use {@code true} for anonymously posting.
     * @param bumpPost   not support.
     * @return true if success.
     * @throws BooruEngineException  when something go wrong. Use <code>getCause</code> to see more details.
     * @throws IllegalStateException will be thrown when the user data not defined.
     */
    @Override
    public boolean commentPost(int post_id, @NotNull String body, boolean postAsAnon, boolean bumpPost) throws BooruEngineException {
        //check userdata
        if (getCookieFromLoginData() == null) {
            throw new BooruEngineException(new IllegalStateException("User data not defined"));
        }

        String cbody =
                "comment=" + body +
                        "&post_anonymous=" + (postAsAnon ? "on" : "off") +
                        "&submit=Post+comment&conf=1";

        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.POST)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .setCookies(loginData.toString().replaceAll(", ", "; "))
                .setBody(cbody)
                .openConnection(getCreateCommentRequest(post_id));

        return connection.getResponse().equals("") && connection.getResponseCode() == 302;
    }

    /**
     * Get address for creating <code>Method.POST</code> request for creating comment.
     *
     * @param id post id.
     * @return the constructed request to server.
     */
    @Override
    public String getCreateCommentRequest(final int id) {
        return getCustomRequest("/index.php?page=comment&id=" + id + "&s=save ");
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
     * Creating post.
     * The <code>title</code> and the <code>source</code> params can be null, but they will be replaced " ".
     *
     * @param post      image file.
     * @param tags      tags with " " separator.
     * @param title     post title. Not required
     * @param source    post source. Not required
     * @param rating    post rating.
     * @param parent_id parent id. Not using.
     * @return true if success (Indicates complete). Otherwise will be thrown an exception.
     * @throws BooruEngineException     when something go wrong. Use <code>getCause</code> to see more details.
     * @throws IllegalStateException    will be thrown when the user data not defined.
     * @throws IllegalArgumentException will be thrown when the required data was not included,
     *                                  not image was specified, or a required field did not exist.
     *                                  As usual when tags not defined or defined bad.
     * @throws IOException              will be thrown when something go wrong on sending post step or
     *                                  when image file corrupt.
     */
    @Override
    public boolean createPost(final @NotNull File post, final @NotNull String tags, final String title, final String source, final @NotNull Rating rating, final String parent_id) throws BooruEngineException {        //check userdata
        if (getCookieFromLoginData() == null) {
            throw new BooruEngineException(new IllegalStateException("User data not defined"));
        }

        //Create connection
        HttpsConnection connection;
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
            //define connection
            connection = new HttpsConnection()
                    .setRequestMethod(Method.POST)
                    .setUserAgent(HttpsConnection.getDefaultUserAgent())
                    .setHeader("Content-Type", "multipart/form-data; boundary=" + constructor.BOUNDARY)
                    .setCookies(getCookieFromLoginData())
                    .openConnection(getCreatePostRequest());
            //send data to stream
            constructor.send(connection.getConnection().getOutputStream());
        } catch (IOException e) {
            throw new BooruEngineException(e);
        }

        String errMessage = connection
                .getResponse()
                .split("You have mail</a></div><div id=\"content\" class=\"content\">")[1]
                .split("<br /><form method=\"post\" action=\"index")[0];

        //get result
        boolean code = connection.getResponseCode() == 200;
        boolean message = errMessage.equals("Image added.");

        if (code && message) return true;
        else {
            if (errMessage.contains("Filetype not allowed.")) {
                throw new BooruEngineException(new IOException("Filetype not allowed. The image could not be added because it already exists or it is corrupted."));
            }
            if (errMessage.contains("Generic error.")) {
                throw new BooruEngineException(new IllegalArgumentException("The required data was not included, not image was specified, or a required field did not exist."));
            } else throw new BooruEngineException(errMessage);
        }
    }

    /**
     * Get address for creating <code>Method.POST</code> request for creating post.
     *
     * @return the constructed request to server.
     */
    @Override
    public String getCreatePostRequest() {
        return getCustomRequest("/index.php?page=post&s=add");
    }
}