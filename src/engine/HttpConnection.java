package engine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for downloading XML or JSON raw data from servers.
 * Use it, when you need to do something with raw data before parsing,
 * or if you want use your own parser.
 */
public class HttpConnection {

    private static final String USER_AGENT = "BooruEngineLib(mkliverout@gmail.com)/1.0";
    private static final Logger logger = Logger.getLogger(HttpConnection.class.getName());

    private String mRequest;
    private String mCookies;
    private String mResponse;
    private Map<String, List<String>> mHeaders;

    public HttpConnection(boolean isLogging) {
        logger.setFilter(record -> isLogging);
    }

    public HttpConnection() {
        logger.setFilter(record -> false);
    }

    /**
     * Create connection and request data.
     *
     * @param url - url.
     * @return server response in String.
     * @throws BooruEngineException - when something go wrong.
     */
    @Deprecated
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

            int responseCode = connection.getResponseCode();
            logger.log(Level.INFO, "Get request to url: " + u);

            if (responseCode != 200) return returnException(responseCode);

            logger.log(Level.INFO, "Successful.");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input;

            while ((input = reader.readLine()) != null) result.append(input);

            reader.close();
        } catch (Exception e) {
            throw new BooruEngineException(e);
        }
        return result.toString();
    }

    private String returnException(final int responseCode) {
        switch (responseCode) {
            case 204: {
                return "204 - No Content: Request was successful.";
            }
            case 400: {
                return "400 - Bad Request: The given parameters could not be parsed.";
            }
            case 401: {
                return "401 - Unauthorized: Authentication failed.";
            }
            case 403: {
                return "403 - Forbidden: Access denied.";
            }
            case 404: {
                return "404 - Not Found: Not found.";
            }
            case 410: {
                return "410 - Gone: Pagination limit.";
            }
            case 420: {
                return "420 - Invalid Record: Record could not be saved";
            }
            case 422: {
                return "422 - Locked: The resource is locked and cannot be modified.";
            }
            case 423: {
                return "423 - Already Exists: Resource already exists.";
            }
            case 424: {
                return "424 - Invalid Parameters: The given parameters were invalid.";
            }
            case 429: {
                return "429 - User Throttled: User is throttled, try again later.";
            }
            case 500: {
                return "500 - Internal Server Error: A database timeout, or some unknown error occurred on the server.";
            }
            case 503: {
                return "503 - Service Unavailable: Server cannot currently handle the request, try again later.";
            }
            case 301: {
                return "301 - Moved Permanently: This and all future requests should be directed to the given URI.";
            }
            default: {
                return responseCode + " - Unknown Error.";
            }
        }
    }

    public HttpConnection setRequestType(String requestType) {
        this.mRequest = requestType;
        return this;
    }

    public HttpConnection setCookies(String cookiesString) {
        this.mCookies = cookiesString;
        return this;
    }

    public HttpConnection setCookies(Map cookies) {
        StringBuilder out = new StringBuilder();
        Set<Map.Entry> entrySet = cookies.entrySet();
        for (Map.Entry<String, String> pair : entrySet) {
            out.append(pair.getKey()).append("=").append(pair.getValue()).append("; ");
        }
        out.replace(out.length() - 2, out.length(), "");
        this.mCookies = out.toString();
        return this;
    }

    public HttpConnection connect(String url) throws BooruEngineException{
        switch(this.mRequest){
            case "GET":{
                getRequest(url, mCookies);
                break;
            }
            default:{
                throw new BooruEngineException("Unknown request - " + this.mRequest + "!");
            }
        }
        return this;
    }

    private void getRequest(String url, String cookies) throws BooruEngineException{
        StringBuilder result = new StringBuilder();

        try {
            URL u = new URL(url);
            //open connection
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            //set request method
            connection.setRequestMethod("GET");
            //add request header
            connection.setRequestProperty("User-Agent", USER_AGENT);

            if (cookies != null && !cookies.equals("")) connection.setRequestProperty("Cookie", cookies);

            int responseCode = connection.getResponseCode();
            logger.log(Level.INFO, "Get request to url: " + u);

            if (responseCode != 200) throw new BooruEngineException(returnException(responseCode));

            logger.log(Level.INFO, "Successful.");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input;

            while ((input = reader.readLine()) != null) result.append(input);

            reader.close();

            mResponse = result.toString();
            mHeaders = connection.getHeaderFields();
        } catch (Exception e) {
            throw new BooruEngineException("Exception in GET request method.", e);
        }
    }

    public String getResponse() {
        return mResponse;
    }

    public Map<String, List<String>> getHeaders() {
        return mHeaders;
    }
}
