package source;

import source.boor.AbstractBoor;
import source.еnum.Rating;
import source.еnum.Boor;

import java.io.Serializable;
import java.util.*;

/**
 * Simple class which can describe post from all boors.
 * <p>
 *     If you want add new boor you can use this class or extend it.
 * <p>
 *     Be careful, if your attributes has specific names -
 * you can implement in you boor RemotePostModuleInterface interface, which have special method
 * {@code newPostInstance(Hashmap&lt;String, String&gt;)}.
 * <p>
 * <p>Support attributes:
 * <p>    <i>id</i> - post id in the boor.
 * <p>    <i>md5</i> - post hash.
 * <p>    <i>rating</i> - image rating in post.
 * <p>    <i>score</i> - post rating.
 * <p>    <i>source</i> - from what place this post was get.
 * <p>    <i>preview_url/preview-url</i> - url to preview image.
 * <p>    <i>preview_url_file/preview-url-file</i> - attribute from danbooru,
 * so in the start there will be append string "https://danbooru.donmai.us".
 * <p>    <i>tags/tag_string/tag-string</i> - this describes the contents of the message and allow to find similar posts.
 * <p>    <i>sample_url</i> - url to sample image view.
 * <p>    <i>file_url/file-url</i> - url to file with high resolution. But for danbooru API there is sample url.
 * <p>    <i>large_file_url/large-file-url</i> - url to file with high resolution for danbooru API.
 * <p>    <i>creator_id/uploader_id/uploader-id</i> - id, who's create/upload this post.
 * <p>    <i>has_comments/last_commented_at</i> - boolean value, which describes, have this post comments or not.
 * but the flag will be {@code true} only if the value will not {@code null}.
 * <p>    <i>created_at</i> - show time, when post was created. Each boor storage this data in onw format,
 * so, method <tt>getCreate_time</tt> return a raw data from server in String. You must process it alone.
 *
 * @see Comment
 */
public class Post implements Serializable {

    private HashMap<String, String> original;

    private int id = Integer.MIN_VALUE;

    private HashSet<String> tags = null;

    private Rating rating = null;

    private String md5 = null;

    private String source = null;

    private int score = Integer.MIN_VALUE;

    private String preview_url = null;

    private String sample_url = null;

    private String file_url = null;

    private int creator_id = Integer.MIN_VALUE;

    private boolean[] has_comments = null;

    private String comments_url = null;

    private AbstractBoor sourceBoorRef = null;

    private String create_time = null;

    private int height = Integer.MIN_VALUE;

    private int width = Integer.MIN_VALUE;

    private int sample_height = Integer.MIN_VALUE;

    private int sample_width = Integer.MIN_VALUE;

    private int preview_height = Integer.MIN_VALUE;

    private int preview_width = Integer.MIN_VALUE;


    /**
     * Default constructor for basic post entity.
     * <p>Unstable.
     *
     * @param hashMap map with all attributes. Some of them will be use here.
     *                   Another can be used in inherit classes.
     * @param sourceBoor reference to boor, from what this post.
     */
    public Post(HashMap<String, String> hashMap, AbstractBoor sourceBoor) {
        sourceBoorRef = sourceBoor;

        defaultConstructor(hashMap);
    }

//    /**
//     * Constructor for basic post entity.
//     * <p>Source boor will be undefined in start,
//     * but in the constructor ending the source will be defined.
//     * <p>Unstable.
//     *
//     * @param hashMap map with all attributes. Some of them will be use here.
//     *                Another can be used in inherit classes.
//     */
//    public Post(HashMap<String, String> hashMap) {
//        setSourceBoor(hashMap.get("boor"));
//        defaultConstructor(hashMap);
//    }

    /**
     * Constructor for special boor.
     * It creating in special method {@code newPostInstance(Hashmap&lt;String, String&gt;)}.
     *
     * @param sourceBoor reference to boor, from what this post.
     */
    public Post(AbstractBoor sourceBoor) {
        sourceBoorRef = sourceBoor;
    }

    /**
     * Default constructor.
     */
    public Post() {
        //DEFAULT CONSTRUCTOR
    }

    /**
     * Copy constructor.
     */
    public Post(Post post){
        defaultConstructor(post.getMap());
    }

    protected void defaultConstructor(HashMap<String, String> hashMap) {
        original = hashMap;
        //create Entry
        Set<Map.Entry<String, String>> entrySet = hashMap.entrySet();
        //for each attribute
        for (Map.Entry<String, String> pair : entrySet) {
            //System.out.println(pair.getKey() + "   " + pair.getValue());
            switch (pair.getKey()) {
                case "id": {
                    setId(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "md5": {
                    setMd5(pair.getValue());
                    break;
                }
                case "rating": {
                    setRating(pair.getValue());
                    break;
                }
                case "score": {
                    setScore(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "source": {
                    setSource(pair.getValue());
                    break;
                }
                case "preview_url": {
                    setPreview_url(pair.getValue());
                    break;
                }
                case "preview-file-url":
                case "preview_file_url": {
                    setPreview_url("https://danbooru.donmai.us" + pair.getValue());
                    break;
                }
                case "tags":
                case "tag-string":
                case "tag_string": {
                    setTags(pair.getValue());
                    break;
                }
                case "sample_url": {
                    setSample_url(pair.getValue());
                    break;
                }
                case "file-url":
                case "file_url": {
                    if (getSourceBoor().equals(Boor.Danbooru.toString())) {
                        setSample_url("https://danbooru.donmai.us" + pair.getValue());
                        break;
                    }
                    setFile_url(pair.getValue());
                    break;
                }
                case "large-file-url":
                case "large_file_url": {
                    setFile_url("https://danbooru.donmai.us" + pair.getValue());
                    break;
                }
                case "creator_id":
                case "uploader-id":
                case "uploader_id": {
                    setCreator_id(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "has_comments":
                case "last-commented-at":
                case "last_commented_at": {
                    if ("null".equals(pair.getValue()) || "0".equals(pair.getValue()) || "false".equals(pair.getValue())) {
                        setHas_comments(false);
                    } else {
                        setHas_comments(true);
                    }
                    break;
                }
                case "updated-at":
                case "created_at":{
                    setCreate_Time(pair.getValue());
                    break;
                }
                case "height":{
                    setHeight(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "width":{
                    setWidth(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "sample_height":{
                    setSample_height(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "sample_width":{
                    setSample_width(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "image-height":
                case "preview_height":{
                    setPreview_height(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "image-width":
                case "preview_width":{
                    setPreview_width(Integer.parseInt(pair.getValue()));
                    break;
                }
            }
        }
        //after all check comments flag
        //when comments not defined - try to create comments address.
        if (has_comments == null){
            comments_url = sourceBoorRef.getCommentsByPostIdRequest(id);
            return;
        }
        //when has comments
        if (has_comments[0]){
            //and if true - setup comments url.
            comments_url = sourceBoorRef.getCommentsByPostIdRequest(id);
        }
    }

    /**
     * Getting post id. If data will not defined - method return {@code Integer.MIN_VALUE}.
     */
    public final int getId() {
        return id;
    }

    public final void setId(int id) {
        this.id = id;
    }

    /**
     * Getting tags {@code Hashset}. If data will not defined - method return {@code null}.
     */
    public final HashSet<String> getTags() {
        return tags;
    }

    /**
     * Getting md5. If data will not defined - method return {@code null}.
     */
    public final String getMd5() {
        return md5;
    }


    public final void setMd5(String md5) {
        this.md5 = md5;
    }

    /**
     * Getting score. If data will not defined - method return {@code Integer.MIN_VALUE}.
     */
    public final int getScore() {
        return score;
    }

    public final void setScore(int score) {
        this.score = score;
    }

    /**
     * Getting the post data rating. If data will not defined - method return {@code null}.
     */
    public final Rating getRating() {
        return rating;
    }

    /**
     * Getting AbstractBoor reference, from where boor  this post. If data will not defined - method return {@code null}.
     */
    public AbstractBoor getSourceBoor() {
        return sourceBoorRef;
    }

    public final void setSourceBoor(AbstractBoor sourceBoor) {
        this.sourceBoorRef = sourceBoor;
    }

    public final void setRating(String data) {
        switch (data) {
            case "s": {
                this.rating = Rating.SAFE;
                break;
            }
            case "q": {
                this.rating = Rating.QUESTIONABLE;
                break;
            }
            case "e": {
                this.rating = Rating.EXPLICIT;
                break;
            }
        }
    }

    /**
     * From what place this post data was get. If data will not defined - method return {@code null}.
     */
    public final String getSource() {
        return source;
    }

    public final void setSource(String source) {
        this.source = source;
    }

    /**
     * Getting url to preview file. If data will not defined - method return {@code null}.
     */
    public final String getPreview_url() {
        return preview_url;
    }

    public final void setPreview_url(String preview_url) {
        //when http not defined
        if (!preview_url.contains("http")) {
            this.preview_url = "https:" + preview_url;
        } else {
            this.preview_url = preview_url;
        }
    }

    public final void setTags(String tags) {
        String[] split = tags.split(" ");
        this.tags = new HashSet<>(Arrays.asList(split));
    }

    /**
     * Getting url to sample file. If data will not defined - method return {@code null}.
     */
    public String getSample_url() {
        return sample_url;
    }

    public final void setSample_url(String sample_url) {
        //when http not defined
        if (!sample_url.contains("http")) {
            this.sample_url = "https:" + sample_url;
        } else {
            this.sample_url = sample_url;
        }
    }

    /**
     * Getting url to full file. If data will not defined - method return {@code null}.
     */
    public String getFile_url() {
        return file_url;
    }

    public final void setFile_url(String file_url) {
        //when http not defined
        if (!file_url.contains("http")) {
            this.file_url = "https:" + file_url;
        } else {
            this.file_url = file_url;
        }
    }

    /**
     * Getting id, who's create/upload post. If data will not defined - method return {@code Integer.MIN_VALUE}.
     */
    public int getCreator_id() {
        return creator_id;
    }

    public final void setCreator_id(int creator_id) {
      this.creator_id = creator_id;
    }

    /**
     * Getting url for comments. If data will not defined - method return {@code null}.
     */
    public String getComments_url() {
        return comments_url;
    }

    public void setComments_url(String comments_url) {
        this.comments_url = comments_url;
    }

    /**
     * Getting boolean value - has post comments or not. If data will not defined - method return {@code false}.
     * When value not defined - method return {@code null}.
     * That means, if something go wrong in constructor or in input data and constructor can't defined value.
     */
    public Boolean isHas_comments() {
        if (has_comments == null) return null;
        return has_comments[0];
    }

    public void setHas_comments(boolean has_comments) {
        this.has_comments = new boolean[]{has_comments};
    }

    /**
     * Getting String value - raw data form the attribute, responsible for the creation time.
     * If data will not defined - method return {@code null}.
     */
    public String getCreate_time() {
        return this.create_time;
    }

    public void setCreate_Time(String create_time) {
        this.create_time = create_time;
    }

    /**
     * @return original Map with <strong>all</strong> non modified post data.
     */
    public HashMap<String, String> getMap(){
        return this.original;
    }

    /**
     * @param key key
     * @return value by this key, or null if key wasn't find.
     */
    public String getValue(String key) {
        return this.original.get(key);
    }

    /**
     * Getting height in pixels of full image. If not defined - method return {@code Integer.MIN_VALUE}.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Set full image height in pixels.
     * @param height image height in pixels.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Getting width in pixels of full image. If not defined - method return {@code Integer.MIN_VALUE}.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Set full image width in pixels.
     * @param width image width in pixels.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Getting height of sample image. If not defined - method return {@code Integer.MIN_VALUE}.
     */
    public int getSample_height() {
        return sample_height;
    }

    /**
     * Set sample image height in pixels.
     * @param sample_height image height in pixels.
     */
    public void setSample_height(int sample_height) {
        this.sample_height = sample_height;
    }

    /**
     * Getting width of sample image. If not defined - method return {@code Integer.MIN_VALUE}.
     */
    public int getSample_width() {
        return sample_width;
    }

    /**
     * Set sample image width in pixels.
     * @param sample_width image width in pixels.
     */
    public void setSample_width(int sample_width) {
        this.sample_width = sample_width;
    }

    /**
     * Getting height of preview image. If not defined - method return {@code Integer.MIN_VALUE}.
     */
    public int getPreview_height() {
        return preview_height;
    }
    /**
     * Set preview image height in pixels.
     * @param preview_height image height in pixels.
     */
    public void setPreview_height(int preview_height) {
        this.preview_height = preview_height;
    }

    /**
     * Getting width of preview image. If not defined - method return {@code Integer.MIN_VALUE}.
     */
    public int getPreview_width() {
        return preview_width;
    }

    /**
     * Set preview image width in pixels.
     * @param preview_width image width in pixels.
     */
    public void setPreview_width(int preview_width) {
        this.preview_width = preview_width;
    }
}
