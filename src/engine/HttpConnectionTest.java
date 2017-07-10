package engine;

import org.junit.Before;
import org.junit.Test;
import source.boor.Gelbooru;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.*;


public class HttpConnectionTest {

    private static final String USER_AGENT = "Mozilla/5.0";
    private final String url = Gelbooru.get().getCompleteRequest(1, "", 0);

    @Test
    public void getRequest_Test() throws Exception {

        URL u = new URL(url);
        //open connection
        HttpURLConnection connection = (HttpURLConnection) u.openConnection();
        //set request method
        connection.setRequestMethod("GET");
        //add request header
        connection.setRequestProperty("User-Agent", USER_AGENT);

        assertEquals("GET", connection.getRequestMethod());
        assertEquals(USER_AGENT, connection.getRequestProperty("User-Agent"));

        int responceCode = connection.getResponseCode();

        //if code != 200 - throw custom exception.

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String input;
        StringBuffer result = new StringBuffer();

        while ((input = reader.readLine()) != null){
            result.append(input);
        }

        reader.close();

        assertTrue(!result.toString().isEmpty());
    }

}