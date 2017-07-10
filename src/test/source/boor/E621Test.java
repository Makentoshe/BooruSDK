package test.source.boor;

import org.junit.Test;
import source.boor.E621;
import source.еnum.Api;
import source.еnum.DataType;

import static org.junit.Assert.*;


public class E621Test {
    @Test
    public void getApi_Test() throws Exception {
        assertEquals(Api.ADVANCED, E621.get().getApi());
    }

    @Test
    public void getDataType_Test() throws Exception{
        assertEquals(DataType.XML_ADVANCED, E621.get().getDataType());
    }

    @Test
    public void getCompleteRequest_Test() throws Exception {
        int itemCount = 100;
        String request = "hatsune_miku";
        int pid = 0;
        String link = E621.get().getCompleteRequest(itemCount, request, pid);
        String expected = "https://e621.net/post/index.xml?limit=100&tags=hatsune_miku&page=0";
        assertEquals(expected, link);
    }

}