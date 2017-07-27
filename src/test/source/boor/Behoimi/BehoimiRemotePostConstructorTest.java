package test.source.boor.Behoimi;

import org.junit.Before;
import org.junit.Test;
import source.Post;
import source.boor.Behoimi;
import source.еnum.Boor;
import source.еnum.Rating;
import test.source.PostTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BehoimiRemotePostConstructorTest {

    private static Post post;

    public BehoimiRemotePostConstructorTest() throws Exception {
        if (post != null) return;
        post = Behoimi.get().newPostInstance(PostTest.getDataFromBoorAdvanced(Behoimi.get(), 633053));
    }

    @Before
    public void setUp() throws Exception {
        post = Behoimi.get().newPostInstance(PostTest.getDataFromBoorAdvanced(Behoimi.get(), 633053));
    }

    @Test
    public void getSourceBoor() throws Exception {
        assertEquals("Boor source ", post.getSourceBoor(), Boor.Behoimi.toString());
    }

    @Test
    public void getId() throws Exception {
        assertEquals("Id ", 633053, post.getId());
    }

    @Test
    public void getMd5() throws Exception {
        assertEquals("Md5 ", "7cb1161617a3a9d9f56d7772cde0f090", post.getMd5());
    }

    @Test
    public void getRating() throws Exception {
        assertEquals(Rating.SAFE, post.getRating());
    }

    @Test
    public void getSource() throws Exception {
        assertEquals("", post.getSource());
    }

    @Test
    public void getPreview() throws Exception {
        assertEquals("http://behoimi.org/data/preview/7c/b1/7cb1161617a3a9d9f56d7772cde0f090.jpg", post.getPreview_url());
    }

    @Test
    public void getTags() throws Exception {
        assertTrue(post.getTags().contains("hatsune_miku"));
    }

    @Test
    public void getSample() throws Exception {
        assertEquals("http://behoimi.org/data/7c/b1/7cb1161617a3a9d9f56d7772cde0f090.jpg", post.getSample_url());
    }

    @Test
    public void getFile() throws Exception {
        assertEquals("http://behoimi.org/data/7c/b1/7cb1161617a3a9d9f56d7772cde0f090.jpg", post.getFile_url());
    }

    @Test
    public void getCreatorId() throws Exception {
        assertEquals("Creator_id ", 1, post.getCreator_id());
    }

    @Test
    public void getHasComment() throws Exception {
        assertFalse("Has comments", post.isHas_comments());
    }

    @Test
    public void getComment() throws Exception {
        assertEquals("Comment url", null, post.getComments_url());
    }

    @Test
    public void getCreateTime() throws Exception {
        assertTrue("Create Time", post.getCreate_time().contains("\"json_class\":\"Time\"") &&
                        post.getCreate_time().contains("\"s\":1475072859") &&
                        post.getCreate_time().contains("\"n\":52387000"));
    }
}
