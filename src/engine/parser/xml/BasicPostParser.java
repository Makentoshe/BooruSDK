package engine.parser.xml;

import engine.parser.xml.AbstractParser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * TODO: remake xml parser. Remove AbstractParser.
 * Parser posts, which have basic structure - post with data in attributes.
 */
public class BasicPostParser extends AbstractParser {

    private final List<HashMap<String, Object>> result = new ArrayList<>();

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

    @Override
    public List getResult() {
        return result;
    }
}
