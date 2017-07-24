package test.source.boor.Rule34;

import org.junit.Test;
import source.boor.Rule34;
import source.еnum.Api;
import source.еnum.Format;

import static org.junit.Assert.*;

//default test for correct basic methods.
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
    public void getCustomRequest_Test() throws Exception{
        String request = Rule34.get().getCustomRequest("request");
        String expected = "https://rule34.xxx/index.php?page=dapi&q=index&s=request";
        assertEquals(expected, request);
    }
}