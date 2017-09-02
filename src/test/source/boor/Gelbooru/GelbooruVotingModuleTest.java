package test.source.boor.Gelbooru;

import engine.BooruEngineException;
import org.junit.Test;
import source.boor.Gelbooru;
import test.source.TestHelper;

import static org.junit.Assert.*;

public class GelbooruVotingModuleTest {

    private final Gelbooru boor;

    public GelbooruVotingModuleTest() {
        boor = Gelbooru.get();
    }

    @Test
    public void getVotePostRequest_Test() throws Exception {
        assertEquals("https://gelbooru.com/index.php?page=post&s=vote", boor.getVotePostRequest());
    }

    @Test
    public void votePostFail_IllegalArgument_Test() throws Exception {
        try {
            boor.votePost(3851398, "sas");
        } catch (BooruEngineException e) {
            assertEquals(IllegalArgumentException.class, e.getCause().getClass());
        }
    }

    @Test
    public void votePostFail_WithoutUserData_Test() throws Exception {
        try {
            boor.votePost(3851398, "up");
        } catch (BooruEngineException e) {
            assertEquals(IllegalStateException.class, e.getCause().getClass());
        }
    }

    @Test
    public void votePostSuccess_Test() throws Exception {
        boor.logIn(TestHelper.getLogin(), TestHelper.getPass());
        assertTrue(boor.votePost(3851398, "up"));
    }

}
