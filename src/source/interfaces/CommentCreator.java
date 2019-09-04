package source.interfaces;


import source.Post;

import java.util.Map;

/**
 * Interface with the single method for creating a <tt>Comment</tt> in the boor instance.
 */
public interface CommentCreator {

    /**
     * Here is a <tt>Comment</tt> remote constructor implementation.
     *
     * @param attributes map of all comment attributes
     * @return <tt>Comment</tt> entity with a setted data.
     */
    Post newCommentInstance(final Map<String, String> attributes);
}
