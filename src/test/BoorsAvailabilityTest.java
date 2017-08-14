package test;

import engine.BooruEngineException;
import engine.HttpsConnection;
import org.junit.Test;
import source.boor.*;

import javax.management.BadAttributeValueExpException;

import static org.junit.Assert.*;

public class BoorsAvailabilityTest {

    private int getResponceCodeFromRequest(String url) throws BooruEngineException{
        return new HttpsConnection()
                .setRequestMethod("GET")
                .setUserAgent("BooruEngineLib(mkliverout@gmail.com)/1.0")
                .openConnection(url)
                .getResponseCode();
    }

    @Test
    public void behoimi_Test() throws Exception {
        assertEquals(200, getResponceCodeFromRequest(Behoimi.get().getCustomRequest("")));
    }

    @Test
    public void danbooru_Test() throws Exception {
        assertEquals(200, getResponceCodeFromRequest(Danbooru.get().getCustomRequest("")));
    }

    @Test
    public void e621_Test() throws Exception {
        assertEquals(200, getResponceCodeFromRequest(E621.get().getCustomRequest("")));
    }

    @Test
    public void gelbooru_Test() throws Exception {
        assertEquals(200, getResponceCodeFromRequest(Gelbooru.get().getCustomRequest("")));
    }

    @Test
    public void konachan_Test() throws Exception {
        assertEquals(200, getResponceCodeFromRequest(Konachan.get().getCustomRequest("")));
    }

    @Test
    public void rule34_Test() throws Exception {
        assertEquals(200, getResponceCodeFromRequest(Rule34.get().getCustomRequest("")));
    }

    @Test
    public void safebooru_Test() throws Exception {
        assertEquals(200, getResponceCodeFromRequest(Safebooru.get().getCustomRequest("")));
    }

    @Test
    public void sakugabooru_Test() throws Exception {
        assertEquals(200, getResponceCodeFromRequest(Sakugabooru.get().getCustomRequest("")));
    }

    @Test
    public void Yandere_Test() throws Exception {
        assertEquals(200, getResponceCodeFromRequest(Yandere.get().getCustomRequest("")));
    }






}
