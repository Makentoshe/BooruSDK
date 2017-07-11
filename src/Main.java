import engine.HttpConnection;
import engine.parser.XmlParser;
import source.boor.Gelbooru;

import java.io.*;

/**
 * Sas
 */
public class Main {

    public static void main(String[] args) throws Exception{
        String str = new HttpConnection(false).getRequest(Gelbooru.get().getCompleteRequest(5, "hatsune_miku", 0));

        XmlParser parser = new XmlParser();
        InputStream is = new ByteArrayInputStream(str.getBytes());

        parser.startParse(is);
        System.out.println(parser.getResult().size());
    }
}
