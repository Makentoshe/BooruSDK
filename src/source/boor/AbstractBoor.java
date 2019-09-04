package source.boor;

import source.еnum.Api;
import source.еnum.Format;

import java.io.Serializable;

/**
 * The main class for all boor's realisation.
 * To implement boor, you need only to extend this class
 * and provide implementation for <tt>getCustomRequest</tt>,
 * <tt>getPostByIdRequest</tt>, <tt>getPostsByTagsRequest</tt> and
 * <tt>newPostInstance</tt>.
 */
public abstract class AbstractBoor implements Serializable {

    protected Format format;

    protected Api api;

    /**
     * Get the result format of the current boor.
     *
     * @return result format. It can be {@code Format.JSON} or {@code Format.XML} or {@code Format.UNDEFINED}.
     */
    public Format getFormat() {
        return this.format;
    }

    /**
     * Get the API of the current boor.
     *
     * @return API format. It can be {@code Api.Basic} or {@code Api.Advanced} or {@code Api.UNDEFINED}.
     */
    public Api getApi() {
        return this.api;
    }

    /**
     * Set the result format of the current boor.
     */
    public void setFormat(Format format) {
        this.format = format;
    }

    /**
     * Set the API of the current boor.
     */
    public void setApi(Api api) {
        this.api = api;
    }

    /**
     * Construct custom request.
     *
     * @param request request.
     * @return get complete request.
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
     * @param format result format (can be {@code Format.JSON} or {@code Format.XML}).
     * @return complete request for item with id.
     */
    public abstract String getPostByIdRequest(int id, Format format);

    /**
     * Create request for getting some posts by tags.
     *
     * @param limit  how many posts must be in page.
     * @param tags   the tags to search for.
     * @param page   page index(from zero).
     * @param format format result (can be {@code Format.JSON} or {@code Format.XML}).
     * @return constructed request to this server.
     */
    public abstract String getPostsByTagsRequest(int limit, String tags, int page, Format format);

    /**
     * Create request for getting some posts by tags.
     * The format is getting from the getFormat() method.
     *
     * @param limit how many items must be in page.
     * @param tags  the tags to search for.
     * @param page  page index(from zero).
     * @return constructed request to this server.
     */
    public final String getPostsByTagsRequest(int limit, String tags, int page) {
        return getPostsByTagsRequest(limit, tags, page, getFormat());
    }

    /**
     * Create request for getting comments by post id.
     *
     * @param post_id post, for which comment will be searching.
     * @param format  in what format must be result output.
     * @return constructed request to server.
     */
    public abstract String getCommentsByPostIdRequest(int post_id, Format format);

    /**
     * Create request for getting comments by post id.
     * The format is getting from the <tt>getFormat</tt> method.
     *
     * @param post_id post, for which comment will be searching.
     * @return constructed request to server.
     */
    public final String getCommentsByPostIdRequest(int post_id) {
        return getCommentsByPostIdRequest(post_id, getFormat());
    }

    /**
     * Create request for getting post data for user.
     *
     * @param post_id post, for which the link will be created.
     * @return the link to post with standard user interface.
     */
    public abstract String getPostLinkById(int post_id);

//    /**
//     * Create request for getting tags.
//     *
//     * @param count count of getting tags. Can be hard limited or disabled.
//     * @param page  page index from zero.
//     * @return the link to list with tags.
//     */
//    public abstract String getTagListRequest(int count, int page);
}
