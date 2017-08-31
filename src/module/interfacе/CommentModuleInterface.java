package module.interfacе;

import engine.BooruEngineException;

/**
 * Interface with the basic methods for commenting *boor posts.
 * <p>
 * Require login information - so use <tt>LoginModuleInterface</tt> for getting access to user data.
 */
public interface CommentModuleInterface {

    /**
     * Create comment for post.
     * <p>
     * Note: <strong>Be careful: Not all *boors support "postAsAnon" or "bumpPost" param.</strong>
     *
     * @param id post id.
     * @param body comment body.
     * @param postAsAnon use {@code true} for anonymously posting.
     * @param bumpPost use {@code true} for bump up post.
     * @return {@code true} if success.
     * @throws BooruEngineException if something go wrong.
     * Use <tt>getCause</tt> to see more details.
     */
    boolean commentPost(final int id, final String body, final boolean postAsAnon, final boolean bumpPost) throws BooruEngineException;

    /**
     * Create request for creating comment.
     *
     * @param id post id.
     * @return constructed request to server.
     */
    String getCreateCommentRequest(final int id);
}
