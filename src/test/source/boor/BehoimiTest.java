package test.source.boor;

import org.junit.Test;
import source.boor.Behoimi;
import source.boor.Danbooru;
import source.еnum.Api;
import source.еnum.DataType;

import static org.junit.Assert.*;

public class BehoimiTest {
    @Test
    public void getApi_Test() throws Exception {
        assertEquals(Api.ADVANCED, Behoimi.get().getApi());
    }

    @Test
    public void getDataType_Test() throws Exception {
        assertEquals(DataType.XML_BASIC, Behoimi.get().getDataType());
    }

    @Test
    public void getCompleteRequest_Test() throws Exception {
        int itemCount = 100;
        String request = "hatsune_miku";
        int pid = 0;
        String link = Behoimi.get().getCompleteRequest(itemCount, request, pid);
        String expected = "http://behoimi.org/post/index.xml?limit=100&tags=hatsune_miku&page=0";
        assertEquals(expected, link);
    }

}