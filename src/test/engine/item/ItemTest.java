package test.engine.item;

import engine.HttpConnection;
import engine.item.Item;
import engine.item.еnum.Rating;
import engine.parser.JsonParser;
import engine.parser.XmlParser;
import org.junit.Test;
import source.boor.AbstractBoorAdvanced;
import source.boor.AbstractBoorBasic;
import source.boor.Danbooru;

import java.util.HashMap;

import static org.junit.Assert.*;


public class ItemTest {


    private HashMap<String, String> getDataFromBoorAdvanced(AbstractBoorAdvanced boor, int id) throws Exception{
        String request1 = boor.getIdRequest(id);
        HttpConnection connection = new HttpConnection(false);
        String responseData1 = connection.getRequest(request1);

        JsonParser parser = new JsonParser();

        return parser.startParse(responseData1).get(0);
    }

    private HashMap<String, String> getDataFromBoorBasic(AbstractBoorBasic boor, int id) throws Exception{
        String request1 = boor.getIdRequest(id);

        XmlParser parser = new XmlParser();
        parser.startParse(request1);
        return (HashMap<String, String>)parser.getResult().get(0);
    }

    @Test
    public void constructorDanbooru_Test() throws Exception {
        Item item = new Item(getDataFromBoorAdvanced(Danbooru.get(), 2790300));

        assertEquals(2790300, item.getId());
        assertEquals("c8191c7018780e6332dc2fb0fb701815", item.getMd5());
        assertEquals(Rating.SAFE,item.getRating());
        assertEquals(9,item.getScore());
        assertEquals("https://i.pximg.net/img-original/img/2017/06/07/23/51/29/63264741_p0.jpg", item.getSource());
        assertEquals("https://danbooru.donmai.us/data/preview/c8191c7018780e6332dc2fb0fb701815.jpg", item.getPreview_url());

    }

}