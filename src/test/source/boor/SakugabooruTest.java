package test.source.boor;

import org.junit.Before;
import org.junit.Test;
import source.boor.Sakugabooru;
import source.еnum.Api;
import source.еnum.Format;

import static org.junit.Assert.*;


public class SakugabooruTest {

    @Before
    public void setUp(){
        Sakugabooru.get().setFormat(Format.JSON);
    }

    @Test
    public void getApi_Test() throws Exception {
        assertEquals(Api.ADVANCED, Sakugabooru.get().getApi());
    }

    @Test
    public void getDataType_Test() throws Exception {
        assertEquals(Format.JSON, Sakugabooru.get().getFormat());
    }

    @Test
    public void getCompleteRequest_Test() throws Exception {
        int itemCount = 100;
        String request = "hatsune_miku";
        int pid = 0;
        String link = Sakugabooru.get().getCompleteRequest(itemCount, request, pid);
        String expected = "https://sakugabooru.com/post/index.json?limit=100&tag=hatsune_miku&page=0";
        assertEquals(expected, link);

        Sakugabooru.get().setFormat(Format.XML);
        link = Sakugabooru.get().getCompleteRequest(itemCount, request, pid);
        expected = "https://sakugabooru.com/post/index.xml?limit=100&tag=hatsune_miku&page=0";
        assertEquals(expected, link);
    }

}