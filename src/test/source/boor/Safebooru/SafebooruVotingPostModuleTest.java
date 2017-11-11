package test.source.boor.Safebooru;

import engine.BooruEngineException;
import org.junit.Test;
import source.boor.Gelbooru;
import source.boor.Safebooru;
import test.source.TestHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SafebooruVotingPostModuleTest {

    private final Safebooru boor;

    public SafebooruVotingPostModuleTest() {
        boor = Safebooru.get();
    }

    @Test
    public void getVotePostRequest_Test() throws Exception {
        assertEquals("https://safebooru.org/index.php?page=post&s=vote", boor.getVotePostRequest(0));
    }

    @Test
    public void votePostFail_IllegalArgument_Test() throws Exception {
        try {
            boor.votePost(2318234, "sas");
        } catch (BooruEngineException e) {
            assertEquals(IllegalArgumentException.class, e.getCause().getClass());
        }
    }

    @Test
    public void votePostFail_WithoutUserData_Test() throws Exception {
        try {
            boor.votePost(2318234, "up");
        } catch (BooruEngineException e) {
            assertEquals(IllegalStateException.class, e.getCause().getClass());
        }
    }

    @Test
    public void votePostUpSuccess_Test() throws Exception {
        boor.logIn(TestHelper.getLogin(), TestHelper.getPass());
        assertTrue(boor.votePost(2318234, "up").getResponseCode()==200);
    }

    @Test
    public void votePostDownSuccess_Test() throws Exception {
        boor.logIn(TestHelper.getLogin(), TestHelper.getPass());
        assertTrue(boor.votePost(2318234, "down").getResponseCode()==200);
    }

}
