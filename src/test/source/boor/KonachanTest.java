package test.source.boor;

import org.junit.Test;
import source.boor.Konachan;
import source.еnum.Api;
import source.еnum.DataType;

import static org.junit.Assert.*;

/**
 * Created by Makentoshe on 10.07.2017.
 */
public class KonachanTest {
    @Test
    public void getApi() throws Exception {
        assertEquals(Api.ADVANCED, Konachan.get().getApi());
    }

    @Test
    public void getDataType_Test() throws Exception {
        assertEquals(DataType.XML_BASIC, Konachan.get().getDataType());
    }

    @Test
    public void getCompleteRequest() throws Exception {
        int itemCount = 100;
        String request = "hatsune_miku";
        int pid = 0;
        String link = Konachan.get().getCompleteRequest(itemCount, request, pid);
        String expected = "https://konachan.com/post.xml?limit=100&tags=hatsune_miku&page=0";
        assertEquals(expected, link);
    }

}