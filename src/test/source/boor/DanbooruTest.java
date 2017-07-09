package test.source.boor;

import org.junit.Test;
import source.boor.Danbooru;
import source.boor.Rule34;
import source.еnum.Api;

import static org.junit.Assert.*;

public class DanbooruTest {
    @Test
    public void getApi_Test() throws Exception {
        assertEquals(Api.ADVANCED, Danbooru.get().getApi());
    }

    @Test
    public void getCompleteRequest_Test() throws Exception {
        int itemCount = 100;
        String request = "hatsune_miku";
        int pid = 0;
        String link = Danbooru.get().getCompleteRequest(itemCount, request, pid);
        String expected = "https://danbooru.donmai.us/posts.xml?tags=hatsune_miku&limit=100&page=0";
        assertEquals(expected, link);
    }

}