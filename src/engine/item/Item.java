package engine.item;

import engine.item.еnum.Rating;
import source.еnum.Boor;

import java.util.*;

/**
 * Simple class which can describe item from all boors.
 * If you want add new boor you can use this class or inherit from it.
 * You getting item as is. This means, that you can't modify this element after creating.
 * You can only getting data and work with it.
 * TODO: create Tests
 */
public class Item {

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

    /**
     * From what boor this item.
     */
    private Boor sourceBoor;

    /**
     * Constructor for basic item entity. Just storage item id, md5, score and sources.
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
     * Constructor for basic item entity. Just storage item id, md5, score and sources.
     * Source boor will be undefined
     *
     * @param hashMap - map with all attributes. Some of them will be use here.
     * Another can be used in inherit classes.
     */
    public Item(HashMap<String, String> hashMap) {
        setSourceBoor(hashMap.get("boor"));
        defaultConstructor(hashMap);
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
                    _setRating(pair.getValue());
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
                    _setTags(pair.getValue());
                    break;
                }
                //file_url, sample_url,
            }
        }
    }

    public final int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
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

    private void setMd5(String md5) {
        this.md5 = md5;
    }

    public final int getScore() {
        return score;
    }

    private void setScore(int score) {
        this.score = score;
    }

    public final Rating getRating() {
        return rating;
    }

    private void setRating(Rating rating) {
        this.rating = rating;
    }

    public final Boor getSourceBoor() {
        return sourceBoor;
    }

    private void setSourceBoor(String sourceBoor) {
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

    private void _setRating(String data) {
        switch (data) {
            case "s": {
                setRating(Rating.SAFE);
                break;
            }
            case "q": {
                setRating(Rating.QUESTIONABLE);
                break;
            }
            case "e": {
                setRating(Rating.EXPLICIT);
                break;
            }
        }
    }

    public final String getSource() {
        return source;
    }

    private void setSource(String source) {
        this.source = source;
    }

    public final String getPreview_url() {
        return preview_url;
    }

    private void setPreview_url(String preview_url) {
        //when http not defined
        if (!preview_url.contains("http")){
            this.preview_url = "https:" + preview_url;
        }else {
            this.preview_url = preview_url;
        }
    }

    private void _setTags(String tags){
        String[] split = tags.split(" ");
        HashSet<String> set = new HashSet<>(Arrays.asList(split));
        setTags(set);
    }

    public String getSample_url() {
        return sample_url;
    }

    public void setSample_url(String sample_url) {
        //when http not defined
        if (!preview_url.contains("http")){
            this.sample_url = "https:" + sample_url;
        }else {
            this.sample_url = sample_url;
        }
    }
}
