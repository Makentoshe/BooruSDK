package test.source.boor.Gelbooru;


import engine.parser.XmlParser;
import org.junit.Test;
import source.Comment;
import source.Post;
import source.boor.Gelbooru;
import test.source.TestHelper;

import static org.junit.Assert.assertEquals;

public class GelbooruCommentConstructorTest {

    private static Comment comment;

    public GelbooruCommentConstructorTest() throws Exception{
        if (comment != null)  return;
        Post post = new Post(TestHelper.getPostFromBoor(Gelbooru.get(), 3535254), Gelbooru.get());
        System.out.println(post.getComments_url());
        XmlParser parser = new XmlParser();
        parser.startParse(post.getComments_url());
        comment = new Comment(parser.getResult().get(0));
    }

    @Test
    public void getId_Test() throws Exception{
        assertEquals(2088861, comment.getId());
    }

    @Test
    public void getCreatorId_Test() throws Exception{
        assertEquals(15516, comment.getCreator_id());
    }

    @Test
    public void getPostId_Test() throws Exception{
        assertEquals(3535254, comment.getPost_id());
    }

    @Test
    public void getBody_Test() throws Exception{
        assertEquals("Her milk must taste extra sweet.", comment.getBody());
    }

    @Test
    public void getCreatedAt_Test() throws Exception{
        assertEquals("2017-01-29 13:03", comment.getCreated_at());
    }

    @Test
    public void getCreatorName_Test() throws Exception{
        assertEquals("runner50", comment.getCreator_name());
    }
}
