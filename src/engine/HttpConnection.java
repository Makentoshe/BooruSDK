package engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Class for downloading XML or JSON raw data from servers.
 * Use it, when you need to do something with raw data before parsing,
 * or if you want use your own parser.
 */
public class HttpConnection {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final Logger logger = Logger.getLogger(HttpConnection.class.getName());
    private int responseCode;

    public HttpConnection(boolean logging){
        logger.setFilter(record -> logging);
    }

    /**
     * Create connection and request data.
     *
     * @param url - url.
     * @return server response in String.
     * @throws BooruEngineException - when something go wrong.
     */
    public String getRequest(String url) throws BooruEngineException {
        StringBuilder result = new StringBuilder();

        try {
            URL u = new URL(url);
            //open connection
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            //set request method
            connection.setRequestMethod("GET");
            //add request header
            connection.setRequestProperty("User-Agent", USER_AGENT);

            responseCode = connection.getResponseCode();
            logger.log(Level.INFO, "Get request to url: " + u);


            if (responseCode != 200) {
                throwException(responseCode);
            }
            logger.log(Level.INFO, "Successful.");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input;

            while ((input = reader.readLine()) != null) {
                result.append(input);
            }

            reader.close();

        }catch (Exception e){
            throw new BooruEngineException(e.getMessage());
        }
        return result.toString();
    }

    private void throwException(final int responseCode) throws BooruEngineException{
        switch (responseCode){
            case 204:{
                throw new BooruEngineException("204 - No Content: Request was successful.");
            }
            case 400:{
                throw new BooruEngineException("400 - Bad Request: The given parameters could not be parsed.");
            }
            case 401:{
                throw new BooruEngineException("401 - Unauthorized: Authentication failed.");
            }
            case 403:{
                throw new BooruEngineException("403 - Forbidden: Access denied.");
            }
            case 404:{
                throw new BooruEngineException("404 - Not Found: Not found.");
            }
            case 410:{
                throw new BooruEngineException("410 - Gone: Pagination limit.");
            }
            case 420:{
                throw new BooruEngineException("420 - Invalid Record: Record could not be saved");
            }
            case 422:{
                throw new BooruEngineException("422 - Locked: The resource is locked and cannot be modified.");
            }
            case 423:{
                throw new BooruEngineException("423 - Already Exists: Resource already exists.");
            }
            case 424:{
                throw new BooruEngineException("424 - Invalid Parameters: The given parameters were invalid.");
            }
            case 429:{
                throw new BooruEngineException("429 - User Throttled: User is throttled, try again later.");
            }
            case 500:{
                throw new BooruEngineException("500 - Internal Server Error: A database timeout, or some unknown error occurred on the server.");
            }
            case 503:{
                throw new BooruEngineException("503 - Service Unavailable: Server cannot currently handle the request, try again later.");
            }
//            case 301:{
//                throw new BooruEngineException("301 - Moved Permanently: This and all future requests should be directed to the given URI.");
//            }
            default:{
                throw new BooruEngineException(responseCode + " - Unknown Error.");
            }
        }
    }

    public int getResponseCode() {
        return responseCode;
    }
}
