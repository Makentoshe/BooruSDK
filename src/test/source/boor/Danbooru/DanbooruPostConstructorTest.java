package test.source.boor.Danbooru;

import org.junit.Test;
import source.Post;
import source.boor.Danbooru;
import source.еnum.Boor;
import source.еnum.Rating;
import test.source.TestHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//Create Danbooru post with default Post constructor.
public class DanbooruPostConstructorTest {

    private static Post post;

    public DanbooruPostConstructorTest() throws Exception {
        if (post != null) return;
        post = new Post(TestHelper.getDataFromBoorAdvanced(Danbooru.get(), 2794154), Danbooru.get());
    }

    @Test
    public void getSourceBoor() throws Exception {
        assertEquals("Boor source ", post.getSourceBoor(), Boor.Danbooru.toString());
    }

    @Test
    public void getId() throws Exception {
        assertEquals("Id ", 2794154, post.getId());
    }

    @Test
    public void getMd5() throws Exception {
        assertEquals("Md5 ", "caf360797479bc7ba19eff236c51f533", post.getMd5());
    }

    @Test
    public void getRating() throws Exception {
        assertEquals("Rating ", Rating.SAFE, post.getRating());
    }

    @Test
    public void getSource() throws Exception {
        assertEquals("Source ", "https://e-hentai.org/s/700eced279/851542-9", post.getSource());
    }

    @Test
    public void getPreview() throws Exception {
        assertEquals("Preview ", "https://danbooru.donmai.us/data/preview/caf360797479bc7ba19eff236c51f533.jpg", post.getPreview_url());
    }

    @Test
    public void getTags() throws Exception {
        assertTrue("Tags ", post.getTags().contains("touhou"));
    }

    @Test
    public void getSample() throws Exception {
        assertEquals("Sample ", "https://danbooru.donmai.us/data/__flandre_scarlet_patchouli_knowledge_remilia_scarlet_and_wakasagihime_touhou_drawn_by_makako_yume_bouei_shoujo_tai__caf360797479bc7ba19eff236c51f533.png", post.getSample_url());
    }

    @Test
    public void getFile() throws Exception {
        assertEquals("File ", "https://danbooru.donmai.us/data/sample/__flandre_scarlet_patchouli_knowledge_remilia_scarlet_and_wakasagihime_touhou_drawn_by_makako_yume_bouei_shoujo_tai__sample-caf360797479bc7ba19eff236c51f533.jpg", post.getFile_url());
    }

    @Test
    public void getCreatorId() throws Exception {
        assertEquals("Creator_id ", 485320, post.getCreator_id());
    }

    @Test
    public void getComment() throws Exception {
        assertTrue("Has comments", post.isHas_comments());
    }

    @Test
    public void getHasComments() throws Exception {
        assertEquals("Comment url", "https://danbooru.donmai.us/comments.json?group_by=comment&search[post_id]=2794154", post.getComments_url());
    }

    @Test
    public void getCreateTime() throws Exception {
        assertEquals("Create Time", "2017-07-21T04:18:16.598-04:00", post.getCreate_time());
    }

}
