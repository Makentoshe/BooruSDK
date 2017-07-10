package test.source.boor;

import org.junit.Assert;
import org.junit.Test;
import source.boor.Gelbooru;
import source.boor.Safebooru;
import source.еnum.Api;
import source.еnum.DataType;

import static org.junit.Assert.*;


public class SafebooruTest {

    @Test
    public void getApi_Test() throws Exception {
        Assert.assertEquals(Api.BASICS, Safebooru.get().getApi());
    }

    @Test
    public void getDataType_Test() throws Exception {
        Assert.assertEquals(DataType.XML_BASIC, Safebooru.get().getDataType());
    }

    @Test
    public void getCompleteRequest_Test() throws Exception {
        int itemCount = 100;
        String request = "hatsune_miku";
        int pid = 0;
        String link = Safebooru.get().getCompleteRequest(itemCount, request, pid);
        String expected = "https://safebooru.org/index.php?page=dapi&s=post&q=index&limit=100&tags=hatsune_miku&pid=0";
        assertEquals(expected, link);
    }

}