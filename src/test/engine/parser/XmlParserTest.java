package test.engine.parser;

import engine.connector.HttpsConnection;
import engine.connector.Method;
import engine.parser.XmlParser;
import org.junit.Test;
import source.boor.Gelbooru;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

public class XmlParserTest {

    @Test
    public void startParse_Url_Test() throws Exception {
        int count = 3;
        XmlParser parser = new XmlParser();

        parser.startParse(Gelbooru.get().getPostsByTagsRequest(count, "hatsune_miku", 0));
        assertEquals(count, parser.getResult().size());
    }

    @Test
    public void startParse_Stream_Test() throws Exception {
        XmlParser parser = new XmlParser(false);

        String xml = new HttpsConnection()
                .setRequestMethod(Method.GET)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .openConnection(Gelbooru.get().getPostsByTagsRequest(2, "hatsune_miku", 0))
                .getResponse();

        InputStream stream = new ByteArrayInputStream(xml.getBytes());
        parser.startParse(stream);

        assertEquals(2, parser.getResult().size());
    }

    @Test
    public void reusable_Test() throws Exception{
        XmlParser parser = new XmlParser(false);

        parser.startParse(Gelbooru.get().getPostsByTagsRequest(2, "hatsune_miku", 0));
        assertEquals(2, parser.getResult().size());

        parser.startParse(Gelbooru.get().getPostsByTagsRequest(8, "hatsune_miku", 0));
        assertEquals(10, parser.getResult().size());
    }

    @Test
    public void notReusable_Test() throws Exception{
        XmlParser parser = new XmlParser(true);

        parser.startParse(Gelbooru.get().getPostsByTagsRequest(2, "hatsune_miku", 0));
        assertEquals(2, parser.getResult().size());

        parser.startParse(Gelbooru.get().getPostsByTagsRequest(8, "hatsune_miku", 0));
        assertEquals(8, parser.getResult().size());
    }
}