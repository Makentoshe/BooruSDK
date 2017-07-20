package test.source.boor;

import engine.HttpConnection;
import source.boor.Item;
import source.еnum.Rating;
import engine.parser.JsonParser;
import engine.parser.XmlParser;
import org.junit.Test;
import source.boor.*;
import source.еnum.Boor;

import java.util.HashMap;
import java.util.Iterator;

import static org.junit.Assert.*;

public class ItemTest {

    private HashMap<String, String> getDataFromBoorAdvanced(AbstractBoorAdvanced boor, int id) throws Exception {
        String request1 = boor.getIdRequest(id);
        System.out.println(request1);
        HttpConnection connection = new HttpConnection(false);
        String responseData1 = connection.getRequest(request1);

        JsonParser parser = new JsonParser();

        return parser.startParse(responseData1).get(0);
    }

    private HashMap<String, String> getDataFromBoorBasic(AbstractBoorBasic boor, int id) throws Exception {
        String request1 = boor.getIdRequest(id);
        System.out.println(request1);

        XmlParser parser = new XmlParser();

        parser.startParse(request1);
        return (HashMap<String, String>) parser.getResult().get(0);
    }

    @Test
    public void constructorDanbooru_Test() throws Exception {
        Item item = Danbooru.get().newItemInstance(getDataFromBoorAdvanced(Danbooru.get(), 2790300));

        assertEquals("Boor source ", item.getSourceBoor(), Boor.Danbooru);
        assertEquals("Id ", 2790300, item.getId());
        assertEquals("Md5 ", "c8191c7018780e6332dc2fb0fb701815", item.getMd5());
        assertEquals("Rating ", Rating.SAFE, item.getRating());
        assertEquals("Source ", "https://i.pximg.net/img-original/img/2017/06/07/23/51/29/63264741_p0.jpg", item.getSource());
        assertEquals("Preview ", "https://danbooru.donmai.us/data/preview/c8191c7018780e6332dc2fb0fb701815.jpg", item.getPreview_url());
        assertTrue("Tags ", item.getTags().contains("hatsune_miku"));
        assertEquals("Sample ", "https://danbooru.donmai.us/data/__hatsune_miku_vocaloid_drawn_by_yue_yue__c8191c7018780e6332dc2fb0fb701815.jpg", item.getSample_url());
        assertEquals("File ","https://danbooru.donmai.us/data/sample/__hatsune_miku_vocaloid_drawn_by_yue_yue__sample-c8191c7018780e6332dc2fb0fb701815.jpg", item.getFile_url());
//        assertEquals("Creator_id ", 497614, item.getCreator_id());
    }

    @Test
    public void constructorGelbooru_Test() throws Exception {
        Item item = Gelbooru.get().newItemInstance(getDataFromBoorBasic(Gelbooru.get(), 3785972));

        assertEquals(item.getSourceBoor(), Boor.Gelbooru);
        assertEquals(3785972, item.getId());
        assertEquals("cf589d62afe26ede34c2f4fa802ff70c", item.getMd5());
        assertEquals(Rating.SAFE, item.getRating());
        assertEquals("https://i.pximg.net/img-original/img/2017/05/11/19/53/24/62849670_p0.jpg", item.getSource());
        assertEquals("https://gelbooru.com/thumbnails/cf/58/thumbnail_cf589d62afe26ede34c2f4fa802ff70c.jpg", item.getPreview_url());
        assertTrue(item.getTags().contains("hatsune_miku"));
        assertEquals("https://assets.gelbooru.com/samples/cf/58/sample_cf589d62afe26ede34c2f4fa802ff70c.jpg", item.getSample_url());
        assertEquals("https://assets.gelbooru.com/images/cf/58/cf589d62afe26ede34c2f4fa802ff70c.jpg", item.getFile_url());
//        assertEquals("Creator_id ", 6498, item.getCreator_id());

    }

    @Test
    public void constructorSafebooru_Test() throws Exception {
        Item item = Safebooru.get().newItemInstance(getDataFromBoorBasic(Safebooru.get(), 2278871));

        assertEquals("Boor source ", item.getSourceBoor(), Boor.SafeBooru);
        assertEquals("Id ", 2278871, item.getId());
        assertEquals("Md5 ","b5e3bdd52dbcbf8f8715bec584ee87e8", item.getMd5());
        assertEquals("Rating ",Rating.SAFE, item.getRating());
        assertEquals("Source ","", item.getSource());
        assertEquals("Preview ","https://safebooru.org/thumbnails/2187/thumbnail_b5e3bdd52dbcbf8f8715bec584ee87e8.jpeg", item.getPreview_url());
        assertTrue("Tags ",item.getTags().contains("hatsune_miku"));
        assertEquals("Sample ","https://safebooru.org/images/2187/b5e3bdd52dbcbf8f8715bec584ee87e8.jpeg", item.getSample_url());
        assertEquals("File ", "https://safebooru.org/images/2187/b5e3bdd52dbcbf8f8715bec584ee87e8.jpeg", item.getFile_url());

    }

    @Test
    public void constructorRule34_Test() throws Exception {
        Item item = Rule34.get().newItemInstance(getDataFromBoorBasic(Rule34.get(), 2421106));

        assertEquals("Boor source ", item.getSourceBoor(), Boor.Rule34);
        assertEquals("Id ", 2421106, item.getId());
        assertEquals("Md5 ","23b64b698780463545dac889883d83c0", item.getMd5());
        assertEquals("Rating ",Rating.EXPLICIT, item.getRating());
        assertEquals("Source ","https://i.pximg.net/img-original/img/2016/11/11/20/21/32/59734064_p0.png", item.getSource());
        assertEquals("Preview ","https://rule34.xxx/thumbnails/2231/thumbnail_06d1a7a474d4a44147a7ac27d1515c26e364cda7.jpg", item.getPreview_url());
        assertTrue("Tags ",item.getTags().contains("hatsune_miku"));
        assertEquals("Sample ","https://img.rule34.xxx/samples/2231/sample_06d1a7a474d4a44147a7ac27d1515c26e364cda7.jpg", item.getSample_url());
        assertEquals("File ", "https://img.rule34.xxx/images/2231/06d1a7a474d4a44147a7ac27d1515c26e364cda7.png", item.getFile_url());

    }

    @Test
    public void constructorYandere_Test() throws Exception {
        Item item = Yandere.get().newItemInstance(getDataFromBoorAdvanced(Yandere.get(), 401662));

        assertEquals("Boor source ", item.getSourceBoor(), Boor.Yandere);
        assertEquals("Id ", 401662, item.getId());
        assertEquals("Md5 ", "a3005b11f1a9fcf9d7e3cdbe04222eb1", item.getMd5());
        assertEquals(Rating.QUESTIONABLE, item.getRating());
        assertEquals("pixiv id 1137649", item.getSource());
        assertEquals("https://assets.yande.re/data/preview/a3/00/a3005b11f1a9fcf9d7e3cdbe04222eb1.jpg", item.getPreview_url());
        assertTrue(item.getTags().contains("kantai_collection"));
        assertEquals(
                "https://files.yande.re/sample/a3005b11f1a9fcf9d7e3cdbe04222eb1/yande.re%20401662%20sample%20bismarck_%28kancolle%29%20bottomless%20fixed%20kantai_collection%20monoto%20no_bra%20nopan%20prinz_eugen_%28kancolle%29%20shirt_lift%20thighhighs%20undressing%20uniform%20uramonoya.jpg",
                item.getSample_url()
        );
        assertEquals("https://files.yande.re/image/a3005b11f1a9fcf9d7e3cdbe04222eb1/yande.re%20401662%20bismarck_%28kancolle%29%20bottomless%20fixed%20kantai_collection%20monoto%20no_bra%20nopan%20prinz_eugen_%28kancolle%29%20shirt_lift%20thighhighs%20undressing%20uniform%20uramonoya.png", item.getFile_url());
    }

    @Test
    public void constructorKonachan_Test() throws Exception {
        Item item = Konachan.get().newItemInstance(getDataFromBoorAdvanced(Konachan.get(), 246946));

        assertEquals("Boor source ", item.getSourceBoor(), Boor.Konachan);
        assertEquals("Id ", 246946, item.getId());
        assertEquals("Md5 ", "6707b3867e49b5d0be6c0a3242ef6776", item.getMd5());
        assertEquals(Rating.SAFE, item.getRating());
        //assertEquals("https://www.pixiv.net/member_illust.php?mode=medium\\u0026illust_id=63950855", item.getSource());
        assertEquals("https://konachan.com/data/preview/67/07/6707b3867e49b5d0be6c0a3242ef6776.jpg", item.getPreview_url());
        assertTrue(item.getTags().contains("aqua_eyes"));
        assertEquals(
                "https://konachan.com/sample/6707b3867e49b5d0be6c0a3242ef6776/Konachan.com%20-%20246946%20sample.jpg",
                item.getSample_url()
        );
        assertEquals(
                "https://konachan.com/image/6707b3867e49b5d0be6c0a3242ef6776/Konachan.com%20-%20246946%20aqua_eyes%20aqua_hair%20bai_yemeng%20bed%20blonde_hair%20bow%20hat%20headphones%20kagamine_rin%20long_hair%20male%20microphone%20scarf%20short_hair%20twintails%20vocaloid.jpg",
                item.getFile_url()
        );

    }

    @Test
    public void constructorBehoimi_Test() throws Exception {
        Item item = Behoimi.get().newItemInstance(getDataFromBoorAdvanced(Behoimi.get(), 633053));

        assertEquals("Boor source ", item.getSourceBoor(), Boor.Behoimi);
        assertEquals("Id ", 633053, item.getId());
        assertEquals("Md5 ", "7cb1161617a3a9d9f56d7772cde0f090", item.getMd5());
        assertEquals(Rating.SAFE, item.getRating());
        assertEquals("", item.getSource());
        assertEquals("http://behoimi.org/data/preview/7c/b1/7cb1161617a3a9d9f56d7772cde0f090.jpg", item.getPreview_url());
        assertTrue(item.getTags().contains("hatsune_miku"));
        assertEquals("http://behoimi.org/data/7c/b1/7cb1161617a3a9d9f56d7772cde0f090.jpg", item.getSample_url());
        assertEquals("http://behoimi.org/data/7c/b1/7cb1161617a3a9d9f56d7772cde0f090.jpg", item.getFile_url());

    }

    @Test
    public void constructorSakugabooru_Test() throws Exception {
        Item item = Sakugabooru.get().newItemInstance(getDataFromBoorAdvanced(Sakugabooru.get(), 36635));

        assertEquals("Boor source ", item.getSourceBoor(), Boor.Sakugabooru);
        assertEquals("Id ", 36635, item.getId());
        assertEquals("Md5 ", "a86bb01d16934d71eeb0bf34cae5d58a", item.getMd5());
        assertEquals(Rating.SAFE, item.getRating());
        assertEquals("OP", item.getSource());
        assertEquals("https://sakugabooru.com/data/preview/a86bb01d16934d71eeb0bf34cae5d58a.jpg", item.getPreview_url());
        assertTrue(item.getTags().contains("animated"));
        assertEquals("https://sakugabooru.com/data/a86bb01d16934d71eeb0bf34cae5d58a.mp4",item.getSample_url());
        assertEquals("https://sakugabooru.com/data/a86bb01d16934d71eeb0bf34cae5d58a.mp4", item.getFile_url());
    }

    @Test
    public void constructorE621_Test() throws Exception {
        Item item = E621.get().newItemInstance(getDataFromBoorAdvanced(E621.get(), 1263892));

        assertEquals("Boor source ", item.getSourceBoor(), Boor.E621);
        assertEquals("Id ", 1263892, item.getId());
        assertEquals("Md5 ", "165b0269a416acb18243bb851249b9b3", item.getMd5());
        assertEquals(Rating.QUESTIONABLE, item.getRating());
        assertEquals("http://iwillbuckyou.tumblr.com/post/152616940313", item.getSource());
        assertEquals("https://static1.e621.net/data/preview/16/5b/165b0269a416acb18243bb851249b9b3.jpg", item.getPreview_url());
        assertTrue(item.getTags().contains("underwear"));
        assertEquals("https://static1.e621.net/data/sample/16/5b/165b0269a416acb18243bb851249b9b3.jpg",item.getSample_url());
        assertEquals("https://static1.e621.net/data/16/5b/165b0269a416acb18243bb851249b9b3.png", item.getFile_url());
    }
}