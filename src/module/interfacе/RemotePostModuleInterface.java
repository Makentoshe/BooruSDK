package module.interfacе;

import source.Post;

import java.util.Map;

/**
 * Interface with the single method for remote <tt>Post</tt> creating.
 */
public interface RemotePostModuleInterface {

    /**
     * Here a <tt>Post</tt> remote constructor implementation.
     * The main feature is reliably indicate from which *boor this post.
     *
     * @param attributes map of all post attributes
     * @return Post entity with a setted data.
     */
    Post newPostInstance(final Map<String, String> attributes);
}
