package test.source.boor.Gelbooru;

import org.junit.Test;
import source.Post;
import source.boor.Gelbooru;
import source.еnum.Boor;
import source.еnum.Rating;
import test.source.TestHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

//Create Gelbooru post with default Post constructor in Gelbooru.
public class GelbooruRemotePostConstructorTest {

    private static Post post;

    public GelbooruRemotePostConstructorTest() throws Exception {
        if (post != null) return;
        post = Gelbooru.get().newPostInstance(TestHelper.getPostFromBoor(Gelbooru.get(), 3785972));
    }

    @Test
    public void getSourceBoor() throws Exception {
        assertEquals(post.getSourceBoor(), Boor.Gelbooru.toString());
    }

    @Test
    public void getId() throws Exception {
        assertEquals(3785972, post.getId());
    }

    @Test
    public void getMd5() throws Exception {
        assertEquals("cf589d62afe26ede34c2f4fa802ff70c", post.getMd5());
    }

    @Test
    public void getRating() throws Exception {
        assertEquals(Rating.SAFE, post.getRating());
    }

    @Test
    public void getSource() throws Exception {
        assertEquals("https://i.pximg.net/img-original/img/2017/05/11/19/53/24/62849670_p0.jpg", post.getSource());
    }

    @Test
    public void getPreview() throws Exception {
        assertTrue(post.getPreview_url().contains("gelbooru.com/thumbnails/cf/58/thumbnail_cf589d62afe26ede34c2f4fa802ff70c.jpg"));
    }

    @Test
    public void getTags() throws Exception {
        assertTrue(post.getTags().contains("hatsune_miku"));
    }

    @Test
    public void getSample() throws Exception {
        assertTrue(post.getSample_url().contains("gelbooru.com/samples/cf/58/sample_cf589d62afe26ede34c2f4fa802ff70c.jpg"));
    }

    @Test
    public void getFile() throws Exception {
        assertTrue(post.getFile_url().contains("gelbooru.com/images/cf/58/cf589d62afe26ede34c2f4fa802ff70c.jpg"));
    }

    @Test
    public void getCreatorId() throws Exception {
        assertEquals("Creator_id ", 6498, post.getCreator_id());
    }

    @Test
    public void getComment() throws Exception {
        assertFalse("Has comments", post.isHas_comments());
    }

    @Test
    public void getHasComments() throws Exception {
        assertEquals("Comment url", null, post.getComments_url());
    }

    @Test
    public void getCreateTime() throws Exception {
        assertEquals("Create Time", "Mon Jul 17 09:30:20 -0500 2017", post.getCreate_time());
    }
}
