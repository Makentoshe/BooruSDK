package test.source.boor.E621;

import engine.BooruEngineException;
import org.junit.Test;
import source.boor.E621;
import source.boor.Gelbooru;
import test.source.TestHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class E621VotingModuleTest extends TestHelper{

    private final E621 boor;
    private final int post_id = 1302571;

    public E621VotingModuleTest() {
        boor = E621.get();
    }

    @Test
    public void getVotePostRequest_Test() throws Exception {
        assertEquals("https://e621.net/post/vote.json", boor.getVotePostRequest());
    }

    @Test
    public void votePostFail_IllegalArgument_Test() throws Exception {
        try {
            boor.votePost(post_id, "sas");
        } catch (BooruEngineException e) {
            assertEquals(IllegalArgumentException.class, e.getCause().getClass());
        }
    }

    @Test
    public void votePostFail_WithoutUserData_Test() throws Exception {
        try {
            boor.votePost(post_id, "1");
        } catch (BooruEngineException e) {
            assertEquals(IllegalStateException.class, e.getCause().getClass());
        }
    }

    @Test
    public void votePostSuccess_Test() throws Exception {
        boor.logIn(getLogin(), getPass());
        assertTrue(boor.votePost(post_id, "1"));
    }

}
