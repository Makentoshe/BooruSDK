package test.source.boor;

import org.junit.Before;
import org.junit.Test;
import source.boor.E621;
import source.еnum.Api;
import source.еnum.Format;

import static org.junit.Assert.*;


public class E621Test {

    @Before
    public void setUp(){
        E621.get().setFormat(Format.JSON);
    }

    @Test
    public void getApi_Test() throws Exception {
        assertEquals(Api.ADVANCED, E621.get().getApi());
    }

    @Test
    public void getDataType_Test() throws Exception{
        assertEquals(Format.JSON, E621.get().getFormat());
    }

    @Test
    public void getCustomRequest_Test() throws Exception {
        String request = E621.get().getCustomRequest("request");
        String expected = "https://e621.net/request";
        assertEquals(expected, request);
    }

//    @Test
//    public void getCompleteRequest_Test() throws Exception {
//        int itemCount = 100;
//        String request = "hatsune_miku";
//        int pid = 0;
//        String link = E621.get().getCompleteRequest(itemCount, request, pid);
//        String expected = "https://e621.net/post/index.json?limit=100&tags=hatsune_miku&page=0";
//        assertEquals(expected, link);
//
//        E621.get().setFormat(Format.XML);
//        link = E621.get().getCompleteRequest(itemCount, request, pid);
//        expected = "https://e621.net/post/index.xml?limit=100&tags=hatsune_miku&page=0";
//        assertEquals(expected, link);
//    }

}