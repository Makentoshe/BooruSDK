package test.source.boor.Rule34;

import engine.BooruEngineException;
import org.junit.Test;
import source.boor.Rule34;
import source.boor.Safebooru;
import test.source.TestHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Rule34VotingModuleTest {

    private final Rule34 boor;

    public Rule34VotingModuleTest() {
        boor = Rule34.get();
    }

    @Test
    public void getVotePostRequest_Test() throws Exception {
        assertEquals("https://rule34.xxx/index.php?page=post&s=vote", boor.getVotePostRequest());
    }

    @Test
    public void votePostFail_UnsupportedOperation_Test() throws Exception {
        try {
            boor.votePost(2489243, "sas");
        } catch (BooruEngineException e) {
            assertEquals(UnsupportedOperationException.class, e.getCause().getClass());
        }
    }

    @Test
    public void votePostFail_WithoutUserData_Test() throws Exception {
        try {
            boor.votePost(2489243, "up");
        } catch (BooruEngineException e) {
            assertEquals(IllegalStateException.class, e.getCause().getClass());
        }
    }

    @Test
    public void votePostUpSuccess_Test() throws Exception {
        boor.logIn(TestHelper.getLogin(), TestHelper.getPass());
        assertTrue(boor.votePost(2489243, "up"));
    }

}
