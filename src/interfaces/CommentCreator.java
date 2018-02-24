package interfaces;


import source.Post;

import java.util.Map;

/**
 * Interface with the single method for remote <tt>Comment</tt> creating.
 */
public interface CommentCreator {

    /**
     * Here a <tt>Comment</tt> remote constructor implementation.
     *
     * @param attributes map of all comment attributes
     * @return <tt>Comment</tt> entity with a setted data.
     */
    Post newCommentInstance(final Map<String, String> attributes);
}
