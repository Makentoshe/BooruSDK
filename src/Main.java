import engine.HttpConnection;
import engine.parser.JsonParser;
import engine.parser.XmlParser;
import source.Comment;
import source.Post;
import source.boor.Danbooru;
import source.boor.Gelbooru;
import source.еnum.Format;
import test.source.PostTest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * TODO: for each boor create comment API access
 */
public class Main {

    public static void main(String[] args) throws Exception{
        String request = Danbooru.get().getPackByTagsRequest(1, "hatsune_miku",0, Format.XML);
        XmlParser parser = new XmlParser();
        parser.startParse(request);
        Post post = new Post((HashMap<String, String>) parser.getResult().get(0), Danbooru.get());
        System.out.println(post.getPreview_url());
    }

}
