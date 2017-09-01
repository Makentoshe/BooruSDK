package engine;

import com.sun.istack.internal.NotNull;

import java.io.*;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Makentoshe on 01.09.2017.
 */
public class MultipartConstructor {

    public final String BOUNDARY = "----BooruEngineLibBoundary_" + randomStringGenerator();

    private List<InputStream> mData = new ArrayList<>();

    public MultipartConstructor createDataBlock(@NotNull final String name, final String data) {
        this.mData.add(new Multipart(name, (data == null ? "" : data), BOUNDARY).getResult());
        return this;
    }

    public MultipartConstructor createFileBlock(@NotNull final String name, @NotNull final File file) throws IOException {
        this.mData.add(new Multipart(name, file, BOUNDARY).getResult());
        return this;
    }

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
}

class Multipart {

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