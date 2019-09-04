package test.source.boor.Konachan;

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
    public void getCustomRequest_Test() throws Exception {
        String request = Konachan.get().getCustomRequest("/request");
        String expected = "https://konachan.com/request";
        assertEquals(expected, request);
    }

    @Test
    public void getIdRequest_Test() throws Exception {
        String request = Konachan.get().getPostByIdRequest(246852);
        String expected = "https://konachan.com/post.json?tags=id:246852";
        assertEquals(expected, request);
    }

    @Test
    public void getPackRequest_Test() throws Exception {
        String request = Konachan.get().getPostsByTagsRequest(10, "hatsune_miku", 0);
        String expected = "https://konachan.com/post/index.json?tags=hatsune_miku&limit=10&page=0";
        assertEquals(expected, request);
    }

    @Test
    public void getPostLinkById_Test() throws Exception {
        String request = Konachan.get().getPostLinkById(393939);
        String expected = "https://konachan.com/post/show/393939";
        assertEquals(expected, request);
    }



}