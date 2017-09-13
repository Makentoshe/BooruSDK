package test.source.boor.Sakugabooru;

import engine.BooruEngineException;
import module.interfacе.LoginModuleInterface;
import module.interfacе.VotingModuleInterface;
import org.junit.Test;
import source.boor.AbstractBoor;
import source.boor.Sakugabooru;
import source.boor.Yandere;
import test.source.TestHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SakugabooruVotingModuleTest extends TestHelper{

    private final AbstractBoor boor;
    private static final int post_id = 38017;

    public SakugabooruVotingModuleTest() {
        boor = Sakugabooru.get();
    }

    @Test
    public void getVotePostRequest_Test() throws Exception {
        assertEquals("https://sakugabooru.com/post/vote.json", ((VotingModuleInterface)boor).getVotePostRequest());
    }

    @Test
    public void votePostFail_IllegalArgument_Test() throws Exception {
        ((LoginModuleInterface)boor).logIn(TestHelper.getLogin(), TestHelper.getPass());
        try {
            ((VotingModuleInterface)boor).votePost(post_id, "sas");
        } catch (BooruEngineException e) {
            assertEquals(IllegalArgumentException.class, e.getCause().getClass());
        }
        ((LoginModuleInterface)boor).logOff();
    }

    @Test
    public void votePostFail_WithoutUserData_Test() throws Exception {
        try {
            ((VotingModuleInterface)boor).votePost(post_id, "0");
        } catch (BooruEngineException e) {
            assertEquals(IllegalStateException.class, e.getCause().getClass());
        }
    }

    @Test
    public void votePostUpSuccess_Test() throws Exception {
        ((LoginModuleInterface)boor).logIn(getLogin(), getPass());
        assertTrue(((VotingModuleInterface)boor).votePost(post_id, "1"));
    }

}
