package test.source.boor.Sakugabooru;

import engine.BooruEngineException;
import module.UploadModule;
import org.junit.Test;
import source.boor.Sakugabooru;
import source.boor.Yandere;
import source.еnum.Rating;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class SakugabooruUploadModuleTest {

    private final UploadModule boor;

    public SakugabooruUploadModuleTest() {
        boor = Sakugabooru.get();
    }

    @Test
    public void getCreatePostRequest_Test() throws Exception {
        assertEquals("https://sakugabooru.com/post/create", boor.getCreatePostRequest());
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
