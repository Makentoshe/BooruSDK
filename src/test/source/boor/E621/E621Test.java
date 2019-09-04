package test.source.boor.E621;

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
        String request = E621.get().getCustomRequest("/request");
        String expected = "https://e621.net/request";
        assertEquals(expected, request);
    }

    @Test
    public void getIdRequest_Test() throws Exception {
        String request = E621.get().getPostByIdRequest(1263892);
        String expected = "https://e621.net/post/show.json?id=1263892";
        assertEquals(expected, request);
    }

    @Test
    public void getPackRequest_Test() throws Exception {
        String request = E621.get().getPostsByTagsRequest(10, "hatsune_miku", 0);
        String expected = "https://e621.net/post/index.json?tags=hatsune_miku&limit=10&page=0";
        assertEquals(expected, request);
    }

    @Test
    public void getPostLinkById_Test() throws Exception {
        String request = E621.get().getPostLinkById(393939);
        String expected = "https://e621.net/post/show/393939";
        assertEquals(expected, request);
    }
}