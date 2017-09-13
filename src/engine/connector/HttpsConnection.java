package engine.connector;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import engine.BooruEngineException;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpsConnection {

    private static String sDefaultUserAgent = "(mkliverout@gmail.com)BooruEngineLib/1.0";

    private final Map<String, String> mHeaders = new HashMap<>(); //request headers
    private Method mRequestMethod;
    private String mBody;
    private InputStream mErrStream;

    //after getting response
    private int mResponseCode = -1;
    private String mResponse;
    private String mResponseMessage;
    private Map<String, List<String>> mResponseHeaders;
    private HttpURLConnection mConnection;

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

    private void setHeaders(final HttpURLConnection connection, final Map<String, String> headers) throws BooruEngineException {
        try {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            connection.setRequestMethod(this.mRequestMethod.toString());
            connection.setInstanceFollowRedirects(false);
        }catch (ProtocolException e){
            throw new BooruEngineException(e);
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
                throw new BooruEngineException(new UnsupportedOperationException("Method " + this.mRequestMethod + "is not supported."));
            }
        }
        return this;
    }

    private void sendPost(final String data) throws BooruEngineException{
        try{
            if (data != null) {
                mConnection.setRequestProperty("Content-Length", String.valueOf(data.length()));
                //send post
                DataOutputStream outputStream = new DataOutputStream(this.mConnection.getOutputStream());
                outputStream.writeBytes(data);
                outputStream.flush();
                outputStream.close();
            }
        } catch (IOException e) {
            this.mErrStream = this.mConnection.getErrorStream();
            throw new BooruEngineException(e);
        }
    }

    private void GET(final URL url) throws BooruEngineException {
        chooseProtocol(url);

        setHeaders(mConnection, this.mHeaders);
    }

    private void POST(final URL url) throws BooruEngineException {
        chooseProtocol(url);

        setHeaders(mConnection, this.mHeaders);
        mConnection.setDoOutput(true);

        if (this.mBody != null) {
            sendPost(this.mBody);
        }
    }

    public String getResponse() throws BooruEngineException {
        try {
            if (this.mResponse == null){
                StringBuilder response = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(mConnection.getInputStream()));
                String input;
                while ((input = reader.readLine()) != null) response.append(input);
                reader.close();
                this.mResponse = response.toString();
            }
            return this.mResponse;
        } catch (IOException e){
            mErrStream = mConnection.getErrorStream();
            throw new BooruEngineException(e);

        }
    }

    public int getResponseCode() throws BooruEngineException {
        try {
            if (this.mResponseCode == -1) this.mResponseCode = mConnection.getResponseCode();
            return this.mResponseCode;
        }catch (IOException e){
            mErrStream = mConnection.getErrorStream();
            throw new BooruEngineException(e);
        }
    }

    public String getResponseMessage() throws BooruEngineException {
        try {
            if (this.mResponseMessage == null) {
                this.mResponseMessage = mConnection.getResponseMessage();
            }
            return this.mResponseMessage;
        }catch (IOException e){
            mErrStream = mConnection.getErrorStream();
            throw new BooruEngineException(e);
        }
    }

    public Map<String, List<String>> getHeaders() throws BooruEngineException {
        if (this.mResponseHeaders == null){
            this.mResponseHeaders = mConnection.getHeaderFields();
        }
        return this.mResponseHeaders;
    }

    public List<String> getHeader(String key) {
        if (this.mResponseHeaders == null){
            this.mResponseHeaders = mConnection.getHeaderFields();
        }
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

    private void chooseProtocol(final URL url) throws BooruEngineException{
        //choose protocol
        try {
            if (url.getProtocol().equals("https")) this.mConnection = (HttpsURLConnection) url.openConnection();
            else this.mConnection = (HttpURLConnection) url.openConnection();
            log.log(Level.INFO, "Using protocol: " + url.getProtocol());
        }catch (IOException ioe1) {
            mErrStream = mConnection.getErrorStream();
            throw new BooruEngineException(ioe1);
        }
    }

    @Override
    public String toString() {
        return this.mRequestMethod + "\n" + this.mHeaders + "\n" + this.mBody;
    }

    public InputStream getErrorStrean(){
        return this.mErrStream;
    }

    public URLConnection getConnection(){
        return this.mConnection;
    }

    public static void setDefaultUserAgent(final String newUserAgent){
        sDefaultUserAgent = newUserAgent;
    }

    public static String getDefaultUserAgent(){
        return sDefaultUserAgent;
    }

}
