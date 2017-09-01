package test.source.boor.Rule34;

import org.junit.Before;
import org.junit.Test;
import source.Post;
import source.boor.Rule34;
import source.еnum.Boor;
import source.еnum.Rating;
import test.source.TestHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


//Create Rule34 post with default Post constructor in Rule34.
public class Rule34RemotePostConstructorTest {

    private static Post post;

    @Before
    public void setUp() throws Exception {
        if (post != null) return;
        post = Rule34.get().newPostInstance(TestHelper.getDataFromBoorBasic(Rule34.get(), 2421106));
    }

    @Test
    public void getSourceBoor() throws Exception {
        assertEquals(post.getSourceBoor(), Boor.Rule34.toString());
    }

    @Test
    public void getId() throws Exception {
        assertEquals("Id ", 2421106, post.getId());
    }

    @Test
    public void getMd5() throws Exception {
        assertEquals("Md5 ","23b64b698780463545dac889883d83c0", post.getMd5());
    }

    @Test
    public void getRating() throws Exception {
        assertEquals("Rating ",Rating.EXPLICIT, post.getRating());
    }

    @Test
    public void getSource() throws Exception {
        assertEquals("Source ","https://i.pximg.net/img-original/img/2016/11/11/20/21/32/59734064_p0.png", post.getSource());
    }

    @Test
    public void getPreview() throws Exception {
        assertEquals("Preview ","https://rule34.xxx/thumbnails/2231/thumbnail_06d1a7a474d4a44147a7ac27d1515c26e364cda7.jpg", post.getPreview_url());
    }

    @Test
    public void getTags() throws Exception {
        assertTrue("Tags ", post.getTags().contains("hatsune_miku"));
    }

    @Test
    public void getSample() throws Exception {
        assertEquals("Sample ","https://img.rule34.xxx/samples/2231/sample_06d1a7a474d4a44147a7ac27d1515c26e364cda7.jpg", post.getSample_url());
    }

    @Test
    public void getFile() throws Exception {
        assertEquals("File ", "https://img.rule34.xxx/images/2231/06d1a7a474d4a44147a7ac27d1515c26e364cda7.png", post.getFile_url());
    }

    @Test
    public void getCreatorId() throws Exception {
        assertEquals("Creator_id ", 48613, post.getCreator_id());
    }

    @Test
    public void getComment() throws Exception {
        assertTrue("Has comments", post.isHas_comments());
    }

    @Test
    public void getHasComments() throws Exception {
        assertEquals("Comment url", "https://rule34.xxx/index.php?page=dapi&q=index&s=comment&post_id=2421106", post.getComments_url());
    }

    @Test
    public void getCreateTime() throws Exception {
        assertEquals("Create Time", "Thu Jun 29 22:12:03 +0200 2017", post.getCreate_time());
    }

}
