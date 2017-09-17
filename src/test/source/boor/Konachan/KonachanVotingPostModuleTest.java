package test.source.boor.Konachan;

import engine.BooruEngineException;
import module.LoginModule;
import module.VotingPostModule;
import org.junit.Test;
import source.boor.Konachan;
import test.source.TestHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class KonachanVotingPostModuleTest {

    private final VotingPostModule voteBoor;
    private final LoginModule loginBoor;

    public KonachanVotingPostModuleTest() {
        voteBoor = Konachan.get();
        loginBoor = Konachan.get();
    }

    @Test
    public void getVotePostRequest_Test() throws Exception {
        assertEquals("https://konachan.com/post/vote.json", voteBoor.getVotePostRequest(0));
    }

    @Test
    public void votePostFail_IllegalArgument_Test() throws Exception {
        loginBoor.logIn(TestHelper.getLogin(), TestHelper.getPass());
        try {
            voteBoor.votePost(248044, "sas");
        } catch (BooruEngineException e) {
            assertEquals(IllegalArgumentException.class, e.getCause().getClass());
        }
        loginBoor.logOff();
    }

    @Test
    public void votePostFail_WithoutUserData_Test() throws Exception {
        try {
            voteBoor.votePost(248044, "0");
        } catch (BooruEngineException e) {
            assertEquals(IllegalStateException.class, e.getCause().getClass());
        }
    }

    @Test
    public void votePostUpSuccess_Test() throws Exception {
        loginBoor.logIn(TestHelper.getLogin(), TestHelper.getPass());
        assertTrue(voteBoor.votePost(248044, "3"));
    }

}
