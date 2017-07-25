import engine.HttpConnection;
import engine.parser.JsonParser;
import source.Comment;
import source.Post;
import source.boor.Danbooru;
import test.source.PostTest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * TODO: create Comment.class and put comments to default Item constructor
 * TODO: for each boor create comment API access
 */
public class Main {

    public static void main(String[] args) throws Exception{
        Post post = Danbooru.get().newPostInstance(PostTest.getDataFromBoorAdvanced(Danbooru.get(), 2794154));

        HttpConnection connection = new HttpConnection(false);

        JsonParser parser = new JsonParser();
        parser.startParse(connection.getRequest(post.getComments_url()));

        ArrayList<HashMap<String, String>> list = (ArrayList<HashMap<String, String>>) parser.getResult();

        ArrayList<Comment> comments = new ArrayList<>(list.size());
        comments.addAll(list.stream().map(Comment::new).collect(Collectors.toList()));

        for (Comment comment : comments){
            System.out.println(comment.getCreator_name());
        }
    }

}
