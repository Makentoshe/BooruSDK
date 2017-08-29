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
     *
     * @param id post id.
     * @param body comment body.
     * @param postAsAnon use {@code true} for anonymously posting. Not all *boors support this param.
     * @param bumpPost ues {@code true} for bump up post. Not all *boors support this param.
     * @return {@code true} if success.
     * @throws BooruEngineException if something go wrong.
     * Use <tt>getCause</tt> to see more details.
     */
    boolean commentPost(final int id, final String body, final boolean postAsAnon, final boolean bumpPost) throws BooruEngineException;

}
