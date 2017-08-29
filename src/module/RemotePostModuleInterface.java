package module;

import source.Post;

import java.util.Map;

/**
 * Interface with the single method for remote <tt>Post</tt> creating.
 */
public interface RemotePostModuleInterface {

    /**
     * Here you can create Post remote constructor.
     * The main feature is reliably indicate from which *boor this post.
     *
     * @param attributes map of all post attributes
     * @return Post entity with setted data.
     */
    Post newPostInstance(final Map<String, String> attributes);
}
