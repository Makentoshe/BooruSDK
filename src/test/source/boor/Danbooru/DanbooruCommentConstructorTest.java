package test.source.boor.Danbooru;

import engine.HttpConnection;
import engine.parser.JsonParser;
import org.junit.Before;
import org.junit.Test;
import source.Comment;
import source.Post;
import source.boor.Danbooru;
import source.еnum.Boor;
import source.еnum.Rating;
import test.source.PostTest;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//Create Danbooru post with default Post constructor.
public class DanbooruCommentConstructorTest {

    private static Comment comment;

    public DanbooruCommentConstructorTest() throws Exception{
        if (comment != null)  return;
        Post post = new Post(PostTest.getDataFromBoorAdvanced(Danbooru.get(), 2794154), Danbooru.get());
        HttpConnection connection = new HttpConnection(false);
        String responseData = connection.getRequest(post.getComments_url());
        JsonParser parser = new JsonParser();
        parser.startParse(responseData);
        comment = new Comment(parser.getResult().get(0));
    }

    @Test
    public void getId_Test() throws Exception{
        assertEquals(1724084, comment.getId());
    }

    @Test
    public void getCreatorId_Test() throws Exception{
        assertEquals(489400, comment.getCreator_id());
    }

    @Test
    public void getPostId_Test() throws Exception{
        assertEquals(2794154, comment.getPost_id());
    }

    @Test
    public void getBody_Test() throws Exception{
        assertEquals("[quote]\\r\\nrom_collector said:\\r\\n\\r\\nMy guess is it's not \\Pandora\\ but \\Pazudora\\, which is how Japan refers to \\Puzzle & Dragons\\.\\r\\n[/quote]\\r\\n\\r\\nRight, that's definitely the best guess so far. Thanks!", comment.getBody());
    }

    @Test
    public void getCreatedAt_Test() throws Exception{
        assertEquals("2017-07-24T14:04:37.119-04:00", comment.getCreated_at());
    }

    @Test
    public void getCreatorName_Test() throws Exception{
        assertEquals("Levander", comment.getCreator_name());
    }

}
