import engine.HttpConnection;
import engine.item.Item;
import engine.parser.JsonParser;
import engine.parser.XmlParser;
import source.boor.*;


import java.util.*;

/**
 *
 */
public class Main {

    public static void main(String[] args) throws Exception{

        Item item = new Item(getDataFromGelbooru());
        System.out.println(item.getPreview_url());

    }

    private static HashMap<String, String> getDataFromDanbooru() throws Exception{
        String request1 = Danbooru.get().getPackByTagsRequest(1, "hatsune_miku", 0);
        HttpConnection connection = new HttpConnection(false);
        String responseData1 = connection.getRequest(request1);

        JsonParser parser = new JsonParser();

        return parser.startParse(responseData1).get(0);
    }

    private static HashMap<String, String> getDataFromYandere() throws Exception{
        String request1 = Yandere.get().getPackByTagsRequest(1, "hatsune_miku", 0);

        System.out.println(request1);

        HttpConnection connection = new HttpConnection(false);
        String responseData1 = connection.getRequest(request1);

        JsonParser parser = new JsonParser();

        return parser.startParse(responseData1).get(0);
    }

    private static HashMap<String, String> getDataFromSakugabooru() throws Exception{
        String request1 = Sakugabooru.get().getPackByTagsRequest(1, "fate/apocrypha", 0);

        System.out.println(request1);

        HttpConnection connection = new HttpConnection(false);
        String responseData1 = connection.getRequest(request1);

        JsonParser parser = new JsonParser();

        return parser.startParse(responseData1).get(0);
    }

    private static HashMap<String, String> getDataFromKonachan() throws Exception{
        String request1 = Konachan.get().getPackByTagsRequest(1, "hatsune_miku", 0);

        System.out.println(request1);

        HttpConnection connection = new HttpConnection(false);
        String responseData1 = connection.getRequest(request1);

        JsonParser parser = new JsonParser();

        return parser.startParse(responseData1).get(0);
    }

    private static HashMap<String, String> getDataFromE621() throws Exception{
        String request1 = E621.get().getPackByTagsRequest(1, "hatsune_miku", 0);

        System.out.println(request1);

        HttpConnection connection = new HttpConnection(false);
        String responseData1 = connection.getRequest(request1);

        JsonParser parser = new JsonParser();

        return parser.startParse(responseData1).get(0);
    }

    private static HashMap<String, String> getDataFromBehoimi() throws Exception{
        String request1 = Behoimi.get().getPackByTagsRequest(1, "hatsune_miku", 0);

        System.out.println(request1);

        HttpConnection connection = new HttpConnection(false);
        String responseData1 = connection.getRequest(request1);

        JsonParser parser = new JsonParser();

        return parser.startParse(responseData1).get(0);
    }

    private static HashMap<String, String> getDataFromGelbooru() throws Exception{
        String request1 = Gelbooru.get().getPackByTagsRequest(1, "hatsune_miku", 0);

        System.out.println(request1);
        XmlParser parser = new XmlParser();
        parser.startParse(request1);
        return (HashMap<String, String>)parser.getResult().get(0);
    }
}
