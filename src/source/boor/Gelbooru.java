package source.boor;

import com.sun.istack.internal.NotNull;
import engine.BooruEngineException;
import engine.HttpsConnection;
import engine.Method;
import module.interfacе.*;
import source.Post;
import source.еnum.Format;
import source.еnum.Rating;

import java.io.*;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Singleton.
 * <p>
 * Describe Gelbooru.
 * <p>
 * Implements <tt>LoginModuleInterface</tt>,<tt>VotingModuleInterface</tt>,
 * <tt>RemotePostModuleInterface</tt>, <tt>CommentModuleInterface</tt>,
 * <tt>UploadModuleInterface</tt>.
 */
/*NOTE:
    Cookie are static and not update, when page is upload.
    csrf-token are static and not update.

    Login is OK
    Commenting is OK
    Post Voting is OK
    Uploading is OK
 */
public class Gelbooru
        extends AbstractBoorBasic
        implements
        LoginModuleInterface,
        VotingModuleInterface,
        RemotePostModuleInterface,
        CommentModuleInterface,
        UploadModuleInterface {

    private static final Gelbooru mInstance = new Gelbooru();

    private final Map<String, String> loginData = new HashMap<>(2);

    public static Gelbooru get() {
        return mInstance;
    }

    private Gelbooru(){
        super();
    }

    @Override
    public String getCustomRequest(String request) {
        return "https://gelbooru.com" + request;
    }

    @Override
    public Post newPostInstance(final Map<String, String> attributes){
        Post post = new Post(mInstance);
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
                    post.setPreview_url("https:" + pair.getValue());
                    break;
                }
                case "tags":{
                    post.setTags(pair.getValue());
                    break;
                }
                case "sample_url":{
                    post.setSample_url("https:" + pair.getValue());
                    break;
                }
                case "file_url":{
                    post.setFile_url("https:" + pair.getValue());
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
                case "has_comments": {
                    if ("true".equals(pair.getValue())) {
                        post.setHas_comments(true);
                    } else {
                        post.setHas_comments(false);
                    }
                    break;
                }
                case "created_at":{
                    post.setCreate_Time(pair.getValue());
                    break;
                }
            }
        }
        //after all check comments flag
        if (post.isHas_comments()){
            //and if true - setup comments url.
            post.setComments_url(mInstance.getCommentsByPostIdRequest(post.getId()));
        }
        return post;
    }

    @Override
    public String getCommentsByPostIdRequest(final int post_id, final Format ignored) {
        return getCustomRequest("/index.php?page=dapi&q=index&s=comment&post_id=" + post_id);
    }

    public void logIn(final String login, final String password) throws BooruEngineException{
        String postData = "user="+login+"&pass="+password+"&submit=Log+in";
        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.POST)
                .setUserAgent(HttpsConnection.DEFAULT_USER_AGENT)
                .setBody(postData)
                .openConnection(getAuthenticateRequest());

        for (int i = 0; i < connection.getHeader("Set-Cookie").size(); i++){
            String[] data = connection.getHeader("Set-Cookie").get(i).split("; ")[0].split("=");
            if (data.length == 2) this.loginData.put(data[0], data[1]);
        }
    }

    @Override
    public void logOff(){
        this.loginData.clear();
    }

    @Override
    public Map<String, String> getLoginData(){
        return this.loginData;
    }

    @Override
    public String getAuthenticateRequest() {
        return getCustomRequest("/index.php?page=account&s=login&code=00");
    }

    @Override
    public String getPostByIdRequest(int id, Format format) {
        return getCustomRequest("/index.php?page=dapi&q=index&s=post&id=" + String.valueOf(id) + (format.equals(Format.JSON)?"json=1":""));
    }

    //action - up
    @Override
    public boolean votePost(final int id, final String action) throws BooruEngineException{
        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.GET)
                .setUserAgent(HttpsConnection.DEFAULT_USER_AGENT)
                .setCookies(loginData.toString().replaceAll(", ", "; "))
                .openConnection(getVotePostRequest() + "&id=" + id + "&type=" + action);

        return !connection.getResponse().equals("");
    }

    @Override
    public String getVotePostRequest() {
        return getCustomRequest("/index.php?page=post&s=vote");
    }

    @Override
    public boolean commentPost(int id, String body, boolean postAsAnon, boolean bumpPost) throws BooruEngineException {
        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.GET)
                .setUserAgent(HttpsConnection.DEFAULT_USER_AGENT)
                .setHeader("Connection", "keep-alive")
                .openConnection("https://gelbooru.com/index.php?page=post&s=view&id=" + id);

        String token = connection.getResponse()
                .split("\"/>\t\t<input type=\"hidden\" name=\"csrf-token\" value=\"")[1]
                .split("\"/>\t\t</td>")[0];

        //get PHPSESSID
        for (int i = 0; i < connection.getHeader("Set-Cookie").size(); i++){
            String[] data = connection.getHeader("Set-Cookie").get(i).split("; ")[0].split("=");
            if (data.length == 2) this.loginData.put(data[0], data[1]);
        }

        String cbody =
                "comment="+body.replaceAll(" ", "+")+
                        "&post_anonymous="+ (postAsAnon?"on":"off") +
                        "&submit=Post+comment&conf=1" +
                        "&csrf-token=" + token
                        .replaceAll(Pattern.quote("+"), "%2B")
                        .replaceAll("/", "%2F")
                        .replaceAll("=", "%3D");

        connection = new HttpsConnection()
                .setRequestMethod(Method.POST)
                .setUserAgent(HttpsConnection.DEFAULT_USER_AGENT)
                .setBody(cbody)
                .setCookies(loginData.toString().replaceAll(", ", "; "))
                .openConnection(getCreateCommentRequest(id));

        return connection.getResponse().equals("");
    }

    @Override
    public String getCreateCommentRequest(final int id) {
        return getCustomRequest("/index.php?page=comment&id="+id+"&s=save");
    }

    @Override
    public String getCookieFromLoginData() {
        return getLoginData().toString().replaceAll(", ", "; ").replaceAll("\\{","").replaceAll("\\}", "");
    }

    @Override
    public boolean createPost(@NotNull File post, @NotNull String tags, String title, String source, @NotNull Rating rating) throws BooruEngineException {
        final String BOUNDARY = "----WebKitFormBoundaryBooruEngineLib";
        final String LINE_FEED = "\r\n";

        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.POST)
                .setUserAgent(HttpsConnection.DEFAULT_USER_AGENT)
                .setHeader("Content-Type", "multipart/form-data; boundary=" + BOUNDARY)
                .setCookies(getCookieFromLoginData())
                .openConnection(getCustomRequest("/index.php?page=post&s=add"));
        try {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(connection.getConnection().getOutputStream(), "UTF-8"), true);
            writer.append("--" + BOUNDARY + LINE_FEED);
            writer.append("Content-Disposition: form-data; name=\"upload\"; filename=\"" + post.getName() + "\"" + LINE_FEED);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(post.getName()) + LINE_FEED);
            writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED).append(LINE_FEED);
            writer.flush();
            FileInputStream inputStream = new FileInputStream(post);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                connection.getConnection().getOutputStream().write(buffer, 0, bytesRead);
            }
            connection.getConnection().getOutputStream().flush();
            inputStream.close();
            writer.append(LINE_FEED);
            writer.flush();

        writer.append("--" + BOUNDARY + LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"source\"" + LINE_FEED + LINE_FEED);
        writer.append(source + LINE_FEED);//put here source

        writer.append("--" + BOUNDARY + LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"title\"" + LINE_FEED + LINE_FEED);
        writer.append(title + LINE_FEED);//put here title

        writer.append("--" + BOUNDARY + LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"tags\"" + LINE_FEED + LINE_FEED);
        writer.append(tags + LINE_FEED);//put here tags

        writer.append("--" + BOUNDARY + LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"rating\"" + LINE_FEED + LINE_FEED);
        writer.append(rating + LINE_FEED);//put here rating

        writer.append("--" + BOUNDARY + LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"submit\"" + LINE_FEED + LINE_FEED);
        writer.append("Upload" + LINE_FEED);
        writer.append("--" + BOUNDARY + "--" + LINE_FEED);
        writer.flush();
        writer.close();
        }catch (IOException e){
            throw new BooruEngineException(e);
        }

        boolean code = connection.getResponseCode() == 200;
        boolean message = connection.getResponse().contains("Image added.");
        return code && message;
    }
}