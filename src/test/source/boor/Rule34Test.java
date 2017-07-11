package test.source.boor;

import org.junit.Test;
import source.boor.Rule34;
import source.еnum.Api;
import source.еnum.Format;

import static org.junit.Assert.*;


public class Rule34Test {

    @Test
    public void getApi() throws Exception {
        assertEquals(Api.BASICS, Rule34.get().getApi());
    }

    @Test
    public void getDataType_Test() throws Exception {
        assertEquals(Format.XML, Rule34.get().getFormat());
    }

    @Test
    public void getCompleteRequest() throws Exception {
        int itemCount = 100;
        String request = "hatsune_miku";
        int pid = 0;
        String link = Rule34.get().getCompleteRequest(itemCount, request, pid);
        String expected = "https://rule34.xxx/index.php?page=dapi&s=post&q=index&limit=100&tags=hatsune_miku&pid=0";
        assertEquals(expected, link);
    }
}