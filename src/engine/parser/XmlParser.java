package engine.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import engine.BooruEngineException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parser posts from xml from url or stream,
 * which have basic structure - post with data in attributes.
 * Instance of this class can be reused, because after getting result -
 * all data will be reset.
 */
public class XmlParser extends DefaultHandler {

    private final List<HashMap<String, Object>> result = new ArrayList<>();

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
     * Parses an XML file from stream into memory
     */
    public void startParse(InputStream stream) throws BooruEngineException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            factory.newSAXParser().parse(stream, this);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new BooruEngineException(e.getMessage());
        }
    }

    /**
     * Event: Parser starts reading an element
     */
    @Override
    public void startElement(String s1, String s2, String elementName, Attributes attributes) throws SAXException {
        if (!"post".equals(elementName)) return;
        //all data about one post
        HashMap<String, Object> post = new HashMap<>();
        //for all attributes in this post
        for(int i = 0; i < attributes.getLength(); i++) {
            //we put it in hash map
            post.put(attributes.getQName(i), attributes.getValue(i));
        }
        //add post in result
        result.add(post);
    }


    /**
     * @return list of posts
     * We can get result only once.
     * After, the data will be clear.
     */
    public List getResult() {
        List list = new ArrayList(result);
        result.clear();
        return list;
    }
}
