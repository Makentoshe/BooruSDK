package engine.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import com.sun.org.apache.xpath.internal.SourceTree;
import engine.BooruEngineException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import source.еnum.Boor;

/**
 * Parser posts and comments from xml from url or stream,
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
    private final HashMap<String, String> data = new HashMap<>();
    private final StringBuilder builder = new StringBuilder();
    //for parsing elements value
    private boolean isParsing = false;


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
    public void startElement(String uri, String localName, String elementName, Attributes attributes) throws SAXException {
        if ("posts".equals(elementName) || "comments".equals(elementName)) return;
        //parsing posts with simple structure
        if (("post".equals(elementName) || "comment".equals(elementName)) && attributes.getLength() > 1){
            parsePostAttributes(attributes);
            return;
        }
        //now we start parsing the element value
        isParsing = true;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        //while element not end - append all characters to value.
        if (isParsing) builder.append(new String(ch, start, length));
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (isParsing) {
            isParsing = false;
            while(builder.length() > 0 && (builder.charAt(0) == '\n' || builder.charAt(0) == ' ')) builder.delete(0, 1);
            data.put(qName, builder.toString());
            builder.delete(0, builder.length());
        }
        if ("post".equals(qName) || "comment".equals(qName)) {
            result.add(new HashMap<>(data));
            data.clear();
        }
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

    private void parsePostAttributes(Attributes attributes){
        boolean flag = false;
        //all data about one post/comment/etc
        //for all attributes
        for(int i = 0; i < attributes.getLength(); i++) {
            //we put it in hash map
            data.put(attributes.getQName(i), attributes.getValue(i));
            //setup boor
            if (attributes.getQName(i).contains("url") && !flag){
                data.put("boor", extractBoor(attributes.getValue(i)));
                flag = true;
            }
        }
        //add data in result
        result.add(new HashMap<>(data));
        //clear hashmap for new data;
        data.clear();
    }
}
