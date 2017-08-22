package engine;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpsConnection {

    public static final String DEFAULT_USER_AGENT = "BooruEngineLib/1.0";

    private final Map<String, String> mHeaders = new HashMap<>(); //request headers
    private Method mRequestMethod;
    private String mBody;

    //after getting response
    private int mResponseCode;
    private String mResponse;
    private String mResponseMessage;
    private Map<String, List<String>> mResponseHeaders;

    private static final Logger log = Logger.getLogger(HttpsConnection.class.getName());

    public HttpsConnection() {
        log.setFilter(record -> false);
    }

    public HttpsConnection(final boolean isLogging) {
        log.setFilter(record -> isLogging);
    }

    public HttpsConnection setRequestMethod(final Method requestMethod) {
        log.log(Level.INFO, "Set Request Method: " + requestMethod);
        this.mRequestMethod = requestMethod;
        return this;
    }

    public HttpsConnection setHeader(final String key, final String value) {
        log.log(Level.INFO, "Set Header: " + key + ": " + value);
        this.mHeaders.put(key, value);
        return this;
    }

    public HttpsConnection setUserAgent(final String userAgent) {
        setHeader("User-Agent", userAgent);
        return this;
    }

    public HttpsConnection setBody(final String body){
        this.mBody = body;
        return this;
    }

    public HttpsConnection setCookies(final String cookies) {
        setHeader("Cookie", cookies);
        return this;
    }

    public HttpsConnection addCookie(final String cookie) {
        return setHeader("Cookie", this.mHeaders.get("Cookie") + "; ");
    }

    public HttpsConnection setAuthorization(final String login, final String pass) {
        setHeader("Authorization", "Basic " + Base64.encode((login + ':' + pass).getBytes()));
        return this;
    }

    private void setHeadersToConnection(final HttpURLConnection connection, final Map<String, String> headers) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    public HttpsConnection openConnection(final String url) throws BooruEngineException {
        try {
            return openConnection(new URL(url));
        } catch (MalformedURLException e) {
            throw new BooruEngineException(e);
        }
    }

    public HttpsConnection openConnection(final URL url) throws BooruEngineException {
        switch (this.mRequestMethod) {
            case GET: {
                GET(url);
                break;
            }
            case POST:{
                POST(url);
                break;
            }
            default: {
                throw new BooruEngineException("Method " + this.mRequestMethod + "is not supported.");
            }
        }
        return this;
    }

    private void GET(final URL url) throws BooruEngineException {
        try {
            HttpURLConnection connection;
            //choose protocol
            if (url.getProtocol().equals("https")) connection = (HttpsURLConnection) url.openConnection();
            else connection = (HttpURLConnection) url.openConnection();

            log.log(Level.INFO, "Using protocol: " + url.getProtocol());

            connection.setRequestMethod("GET");
            setHeadersToConnection(connection, this.mHeaders);

            this.mResponseCode = connection.getResponseCode();
            this.mResponseMessage = connection.getResponseMessage();
            this.mResponseHeaders = connection.getHeaderFields();

            StringBuilder response = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input;
            while ((input = reader.readLine()) != null) response.append(input);
            reader.close();
            this.mResponse = response.toString();

        } catch (Exception e) {
            throw new BooruEngineException(e);
        }
    }

    private void POST(final URL url) throws BooruEngineException {
        try {
            HttpURLConnection connection;

            //choose protocol
            if (url.getProtocol().equals("https")) connection = (HttpsURLConnection) url.openConnection();
            else connection = (HttpURLConnection) url.openConnection();

            log.log(Level.INFO, "Using protocol: " + url.getProtocol());

            //set meta-data
            connection.setRequestMethod("POST");
            setHeadersToConnection(connection, this.mHeaders);

            connection.setInstanceFollowRedirects(false);

            //send post request
            connection.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(this.mBody);
            outputStream.flush();
            outputStream.close();

//            //get response
//            if (connection.getResponseCode() != 200) {
//                throw new BooruEngineException(returnException(connection.getResponseCode()));
//            }
//            log.log(Level.INFO, this.mRequestMethod + " request success.");

            this.mResponseCode = connection.getResponseCode();
            this.mResponseMessage = connection.getResponseMessage();
            this.mResponseHeaders = connection.getHeaderFields();

            StringBuilder response = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input;
            while ((input = reader.readLine()) != null) response.append(input);
            reader.close();
            this.mResponse = response.toString();

        } catch (Exception e) {
            throw new BooruEngineException(e);
        }
    }

    public String getResponse() {
        return this.mResponse;
    }

    public int getResponseCode() {
        return this.mResponseCode;
    }

    public String getResponseMessage() {
        return this.mResponseMessage;
    }

    public Map<String, List<String>> getHeaders() {
        return this.mResponseHeaders;
    }

    public List<String> getHeader(String key) {
        return this.mResponseHeaders.get(key);
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
}
