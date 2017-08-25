package module;

import engine.BooruEngineException;

/**
 * Interface with the methods for a voting posts on any *boor.
 */
public interface VotingModule {

    /**
     * Voting a post. To perform this action need any identification data,
     * so it will be okay, if you use <tt>LoginModule</tt>.
     *
     * @param id post id.
     * @param action any action - it can be score quantity or action "up".
     * @return true if success.
     * @throws BooruEngineException when something go wrong. Use <tt>getCause</tt> to see more details.
     */
    boolean votePost(final int id, final String action) throws BooruEngineException;


    /**
     * Create request for voting post by post id.
     *
     * @return constructed request to server.
     */
    String getVotePostRequest();

}
