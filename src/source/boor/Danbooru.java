package source.boor;

import module.LoginModule;
import module.PostModule;
import source.Post;
import source.еnum.Format;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Singleton.
 * Storage data about Danbooru API and method for getting request
 * NO LONGER SUPPORTED.
 */
@Deprecated
public class Danbooru extends AbstractBoorAdvanced implements PostModule {

    private static final Danbooru instance = new Danbooru();

    public static Danbooru get() {
        return instance;
    }

    private Danbooru(){
        super();
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    @Override
    public String getCustomRequest(String request) {
        return "https://danbooru.donmai.us" + request;
    }

    @Override
    public String getPackByTagsRequest(int limit, String tags, int page, Format format) {
        return getCustomRequest("/posts." + format.toString().toLowerCase() +
                "?tags=" + tags + "&limit=" + limit + "&page=" + page);
    }

    @Override
    public String getCommentsByPostIdRequest(int post_id, Format format) {
        return getCustomRequest("/comments." + format.toString().toLowerCase() +
                "?group_by=comment&search[post_id]=" + post_id);
    }

    @Override
    public Post newPostInstance(HashMap<String, String> attributes) {
        Post post = new Post(instance);
        //create Entry
        Set<Map.Entry<String, String>> entrySet = attributes.entrySet();
        //for each attribute
        for (Map.Entry<String, String> pair : entrySet) {
            switch (pair.getKey()) {
                case "id": {
                    post.setId(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "md5": {
                    post.setMd5(pair.getValue());
                    break;
                }
                case "rating": {
                    post.setRating(pair.getValue());
                    break;
                }
                case "score": {
                    post.setScore(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "preview_file_url": {
                    post.setPreview_url("https://danbooru.donmai.us" + pair.getValue());
                    break;
                }
                case "tag_string": {
                    post.setTags(pair.getValue());
                    break;
                }
                case "file_url": {
                    post.setSample_url("https://danbooru.donmai.us" + pair.getValue());
                    break;
                }
                case "large_file_url": {
                    post.setFile_url("https://danbooru.donmai.us" + pair.getValue());
                    break;
                }
                case "source": {
                    post.setSource(pair.getValue());
                    break;
                }
                case "uploader_id": {
                    post.setCreator_id(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "last_commented_at": {
                    if (!"null".equals(pair.getValue())) {
                        post.setHas_comments(true);

                    } else {
                        post.setHas_comments(false);
                    }
                    break;
                }
                case "created_at": {
                    post.setCreate_Time(pair.getValue());
                    break;
                }

            }
        }
        //after all check comments flag
        if (post.isHas_comments()) {
            //and if true - setup comments url.
            post.setComments_url(instance.getCommentsByPostIdRequest(post.getId()));
        }
        return post;
    }

}
