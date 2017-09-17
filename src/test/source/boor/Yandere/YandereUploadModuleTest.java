package test.source.boor.Yandere;

import engine.BooruEngineException;
import module.UploadModule;
import org.junit.Test;
import source.boor.Yandere;
import source.еnum.Rating;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class YandereUploadModuleTest {

    private final UploadModule boor;

    public YandereUploadModuleTest() {
        boor = Yandere.get();
    }

    @Test
    public void getCreatePostRequest_Test() throws Exception {
        assertEquals("https://yande.re/post/create.json", boor.getPostRequest());
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
