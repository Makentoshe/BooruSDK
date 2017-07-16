import engine.HttpConnection;
import engine.item.Item;
import engine.parser.JsonParser;
import engine.parser.XmlParser;
import source.boor.*;


import java.util.*;

/**
 */
public class Main {

    public static void main(String[] args) throws Exception{
        String request = Danbooru.get().getCompleteRequest(1, "hatsune_miku", 0);

        HttpConnection connection = new HttpConnection(false);
        String responseData = connection.getRequest(request);

        System.out.println(connection.getRequest(Danbooru.get().getCustomRequest("md5=cbb9f2e3eeac70328829d248d1f92f50")));

        JsonParser parser = new JsonParser();

        List<HashMap<String, String>> list = parser.startParse(responseData);
        HashMap<String, String> data = list.get(0);
        Set<Map.Entry<String, String>> entrySet = data.entrySet();
//        for (Map.Entry<String,String> pair : entrySet) {
//            System.out.println(pair.getKey() + ": " + pair.getValue());
//        }

        Item item = new Item(data);
        System.out.println(item.getMd5());

        XmlParser xmlParser = new XmlParser(false);
        xmlParser.startParse(Gelbooru.get().getCompleteRequest(1, "touhou", 0));
        List<HashMap<String, String>> list1 = xmlParser.getResult();
        HashMap<String, String> data1 = list1.get(0);
        Item item1 = new Item(data1);
        System.out.println(item1.getPreview_url());
    }
}
