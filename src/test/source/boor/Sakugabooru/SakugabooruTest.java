package test.source.boor.Sakugabooru;

import org.junit.Before;
import org.junit.Test;
import source.boor.Sakugabooru;
import source.еnum.Api;
import source.еnum.Format;

import static org.junit.Assert.*;


public class SakugabooruTest {

    @Before
    public void setUp(){
        Sakugabooru.get().setFormat(Format.JSON);
    }

    @Test
    public void getApi_Test() throws Exception {
        assertEquals(Api.ADVANCED, Sakugabooru.get().getApi());
    }

    @Test
    public void getDataType_Test() throws Exception {
        assertEquals(Format.JSON, Sakugabooru.get().getFormat());
    }

    @Test
    public void getCustomRequest_Test() throws Exception {
        String request = Sakugabooru.get().getCustomRequest("/request");
        String expected = "https://www.sakugabooru.com/request";
        assertEquals(expected, request);
    }

    @Test
    public void getIdRequest_Test() throws Exception{
        String request = Sakugabooru.get().getPostByIdRequest(401562);
        String expected = "https://www.sakugabooru.com/post.json?tags=id:401562";
        assertEquals(expected, request);
    }

    @Test
    public void getPackByTagsRequest_Test() throws Exception{
        String request = Sakugabooru.get().getPostsByTagsRequest(10, "hatsune_miku", 0);
        String expected = "https://www.sakugabooru.com/post.json?tags=hatsune_miku&limit=10&page=0";
        assertEquals(expected, request);
    }

    @Test
    public void getPostLinkById_Test() throws Exception {
        String request = Sakugabooru.get().getPostLinkById(393939);
        String expected = "https://www.sakugabooru.com/post/show/393939";
        assertEquals(expected, request);
    }

}