package test;

import engine.BooruEngineException;
import engine.HttpsConnection;
import org.junit.Test;
import source.boor.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;

import static org.junit.Assert.*;

public class BoorsAvailabilityTest {

    private int getResponseCodeFromRequest(String url) throws BooruEngineException {
        return new HttpsConnection()
                .setRequestMethod("GET")
                .setUserAgent("BooruEngineLib(mkliverout@gmail.com)/1.0")
                .openConnection(url)
                .getResponseCode();
    }

    @Test
    public void behoimi_Test() throws Exception {
        assertEquals(200, getResponseCodeFromRequest(Behoimi.get().getCustomRequest("")));
    }

    @Test
    public void danbooru_Test() throws Exception {
        assertEquals(200, getResponseCodeFromRequest(Danbooru.get().getCustomRequest("")));
    }

    @Test
    public void e621_Test() throws Exception {
        HttpsConnection connection = new HttpsConnection(true)
                .setRequestMethod("GET")
                .setUserAgent("BooruEngineLib(mkliverout@gmail.com)/1.0")
                .openConnection(E621.get().getCustomRequest(""));
        assertEquals(200, connection.getResponseCode());
    }

    @Test
    public void gelbooru_Test() throws Exception {
        assertEquals(200, getResponseCodeFromRequest(Gelbooru.get().getCustomRequest("")));
    }

    @Test
    public void konachan_Test() throws Exception {
        assertEquals(200, getResponseCodeFromRequest(Konachan.get().getCustomRequest("")));
    }

    @Test
    public void rule34_Test() throws Exception {
        assertEquals(200, getResponseCodeFromRequest(Rule34.get().getCustomRequest("")));
    }

    @Test
    public void safebooru_Test() throws Exception {
        assertEquals(200, getResponseCodeFromRequest(Safebooru.get().getCustomRequest("")));
    }

    @Test
    public void sakugabooru_Test() throws Exception {
        assertEquals(200, getResponseCodeFromRequest(Sakugabooru.get().getCustomRequest("")));
    }

    @Test
    public void Yandere_Test() throws Exception {
        assertEquals(200, getResponseCodeFromRequest(Yandere.get().getCustomRequest("")));
    }
}
