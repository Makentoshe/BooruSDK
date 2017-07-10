package test.engine;

import engine.BooruEngineException;
import engine.HttpConnection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import source.boor.Danbooru;
import source.boor.Gelbooru;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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