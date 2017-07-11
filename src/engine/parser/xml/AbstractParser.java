package engine.parser.xml;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import engine.BooruEngineException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parse xml from url or file.
 */
abstract class AbstractParser extends DefaultHandler {

    /**
     * Parses an XML file from url into memory
     */
    public void startParse(String url) throws BooruEngineException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            factory.newSAXParser().parse(url, this);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new BooruEngineException(e.getMessage());
        }
    }

    /**
     * Parses an XML file from file into memory
     */
    public void startParse(File file) throws BooruEngineException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            factory.newSAXParser().parse(file, this);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new BooruEngineException(e.getMessage());
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
