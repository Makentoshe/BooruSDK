package source.boor;

import source.еnum.Rating;
import source.еnum.Boor;

import java.util.*;

/**
 * Simple class which can describe item from all boors.
 * If you want add new boor you can use this class or inherit from it.
 * You getting item as is. This means, that you can't modify this element after creating.
 * You can only getting data and work with it.
 *
 */
public class Item{
    /**
     * Item id.
     */
    private int id;

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
     * From what place this item was get.
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
     * Id, who's create/upload item.
     */
    private int creator_id;

    /**
     * From what boor this item.
     */
    private Boor sourceBoor;

    /**
     * Default constructor for basic item entity.
     * Setup only most important info. Unstable.
     *
     * @param hashMap - map with all attributes. Some of them will be use here.
     * Another can be used in inherit classes.
     * @param sourceBoor - from what boor this item will be get.
     */
    public Item(HashMap<String, String> hashMap, Boor sourceBoor) {
        this.sourceBoor = sourceBoor;
        defaultConstructor(hashMap);
    }

    /**
     * Constructor for basic item entity.
     * Setup only most important info.
     * Source boor will be undefined.
     * Unstable.
     *
     * @param hashMap - map with all attributes. Some of them will be use here.
     * Another can be used in inherit classes.
     */
    public Item(HashMap<String, String> hashMap) {
        setSourceBoor(hashMap.get("boor"));
        defaultConstructor(hashMap);
    }

    public Item(Boor sourceBoor){
        this.sourceBoor = sourceBoor;
    }

    public Item(){
        this.sourceBoor = Boor.Undefined;
    }

    private void defaultConstructor(HashMap<String, String> hashMap){
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
                case "preview_url":{
                    setPreview_url(pair.getValue());
                    break;
                }
                case "preview_file_url":{
                    setPreview_url("https://danbooru.donmai.us" + pair.getValue());
                    break;
                }
                case "tags":
                case "tag_string":{
                    setTags(pair.getValue());
                    break;
                }
                case "sample_url":{
                    setSample_url(pair.getValue());
                    break;
                }
                case "file_url":{
                    if (getSourceBoor().equals(Boor.Danbooru)){
                        setSample_url("https://danbooru.donmai.us" + pair.getValue());
                        break;
                    }
                    setFile_url(pair.getValue());
                    break;
                }
                case "large_file_url":{
                    setFile_url("https://danbooru.donmai.us" + pair.getValue());
                }
                case "creator_id":
                case "uploader_id":{
                    setCreator_id(Integer.parseInt(pair.getValue()));
                    break;
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

    public final Boor getSourceBoor() {
        return sourceBoor;
    }

    protected final void setSourceBoor(String sourceBoor) {
        switch(sourceBoor){
            case "Gelbooru":{
                this.sourceBoor = Boor.Gelbooru;
                break;
            }
            case "Danbooru":{
                this.sourceBoor = Boor.Danbooru;
                break;
            }
            case "Yandere":{
                this.sourceBoor = Boor.Yandere;
                break;
            }
            case "E621":{
                this.sourceBoor = Boor.E621;
                break;
            }
            case "Behoimi":{
                this.sourceBoor = Boor.Behoimi;
                break;
            }
            case "Sakugabooru":{
                this.sourceBoor = Boor.Sakugabooru;
                break;
            }
            case "Rule34":{
                this.sourceBoor = Boor.Rule34;
                break;
            }
            case "SafeBooru":{
                this.sourceBoor = Boor.SafeBooru;
                break;
            }
            case "Konachan":{
                this.sourceBoor = Boor.Konachan;
                break;
            }
            default:{
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
        if (!preview_url.contains("http")){
            this.preview_url = "https:" + preview_url;
        }else {
            this.preview_url = preview_url;
        }
    }

    protected final void setTags(String tags){
        String[] split = tags.split(" ");
        this.tags = new HashSet<>(Arrays.asList(split));
    }

    public String getSample_url() {
        return sample_url;
    }

    protected final void setSample_url(String sample_url) {
        //when http not defined
        if (!sample_url.contains("http")){
            this.sample_url = "https:" + sample_url;
        }else {
            this.sample_url = sample_url;
        }
    }

    public String getFile_url() {
        return file_url;
    }

    protected final void setFile_url(String file_url) {
        //when http not defined
        if (!file_url.contains("http")){
            this.file_url = "https:" + file_url;
        }else {
            this.file_url = file_url;
        }
    }

    public int getCreator_id() {
        return creator_id;
    }

    protected final void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }
}
