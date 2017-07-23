package source;

import source.еnum.Rating;
import source.еnum.Boor;

import java.util.*;

/**
 * Simple class which can describe post from all boors.
 * <p>
 *     If you want add new boor you can use this class or inherit from it.
 * <p>
 *     You getting post as is. This means, that you can't modify this element after creating.
 * You can dynamically add data to entity, but if the data is already defined, you can't change it.
 * For example, when you try setup new id, when the id is defined - this operation will be ignored.
 * <p>
 *     You can only getting data and work with it.
 * <p>
 *     Default constructor for Post support some formats attributes.
 * <p>
 *     Be careful, if your attributes has specific names -
 * you must create Post entity in special method {@code newPostInstance(Hashmap&lt;String, String&gt;)} in you boor.
 * <p>
 * <p>Support attributes:
 * <p>    <i>id</i> - post id in the boor.
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
 */
//TODO: yandere and sakugabooru is not support searching comments by post_id. Try to find something else...
public class Post {

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

    private Boor sourceBoor = null;

    /**
     * Default constructor for basic post entity.
     * <p>Unstable.
     *
     * @param hashMap map with all attributes. Some of them will be use here.
     *                   Another can be used in inherit classes.
     * @param sourceBoor from what boor this item will be get.
     */
    public Post(HashMap<String, String> hashMap, Boor sourceBoor) {
        this.sourceBoor = sourceBoor;
        defaultConstructor(hashMap);
    }

    /**
     * Constructor for basic post entity.
     * <p>Source boor will be undefined.
     * <p>Unstable.
     *
     * @param hashMap map with all attributes. Some of them will be use here.
     *                Another can be used in inherit classes.
     */
    public Post(HashMap<String, String> hashMap) {
        setSourceBoor(hashMap.get("boor"));
        defaultConstructor(hashMap);
    }

    /**
     * Constructor for special boor. It creating in special method {@code newPostInstance(Hashmap&lt;String, String&gt;)}.
     *
     * @param sourceBoor from what boor this item will be get.
     */
    public Post(Boor sourceBoor) {
        this.sourceBoor = sourceBoor;
    }

    /**
     * Constructor for special boor. It creating in special method {@code newPostInstance(Hashmap&lt;String, String&gt;)}.
     * <p>But the boor will be have {@code Undefined} status.
     */
    public Post() {
        this.sourceBoor = Boor.Undefined;
    }


    protected void defaultConstructor(HashMap<String, String> hashMap) {
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

    /**
     * Getting post id. If data will not defined - method return {@code Integer.MIN_VALUE}.
     */
    public final int getId() {
        return id;
    }

    /**
     * Method will be success only if data was not defined yet.
     */
    public final void setId(int id) {
        if (this.id == Integer.MIN_VALUE) this.id = id;
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

    /**
     * Method will be success only if data was not defined yet.
     */
    public final void setMd5(String md5) {
        if (this.md5 == null) this.md5 = md5;
    }

    /**
     * Getting score. If data will not defined - method return {@code Integer.MIN_VALUE}.
     */
    public final int getScore() {
        return score;
    }

    /**
     * Method will be success only if data was not defined yet.
     */
    public final void setScore(int score) {
        if (this.score == Integer.MIN_VALUE) this.score = score;
    }

    /**
     * Getting the post data rating. If data will not defined - method return {@code null}.
     */
    public final Rating getRating() {
        return rating;
    }

    /**
     * Getting string with name, from where boor  this post. If data will not defined - method return {@code null}.
     */
    public String getSourceBoor() {
        return sourceBoor.toString();
    }

    /**
     * Method will be success only if data was not defined yet.
     */
    public final void setSourceBoor(String sourceBoor) {
        if (this.sourceBoor != null) return;

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

    /**
     * Method will be success only if data was not defined yet.
     */
    public final void setRating(String data) {
        if (this.rating != null) return;

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

    /**
     * Method will be success only if data was not defined yet.
     */
    public final void setSource(String source) {
        if (this.source == null) this.source = source;
    }

    /**
     * Getting url to preview file. If data will not defined - method return {@code null}.
     */
    public final String getPreview_url() {
        return preview_url;
    }

    /**
     * Method will be success only if data was not defined yet.
     */
    public final void setPreview_url(String preview_url) {
        if (this.preview_url != null) return;

        //when http not defined
        if (!preview_url.contains("http")) {
            this.preview_url = "https:" + preview_url;
        } else {
            this.preview_url = preview_url;
        }
    }

    /**
     * Method will be success only if data was not defined yet.
     */
    public final void setTags(String tags) {
        if (this.tags != null) return;

        String[] split = tags.split(" ");
        this.tags = new HashSet<>(Arrays.asList(split));
    }

    /**
     * Getting url to sample file. If data will not defined - method return {@code null}.
     */
    public String getSample_url() {
        return sample_url;
    }

    /**
     * Method will be success only if data was not defined yet.
     */
    public final void setSample_url(String sample_url) {
        if (this.sample_url != null) return;
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

    /**
     * Method will be success only if data was not defined yet.
     */
    public final void setFile_url(String file_url) {
        if (this.file_url != null) return;
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

    /**
     * Method will be success only if data was not defined yet.
     */
    public final void setCreator_id(int creator_id) {
        if (this.creator_id == Integer.MIN_VALUE) this.creator_id = creator_id;
    }

    /**
     * Getting url for comments. If data will not defined - method return {@code null}.
     */
    public String getComments_url() {
        return comments_url;
    }

    /**
     * Method will be success only if data was not defined yet.
     */
    public void setComments_url(String comments_url) {
        if (this.comments_url != null) return;
        this.comments_url = comments_url;
    }

    /**
     * Getting boolean value - has post comments or not. If data will not defined - method return {@code false}.
     */
    public boolean isHas_comments() {
        return this.has_comments != null && has_comments[0];
    }

    /**
     * Method will be success only if data was not defined yet.
     */
    public void setHas_comments(boolean has_comments) {
        if (this.has_comments != null) return;
        this.has_comments = new boolean[]{has_comments};
    }
}
