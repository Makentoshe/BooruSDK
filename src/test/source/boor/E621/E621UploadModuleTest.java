package test.source.boor.E621;

import engine.BooruEngineException;
import org.junit.Test;
import source.boor.E621;
import source.еnum.Rating;
import test.source.TestHelper;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class E621UploadModuleTest extends TestHelper{

    private final E621 boor;

    public E621UploadModuleTest() {
        boor = E621.get();
    }

    @Test
    public void getVotePostRequest_Test() throws Exception {
        assertEquals("https://e621.net/post/create", boor.getCreatePostRequest());
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
