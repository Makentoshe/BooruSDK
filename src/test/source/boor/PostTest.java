package test.source.boor;

import engine.HttpConnection;
import source.boor.Post;
import source.еnum.Rating;
import engine.parser.JsonParser;
import engine.parser.XmlParser;
import org.junit.Test;
import source.boor.*;
import source.еnum.Boor;

import java.util.HashMap;

import static org.junit.Assert.*;

public class PostTest {

    private HashMap<String, String> getDataFromBoorAdvanced(AbstractBoorAdvanced boor, int id) throws Exception {
        String request1 = boor.getIdRequest(id);
        System.out.println(request1);
        HttpConnection connection = new HttpConnection(false);
        String responseData1 = connection.getRequest(request1);

        JsonParser parser = new JsonParser();

        parser.startParse(responseData1);

        return parser.getResult().get(0);
    }

    private HashMap<String, String> getDataFromBoorBasic(AbstractBoorBasic boor, int id) throws Exception {
        String request1 = boor.getIdRequest(id);
        System.out.println(request1);

        XmlParser parser = new XmlParser();

        parser.startParse(request1);
        return (HashMap<String, String>) parser.getResult().get(0);
    }

    @Test
    public void constructorPostInDanbooru_Test() throws Exception {
        Post post = Danbooru.get().newPostInstance(getDataFromBoorAdvanced(Danbooru.get(), 2790300));

        assertEquals("Boor source ", post.getSourceBoor(), Boor.Danbooru.toString());
        assertEquals("Id ", 2790300, post.getId());
        assertEquals("Md5 ", "c8191c7018780e6332dc2fb0fb701815", post.getMd5());
        assertEquals("Rating ", Rating.SAFE, post.getRating());
        assertEquals("Source ", "https://i.pximg.net/img-original/img/2017/06/07/23/51/29/63264741_p0.jpg", post.getSource());
        assertEquals("Preview ", "https://danbooru.donmai.us/data/preview/c8191c7018780e6332dc2fb0fb701815.jpg", post.getPreview_url());
        assertTrue("Tags ", post.getTags().contains("hatsune_miku"));
        assertEquals("Sample ", "https://danbooru.donmai.us/data/__hatsune_miku_vocaloid_drawn_by_yue_yue__c8191c7018780e6332dc2fb0fb701815.jpg", post.getSample_url());
        assertEquals("File ","https://danbooru.donmai.us/data/sample/__hatsune_miku_vocaloid_drawn_by_yue_yue__sample-c8191c7018780e6332dc2fb0fb701815.jpg", post.getFile_url());
//        assertEquals("Creator_id ", 497614, post.getCreator_id());
    }

    @Test
    public void constructorDanbooru_Test() throws Exception {
        Post post = new Post(getDataFromBoorAdvanced(Danbooru.get(), 2790300));

        assertEquals("Boor source ", post.getSourceBoor(), Boor.Danbooru.toString());
        assertEquals("Id ", 2790300, post.getId());
        assertEquals("Md5 ", "c8191c7018780e6332dc2fb0fb701815", post.getMd5());
        assertEquals("Rating ", Rating.SAFE, post.getRating());
        assertEquals("Source ", "https://i.pximg.net/img-original/img/2017/06/07/23/51/29/63264741_p0.jpg", post.getSource());
        assertEquals("Preview ", "https://danbooru.donmai.us/data/preview/c8191c7018780e6332dc2fb0fb701815.jpg", post.getPreview_url());
        assertTrue("Tags ", post.getTags().contains("hatsune_miku"));
        assertEquals("Sample ", "https://danbooru.donmai.us/data/__hatsune_miku_vocaloid_drawn_by_yue_yue__c8191c7018780e6332dc2fb0fb701815.jpg", post.getSample_url());
        assertEquals("File ","https://danbooru.donmai.us/data/sample/__hatsune_miku_vocaloid_drawn_by_yue_yue__sample-c8191c7018780e6332dc2fb0fb701815.jpg", post.getFile_url());
        assertEquals("Creator_id ", 497614, post.getCreator_id());
        assertFalse("Has comments", post.isHas_comments());
    }

    @Test
    public void constructorGelbooru_Test() throws Exception {
        Post post = Gelbooru.get().newPostInstance(getDataFromBoorBasic(Gelbooru.get(), 3785972));

        assertEquals(post.getSourceBoor(), Boor.Gelbooru.toString());
        assertEquals(3785972, post.getId());
        assertEquals("cf589d62afe26ede34c2f4fa802ff70c", post.getMd5());
        assertEquals(Rating.SAFE, post.getRating());
        assertEquals("https://i.pximg.net/img-original/img/2017/05/11/19/53/24/62849670_p0.jpg", post.getSource());
        assertEquals("https://gelbooru.com/thumbnails/cf/58/thumbnail_cf589d62afe26ede34c2f4fa802ff70c.jpg", post.getPreview_url());
        assertTrue(post.getTags().contains("hatsune_miku"));
        assertEquals("https://assets.gelbooru.com/samples/cf/58/sample_cf589d62afe26ede34c2f4fa802ff70c.jpg", post.getSample_url());
        assertEquals("https://assets.gelbooru.com/images/cf/58/cf589d62afe26ede34c2f4fa802ff70c.jpg", post.getFile_url());
//        assertEquals("Creator_id ", 6498, post.getCreator_id());

    }

    @Test
    public void constructorSafebooru_Test() throws Exception {
        Post post = Safebooru.get().newPostInstance(getDataFromBoorBasic(Safebooru.get(), 2278871));

        assertEquals("Boor source ", post.getSourceBoor(), Boor.SafeBooru.toString());
        assertEquals("Id ", 2278871, post.getId());
        assertEquals("Md5 ","b5e3bdd52dbcbf8f8715bec584ee87e8", post.getMd5());
        assertEquals("Rating ",Rating.SAFE, post.getRating());
        assertEquals("Source ","", post.getSource());
        assertEquals("Preview ","https://safebooru.org/thumbnails/2187/thumbnail_b5e3bdd52dbcbf8f8715bec584ee87e8.jpeg", post.getPreview_url());
        assertTrue("Tags ", post.getTags().contains("hatsune_miku"));
        assertEquals("Sample ","https://safebooru.org/images/2187/b5e3bdd52dbcbf8f8715bec584ee87e8.jpeg", post.getSample_url());
        assertEquals("File ", "https://safebooru.org/images/2187/b5e3bdd52dbcbf8f8715bec584ee87e8.jpeg", post.getFile_url());

    }

    @Test
    public void constructorRule34_Test() throws Exception {
        Post post = Rule34.get().newPostInstance(getDataFromBoorBasic(Rule34.get(), 2421106));

        assertEquals("Boor source ", post.getSourceBoor(), Boor.Rule34.toString());
        assertEquals("Id ", 2421106, post.getId());
        assertEquals("Md5 ","23b64b698780463545dac889883d83c0", post.getMd5());
        assertEquals("Rating ",Rating.EXPLICIT, post.getRating());
        assertEquals("Source ","https://i.pximg.net/img-original/img/2016/11/11/20/21/32/59734064_p0.png", post.getSource());
        assertEquals("Preview ","https://rule34.xxx/thumbnails/2231/thumbnail_06d1a7a474d4a44147a7ac27d1515c26e364cda7.jpg", post.getPreview_url());
        assertTrue("Tags ", post.getTags().contains("hatsune_miku"));
        assertEquals("Sample ","https://img.rule34.xxx/samples/2231/sample_06d1a7a474d4a44147a7ac27d1515c26e364cda7.jpg", post.getSample_url());
        assertEquals("File ", "https://img.rule34.xxx/images/2231/06d1a7a474d4a44147a7ac27d1515c26e364cda7.png", post.getFile_url());

    }

    @Test
    public void constructorYandere_Test() throws Exception {
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
    }

    @Test
    public void constructorKonachan_Test() throws Exception {
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
                "https://konachan.com/image/6707b3867e49b5d0be6c0a3242ef6776/Konachan.com%20-%20246946%20aqua_eyes%20aqua_hair%20bai_yemeng%20bed%20blonde_hair%20bow%20hat%20headphones%20kagamine_rin%20long_hair%20male%20microphone%20scarf%20short_hair%20twintails%20vocaloid.jpg",
                post.getFile_url()
        );

    }

    @Test
    public void constructorBehoimi_Test() throws Exception {
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

    }

    @Test
    public void constructorSakugabooru_Test() throws Exception {
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
    }

    @Test
    public void constructorE621_Test() throws Exception {
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
    }
}