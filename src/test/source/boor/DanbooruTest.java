package test.source.boor;

import org.junit.Test;
import source.boor.Danbooru;
import source.еnum.Api;
import source.еnum.Format;

import static org.junit.Assert.*;

public class DanbooruTest {
    @Test
    public void getApi_Test() throws Exception {
        assertEquals(Api.ADVANCED, Danbooru.get().getApi());
    }

    @Test
    public void getDataType_Test() throws Exception{
        assertEquals(Format.JSON, Danbooru.get().getFormat());
    }

    @Test
    public void getCompleteRequest_Test() throws Exception {
        int itemCount = 100;
        String request = "hatsune_miku";
        int pid = 0;
        String link = Danbooru.get().getCompleteRequest(itemCount, request, pid);
        String expected = "https://danbooru.donmai.us/posts."+Danbooru.get().getFormat().toString().toLowerCase()+"?limit=100&tags=hatsune_miku&page=0";
        assertEquals(expected, link);
    }

}