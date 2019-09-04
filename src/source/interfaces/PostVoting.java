package source.interfaces;

import engine.BooruEngineException;
import engine.connector.HttpsConnection;

/**
 * Interface with the method for a voting posts on any *boor.
 */
public interface PostVoting {

    /**
     * Voting a post. To perform this action need any identification data,
     * so it will be okay, if you use <tt>LoginModule</tt>.
     *
     * @param id post id.
     * @param action any action - it can be score quantity(from 0 to 3) or action "up"/"down".
     * @return connection with post-request response.
     * @throws BooruEngineException when something go wrong. Use <tt>getCause</tt> to see more details.
     */
    HttpsConnection votePost(final int id, final String action) throws BooruEngineException;

    /**
     * Get request for voting post.
     *
     * @param post_id post id.
     * @return constructed request to server.
     */
    String getVotePostRequest(int post_id);
}
