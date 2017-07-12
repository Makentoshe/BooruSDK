package test.source.boor;

import org.junit.Before;
import org.junit.Test;
import source.boor.Konachan;
import source.еnum.Api;
import source.еnum.Format;

import static org.junit.Assert.*;


public class KonachanTest {

    @Before
    public void setUp(){
        Konachan.get().setFormat(Format.JSON);
    }

    @Test
    public void getApi() throws Exception {
        assertEquals(Api.ADVANCED, Konachan.get().getApi());
    }

    @Test
    public void getDataType_Test() throws Exception {
        assertEquals(Format.JSON, Konachan.get().getFormat());
    }

    @Test
    public void getCompleteRequest() throws Exception {
        int itemCount = 100;
        String request = "hatsune_miku";
        int pid = 0;
        String link = Konachan.get().getCompleteRequest(itemCount, request, pid);
        String expected = "https://konachan.com/post.json?limit=100&tags=hatsune_miku&page=0";
        assertEquals(expected, link);

        Konachan.get().setFormat(Format.XML);
        link = Konachan.get().getCompleteRequest(itemCount, request, pid);
        expected = "https://konachan.com/post.xml?limit=100&tags=hatsune_miku&page=0";
        assertEquals(expected, link);
    }

}