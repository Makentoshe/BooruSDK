package source.interfaces;

import source.Post;

import java.util.Map;

/**
 * Interface with the single method for remote <tt>Post</tt> creating.
 */
public interface PostCreator {

    /**
     * Here a <tt>Post</tt> remote constructor implementation.
     * The main feature is reliably indicate from which *boor this post.
     * <p>
     * If *boor have specified attribute names
     * there will be Ok to realise <code>Post</code> construction here.
     *
     * @param attributes map of all post attributes
     * @return Post entity with a setted data.
     */
    Post newPostInstance(final Map<String, String> attributes);
}
