package test.source.boor.Safebooru;

import org.junit.Assert;
import org.junit.Test;
import source.boor.Safebooru;
import source.еnum.Api;
import source.еnum.Format;

import static org.junit.Assert.*;


public class SafebooruTest {

    @Test
    public void getApi_Test() throws Exception {
        Assert.assertEquals(Api.BASICS, Safebooru.get().getApi());
    }

    @Test
    public void getDataType_Test() throws Exception {
        Assert.assertEquals(Format.XML, Safebooru.get().getFormat());
    }

    @Test
    public void getCustomRequest_Test() throws Exception {
        String request = Safebooru.get().getCustomRequest("/request");
        String expected = "https://safebooru.org/request";
        assertEquals(expected, request);
    }

    @Test
    public void getPostLinkById_Test() throws Exception {
        String request = Safebooru.get().getPostLinkById(393939);
        String expected = "https://safebooru.org/index.php?page=post&s=view&id=393939";
        assertEquals(expected, request);
    }
}