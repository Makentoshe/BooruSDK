package test.source.boor;

import org.junit.Test;
import source.boor.Gelbooru;

import static org.junit.Assert.*;

public class AbstractBoorBasicTest {

    @Test
    public void getCustomRequest() throws Exception {
        String request = Gelbooru.get().getCustomRequest("/___request");
        String expected = "https://gelbooru.com/___request";
        assertEquals(expected, request);
    }

    @Test
    public void getIdRequest() throws Exception {
        String request = Gelbooru.get().getPostByIdRequest(2281488);
        String expected = "https://gelbooru.com/index.php?page=dapi&q=index&s=post&id=2281488";
        assertEquals(expected, request);
    }

    @Test
    public void getPackRequest() throws Exception {
        String request = Gelbooru.get().getPostsByTagsRequest(10, "touhou", 1);
        String expected = "https://gelbooru.com/index.php?page=dapi&q=index&s=post&limit=10&tags=touhou&pid=1";
        assertEquals(expected, request);
    }

}