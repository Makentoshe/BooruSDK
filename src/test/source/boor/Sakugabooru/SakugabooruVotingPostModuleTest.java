package test.source.boor.Sakugabooru;

import engine.BooruEngineException;
import module.LoginModule;
import module.VotingPostModule;
import org.junit.Test;
import source.boor.AbstractBoor;
import source.boor.Sakugabooru;
import test.source.TestHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SakugabooruVotingPostModuleTest extends TestHelper{

    private final AbstractBoor boor;
    private static final int post_id = 38017;

    public SakugabooruVotingPostModuleTest() {
        boor = Sakugabooru.get();
    }

    @Test
    public void getVotePostRequest_Test() throws Exception {
        assertEquals("https://sakugabooru.com/post/vote.json", ((VotingPostModule)boor).getVotePostRequest(0));
    }

    @Test
    public void votePostFail_IllegalArgument_Test() throws Exception {
        ((LoginModule)boor).logIn(TestHelper.getLogin(), TestHelper.getPass());
        try {
            ((VotingPostModule)boor).votePost(post_id, "sas");
        } catch (BooruEngineException e) {
            assertEquals(IllegalArgumentException.class, e.getCause().getClass());
        }
        ((LoginModule)boor).logOff();
    }

    @Test
    public void votePostFail_WithoutUserData_Test() throws Exception {
        try {
            ((VotingPostModule)boor).votePost(post_id, "0");
        } catch (BooruEngineException e) {
            assertEquals(IllegalStateException.class, e.getCause().getClass());
        }
    }

    @Test
    public void votePostUpSuccess_Test() throws Exception {
        ((LoginModule)boor).logIn(getLogin(), getPass());
        assertTrue(((VotingPostModule)boor).votePost(post_id, "1"));
    }

}
