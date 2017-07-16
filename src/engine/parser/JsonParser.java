package engine.parser;


import com.google.gson.*;

import java.util.*;

/**
 * Parse JSONArray to a list of posts, where one post - is a hashmap with all data in it.
 */
public class JsonParser {

    /**
     * Parsing json with gson help.
     *
     * @param dataToParse - string, which describe json array.
     * @return list of Hashmaps, where the hashmap is a post.
     */
    public List<HashMap<String, String>> startParse(String dataToParse){
        List<HashMap<String, String>> result = new ArrayList<>();
        //create gson parser
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        //get all data in posts array
        JsonArray posts = parser.parse(dataToParse).getAsJsonArray();
        //for each post
        for (JsonElement object : posts){
            //create the hashmap for storing data
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
                //check splith length - when some values equals "" and we removing quotes - we must track this.
                if (split.length == 2) {
                    postData.put(split[0], split[1]);
                } else {
                    postData.put(split[0], "");
                }
            }
            //put hashmap to result list
            result.add(postData);
        }
        return result;
    }

}
