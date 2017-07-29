package source.boor;


import source.Post;
import source.еnum.Api;
import source.еnum.Format;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BigBooru extends AbstractBoor {

    private static BigBooru instance = null;

    public static BigBooru get(){
        if (instance == null) instance = new BigBooru();
        return instance;
    }

    private final Format format = Format.XML;

    private final Api api = Api.BASICS;

    @Override
    public Format getFormat() {
        return format;
    }

    @Override
    public Api getApi() {
        return api;
    }

    @Override
    public String getCustomRequest(String request) {
        return "https://tbib.org/index.php?page=dapi&q=index&s=";
    }

    @Override
    public String getPostByIdRequest(int id, Format ignore) {
        return getCustomRequest("s=post&id=" + id);
    }

    @Override
    public String getPackByTagsRequest(int limit, String tags, int page, Format ignore) {
        return getCustomRequest("post&limit=" + limit + "&tags=" + tags + "&pid=" + page);
    }

    @Override
    public String getCommentsByPostIdRequest(int post_id, Format format) {
        return getCustomRequest("comment&post_id=" + post_id);
    }

    @Override
    public Post newPostInstance(HashMap<String, String> attributes) {
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
                    post.setCreate_time(pair.getValue());
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
}
