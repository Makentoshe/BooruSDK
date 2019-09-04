package engine.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import engine.APIException;
import engine.BooruEngineException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import source.еnum.Boor;

/**
 * Parsing posts and comments xml, from url or stream, which has 2 structure types -
 * the data storage in attributes and the data storage in elements.
 * That mean, that you can parse xml as effectively as JSON.
 * <p>
 * When you create new instance of this class, you can specify in constructor,
 * will be the result data of this instance reusable or not.
 * If data reusable - when you start new parsing process(call <tt>startParse()</tt> again)
 * all data from last parsing process will be cleared.
 * If instance not reused -  results will be collect in <tt>getResult()</tt> method.
 * You will can get all results by one call.
 * And, of course, you can't start new process from zero.
 * As default - instance will be reused.
 */
public class XmlParser extends DefaultHandler {

    private final List<HashMap<String, String>> result = new ArrayList<>();
    private boolean reusable;
    private final HashMap<String, String> data = new HashMap<>();
    private final StringBuilder builder = new StringBuilder();
    //for parsing elements value
    private boolean isParsing = false;

    /**
     * @param isReusable you can specify reusability.
     */
    public XmlParser(boolean isReusable){
        reusable = isReusable;
    }

    /**
     * Default constructor - reusability is enable.
     */
    public XmlParser(){
        reusable = true;
    }

    /**
     * Parses an XML file from url into memory.
     * <p>
     * The parse result can be get with {@code getResult()} method help.
     *
     * @param url - url to data.
     * @throws BooruEngineException - when parsing is going wrong.
     * Use <tt>getCause</tt> to see more details.
     */
    public XmlParser startParse(String url) throws BooruEngineException {
        if (reusable && result.size() > 0) result.clear();

        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            factory.newSAXParser().parse(url, this);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new BooruEngineException(e);
        }
        return this;
    }

    /**
     * Parses an XML file from stream into memory.
     * <p>
     * The parse result can be get with {@code getResult()} method help.
     *
     * @param stream - data stream.
     * @throws BooruEngineException - when parsing is going wrong.
     * Use <tt>getCause</tt> to see more details.
     */
    public void startParse(InputStream stream) throws BooruEngineException {
        if (reusable) result.clear();

        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            factory.newSAXParser().parse(stream, this);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new BooruEngineException(e);
        }
    }

    /**
     * Event: Parser starts reading an element.
     */
    @Override
    public void startElement(String uri, String localName, String elementName, Attributes attributes) throws SAXException {
        if ("response".equals(elementName)){
            boolean _throw = false;
            String reason = "";
            for (int i = 0; i < attributes.getLength(); i++){
                if ("success".equals(attributes.getQName(i))){
                    _throw = true;
                }
                if ("reason".equals(attributes.getQName(i))){
                    reason = attributes.getValue(i);
                }
            }
            if (_throw){
                throw new RuntimeException(new APIException(reason));
            }
        }
        if ("posts".equals(elementName) || "comments".equals(elementName)) return;
        //parsing posts with simple structure
        if (("post".equals(elementName) || "comment".equals(elementName)) && attributes.getLength() > 1){
            parsePostAttributes(attributes);
            return;
        }
        //now we start parsing the element value
        isParsing = true;
    }

    /**
     * Event: Parser reading an element data.
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        //while element not end - append all characters to value.
        if (isParsing) builder.append(new String(ch, start, length));
    }

    /**
     * Event: Parser ends reading an element.
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (isParsing) {
            isParsing = false;
            while(builder.length() > 0 && (builder.charAt(0) == '\n' || builder.charAt(0) == ' ')) builder.delete(0, 1);
            data.put(qName, builder.toString());
            builder.delete(0, builder.length());
        }
        if (("post".equals(qName) || "comment".equals(qName)) && data.size() != 0) {
            result.add(new HashMap<>(data));
            data.clear();
        }
    }

    /**
     * @return list of Hashmaps, where each Hashmap describe one post.
     * Hashmap has next structure - &lt;Attribute_name, Attrubute_value&gt;.
     */
    public List<HashMap<String, String>> getResult() {
        return result;
    }

    /**
     * @return is parser reusable or not.
     */
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
