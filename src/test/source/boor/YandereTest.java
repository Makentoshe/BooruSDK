package test.source.boor;

import org.junit.Test;
import source.boor.E621;
import source.boor.Yandere;
import source.еnum.Api;

import static org.junit.Assert.*;

/**
 * Created by Makentoshe on 10.07.2017.
 */
public class YandereTest {
    @Test
    public void getApi_Test() throws Exception {
        assertEquals(Api.ADVANCED, Yandere.get().getApi());
    }

    @Test
    public void getCompleteRequest_Test() throws Exception {
        int itemCount = 100;
        String request = "hatsune_miku";
        int pid = 0;
        String link = Yandere.get().getCompleteRequest(itemCount, request, pid);
        String expected = "https://yande.re/post.xml?limit=100&tags=hatsune_miku&page=0";
        assertEquals(expected, link);
    }

}