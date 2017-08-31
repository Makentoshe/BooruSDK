package test.engine;

import engine.BooruEngineException;
import engine.connector.HttpConnection;
import org.junit.Test;

import static org.junit.Assert.*;


public class HttpConnectionTest {

    @Test
    public void getRequest_SuccessRequest_Test() throws Exception {
        String res = new HttpConnection(false).getRequest("https://pastebin.com/raw/2mUw0U3z");
        assertEquals("Xcasca", res);
    }

    @Test(expected = BooruEngineException.class)
    public void getRequest_FailedRequest_Test() throws Exception{
        String res = new HttpConnection(false).getRequest("SAS");
    }
}