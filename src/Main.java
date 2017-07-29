
import engine.HttpConnection;
import engine.parser.JsonParser;
import engine.parser.XmlParser;

import source.boor.Danbooru;
import source.boor.Safebooru;

import java.util.HashMap;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception{
        HttpConnection connection = new HttpConnection();
        String response = connection.getRequest(Danbooru.get().getPostByIdRequest(2794154));
        JsonParser parser = new JsonParser();
        parser.startParse(response);
        List<HashMap<String, String>> result = parser.getResult();
        System.out.println(result.size());
    }

}
