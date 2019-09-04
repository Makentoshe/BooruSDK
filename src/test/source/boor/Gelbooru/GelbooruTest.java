package test.source.boor.Gelbooru;

import org.junit.Test;
import source.boor.Gelbooru;
import source.еnum.Api;
import source.еnum.Format;

import static org.junit.Assert.*;

//default test for correct basic methods.
public class GelbooruTest {

    @Test
    public void getApi_Test() throws Exception {
        assertEquals(Api.BASICS, Gelbooru.get().getApi());
    }

    @Test
    public void getDataType_Test() throws Exception{
        assertEquals(Format.XML, Gelbooru.get().getFormat());
    }

    @Test
    public void getCustomRequest_Test() throws Exception{
        String request = Gelbooru.get().getCustomRequest("/request");
        String expected = "https://gelbooru.com/request";
        assertEquals(expected, request);
    }

    @Test
    public void getPostLinkById_Test() throws Exception {
        String request = Gelbooru.get().getPostLinkById(393939);
        String expected = "https://gelbooru.com/index.php?page=post&s=view&id=393939";
        assertEquals(expected, request);
    }

}