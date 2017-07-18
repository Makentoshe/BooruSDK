package test.engine.item;

import engine.HttpConnection;
import engine.item.Item;
import engine.item.еnum.Rating;
import engine.parser.JsonParser;
import engine.parser.XmlParser;
import org.junit.Test;
import source.boor.*;
import source.еnum.Boor;

import java.util.HashMap;

import static org.junit.Assert.*;


public class ItemTest {

    private HashMap<String, String> getDataFromBoorAdvanced(AbstractBoorAdvanced boor, int id) throws Exception {
        String request1 = boor.getIdRequest(id);
        //System.out.println(request1);
        HttpConnection connection = new HttpConnection(false);
        String responseData1 = connection.getRequest(request1);

        JsonParser parser = new JsonParser();

        return parser.startParse(responseData1).get(0);
    }

    private HashMap<String, String> getDataFromBoorBasic(AbstractBoorBasic boor, int id) throws Exception {
        String request1 = boor.getIdRequest(id);
        //System.out.println(request1);

        XmlParser parser = new XmlParser();

        parser.startParse(request1);
        return (HashMap<String, String>) parser.getResult().get(0);
    }

    @Test
    public void constructorDanbooru_Test() throws Exception {
        Item item = new Item(getDataFromBoorAdvanced(Danbooru.get(), 2790300));

        assertEquals(item.getSourceBoor(), Boor.Undefined);
        assertEquals(2790300, item.getId());
        assertEquals("c8191c7018780e6332dc2fb0fb701815", item.getMd5());
        assertEquals(Rating.SAFE, item.getRating());
        assertEquals("https://i.pximg.net/img-original/img/2017/06/07/23/51/29/63264741_p0.jpg", item.getSource());
        assertEquals("https://danbooru.donmai.us/data/preview/c8191c7018780e6332dc2fb0fb701815.jpg", item.getPreview_url());
        assertTrue(item.getTags().contains("hatsune_miku"));

    }

    //same for Safebooru and Rule34
    @Test
    public void constructorGelbooru_Test() throws Exception {
        Item item = new Item(getDataFromBoorBasic(Gelbooru.get(), 3785972));

        assertEquals(item.getSourceBoor(), Boor.Undefined);
        assertEquals(3785972, item.getId());
        assertEquals("cf589d62afe26ede34c2f4fa802ff70c", item.getMd5());
        assertEquals(Rating.SAFE, item.getRating());
        assertEquals("https://i.pximg.net/img-original/img/2017/05/11/19/53/24/62849670_p0.jpg", item.getSource());
        assertEquals("https://assets.gelbooru.com/thumbnails/cf/58/thumbnail_cf589d62afe26ede34c2f4fa802ff70c.jpg", item.getPreview_url());
        assertTrue(item.getTags().contains("hatsune_miku"));
    }

    @Test
    public void constructorYandere_Test() throws Exception {
        Item item = new Item(getDataFromBoorAdvanced(Yandere.get(), 401662));

        assertEquals(item.getSourceBoor(), Boor.Undefined);
        assertEquals(401662, item.getId());
        assertEquals("a3005b11f1a9fcf9d7e3cdbe04222eb1", item.getMd5());
        assertEquals(Rating.QUESTIONABLE, item.getRating());
        assertEquals("pixiv id 1137649", item.getSource());
        assertEquals("https://assets.yande.re/data/preview/a3/00/a3005b11f1a9fcf9d7e3cdbe04222eb1.jpg", item.getPreview_url());
        assertTrue(item.getTags().contains("kantai_collection"));
    }

}