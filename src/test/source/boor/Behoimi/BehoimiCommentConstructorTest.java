package test.source.boor.Behoimi;

import engine.connector.HttpConnection;
import engine.parser.JsonParser;
import org.junit.Test;
import source.Comment;
import source.Post;
import source.boor.Behoimi;
import test.source.TestHelper;

import static org.junit.Assert.assertEquals;

//Create Danbooru post with default Post constructor.
public class BehoimiCommentConstructorTest {

    private static Comment comment;

    public BehoimiCommentConstructorTest() throws Exception{
        if (comment != null)  return;
        Post post = new Post(TestHelper.getDataFromBoorAdvanced(Behoimi.get(), 608453), Behoimi.get());
        HttpConnection connection = new HttpConnection(false);
        String responseData = connection.getRequest(post.getComments_url());
        JsonParser parser = new JsonParser();
        parser.startParse(responseData);
        comment = new Comment(parser.getResult().get(0));
    }

    @Test
    public void getId_Test() throws Exception{
        assertEquals(1767, comment.getId());
    }

    @Test
    public void getCreatorId_Test() throws Exception{
        assertEquals(2403, comment.getCreator_id());
    }

    @Test
    public void getPostId_Test() throws Exception{
        assertEquals(608453, comment.getPost_id());
    }

    @Test
    public void getBody_Test() throws Exception{
        assertEquals("And where I can buy them? Google didn't helped me on this matter.", comment.getBody());
    }

    @Test
    public void getCreatedAt_Test() throws Exception{
        assertEquals("2016-01-07 10:36", comment.getCreated_at());
    }

    @Test
    public void getCreatorName_Test() throws Exception{
        assertEquals("Ale", comment.getCreator_name());
    }

}
