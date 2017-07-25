package test.source;

import engine.HttpConnection;
import source.Post;
import source.еnum.Rating;
import engine.parser.JsonParser;
import engine.parser.XmlParser;
import org.junit.Test;
import source.boor.*;
import source.еnum.Boor;

import java.util.HashMap;

import static org.junit.Assert.*;

public class PostTest {

    public static HashMap<String, String> getDataFromBoorAdvanced(AbstractBoorAdvanced boor, int id) throws Exception {
        String request1 = boor.getPostByIdRequest(id);
        //System.out.println(request1);
        HttpConnection connection = new HttpConnection(false);
        String responseData1 = connection.getRequest(request1);

        JsonParser parser = new JsonParser();

        parser.startParse(responseData1);

        return parser.getResult().get(0);
    }

    public static HashMap<String, String> getDataFromBoorBasic(AbstractBoorBasic boor, int id) throws Exception {
        String request1 = boor.getPostByIdRequest(id);
        //System.out.println(request1);

        XmlParser parser = new XmlParser();

        parser.startParse(request1);
        return (HashMap<String, String>) parser.getResult().get(0);
    }

    @Test
    public void constructorGelbooru_Test() throws Exception {
        Post post = new Post(getDataFromBoorBasic(Gelbooru.get(), 3785972), Gelbooru.get());

        assertEquals(post.getSourceBoor(), Boor.Gelbooru.toString());
        assertEquals(3785972, post.getId());
        assertEquals("cf589d62afe26ede34c2f4fa802ff70c", post.getMd5());
        assertEquals(Rating.SAFE, post.getRating());
        assertEquals("https://i.pximg.net/img-original/img/2017/05/11/19/53/24/62849670_p0.jpg", post.getSource());
        assertEquals("https://assets.gelbooru.com/thumbnails/cf/58/thumbnail_cf589d62afe26ede34c2f4fa802ff70c.jpg", post.getPreview_url());
        assertTrue(post.getTags().contains("hatsune_miku"));
        assertEquals("https://gelbooru.com/samples/cf/58/sample_cf589d62afe26ede34c2f4fa802ff70c.jpg", post.getSample_url());
        assertEquals("https://gelbooru.com/images/cf/58/cf589d62afe26ede34c2f4fa802ff70c.jpg", post.getFile_url());
        assertEquals("Creator_id ", 6498, post.getCreator_id());
        assertFalse("Has comments", post.isHas_comments());
        assertEquals("Comment url", null, post.getComments_url());
        assertEquals("Create Time", "Mon Jul 17 09:30:20 -0500 2017", post.getCreate_time());

    }

    @Test
    public void constructorPostInSakugabooru_Test() throws Exception {
        Post post = Sakugabooru.get().newPostInstance(getDataFromBoorAdvanced(Sakugabooru.get(), 36635));
    }
}