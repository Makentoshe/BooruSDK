package test.source;

import engine.HttpConnection;
import source.Post;
import source.еnum.Rating;
import engine.parser.JsonParser;
import engine.parser.XmlParser;
import org.junit.Test;
import source.boor.*;
import source.еnum.Boor;

import java.util.HashMap;

import static org.junit.Assert.*;

public class PostTest {

    public static HashMap<String, String> getDataFromBoorAdvanced(AbstractBoorAdvanced boor, int id) throws Exception {
        String request1 = boor.getPostByIdRequest(id);
        //System.out.println(request1);
        HttpConnection connection = new HttpConnection(false);
        String responseData1 = connection.getRequest(request1);

        JsonParser parser = new JsonParser();

        parser.startParse(responseData1);

        return parser.getResult().get(0);
    }

    public static HashMap<String, String> getDataFromBoorBasic(AbstractBoorBasic boor, int id) throws Exception {
        String request1 = boor.getPostByIdRequest(id);
        //System.out.println(request1);

        XmlParser parser = new XmlParser();

        parser.startParse(request1);
        return (HashMap<String, String>) parser.getResult().get(0);
    }

    @Test
    public void constructorGelbooru_Test() throws Exception {
        Post post = new Post(getDataFromBoorBasic(Gelbooru.get(), 3785972), Gelbooru.get());

        assertEquals(post.getSourceBoor(), Boor.Gelbooru.toString());
        assertEquals(3785972, post.getId());
        assertEquals("cf589d62afe26ede34c2f4fa802ff70c", post.getMd5());
        assertEquals(Rating.SAFE, post.getRating());
        assertEquals("https://i.pximg.net/img-original/img/2017/05/11/19/53/24/62849670_p0.jpg", post.getSource());
        assertEquals("https://assets.gelbooru.com/thumbnails/cf/58/thumbnail_cf589d62afe26ede34c2f4fa802ff70c.jpg", post.getPreview_url());
        assertTrue(post.getTags().contains("hatsune_miku"));
        assertEquals("https://gelbooru.com/samples/cf/58/sample_cf589d62afe26ede34c2f4fa802ff70c.jpg", post.getSample_url());
        assertEquals("https://gelbooru.com/images/cf/58/cf589d62afe26ede34c2f4fa802ff70c.jpg", post.getFile_url());
        assertEquals("Creator_id ", 6498, post.getCreator_id());
        assertFalse("Has comments", post.isHas_comments());
        assertEquals("Comment url", null, post.getComments_url());
        assertEquals("Create Time", "Mon Jul 17 09:30:20 -0500 2017", post.getCreate_time());

    }

    @Test
    public void constructorPostInSafebooru_Test() throws Exception {
        Post post = Safebooru.get().newPostInstance(getDataFromBoorBasic(Safebooru.get(), 2278871));

        assertEquals("Boor source ", post.getSourceBoor(), Boor.Safebooru.toString());
        assertEquals("Id ", 2278871, post.getId());
        assertEquals("Md5 ","b5e3bdd52dbcbf8f8715bec584ee87e8", post.getMd5());
        assertEquals("Rating ",Rating.SAFE, post.getRating());
        assertEquals("Source ","", post.getSource());
        assertEquals("Preview ","https://safebooru.org/thumbnails/2187/thumbnail_b5e3bdd52dbcbf8f8715bec584ee87e8.jpeg", post.getPreview_url());
        assertTrue("Tags ", post.getTags().contains("hatsune_miku"));
        assertEquals("Sample ","https://safebooru.org/images/2187/b5e3bdd52dbcbf8f8715bec584ee87e8.jpeg", post.getSample_url());
        assertEquals("File ", "https://safebooru.org/images/2187/b5e3bdd52dbcbf8f8715bec584ee87e8.jpeg", post.getFile_url());
        assertEquals("Creator_id ", 8970, post.getCreator_id());
        assertFalse("Has comments", post.isHas_comments());
        assertEquals("Comment url", null, post.getComments_url());
        assertEquals("Create Time", "Mon Jul 17 09:06:24 +0200 2017", post.getCreate_time());

    }

    @Test
    public void constructorPostInYandere_Test() throws Exception {
        Post post = Yandere.get().newPostInstance(getDataFromBoorAdvanced(Yandere.get(), 401662));

        assertEquals("Boor source ", post.getSourceBoor(), Boor.Yandere.toString());
        assertEquals("Id ", 401662, post.getId());
        assertEquals("Md5 ", "a3005b11f1a9fcf9d7e3cdbe04222eb1", post.getMd5());
        assertEquals(Rating.QUESTIONABLE, post.getRating());
        assertEquals("pixiv id 1137649", post.getSource());
        assertEquals("https://assets.yande.re/data/preview/a3/00/a3005b11f1a9fcf9d7e3cdbe04222eb1.jpg", post.getPreview_url());
        assertTrue(post.getTags().contains("kantai_collection"));
        assertEquals(
                "https://files.yande.re/sample/a3005b11f1a9fcf9d7e3cdbe04222eb1/yande.re%20401662%20sample%20bismarck_%28kancolle%29%20bottomless%20fixed%20kantai_collection%20monoto%20no_bra%20nopan%20prinz_eugen_%28kancolle%29%20shirt_lift%20thighhighs%20undressing%20uniform%20uramonoya.jpg",
                post.getSample_url()
        );
        assertEquals("https://files.yande.re/image/a3005b11f1a9fcf9d7e3cdbe04222eb1/yande.re%20401662%20bismarck_%28kancolle%29%20bottomless%20fixed%20kantai_collection%20monoto%20no_bra%20nopan%20prinz_eugen_%28kancolle%29%20shirt_lift%20thighhighs%20undressing%20uniform%20uramonoya.png", post.getFile_url());
        assertEquals("Creator_id ", 70832, post.getCreator_id());
        assertFalse("Has comments", post.isHas_comments());
        assertEquals("Comment url", null, post.getComments_url());
        assertEquals("Create Time", "1500395759", post.getCreate_time());
    }

    @Test
    public void constructorPostInKonachan_Test() throws Exception {
        Post post = Konachan.get().newPostInstance(getDataFromBoorAdvanced(Konachan.get(), 246946));

        assertEquals("Boor source ", post.getSourceBoor(), Boor.Konachan.toString());
        assertEquals("Id ", 246946, post.getId());
        assertEquals("Md5 ", "6707b3867e49b5d0be6c0a3242ef6776", post.getMd5());
        assertEquals(Rating.SAFE, post.getRating());
        //assertEquals("https://www.pixiv.net/member_illust.php?mode=medium\\u0026illust_id=63950855", post.getSource());
        assertEquals("https://konachan.com/data/preview/67/07/6707b3867e49b5d0be6c0a3242ef6776.jpg", post.getPreview_url());
        assertTrue(post.getTags().contains("aqua_eyes"));
        assertEquals(
                "https://konachan.com/sample/6707b3867e49b5d0be6c0a3242ef6776/Konachan.com%20-%20246946%20sample.jpg",
                post.getSample_url()
        );
        assertEquals(
                "https://konachan.com/image/6707b3867e49b5d0be6c0a3242ef6776/Konachan.com%20-%20246946%20aqua_eyes%20aqua_hair%20bai_yemeng%20bed%20blonde_hair%20bow%20hat%20hatsune_miku%20headphones%20long_hair%20male%20microphone%20scarf%20short_hair%20signed%20twintails%20vocaloid.jpg",
                post.getFile_url()
        );
        assertEquals("Creator_id ", 156425, post.getCreator_id());
        assertFalse("Has comments", post.isHas_comments());
        assertEquals("Comment url", null, post.getComments_url());
        assertEquals("Create Time", "1500476568", post.getCreate_time());
    }

    @Test
    public void constructorPostInBehoimi_Test() throws Exception {
        Post post = Behoimi.get().newPostInstance(getDataFromBoorAdvanced(Behoimi.get(), 633053));

        assertEquals("Boor source ", post.getSourceBoor(), Boor.Behoimi.toString());
        assertEquals("Id ", 633053, post.getId());
        assertEquals("Md5 ", "7cb1161617a3a9d9f56d7772cde0f090", post.getMd5());
        assertEquals(Rating.SAFE, post.getRating());
        assertEquals("", post.getSource());
        assertEquals("http://behoimi.org/data/preview/7c/b1/7cb1161617a3a9d9f56d7772cde0f090.jpg", post.getPreview_url());
        assertTrue(post.getTags().contains("hatsune_miku"));
        assertEquals("http://behoimi.org/data/7c/b1/7cb1161617a3a9d9f56d7772cde0f090.jpg", post.getSample_url());
        assertEquals("http://behoimi.org/data/7c/b1/7cb1161617a3a9d9f56d7772cde0f090.jpg", post.getFile_url());
        assertEquals("Creator_id ", 1, post.getCreator_id());
        assertFalse("Has comments", post.isHas_comments());
        assertEquals("Comment url", null, post.getComments_url());
        //assertEquals("Create Time", "{\"n\":52387000,\"s\":1475072859,\"json_class\":\"Time\"}", post.getCreate_time());
    }

    @Test
    public void constructorPostInSakugabooru_Test() throws Exception {
        Post post = Sakugabooru.get().newPostInstance(getDataFromBoorAdvanced(Sakugabooru.get(), 36635));

        assertEquals("Boor source ", post.getSourceBoor(), Boor.Sakugabooru.toString());
        assertEquals("Id ", 36635, post.getId());
        assertEquals("Md5 ", "a86bb01d16934d71eeb0bf34cae5d58a", post.getMd5());
        assertEquals(Rating.SAFE, post.getRating());
        assertEquals("OP", post.getSource());
        assertEquals("https://sakugabooru.com/data/preview/a86bb01d16934d71eeb0bf34cae5d58a.jpg", post.getPreview_url());
        assertTrue(post.getTags().contains("animated"));
        assertEquals("https://sakugabooru.com/data/a86bb01d16934d71eeb0bf34cae5d58a.mp4", post.getSample_url());
        assertEquals("https://sakugabooru.com/data/a86bb01d16934d71eeb0bf34cae5d58a.mp4", post.getFile_url());
        assertEquals("Creator_id ", 508, post.getCreator_id());
        assertFalse("Has comments", post.isHas_comments());
        assertEquals("Comment url", null, post.getComments_url());
        assertEquals("Create Time", "1500566775", post.getCreate_time());
    }

    @Test
    public void constructorPostInE621_Test() throws Exception {
        Post post = E621.get().newPostInstance(getDataFromBoorAdvanced(E621.get(), 1263892));

        assertEquals("Boor source ", post.getSourceBoor(), Boor.E621.toString());
        assertEquals("Id ", 1263892, post.getId());
        assertEquals("Md5 ", "165b0269a416acb18243bb851249b9b3", post.getMd5());
        assertEquals(Rating.QUESTIONABLE, post.getRating());
        assertEquals("http://iwillbuckyou.tumblr.com/post/152616940313", post.getSource());
        assertEquals("https://static1.e621.net/data/preview/16/5b/165b0269a416acb18243bb851249b9b3.jpg", post.getPreview_url());
        assertTrue(post.getTags().contains("underwear"));
        assertEquals("https://static1.e621.net/data/sample/16/5b/165b0269a416acb18243bb851249b9b3.jpg", post.getSample_url());
        assertEquals("https://static1.e621.net/data/16/5b/165b0269a416acb18243bb851249b9b3.png", post.getFile_url());
        assertEquals("Creator_id ", 33842, post.getCreator_id());
        assertFalse("Has comments", post.isHas_comments());
        assertEquals("Comment url", null, post.getComments_url());
        assertEquals("Create Time", "{\"json_class\":\"Time\",\"s\":1498954819,\"n\":842844000}", post.getCreate_time());
    }
}