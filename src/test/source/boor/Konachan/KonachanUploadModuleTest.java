package test.source.boor.Konachan;

import engine.BooruEngineException;
import module.interfacе.LoginModuleInterface;
import module.interfacе.UploadModuleInterface;
import org.junit.Test;
import source.boor.Konachan;
import source.boor.Rule34;
import source.еnum.Rating;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class KonachanUploadModuleTest {

    private final UploadModuleInterface boor;

    public KonachanUploadModuleTest() {
        boor = Konachan.get();
    }

    @Test
    public void getCreatePostRequest_Test() throws Exception {
        assertEquals("https://konachan.com/post/create.json", boor.getCreatePostRequest());
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
