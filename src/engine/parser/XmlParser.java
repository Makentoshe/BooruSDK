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
import source.еnum.Boor;

/**
 * Parser posts from xml from url or stream,
 * which have basic structure - post with data in attributes.
 *
 * Instance of this class can be reused by option in constructor.
 * As default - instance will be reused.
 * If instance not reused - results will be collect in getResult() method.
 * We will can get all results by one call.
 */
public class XmlParser extends DefaultHandler {

    private final List<HashMap<String, String>> result = new ArrayList<>();
    private boolean reusable;

    public XmlParser(boolean reusable){
        this.reusable = reusable;
    }

    public XmlParser(){
        reusable = true;
    }

    /**
     * Parses an XML file from url into memory
     */
    public void startParse(String url) throws BooruEngineException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            factory.newSAXParser().parse(url, this);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new BooruEngineException(e);
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
            throw new BooruEngineException(e);
        }
    }

    /**
     * Event: Parser starts reading an element
     */
    @Override
    public void startElement(String s1, String s2, String elementName, Attributes attributes) throws SAXException {
        if (!"post".equals(elementName)) return;
        boolean flag = false;
        //all data about one post
        HashMap<String, String> post = new HashMap<>();
        //for all attributes in this post
        for(int i = 0; i < attributes.getLength(); i++) {
            //we put it in hash map
            post.put(attributes.getQName(i), attributes.getValue(i));
            //setup boor
            if (attributes.getQName(i).contains("url") && !flag){
                post.put("boor", extractBoor(attributes.getValue(i)));
                flag = true;
            }
        }
        //add post in result
        result.add(post);
    }


    /**
     * @return list of posts
     * If reusable flag enable we can get result only once.
     * After, the data will be clear.
     */
    public List getResult() {
        if (reusable) {
            List list = new ArrayList(result);
            result.clear();
            return list;
        }
        return result;
    }

    public boolean isReusable(){
        return reusable;
    }

    private String extractBoor(String url) {
        String boor = "";
        if (url.contains("gelbooru")) boor = Boor.Gelbooru.toString();
        else if (url.contains("rule34")) boor = Boor.Rule34.toString();
        else if (url.contains("safebooru")) boor = Boor.Safebooru.toString();
        return boor;
    }
}
