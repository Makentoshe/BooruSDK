package source.boor;

import source.еnum.Rating;
import source.еnum.Boor;

import java.util.*;

/**
 * Simple class which can describe post from all boors.
 * <p>If you want add new boor  you can use this class or inherit from it.
 * <p>You getting post as is. This means, that you can't modify this element after creating.
 * You can only getting data and work with it.
 * TODO: yandere and sakugabooru is not support searching comments by post_id. Try to find something else...
 */
public class Post {
    /**
     * Post id.
     */
    private int id;

    /**
     * List with tags.
     */
    private HashSet<String> tags;

    /**
     * Post rating.
     */
    private Rating rating;

    private String md5;

    /**
     * From what place this post was get.
     */
    private String source;

    /**
     * Scores.
     */
    private int score;

    /**
     * Url in string format with preview address.
     */
    private String preview_url;

    private String sample_url;

    private String file_url;

    /**
     * Id, who's create/upload post.
     */
    private int creator_id;

    /**
     * Are there any comments on the post.
     */
    private boolean has_comments;

    /**
     * Url for comments request.
     */
    private String comments_url;

    /**
     * From what boor this post.
     */
    private Boor sourceBoor;

    /**
     * Default constructor for basic post entity.
     * <p>Setup only most important info. Unstable.
     *
     * @param hashMap    - map with all attributes. Some of them will be use here.
     *                   Another can be used in inherit classes.
     * @param sourceBoor - from what boor this item will be get.
     */
    public Post(HashMap<String, String> hashMap, Boor sourceBoor) {
        this.sourceBoor = sourceBoor;
        defaultConstructor(hashMap);
    }

    /**
     * Constructor for basic post entity.
     * <p>Setup only most important info.
     * <p>Source boor will be undefined.
     * <p>Unstable.
     *
     * @param hashMap - map with all attributes. Some of them will be use here.
     *                Another can be used in inherit classes.
     */
    public Post(HashMap<String, String> hashMap) {
        setSourceBoor(hashMap.get("boor"));
        defaultConstructor(hashMap);
    }

    /**
     * Constructor for special boor. It creating in special method "newPostInstance".
     *
     * @param sourceBoor - from what boor this item will be get.
     */
    public Post(Boor sourceBoor) {
        this.sourceBoor = sourceBoor;
    }

    /**
     * Constructor for special boor. It creating in special method "newPostInstance".
     * <p>But the boor will be have Undefined status.
     */
    public Post() {
        this.sourceBoor = Boor.Undefined;
    }


    /**
     * Default constructor for Posts. It support some formats attributes.
     * <p>Be careful, if your attributes has specific names -
     * you must create Post entity in special method {@code newPostInstance(Hashmap&lt;String, String&gt;)} in you boor.
     * <p>
     * <p>Support attributes:
     * <p>    <i>id</i> - post id in boor.
     * <p>    <i>md5</i> - post hash.
     * <p>    <i>rating</i> - image rating in post.
     * <p>    <i>score</i> - post rating.
     * <p>    <i>source</i> - from what place this post was get.
     * <p>    <i>preview_url</i> - url to preview image.
     * <p>    <i>preview_url_file</i> - attribute from danbooru,
     * so in the start there will be append string "https://danbooru.donmai.us".
     * <p>    <i>tags</i> - this describes the contents of the message and allow to find similar posts.
     * <p>    <i>tag_string</i> - same as "<i>tags</i>".
     * <p>    <i>sample_url</i> - url to sample image view.
     * <p>    <i>file_url</i> - url to file with high resolution. But for danbooru API there is sample url.
     * <p>    <i>large_file_url</i> - url to file with high resolution for danbooru API.
     * <p>    <i>creator_id</i> - id, who's create/upload this post.
     * <p>    <i>uploader_id</i> - id, who's create/upload this post.
     * <p>    <i>has_comments</i> - boolean value, which describes, have this post comments or not.
     * <p>    <i>last_commented_at</i> - same as "<i>has_comments</i>",
     * but the flag will be {@code true} only if the value will not {@code null}.
     *
     * @param hashMap - all post attributes contains here. It has the next structure:
     *                HashMap&lt;Attribute_name, Attribute_value&gt;.
     */
    private void defaultConstructor(HashMap<String, String> hashMap) {
        //create Entry
        Set<Map.Entry<String, String>> entrySet = hashMap.entrySet();
        //for each attribute
        for (Map.Entry<String, String> pair : entrySet) {
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
                case "preview_file_url": {
                    setPreview_url("https://danbooru.donmai.us" + pair.getValue());
                    break;
                }
                case "tags":
                case "tag_string": {
                    setTags(pair.getValue());
                    break;
                }
                case "sample_url": {
                    setSample_url(pair.getValue());
                    break;
                }
                case "file_url": {
                    if (getSourceBoor().equals(Boor.Danbooru.toString())) {
                        setSample_url("https://danbooru.donmai.us" + pair.getValue());
                        break;
                    }
                    setFile_url(pair.getValue());
                    break;
                }
                case "large_file_url": {
                    setFile_url("https://danbooru.donmai.us" + pair.getValue());
                    break;
                }
                case "creator_id":
                case "uploader_id": {
                    setCreator_id(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "has_comments":
                case "last_commented_at": {
                    if (!"null".equals(pair.getValue()) || "true".equals(pair.getValue())) {
                        setHas_comments(true);

                    } else {
                        setHas_comments(false);
                    }
                }
            }
        }
    }

    public final int getId() {
        return id;
    }

    final void setId(int id) {
        this.id = id;
    }

    public final HashSet<String> getTags() {
        return tags;
    }

    public final String getMd5() {
        return md5;
    }

    protected final void setMd5(String md5) {
        this.md5 = md5;
    }

    public final int getScore() {
        return score;
    }

    protected final void setScore(int score) {
        this.score = score;
    }

    public final Rating getRating() {
        return rating;
    }

    public String getSourceBoor() {
        return sourceBoor.toString();
    }

    final void setSourceBoor(String sourceBoor) {
        switch (sourceBoor) {
            case "Gelbooru": {
                this.sourceBoor = Boor.Gelbooru;
                break;
            }
            case "Danbooru": {
                this.sourceBoor = Boor.Danbooru;
                break;
            }
            case "Yandere": {
                this.sourceBoor = Boor.Yandere;
                break;
            }
            case "E621": {
                this.sourceBoor = Boor.E621;
                break;
            }
            case "Behoimi": {
                this.sourceBoor = Boor.Behoimi;
                break;
            }
            case "Sakugabooru": {
                this.sourceBoor = Boor.Sakugabooru;
                break;
            }
            case "Rule34": {
                this.sourceBoor = Boor.Rule34;
                break;
            }
            case "SafeBooru": {
                this.sourceBoor = Boor.SafeBooru;
                break;
            }
            case "Konachan": {
                this.sourceBoor = Boor.Konachan;
                break;
            }
            default: {
                this.sourceBoor = Boor.Undefined;
            }
        }
    }

    protected final void setRating(String data) {
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

    public final String getSource() {
        return source;
    }

    protected final void setSource(String source) {
        this.source = source;
    }

    public final String getPreview_url() {
        return preview_url;
    }

    protected final void setPreview_url(String preview_url) {
        //when http not defined
        if (!preview_url.contains("http")) {
            this.preview_url = "https:" + preview_url;
        } else {
            this.preview_url = preview_url;
        }
    }

    protected final void setTags(String tags) {
        String[] split = tags.split(" ");
        this.tags = new HashSet<>(Arrays.asList(split));
    }

    public String getSample_url() {
        return sample_url;
    }

    protected final void setSample_url(String sample_url) {
        //when http not defined
        if (!sample_url.contains("http")) {
            this.sample_url = "https:" + sample_url;
        } else {
            this.sample_url = sample_url;
        }
    }

    public String getFile_url() {
        return file_url;
    }

    protected final void setFile_url(String file_url) {
        //when http not defined
        if (!file_url.contains("http")) {
            this.file_url = "https:" + file_url;
        } else {
            this.file_url = file_url;
        }
    }

    public int getCreator_id() {
        return creator_id;
    }

    protected final void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public String getComments_url() {
        return comments_url;
    }

    public void setComments_url(String comments_url) {
        this.comments_url = comments_url;
    }

    public boolean isHas_comments() {
        return has_comments;
    }

    public void setHas_comments(boolean has_comments) {
        this.has_comments = has_comments;
    }
}
