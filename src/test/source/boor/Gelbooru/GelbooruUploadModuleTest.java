package test.source.boor.Gelbooru;

import engine.BooruEngineException;
import org.junit.Test;
import source.boor.Gelbooru;
import source.еnum.Rating;

import java.io.File;

import static org.junit.Assert.*;

public class GelbooruUploadModuleTest {

    private final Gelbooru boor;

    public GelbooruUploadModuleTest() {
        boor = Gelbooru.get();
    }

    @Test
    public void getVotePostRequest_Test() throws Exception {
        assertEquals("https://gelbooru.com/index.php?page=post&s=add", boor.getCreatePostRequest());
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
