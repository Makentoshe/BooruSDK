package test.source.boor.Konachan;

import engine.BooruEngineException;
import module.interfacе.LoginModuleInterface;
import module.interfacе.VotingModuleInterface;
import org.junit.Test;
import source.boor.Konachan;
import test.source.TestHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class KonachanVotingModuleTest {

    private final VotingModuleInterface voteBoor;
    private final LoginModuleInterface loginBoor;

    public KonachanVotingModuleTest() {
        voteBoor = Konachan.get();
        loginBoor = Konachan.get();
    }

    @Test
    public void getVotePostRequest_Test() throws Exception {
        assertEquals("https://konachan.com/post/vote.json", voteBoor.getVotePostRequest());
    }

    @Test
    public void votePostFail_IllegalArgument_Test() throws Exception {
        try {
            voteBoor.votePost(248044, "sas");
        } catch (BooruEngineException e) {
            assertEquals(IllegalArgumentException.class, e.getCause().getClass());
        }
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
