package test.source.boor.Sakugabooru;

import org.junit.Before;
import org.junit.Test;
import source.Post;
import source.boor.Sakugabooru;
import source.еnum.Boor;
import source.еnum.Rating;
import test.source.TestHelper;

import static org.junit.Assert.*;

public class SakugabooruPostConstructor {

    private static Post post;

    @Before
    public void setUp() throws Exception {
        if (post != null) return;
        post = new Post(TestHelper.getDataFromBoorAdvanced(Sakugabooru.get(), 1), Sakugabooru.get());
        System.out.println(post);
    }

    @Test
    public void getSourceBoor() throws Exception {
        assertEquals("Boor source ", post.getSourceBoor(), Boor.Sakugabooru.toString());
    }

    @Test
    public void getId() throws Exception {
        assertEquals("Id ", 1, post.getId());
    }

    @Test
    public void getMd5() throws Exception {
        assertEquals("Md5 ", "b8ced04deaa3b05076acf64d0fceac93", post.getMd5());
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
        assertEquals("https://sakugabooru.com/data/preview/b8ced04deaa3b05076acf64d0fceac93.jpg", post.getPreview_url());
    }

    @Test
    public void getTags() throws Exception {
        assertTrue(post.getTags().contains("test"));
    }

    @Test
    public void getSample() throws Exception {
        assertEquals("https://sakugabooru.com/data/b8ced04deaa3b05076acf64d0fceac93.gif", post.getSample_url());
    }

    @Test
    public void getFile() throws Exception {
        assertEquals("https://sakugabooru.com/data/b8ced04deaa3b05076acf64d0fceac93.gif", post.getFile_url());
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
        assertEquals("Create Time", "1376667827", post.getCreate_time());
    }
}
