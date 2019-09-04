package test.source.boor.Yandere;

import org.junit.Before;
import org.junit.Test;
import source.boor.Yandere;
import source.еnum.Api;
import source.еnum.Format;

import static org.junit.Assert.*;


public class YandereTest {

    @Before
    public void setUp(){
        Yandere.get().setFormat(Format.JSON);
    }

    @Test
    public void getApi_Test() throws Exception {
        assertEquals(Api.ADVANCED, Yandere.get().getApi());
    }

    @Test
    public void getDataType_Test() throws Exception {
        assertEquals(Format.JSON, Yandere.get().getFormat());
    }

    @Test
    public void getCustomRequest_Test() throws Exception {
        String request = Yandere.get().getCustomRequest("/request");
        String expected = "https://yande.re/request";
        assertEquals(expected, request);
    }

    @Test
    public void getIdRequest_Test() throws Exception{
        String request = Yandere.get().getPostByIdRequest(401562);
        String expected = "https://yande.re/post.json?tags=id:401562";
        assertEquals(expected, request);
    }

    @Test
    public void getPackByTagsRequest_Test() throws Exception{
        String request = Yandere.get().getPostsByTagsRequest(10, "hatsune_miku", 0);
        String expected = "https://yande.re/post.json?tags=hatsune_miku&limit=10&page=0";
        assertEquals(expected, request);
    }

    @Test
    public void getPostLinkById_Test() throws Exception {
        String request = Yandere.get().getPostLinkById(393939);
        String expected = "https://yande.re/post/show/393939";
        assertEquals(expected, request);
    }


}