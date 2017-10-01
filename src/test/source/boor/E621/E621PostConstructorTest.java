package test.source.boor.E621;

import org.junit.Test;
import source.Post;
import source.boor.E621;
import source.еnum.Boor;
import source.еnum.Rating;
import test.source.TestHelper;

import static org.junit.Assert.*;


public class E621PostConstructorTest {

    private static Post post;

    public E621PostConstructorTest() throws Exception {
        if (post != null) return;
        post = new Post(TestHelper.getDataFromBoorAdvanced(E621.get(), 1263892), E621.get());
    }

    @Test
    public void getSourceBoor() throws Exception {
        assertEquals("Boor source ", post.getSourceBoor(), Boor.E621.toString());
    }

    @Test
    public void getId() throws Exception {
        assertEquals("Id ", 1263892, post.getId());
    }

    @Test
    public void getMd5() throws Exception {
        assertEquals("Md5 ", "165b0269a416acb18243bb851249b9b3", post.getMd5());
    }

    @Test
    public void getRating() throws Exception {
        assertEquals(Rating.QUESTIONABLE, post.getRating());
    }

    @Test
    public void getSource() throws Exception {
        assertEquals("http://iwillbuckyou.tumblr.com/post/152616940313", post.getSource());
    }

    @Test
    public void getPreview() throws Exception {
        assertEquals("https://static1.e621.net/data/preview/16/5b/165b0269a416acb18243bb851249b9b3.jpg", post.getPreview_url());
    }

    @Test
    public void getTags() throws Exception {
        assertTrue(post.getTags().contains("underwear"));
    }

    @Test
    public void getSample() throws Exception {
        assertEquals("https://static1.e621.net/data/sample/16/5b/165b0269a416acb18243bb851249b9b3.jpg", post.getSample_url());
    }

    @Test
    public void getFile() throws Exception {
        assertEquals("https://static1.e621.net/data/16/5b/165b0269a416acb18243bb851249b9b3.png", post.getFile_url());
    }

    @Test
    public void getCreatorId() throws Exception {
        assertEquals("Creator_id ", 33842, post.getCreator_id());
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
        assertEquals("Create Time", "{\"json_class\":\"Time\",\"s\":1498954819,\"n\":842844000}", post.getCreate_time());
    }
}
