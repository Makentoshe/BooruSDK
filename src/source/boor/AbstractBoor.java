package source.boor;

import source.Post;
import source.еnum.Api;
import source.еnum.Format;

import java.util.HashMap;

/**
 * The main class for all boor's realisation.
 * To implement boor, you need only to extend this class
 * and provide implementation for <tt>getCustomRequest</tt>,
 * <tt>getPostByIdRequest</tt>, <tt>getPackByTagsRequest</tt> and
 * <tt>newPostInstance</tt>.
 * <p>
 * You also need to implement <tt>getFormat</tt> and <tt>getApi</tt>.
 * So, you need to create 2 fields - {@code Format} and {@code Api}.
 * They should be explicitly set <strong>and not be null</strong>.
 * But then you can change them by custom methods.
 * <tt>getFormat</tt> and <tt>getApi</tt> implements must return this fields.
 */
public abstract class AbstractBoor {

    /**
     * Get the result format of the current boor.
     *
     * @return result format. It can be {@code Format.JSON} or {@code Format.XML} or {@code Format.UNDEFINED}.
     */
    public abstract Format getFormat();

    /**
     * Get the API of the current boor.
     *
     * @return API format. It can be {@code Api.Basic} or {@code Api.Advanced} or {@code Api.UNDEFINED}.
     */
    public abstract Api getApi();

    /**
     * Construct custom request.
     *
     * @param request request.
     * @return complete get request
     */
    public abstract String getCustomRequest(String request);

    /**
     * Construct request by id.
     * The format is getting from the {@code getFormat()} method.
     *
     * @param id post id.
     * @return complete request for item with id.
     */
    public final String getPostByIdRequest(int id) {
        return getPostByIdRequest(id, getFormat());
    }

    /**
     * Construct request by id.
     *
     * @param id     post id.
     * @param format result format.
     * @return complete request for item with id.
     */
    public abstract String getPostByIdRequest(int id, Format format);

    /**
     * Create request for getting some posts by tags.
     *
     * @param limit  how many posts must be in page.
     * @param tags   the tags to search for.
     * @param page   page index(from zero).
     * @param format format result(Can be JSON or XML)
     * @return constructed request to this server.
     */
    public abstract String getPackByTagsRequest(int limit, String tags, int page, Format format);

    /**
     * Create request for getting some posts by tags.
     * The format is getting from the getFormat() method.
     *
     * @param limit how many items must be in page.
     * @param tags  the tags to search for.
     * @param page  page index(from zero).
     * @return constructed request to this server.
     */
    public final String getPackByTagsRequest(int limit, String tags, int page) {
        return getPackByTagsRequest(limit, tags, page, getFormat());
    }


    /**
     * Create request for getting comments by post id.
     *
     * @param post_id post, for which comment will be searching
     * @param format in what format must be result output.
     * @return constructed request to server.
     */
    public abstract String getCommentsByPostIdRequest(int post_id, Format format);


    /**
     * Create request for getting comments by post id.
     * The format is getting from the getFormat() method.
     *
     * @param post_id post, for which comment will be searching
     * @return constructed request to server.
     */
    public final String getCommentsByPostIdRequest(int post_id){
        return getCommentsByPostIdRequest(post_id, getFormat());
    }

    /**
     * Empty method for boor.
     * Here you can create Post and create remote "constructor".
     *
     * @param attributes list of all post attributes
     * @return Post entity with setted data.
     */
    public abstract Post newPostInstance(HashMap<String, String> attributes);
}
