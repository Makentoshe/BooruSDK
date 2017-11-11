package source;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Simple class which can describe comment for post from all boors.
 * <p>
 * This instance getting as is. That means, that you <strong>can't</strong> modify fields after data defined.
 * Of course, when field is undefined you can setup value.
 * If you want change one or more fields - you must create new instance.
 * When data is defined - all setter calls will be ignored.
 * <p>
 * This class have default constructor for boors, and it isn't guaranteed that it will work correctly.
 * If your attributes has specific names - you must create Post entity
 * in special method {@code newCommentInstance(Hashmap&lt;String, String&gt;)} in you boor.
 * Also if you want add more functionality - just extend this class.
 * <p>
 * <p>Support attributes:
 * <p>    <i>id</i> - comment id in the boor.
 * <p>    <i>creator_id/creator-id</i> - id, who's create this comment.
 * <p>    <i>creator/creator_name/creator-name</i> - name, who's create this comment.
 * <p>    <i>body</i> - comment as is without any modifications.
 * <p>    <i>created_at/created-at</i> - in what time comment was create. Each boor have own date-time format.
 * <p>    <i>post_id/post-id</i> - for what post comment was created.
 *
 * @see Post
 */
public class Comment implements Serializable {
    private int id = Integer.MIN_VALUE;

    private String created_at = null;

    private int post_id = Integer.MIN_VALUE;

    private int creator_id = Integer.MIN_VALUE;

    private String body = null;

    private String creator_name = null;

    /**
     * Default constructor.
     *
     * @param attributes map with all attributes.
     */
    public Comment(HashMap<String, String> attributes){
        constructor(attributes);
    }

    private void constructor(HashMap<String, String> attributes){
        Set<Map.Entry<String, String>> entrySet = attributes.entrySet();
        for (Map.Entry<String, String> pair : entrySet) {
            switch (pair.getKey()){
                case "id" :{
                    setId(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "creator-id":
                case "creator_id" :{
                    setCreator_id(Integer.parseInt(pair.getValue()));
                    break;
                }
                case "creator":
                case "creator-name":
                case "creator_name":{
                    setCreator_name(pair.getValue());
                    break;
                }
                case "body" :{
                    setBody(pair.getValue());
                    break;
                }
                case "created-at":
                case "created_at" :{
                    setCreated_at(pair.getValue());
                    break;
                }
                case "post-id":
                case "post_id":{
                    setPost_id(Integer.parseInt(pair.getValue()));
                    break;
                }
            }
        }
    }

    /**
     * Getting comment id. If data will not defined - method return {@code Integer.MIN_VALUE}.
     */
    public int getId() {
        return id;
    }

    /**
     * Method will be success only if data was not defined yet.
     */
    public void setId(int id) {
        if (this.id != Integer.MIN_VALUE) return;
        this.id = id;
    }

    /**
     * Getting the creating time. If data will not defined - method return {@code null}.
     */
    public String getCreated_at() {
        return created_at;
    }

    /**
     * Method will be success only if data was not defined yet.
     */
    public void setCreated_at(String created_at) {
        if (this.created_at != null) return;
        this.created_at = created_at;
    }

    /**
     * Getting the post id. If data will not defined - method return {@code Integer.MIN_VALUE}.
     */
    public int getPost_id() {
        return post_id;
    }

    /**
     * Method will be success only if data was not defined yet.
     */
    public void setPost_id(int post_id) {
        if (this.post_id != Integer.MIN_VALUE) return;
        this.post_id = post_id;
    }

    /**
     * Getting the creator id. If data will not defined - method return {@code Integer.MIN_VALUE}.
     */
    public int getCreator_id() {
        return creator_id;
    }

    /**
     * Method will be success only if data was not defined yet.
     */
    public void setCreator_id(int creator_id) {
        if (this.creator_id != Integer.MIN_VALUE) return;
        this.creator_id = creator_id;
    }

    /**
     * Getting the comment body. If data will not defined - method return {@code null}.
     */
    public String getBody() {
        return body;
    }

    /**
     * Method will be success only if data was not defined yet.
     */
    public void setBody(String body) {
        if (this.body != null) return;
        this.body = body;
    }

    /**
     * Getting the creator name. If data will not defined - method return {@code null}.
     */
    public String getCreator_name() {
        return creator_name;
    }

    /**
     * Method will be success only if data was not defined yet.
     */
    public void setCreator_name(String creator_name) {
        if (this.creator_name != null) return;
        this.creator_name = creator_name;
    }
}
