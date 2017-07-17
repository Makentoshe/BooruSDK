package test.source.boor;

import org.junit.Before;
import org.junit.Test;
import source.boor.Behoimi;
import source.еnum.Api;
import source.еnum.Format;

import static org.junit.Assert.*;

public class BehoimiTest {

    @Before
    public void setUp(){
        Behoimi.get().setFormat(Format.JSON);
    }

    @Test
    public void getApi_Test() throws Exception {
        assertEquals(Api.ADVANCED, Behoimi.get().getApi());
    }

    @Test
    public void getDataType_Test() throws Exception {
        assertEquals(Format.JSON, Behoimi.get().getFormat());
    }

    @Test
    public void getCustomRequest_Test() throws Exception {
        String request = Behoimi.get().getCustomRequest("request");
        String expected = "https://behoimi.org/request";
        assertEquals(expected, request);
    }

//    @Test
//    public void getCompleteRequest_Test() throws Exception {
//        int itemCount = 100;
//        String request = "hatsune_miku";
//        int pid = 0;
//        String link = Behoimi.get().getCompleteRequest(itemCount, request, pid);
//        String expected = "http://behoimi.org/post/index.json?limit=100&tags=hatsune_miku&page=0";
//        assertEquals(expected, link);
//
//        Behoimi.get().setFormat(Format.XML);
//        link = Behoimi.get().getCompleteRequest(itemCount, request, pid);
//        expected = "http://behoimi.org/post/index.xml?limit=100&tags=hatsune_miku&page=0";
//        assertEquals(expected, link);
//    }

}