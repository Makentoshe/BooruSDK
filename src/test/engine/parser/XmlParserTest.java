package test.engine.parser;

import engine.HttpConnection;
import engine.parser.XmlParser;
import org.junit.Test;
import source.boor.Danbooru;
import source.boor.Gelbooru;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

public class XmlParserTest {

    @Test
    public void startParse_Url_Test() throws Exception {
        int count = 3;
        XmlParser parser = new XmlParser();

        parser.startParse(Gelbooru.get().getCompleteRequest(count, "hatsune_miku", 0));
        assertEquals(count, parser.getResult().size());

        parser.startParse(Danbooru.get().getCustomRequest("limit=" + count));
        assertEquals(count, parser.getResult().size());
    }

    @Test
    public void startParse_Stream_Test() throws Exception {
        XmlParser parser = new XmlParser();

        String xml = new HttpConnection(false).getRequest(Gelbooru.get().getCompleteRequest(2, "hatsune_miku", 0));
        InputStream stream = new ByteArrayInputStream(xml.getBytes());
        parser.startParse(stream);

        assertEquals(2, parser.getResult().size());
    }
}