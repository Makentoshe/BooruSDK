package test.source.boor.Yandere;

import org.junit.Before;
import org.junit.Test;
import source.Post;
import source.boor.Konachan;
import source.boor.Yandere;
import source.еnum.Boor;
import source.еnum.Rating;
import test.source.PostTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class YandereRemotePostConstructorTest {

    private static Post post;

    @Before
    public void setUp() throws Exception {
        if (post != null) return;
        post = Yandere.get().newPostInstance(PostTest.getDataFromBoorAdvanced(Yandere.get(), 401662));
    }

    @Test
    public void getSourceBoor() throws Exception {
        assertEquals("Boor source ", post.getSourceBoor(), Boor.Yandere.toString());
    }

    @Test
    public void getId() throws Exception {
        assertEquals("Id ", 401662, post.getId());
    }

    @Test
    public void getMd5() throws Exception {
        assertEquals("Md5 ", "a3005b11f1a9fcf9d7e3cdbe04222eb1", post.getMd5());
    }

    @Test
    public void getRating() throws Exception {
        assertEquals(Rating.QUESTIONABLE, post.getRating());
    }

    @Test
    public void getSource() throws Exception {
        assertEquals("pixiv id 1137649", post.getSource());
    }

    @Test
    public void getPreview() throws Exception {
        assertEquals("https://assets.yande.re/data/preview/a3/00/a3005b11f1a9fcf9d7e3cdbe04222eb1.jpg", post.getPreview_url());
    }

    @Test
    public void getTags() throws Exception {
        assertTrue(post.getTags().contains("kantai_collection"));
    }

    @Test
    public void getSample() throws Exception {
        assertEquals(
                "https://files.yande.re/sample/a3005b11f1a9fcf9d7e3cdbe04222eb1/yande.re%20401662%20sample%20bismarck_%28kancolle%29%20bottomless%20fixed%20kantai_collection%20monoto%20no_bra%20nopan%20prinz_eugen_%28kancolle%29%20shirt_lift%20thighhighs%20undressing%20uniform%20uramonoya.jpg",
                post.getSample_url()
        );
    }

    @Test
    public void getFile() throws Exception {
        assertEquals("https://files.yande.re/image/a3005b11f1a9fcf9d7e3cdbe04222eb1/yande.re%20401662%20bismarck_%28kancolle%29%20bottomless%20fixed%20kantai_collection%20monoto%20no_bra%20nopan%20prinz_eugen_%28kancolle%29%20shirt_lift%20thighhighs%20undressing%20uniform%20uramonoya.png", post.getFile_url());
    }

    @Test
    public void getCreatorId() throws Exception {
        assertEquals("Creator_id ", 70832, post.getCreator_id());
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
        assertEquals("Create Time", "1500395759", post.getCreate_time());
    }
}
