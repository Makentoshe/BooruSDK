package test.source.boor.Behoimi;

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
        String expected = "http://behoimi.org/request";
        assertEquals(expected, request);
    }

    @Test
    public void getIdRequest_Test() throws Exception {
        String request = Behoimi.get().getPostByIdRequest(637175);
        String expected = "http://behoimi.org/post/index.json?tags=id:637175";
        assertEquals(expected, request);
    }

    @Test
    public void getPackRequest_Test() throws Exception {
        String request = Behoimi.get().getPackByTagsRequest(10, "hatsune_miku", 0);
        String expected = "http://behoimi.org/post/index.json?tags=hatsune_miku&limit=10&page=0";
        assertEquals(expected, request);
    }

}