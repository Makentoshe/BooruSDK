package engine.parser;

import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parse xml from url.
 */
abstract class AbstractParser extends DefaultHandler {

    /**
     * Parses an XML file into memory
     */
    public void startParse(String url) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            factory.newSAXParser().parse(url, this);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    /**
     * Event: Parser starts reading an element
     */
    @Override
    public abstract void startElement(String s1, String s2, String elementName, Attributes attributes) throws SAXException;


    /**
     * @return list of posts
     */
    public abstract List getResult();
}
