package test.source.boor;

import org.junit.Test;
import source.boor.Gelbooru;
import source.еnum.Api;
import source.еnum.Format;

import static org.junit.Assert.*;

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
    public void getCompleteRequest_Test() throws Exception {
        int itemCount = 100;
        String request = "hatsune_miku";
        int pid = 0;
        String link = Gelbooru.get().getCompleteRequest(itemCount, request, pid);
        String expected = "https://gelbooru.com/index.php?page=dapi&s=post&q=index&limit=100&tags=hatsune_miku&pid=0";
        assertEquals(expected, link);
    }

}