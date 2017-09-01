package source.boor;

import com.google.gson.JsonSyntaxException;
import com.sun.istack.internal.NotNull;
import engine.BooruEngineException;
import engine.MultipartConstructor;
import engine.connector.HttpsConnection;
import engine.connector.Method;
import engine.parser.JsonParser;
import module.interfacе.LoginModuleInterface;
import module.interfacе.RemotePostModuleInterface;
import module.interfacе.UploadModuleInterface;
import module.interfacе.VotingModuleInterface;
import source.Post;
import source.еnum.Format;
import source.еnum.Rating;

import javax.naming.AuthenticationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Singleton.
 * <p>
 * Describe Konachan.
 * <p>
 * Implements <tt>LoginModuleInterface</tt>, <tt>VotingModuleInterface</tt>, <tt>RemotePostModuleInterface</tt>.
 */
/*NOTE:
    Cookie is static
    csrf-token is static

    Login is OK
    Commenting is ... TODO: make CommentModuleInterface implementation(do it after ~2 weeks).
    Post Voting is OK
 */
public class Konachan extends AbstractBoorAdvanced implements LoginModuleInterface, VotingModuleInterface,
        RemotePostModuleInterface, UploadModuleInterface {

    private static final Konachan instance = new Konachan();

    private final Map<String, String> loginData = new HashMap<>();

    public static Konachan get() {
        return instance;
    }

    private Konachan() {
        super();
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    @Override
    public String getCustomRequest(String request) {
        return "https://konachan.com" + request;
    }

    @Override
    public String getPostByIdRequest(int id, Format format) {
        return getCustomRequest("/post." + format.toString().toLowerCase() + "?tags=id:" + id);
    }

    @Override
    public String getCommentsByPostIdRequest(int post_id, Format format) {
        return getCustomRequest("/comment." + format.toString().toLowerCase() + "?post_id=" + post_id);
    }

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
        post.setComments_url(getCommentsByPostIdRequest(post.getId()));
        return post;
    }

    @Override
    public String getAuthenticateRequest() {
        return getCustomRequest("/user/authenticate");
    }

    @Override
    public void logIn(final String login, final String password) throws BooruEngineException {
        if (!loginData.containsKey("konachan.com") || !loginData.containsKey("authenticity_token")) {
            //get connection
            HttpsConnection connection = new HttpsConnection()
                    .setRequestMethod(Method.GET)
                    .setUserAgent(HttpsConnection.getDefaultUserAgent())
                    .openConnection(getCustomRequest("/user/login"));

            //set cookie
            if (!loginData.containsKey("konachan.com")) {
                setCookie(connection);
            }
            //set token
            if (!loginData.containsKey("authenticity_token")) {
                setToken(connection);
            }
        }

        //if already have not cookie - throw an exception
        if (!loginData.containsKey("konachan.com")) {
            throw new BooruEngineException("Can't find \"konachan.com\" cookie in login data.");
        }
        //if already have not token - throw an exception
        if (!loginData.containsKey("authenticity_token")) {
            throw new BooruEngineException("Can't find \"authenticity_token\" in login data.");
        }

        //create new connection for login
        String postData = "authenticity_token=" + loginData.get("authenticity_token") + "&user%5Bname%5D=" + login +
                "&user%5Bpassword%5D=" + password + "&commit=Login";
        String cookie = "konachan.com=" + loginData.get("konachan.com");

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
    public Map<String, String> getLoginData() {
        return this.loginData;
    }

    protected void setCookie(final HttpsConnection connection) {
        connection
                .getHeader("Set-Cookie")
                .stream()
                .filter(s -> s.contains("konachan.com"))
                .forEach(s -> {
                    String[] split = s.split("=");
                    loginData.put(split[0], split[1].split("; ")[0]);
                });
    }

    protected void setToken(final HttpsConnection connection) throws BooruEngineException {
        String s = connection.getResponse();
        String data = s.split("name=\"csrf-param\" />")[1]
                .split(" name=\"csrf-token\" />")[0]
                .replaceAll("\"", "")
                .replace("<meta content=", "")
                .replaceAll(Pattern.quote("+"), "%2B");
        loginData.put("authenticity_token", data);
    }

    //score - 0 is remove, from 1 to 3. 3 is favorite.
    @Override
    public boolean votePost(final int id, final String score) throws BooruEngineException {
        String data;
        try {
            data = loginData.get("authenticity_token").replaceAll("%2B", "+");
        } catch (NullPointerException e) {
            throw new BooruEngineException("User data not defined.", new IllegalStateException());
        }
        try {
            new HttpsConnection()
                    .setRequestMethod(Method.POST)
                    .setUserAgent(HttpsConnection.getDefaultUserAgent())
                    .setCookies(getCookieFromLoginData())
                    .setHeader("X-CSRF-Token", data)
                    .setBody("id=" + id + "&score=" + score)
                    .openConnection(getCustomRequest("/post/vote.json"));
        } catch (BooruEngineException e) {
            throw new BooruEngineException(e.getCause().getMessage());
        }
        return true;
    }

    @Override
    public String getVotePostRequest() {
        return null;
    }

    @Override
    public String getCookieFromLoginData() {
        return getLoginData().toString().replaceAll(", ", "; ").replaceAll("\\{", "").replaceAll("\\}", "");
    }

    @Override
    public boolean createPost(final @NotNull File post, final @NotNull String tags, final String title, final String source, final @NotNull Rating rating, final String parent_id
    ) throws BooruEngineException {
        //check userdata
        String token;
        if (getCookieFromLoginData() == null) {
            throw new BooruEngineException(new IllegalStateException("User data not defined."));
        }
        try {
            token = loginData.get("authenticity_token").replaceAll("%2B", "+");
        } catch (NullPointerException e) {
            throw new BooruEngineException(new IllegalStateException("User data not defined."));
        }

        //write all data with stream to server
        HttpsConnection connection;
        try {
            //create constructor
            MultipartConstructor constructor = new MultipartConstructor()
                    .createDataBlock("authenticity_token", token)
                    .createFileBlock("post[file]", post)
                    .createDataBlock("post[source]", (source == null ? "" : source))
                    .createDataBlock("post[tags]", tags)
                    .createDataBlock("post[parent_id]", (parent_id == null ? "" : parent_id))
                    //capitalise data
                    .createDataBlock("post[rating]", rating.toString().toLowerCase().substring(0, 1).toUpperCase() + rating.toString().toLowerCase().substring(1));

            //Create connection
            connection = new HttpsConnection()
                    .setRequestMethod(Method.POST)
                    .setUserAgent(HttpsConnection.getDefaultUserAgent())
                    .setHeader("Content-Type", "multipart/form-data; boundary=" + constructor.BOUNDARY)
                    .setCookies(getCookieFromLoginData())
                    .openConnection(getCreatePostRequest());

            //send data
            constructor.send(connection.getConnection().getOutputStream());
        } catch (IOException e) {
            throw new BooruEngineException(e);
        }
        //get response
        try {
            JsonParser parser = new JsonParser();
            parser.startParse(connection.getResponse());
            List<HashMap<String, String>> jsonResult = parser.getResult();
            //if success - true
            if (jsonResult.get(0).get("success").equals("true")) return true;
            //else throw exception with reason
            else {
                throw new BooruEngineException(jsonResult.get(0).get("reason"), new IOException());
            }
            //when something go wring catch anu exception and throw in BEE
        } catch (Exception e){
            throw new BooruEngineException(connection.getResponseMessage(), e);
        }
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