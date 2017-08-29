package module.interfacе;

import engine.BooruEngineException;

/**
 * Interface with the method for a voting posts on any *boor.
 */
public interface VotingModuleInterface {

    /**
     * Voting a post. To perform this action need any identification data,
     * so it will be okay, if you use <tt>LoginModuleInterface</tt>.
     *
     * @param id post id.
     * @param action any action - it can be score quantity(from 0 to 3) or action "up"/"down".
     * @return true if success.
     * @throws BooruEngineException when something go wrong. Use <tt>getCause</tt> to see more details.
     */
    boolean votePost(final int id, final String action) throws BooruEngineException;

    /**
     * Get request for voting post.
     *
     * @return constructed request to server.
     */
    String getVotePostRequest();
}
