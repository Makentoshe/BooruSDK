package test.source.boor;

import org.junit.Before;
import org.junit.Test;
import source.boor.Danbooru;
import source.еnum.Api;
import source.еnum.Format;

import static org.junit.Assert.*;

public class DanbooruTest {

    @Before
    public void setUp(){
        Danbooru.get().setFormat(Format.JSON);
    }

    @Test
    public void getApi_Test() throws Exception {
        assertEquals(Api.ADVANCED, Danbooru.get().getApi());
    }

    @Test
    public void getDataType_Test() throws Exception{
        assertEquals(Format.JSON, Danbooru.get().getFormat());
    }

    @Test
    public void getCustomRequest_Test() throws Exception {
        String request = Danbooru.get().getCustomRequest("request");
        String expected = "https://danbooru.donmai.us/request";
        assertEquals(expected, request);
    }

}