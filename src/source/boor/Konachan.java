package source.boor;

import engine.BooruEngineException;
import engine.HttpsConnection;
import engine.Method;
import module.LoginModule;
import source.Post;
import source.еnum.Format;

import java.util.*;

/**
 * Singleton.
 * Storage data about Konachan API and method for getting request.
 */
public class Konachan extends AbstractBoorAdvanced {

    private static final Konachan instance = new Konachan();

    private Map<String, String> loginData = new HashMap<>();

    public static Konachan get() {
        return instance;
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
        return getCustomRequest("post." + format.toString().toLowerCase() + "?tags=id:" + id);
    }

    @Override
    public String getCommentsByPostIdRequest(int post_id, Format format) {
        return getCustomRequest("comment." + format.toString().toLowerCase() + "?post_id=" + post_id);
    }

    @Override
    public Post newPostInstance(HashMap<String, String> attributes) {
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

    public void logIn(final String login, final String password) throws BooruEngineException {
        if (!loginData.containsKey("konachan.com") || !loginData.containsKey("authenticity_token")) {
            //get connection
            HttpsConnection connection = new HttpsConnection()
                    .setRequestMethod(Method.GET)
                    .setUserAgent(HttpsConnection.DEFAULT_USER_AGENT)
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

        System.out.println(loginData);
        System.out.println(" ");

        //if already have not cookie - throw an exception
        if (!loginData.containsKey("konachan.com")) {
            throw new BooruEngineException("Can't find \"konachan.com\" cookie in login data.");
        }
        //if already have not token - throw an exception
        if (!loginData.containsKey("authenticity_token")) {
            throw new BooruEngineException("Can't find \"authenticity_token\" in login data.");
        }

        //create new connection for login
        String logIn = getCustomRequest("/user/authenticate");
        String postData = "authenticity_token=" + loginData.get("authenticity_token") + "&user%5Bname%5D=" + login +
                "&user%5Bpassword%5D=" + password + "&commit=Login";
        String cookie = "konachan.com=" + loginData.get("konachan.com");

        //create connection
        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.POST)
                .setUserAgent(HttpsConnection.DEFAULT_USER_AGENT)
                .setBody(postData)
                .setCookies(cookie)
                .openConnection(logIn);

        if (connection.getHeader("Set-Cookie") == null){
            throw new BooruEngineException(new NullPointerException());
        }

        for (int i = 0; i < connection.getHeader("Set-Cookie").size(); i++) {
            String[] data = connection.getHeader("Set-Cookie").get(i).split("; ")[0].split("=");
            if (data.length == 2) this.loginData.put(data[0], data[1]);
        }
    }

    public void logOff() {
        this.loginData.clear();
    }

    public Map<String, String> getLoginData() {
        return this.loginData;
    }

    private void setCookie(final HttpsConnection connection) {
        connection
                .getHeader("Set-Cookie")
                .stream()
                .filter(s -> s.contains("konachan.com"))
                .forEach(s -> {
                    String[] split = s.split("=");
                    loginData.put(split[0], split[1].split("; ")[0]);
                });
    }

    private void setToken(final HttpsConnection connection) {
        String s = connection.getResponse();
        String data = s.split("name=\"csrf-param\" />")[1]
                .split(" name=\"csrf-token\" />")[0]
                .replaceAll("\"", "")
                .replace("<meta content=", "");
        loginData.put("authenticity_token", data);
    }



    public void vote(final int id, final int score) throws BooruEngineException{
        new HttpsConnection()
                .setRequestMethod(Method.POST)
                .setUserAgent(HttpsConnection.DEFAULT_USER_AGENT)
                .setCookies(loginData.toString().replaceAll(", ", "; "))
                .setHeader("X-CSRF-Token", loginData.get("authenticity_token"))
                .setBody("id="+id+"&score=" + score)
                .openConnection(getCustomRequest("/post/vote.json"));

    }
}