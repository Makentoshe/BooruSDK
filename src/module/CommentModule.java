package module;


import source.Post;

import java.util.Map;

/**
 * Interface with the single method for remote <tt>Comment</tt> creating.
 */
public interface CommentModule {

    /**
     * Here you can create <tt>Comment</tt> remote constructor.
     *
     * @param attributes map of all comment attributes
     * @return <tt>Comment</tt> entity with setted data.
     */
    Post newCommentInstance(final Map<String, String> attributes);
}
