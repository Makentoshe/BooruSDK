package engine.item;

import engine.item.еnum.Rating;

import java.util.HashSet;

/**
 * Simple class which can describe item from all boors.
 * If you want add new boor you can use this class or inherit from it.
 */
public class Item {

    /**
     * Item id.
     */
    private int id;

    /**
     * Url to file.
     */
    private String file_url;

    /**
     * Url to sample.
     */
    private String sample_url;

    /**
     * Url to preview.
     */
    private String preview_url;

    /**
     * List with tags.
     */
    private HashSet<String> tags;

    /**
     * Item rating.
     */
    private Rating rating;

    private String md5;

    /**
     * Scores.
     */
    private int score;

    /**
     * Item extension.
     */
    private String extension;

    /**
     * Uploader id.
     */
    private int uploader_id;

    /**
     * Time, when item was created.
     */
    private String created_at;

    public final int getId() {
        return id;
    }

    public final void setId(int id) {
        this.id = id;
    }

    public final String getFile_url() {
        return file_url;
    }

    public final void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public final String getSample_url() {
        return sample_url;
    }

    public final void setSample_url(String sample_url) {
        this.sample_url = sample_url;
    }

    public final String getPreview_url() {
        return preview_url;
    }

    public final void setPreview_url(String preview_url) {
        this.preview_url = preview_url;
    }

    public final HashSet<String> getTags() {
        return tags;
    }

    public final void setTags(HashSet<String> tags) {
        this.tags = tags;
    }

    public final String getMd5() {
        return md5;
    }

    public final void setMd5(String md5) {
        this.md5 = md5;
    }

    public final int getScore() {
        return score;
    }

    public final void setScore(int score) {
        this.score = score;
    }

    public final String getExtension() {
        return extension;
    }

    public final void setExtension(String extension) {
        this.extension = extension;
    }

    public final int getUploader_id() {
        return uploader_id;
    }

    public final void setUploader_id(int uploader_id) {
        this.uploader_id = uploader_id;
    }

    public final String getCreated_at() {
        return created_at;
    }

    public final void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public final Rating getRating() {
        return rating;
    }

    public final void setRating(Rating rating) {
        this.rating = rating;
    }
}
