package engine;

import com.sun.istack.internal.NotNull;

import java.io.*;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The class for creating multipart/form-data body for {@code Method.POST} requests.
 * It had basic boundary which construct from final string and random string value.
 * Each new multipart body will be contain unique boundary.
 * <p>
 * Main function is create, construct and send multipart body to server. All operations
 * using streams and after sending data they all will be automatically closed.
 * <p>
 * Blocks will be connected in the same order in which they were created.
 * Each block before the connection will be in the stream.
 * So, if first will be created a File Block, and then 3 Data Blocks - the structure will be the next
 * <p>File Block
 * <p>Data Block
 * <p>Data Block
 * <p>Data Block
 * <p>All the blocks will eventually merge into one output stream, which will be sent data to the server.
 */
public class MultipartConstructor implements Serializable {

    private List<InputStream> mData = new ArrayList<>();

    private final String BOUNDARY = "----BooruEngineLibBoundary_" + randomStringGenerator();

    /**
     * Create simple data block which contain
     * <p>--<code>BOUNDARY</code>
     * <p>Content-Disposition: form-data; name=<code>name</code>
     * <p>
     * <p><code>data</code>
     *
     * @param name block name.
     * @param data data to send.
     * @return self for realise chaining.
     */
    public MultipartConstructor createDataBlock(@NotNull final String name, final String data) {
        this.mData.add(new Multipart(name, (data == null ? "" : data), BOUNDARY).getResult());
        return this;
    }

    /**
     * Create simple data block which contain
     * <p>--<code>BOUNDARY</code>
     * <p>Content-Disposition: form-data; name="<code>name</code>"; filename="<code>filename_here</code>"
     * <p>Content-Type: <code>file_type</code> - as example - image/jpeg
     * <p>
     * <p><code>file</code> in binary form.
     *
     * @param name block name.
     * @param file file to send.
     * @return self for realise chaining.
     */
    public MultipartConstructor createFileBlock(@NotNull final String name, @NotNull final File file) throws IOException {
        this.mData.add(new Multipart(name, file, BOUNDARY).getResult());
        return this;
    }


    /**
     * Method will be append all created blocks and send them to stream.
     * It also create final boundary.
     *
     * @param stream output stream
     * @throws IOException if an I/O error occurs.
     */
    public void send(OutputStream stream) throws IOException {
        this.mData.add(Multipart.createFinalisation(BOUNDARY));

        byte[] buffer = new byte[4096];
        int bytesRead;

        for (InputStream inputStream : this.mData) {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                stream.write(buffer, 0, bytesRead);
            }
            stream.flush();
            inputStream.close();
        }
        stream.close();
    }

    private String randomStringGenerator() {
        final char[] CHARSET_AZ_az_09 = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghgklmnopqrstuvwxyz0123456789").toCharArray();
        Random random = new SecureRandom();
        char[] out = new char[5];
        for (int i = 0; i < out.length; i++) {
            int randomCharIndex = random.nextInt(CHARSET_AZ_az_09.length);
            out[i] = CHARSET_AZ_az_09[randomCharIndex];
        }
        return new String(out);
    }

    /**
     * Get boundary, specified for current class.
     */
    public String getBoundary() {
        return BOUNDARY;
    }
}

class Multipart implements Serializable {

    private final static String LINE_FEED = "\r\n";

    private InputStream resultData;

    Multipart(final String name, final String data, final String boundary) {
        resultData = new ByteArrayInputStream(("--" + boundary + LINE_FEED +
                "Content-Disposition: form-data; name=\"" + name + "\"" + LINE_FEED + LINE_FEED +
                data + LINE_FEED).getBytes());
    }

    Multipart(final String name, final File file, final String boundary) throws IOException {
        String data = "--" + boundary + LINE_FEED +
                "Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + file.getName() + "\"" + LINE_FEED +
                "Content-Type: " + URLConnection.guessContentTypeFromName(file.getName()) + LINE_FEED +
                "Content-Transfer-Encoding: binary" + LINE_FEED + LINE_FEED;

        FileInputStream fileInputStream = new FileInputStream(file);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        outputStream.close();

        resultData = new SequenceInputStream(
                new ByteArrayInputStream(data.getBytes()),
                new SequenceInputStream(
                        new ByteArrayInputStream(outputStream.toByteArray()),
                        new ByteArrayInputStream(LINE_FEED.getBytes())));

    }

    static InputStream createFinalisation(final String boundary) {
        return new ByteArrayInputStream(("--" + boundary + "--" + LINE_FEED).getBytes());
    }

    InputStream getResult() {
        return resultData;
    }

}