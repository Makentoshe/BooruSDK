package test.source.boor.Konachan;

import org.junit.Test;
import source.Post;
import source.boor.Konachan;
import source.еnum.Boor;
import source.еnum.Rating;
import test.source.TestHelper;

import static org.junit.Assert.*;


public class KonachanPostConstructorTest {

    private static Post post;

    public KonachanPostConstructorTest() throws Exception{
        if (post != null) return;
        post = new Post(TestHelper.getDataFromBoorAdvanced(Konachan.get(), 246946), Konachan.get());
    }

    @Test
    public void getSourceBoor() throws Exception {
        assertEquals("Boor source ", post.getSourceBoor(), Boor.Konachan.toString());
    }

    @Test
    public void getId() throws Exception {
        assertEquals("Id ", 246946, post.getId());
    }

    @Test
    public void getMd5() throws Exception {
        assertEquals("Md5 ", "6707b3867e49b5d0be6c0a3242ef6776", post.getMd5());
    }

    @Test
    public void getRating() throws Exception {
        assertEquals(Rating.SAFE, post.getRating());
    }

    @Test
    public void getSource() throws Exception {
        assertEquals("https://www.pixiv.net/member_illust.php?mode=medium&illust_id=63950855", post.getSource());
    }

    @Test
    public void getPreview() throws Exception {
        assertEquals("https://konachan.com/data/preview/67/07/6707b3867e49b5d0be6c0a3242ef6776.jpg", post.getPreview_url());
    }

    @Test
    public void getTags() throws Exception {
        assertTrue(post.getTags().contains("aqua_eyes"));
    }

    @Test
    public void getSample() throws Exception {
        assertEquals(
                "https://konachan.com/sample/6707b3867e49b5d0be6c0a3242ef6776/Konachan.com%20-%20246946%20sample.jpg",
                post.getSample_url()
        );
    }

    @Test
    public void getFile() throws Exception {
        assertEquals(
                "https://konachan.com/image/6707b3867e49b5d0be6c0a3242ef6776/Konachan.com%20-%20246946%20aqua_eyes%20aqua_hair%20bai_yemeng%20bed%20blonde_hair%20bow%20hat%20hatsune_miku%20headphones%20long_hair%20male%20microphone%20scarf%20short_hair%20signed%20twintails%20vocaloid.jpg",
                post.getFile_url()
        );
    }

    @Test
    public void getCreatorId() throws Exception {
        assertEquals("Creator_id ", 156425, post.getCreator_id());
    }

    @Test
    public void getHasComment() throws Exception {
        assertEquals("Has comments", null, post.isHas_comments());
    }

    @Test
    public void getComment() throws Exception {
        assertEquals("Comment url", "https://konachan.com/comment.json?post_id=246946", post.getComments_url());
    }

    @Test
    public void getCreateTime() throws Exception {
        assertEquals("Create Time", "1500476568", post.getCreate_time());
    }
}
