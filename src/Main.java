import engine.HttpConnection;
import engine.parser.JsonParser;
import engine.parser.XmlParser;
import source.Comment;
import source.Post;
import source.boor.Behoimi;
import source.boor.Danbooru;
import source.boor.Gelbooru;
import source.boor.Konachan;
import source.еnum.Format;
import test.source.PostTest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO: for each boor create comment API access
 */
public class Main {

    public static void main(String[] args) throws Exception{
        Post post = new Post(PostTest.getDataFromBoorAdvanced(Konachan.get(), 246937), Konachan.get());
        HttpConnection connection = new HttpConnection(false);
        String responseData = connection.getRequest(post.getComments_url());
        System.out.println(responseData);
    }

}
