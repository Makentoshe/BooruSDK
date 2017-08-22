package module;

import engine.BooruEngineException;

/**
 * Created by Makentoshe on 22.08.2017.
 */
public interface VotingModule {

    void votePost(final int id, final String action) throws BooruEngineException;

}
