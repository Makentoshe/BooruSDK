package engine.connector;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import engine.BooruEngineConnectionException;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class is support for HTTP and HTTPS specific features and chooses which protocol to use.
 * <p>
 * Each HttpConnection instance is used to make a single request
 * but the underlying network connection to the HTTP server may be
 * transparently shared by other instances.
 *
 * <p>As default the automatic redirection is disabled
 * for getting data from request with 302 code or something else.
 * Class allows to abstract from the <code>HttpURLConnection</code> and <code>HttpsURLConnection</code> classes
 * and work with requests data and responses. But if there is a need to work with the base class
 * in a specific entity of this class, there is a <code>getConnection()</code> method
 * for getting <code>HttpUrlConnection</code> or <code>HttpsUrlConnection</code>.
 *
 * <p>This is a very simple to usage. It first all data must be setted
 * with the set methods - headers, body, request method and else.
 * Then the connection will be create with <code>openConnection</code> methods.
 * After this class will be create connections and send all data.
 * In the end, when the request is complete the response data will be available,
 * but trying to send some data again - the exception will be threw.
 *
 * @see     java.net.HttpURLConnection
 * @see     javax.net.ssl.HttpsURLConnection
 */
public class HttpsConnection implements Serializable {

    private static String sDefaultUserAgent = "BooruEngineLib/1.0";

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

    /**
     * Default constructor with offed logging.
     */
    public HttpsConnection() {
        log.setFilter(record -> false);
    }


    /**
     * Constructor with choice.
     *
     * @param isLogging - switching logging.
     */
    public HttpsConnection(final boolean isLogging) {
        log.setFilter(record -> isLogging);
    }

    /**
     * Set request method for connection.
     *
     * @param requestMethod request method.
     * @return self.
     */
    public HttpsConnection setRequestMethod(final Method requestMethod) {
        log.log(Level.INFO, "Set Request Method: " + requestMethod);
        this.mRequestMethod = requestMethod;
        return this;
    }

    /**
     * Create header for connection
     *
     * @param key header name.
     * @param value header data.
     * @return self.
     */
    public HttpsConnection setHeader(final String key, final String value) {
        log.log(Level.INFO, "Set Header: " + key + ": " + value);
        this.mHeaders.put(key, value);
        return this;
    }

    /**
     * Set user-agent for connection.
     *
     * @param userAgent user agent.
     * @return self.
     */
    public HttpsConnection setUserAgent(final String userAgent) {
        setHeader("User-Agent", userAgent);
        return this;
    }

    /**
     * Create body for connection. Used, when request method is POST and ignored otherwise.
     *
     * @param body connection body.
     * @return self.
     */
    public HttpsConnection setBody(final String body){
        this.mBody = body;
        return this;
    }

    /**
     * Set cookies for connection.
     *     Example:
     *     cookie1=data1; cookie2=data2
     *
     * @param cookies cookie data.
     * @return self.
     */
    public HttpsConnection setCookies(final String cookies) {
        setHeader("Cookie", cookies);
        return this;
    }

    /**
     * Add cookie for created cookies.
     *
     * @param cookie cookie data.
     * @return self.
     */
    public HttpsConnection addCookie(final String cookie) {
        return setHeader("Cookie", this.mHeaders.get("Cookie") + "; ");
    }

    private HttpsConnection setHeaders(final HttpURLConnection connection, final Map<String, String> headers) throws BooruEngineConnectionException {
        try {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            connection.setRequestMethod(this.mRequestMethod.toString());
            connection.setInstanceFollowRedirects(false);
        }catch (ProtocolException e){
            throw new BooruEngineConnectionException(e);
        }
        return this;
    }

    /**
     * Create connection to server with url address.
     *
     * @param url url.
     * @return self.
     * @throws BooruEngineConnectionException when something go wrong. Use <code>getCause</code> to see more details.
     */
    public HttpsConnection openConnection(final String url) throws BooruEngineConnectionException {
        try {
            return openConnection(new URL(url));
        } catch (MalformedURLException e) {
            throw new BooruEngineConnectionException(e);
        }
    }

    /**
     * Create connection to server with url address.
     *
     * @param url url.
     * @return self.
     * @throws BooruEngineConnectionException when something go wrong. Use <code>getCause</code> to see more details.
     */
    public HttpsConnection openConnection(final URL url) throws BooruEngineConnectionException {
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
                throw new BooruEngineConnectionException(new UnsupportedOperationException("Method " + this.mRequestMethod + "is not supported."));
            }
        }
        return this;
    }

    private void sendPost(final String data) throws BooruEngineConnectionException{
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
            throw new BooruEngineConnectionException(e);
        }
    }

    private void GET(final URL url) throws BooruEngineConnectionException {
        chooseProtocol(url);

        setHeaders(mConnection, this.mHeaders);
    }

    private void POST(final URL url) throws BooruEngineConnectionException {
        chooseProtocol(url);

        setHeaders(mConnection, this.mHeaders);
        mConnection.setDoOutput(true);

        if (this.mBody != null) {
            sendPost(this.mBody);
        }
    }

    /**
     * Get the server response.
     *
     * @return response.
     * @throws BooruEngineConnectionException  when something go wrong. Use <code>getCause</code> to see more details.
     */
    public String getResponse() throws BooruEngineConnectionException {
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
            throw new BooruEngineConnectionException(e);

        }
    }

    /**
     * Get response code.
     *
     * @return response code.
     * @throws BooruEngineConnectionException when something go wrong. Use <code>getCause</code> to see more details.
     */
    public int getResponseCode() throws BooruEngineConnectionException {
        try {
            if (this.mResponseCode == -1) this.mResponseCode = mConnection.getResponseCode();
            return this.mResponseCode;
        }catch (IOException e){
            mErrStream = mConnection.getErrorStream();
            throw new BooruEngineConnectionException(e);
        }
    }

    /**
     * Get response message.
     *
     * @return response message.
     * @throws BooruEngineConnectionException when something go wrong. Use <code>getCause</code> to see more details.
     */
    public String getResponseMessage() throws BooruEngineConnectionException {
        try {
            if (this.mResponseMessage == null) {
                this.mResponseMessage = mConnection.getResponseMessage();
            }
            return this.mResponseMessage;
        }catch (IOException e){
            mErrStream = mConnection.getErrorStream();
            throw new BooruEngineConnectionException(e);
        }
    }

    /**
     * Get headers, which will be in hashmap.
     *
     * @return headers.
     */
    public Map<String, List<String>> getHeaders(){
        if (this.mResponseHeaders == null){
            this.mResponseHeaders = mConnection.getHeaderFields();
        }
        return this.mResponseHeaders;
    }

    /**
     * Get header. The value will be in list of strings.
     *
     * @return header.
     */
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

    private void chooseProtocol(final URL url) throws BooruEngineConnectionException{
        //choose protocol
        try {
            if (url.getProtocol().equals("https")) this.mConnection = (HttpsURLConnection) url.openConnection();
            else this.mConnection = (HttpURLConnection) url.openConnection();
            log.log(Level.INFO, "Using protocol: " + url.getProtocol());
        }catch (IOException ioe1) {
            mErrStream = mConnection.getErrorStream();
            throw new BooruEngineConnectionException(ioe1);
        }
    }

    @Override
    public String toString() {
        return this.mRequestMethod + "\n" + this.mHeaders + "\n" + this.mBody;
    }

    /**
     * Returns the error stream if the connection failed
     * but the server sent useful data nonetheless.
     *
     * <p>This method will not cause a connection to be initiated. If
     * the connection was not connected, or if the server did not have
     * an error while connecting or if the server had an error but
     * no error data was sent, this method will return null.
     *
     * @return an error stream if any, null if there have been no
     * errors, the connection is not connected or the server sent no
     * useful data.
     */
    public InputStream getErrorStrean(){
        return this.mErrStream;
    }

    /**
     * Return a default <code>URLConnection</code> for performing specific actions with connection.
     *
     * @return default java connection.
     */
    public URLConnection getConnection(){
        return this.mConnection;
    }

    /**
     * Change the default user agent by custom.
     * The new user agent will be user in ALL realisations of this class.
     * @param newUserAgent - new user agent.
     */
    public static void setDefaultUserAgent(final String newUserAgent){
        sDefaultUserAgent = newUserAgent;
    }

    /**
     * Get default user agent.
     *
     * @return default user agent.
     */
    public static String getDefaultUserAgent(){
        return sDefaultUserAgent;
    }

}
