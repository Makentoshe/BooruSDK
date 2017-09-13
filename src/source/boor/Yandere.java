package source.boor;

import com.sun.istack.internal.NotNull;
import engine.BooruEngineException;
import engine.MultipartConstructor;
import engine.connector.HttpsConnection;
import engine.connector.Method;
import module.*;
import module.interfacе.*;
import source.Post;
import source.еnum.Format;
import source.еnum.Rating;

import javax.naming.AuthenticationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/*NOTE:
    Not supported "has_comments" and comment searching.

    Cookie are not static
    csrf-token are not static

    Login is OK
    Commenting is OK
    Post Voting is OK
*/
/**
 * Singleton which describe Yandere. This class can help user to login, vote posts, create posts, comment posts, etc.
 * Default {@code format} is {@code Format.XML}. Default {@code api} is {@code API.Basic}.
 * <p>
 * Implements <code>LoginModule</code>,<code>VotingModule</code>,
 * <code>RemotePostModule</code>, <code>CommentModule</code>,
 * <code>UploadModule</code>.
 */
public class Yandere extends AbstractBoorAdvanced implements LoginModule, RemotePostModule,
        VotingModule, CommentModule, UploadModule {

    private static final Yandere instance = new Yandere();

    private final HashMap<String, String> loginData = new HashMap<>();

    /**
     * Get access to Yandere.
     *
     * @return self.
     */
    public static Yandere get() {
        return instance;
    }

    private Yandere() {
        super();
    }

    /**
     * Get a host machine name and create custom request.
     *
     * @param request request.
     * @return the host machine address.
     */
    @Override
    public String getCustomRequest(String request) {
        return "https://yande.re" + request;
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
    public String getPackByTagsRequest(int limit, String tags, int page, Format format) {
        return getCustomRequest("/post." + format.toString().toLowerCase() +
                "?tags=" + tags + "&limit=" + limit + "&page=" + page);
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

    /**
     * Remote <code>Post</code> constructor specified on posts from Yandere.
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
            }
        }
        return post;
    }

    /**
     * Authenticate user by login and pass.
     *
     * @param login    user login
     * @param password user pass
     * @throws BooruEngineException    will be contain <code>AuthenticationException</code>.
     * @throws AuthenticationException will be thrown when authentication was failed.
     * @throws IllegalStateException   will be thrown when something go wrong
     *                                 with getting cookie and token before login request.
     */
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
            throw new BooruEngineException("Can't find \"yande.re\" cookie in login data.", new IllegalStateException());
        }
        //if already have not token - throw an exception
        if (!loginData.containsKey("authenticity_token")) {
            throw new BooruEngineException("Can't find \"authenticity_token\" in login data.", new IllegalStateException());
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

    protected void setCookie(final HttpsConnection connection) {
        connection
                .getHeader("Set-Cookie")
                .stream()
                .filter(s -> s.contains("yande.re"))
                .forEach(s -> {
                    String[] split = s.split("=");
                    loginData.put(split[0], split[1].split("; ")[0]);
                });
    }

    protected String setToken(final HttpsConnection connection) throws BooruEngineException {
        String s = connection.getResponse();
        String data = s.split("\"csrf-token\" content=\"")[1]
                .split("\" />")[0]
                .replace("<meta content=", "")
                .replaceAll(Pattern.quote("+"), "%2B");

        loginData.put("authenticity_token", data);
        return data;
    }

    /**
     * Voting post.
     * <p>
     * Scores can be:
     * <p>
     * 0 - remove vote.
     * <p>
     * 1 or 2 - vote.
     * <p>
     * 3 - vote and add to favorites.
     * <p>
     *
     * @param post_id post id.
     * @param score   scores to post.
     * @return true if success.
     * @throws BooruEngineException          when something go wrong. Use <code>getCause</code> to see more details.
     * @throws IllegalStateException         will be thrown when the user data not defined.
     * @throws UnsupportedOperationException will be thrown when action is not supporting.
     * @throws IllegalArgumentException      will be thrown when score param not contain expected value.
     */
    @Override
    public boolean votePost(int post_id, String score) throws BooruEngineException {
        String token;
        HttpsConnection connection;

        //check userdata
        if (getCookieFromLoginData() == null) {
            throw new BooruEngineException(new IllegalStateException("User data not defined."));
        }

        try {
            //validate action
            int s = Integer.parseInt(score);
            if (s > 3 || s < 0) {
                throw new BooruEngineException("Score can't be more then the 3 and less than the 0",
                        new IllegalArgumentException(score)
                );
            }

            //set necessary data
            connection = new HttpsConnection()
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

            try {
                token = loginData.get("authenticity_token").replaceAll("%2B", "+");
            } catch (NullPointerException e) {
                throw new BooruEngineException(new IllegalStateException("User data not defined."));
            }

            //create connection
            connection = new HttpsConnection()
                    .setRequestMethod(Method.POST)
                    .setUserAgent(HttpsConnection.getDefaultUserAgent())
                    .setCookies(getCookieFromLoginData())
                    .setHeader("X-CSRF-Token", token)
                    .setBody("id=" + post_id + "&score=" + score)
                    .openConnection(getCustomRequest("/post/vote.json"));

            token = connection.getResponse();

        } catch (NumberFormatException e) {
            throw new BooruEngineException(new IllegalArgumentException(score));
        } catch (NullPointerException e) {
            throw new BooruEngineException("User data not defined.", new IllegalStateException());
        } catch (BooruEngineException e) {
            throw new BooruEngineException(e.getCause().getMessage());
        }

        return token.split("\"success\":")[1].contains("true");
    }

    /**
     * Get address for creating <code>Method.POST</code> request for voting post.
     *
     * @return the constructed request to server.
     */
    @Override
    public String getVotePostRequest() {
        return getCustomRequest("/post/vote.json");
    }

    /**
     * Create comment for post with id: post_id.
     * <p>
     * Note: Be careful: Not all *boors support "postAsAnon" or "bumpPost" param.
     *
     * @param post_id    post id.
     * @param body       comment body.
     * @param postAsAnon use {@code true} for anonymously posting.
     * @param bumpPost   use {@code true} for bump up post.
     * @return true if success.
     * @throws BooruEngineException  when something go wrong.
     *                               Use <code>getCause</code> to see more details.
     * @throws IllegalStateException will be thrown when the user data not defined.
     * @throws NoSuchElementException will be thrown when the Set-Cookie header was not got.
     * @throws RuntimeException will be thrown when the Set-Cookie header was got, but comment was not created
     */
    @Override
    public boolean commentPost(int post_id, String body, boolean postAsAnon, boolean bumpPost) throws BooruEngineException {
        //check userdata
        if (getCookieFromLoginData() == null) {
            throw new BooruEngineException(new IllegalStateException("User data not defined"));
        }

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
                .append("&comment%5Bpost_id%5D=").append(post_id)
                .append("&comment%5Bbody%5D=").append(body.replaceAll(" ", "+"))
                .append("&commit=Post");

        //send body to server
        connection = new HttpsConnection()
                .setRequestMethod(Method.POST)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .setCookies(loginData.toString().replaceAll(", ", "; "))
                .setBody(cbody.toString())
                .openConnection(getCreateCommentRequest(post_id));

        //try to get Set-Cookie header
        //if failed(NPE) - catch and throw BEE
        try {
            for (int i = 0; i < connection.getHeader("Set-Cookie").size(); i++) {
                String[] data = connection.getHeader("Set-Cookie").get(i).split("; ")[0].split("=");
                //if cookie notice=Comment+created - OK
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

    /**
     * Get address for creating <code>Method.POST</code> request for creating post.
     *
     * @return the constructed request to server.
     */
    @Override
    public String getCreateCommentRequest(int id) {
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

    /**
     * Creating post.
     * The <code>title</code> and the <code>source</code> params can be null, but they will be replaced "".
     *
     * @param post      image file.
     * @param tags      tags with " " separator.
     * @param title     post title. Not using.
     * @param source    post source. Not required.
     * @param rating    post rating.
     * @param parent_id parent id.
     * @return response of post request.
     * @throws BooruEngineException  when something go wrong. Use <code>getCause</code> to see more details.
     * @throws IllegalStateException will be thrown when the user data not defined.
     * @throws IOException           will be thrown when something go wrong on sending post step or
     *                               when image file corrupt.
     */
    @Override
    public String createPost(final @NotNull File post, final @NotNull String tags, final String title,
                             final String source, final @NotNull Rating rating, final String parent_id)
            throws BooruEngineException {
        //check userdata
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
        if (!loginData.containsKey("yande.re")) {
            setCookie(connection);
        }
        //set token
        if (!loginData.containsKey("authenticity_token")) {
            setToken(connection);
        }

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
                    .openConnection(getCreatePostRequest());

            //send data
            constructor.send(connection.getConnection().getOutputStream());
        } catch (IOException e) {
            throw new BooruEngineException(e);
        }
        //get response
        //will be return {"post_id":409015,"location":"https://yande.re/post/show/409015","success":true}

        return connection.getResponse();

//        try {
//            JsonParser parser = new JsonParser();
//            parser.startParse(connection.getResponse());
//            List<HashMap<String, String>> jsonResult = parser.getResult();
//            //if success - true
//            if (jsonResult.get(0).get("success").equals("true")) return true;
//                //else throw exception with reason
//            else {
//                throw new BooruEngineException(jsonResult.get(0).get("reason"), new IOException());
//            }
//            //when something go wrong - catch any exception and throw in BEE
//        } catch (Exception e) {
//            throw new BooruEngineException(connection.getResponseMessage(), e);
//        }
    }

    /**
     * Get address for creating <code>Method.POST</code> request for creating post.
     *
     * @return the constructed request to server.
     */
    @Override
    public String getCreatePostRequest() {
        return getCustomRequest("/post/create.json");
    }
}
