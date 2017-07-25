package test.source.boor.Safebooru;

import org.junit.Before;
import org.junit.Test;
import source.Post;
import source.boor.Rule34;
import source.boor.Safebooru;
import source.еnum.Boor;
import source.еnum.Rating;
import test.source.PostTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class SafebooruRemotePostConstructorTest {

    private Post post;

    @Before
    public void setUp() throws Exception {
        post = Safebooru.get().newPostInstance(PostTest.getDataFromBoorBasic(Safebooru.get(), 2278871));
    }

    @Test
    public void getSourceBoor() throws Exception {
        assertEquals(post.getSourceBoor(), Boor.Safebooru.toString());
    }

    @Test
    public void getId() throws Exception {
        assertEquals("Id ", 2278871, post.getId());
    }

    @Test
    public void getMd5() throws Exception {
        assertEquals("Md5 ", "b5e3bdd52dbcbf8f8715bec584ee87e8", post.getMd5());
    }

    @Test
    public void getRating() throws Exception {
        assertEquals("Rating ", Rating.SAFE, post.getRating());
    }

    @Test
    public void getSource() throws Exception {
        assertEquals("Source ", "", post.getSource());
    }

    @Test
    public void getPreview() throws Exception {
        assertEquals("Preview ", "https://safebooru.org/thumbnails/2187/thumbnail_b5e3bdd52dbcbf8f8715bec584ee87e8.jpeg", post.getPreview_url());
    }

    @Test
    public void getTags() throws Exception {
        assertTrue("Tags ", post.getTags().contains("hatsune_miku"));
    }

    @Test
    public void getSample() throws Exception {
        assertEquals("Sample ", "https://safebooru.org/images/2187/b5e3bdd52dbcbf8f8715bec584ee87e8.jpeg", post.getSample_url());
    }

    @Test
    public void getFile() throws Exception {
        assertEquals("File ", "https://safebooru.org/images/2187/b5e3bdd52dbcbf8f8715bec584ee87e8.jpeg", post.getFile_url());
    }

    @Test
    public void getCreatorId() throws Exception {
        assertEquals("Creator_id ", 8970, post.getCreator_id());
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
        assertEquals("Create Time", "Mon Jul 17 09:06:24 +0200 2017", post.getCreate_time());
    }


}
