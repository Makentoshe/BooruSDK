package test.engine.parser;

import engine.connector.HttpsConnection;
import engine.connector.Method;
import engine.parser.JsonParser;
import org.junit.Test;
import source.boor.Konachan;
import source.boor.Yandere;

import java.util.*;

import static org.junit.Assert.*;


public class JsonParserTest {

    @Test
    public void parse_Test_1(){
        String responseData = "[{\"id\":2788904,\"created_at\":\"2017-07-16T04:22:26.022-04:00\",\"uploader_id\":449845,\"score\":1,\"source\":\"https://i.pximg.net/img-original/img/2014/04/04/17/57/56/42688605_p0.jpg\",\"md5\":\"c766afa442880e66e476a40fda2ee5de\",\"last_comment_bumped_at\":null,\"rating\":\"s\",\"image_width\":1705,\"image_height\":1395,\"tag_string\":\"1girl barefoot black_dress black_ribbon blue_eyes blue_hair breasts bubble cleavage collarbone dress fish floating_hair full_body hair_between_eyes hair_ribbon hatsune_miku highres long_hair longyu looking_at_viewer medium_breasts open_mouth ribbon shinkai_shoujo_(vocaloid) sleeveless sleeveless_dress solo twintails underwater very_long_hair vocaloid\",\"is_note_locked\":false,\"fav_count\":2,\"file_ext\":\"jpg\",\"last_noted_at\":null,\"is_rating_locked\":false,\"parent_id\":null,\"has_children\":false,\"approver_id\":62191,\"tag_count_general\":28,\"tag_count_artist\":1,\"tag_count_character\":1,\"tag_count_copyright\":2,\"file_size\":242807,\"is_status_locked\":false,\"fav_string\":\"fav:496357 fav:469777\",\"pool_string\":\"\",\"up_score\":1,\"down_score\":0,\"is_pending\":false,\"is_flagged\":false,\"is_deleted\":false,\"tag_count\":32,\"updated_at\":\"2017-07-16T04:32:06.043-04:00\",\"is_banned\":false,\"pixiv_id\":42688605,\"last_commented_at\":null,\"has_active_children\":false,\"bit_flags\":0,\"uploader_name\":\"Rignak\",\"has_large\":true,\"tag_string_artist\":\"longyu\",\"tag_string_character\":\"hatsune_miku\",\"tag_string_copyright\":\"shinkai_shoujo_(vocaloid) vocaloid\",\"tag_string_general\":\"1girl barefoot black_dress black_ribbon blue_eyes blue_hair breasts bubble cleavage collarbone dress fish floating_hair full_body hair_between_eyes hair_ribbon highres long_hair looking_at_viewer medium_breasts open_mouth ribbon sleeveless sleeveless_dress solo twintails underwater very_long_hair\",\"has_visible_children\":false,\"children_ids\":null,\"file_url\":\"/data/__hatsune_miku_shinkai_shoujo_vocaloid_and_vocaloid_drawn_by_longyu__c766afa442880e66e476a40fda2ee5de.jpg\",\"large_file_url\":\"/data/sample/__hatsune_miku_shinkai_shoujo_vocaloid_and_vocaloid_drawn_by_longyu__sample-c766afa442880e66e476a40fda2ee5de.jpg\",\"preview_file_url\":\"/data/preview/c766afa442880e66e476a40fda2ee5de.jpg\"}]\n";

        JsonParser parser = new JsonParser();

        parser.startParse(responseData);


        //get EntrySet for first element
        Set<Map.Entry<String, String>> entrySet = parser.getResult().get(0).entrySet();
        entrySet.stream()
                .filter(pair -> "id".equals(pair.getKey()))
                .forEach(pair -> assertEquals(Integer.toString(2788904), pair.getValue()));
    }

    @Test
    public void parse_Test_2(){
        String responseData = "[{\"status\":\"active\",\"creator_id\":1,\"preview_width\":99,\"source\":\"\",\"author\":\"nil!\",\"width\":665,\"score\":0,\"preview_height\":150,\"has_comments\":false,\"sample_width\":665,\"has_children\":false,\"sample_url\":\"http://behoimi.org/data/7c/b1/7cb1161617a3a9d9f56d7772cde0f090.jpg\",\"file_url\":\"http://behoimi.org/data/7c/b1/7cb1161617a3a9d9f56d7772cde0f090.jpg\",\"parent_id\":null,\"sample_height\":1000,\"md5\":\"7cb1161617a3a9d9f56d7772cde0f090\",\"tags\":\"apron aqua_hair bear_hat colorful_pop_star!! cosplay dress haruka hatsune_miku hood thighhighs twintails\",\"change\":1209924,\"has_notes\":false,\"rating\":\"s\",\"id\":633053,\"height\":1000,\"preview_url\":\"http://behoimi.org/data/preview/7c/b1/7cb1161617a3a9d9f56d7772cde0f090.jpg\",\"file_size\":627398,\"created_at\":{\"n\":52387000,\"s\":1475072859,\"json_class\":\"Time\"}}]\n";

        JsonParser parser = new JsonParser();
        parser.startParse(responseData);

        //get EntrySet for first element
        Set<Map.Entry<String, String>> entrySet = parser.getResult().get(0).entrySet();
        entrySet.stream()
                .filter(pair -> "created_at".equals(pair.getKey()))
                .forEach(pair -> assertEquals("{\"n\":52387000,\"s\":1475072859,\"json_class\":\"Time\"}", pair.getValue()));
    }

    //Need INTERNET Connection
    @Test
    public void parseJsonArray_Test() throws Exception{
        //create connection
        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.GET)
                .setUserAgent("BooruEngineLib(mkliverout@gmail.com)/1.0")
                .openConnection(Konachan.get().getPostsByTagsRequest(5, "hatsune_miku", 0));
        //get data from server
        String responseData = connection.getResponse();
        //create parser
        JsonParser parser = new JsonParser();
        //start parse data
        parser.startParse(responseData);
        //get result
        List<HashMap<String, String>> data = parser.getResult();
        assertEquals(5, data.size());//5 posts
    }

    //Need INTERNET Connection
    @Test
    public void parseJsonElement_Test() throws Exception{
        //create connection
        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.GET)
                .setUserAgent("BooruEngineLib(mkliverout@gmail.com)/1.0")
                .openConnection(Yandere.get().getPostByIdRequest(405591));
        //get data from server
        String responseData = connection.getResponse();
        //create parser
        JsonParser parser = new JsonParser();
        //start parse data
        parser.startParse(responseData);
        //get result
        List<HashMap<String, String>> data = parser.getResult();
        assertEquals(1, data.size());//single post
    }

}