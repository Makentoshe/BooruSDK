package test.engine.parser;

import engine.parser.xml.BasicPostParser;
import org.junit.Test;
import source.boor.Gelbooru;

import static org.junit.Assert.*;

/**
 * Created by Makentoshe on 10.07.2017.
 */
public class BasicPostParserTest {

    @Test
    public void goodResult() throws Exception {
        BasicPostParser parser = new BasicPostParser();
        parser.startParse(Gelbooru.get().getCompleteRequest(2, "hatsune_miku", 0));

        assertEquals(2, parser.getResult().size());
    }

    @Test
    public void goodResult2() throws Exception {
        BasicPostParser parser = new BasicPostParser();
        parser.startParse(Gelbooru.get().getCompleteRequest(200, "hatsune_miku", 0));

        assertEquals(200, parser.getResult().size());
    }

}