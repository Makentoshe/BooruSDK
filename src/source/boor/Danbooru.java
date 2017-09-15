package source.boor;

import engine.BooruEngineException;
import engine.connector.HttpsConnection;
import engine.connector.Method;
import module.LoginModule;
import module.RemotePostModule;
import module.VotingModule;
import source.Post;
import source.еnum.Format;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;


//token not static
//cookies are static

/**
 * Singleton.
 * Storage data about Danbooru API and method for getting request
 */
public class Danbooru extends AbstractBoorAdvanced implements RemotePostModule, LoginModule {

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
        return "https://danbooru.donmai.us" + request;
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
        return getCustomRequest("/posts." + format.toString().toLowerCase() +
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
        return getCustomRequest("/comments." + format.toString().toLowerCase() +
                "?group_by=comment&search[post_id]=" + post_id);
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

            }
        }
        //after all check comments flag
        if (post.isHas_comments()) {
            //and if true - setup comments url.
            post.setComments_url(instance.getCommentsByPostIdRequest(post.getId()));
        }
        return post;
    }


    @Override
    public void logIn(String login, String password) throws BooruEngineException {
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
            throw new BooruEngineException("Can't find \"_danbooru_session\" cookie in login data.", new IllegalStateException());
        }
        //if already have not token - throw an exception
        if (!loginData.containsKey("authenticity_token")) {
            throw new BooruEngineException("Can't find \"authenticity_token\" in login data.", new IllegalStateException());
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

}
