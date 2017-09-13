package test.source.boor.Yandere;

import engine.BooruEngineException;
import module.LoginModule;
import module.VotingModule;
import org.junit.Test;
import source.boor.Yandere;
import test.source.TestHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class YandereVotingModuleTest {

    private final VotingModule voteBoor;
    private final LoginModule loginBoor;
    private static final int post_id = 409015;

    public YandereVotingModuleTest() {
        voteBoor = Yandere.get();
        loginBoor = Yandere.get();
    }

    @Test
    public void getVotePostRequest_Test() throws Exception {
        assertEquals("https://yande.re/post/vote.json", voteBoor.getVotePostRequest());
    }

    @Test
    public void votePostFail_IllegalArgument_Test() throws Exception {
        loginBoor.logIn(TestHelper.getLogin(), TestHelper.getPass());
        try {
            voteBoor.votePost(post_id, "sas");
        } catch (BooruEngineException e) {
            assertEquals(IllegalArgumentException.class, e.getCause().getClass());
        }
        loginBoor.logOff();
    }

    @Test
    public void votePostFail_WithoutUserData_Test() throws Exception {
        try {
            voteBoor.votePost(post_id, "0");
        } catch (BooruEngineException e) {
            assertEquals(IllegalStateException.class, e.getCause().getClass());
        }
    }

    @Test
    public void votePostUpSuccess_Test() throws Exception {
        loginBoor.logIn(TestHelper.getLogin(), TestHelper.getPass());
        assertTrue(voteBoor.votePost(post_id, "3"));
    }

}
