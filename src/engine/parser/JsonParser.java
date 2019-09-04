package engine.parser;

import com.google.gson.*;
import source.еnum.Boor;

import java.util.*;

/**
 * Parsing JSON arrays and elements. Another JSON Objects can be parse incorrectly.
 * <p>
 * When you create new instance of this class, you can specify in constructor,
 * will be the result data of this instance reusable or not.
 * If data reusable - when you start new parsing process(call <tt>startParse()</tt> again)
 * all data from last parsing process will be cleared.
 * If instance not reused - results will be collect in <tt>getResult()</tt> method.
 * You will can get all results by one call.
 * And, of course, you can't start new process from zero.
 * As default - instance will be reused.
 */
public class JsonParser {

    private final List<HashMap<String, String>> result = new ArrayList<>();
    private boolean reusable;

    /**
     * Default constructor - reusability is enable.
     */
    public JsonParser(){
        reusable = true;
    }

    /**
     * @param isReusable you can specify reusability.
     */
    public JsonParser(boolean isReusable){
        reusable = isReusable;
    }

    /**
     * Parsing json with GSON help.
     * It parse two objects - {@code JsonArray} or {@code JsonElement}.
     * Another objects can be processing incorrectly.
     * <p>The parse result can be get with {@code getResult()} method help.
     *
     * @param dataToParse string, which describe json array.
     */
    public JsonParser startParse(String dataToParse) {
        if (reusable) result.clear();
        //create gson parser
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        try {
            //get all data in posts array
            JsonArray posts = parser.parse(dataToParse).getAsJsonArray();
            //start parse
            parseArray(posts);
        } catch (IllegalStateException notArray) {
            //when the data not JSON Array
            JsonElement object = parser.parse(dataToParse).getAsJsonObject();
            //create array and put here single object
            JsonArray array = new JsonArray();
            array.add(object);
            //and parse as array
            parseArray(array);
        }
        return this;
    }

    /**
     * @return list of Hash maps, where each Hash map describe one post.
     * Hash map has next structure - &lt;Attribute_name, Attribute_value&gt;.
     */
    public List<HashMap<String, String>> getResult(){
        return result;
    }

    /**
     * @return is parser reusable or not.
     */
    public boolean isReusable(){
        return reusable;
    }

    private void parseArray(JsonArray array) {
        //for each post
        for (JsonElement object : array) {
            //for extracting boor name
            boolean flag = false;
            //create the hash map for storing data
            HashMap<String, String> postData = new HashMap<>();
            //for each attribute
            for (Object object1 : object.getAsJsonObject().entrySet()) {
                //get all data in one string
                String attribute = object1.toString();
                //if attribute not contains { or } - Behoimi check
                if (!attribute.contains("{") || !attribute.contains("{")) {
                    //we remove all quotes
                    attribute = attribute.replaceAll("\"", "");
                }
                //split this string to key and value
                String[] split = attribute.split("=");
                //check split length - when some values equals "" and we removing quotes - we must track this.
                if (split.length >= 2) {
                    //when data contains more than one "=", the array will be split more than the 2 parts.
                    // So we append all data to one
                    if (split.length > 2) for (int i = 2; i < split.length; i++) split[1] += "=" + split[i];
                    //and then put data to hash map
                    postData.put(split[0], split[1]);
                } else {
                    postData.put(split[0], "");
                }
                //trying to identify boor
                //when we do it - flag will be true and next accesses will be deny.
                if (split[0].contains("url") && !flag) {
                    String boor = extractBoor(split[1]);
                    postData.put("boor", boor);
                    flag = true;
                }
            }
            //put hash map to result list
            result.add(postData);

        }
    }

    private String extractBoor(String url) {
        String boor;
        if (url.contains("behoimi")) boor = Boor.Behoimi.toString();
        else if (url.contains("e621")) boor = Boor.E621.toString();
        else if (url.contains("konachan")) boor = Boor.Konachan.toString();
        else if (url.contains("yande")) boor = Boor.Yandere.toString();
        else if (url.contains("sakugabooru")) boor = Boor.Sakugabooru.toString();
        else boor = Boor.Danbooru.toString();
        return boor;
    }
}
