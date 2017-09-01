package source.boor;

import com.sun.istack.internal.NotNull;
import engine.BooruEngineException;
import engine.connector.HttpsConnection;
import engine.connector.Method;
import module.interfacе.*;
import source.Post;
import source.еnum.Format;
import source.еnum.Rating;

import java.io.*;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Singleton.
 * <p>
 * Describe Rule34.
 * <p>
 * Implements <tt>LoginModuleInterface</tt>, <tt>VotingModuleInterface</tt>, <tt>RemotePostModuleInterface</tt>, <tt>CommentModuleInterface</tt>.
 */
/*NOTE:
    Cookie are static.
    csrf-token disable.

    Login is OK
    Commenting is OK
    Post Voting is OK
 */
public class Rule34 extends AbstractBoorBasic implements LoginModuleInterface, VotingModuleInterface,
        RemotePostModuleInterface, CommentModuleInterface, UploadModuleInterface {

    private static final Rule34 instance = new Rule34();

    private Map<String, String> loginData = new HashMap<>(2);

    public static Rule34 get() {
        return instance;
    }

    private Rule34(){
        super();
    }

    @Override
    public String getCustomRequest(String request) {
        return "https://rule34.xxx" + request;
    }

    @Override
    public String getCommentsByPostIdRequest(int post_id, Format format) {
        return getCustomRequest("/index.php?page=dapi&q=index&s=comment&post_id=" + post_id);
    }

    @Override
    public Post newPostInstance(final Map<String, String> attributes){
        Post post = new Post(instance);
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
                    post.setPreview_url(pair.getValue());
                    break;
                }
                case "tags":{
                    post.setTags(pair.getValue());
                    break;
                }
                case "sample_url":{
                    post.setSample_url(pair.getValue());
                    break;
                }
                case "file_url":{
                    post.setFile_url(pair.getValue());
                    break;
                }
                case "source":{
                    post.setSource(pair.getValue());
                    break;
                }case "creator_id": {
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
            post.setComments_url(instance.getCommentsByPostIdRequest(post.getId()));
        }
        return post;
    }

    @Override
    public String getAuthenticateRequest() {
        return getCustomRequest("/index.php?page=account&s=login&code=00");
    }

    @Override
    public void logIn(final String login, final String password) throws BooruEngineException {
        String postData = "user="+login+"&pass="+password+"&submit=Log+in";

        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.POST)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .setBody(postData)
                .openConnection(getAuthenticateRequest());

        for (int i = 0; i < connection.getHeader("Set-Cookie").size(); i++){
            String[] data = connection.getHeader("Set-Cookie").get(i).split("; ")[0].split("=");
            if (data.length == 2) this.loginData.put(data[0], data[1]);        }
    }

    @Override
    public void logOff(){
        this.loginData.clear();
    }

    @Override
    public Map<String, String> getLoginData(){
        return this.loginData;
    }

    //up of down
    @Override
    public boolean votePost(final int id, final String action) throws BooruEngineException{
        if (!action.equals("up")) return false;

        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.GET)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
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
        String cbody =
                "comment="+body.replaceAll(" ", "+")+
                        "&post_anonymous="+ (postAsAnon?"on":"off") +
                        "&submit=Post+comment&conf=2";

        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.POST)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .setCookies(loginData.toString().replaceAll(", ", "; "))
                .setBody(cbody)
                .openConnection(getCreateCommentRequest(id));

        return connection.getResponse().equals("") && connection.getResponseCode() == 302;
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
    public boolean createPost(final @NotNull File post, final @NotNull String tags, final String title, final String source, final @NotNull Rating rating, final String parent_id) throws BooruEngineException {
        //check userdata
        if (getCookieFromLoginData() == null) {
            throw new BooruEngineException(new IllegalStateException("User data not defined"));
        }

        final String BOUNDARY = "----WebKitFormBoundaryBooruEngineLib";
        final String LINE_FEED = "\r\n";

        //Create connection
        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.POST)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .setHeader("Content-Type", "multipart/form-data; boundary=" + BOUNDARY)
                .setCookies(getCookieFromLoginData())
                .openConnection(getCreatePostRequest());
        //and write all data with stream to server

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
            writer.append((source == null ? " " : source) + LINE_FEED);//put here source

            writer.append("--" + BOUNDARY + LINE_FEED);
            writer.append("Content-Disposition: form-data; name=\"title\"" + LINE_FEED + LINE_FEED);
            writer.append((title == null ? " " : title) + LINE_FEED);//put here title

            writer.append("--" + BOUNDARY + LINE_FEED);
            writer.append("Content-Disposition: form-data; name=\"tags\"" + LINE_FEED + LINE_FEED);
            writer.append(tags + LINE_FEED);//put here tags

            writer.append("--" + BOUNDARY + LINE_FEED);
            writer.append("Content-Disposition: form-data; name=\"rating\"" + LINE_FEED + LINE_FEED);
            writer.append(rating.toString().toLowerCase().charAt(0) + LINE_FEED);//put here rating

            writer.append("--" + BOUNDARY + LINE_FEED);
            writer.append("Content-Disposition: form-data; name=\"submit\"" + LINE_FEED + LINE_FEED);
            writer.append("Upload" + LINE_FEED);
            writer.append("--" + BOUNDARY + "--" + LINE_FEED);
            writer.flush();
            writer.close();
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
        else{
            if (errMessage.contains("Filetype not allowed.")){
                throw new BooruEngineException(new IOException("Filetype not allowed. The image could not be added because it already exists or it is corrupted."));
            }
            if (errMessage.contains("Generic error.")){
                throw new BooruEngineException(new IllegalArgumentException("The required data was not included, not image was specified, or a required field did not exist."));
            }
            else throw new BooruEngineException(errMessage);
        }
    }

    @Override
    public String getCreatePostRequest() {
        return getCustomRequest("/index.php?page=post&s=add");
    }
}
