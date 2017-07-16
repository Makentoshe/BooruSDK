import engine.HttpConnection;
import engine.parser.JsonParser;
import source.boor.*;
import source.еnum.Format;

import java.util.*;

/**
 */
public class Main {

    public static void main(String[] args) throws Exception{
        String request = Behoimi.get().getCompleteRequest(1, "hatsune_miku", 0, Format.JSON);

        HttpConnection connection = new HttpConnection(false);
        String responseData = connection.getRequest(request);

        JsonParser parser = new JsonParser();

        System.out.println(responseData);

        List<HashMap<String, String>> list = parser.startParse(responseData);
        HashMap<String, String> data = list.get(0);
        Set<Map.Entry<String, String>> entrySet = data.entrySet();
        for (Map.Entry<String,String> pair : entrySet) {
            System.out.println(pair.getKey() + ": " + pair.getValue());
        }

    }
}
