package module;

import source.Post;

import java.util.HashMap;

/**
 * Interface with the single method for remote <tt>Post</tt> creating.
 */
public interface PostModule {

    /**
     * Here you can create Post and create remote "constructor".
     * The main feature is reliably indicate from which *boor this post.
     *
     * @param attributes list of all post attributes
     * @return Post entity with setted data.
     */
    Post newPostInstance(HashMap<String, String> attributes);
}
