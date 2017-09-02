package test.source.boor.Rule34;

import engine.BooruEngineException;
import org.junit.Test;
import source.boor.Rule34;
import source.boor.Safebooru;
import source.еnum.Rating;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class Rule34UploadModuleTest {

    private final Rule34 boor;

    public Rule34UploadModuleTest() {
        boor = Rule34.get();
    }

    @Test
    public void getVotePostRequest_Test() throws Exception {
        assertEquals("https://rule34.xxx/index.php?page=post&s=add", boor.getCreatePostRequest());
    }

    @Test
    public void createPostFail_WithoutUserData_Test() throws Exception {
        System.out.println(boor.getLoginData());
        try {
            boor.createPost(new File(""), "tags", null, null, Rating.SAFE, null);
        } catch (BooruEngineException e) {
            assertEquals(IllegalStateException.class, e.getCause().getClass());
        }
    }
}
