package test.source.boor.Safebooru;

import engine.BooruEngineException;
import org.junit.Test;
import source.boor.Safebooru;
import source.еnum.Rating;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class SafebooruUploadModuleTest {

    private final Safebooru boor;

    public SafebooruUploadModuleTest() {
        boor = Safebooru.get();
    }

    @Test
    public void getVotePostRequest_Test() throws Exception {
        assertEquals("https://safebooru.org/index.php?page=post&s=add", boor.getCreatePostRequest());
    }

    @Test
    public void createPostFail_WithoutUserData_Test() throws Exception {
        try {
            boor.createPost(new File(""), "tags", null, null, Rating.SAFE, null);
        } catch (BooruEngineException e) {
            assertEquals(IllegalStateException.class, e.getCause().getClass());
        }
    }
}
