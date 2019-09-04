package source.boor;

import engine.BooruEngineConnectionException;
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
import java.util.*;
import java.util.regex.Pattern;

//token not static
//cookies are static

/**
 * Singleton which describe Danbooru. This class can help user to login, vote posts, create posts, comment posts, etc.
 * Default {@code format} is {@code Format.JSON}. Default {@code api} is {@code API.Advanced}.
 * <p>
 * Implements <code>Login</code>,<code>PostVoting</code>,
 * <code>PostCreator</code>, <code>CommentUploader</code>,
 * <code>PostUploader</code>.
 */
public class Danbooru extends AbstractBoor
        implements PostCreator, Login, CommentUploader, PostUploader, Autocomplete {

    private final HashMap<String, String> loginData = new HashMap<>();
    private static final Danbooru instance = new Danbooru();

    /**
     * Get access to Danbooru.
     *
     * @return self.
     */
    public static Danbooru get() {
        return instance;
    }

    private Danbooru() {
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
        return "https://danbooru.donmai.us" + request;
    }

    @Override
    public String getPostByIdRequest(int id, Format format) {
        return getCustomRequest("/posts/" + String.valueOf(id) + "." + format.toString().toLowerCase());
    }

    /**
     * Get address for getting some posts by tags.
     *
     * @param limit  how many items must be in page.
     * @param tags   the tags to search for.
     * @param page   page index(from zero).
     * @param format format result.
     * @return the constructed request to server.
     */
    @Override
    public String getPostsByTagsRequest(int limit, String tags, int page, Format format) {
        return getCustomRequest("/posts." + format.toString().toLowerCase() +
                "?tags=" + tags + "&limit=" + limit + "&page=" + page);
    }

    /**
     * Get address for getting comments by post id.
     *
     * @param post_id post, for which comment will be searching.
     * @param format  result format (can be {@code Format.JSON} or {@code Format.XML}).
     * @return the constructed request to server.
     */
    @Override
    public String getCommentsByPostIdRequest(int post_id, Format format) {
        return getCustomRequest("/comments." + format.toString().toLowerCase() +
                "?group_by=comment&search[post_id]=" + post_id);
    }

    @Override
    public String getPostLinkById(int post_id) {
        return getCustomRequest("/posts/" + post_id);
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
                case "preview_file_url": {
                    post.setPreview_url("https://danbooru.donmai.us" + pair.getValue());
                    break;
                }
                case "tag_string": {
                    post.setTags(pair.getValue());
                    break;
                }
                case "file_url": {
                    post.setSample_url("https://danbooru.donmai.us" + pair.getValue());
                    break;
                }
                case "large_file_url": {
                    post.setFile_url("https://danbooru.donmai.us" + pair.getValue());
                    break;
                }
                case "source": {
                    post.setSource(pair.getValue());
                    break;
                }
                case "uploader_id": {
                    post.setCreator_id(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "last_commented_at": {
                    if (!"null".equals(pair.getValue())) {
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
                case "image-width":{
                    post.setPreview_width(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "image-height":{
                    post.setPreview_height(Integer.parseInt(pair.getValue()));
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
        if (!loginData.containsKey("_danbooru_session") || !loginData.containsKey("authenticity_token")) {
            //get connection
            HttpsConnection connection = new HttpsConnection()
                    .setRequestMethod(Method.GET)
                    .setUserAgent(HttpsConnection.getDefaultUserAgent())
                    .openConnection(getCustomRequest(""));
            //set cookie
            if (!loginData.containsKey("_danbooru_session")) setCookie(connection);
            //set token
            if (!loginData.containsKey("authenticity_token")) setToken(connection);
        }

        //if already have not cookie - throw an exception
        if (!loginData.containsKey("_danbooru_session")) {
            throw new BooruEngineException("Can't find \"_danbooru_session\" cookie.", new IllegalStateException());
        }
        //if already have not token - throw an exception
        if (!loginData.containsKey("authenticity_token")) {
            throw new BooruEngineException("Can't find \"authenticity_token\".", new IllegalStateException());
        }

        //create new connection for login
        String postData = "utf8=%E2%9C%93&authenticity_token=" + loginData.get("authenticity_token") + "&url=&name=" + login +
                "&&password=" + password + "&remember=1&commit=Submit";
        String cookie = "_danbooru_session=" + loginData.get("_danbooru_session");

        //create connection
        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.POST)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .setBody(postData)
                .setCookies(cookie)
                .openConnection(getAuthenticateRequest());
        //process response
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
        return getCustomRequest("/session");
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
     * Create comment for post with id <code>post_id</code>.
     * Params <code>postAsAnon</code> is useless because is not supporting.
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
        //check user data
        String token = checkUserData();
        //create post body
        String cbody = "utf8=%E2%9C%93&authenticity_token=" + token +
                "&comment%5Bpost_id%5D=" + post_id +
                "&comment%5Bbody%5D=" + body +
                "&commit=Submit" + (bumpPost ? "&comment%5Bdo_not_bump_post%5D=0" : "&comment%5Bdo_not_bump_post%5D=1");
        //send post
        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.POST)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .setCookies(getCookieFromLoginData())
                .setBody(cbody)
                .openConnection(getCommentRequest(post_id));
        //remove used token
        loginData.remove("authenticity_token");

        return connection;
    }

    /**
     * Get address for creating <code>Method.POST</code> request for creating comment.
     *
     * @param id post id, for which comment will be created.
     * @return the constructed request to server.
     */
    @Override
    public String getCommentRequest(int id) {
        return getCustomRequest("/comments");
    }

    /**
     * Create upload on Danbooru.
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
    public HttpsConnection createPost(File post, String tags, String title, String source, Rating rating, String parent_id) throws BooruEngineException {
        //check userdata
        String token = checkUserData();

        //write all data with stream to server
        HttpsConnection connection;
        try {
            //create constructor
            MultipartConstructor constructor = new MultipartConstructor()
                    .createDataBlock("utf8", "вњ“")
                    .createDataBlock("authenticity_token", token)
                    .createDataBlock("url", "") //what is it?
                    .createDataBlock("ref", "") //what is it?
                    .createDataBlock("normalized_url", "")//what is it?
                    .createDataBlock("upload[referer_url]", "")//what is it? mb get data from url
                    .createFileBlock("upload[file]", post)
                    .createDataBlock("upload[source]", source)
                    .createDataBlock("upload[rating]", rating.toString().toLowerCase().substring(0, 1))
                    .createDataBlock("upload[parent_id]", parent_id)
                    .createDataBlock("upload[artist_commentary_title]", "")
                    .createDataBlock("upload[artist_commentary_desc]", "")
                    .createDataBlock("upload[include_artist_commentary]", "0")
                    .createDataBlock("upload[tag_string]", tags);

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
        //remove used token
        loginData.remove("authenticity_token");
        return connection;
    }

    /**
     * Get address for creating <code>Method.POST</code> request for creating post.
     *
     * @return the constructed request to server.
     */
    @Override
    public String getPostRequest() {
        return getCustomRequest("/uploads.json");
    }

    protected void setCookie(final HttpsConnection connection) {
        connection
                .getHeader("Set-Cookie")
                .stream()
                .filter(s -> s.contains("_danbooru_session"))
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

    //return token
    protected String checkUserData() throws BooruEngineException {
        //check cookie
        if (getCookieFromLoginData() == null) {
            throw new BooruEngineException(new IllegalStateException("User data is not defined."));
        }
        //check authenticity token
        try {
            return loginData.get("authenticity_token").replaceAll("%2B", "+");
        } catch (NullPointerException npe1) {
            setToken(new HttpsConnection()
                    .setRequestMethod(Method.GET)
                    .setUserAgent(HttpsConnection.getDefaultUserAgent())
                    .openConnection(getCustomRequest("")));
            try {
                return loginData.get("authenticity_token").replaceAll("%2B", "+");
            } catch (NullPointerException npe2) {
                throw new BooruEngineException("\"authenticity_token\" not defined.");
            }
        }
    }

    @Override
    public String getAutocompleteSearchRequest(String term) {
        return getCustomRequest("/tags/autocomplete.json?search%5Bname_matches%5D=" + term);
    }

    @Override
    public String[] getAutocompleteVariations(String term) throws BooruEngineConnectionException {
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
