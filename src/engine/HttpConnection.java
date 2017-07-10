package engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Makentoshe on 10.07.2017.
 */
public class HttpConnection {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final Logger logger = Logger.getLogger(HttpConnection.class.getName());

    public HttpConnection(boolean logging){
        logger.setFilter(record -> logging);
    }

    public static String getRequest(String url) throws IOException {
        URL u = new URL(url);
        //open connection
        HttpURLConnection connection = (HttpURLConnection) u.openConnection();
        //set request method
        connection.setRequestMethod("GET");
        //add request header
        connection.setRequestProperty("User-Agent", USER_AGENT);

        int responceCode = connection.getResponseCode();
        logger.log(Level.INFO, "Get request to url: " + u);
        logger.log(Level.INFO, "Responce code: " + responceCode);
        //if code != 200 - throw custom exception.

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String input;
        StringBuffer result = new StringBuffer();

        while ((input = reader.readLine()) != null){
            result.append(input);
        }

        reader.close();

        return result.toString();
    }

}
