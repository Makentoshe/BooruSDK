package test.engine.parser;

import engine.HttpConnection;
import engine.parser.XmlParser;
import org.junit.Test;
import source.boor.Danbooru;
import source.boor.Gelbooru;
import source.еnum.Format;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

public class XmlParserTest {

    @Test
    public void startParse_Url_Test() throws Exception {
        int count = 3;
        XmlParser parser = new XmlParser();

        parser.startParse(Gelbooru.get().getPackRequest(count, "hatsune_miku", 0));
        assertEquals(count, parser.getResult().size()); //after this result will be reset

        assertEquals(0, parser.getResult().size());
    }

    @Test
    public void startParse_Stream_Test() throws Exception {
        XmlParser parser = new XmlParser(false);

        String xml = new HttpConnection(false).getRequest(Gelbooru.get().getPackRequest(2, "hatsune_miku", 0));
        InputStream stream = new ByteArrayInputStream(xml.getBytes());
        parser.startParse(stream);

        assertEquals(2, parser.getResult().size());
    }

    @Test
    public void reusable_Test() throws Exception{
        XmlParser parser = new XmlParser(false);

        parser.startParse(Gelbooru.get().getPackRequest(2, "hatsune_miku", 0));
        assertEquals(2, parser.getResult().size());

        parser.startParse(Gelbooru.get().getPackRequest(8, "hatsune_miku", 0));
        assertEquals(10, parser.getResult().size());
    }
}