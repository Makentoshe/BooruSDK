import engine.HttpConnection;
import engine.parser.XmlParser;
import source.boor.Behoimi;
import source.еnum.Format;

import java.io.ByteArrayInputStream;


/**
 */
public class Main {

    public static void main(String[] args) throws Exception{
        String request = Behoimi.get().getCompleteRequest(2, "hatsune_miku", 0, Format.XML);

        HttpConnection connection = new HttpConnection(false);
        String responseData = connection.getRequest(request);

        //not reusable
        XmlParser parser = new XmlParser(false);
        parser.startParse(new ByteArrayInputStream(responseData.getBytes()));

        parser.startParse(new ByteArrayInputStream(responseData.getBytes()));

        System.out.println(parser.getResult().size());
        System.out.println(parser.getResult().size());

        //System.out.println(parser.getResult());

    }
}
